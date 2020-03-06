package java.nio;

import java.io.FileDescriptor;
import sun.misc.Unsafe;


/**
 * 一个直接字节缓冲区，其内容是文件的内存映射区域。
 *
 * <p>映射的字节缓冲区是通过 {@link java.nio.channels.FileChannel#map }FileChannel.map方法创建的。
 * 此类使用特定于内存映射文件区域的操作扩展了{@link ByteBuffer}ByteBuffer类。
 *
 * <p> 映射的字节缓冲区及其表示的文件映射将保持有效，直到缓冲区本身被垃圾回收为止。
 *
 * <p> 映射的字节缓冲区的内容可以随时更改，例如，如果此程序或其他程序更改了映射文件的相应区域的内容。
 * 此类更改是否发生以及何时发生，取决于操作系统，因此未指定。
 *
 * <a name="inaccess"></a><p> 映射字节缓冲区的全部或部分可能随时无法访问，例如，如果映射文件被截断。
 * 尝试访问映射的字节缓冲区的不可访问区域将不会更改缓冲区的内容，
 * 并且将导致在访问时或稍后发生未指定的异常。因此，强烈建议采取适当的预防措施，
 * 以防止该程序或并发运行的程序对映射文件的操作，除非要读取或写入文件的内容。
 *
 * <p> 否则（除此之外），映射的字节缓冲区的行为与普通的直接字节缓冲区没有什么不同。 </p>
 *
 * @author Mark Reinhold
 * @author JSR-51 Expert Group
 * @since 1.4
 */

