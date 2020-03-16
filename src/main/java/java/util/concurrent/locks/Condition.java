/*
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

/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent.locks;
import java.util.concurrent.TimeUnit;
import java.util.Date;

/**
 * <p>{@code Condition}将对象{@code Object}监视方法（{@link Object#wait() wait},
 * {@link Object#notify notify}和@link Object#notifyAll notifyAll}）分解为不同的对象，
 * 从而通过与任意Lock实现结合使用，从而使每个对象具有多个等待集。如果{@link Lock}替换了同步方法和语句的使用，
 * 而{@code Condition}替换了Object监视器方法的使用。
 *
 * <p> Conditions（也称为条件队列或条件变量）为一个线程暂停执行（“等待”）直到另一线程通知某些状态条件现在可能为真提供了一种方法。
 * 由于对该共享状态信息的访问发生在不同的线程中，因此必须对其进行保护，因此某种形式的锁与该条件相关联。
 * 等待条件提供的关键属性是，它自动释放关联的锁并挂起当前线程，就像Object.wait一样。
 *
 * <p>{@code Condition}实例从本质上绑定到锁。要获取特定Lock实例的Condition实例，请使用其{@link Lock#newCondition newCondition()}方法。
 *
 * <p> 例如，假设我们有一个支持{@code put}和{@code take}方法的有限缓冲区。
 * 如果尝试在空缓冲区上进行取带，则线程将阻塞，直到有可用项为止。
 * 如果尝试在完整的缓冲区上进行放置，则线程将阻塞，直到有可用空间为止。
 * 我们希望继续等待放置线程，并在单独的等待集中获取线程，以便我们可以使用仅当缓冲区中的项目或空间可用时才通知单个线程的优化。
 * 这可以使用两个{@link Condition}实例来实现。
 * <pre>
 * class BoundedBuffer {
 *   <b>final Lock lock = new ReentrantLock();</b>
 *   final Condition notFull  = <b>lock.newCondition(); </b>
 *   final Condition notEmpty = <b>lock.newCondition(); </b>
 *
 *   final Object[] items = new Object[100];
 *   int putptr, takeptr, count;
 *
 *   public void put(Object x) throws InterruptedException {
 *     <b>lock.lock();
 *     try {</b>
 *       while (count == items.length)
 *         <b>notFull.await();</b>
 *       items[putptr] = x;
 *       if (++putptr == items.length) putptr = 0;
 *       ++count;
 *       <b>notEmpty.signal();</b>
 *     <b>} finally {
 *       lock.unlock();
 *     }</b>
 *   }
 *
 *   public Object take() throws InterruptedException {
 *     <b>lock.lock();
 *     try {</b>
 *       while (count == 0)
 *         <b>notEmpty.await();</b>
 *       Object x = items[takeptr];
 *       if (++takeptr == items.length) takeptr = 0;
 *       --count;
 *       <b>notFull.signal();</b>
 *       return x;
 *     <b>} finally {
 *       lock.unlock();
 *     }</b>
 *   }
 * }
 * </pre>
 *
 * （{@link java.util.concurrent.ArrayBlockingQueue} 类提供了此功能，因此没有理由实现此示例用法类。）
 *
 * <p>{@code Condition}  实现可以提供与对象{@code Object}监视方法不同的行为和语义，
 * 例如保证通知的顺序，或者在执行通知时不需要保持锁定。如果实现提供了这种专门的语义，则实现必须记录这些语义。
 *
 * <p> 请注意，{@code Condition}实例只是普通对象，它们本身可以用作同步语句中的目标，
 * 并且可以调用自己的监视器{@link Object#wait wait}和{@link Object#notify notification}方法。
 * 获取条件实例的{@code Condition}锁或使用​​其监视器方法与获取与该条件相
 * 关联的锁或使用其{@linkplain #await waiting}和 {@linkplain #signal signalling}
 * 方法没有特定的关系。建议避免混淆，除非可能在自己的实现中，
 * 否则不要以这种方式使用Condition实例。
 *
 * <p>除非另有说明，否则为任何参数传递null值都会导致引发NullPointerException。
 *
 * <h3>Implementation Considerations</h3>实施注意事项
 *
 * <p> 当等待{@code Condition}时，通常会允许“虚假唤醒”，作为对底层平台语义的让步。
 * 这对大多数应用程序几乎没有实际影响，因为应该始终在循环中等待条件，测试正在等待的状态谓词。
 * 一个实现可以自由地消除虚假唤醒的可能性，但是建议应用程序程序员始终假定它们会发生，因此总是在循环中等待。
 *
 * <p>T条件等待的三种形式（可中断，不可中断和定时）在它们在某些平台上的实现容易程度和性能特征上可能有所不同。
 * 特别是，可能很难提供这些功能并维护特定的语义，例如排序保证。此外，中断线程的实际挂起的能力可能并不总是在所有平台上都可行。
 *
 * <p>因此，不需要实现为所有三种等待形式定义完全相同的保证或语义，也不需要支持中断线程的实际挂起。
 *
 * <p>需要一个实现来清楚地记录每个等待方法提供的语义和保证，并且当实现确实支持中断线程挂起时，则它必须服从此接口中定义的中断语义。
 *
 * <p>由于中断通常意味着取消，并且通常不经常进行中断检查，因此与正常方法返回相比，
 * 实现可能更喜欢响应中断。即使可以证明中断发生在另一个可能解除线程阻塞的操作之后，也是如此。实现应记录此行为。
 *
 * @since 1.5
 * @author Doug Lea
 */
