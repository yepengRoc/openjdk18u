/*
 * Copyright (c) 1994, 2012, Oracle and/or its affiliates. All rights reserved.
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

package java.lang;

/**
 * 类对象是类层次结构的根。每个类都有Object作为超类。所有对象（包括数组）都实现此类的方法。
 *
 * @author  unascribed
 * @see     java.lang.Class
 * @since   JDK1.0
 */
public class Object {

    private static native void registerNatives();
    static {
        registerNatives();
    }

    /**
     * 返回此对象的运行时类。返回的Class对象是被表示的类的静态同步方法锁定的对象。
     *
     * <p><实际的结果类型是Class <？扩展| X |>其中| X |是擦除将调用getClass的表达式的静态类型。
     * 例如，此代码段中不需要强制转换：
     *
     * <p>
     * Number n = 0;
     * Class<? extends Number> c = n.getClass();
     * </p>
     *
     * @return The {@code Class} object that represents the runtime
     *         class of this object.
     * @jls 15.8.2 Class Literals
     */
    public final native Class<?> getClass();

    /**
     * 返回对象的哈希码值。支持此方法是为了使哈希表（例如{@link java.util.HashMap提供的哈希表）受益。
     * <p>
     * hashCode的一般约定为：
     * <ul>
     * <li>只要在Java应用程序执行期间在同一对象上多次调用它，hashCode方法就必须一致地返回相同的整数，
     * 前提是不修改该对象的equals比较中使用的信息。从一个应用程序的一次执行到同一应用程序的另一次执行，此整数不必保持一致。
     * <li>如果根据equals（Object）方法两个对象相等，则在两个对象中的每个对象上调用hashCode方法必须产生相同的整数结果。
     * <li>如果根据equals（java.lang.Object）方法，如果两个对象不相等，
     * 则不需要在两个对象中的每个对象上调用hashCode方法必须产生不同的整数结果。但是，
     * 程序员应该意识到，为不相等的对象生成不同的整数结果可能会提高哈希表的性能。
     * </ul>
     * <p>
     * 在合理可行的范围内，由Object类定义的hashCode方法确实为不同的对象返回不同的整数。
     * （通常通过将对象的内部地址转换为整数来实现，但是Java™编程语言不需要此实现技术。）
     *
     * @return  a hash code value for this object.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.lang.System#identityHashCode
     */
    public native int hashCode();

    /**
     * 指示其他某个对象是否“等于”该对象。
     * <p>
     * The equals方法对非null对象引用实现对等关系：
     * <ul>
     * <li>这是自反的：对于任何非空参考值x，x.equals（x）应该返回true。
     * <li>它是对称的：对于任何非空参考值x和y，当且仅当y.equals（x）返回true时，x.equals（y）才应返回true。
     * <li>它是可传递的：对于x，y和z的任何非空引用值，如果x.equals（y）返回true，
     * 而y.equals（z）返回true，则x.equals（z）应该返回true。
     * <li>这是一致的：对于任何非空引用值x和y，如果未修改对象的equals比较中使用的信息，
     * 则多次调用x.equals（y）始终返回true或始终返回false。
     * <li>对于任何非null参考值x，x.equals（null）应该返回false。
     * </ul>
     * <p>
     * 类Object的equals方法在对象上实现了最有区别的对等关系。
     * 也就是说，对于任何非空参考值x和y，当且仅当x和y引用相同的对象（x == y的值为true）时，此方法才返回true。
     * <p>
     * 请注意，通常有必要在每次重写此方法时都重写hashCode方法，以便维护hashCode方法的常规协定，
     * 该协定规定相等的对象必须具有相等的哈希码。
     *
     * @param   obj   the reference object with which to compare.
     * @return  {@code true} if this object is the same as the obj
     *          argument; {@code false} otherwise.
     * @see     #hashCode()
     * @see     java.util.HashMap
     */
    public boolean equals(Object obj) {
        return (this == obj);
    }

