
package java.nio.channels;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;


/**
 * A multiplexor of {@link SelectableChannel} objects.
 *{@link SelectableChannel}对象的多路复用器。
 * <p> A selector may be created by invoking the {@link #open open} method of
 * this class, which will use the system's default {@link
 * java.nio.channels.spi.SelectorProvider selector provider} to
 * create a new selector.  A selector may also be created by invoking the
 * {@link java.nio.channels.spi.SelectorProvider#openSelector openSelector}
 * method of a custom selector provider.  A selector remains open until it is
 * closed via its {@link #close close} method.
 *
 *<p>可以通过调用以下方法的{@link #open open}方法来创建选择器：此类，
 * 它将使用系统的默认{@linkjava.nio.channels.spi.SelectorProvider选择器提供程序}创建一个新的选择器。
 * 选择器也可以通过调用{@link java.nio.channels.spi.SelectorProvider＃openSelector openSelector}自定义选择器提供程序的方法。
 * 选择器保持打开状态直到通过其{@link #close close}方法关闭。
 * <a name="ks"></a>
 *
 * <p> A selectable channel's registration with a selector is represented by a
 * {@link SelectionKey} object.  A selector maintains three sets of selection
 * keys:
 * 可选通道在选择器中的注册由SelectionKey对象表示。选择器维护三组选择键：
 *
 * <ul>
 *
 *   <li><p> The <i>key set</i> contains the keys representing the current
 *   channel registrations of this selector.  This set is returned by the
 *   {@link #keys() keys} method. </p></li>
 *键集包含代表此选择器当前通道注册的键。该集合由keys方法返回。
 *   <li><p> The <i>selected-key set</i> is the set of keys such that each
 *   key's channel was detected to be ready for at least one of the operations
 *   identified in the key's interest set during a prior selection operation.
 *   This set is returned by the {@link #selectedKeys() selectedKeys} method.
 *   The selected-key set is always a subset of the key set. </p></li>
 *   所选键集合是键集合，使得在先前的选择操作期间，每个键的信道被检测为准备好在键的兴趣集中标识的至少一个操作。
 *   该集合由selectedKeys方法返回。
 *
 *   <li><p> The <i>cancelled-key</i> set is the set of keys that have been
 *   cancelled but whose channels have not yet been deregistered.  This set is
 *   not directly accessible.  The cancelled-key set is always a subset of the
 *   key set. </p></li>
 *  所选键集始终是键集的子集。取消键集是已取消但其通道尚未注销的键集。
 *  此集不能直接访问。取消键集始终是键集的子集。
 * </ul>
 *
 * <p> All three sets are empty in a newly-created selector.  在新创建的选择器中，
 *
 * <p> A key is added to a selector's key set as a side effect of registering a
 * channel via the channel's {@link SelectableChannel#register(Selector,int)
 * register} method.  Cancelled keys are removed from the key set during
 * selection operations.  The key set itself is not directly modifiable.
 * 所有三个集合均为空。一个键被添加到选择器的键集中，作为通过通道的注册方法注册通道的副作用。在选择操作期间，已取消的键将从键集中删除。
 *   密钥集本身不能直接修改。
 * <p> A key is added to its selector's cancelled-key set when it is cancelled,
 * whether by closing its channel or by invoking its {@link SelectionKey#cancel
 * cancel} method.  Cancelling a key will cause its channel to be deregistered
 * during the next selection operation, at which time the key will removed from
 * all of the selector's key sets.
 * 取消键时，无论是通过关闭其通道还是通过调用其cancel方法，都会将一个键添加到其选择器的cancelled键集中。
 * 取消某个键将导致其频道在下一次选择操作期间被注销，这时该键将从所有选择器的键集中删除。
 *
 * <a name="sks"></a><p> Keys are added to the selected-key set by selection
 * operations.  A key may be removed directly from the selected-key set by
 * invoking the set's {@link java.util.Set#remove(java.lang.Object) remove}
 * method or by invoking the {@link java.util.Iterator#remove() remove} method
 * of an {@link java.util.Iterator iterator} obtained from the
 * set.  Keys are never removed from the selected-key set in any other way;
 * they are not, in particular, removed as a side effect of selection
 * operations.  Keys may not be added directly to the selected-key set. </p>
 * 通过选择操作将键添加到所选键集。通
 * 过调用集合的remove方法或调用从集合中获得的迭代器的remove方法，可以直接从所选键集中删除密钥。决不能以任何其他方式将键从选定键集中删除。
 * 作为选择操作的副作用，尤其不要删除它们。键可能无法直接添加到所选键集中。
 *
 * <a name="selop"></a>
 * <h2>Selection</h2>选拔
 *
 * <p> During each selection operation, keys may be added to and removed from a
 * selector's selected-key set and may be removed from its key and
 * cancelled-key sets.  Selection is performed by the {@link #select()}, {@link
 * #select(long)}, and {@link #selectNow()} methods, and involves three steps:
 * </p>
 *  在每个选择操作期间，可以将键添加到选择器的选定键集中或从中删除，
 *  *  * 也可以将其从选择键和取消键集中删除。选择是通过select（），select（long）和selectNow（）方法执行的，
 *  包括三个步骤：
 *
 * <ol>
 *
 *   <li><p> Each key in the cancelled-key set is removed from each key set of
 *   which it is a member, and its channel is deregistered.  This step leaves
 *   the cancelled-key set empty. </p></li>
 *  已取消键集中的每个键都从其所属的每个键集中删除，并且其通道已注销。此步骤将取消键设置为空。
 *
 *   <li><p> The underlying operating system is queried for an update as to the
 *   readiness of each remaining channel to perform any of the operations
 *   identified by its key's interest set as of the moment that the selection
 *   operation began.  For a channel that is ready for at least one such
 *   operation, one of the following two actions is performed: </p>
 *   询问底层操作系统是否有更新，
 *  * 有关每个剩余通道的准备情况，以执行自选择操作开始时由其键的兴趣集标识的任何操作。对于准备进行至少一项此类操作的通道，
 *  * 将执行以下两个操作之一：
 *
 *   <ol>
 *
 *     <li><p> If the channel's key is not already in the selected-key set then
 *     it is added to that set and its ready-operation set is modified to
 *     identify exactly those operations for which the channel is now reported
 *     to be ready.  Any readiness information previously recorded in the ready
 *     set is discarded.  </p></li>
 *     如果通道的键尚未位于所选键集中，则将其添加到该键集中，并修改其就绪操作集以准确标识现在报告通道已准备就绪的那些操作。
 *  * 先前记录在就绪集中的任何准备信息都将被丢弃。
 *
 *     <li><p> Otherwise the channel's key is already in the selected-key set,
 *     so its ready-operation set is modified to identify any new operations
 *     for which the channel is reported to be ready.  Any readiness
 *     information previously recorded in the ready set is preserved; in other
 *     words, the ready set returned by the underlying system is
 *     bitwise-disjoined into the key's current ready set. </p></li>
 *否则，通道的键已经在所选键集中，因此修改其就绪操作集以识别报告通道已准备就绪的任何新操作。
 * 先前记录在就绪集中的任何准备信息都将保留；换句话说，底层系统返回的就绪集按位分离到密钥的当前就绪集中。
 *   </ol>
 *
 *   If all of the keys in the key set at the start of this step have empty
 *   interest sets then neither the selected-key set nor any of the keys'
 *   ready-operation sets will be updated.
 *   如果在此步骤开始时键集中的所有键都具有空兴趣集，
 *  * 则所选键集和任何键的就绪操作集都不会更新。
 *
 *   <li><p> If any keys were added to the cancelled-key set while step (2) was
 *   in progress then they are processed as in step (1). </p></li>
 *如果在执行步骤（2）时将任何键添加到取消键集中，则将按步骤（1）进行处理。
 * </ol>
 *
 * <p> Whether or not a selection operation blocks to wait for one or more
 * channels to become ready, and if so for how long, is the only essential
 * difference between the three selection methods. </p>
 * 选择操作是否阻塞等待一个或多个通道准备就绪，
 * 如果等待了多长时间，则是这三种选择方法之间的唯一本质区别。
 *
 * <h2>Concurrency</h2>
 *
 * <p> Selectors are themselves safe for use by multiple concurrent threads;
 * their key sets, however, are not.
 *选择器本身可以安全地供多个并发线程使用。 *但是，它们的密钥集不是。
 * <p> The selection operations synchronize on the selector itself, on the key
 * set, and on the selected-key set, in that order.  They also synchronize on
 * the cancelled-key set during steps (1) and (3) above.
 *
 * <p> Changes made to the interest sets of a selector's keys while a
 * selection operation is in progress have no effect upon that operation; they
 * will be seen by the next selection operation.
 *
 * <p> Keys may be cancelled and channels may be closed at any time.  Hence the
 * presence of a key in one or more of a selector's key sets does not imply
 * that the key is valid or that its channel is open.  Application code should
 * be careful to synchronize and check these conditions as necessary if there
 * is any possibility that another thread will cancel a key or close a channel.
 *
 * <p> A thread blocked in one of the {@link #select()} or {@link
 * #select(long)} methods may be interrupted by some other thread in one of
 * three ways:
 *
 * <ul>
 *
 *   <li><p> By invoking the selector's {@link #wakeup wakeup} method,
 *   </p></li>
 *
 *   <li><p> By invoking the selector's {@link #close close} method, or
 *   </p></li>
 *
 *   <li><p> By invoking the blocked thread's {@link
 *   java.lang.Thread#interrupt() interrupt} method, in which case its
 *   interrupt status will be set and the selector's {@link #wakeup wakeup}
 *   method will be invoked. </p></li>
 *
 * </ul>
 *
 * <p> The {@link #close close} method synchronizes on the selector and all
 * three key sets in the same order as in a selection operation.
 *  选择器本身可以安全地供多个并发线程使用。但是，它们的密钥集不是。
 * 选择操作按该顺序在选择器本身，键集和选定键集上同步。它们还会在上面的步骤（1）和（3）期间同步取消键集。
 * 在进行选择操作时，对选择器的键的兴趣集所做的更改对该操作没有影响；
 * 他们将在下一个选择操作中看到。可以随时取消键并关闭通道。
 * 因此，一个选择器的一个或多个键集中的键的存在并不表示该键有效或其通道已打开。
 * 如果其他线程有可能取消键或关闭通道，则应用程序代码应谨慎同步并在必要时检查这些条件。
 * 在select（）或select（long）方法之一中阻塞的线程可能会以其他三种方式之一被其他某个线程中断：
 * 通过调用选择器的唤醒方法，通过调用选择器的close方法，或者通过调用被阻塞线程的中断方法，在这种情况下，
 * 将设置其中断状态，并调用选择器的唤醒方法。close方法以与选择操作相同的顺序在选择器和所有三个键集上同步。
 * <a name="ksc"></a>
 *
 * <p> A selector's key and selected-key sets are not, in general, safe for use
 * by multiple concurrent threads.  If such a thread might modify one of these
 * sets directly then access should be controlled by synchronizing on the set
 * itself.  The iterators returned by these sets' {@link
 * java.util.Set#iterator() iterator} methods are <i>fail-fast:</i> If the set
 * is modified after the iterator is created, in any way except by invoking the
 * iterator's own {@link java.util.Iterator#remove() remove} method, then a
 * {@link java.util.ConcurrentModificationException} will be thrown. </p>
 * 通常，选择器的键集和选定的键集不能安全地供多个并发线程使用。如果此类线程可以直接修改这些集合之一，
 * 则应通过在集合本身上进行同步来控制访问。这些集合的迭代器方法返回的迭代器是快速失败的：如果在创建迭代器后修改集合，
 * 则可以通过调用迭代器自己的remove方法的任何方式来修改该集合，否则将抛出ConcurrentModificationException。
 *
 *
 * @author Mark Reinhold
 * @author JSR-51 Expert Group
 * @since 1.4
 *
 * @see SelectableChannel
 * @see SelectionKey
 */

