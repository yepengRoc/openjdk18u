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

/**
 * <p>与使用同步方法和语句相比，{@code Lock}锁实现提供了更广泛的锁操作。
 * 它们允许更灵活的结构，可以具有完全不同的属性，并且可以支持多个关联的Condition{@link Condition} 对象。
 *
 * <p>锁是一种用于控制多个线程对共享资源的访问的工具。通常，锁提供对共享资源的独占访问：
 * 一次只能有一个线程可以获取该锁，而对共享资源的所有访问都需要首先获取该锁。
 * 但是，某些锁可能允许并发访问共享资源，例如ReadWriteLock{@link ReadWriteLock}的读锁。
 *
 * <p> 使用同步方法{@code synchronized}或语句可访问与每个对象关联的隐式监视器锁，
 * 但会强制所有锁的获取和释放以块结构的方式发生：当获取多个锁时，它们必须以相反的顺序释放，
 * 并且所有锁必须在获得它们的相同词汇范围内释放。
 *
 * <p>尽管用于同步方法和语句的作用域机制使使用监视器锁的编程变得更加容易，
 * 并且有助于避免许多常见的涉及锁的编程错误，但在某些情况下，
 * 您需要以更灵活的方式使用锁。例如，某些用于遍历并发访问的数据结构的算法需要使用“移交”或“链锁”：
 * 您获取节点A的锁，然后获取节点B的锁，然后释放A并获取C，然后释放B并获得D等。
 * Lock接口的实现通过允许在不同范围内获取和释放锁，并允许以任意顺序获取和释放多个锁，从而启用了此类技术。
 *
 * <p>灵活性的提高带来了额外的责任。缺少块结构锁定将消除同步方法和语句发生的自动锁定释放。
 * 在大多数情况下，应使用以下惯用法：
 *
 *  <pre> {@code
 * Lock l = ...;
 * l.lock();
 * try {
 *   // access the resource protected by this lock
 * } finally {
 *   l.unlock();
 * }}</pre>
 *
 * 当锁定和解锁发生在不同的范围内时，必须小心以确保通过try-finally或try-catch保护持有锁定时执行的所有代码，以确保在必要时释放锁定。
 *
 * <p>{@code Lock} 锁实现通过使用非阻塞尝试获取锁（{@link #tryLock()}），
 * 尝试获取可被中断的锁（{@link #lockInterruptibly}以及尝试获取锁），
 * 提供了比同步方法和语句更多的功能。可能超时的锁（{@link #tryLock(long, TimeUnit)}）。
 *
 * <p>Lock类还可以提供与隐式监视器锁定完全不同的行为和语义，例如保证顺序，
 * 不可重用或死锁检测。如果实现提供了这种专门的语义，则实现必须记录这些语义。
 *
 * <p> 请注意，Lock实例只是普通对象，它们本身可以用作同步语句中的目标。
 * 获取Lock实例的监视器锁定与调用该实例的任何{@code Lock}方法没有指定的关系。
 * 建议避免混淆，除非在自己的实现中使用，否则不要以这种方式使用{@code Lock}实例。
 *
 * <p>除非另有说明，否则为任何参数传递null值都会导致引发NullPointerException。
 *
 * <h3>Memory Synchronization</h3>内存同步
 *
 * <p>所有锁实现必须强制执行与内置监视器锁所提供的相同的内存同步语义，如Java语言规范（17.4内存模型）中所述：
 * <a href="https://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html#jls-17.4">
 * The Java Language Specification (17.4 Memory Model)</a>:
 * <ul>
 * <li>成功的锁定操作与成功的锁定操作具有相同的内存同步效果。
 * <li>成功的解锁操作与成功的解锁操作具有相同的内存同步效果。</li>
 * </ul>
 *
 * 不成功的锁定和解锁操作以及可重入的锁定/解锁操作不需要任何内存同步效果。
 *
 * <h3>Implementation Considerations</h3>实施注意事项
 *
 * <p>锁获取的三种形式（可中断，不可中断和定时）可能在性能特征，
 * 订购保证或其他实现质量上有所不同。此外，在给定的Lock类中，
 * 可能无法提供中断正在进行的锁定的功能。因此，不需要为所有三种形式的锁获取定义完全相同的保证或语义的实现，
 * 也不需要支持正在进行的锁获取的中断的实现。需要一个实现来清楚地记录每个锁定方法提供的语义和保证。
 * 在支持锁获取中断的范围内，它还必须服从该接口中定义的中断语义：全部或仅在方法输入时才这样做。
 *
 * <p>由于中断通常意味着取消，并且通常不经常进行中断检查，因此与正常方法返回相比，
 * 实现可能更喜欢响应中断。即使可以证明在另一个操作取消线程之后发生中断也是如此。实现应记录此行为。
 *
 * @see ReentrantLock
 * @see Condition
 * @see ReadWriteLock
 *
 * @since 1.5
 * @author Doug Lea
 */
public interface Lock {