    /**
     * 请注意，通常有必要在每次重写此方法时都重写hashCode方法，以便维护hashCode方法的常规协定，
     * 该协定规定相等的对象必须具有相等的哈希码。
     * <pre>
     * x.clone() != x
     * 将为真，并且该表达式：
     * <pre>
     * x.clone().getClass() == x.getClass()
     * 是正确的，但这不是绝对要求。虽然通常情况是:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * 是真的，这不是绝对要求。
     * <p>
     * 按照约定，应该通过调用super.clone获得返回的对象。
     * 如果一个类及其所有超类（对象除外）都遵守此约定，则x.clone().getClass（）== x.getClass（）就是这种情况。
     * <p>
     * 按照惯例，此方法返回的对象应独立于该对象（将被克隆）。为了实现这种独立性，
     * 可能有必要在返回super.clone之前修改该对象的一个​​或多个字段。
     * 通常，这意味着复制构成要克隆对象的内部“深度结构”的任何可变对象，
     * 并用对副本的引用替换对这些对象的引用。如果一个类仅包含基本字段或对不可变对象的引用，
     * 则通常情况是无需修改super.clone返回的对象中的任何字段。
     * <p>
     * 类Object的方法clone执行特定的克隆操作。首先，如果此对象的类未实现Cloneable接口，
     * 则抛出CloneNotSupportedException。请注意，所有数组都被认为实现了Cloneable接口，
     * 并且数组类型T []的clone方法的返回类型为T []，其中T是任何引用或原始类型。否则，
     * 此方法将创建此对象的类的新实例，并使用该对象相应字段的内容完全初始化其所有字段，
     * 就像通过赋值一样；字段的内容本身不会被克隆。因此，此方法执行此对象的“浅复制”，而不是“深复制”操作。
     * <p>
     * Object类本身并不实现Cloneable接口，因此在对象为Object的对象上调用clone方法将导致在运行时引发异常。
     *
     * @return     a clone of this instance.
     * @throws  CloneNotSupportedException  if the object's class does not
     *               support the {@code Cloneable} interface. Subclasses
     *               that override the {@code clone} method can also
     *               throw this exception to indicate that an instance cannot
     *               be cloned.
     * @see java.lang.Cloneable
     */
    protected native Object clone() throws CloneNotSupportedException;

    /**
     * 返回对象的字符串表示形式。通常，toString方法返回一个“以文本形式表示”此对象的字符串。
     * 结果应该是简洁易懂的表示形式，便于人们阅读。建议所有子类都重写此方法。
     * <p>
     * Object类的toString方法返回一个字符串，该字符串包括该对象是其实例的类的名称，
     * 符号字符“ @”以及该对象的哈希码的无符号十六进制表示形式。换句话说，此方法返回的字符串等于：
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return  a string representation of the object.
     */
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    /**
     * 唤醒正在此对象的监视器上等待的单个线程。如果有任何线程在该对象上等待，则选择其中一个唤醒。
     * 该选择是任意的，并且可以根据实现情况进行选择。线程通过调用其中一个wait方法在对象的监视器上等待。
     * <p>
     * 在当前线程放弃该对象上的锁之前，唤醒的线程将无法继续。唤醒的线程将以通常的方式与任何其他可能正在主动竞争以与此对象进行同步的线程竞争。
     * 例如，被唤醒的线程在作为锁定该对象的下一个线程时没有任何可靠的特权或劣势。
     * <p>
     * 此方法只能由作为该对象的监视器的所有者的线程调用。线程通过以下三种方式之一成为对象监视器的所有者：
     * <ul>
     * <li>通过执行该对象的同步实例方法。
     * <li>通过执行在对象上同步的同步语句的主体。
     * <li>对于类类型的对象，通过执行该类的同步静态方法。
     * </ul>
     * <p>
     * 一次只能有一个线程拥有对象的监视器。
     *
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of this object's monitor.
     * @see        java.lang.Object#notifyAll()
     * @see        java.lang.Object#wait()
     */
    public final native void notify();

    /**
     * 唤醒正在此对象的监视器上等待的所有线程。线程通过调用其中一个wait方法在对象的监视器上等待。
     * <p>
     * 在当前线程放弃对该对象的锁定之前，唤醒的线程将无法继续。
     * 唤醒的线程将以通常的方式与可能正在竞争在此对象上进行同步的任何其他线程竞争。
     * 例如，被唤醒的线程在成为锁定该对象的下一个线程时没有任何可靠的特权或劣势。
     * <p>
     * 此方法只能由作为该对象的监视器的所有者的线程调用。有关线程可以成为监视器所有者的方式的描述，请参见notify方法。
     *
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of this object's monitor.
     * @see        java.lang.Object#notify()
     * @see        java.lang.Object#wait()
     */
    public final native void notifyAll();