public abstract class MappedByteBuffer
    extends ByteBuffer
{

    // This is a little bit backwards: By rights MappedByteBuffer should be a
    // subclass of DirectByteBuffer, but to keep the spec clear and simple, and
    // for optimization purposes, it's easier to do it the other way around.
    // This works because DirectByteBuffer is a package-private class.
    //这有点倒退：按权利，MappedByteBuffer应该是DirectByteBuffer的一个子类，
    // 但是为了使规范清晰易懂，并且出于优化目的，反之亦然。 这是有效的，因为DirectByteBuffer是程序包专用的类。
    // For mapped buffers, a FileDescriptor that may be used for mapping
    // operations if valid; null if the buffer is not mapped.
    // 对于映射的缓冲区，一个FileDescriptor（如果有效）可用于映射操作；如果未映射缓冲区，则为null。
    private final FileDescriptor fd;

    // This should only be invoked by the DirectByteBuffer constructors
    //
    MappedByteBuffer(int mark, int pos, int lim, int cap, // package-private
                     FileDescriptor fd)
    {
        super(mark, pos, lim, cap);
        this.fd = fd;
    }

    MappedByteBuffer(int mark, int pos, int lim, int cap) { // package-private
        super(mark, pos, lim, cap);
        this.fd = null;
    }

    private void checkMapped() {
        if (fd == null)
            // Can only happen if a luser explicitly casts a direct byte buffer
            throw new UnsupportedOperationException();
    }

    // Returns the distance (in bytes) of the buffer from the page aligned address
    // of the mapping. Computed each time to avoid storing in every direct buffer.
    private long mappingOffset() {
        int ps = Bits.pageSize();
        long offset = address % ps;
        return (offset >= 0) ? offset : (ps + offset);
    }

    private long mappingAddress(long mappingOffset) {
        return address - mappingOffset;
    }

    private long mappingLength(long mappingOffset) {
        return (long)capacity() + mappingOffset;
    }

    /**
     * Tells whether or not this buffer's content is resident in physical
     * memory.
     *
     * <p> A return value of <tt>true</tt> implies that it is highly likely
     * that all of the data in this buffer is resident in physical memory and
     * may therefore be accessed without incurring any virtual-memory page
     * faults or I/O operations.  A return value of <tt>false</tt> does not
     * necessarily imply that the buffer's content is not resident in physical
     * memory.
     *
     * <p> The returned value is a hint, rather than a guarantee, because the
     * underlying operating system may have paged out some of the buffer's data
     * by the time that an invocation of this method returns.  </p>
     * <p> 告知此缓冲区的内容是否驻留在物理内存中。
     * <p> 返回值true表示此缓冲区中的所有数据很有可能驻留在物理内存中，
     * 因此可以在不引起任何虚拟内存页面错误或I / O操作的情况下进行访问。
     * 返回值false不一定表示缓冲区的内容未驻留在物理内存中。
     * <p> 返回的值是提示，而不是保证，因为在此方法的调用返回时，底层操作系统可能已调出缓冲区的某些数据。
     *
     * @return  <tt>true</tt> if it is likely that this buffer's content
     *          is resident in physical memory
     */
    public final boolean isLoaded() {
        checkMapped();
        if ((address == 0) || (capacity() == 0))
            return true;
        long offset = mappingOffset();
        long length = mappingLength(offset);
        return isLoaded0(mappingAddress(offset), length, Bits.pageCount(length));
    }

    // not used, but a potential target for a store, see load() for details.
    private static byte unused;

    /**
     * Loads this buffer's content into physical memory.
     *
     * <p> This method makes a best effort to ensure that, when it returns,
     * this buffer's content is resident in physical memory.  Invoking this
     * method may cause some number of page faults and I/O operations to
     * occur. </p>
     * <p>将该缓冲区的内容加载到物理内存中。
     * <p>此方法将尽最大努力确保在返回时该缓冲区的内容驻留在物理内存中。调用此方法可能会导致一定数量的页面错误和I / O操作。
     *
     * @return  This buffer
     */
    public final MappedByteBuffer load() {
        checkMapped();
        if ((address == 0) || (capacity() == 0))
            return this;
        long offset = mappingOffset();
        long length = mappingLength(offset);
        load0(mappingAddress(offset), length);

        // Read a byte from each page to bring it into memory. A checksum
        // is computed as we go along to prevent the compiler from otherwise
        // considering the loop as dead code.
        Unsafe unsafe = Unsafe.getUnsafe();
        int ps = Bits.pageSize();
        int count = Bits.pageCount(length);
        long a = mappingAddress(offset);
        byte x = 0;
        for (int i=0; i<count; i++) {
            x ^= unsafe.getByte(a);
            a += ps;
        }
        if (unused != 0)
            unused = x;

        return this;
    }

    /**
     * Forces any changes made to this buffer's content to be written to the
     * storage device containing the mapped file.
     *
     * <p> If the file mapped into this buffer resides on a local storage
     * device then when this method returns it is guaranteed that all changes
     * made to the buffer since it was created, or since this method was last
     * invoked, will have been written to that device.
     *
     * <p> If the file does not reside on a local device then no such guarantee
     * is made.
     *  <p> 强制对此缓冲区内容进行的任何更改都将写入包含映射文件的存储设备中。
     *  <p> 如果映射到此缓冲区的文件位于本地存储设备上，则当此方法返回时，
     * 可以保证自创建缓冲区以来或对该方法最后一次调用以来对该缓冲区所做的所有更改都已写入该设备。
     *  <p> 如果文件不在本地设备上，则不做任何保证。
     *  <p>如果未以读/写模式({@link java.nio.channels.FileChannel.MapMode#READ_WRITE})映射此缓冲区，则调用此方法无效。
     *
     * <p> If this buffer was not mapped in read/write mode ({@link
     * java.nio.channels.FileChannel.MapMode#READ_WRITE}) then invoking this
     * method has no effect. </p>
     *
     * @return  This buffer
     */
    public final MappedByteBuffer force() {
        checkMapped();
        if ((address != 0) && (capacity() != 0)) {
            long offset = mappingOffset();
            force0(fd, mappingAddress(offset), mappingLength(offset));
        }
        return this;
    }

    private native boolean isLoaded0(long address, long length, int pageCount);
    private native void load0(long address, long length);
    private native void force0(FileDescriptor fd, long address, long length);
}