    /**
     * <p>获取锁。
     * <p>如果该锁不可用，则出于线程调度目的，当前线程将被禁用，并处于休眠状态，直到获得该锁为止。
     * <p>实施注意事项
     * <p>锁实现可能能够检测到锁的错误使用，例如可能导致死锁的调用，并且在这种情况下可能引发（未经检查的）异常。
     * 该Lock实现必须记录情况和异常类型。
     */
    void lock();

    /**
     * <p>除非当前线程被中断{@linkplain Thread#interrupt interrupted}.，否则获取锁。
     * <p>获取锁（如果有）并立即返回。
     * <p>如果该锁不可用，则出于线程调度目的，当前线程将被禁用，
     * 并处于休眠状态，直到发生以下两种情况之一：
     *
     * <ul>
     * <li>该锁由当前线程获取；要么
     * <li> 其他一些线程中断当前线程，并且支持中断 {@linkplain Thread#interrupt interrupts} 获取锁
     * </ul>
     *
     * <p>如果当前线程:
     * <ul>
     * <li>在进入此方法时已设置其中断状态；
     * <li>要么获取锁时被中断，并且支持锁获取的中断{@linkplain Thread#interrupt interrupted} ，
     * 然后抛出InterruptedException并清除当前线程的中断状态。
     * ：
     * <p><b>Implementation Considerations</b>实施注意事项
     *
     * <p>在某些实现中，中断锁获取的能力可能是不可能的，并且如果可能的话可能是昂贵的操作。
     * 程序员应意识到可能是这种情况。在这种情况下，实现应记录在案。
     *
     * <p>与正常方法返回相比，实现可能更喜欢对中断做出响应。
     *
     * <p>锁实现可能能够检测到锁的错误使用，例如可能导致死锁的调用，
     * 并且在这种情况下可能引发（未经检查的）异常。该Lock实现必须记录情况和异常类型。
     *
     * @throws InterruptedException if the current thread is
     *         interrupted while acquiring the lock (and interruption
     *         of lock acquisition is supported)
     */
    void lockInterruptibly() throws InterruptedException;

    /**
     * <p>仅在调用时释放锁时才获取锁。
     * <p>获取锁（如果有），并立即返回true值。如果锁不可用，则此方法将立即返回false值。
     * <p>该方法的典型用法是：
     *  <pre> {@code
     * Lock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     *
     * 此用法可确保在获取锁后将其解锁，并且在未获取锁时不会尝试解锁。
     *
     * @return {@code true} if the lock was acquired and
     *         {@code false} otherwise
     */
    boolean tryLock();

    /**
     * Acquires the lock if it is free within the given waiting time and the
     * current thread has not been {@linkplain Thread#interrupt interrupted}.
     *
     * <p>If the lock is available this method returns immediately
     * with the value {@code true}.
     * If the lock is not available then
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until one of three things happens:
     * <ul>
     * <li>The lock is acquired by the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of lock acquisition is supported; or
     * <li>The specified waiting time elapses
     * </ul>
     *
     * <p>If the lock is acquired then the value {@code true} is returned.
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring
     * the lock, and interruption of lock acquisition is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     *
     * <p>If the specified waiting time elapses then the value {@code false}
     * is returned.
     * If the time is
     * less than or equal to zero, the method will not wait at all.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>The ability to interrupt a lock acquisition in some implementations
     * may not be possible, and if possible may
     * be an expensive operation.
     * The programmer should be aware that this may be the case. An
     * implementation should document when this is the case.
     *
     * <p>An implementation can favor responding to an interrupt over normal
     * method return, or reporting a timeout.
     *
     * <p>A {@code Lock} implementation may be able to detect
     * erroneous use of the lock, such as an invocation that would cause
     * deadlock, and may throw an (unchecked) exception in such circumstances.
     * The circumstances and the exception type must be documented by that
     * {@code Lock} implementation.
     *
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return {@code true} if the lock was acquired and {@code false}
     *         if the waiting time elapsed before the lock was acquired
     *
     * @throws InterruptedException if the current thread is interrupted
     *         while acquiring the lock (and interruption of lock
     *         acquisition is supported)
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * 释放锁。
     *
     * <p><b>Implementation Considerations</b>实施注意事项
     *
     * <p>锁实现通常会限制哪些线程可以释放锁（通常只有锁的持有者才能释放锁），
     * 并且如果违反该限制，则可能引发（未经检查）异常。任何限制和异常类型都必须由那个Lock实现记录下来。
     *
     */
    void unlock();

    /**
     * 返回绑定到此Lock{@code Lock} 实例的新Condition {@link Condition}实例。
     *
     * <p>在等待该条件之前，该锁必须由当前线程持有。对{@link Condition#await()}的调用将在等待之前自动释放该锁，并在等待返回之前重新获取该锁。
     * <p><b>Implementation Considerations</b>
     *
     * <p> Condition{@link Condition}实例的确切操作取决于Lock{@code Lock}实现，并且必须由该实现记录。
     *
     * @return A new {@link Condition} instance for this {@code Lock} instance
     * @throws UnsupportedOperationException if this {@code Lock}
     *         implementation does not support conditions
     */
    Condition newCondition();
}