    /**
     * 使当前线程等待，直到另一个线程为此对象调用notify（）方法或notifyAll（）方法，或者经过了指定的时间。
     * <p>
     * 当前线程必须拥有该对象的监视器
     * <p>
     * 此方法使当前线程（称为T）将自己置于该对象的等待集中，然后放弃对该对象的所有同步声明。
     * 出于线程调度目的，线程T被禁用，并且在发生以下四种情况之一之前处于休眠状态：
     * <ul>
     * <li>其他一些线程为此对象调用notify方法，并且线程T恰好被任意选择为要唤醒的线程。
     * <li>其他一些线程为此对象调用notifyAll方法。
     * <li>其他一些线程中断线程T。
     * <li>T指定的实时量或多或少已经过去。但是，如果超时为零，则不考虑实时，线程只是等待直到通知。
     * </ul>
     * 然后从该对象的等待集中删除线程T，并重新启用线程T进行线程调度。然后，
     * 它以通常的方式与其他线程竞争在对象上进行同步的权利。一旦它获得了对象的控制权，
     * 它对对象的所有同步声明都将恢复到原样-即，恢复到调用wait方法时的情况。然后，
     * 线程T从调用wait方法返回。因此，从等待方法返回时，对象和线程T的同步状态与调用等待方法时的状态完全相同。
     * <p>
     * 线程也可以唤醒，而不会被通知，中断或超时，即所谓的虚假唤醒。
     * 尽管在实践中这种情况很少发生，但应用程序必须通过测试应该导致线程唤醒的条件来防范它，
     * 并在条件不满足时继续等待。换句话说，等待应该总是像这样循环执行：
     * <pre>
     *     synchronized (obj) {
     *          while (<condition does not hold>)
     *              obj.wait(timeout);
     *          ... // Perform action appropriate to condition
     *      }
     * </pre>
     * （有关此主题的更多信息，请参见Doug Lea的“ Java并行编程（第二版）”（
     * Addison-Wesley，2000年）中的3.2.3节，或Joshua Bloch的“有效的Java编程语言指南”（Addison-卫斯理，2001）。
     *
     * <p>
     * 如果当前线程在等待之前或等待期间被任何线程中断 {@linkplain java.lang.Thread#interrupt() interrupted} ，
     * 则抛出InterruptedException。如上所述，直到该对象的锁定状态恢复之前，不会引发此异常。
     * <p>
     * 请注意，wait方法将当前线程放入此对象的等待集中，因此只会解锁该对象；当线程等待时，当前线程可以在其上同步的所有其他对象保持锁定。
     * <p>
     * 此方法只能由作为该对象的监视器的所有者的线程调用。有关线程可以成为监视器所有者的方式的描述，请参见notify方法。
     *
     * @param      timeout   the maximum time to wait in milliseconds.
     * @throws  IllegalArgumentException      if the value of timeout is
     *               negative.
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of the object's monitor.
     * @throws  InterruptedException if any thread interrupted the
     *             current thread before or while the current thread
     *             was waiting for a notification.  The <i>interrupted
     *             status</i> of the current thread is cleared when
     *             this exception is thrown.
     * @see        java.lang.Object#notify()
     * @see        java.lang.Object#notifyAll()
     */
    public final native void wait(long timeout) throws InterruptedException;