public abstract class Selector implements Closeable {

    /**
     * Initializes a new instance of this class.
     */
    protected Selector() { }

    /**
     * Opens a selector.
     *
     * <p> The new selector is created by invoking the {@link
     * java.nio.channels.spi.SelectorProvider#openSelector openSelector} method
     * of the system-wide default {@link
     * java.nio.channels.spi.SelectorProvider} object.  </p>
     *
     * @return  A new selector
     *
     * @throws  IOException
     *          If an I/O error occurs
     */
    public static Selector open() throws IOException {
        return SelectorProvider.provider().openSelector();
    }

    /**
     * Tells whether or not this selector is open.
     *
     * @return <tt>true</tt> if, and only if, this selector is open
     */
    public abstract boolean isOpen();

    /**
     * Returns the provider that created this channel.
     *
     * @return  The provider that created this channel
     */
    public abstract SelectorProvider provider();

    /**
     * Returns this selector's key set.
     *
     * <p> The key set is not directly modifiable.  A key is removed only after
     * it has been cancelled and its channel has been deregistered.  Any
     * attempt to modify the key set will cause an {@link
     * UnsupportedOperationException} to be thrown.
     *
     * <p> The key set is <a href="#ksc">not thread-safe</a>. </p>
     *
     * @return  This selector's key set
     *
     * @throws  ClosedSelectorException
     *          If this selector is closed
     */
    public abstract Set<SelectionKey> keys();

