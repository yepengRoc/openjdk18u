/*
 * Copyright (c) 1998, 2011, Oracle and/or its affiliates. All rights reserved.
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

/**
 * A {@link Map} that further provides a <em>total ordering</em> on its keys.
 * The map is ordered according to the {@linkplain Comparable natural
 * ordering} of its keys, or by a {@link Comparator} typically
 * provided at sorted map creation time.  This order is reflected when
 * iterating over the sorted map's collection views (returned by the
 * {@code entrySet}, {@code keySet} and {@code values} methods).
 * Several additional operations are provided to take advantage of the
 * ordering.  (This interface is the map analogue of {@link SortedSet}.)
 * 进一步提供其键的总排序的Map。根据其键的自然顺序或通常在排序的map创建时提供的Comparator来对map进行排序。
 * 遍历排序后的map的集合视图（由entrySet，keySet和values方法返回）时，将反映此顺序。提供了一些附加操作以利用订购的优势。
 * （此接口是SortedSet的map类似物。）
 *
 * <p>All keys inserted into a sorted map must implement the {@code Comparable}
 * interface (or be accepted by the specified comparator).  Furthermore, all
 * such keys must be <em>mutually comparable</em>: {@code k1.compareTo(k2)} (or
 * {@code comparator.compare(k1, k2)}) must not throw a
 * {@code ClassCastException} for any keys {@code k1} and {@code k2} in
 * the sorted map.  Attempts to violate this restriction will cause the
 * offending method or constructor invocation to throw a
 * {@code ClassCastException}.
 * 插入已排序映射中的所有键都必须实现Comparable接口（或被指定的比较器接受）。
 * 此外，所有此类键都必须相互可比较：k1.compareTo（k2）（或比较器.compare（k1，k2））
 * 不得为已排序映射中的任何键k1和k2引发ClassCastException。尝试违反此限制将导致有问题的方法或构造函数调用引发ClassCastException。
 *
 * <p>Note that the ordering maintained by a sorted map (whether or not an
 * explicit comparator is provided) must be <em>consistent with equals</em> if
 * the sorted map is to correctly implement the {@code Map} interface.  (See
 * the {@code Comparable} interface or {@code Comparator} interface for a
 * precise definition of <em>consistent with equals</em>.)  This is so because
 * the {@code Map} interface is defined in terms of the {@code equals}
 * operation, but a sorted map performs all key comparisons using its
 * {@code compareTo} (or {@code compare}) method, so two keys that are
 * deemed equal by this method are, from the standpoint of the sorted map,
 * equal.  The behavior of a tree map <em>is</em> well-defined even if its
 * ordering is inconsistent with equals; it just fails to obey the general
 * contract of the {@code Map} interface.
 * 请注意，如果排序后的映射要正确实现Map接口，则排序后的映射（无论是否提供显式比较器）所维护的顺序必须等于equals。（
 * 有关与equals一致的精确定义，请参见Comparable接口或Comparator接口。）之所以这样，是因为Map接口是根据equals操作定义的，
 * 但是排序后的map使用其compareTo（或compare）方法执行所有键比较。，因此从排序后的映射的角度来看，此方法认为相等的两个键是相等的。
 * 树形图的行为是明确定义的，即使其顺序与equals不一致也是如此；它只是不遵守Map接口的一般约定。
 *
 * <p>All general-purpose sorted map implementation classes should provide four
 * "standard" constructors. It is not possible to enforce this recommendation
 * though as required constructors cannot be specified by interfaces. The
 * expected "standard" constructors for all sorted map implementations are:
 * 所有通用排序的映射实现类都应提供四个“标准”构造函数。尽管由于接口无法指定所需的构造函数，所以无法实施此建议。所有排序的map实现的预期“标准”构造函数为：
 * <ol>
 *   <li>A void (no arguments) constructor, which creates an empty sorted map
 *   sorted according to the natural ordering of its keys.</li>
 *   <li>A constructor with a single argument of type {@code Comparator}, which
 *   creates an empty sorted map sorted according to the specified comparator.</li>
 *   <li>A constructor with a single argument of type {@code Map}, which creates
 *   a new map with the same key-value mappings as its argument, sorted
 *   according to the keys' natural ordering.</li>
 *   <li>A constructor with a single argument of type {@code SortedMap}, which
 *   creates a new sorted map with the same key-value mappings and the same
 *   ordering as the input sorted map.</li>
 * </ol>
 * 1一个void（无参数）构造函数，该构造函数创建一个空的排序映射，该映射根据其键的自然顺序进行排序。
 * 2一个具有Comparator类型的单个参数的构造函数，该构造函数创建一个空的已排序映射，该映射根据指定的比较器进行排序。
 * 3具有单个Map类型参数的构造函数，该构造函数将创建一个新映射，该映射具有与其参数相同的键-值映射关系，并根据键的自然顺序进行排序。
 * 4具有一个SortedMap类型的单个参数的构造函数，该构造函数将创建一个新的排序映射，并与输入的排序映射具有相同的键值映射和相同的顺序。
 *
 * <p><strong>Note</strong>: several methods return submaps with restricted key
 * ranges. Such ranges are <em>half-open</em>, that is, they include their low
 * endpoint but not their high endpoint (where applicable).  If you need a
 * <em>closed range</em> (which includes both endpoints), and the key type
 * allows for calculation of the successor of a given key, merely request
 * the subrange from {@code lowEndpoint} to
 * {@code successor(highEndpoint)}.  For example, suppose that {@code m}
 * is a map whose keys are strings.  The following idiom obtains a view
 * containing all of the key-value mappings in {@code m} whose keys are
 * between {@code low} and {@code high}, inclusive:<pre>
 *   SortedMap&lt;String, V&gt; sub = m.subMap(low, high+"\0");</pre>
 *   注意：几种方法会返回键范围受限的子图。这样的范围是半开的，也就是说，它们包括其低端点，
 *   但不包括其高端点（如果适用）。如果您需要一个封闭范围（包括两个端点），并且键类型允许计算给定键的后继者，
 *   则只需请求从lowEndpoint到后继者（highEndpoint）的子范围。例如，假设m是一个键为字符串的映射。以下成语获取一个视图，
 *   其中包含m中的所有键-值映射，其键在低端和高端之间（包括端值）：
 *   SortedMap<String, V> sub = m.subMap(low, high+"\0");
 *
 * A similar technique can be used to generate an <em>open range</em>
 * (which contains neither endpoint).  The following idiom obtains a
 * view containing all of the key-value mappings in {@code m} whose keys
 * are between {@code low} and {@code high}, exclusive:<pre>
 *   SortedMap&lt;String, V&gt; sub = m.subMap(low+"\0", high);</pre>
 *   可以使用类似的技术来生成一个开放范围（不包含任何端点）。以下成语获取一个视图，该视图包含m中的所有键-值映射，其键在低和高之间（不包括）：
 *   SortedMap<String, V> sub = m.subMap(low+"\0", high);
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author  Josh Bloch
 * @see Map
 * @see TreeMap
 * @see SortedSet
 * @see Comparator
 * @see Comparable
 * @see Collection
 * @see ClassCastException
 * @since 1.2
 */