    /**
     * Causes the current thread to wait until another thread invokes the
     * {@link java.lang.Object#notify()} method or the
     * {@link java.lang.Object#notifyAll()} method for this object, or
     * some other thread interrupts the current thread, or a certain
     * amount of real time has elapsed.
     * <p>
     * This method is similar to the {@code wait} method of one
     * argument, but it allows finer control over the amount of time to
     * wait for a notification before giving up. The amount of real time,
     * measured in nanoseconds, is given by:
     * <blockquote>
     * <pre>
     * 1000000*timeout+nanos</pre></blockquote>
     * <p>
     * In all other respects, this method does the same thing as the
     * method {@link #wait(long)} of one argument. In particular,
     * {@code wait(0, 0)} means the same thing as {@code wait(0)}.
     * <p>
     * The current thread must own this object's monitor. The thread
     * releases ownership of this monitor and waits until either of the
     * following two conditions has occurred:
     * <ul>
     * <li>Another thread notifies threads waiting on this object's monitor
     *     to wake up either through a call to the {@code notify} method
     *     or the {@code notifyAll} method.
     * <li>The timeout period, specified by {@code timeout}
     *     milliseconds plus {@code nanos} nanoseconds arguments, has
     *     elapsed.
     * </ul>
     * <p>
     * The thread then waits until it can re-obtain ownership of the
     * monitor and resumes execution.
     * <p>
     * As in the one argument version, interrupts and spurious wakeups are
     * possible, and this method should always be used in a loop:
     * <pre>
     *     synchronized (obj) {
     *         while (&lt;condition does not hold&gt;)
     *             obj.wait(timeout, nanos);
     *         ... // Perform action appropriate to condition
     *     }
     * </pre>
     * This method should only be called by a thread that is the owner
     * of this object's monitor. See the {@code notify} method for a
     * description of the ways in which a thread can become the owner of
     * a monitor.
     *
     * @param      timeout   the maximum time to wait in milliseconds.
     * @param      nanos      additional time, in nanoseconds range
     *                       0-999999.
     * @throws  IllegalArgumentException      if the value of timeout is
     *                      negative or the value of nanos is
     *                      not in the range 0-999999.
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of this object's monitor.
     * @throws  InterruptedException if any thread interrupted the
     *             current thread before or while the current thread
     *             was waiting for a notification.  The <i>interrupted
     *             status</i> of the current thread is cleared when
     *             this exception is thrown.
     */
    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }

    /**
     * Causes the current thread to wait until another thread invokes the
     * {@link java.lang.Object#notify()} method or the
     * {@link java.lang.Object#notifyAll()} method for this object.
     * In other words, this method behaves exactly as if it simply
     * performs the call {@code wait(0)}.
     * <p>
     * The current thread must own this object's monitor. The thread
     * releases ownership of this monitor and waits until another thread
     * notifies threads waiting on this object's monitor to wake up
     * either through a call to the {@code notify} method or the
     * {@code notifyAll} method. The thread then waits until it can
     * re-obtain ownership of the monitor and resumes execution.
     * <p>
     * As in the one argument version, interrupts and spurious wakeups are
     * possible, and this method should always be used in a loop:
     * <pre>
     *     synchronized (obj) {
     *         while (&lt;condition does not hold&gt;)
     *             obj.wait();
     *         ... // Perform action appropriate to condition
     *     }
     * </pre>
     * This method should only be called by a thread that is the owner
     * of this object's monitor. See the {@code notify} method for a
     * description of the ways in which a thread can become the owner of
     * a monitor.
     *
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of the object's monitor.
     * @throws  InterruptedException if any thread interrupted the
     *             current thread before or while the current thread
     *             was waiting for a notification.  The <i>interrupted
     *             status</i> of the current thread is cleared when
     *             this exception is thrown.
     * @see        java.lang.Object#notify()
     * @see        java.lang.Object#notifyAll()
     */
    public final void wait() throws InterruptedException {
        wait(0);
    }

    /**
     *当垃圾回收确定不再有对该对象的引用时，由垃圾回收器在对象上调用。
     * 子类覆盖finalize方法以处置系统资源或执行其他清除。
     * <p>
     * finalize的一般约定是，当Java™虚拟机确定不再有任何手段可以使尚未死亡的任何线程可以访问该对象时（除非由于执行操作而导致），
     * 调用finalize。由完成的其他一些对象或类的完成确定。finalize方法可以采取任何措施，包括使该对象可再次用于其他线程。
     * 但是，最终确定的通常目的是在清除对象之前将其清除。例如，代表输入/输出连接的对象的finalize方法可能会执行显式I / O事务，
     * 以在永久丢弃该对象之前中断连接。
     * <p>
     * Object类的finalize方法不执行任何特殊操作；它只是正常返回。Object的子类可以覆盖此定义。
     * <p>
     * Java编程语言不能保证哪个线程将为任何给定对象调用finalize方法。但是，
     * 可以保证，调用finalize的线程在调用finalize时不会持有任何用户可见的同步锁。
     * 如果finalize方法引发了未捕获的异常，则将忽略该异常，并终止该对象的终止。
     * <p>
     * 在为对象调用了finalize方法之后，直到Java虚拟机再次确定不再有任何方法可以由尚未死亡的任何线程访问该对象之后，
     * 才采取进一步的措施，包括可能的措施可以通过其他准备完成的对象或类来完成，此时可以丢弃该对象。
     * <p>
     * 对于任何给定的对象，Java虚拟机都不会多次调用finalize方法。-- finalize方法只会执行一次
     * <p>
     * 由finalize方法引发的任何异常都会导致该对象的终止终止，但否则将被忽略。
     *
     * @throws Throwable the {@code Exception} raised by this method
     * @see java.lang.ref.WeakReference
     * @see java.lang.ref.PhantomReference
     * @jls 12.6 Finalization of Class Instances
     */
    protected void finalize() throws Throwable { }
}
