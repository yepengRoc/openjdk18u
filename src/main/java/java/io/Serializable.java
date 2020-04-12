/*
 * Copyright (c) 1996, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.io;

/**
 * Serializability of a class is enabled by the class implementing the
 * java.io.Serializable interface. Classes that do not implement this
 * interface will not have any of their state serialized or
 * deserialized.  All subtypes of a serializable class are themselves
 * serializable.  The serialization interface has no methods or fields
 * and serves only to identify the semantics of being serializable. <p>
 *     通过实现java.io.Serializable接口的类，可以启用类的可序列化性。未实现此接口的类将不会对其状态进行序列化或反序列化。
 *     可序列化类的所有子类型本身都是可序列化的。序列化接口没有方法或字段，仅用于标识可序列化的语义。
 *
 * To allow subtypes of non-serializable classes to be serialized, the
 * subtype may assume responsibility for saving and restoring the
 * state of the supertype's public, protected, and (if accessible)
 * package fields.  The subtype may assume this responsibility only if
 * the class it extends has an accessible no-arg constructor to
 * initialize the class's state.  It is an error to declare a class
 * Serializable if this is not the case.  The error will be detected at
 * runtime. <p>
 *     为了允许不可序列化类的子类型被序列化，该子类型可以承担保存和恢复超类型的公共，
 *     受保护和（如果可访问）包字段状态的责任。仅当其扩展的类具有可访问的无参数构造函数以初始化类的状态时，
 *     该子类型才可以承担此责任。如果不是这样，则声明一个可序列化的类是错误的。该错误将在运行时检测到。
 *
 * During deserialization, the fields of non-serializable classes will
 * be initialized using the public or protected no-arg constructor of
 * the class.  A no-arg constructor must be accessible to the subclass
 * that is serializable.  The fields of serializable subclasses will
 * be restored from the stream. <p>
 *     反序列化期间，将使用该类的公共或受保护的无参数构造函数初始化不可序列化类的字段。
 *     无参数构造函数必须可序列化的子类可访问。可序列化子类的字段将从流中恢复
 *
 * When traversing a graph, an object may be encountered that does not
 * support the Serializable interface. In this case the
 * NotSerializableException will be thrown and will identify the class
 * of the non-serializable object. <p>
 *     遍历图形时，可能会遇到不支持Serializable接口的对象。在这种情况下，将抛出NotSerializableException并标识不可序列化对象的类。
 *
 * Classes that require special handling during the serialization and
 * deserialization process must implement special methods with these exact
 * signatures:
 * 在序列化和反序列化过程中需要特殊处理的类必须实现具有以下确切签名的特殊方法：
 * <PRE>
 * private void writeObject(java.io.ObjectOutputStream out)
 *     throws IOException
 * private void readObject(java.io.ObjectInputStream in)
 *     throws IOException, ClassNotFoundException;
 * private void readObjectNoData()
 *     throws ObjectStreamException;
 * </PRE>
 *
 * <p>The writeObject method is responsible for writing the state of the
 * object for its particular class so that the corresponding
 * readObject method can restore it.  The default mechanism for saving
 * the Object's fields can be invoked by calling
 * out.defaultWriteObject. The method does not need to concern
 * itself with the state belonging to its superclasses or subclasses.
 * State is saved by writing the individual fields to the
 * ObjectOutputStream using the writeObject method or by using the
 * methods for primitive data types supported by DataOutput.
 * writeObject方法负责为其特定类写入对象的状态，以便相应的readObject方法可以还原它。
 * 保存对象字段的默认机制可以通过调用out.defaultWriteObject来调用。该方法无需将自身与属于其超类或子类的状态相关。
 * 通过使用writeObject方法将单个字段写入ObjectOutputStream或使用DataOutput支持的原始数据类型的方法来保存状态。
 *
 * <p>The readObject method is responsible for reading from the stream and
 * restoring the classes fields. It may call in.defaultReadObject to invoke
 * the default mechanism for restoring the object's non-static and
 * non-transient fields.  The defaultReadObject method uses information in
 * the stream to assign the fields of the object saved in the stream with the
 * correspondingly named fields in the current object.  This handles the case
 * when the class has evolved to add new fields. The method does not need to
 * concern itself with the state belonging to its superclasses or subclasses.
 * State is saved by writing the individual fields to the
 * ObjectOutputStream using the writeObject method or by using the
 * methods for primitive data types supported by DataOutput.
 * readObject方法负责从流中读取并还原类字段。它可以调用in.defaultReadObject来调用用于还原对象的非静态和非瞬态字段的默认机制。
 * defaultReadObject方法使用流中的信息为流中保存的对象的字段分配当前对象中相应命名的字段。这处理了类已演化为添加新字段的情况。
 * 该方法无需将自身与属于其超类或子类的状态相关。通过使用writeObject方法将单个字段写入ObjectOutputStream或使用DataOutput支持的
 * 原始数据类型的方法来保存状态。
 *
 * <p>The readObjectNoData method is responsible for initializing the state of
 * the object for its particular class in the event that the serialization
 * stream does not list the given class as a superclass of the object being
 * deserialized.  This may occur in cases where the receiving party uses a
 * different version of the deserialized instance's class than the sending
 * party, and the receiver's version extends classes that are not extended by
 * the sender's version.  This may also occur if the serialization stream has
 * been tampered; hence, readObjectNoData is useful for initializing
 * deserialized objects properly despite a "hostile" or incomplete source
 * stream.
 * 如果序列化流未将给定类列为要反序列化的对象的超类，则readObjectNoData方法负责为其特定类初始化对象的状态。
 * 在接收方使用与发送方不同的反序列化实例类的版本，并且接收方的版本扩展了发送方版本未扩展的类的情况下，
 * 可能会发生这种情况。如果序列化流已被篡改，也会发生这种情况。因此，尽管源流“敌对”或不完整，但readObjectNoData对于正确初始化反序列化的对象很有用。
 * <p>Serializable classes that need to designate an alternative object to be
 * used when writing an object to the stream should implement this
 * special method with the exact signature:
 * 在将对象写入流中时需要指定要使用的替代对象的可序列化类应使用确切的签名实现此特殊方法：
 * <PRE>
 * ANY-ACCESS-MODIFIER Object writeReplace() throws ObjectStreamException;
 * </PRE><p>
 *
 * This writeReplace method is invoked by serialization if the method
 * exists and it would be accessible from a method defined within the
 * class of the object being serialized. Thus, the method can have private,
 * protected and package-private access. Subclass access to this method
 * follows java accessibility rules. <p>
 *     如果该writeReplace方法存在，则可以通过序列化调用，并且可以从正在序列化的对象的类中定义的方法访问该方法。
 *     因此，该方法可以具有私有，受保护和程序包私有的访问。对该方法的子类访问遵循Java可访问性规则。
 *
 * Classes that need to designate a replacement when an instance of it
 * is read from the stream should implement this special method with the
 * exact signature.
 * 从流中读取实例时需要指定替换的类应使用具有确切签名的特殊方法来实现。
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER Object readResolve() throws ObjectStreamException;
 * </PRE><p>
 *
 * This readResolve method follows the same invocation rules and
 * accessibility rules as writeReplace.<p>此readResolve方法遵循与writeReplace相同的调用规则和可访问性规则。
 *
 * The serialization runtime associates with each serializable class a version
 * number, called a serialVersionUID, which is used during deserialization to
 * verify that the sender and receiver of a serialized object have loaded
 * classes for that object that are compatible with respect to serialization.
 * If the receiver has loaded a class for the object that has a different
 * serialVersionUID than that of the corresponding sender's class, then
 * deserialization will result in an {@link InvalidClassException}.  A
 * serializable class can declare its own serialVersionUID explicitly by
 * declaring a field named <code>"serialVersionUID"</code> that must be static,
 * final, and of type <code>long</code>:
 * 序列化运行时与每个可序列化的类关联一个版本号，称为serialVersionUID，
 * 在反序列化期间使用该版本号来验证序列化对象的发送者和接收者是否已加载了该对象的与序列化兼容的类。
 * 如果接收者已为该对象加载了一个与相应发送者类具有不同的serialVersionUID的类，则反序列化将导致InvalidClassException。
 * 可序列化的类可以通过声明一个名称为“ serialVersionUID”的字段来显式声明其自己的serialVersionUID，该字段必须是静态的，最终的且类型为long：
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER static final long serialVersionUID = 42L;
 * </PRE>
 *
 * If a serializable class does not explicitly declare a serialVersionUID, then
 * the serialization runtime will calculate a default serialVersionUID value
 * for that class based on various aspects of the class, as described in the
 * Java(TM) Object Serialization Specification.  However, it is <em>strongly
 * recommended</em> that all serializable classes explicitly declare
 * serialVersionUID values, since the default serialVersionUID computation is
 * highly sensitive to class details that may vary depending on compiler
 * implementations, and can thus result in unexpected
 * <code>InvalidClassException</code>s during deserialization.  Therefore, to
 * guarantee a consistent serialVersionUID value across different java compiler
 * implementations, a serializable class must declare an explicit
 * serialVersionUID value.  It is also strongly advised that explicit
 * serialVersionUID declarations use the <code>private</code> modifier where
 * possible, since such declarations apply only to the immediately declaring
 * class--serialVersionUID fields are not useful as inherited members. Array
 * classes cannot declare an explicit serialVersionUID, so they always have
 * the default computed value, but the requirement for matching
 * serialVersionUID values is waived for array classes.
 * 如果可序列化的类未明确声明serialVersionUID，则串行化运行时将根据该类的各个方面为该类计算默认的serialVersionUID值，
 * 如Java™对象序列化规范中所述。但是，强烈建议所有可序列化的类显式声明serialVersionUID值，
 * 因为默认的serialVersionUID计算对类详细信息高度敏感，而类详细信息可能会根据编译器的实现而有所不同，
 * 因此可能在反序列化期间导致意外的InvalidClassExceptions。因此，为了保证不同Java编译器实现之间的serialVersionUID值一致，
 * 可序列化的类必须声明一个显式的serialVersionUID值。还强烈建议显式serialVersionUID声明在可能的情况下使用private修饰符，
 * 因为此类声明仅适用于立即声明的类-serialVersionUID字段作为继承成员不起作用。数组类不能声明显式的serialVersionUID，
 * 因此它们始终具有默认的计算值，但是对于数组类，无需匹配serialVersionUID值。
 *
 * @author  unascribed
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 * @see java.io.ObjectOutput
 * @see java.io.ObjectInput
 * @see java.io.Externalizable
 * @since   JDK1.1
 */
public interface Serializable {
}
