/*
 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.lang.ref;

import sun.misc.Cleaner;
import sun.misc.JavaLangRefAccess;
import sun.misc.SharedSecrets;

/**
 * Abstract base class for reference objects.  This class defines the
 * operations common to all reference objects.  Because reference objects are
 * implemented in close cooperation with the garbage collector, this class may
 * not be subclassed directly.
 * 参考对象的抽象基类。此类定义所有参考对象共有的操作。由于引用对象是与垃圾回收器紧密协作实现的，因此此类不能直接子类化。
 * @author   Mark Reinhold
 * @since    1.2
 */

public abstract class Reference<T> {

    /* A Reference instance is in one of four possible internal states:
     *    引用实例处于四种可能的内部状态之一：
     *     Active: Subject to special treatment by the garbage collector.  Some
     *     time after the collector detects that the reachability of the
     *     referent has changed to the appropriate state, it changes the
     *     instance's state to either Pending or Inactive, depending upon
     *     whether or not the instance was registered with a queue when it was
     *     created.  In the former case it also adds the instance to the
     *     pending-Reference list.  Newly-created instances are Active.
     *     活动：由垃圾收集器进行特殊处理。收集器检测到引用对象的可到达性已更改为适当的状态后，
     *    会根据实例在创建时是否向队列注册了实例，将实例的状态更改为“待处理”或“非活动”。在前一种情况下，
     *    它还将实例添加到“未决引用”列表中。新创建的实例处于活动状态。
     *
     *     Pending: An element of the pending-Reference list, waiting to be
     *     enqueued by the Reference-handler thread.  Unregistered instances
     *     are never in this state.
     *      待处理：待处理引用列表中的元素，等待被引用处理程序线程排队。未注册的实例永远不会处于此状态。
     *
     *     Enqueued: An element of the queue with which the instance was
     *     registered when it was created.  When an instance is removed from
     *     its ReferenceQueue, it is made Inactive.  Unregistered instances are
     *     never in this state.
     *     排队：创建实例时向其注册实例的队列元素。将实例从其ReferenceQueue中删除后，该实例将变为非活动状态。未注册的实例永远不会处于此状态。
     *
     *     Inactive: Nothing more to do.  Once an instance becomes Inactive its
     *     state will never change again.
     *      不活动：无事可做。一旦实例变为非活动状态，其状态将不再改变。
     *
     * The state is encoded in the queue and next fields as follows:
     *      状态在队列和下一个字段中编码如下：
     *     Active: queue = ReferenceQueue with which instance is registered, or
     *     ReferenceQueue.NULL if it was not registered with a queue; next =
     *     null.
     *
     *     Pending: queue = ReferenceQueue with which instance is registered;
     *     next = this
     *
     *     Enqueued: queue = ReferenceQueue.ENQUEUED; next = Following instance
     *     in queue, or this if at end of list.
     *
     *     Inactive: queue = ReferenceQueue.NULL; next = this.
     *
     * With this scheme the collector need only examine the next field in order
     * to determine whether a Reference instance requires special treatment: If
     * the next field is null then the instance is active; if it is non-null,
     * then the collector should treat the instance normally.
     * 使用此方案，收集器仅需要检查下一个字段即可确定Reference实例是否需要特殊处理：
     * 如果下一个字段为null，则该实例处于活动状态；否则，该实例处于活动状态。如果它为非null，则收集器应正常处理该实例。
     *
     * To ensure that a concurrent collector can discover active Reference
     * objects without interfering with application threads that may apply
     * the enqueue() method to those objects, collectors should link
     * discovered objects through the discovered field. The discovered
     * field is also used for linking Reference objects in the pending list.
     * 为了确保并发的收集器可以发现活动的Reference对象而不会干扰可能将enqueue（）方法应用于这些对象的应用程序线程，
     * 收集器应通过发现字段链接发现的对象。发现字段还用于链接未决列表中的引用对象。
     */

    private T referent;         /* Treated specially by GC */

    volatile ReferenceQueue<? super T> queue;

    /* When active:   NULL
     *     pending:   this
     *    Enqueued:   next reference in queue (or this if last)
     *    Inactive:   this
     */
    @SuppressWarnings("rawtypes")
    volatile Reference next;

    /* When active:   next element in a discovered reference list maintained by GC (or this if last)
     *     pending:   next element in the pending list (or null if last)
     *   otherwise:   NULL
     */
    transient private Reference<T> discovered;  /* used by VM */


    /* Object used to synchronize with the garbage collector.  The collector
     * must acquire this lock at the beginning of each collection cycle.  It is
     * therefore critical that any code holding this lock complete as quickly
     * as possible, allocate no new objects, and avoid calling user code.
     */
    static private class Lock { }
    private static Lock lock = new Lock();


    /* List of References waiting to be enqueued.  The collector adds
     * References to this list, while the Reference-handler thread removes
     * them.  This list is protected by the above lock object. The
     * list uses the discovered field to link its elements.
     */
    private static Reference<Object> pending = null;

    /* High-priority thread to enqueue pending References
     */
    private static class ReferenceHandler extends Thread {