    /**
     * Returns this selector's selected-key set.
     *
     * <p> Keys may be removed from, but not directly added to, the
     * selected-key set.  Any attempt to add an object to the key set will
     * cause an {@link UnsupportedOperationException} to be thrown.
     *
     * <p> The selected-key set is <a href="#ksc">not thread-safe</a>. </p>
     *
     * @return  This selector's selected-key set
     *
     * @throws  ClosedSelectorException
     *          If this selector is closed
     */
    public abstract Set<SelectionKey> selectedKeys();

    /**
     * Selects a set of keys whose corresponding channels are ready for I/O
     * operations.
     *
     * <p> This method performs a non-blocking <a href="#selop">selection
     * operation</a>.  If no channels have become selectable since the previous
     * selection operation then this method immediately returns zero.
     *
     * <p> Invoking this method clears the effect of any previous invocations
     * of the {@link #wakeup wakeup} method.  </p>
     *
     * @return  The number of keys, possibly zero, whose ready-operation sets
     *          were updated by the selection operation
     *
     * @throws  IOException
     *          If an I/O error occurs
     *
     * @throws  ClosedSelectorException
     *          If this selector is closed
     */
    public abstract int selectNow() throws IOException;

    /**
     * Selects a set of keys whose corresponding channels are ready for I/O
     * operations.
     *
     * <p> This method performs a blocking <a href="#selop">selection
     * operation</a>.  It returns only after at least one channel is selected,
     * this selector's {@link #wakeup wakeup} method is invoked, the current
     * thread is interrupted, or the given timeout period expires, whichever
     * comes first.
     *
     * <p> This method does not offer real-time guarantees: It schedules the
     * timeout as if by invoking the {@link Object#wait(long)} method. </p>
     *
     * @param  timeout  If positive, block for up to <tt>timeout</tt>
     *                  milliseconds, more or less, while waiting for a
     *                  channel to become ready; if zero, block indefinitely;
     *                  must not be negative
     *
     * @return  The number of keys, possibly zero,
     *          whose ready-operation sets were updated
     *
     * @throws  IOException
     *          If an I/O error occurs
     *
     * @throws  ClosedSelectorException
     *          If this selector is closed
     *
     * @throws  IllegalArgumentException
     *          If the value of the timeout argument is negative
     */
    public abstract int select(long timeout)
        throws IOException;