public interface SortedMap<K,V> extends Map<K,V> {
    /**
     * Returns the comparator used to order the keys in this map, or
     * {@code null} if this map uses the {@linkplain Comparable
     * natural ordering} of its keys.
     *
     * @return the comparator used to order the keys in this map,
     *         or {@code null} if this map uses the natural ordering
     *         of its keys
     */
    Comparator<? super K> comparator();

    /**
     * Returns a view of the portion of this map whose keys range from
     * {@code fromKey}, inclusive, to {@code toKey}, exclusive.  (If
     * {@code fromKey} and {@code toKey} are equal, the returned map
     * is empty.)  The returned map is backed by this map, so changes
     * in the returned map are reflected in this map, and vice-versa.
     * The returned map supports all optional map operations that this
     * map supports.
     *
     * <p>The returned map will throw an {@code IllegalArgumentException}
     * on an attempt to insert a key outside its range.
     *
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @param toKey high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys range from
     *         {@code fromKey}, inclusive, to {@code toKey}, exclusive
     * @throws ClassCastException if {@code fromKey} and {@code toKey}
     *         cannot be compared to one another using this map's comparator
     *         (or, if the map has no comparator, using natural ordering).
     *         Implementations may, but are not required to, throw this
     *         exception if {@code fromKey} or {@code toKey}
     *         cannot be compared to keys currently in the map.
     * @throws NullPointerException if {@code fromKey} or {@code toKey}
     *         is null and this map does not permit null keys
     * @throws IllegalArgumentException if {@code fromKey} is greater than
     *         {@code toKey}; or if this map itself has a restricted
     *         range, and {@code fromKey} or {@code toKey} lies
     *         outside the bounds of the range
     */
    SortedMap<K,V> subMap(K fromKey, K toKey);