public interface Condition {

    /**
     * 使当前线程等待，直到发出信号或被中断为止{@linkplain Thread#interrupt interrupted}。
     *
     * <p>与此条件相关联的锁被原子释放，并且出于线程调度目的，当前线程被禁用，并且处于休眠状态，直到发生以下四种情况之一：
     * <ul>
     * <li>其他一些线程为此{@code Condition}调用{@link #signal}方法，并且当前线程恰好被选择为要唤醒的线程；要么
     * <li>其他一些线程为此{@code Condition}调用{@link #signalAll}方法。要么
     * <li>其他一些线程中断当前线程，并支持{@linkplain Thread#interrupt interrupts}中断线程挂起；要么
     * <li>发生“虚假唤醒”。
     * </ul>
     *
     * <p>在所有情况下，在此方法可以返回之前，当前线程必须重新获取与此条件关联的锁。当线程返回时，可以保证保持此锁。
     *
     * <p>If the current thread:
     * <ul>
     * <li>在进入此方法时已设置其中断状态；要么
     * <li> 等待期间中断{@linkplain Thread#interrupt interrupted} 并支持中断线程挂起，
     *
     * </ul>然后抛出InterruptedException并清除当前线程的中断状态。在第一种情况下，没有规定在释放锁之前是否进行了中断测试。
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>T当调用此方法时，假定当前线程持有与此{@code Condition} 关联的锁。由实现来确定是否是这种情况，
     * 如果不是，则如何确定。通常，将引发异常（例如IllegalMonitorStateException），并且实现必须记录该事实。
     *
     * <p>与响应信号的正常方法返回相比，实现可能更喜欢对中断做出响应。在那种情况下，
     * 实现必须确保将信号重定向到另一个等待线程（如果有）。
     *
     * @throws InterruptedException if the current thread is interrupted
     *         (and interruption of thread suspension is supported)
     */
    void await() throws InterruptedException;