    /**
     * Selects a set of keys whose corresponding channels are ready for I/O
     * operations.
     *
     * <p> This method performs a blocking <a href="#selop">selection
     * operation</a>.  It returns only after at least one channel is selected,
     * this selector's {@link #wakeup wakeup} method is invoked, or the current
     * thread is interrupted, whichever comes first.  </p>
     *
     * @return  The number of keys, possibly zero,
     *          whose ready-operation sets were updated
     *
     * @throws  IOException
     *          If an I/O error occurs
     *
     * @throws  ClosedSelectorException
     *          If this selector is closed
     */
    public abstract int select() throws IOException;

    /**
     * Causes the first selection operation that has not yet returned to return
     * immediately.
     *
     * <p> If another thread is currently blocked in an invocation of the
     * {@link #select()} or {@link #select(long)} methods then that invocation
     * will return immediately.  If no selection operation is currently in
     * progress then the next invocation of one of these methods will return
     * immediately unless the {@link #selectNow()} method is invoked in the
     * meantime.  In any case the value returned by that invocation may be
     * non-zero.  Subsequent invocations of the {@link #select()} or {@link
     * #select(long)} methods will block as usual unless this method is invoked
     * again in the meantime.
     *
     * <p> Invoking this method more than once between two successive selection
     * operations has the same effect as invoking it just once.  </p>
     *
     * @return  This selector
     */
    public abstract Selector wakeup();

    /**
     * Closes this selector.
     *
     * <p> If a thread is currently blocked in one of this selector's selection
     * methods then it is interrupted as if by invoking the selector's {@link
     * #wakeup wakeup} method.
     *
     * <p> Any uncancelled keys still associated with this selector are
     * invalidated, their channels are deregistered, and any other resources
     * associated with this selector are released.
     *
     * <p> If this selector is already closed then invoking this method has no
     * effect.
     *
     * <p> After a selector is closed, any further attempt to use it, except by
     * invoking this method or the {@link #wakeup wakeup} method, will cause a
     * {@link ClosedSelectorException} to be thrown. </p>
     *
     * @throws  IOException
     *          If an I/O error occurs
     */
    public abstract void close() throws IOException;

}