    /**
     * Returns a view of the portion of this map whose keys are
     * strictly less than {@code toKey}.  The returned map is backed
     * by this map, so changes in the returned map are reflected in
     * this map, and vice-versa.  The returned map supports all
     * optional map operations that this map supports.
     *
     * <p>The returned map will throw an {@code IllegalArgumentException}
     * on an attempt to insert a key outside its range.
     *
     * @param toKey high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are strictly
     *         less than {@code toKey}
     * @throws ClassCastException if {@code toKey} is not compatible
     *         with this map's comparator (or, if the map has no comparator,
     *         if {@code toKey} does not implement {@link Comparable}).
     *         Implementations may, but are not required to, throw this
     *         exception if {@code toKey} cannot be compared to keys
     *         currently in the map.
     * @throws NullPointerException if {@code toKey} is null and
     *         this map does not permit null keys
     * @throws IllegalArgumentException if this map itself has a
     *         restricted range, and {@code toKey} lies outside the
     *         bounds of the range
     */
    SortedMap<K,V> headMap(K toKey);

    /**
     * Returns a view of the portion of this map whose keys are
     * greater than or equal to {@code fromKey}.  The returned map is
     * backed by this map, so changes in the returned map are
     * reflected in this map, and vice-versa.  The returned map
     * supports all optional map operations that this map supports.
     *
     * <p>The returned map will throw an {@code IllegalArgumentException}
     * on an attempt to insert a key outside its range.
     *
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are greater
     *         than or equal to {@code fromKey}
     * @throws ClassCastException if {@code fromKey} is not compatible
     *         with this map's comparator (or, if the map has no comparator,
     *         if {@code fromKey} does not implement {@link Comparable}).
     *         Implementations may, but are not required to, throw this
     *         exception if {@code fromKey} cannot be compared to keys
     *         currently in the map.
     * @throws NullPointerException if {@code fromKey} is null and
     *         this map does not permit null keys
     * @throws IllegalArgumentException if this map itself has a
     *         restricted range, and {@code fromKey} lies outside the
     *         bounds of the range
     */
    SortedMap<K,V> tailMap(K fromKey);

    /**
     * Returns the first (lowest) key currently in this map.
     *
     * @return the first (lowest) key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    K firstKey();

    /**
     * Returns the last (highest) key currently in this map.
     *
     * @return the last (highest) key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    K lastKey();

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set's iterator returns the keys in ascending order.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * {@code Iterator.remove}, {@code Set.remove},
     * {@code removeAll}, {@code retainAll}, and {@code clear}
     * operations.  It does not support the {@code add} or {@code addAll}
     * operations.
     *
     * @return a set view of the keys contained in this map, sorted in
     *         ascending order
     */
    Set<K> keySet();

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection's iterator returns the values in ascending order
     * of the corresponding keys.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own {@code remove} operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Collection.remove}, {@code removeAll},
     * {@code retainAll} and {@code clear} operations.  It does not
     * support the {@code add} or {@code addAll} operations.
     *
     * @return a collection view of the values contained in this map,
     *         sorted in ascending key order
     */
    Collection<V> values();

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set's iterator returns the entries in ascending key order.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation, or through the
     * {@code setValue} operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Set.remove}, {@code removeAll}, {@code retainAll} and
     * {@code clear} operations.  It does not support the
     * {@code add} or {@code addAll} operations.
     *
     * @return a set view of the mappings contained in this map,
     *         sorted in ascending key order
     */
    Set<Map.Entry<K, V>> entrySet();
}
