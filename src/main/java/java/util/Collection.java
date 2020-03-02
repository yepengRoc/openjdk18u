/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.util;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>集合层次结构中的根接口。集合代表一组对象，称为其元素。
 * 一些集合允许重复的元素，而另一些则不允许。
 * 一些是有序的，而其他则是无序的。JDK不提供此接口的任何直接实现：
 * 它提供更特定的子接口（如Set和List）的实现。该接口通常用于传递集合并在需要最大通用性的地方操作它们。
 *
 * <p>Bag或多集（可能包含重复元素的无序集合）应直接实现此接口。
 *
 * <p>所有通用的Collection实现类（通常通过其子接口之一间接实现Collection）都应提供两个“标准”构造函数：
 * 一个void（无参数）构造函数（用于创建一个空集合）和一个带有单个参数类型的构造函数集合，
 * 它将创建一个新集合，其元素与其参数相同。实际上，后一个构造函数允许用户复制任何集合，
 * 从而生成所需实现类型的等效集合。无法强制执行此约定（因为接口不能包含构造函数），
 * 但是Java平台库中的所有通用Collection实现都可以遵循。
 *
 * <p>如果此接口不支持该操作，则指定该接口中包含的“破坏性”方法（即，修改其操作的集合的方法）
 * 以引发UnsupportedOperationException。在这种情况下，如果调用对集合没有影响，
 * 则可以但不要求这些方法引发UnsupportedOperationException。例如，如果要添加的集合为空，
 * 则对一个不可修改的集合调用{@link #addAll(Collection)}方法可能（但并非必须）引发异常。
 *
 * <p><a name="optional-restrictions">
 * 一些集合实现对它们可能包含的元素有限制。例如，某些实现禁止使用null元素，而某些实现对其元素类型有限制。
 * 尝试添加不合格元素会引发未经检查的异常，通常为NullPointerException或ClassCastException。
 * 尝试查询不合格元素的存在可能会引发异常，或者可能仅返回false；否则，可能会抛出异常。
 * 一些实现将表现出前一种行为，而某些将表现出后者。更一般地，尝试对不合格元素执行操作，
 * 该操作的完成不会导致将不合格元素插入集合中，这可能会导致异常，也可能会成功，取决于具体实现。
 * 此类异常在此接口的规范中标记为“可选”。
 *
 * <p>
 * 由每个集合决定自己的同步策略。在实现中没有更强有力的保证的情况下，
 * 未定义的行为可能是由于调用另一个线程正在修改（变异的，修改）集合上的任何方法而导致的；
 * 这包括直接调用，将​​集合传递给可能执行调用的方法，以及使用现有的迭代器检查集合。
 *
 * <p>Collections Framework接口中的许多方法都是根据equals方法定义的。
 * 例如，contains（Object o）方法的规范说：
 * “当且仅当此集合包含至少一个元素e使得（o == null？e == null：o.equals（e））时，返回true。。
 * 此规范不应解释为暗示使用非空参数o调用Collection.contains会导致对任何元素e调用o.equals（e）。
 * 实现类可以自由实现优化，从而避免了等号调用，例如，首先比较两个元素的哈希码。
 * （Object.hashCode（）规范保证了具有不相等哈希码的两个对象不能相等。）更一般而言，
 * 各种Collections Framework接口的实现均可在实现者认为合适的地方自由利用基础Object方法的指定行为。
 *
 * <p>某些执行集合递归遍历的集合操作可能会失败，但自引用实例的例外情况是，集合直接或间接包含自身。
 * 这包括clone（），equals（），hashCode（）和toString（）方法。
 * 实现可以有选择地处理自引用场景，但是大多数当前实现不这样做。
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @implSpec
 * 默认方法实现（继承或以其他方式）不应用任何同步协议。如果Collection实现具有特定的同步协议，则它必须覆盖默认实现以应用该协议。
 *
 * @param <E> the type of elements in this collection
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Set
 * @see     List
 * @see     Map
 * @see     SortedSet
 * @see     SortedMap
 * @see     HashSet
 * @see     TreeSet
 * @see     ArrayList
 * @see     LinkedList
 * @see     Vector
 * @see     Collections
 * @see     Arrays
 * @see     AbstractCollection
 * @since 1.2
 */

public interface Collection<E> extends Iterable<E> {
    // Query Operations

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection
     */
    int size();

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    boolean isEmpty();

    /**
     * Returns <tt>true</tt> if this collection contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this collection
     * contains at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this collection is to be tested
     * @return <tt>true</tt> if this collection contains the specified
     *         element
     * @throws ClassCastException if the type of the specified element
     *         is incompatible with this collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     *         (<a href="#optional-restrictions">optional</a>)
     */
    boolean contains(Object o);

    /**
     * Returns an iterator over the elements in this collection.  There are no
     * guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     *
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
    Iterator<E> iterator();

    /**
     * Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection.  (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this collection
     */
    Object[] toArray();

    /**
     * Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified array.
     * If the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     *
     * <p>If this collection fits in the specified array with room to spare
     * (i.e., the array has more elements than this collection), the element
     * in the array immediately following the end of the collection is set to
     * <tt>null</tt>.  (This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does
     * not contain any <tt>null</tt> elements.)
     *
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     *
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     * <p>Suppose <tt>x</tt> is a collection known to contain only strings.
     * The following code can be used to dump the collection into a newly
     * allocated array of <tt>String</tt>:
     *
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     *
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     *
     * @param <T> the runtime type of the array to contain the collection
     * @param a the array into which the elements of this collection are to be
     *        stored, if it is big enough; otherwise, a new array of the same
     *        runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this collection
     * @throws NullPointerException if the specified array is null
     */
    <T> T[] toArray(T[] a);

    // Modification Operations

    /**
     * Ensures that this collection contains the specified element (optional
     * operation).  Returns <tt>true</tt> if this collection changed as a
     * result of the call.  (Returns <tt>false</tt> if this collection does
     * not permit duplicates and already contains the specified element.)<p>
     *
     * Collections that support this operation may place limitations on what
     * elements may be added to this collection.  In particular, some
     * collections will refuse to add <tt>null</tt> elements, and others will
     * impose restrictions on the type of elements that may be added.
     * Collection classes should clearly specify in their documentation any
     * restrictions on what elements may be added.<p>
     *
     * If a collection refuses to add a particular element for any reason
     * other than that it already contains the element, it <i>must</i> throw
     * an exception (rather than returning <tt>false</tt>).  This preserves
     * the invariant that a collection always contains the specified element
     * after this call returns.
     *
     * @param e element whose presence in this collection is to be ensured
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     * @throws UnsupportedOperationException if the <tt>add</tt> operation
     *         is not supported by this collection
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this collection
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     * @throws IllegalArgumentException if some property of the element
     *         prevents it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to insertion restrictions
     */
    boolean add(E e);

    /**
     * Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).  More formally,
     * removes an element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if
     * this collection contains one or more such elements.  Returns
     * <tt>true</tt> if this collection contained the specified element (or
     * equivalently, if this collection changed as a result of the call).
     *
     * @param o element to be removed from this collection, if present
     * @return <tt>true</tt> if an element was removed as a result of this call
     * @throws ClassCastException if the type of the specified element
     *         is incompatible with this collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *         is not supported by this collection
     */
    boolean remove(Object o);


    // Bulk Operations

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements
     * in the specified collection.
     *
     * @param  c collection to be checked for containment in this collection
     * @return <tt>true</tt> if this collection contains all of the elements
     *         in the specified collection
     * @throws ClassCastException if the types of one or more elements
     *         in the specified collection are incompatible with this
     *         collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *         or more null elements and this collection does not permit null
     *         elements
     *         (<a href="#optional-restrictions">optional</a>),
     *         or if the specified collection is null.
     * @see    #contains(Object)
     */
    boolean containsAll(Collection<?> c);

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation).  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in progress.
     * (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param c collection containing elements to be added to this collection
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *         is not supported by this collection
     * @throws ClassCastException if the class of an element of the specified
     *         collection prevents it from being added to this collection
     * @throws NullPointerException if the specified collection contains a
     *         null element and this collection does not permit null elements,
     *         or if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     *         specified collection prevents it from being added to this
     *         collection
     * @throws IllegalStateException if not all the elements can be added at
     *         this time due to insertion restrictions
     * @see #add(Object)
     */
    boolean addAll(Collection<? extends E> c);

    /**
     * Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation).  After this call returns,
     * this collection will contain no elements in common with the specified
     * collection.
     *
     * @param c collection containing elements to be removed from this collection
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     * @throws UnsupportedOperationException if the <tt>removeAll</tt> method
     *         is not supported by this collection
     * @throws ClassCastException if the types of one or more elements
     *         in this collection are incompatible with the specified
     *         collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if this collection contains one or more
     *         null elements and the specified collection does not support
     *         null elements
     *         (<a href="#optional-restrictions">optional</a>),
     *         or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean removeAll(Collection<?> c);

    /**
     * Removes all of the elements of this collection that satisfy the given
     * predicate.  Errors or runtime exceptions thrown during iteration or by
     * the predicate are relayed to the caller.
     *
     * @implSpec
     * The default implementation traverses all elements of the collection using
     * its {@link #iterator}.  Each matching element is removed using
     * {@link Iterator#remove()}.  If the collection's iterator does not
     * support removal then an {@code UnsupportedOperationException} will be
     * thrown on the first matching element.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     *        removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if elements cannot be removed
     *         from this collection.  Implementations may throw this exception if a
     *         matching element cannot be removed or if, in general, removal is not
     *         supported.
     * @since 1.8
     */
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).  In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.
     *
     * @param c collection containing elements to be retained in this collection
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation
     *         is not supported by this collection
     * @throws ClassCastException if the types of one or more elements
     *         in this collection are incompatible with the specified
     *         collection
     *         (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if this collection contains one or more
     *         null elements and the specified collection does not permit null
     *         elements
     *         (<a href="#optional-restrictions">optional</a>),
     *         or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean retainAll(Collection<?> c);

    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     *         is not supported by this collection
     */
    void clear();


    // Comparison and hashing

    /**
     * Compares the specified object with this collection for equality. <p>
     *
     * While the <tt>Collection</tt> interface adds no stipulations to the
     * general contract for the <tt>Object.equals</tt>, programmers who
     * implement the <tt>Collection</tt> interface "directly" (in other words,
     * create a class that is a <tt>Collection</tt> but is not a <tt>Set</tt>
     * or a <tt>List</tt>) must exercise care if they choose to override the
     * <tt>Object.equals</tt>.  It is not necessary to do so, and the simplest
     * course of action is to rely on <tt>Object</tt>'s implementation, but
     * the implementor may wish to implement a "value comparison" in place of
     * the default "reference comparison."  (The <tt>List</tt> and
     * <tt>Set</tt> interfaces mandate such value comparisons.)<p>
     *
     * The general contract for the <tt>Object.equals</tt> method states that
     * equals must be symmetric (in other words, <tt>a.equals(b)</tt> if and
     * only if <tt>b.equals(a)</tt>).  The contracts for <tt>List.equals</tt>
     * and <tt>Set.equals</tt> state that lists are only equal to other lists,
     * and sets to other sets.  Thus, a custom <tt>equals</tt> method for a
     * collection class that implements neither the <tt>List</tt> nor
     * <tt>Set</tt> interface must return <tt>false</tt> when this collection
     * is compared to any list or set.  (By the same logic, it is not possible
     * to write a class that correctly implements both the <tt>Set</tt> and
     * <tt>List</tt> interfaces.)
     *
     * @param o object to be compared for equality with this collection
     * @return <tt>true</tt> if the specified object is equal to this
     * collection
     *
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     * @see List#equals(Object)
     */
    boolean equals(Object o);

    /**
     * Returns the hash code value for this collection.  While the
     * <tt>Collection</tt> interface adds no stipulations to the general
     * contract for the <tt>Object.hashCode</tt> method, programmers should
     * take note that any class that overrides the <tt>Object.equals</tt>
     * method must also override the <tt>Object.hashCode</tt> method in order
     * to satisfy the general contract for the <tt>Object.hashCode</tt> method.
     * In particular, <tt>c1.equals(c2)</tt> implies that
     * <tt>c1.hashCode()==c2.hashCode()</tt>.
     *
     * @return the hash code value for this collection
     *
     * @see Object#hashCode()
     * @see Object#equals(Object)
     */
    int hashCode();

    /**
     * Creates a {@link Spliterator} over the elements in this collection.
     *
     * Implementations should document characteristic values reported by the
     * spliterator.  Such characteristic values are not required to be reported
     * if the spliterator reports {@link Spliterator#SIZED} and this collection
     * contains no elements.
     *
     * <p>The default implementation should be overridden by subclasses that
     * can return a more efficient spliterator.  In order to
     * preserve expected laziness behavior for the {@link #stream()} and
     * {@link #parallelStream()}} methods, spliterators should either have the
     * characteristic of {@code IMMUTABLE} or {@code CONCURRENT}, or be
     * <em><a href="Spliterator.html#binding">late-binding</a></em>.
     * If none of these is practical, the overriding class should describe the
     * spliterator's documented policy of binding and structural interference,
     * and should override the {@link #stream()} and {@link #parallelStream()}
     * methods to create streams using a {@code Supplier} of the spliterator,
     * as in:
     * <pre>{@code
     *     Stream<E> s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)
     * }</pre>
     * <p>These requirements ensure that streams produced by the
     * {@link #stream()} and {@link #parallelStream()} methods will reflect the
     * contents of the collection as of initiation of the terminal stream
     * operation.
     *
     * @implSpec
     * The default implementation creates a
     * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
     * from the collections's {@code Iterator}.  The spliterator inherits the
     * <em>fail-fast</em> properties of the collection's iterator.
     * <p>
     * The created {@code Spliterator} reports {@link Spliterator#SIZED}.
     *
     * @implNote
     * The created {@code Spliterator} additionally reports
     * {@link Spliterator#SUBSIZED}.
     *
     * <p>If a spliterator covers no elements then the reporting of additional
     * characteristic values, beyond that of {@code SIZED} and {@code SUBSIZED},
     * does not aid clients to control, specialize or simplify computation.
     * However, this does enable shared use of an immutable and empty
     * spliterator instance (see {@link Spliterators#emptySpliterator()}) for
     * empty collections, and enables clients to determine if such a spliterator
     * covers no elements.
     *
     * @return a {@code Spliterator} over the elements in this collection
     * @since 1.8
     */
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }

    /**
     * Returns a sequential {@code Stream} with this collection as its source.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @implSpec
     * The default implementation creates a sequential {@code Stream} from the
     * collection's {@code Spliterator}.
     *
     * @return a sequential {@code Stream} over the elements in this collection
     * @since 1.8
     */
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns a possibly parallel {@code Stream} with this collection as its
     * source.  It is allowable for this method to return a sequential stream.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @implSpec
     * The default implementation creates a parallel {@code Stream} from the
     * collection's {@code Spliterator}.
     *
     * @return a possibly parallel {@code Stream} over the elements in this
     * collection
     * @since 1.8
     */
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
