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

package java.util;

/**
 * <p>设计用于在处理之前容纳元素的集合。除了基本的Collection操作外，队列还提供其他插入，
 * 提取和检查操作。这些方法中的每一种都以两种形式存在：一种在操作失败时引发异常，
 * 另一种返回一个特殊值（根据操作而为null或false）。
 * 插入操作的后一种形式是专门为与容量受限的Queue实现一起使用而设计的；
 * 在大多数实现中，插入操作不会失败。
 *
 *  队列方法摘要
 *  操作          抛异常                     返回指定的值
 *  插入 {@link Queue#add add(e)}         {@link Queue#offer offer(e)}
 *  移除 {@link Queue#remove remove()}    {@link Queue#poll poll()}
 *  检查 {@link Queue#element element()}  {@link Queue#peek peek()}
 *
 * <p>队列通常但不一定以FIFO（先进先出）的方式对元素进行排序。
 * 例外情况包括优先级队列（根据提供的比较器对元素进行排序或元素的自然排序）和LIFO队列（或堆栈），
 * 对LIFO进行排序（后进先出）。无论使用哪种顺序，队列的开头都是该元素，可以通过调用remove（）或poll（）将其删除。
 * 在FIFO队列中，所有新元素都插入到队列的尾部。其他种类的队列可能使用不同的放置规则。每个Queue实现必须指定其排序属性。
 *
 * <p>{@link #offer offer} 方法在可能的情况下插入一个元素，否则返回false。
 * 这不同于{@linkjava.util.Collection#add Collection.add} 方,
 * 后者只能通过引发未经检查的异常来添加元素。 The
 * {@code offer} 方法设计用于在正常情况下（而不是在例外情况下）发生故障时，
 * 例如在固定容量（或“有界”）队列中使用。
 *
 * <p>{@link #remove()} 和 {@link #poll()} 方法删除并返回队列的头部。
 * 确切地说，从队列中删除了哪个元素是队列的排序策略的函数，每个实现可的实现方式不同。
 * The {@code remove()} 和{@code poll()} 方法的区别仅在于队列为空时它们的行为不同：
 * the {@code remove()} 方法引发异常,
 * 而 {@code poll()} 方法返回null。
 *
 * <p>{@link #element()} and {@link #peek()} 方法返回但不删除队列的头部。
 *
 * <p>The {@code Queue} 接口没有定义并发编程中常见的阻塞队列方法。
 *  这些方法等待元素出现或空间可用它们在{@link java.util.concurrent.BlockingQueue}
 *  接口中定义, 该接口扩展了Queue接口。
 *
 *
 * <p>{@code Queue} 队列实现通常不允许插入null元素，尽管某些实现
 * {@link LinkedList}, 不禁止插入null.
 * 即使在允许的实现中，也不应将null插入到 {@code Queue}中,
 * 因为{@code poll} 方法还将null用作特殊的返回值，以指示该队列不包含任何元素。
 *
 * <p>{@code Queue} 实现通常不定义方法equals和hashCode的基于元素的版本，
 * 而是从Object类继承基于身份的版本，因为对于具有相同元素但排序属性不同的队列，
 * 基于元素的相等性并不总是很好定义的。
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @see java.util.Collection
 * @see LinkedList
 * @see PriorityQueue
 * @see java.util.concurrent.LinkedBlockingQueue
 * @see java.util.concurrent.BlockingQueue
 * @see java.util.concurrent.ArrayBlockingQueue
 * @see java.util.concurrent.LinkedBlockingQueue
 * @see java.util.concurrent.PriorityBlockingQueue
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 */
public interface Queue<E> extends Collection<E> {
    /**
     * 如果可以立即将指定的元素插入此队列，而不会违反容量限制，则在成功时返回true，
     * 如果当前没有可用空间，则抛出{@code IllegalStateException}
     *
     * @param e the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     *         this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this queue
     */
    boolean add(E e);

    /**
     * 如果可以在不违反容量限制的情况下立即将指定的元素插入此队列。
     * 当使用容量受限的队列时，此方法通常比add（E）更可取，
     * 因为add（E）可能仅通过引发异常而无法插入元素。
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this queue, else
     *         {@code false}
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     *         this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this queue
     */
    boolean offer(E e);

    /**
     * 检索并删除此队列的头。此方法与poll的不同之处仅在于，如果此队列为空，它将引发异常。
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    E remove();

    /**
     * 检索并删除此队列的头部，如果此队列为空，则返回null。
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    E poll();

    /**
     * 检索但不删除此队列的头。此方法与peek的不同之处仅在于，如果此队列为空，它将引发异常。
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    E element();

    /**
     * Retrieves-检索
     *检索但不删除此队列的头部，如果此队列为空，则返回null。
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    E peek();
}
