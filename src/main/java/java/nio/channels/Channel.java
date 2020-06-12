/*
 * Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.nio.channels;

import java.io.IOException;
import java.io.Closeable;


/**
 * A nexus for I/O operations.
 *
 * <p> A channel represents an open connection to an entity such as a hardware
 * device, a file, a network socket, or a program component that is capable of
 * performing one or more distinct I/O operations, for example reading or
 * writing.
 *
 * <p> A channel is either open or closed.  A channel is open upon creation,
 * and once closed it remains closed.  Once a channel is closed, any attempt to
 * invoke an I/O operation upon it will cause a {@link ClosedChannelException}
 * to be thrown.  Whether or not a channel is open may be tested by invoking
 * its {@link #isOpen isOpen} method.
 *
 * <p> Channels are, in general, intended to be safe for multithreaded access
 * as described in the specifications of the interfaces and classes that extend
 * and implement this interface.
 *
 *
 * @author Mark Reinhold
 * @author JSR-51 Expert Group
 * @since 1.4
 *
 * 承上启下的I / O操作。
 * 通道表示一个打开的连接到一个实体，如一个硬件设备，文件，网络套接字，或者能够执行一个或多个不同的I / O操作，例如读或写的程序组件。
 *  通道是打开或关闭。 通道是在创建开放的，一旦关闭它仍然关闭。 一旦通道被关闭时，任何试图调用I / O操作后，它会导致ClosedChannelException被抛出。
 * 是否进行了信道是开放可通过调用其被测试isOpen方法。
 * 信道，在一般情况下，意在扩展和实现此接口的接口和类的规格说明对于多线程访问安全
 */

public interface Channel extends Closeable {

    /**
     * Tells whether or not this channel is open.
     *
     * @return <tt>true</tt> if, and only if, this channel is open
     */
    public boolean isOpen();

    /**
     * Closes this channel.
     *
     * <p> After a channel is closed, any further attempt to invoke I/O
     * operations upon it will cause a {@link ClosedChannelException} to be
     * thrown.
     *
     * <p> If this channel is already closed then invoking this method has no
     * effect.
     *
     * <p> This method may be invoked at any time.  If some other thread has
     * already invoked it, however, then another invocation will block until
     * the first invocation is complete, after which it will return without
     * effect. </p>
     *
     * @throws  IOException  If an I/O error occurs
     */
    public void close() throws IOException;

}