        private static void ensureClassInitialized(Class<?> clazz) {
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw (Error) new NoClassDefFoundError(e.getMessage()).initCause(e);
            }
        }

        static {
            // pre-load and initialize InterruptedException and Cleaner classes
            // so that we don't get into trouble later in the run loop if there's
            // memory shortage while loading/initializing them lazily.
            ensureClassInitialized(InterruptedException.class);
            ensureClassInitialized(Cleaner.class);
        }

        ReferenceHandler(ThreadGroup g, String name) {
            super(g, name);
        }

        public void run() {
            while (true) {
                tryHandlePending(true);
            }
        }
    }

    /**
     * Try handle pending {@link Reference} if there is one.<p>
     * Return {@code true} as a hint that there might be another
     * {@link Reference} pending or {@code false} when there are no more pending
     * {@link Reference}s at the moment and the program can do some other
     * useful work instead of looping.
     *
     * @param waitForNotify if {@code true} and there was no pending
     *                      {@link Reference}, wait until notified from VM
     *                      or interrupted; if {@code false}, return immediately
     *                      when there is no pending {@link Reference}.
     * @return {@code true} if there was a {@link Reference} pending and it
     *         was processed, or we waited for notification and either got it
     *         or thread was interrupted before being notified;
     *         {@code false} otherwise.
     */
    static boolean tryHandlePending(boolean waitForNotify) {
        Reference<Object> r;
        Cleaner c;
        try {
            synchronized (lock) {
                if (pending != null) {
                    r = pending;
                    // 'instanceof' might throw OutOfMemoryError sometimes
                    // so do this before un-linking 'r' from the 'pending' chain...
                    c = r instanceof Cleaner ? (Cleaner) r : null;
                    // unlink 'r' from 'pending' chain
                    pending = r.discovered;
                    r.discovered = null;
                } else {
                    // The waiting on the lock may cause an OutOfMemoryError
                    // because it may try to allocate exception objects.
                    if (waitForNotify) {
                        lock.wait();
                    }
                    // retry if waited
                    return waitForNotify;
                }
            }
        } catch (OutOfMemoryError x) {
            // Give other threads CPU time so they hopefully drop some live references
            // and GC reclaims some space.
            // Also prevent CPU intensive spinning in case 'r instanceof Cleaner' above
            // persistently throws OOME for some time...
            Thread.yield();
            // retry
            return true;
        } catch (InterruptedException x) {
            // retry
            return true;
        }

        // Fast path for cleaners
        if (c != null) {
            c.clean();
            return true;
        }

        ReferenceQueue<? super Object> q = r.queue;
        if (q != ReferenceQueue.NULL) q.enqueue(r);
        return true;
    }

    static {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        for (ThreadGroup tgn = tg;
             tgn != null;
             tg = tgn, tgn = tg.getParent());
        Thread handler = new ReferenceHandler(tg, "Reference Handler");
        /* If there were a special system-only priority greater than
         * MAX_PRIORITY, it would be used here
         */
        handler.setPriority(Thread.MAX_PRIORITY);
        handler.setDaemon(true);
        handler.start();

        // provide access in SharedSecrets
        SharedSecrets.setJavaLangRefAccess(new JavaLangRefAccess() {
            @Override
            public boolean tryHandlePendingReference() {
                return tryHandlePending(false);
            }
        });
    }

    /* -- Referent accessor and setters -- */

    /**
     * Returns this reference object's referent.  If this reference object has
     * been cleared, either by the program or by the garbage collector, then
     * this method returns <code>null</code>.
     *
     * @return   The object to which this reference refers, or
     *           <code>null</code> if this reference object has been cleared
     */
    public T get() {
        return this.referent;
    }

    /**
     * Clears this reference object.  Invoking this method will not cause this
     * object to be enqueued.
     *
     * <p> This method is invoked only by Java code; when the garbage collector
     * clears references it does so directly, without invoking this method.
     */
    public void clear() {
        this.referent = null;
    }


    /* -- Queue operations -- */

    /**
     * Tells whether or not this reference object has been enqueued, either by
     * the program or by the garbage collector.  If this reference object was
     * not registered with a queue when it was created, then this method will
     * always return <code>false</code>.
     *
     * @return   <code>true</code> if and only if this reference object has
     *           been enqueued
     */
    public boolean isEnqueued() {
        return (this.queue == ReferenceQueue.ENQUEUED);
    }

    /**
     * Adds this reference object to the queue with which it is registered,
     * if any.
     *
     * <p> This method is invoked only by Java code; when the garbage collector
     * enqueues references it does so directly, without invoking this method.
     *
     * @return   <code>true</code> if this reference object was successfully
     *           enqueued; <code>false</code> if it was already enqueued or if
     *           it was not registered with a queue when it was created
     */
    public boolean enqueue() {
        return this.queue.enqueue(this);
    }


    /* -- Constructors -- */

    Reference(T referent) {
        this(referent, null);
    }

    Reference(T referent, ReferenceQueue<? super T> queue) {
        this.referent = referent;
        this.queue = (queue == null) ? ReferenceQueue.NULL : queue;
    }

}