    /**
     * 使当前线程等待，直到发出信号。
     *
     * <p>与此条件相关联的锁被原子释放，并且出于线程调度目的，当前线程被禁用，
     * 并且处于休眠状态，直到发生以下三种情况之一：
     * <ul>
     * <li>其他一些线程为此{@code Condition}调用{@link #signal}方法，并且当前线程恰好被选择为要唤醒的线程；要么
     * <li> 其他一些线程为此条件调用{@link #signalAll}方法。要么
     * <li>发生“虚假唤醒”。
     * </ul>
     *
     * <p>在所有情况下，在此方法可以返回之前，当前线程必须重新获取与此条件关联的锁。当线程返回时，可以保证保持此锁。
     *
     * <p>如果当前线程进入此方法时设置了中断状态，或者在等待时被中断{@linkplain Thread#interrupt interrupted}，
     * 它将继续等待直到发出信号。当它最终从该方法返回时，其中断状态仍将被设置。
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>当调用此方法时，假定当前线程持有与此{@code Condition}关联的锁。由实现来确定是否是这种情况，
     * 如果不是，则如何确定。通常，将引发异常（例如IllegalMonitorStateException），并且实现必须记录该事实。
     */
    void awaitUninterruptibly();

    /**
     * Causes the current thread to wait until it is signalled or interrupted,
     * or the specified waiting time elapses.
     *
     * <p>The lock associated with this condition is atomically
     * released and the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until <em>one</em> of five things happens:
     * <ul>
     * <li>Some other thread invokes the {@link #signal} method for this
     * {@code Condition} and the current thread happens to be chosen as the
     * thread to be awakened; or
     * <li>Some other thread invokes the {@link #signalAll} method for this
     * {@code Condition}; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of thread suspension is supported; or
     * <li>The specified waiting time elapses; or
     * <li>A &quot;<em>spurious wakeup</em>&quot; occurs.
     * </ul>
     *
     * <p>In all cases, before this method can return the current thread must
     * re-acquire the lock associated with this condition. When the
     * thread returns it is <em>guaranteed</em> to hold this lock.
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while waiting
     * and interruption of thread suspension is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared. It is not specified, in the first
     * case, whether or not the test for interruption occurs before the lock
     * is released.
     *
     * <p>The method returns an estimate of the number of nanoseconds
     * remaining to wait given the supplied {@code nanosTimeout}
     * value upon return, or a value less than or equal to zero if it
     * timed out. This value can be used to determine whether and how
     * long to re-wait in cases where the wait returns but an awaited
     * condition still does not hold. Typical uses of this method take
     * the following form:
     *
     *  <pre> {@code
     * boolean aMethod(long timeout, TimeUnit unit) {
     *   long nanos = unit.toNanos(timeout);
     *   lock.lock();
     *   try {
     *     while (!conditionBeingWaitedFor()) {
     *       if (nanos <= 0L)
     *         return false;
     *       nanos = theCondition.awaitNanos(nanos);
     *     }
     *     // ...
     *   } finally {
     *     lock.unlock();
     *   }
     * }}</pre>
     *
     * <p>Design note: This method requires a nanosecond argument so
     * as to avoid truncation errors in reporting remaining times.
     * Such precision loss would make it difficult for programmers to
     * ensure that total waiting times are not systematically shorter
     * than specified when re-waits occur.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>The current thread is assumed to hold the lock associated with this
     * {@code Condition} when this method is called.
     * It is up to the implementation to determine if this is
     * the case and if not, how to respond. Typically, an exception will be
     * thrown (such as {@link IllegalMonitorStateException}) and the
     * implementation must document that fact.
     *
     * <p>An implementation can favor responding to an interrupt over normal
     * method return in response to a signal, or over indicating the elapse
     * of the specified waiting time. In either case the implementation
     * must ensure that the signal is redirected to another waiting thread, if
     * there is one.
     *
     * @param nanosTimeout the maximum time to wait, in nanoseconds
     * @return an estimate of the {@code nanosTimeout} value minus
     *         the time spent waiting upon return from this method.
     *         A positive value may be used as the argument to a
     *         subsequent call to this method to finish waiting out
     *         the desired time.  A value less than or equal to zero
     *         indicates that no time remains.
     * @throws InterruptedException if the current thread is interrupted
     *         (and interruption of thread suspension is supported)
     */
    long awaitNanos(long nanosTimeout) throws InterruptedException;

    /**
     * Causes the current thread to wait until it is signalled or interrupted,
     * or the specified waiting time elapses. This method is behaviorally
     * equivalent to:
     *  <pre> {@code awaitNanos(unit.toNanos(time)) > 0}</pre>
     *
     * @param time the maximum time to wait
     * @param unit the time unit of the {@code time} argument
     * @return {@code false} if the waiting time detectably elapsed
     *         before return from the method, else {@code true}
     * @throws InterruptedException if the current thread is interrupted
     *         (and interruption of thread suspension is supported)
     */
    boolean await(long time, TimeUnit unit) throws InterruptedException;

    /**
     * Causes the current thread to wait until it is signalled or interrupted,
     * or the specified deadline elapses.
     *
     * <p>The lock associated with this condition is atomically
     * released and the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until <em>one</em> of five things happens:
     * <ul>
     * <li>Some other thread invokes the {@link #signal} method for this
     * {@code Condition} and the current thread happens to be chosen as the
     * thread to be awakened; or
     * <li>Some other thread invokes the {@link #signalAll} method for this
     * {@code Condition}; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of thread suspension is supported; or
     * <li>The specified deadline elapses; or
     * <li>A &quot;<em>spurious wakeup</em>&quot; occurs.
     * </ul>
     *
     * <p>In all cases, before this method can return the current thread must
     * re-acquire the lock associated with this condition. When the
     * thread returns it is <em>guaranteed</em> to hold this lock.
     *
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while waiting
     * and interruption of thread suspension is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared. It is not specified, in the first
     * case, whether or not the test for interruption occurs before the lock
     * is released.
     *
     *
     * <p>The return value indicates whether the deadline has elapsed,
     * which can be used as follows:
     *  <pre> {@code
     * boolean aMethod(Date deadline) {
     *   boolean stillWaiting = true;
     *   lock.lock();
     *   try {
     *     while (!conditionBeingWaitedFor()) {
     *       if (!stillWaiting)
     *         return false;
     *       stillWaiting = theCondition.awaitUntil(deadline);
     *     }
     *     // ...
     *   } finally {
     *     lock.unlock();
     *   }
     * }}</pre>
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>The current thread is assumed to hold the lock associated with this
     * {@code Condition} when this method is called.
     * It is up to the implementation to determine if this is
     * the case and if not, how to respond. Typically, an exception will be
     * thrown (such as {@link IllegalMonitorStateException}) and the
     * implementation must document that fact.
     *
     * <p>An implementation can favor responding to an interrupt over normal
     * method return in response to a signal, or over indicating the passing
     * of the specified deadline. In either case the implementation
     * must ensure that the signal is redirected to another waiting thread, if
     * there is one.
     *
     * @param deadline the absolute time to wait until
     * @return {@code false} if the deadline has elapsed upon return, else
     *         {@code true}
     * @throws InterruptedException if the current thread is interrupted
     *         (and interruption of thread suspension is supported)
     */
    boolean awaitUntil(Date deadline) throws InterruptedException;

    /**
     * Wakes up one waiting thread.
     *
     * <p>If any threads are waiting on this condition then one
     * is selected for waking up. That thread must then re-acquire the
     * lock before returning from {@code await}.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>An implementation may (and typically does) require that the
     * current thread hold the lock associated with this {@code
     * Condition} when this method is called. Implementations must
     * document this precondition and any actions taken if the lock is
     * not held. Typically, an exception such as {@link
     * IllegalMonitorStateException} will be thrown.
     */
    void signal();

    /**
     * Wakes up all waiting threads.
     *
     * <p>If any threads are waiting on this condition then they are
     * all woken up. Each thread must re-acquire the lock before it can
     * return from {@code await}.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>An implementation may (and typically does) require that the
     * current thread hold the lock associated with this {@code
     * Condition} when this method is called. Implementations must
     * document this precondition and any actions taken if the lock is
     * not held. Typically, an exception such as {@link
     * IllegalMonitorStateException} will be thrown.
     */
    void signalAll();
}
