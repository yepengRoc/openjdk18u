package com.sun.corba.se.spi.activation;

/**
* com/sun/corba/se/spi/activation/ActivatorHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/jenkins/workspace/build-scripts/jobs/jdk8u/jdk8u-mac-x64-hotspot/workspace/build/src/corba/src/share/classes/com/sun/corba/se/spi/activation/activation.idl
* Sunday, January 19, 2020 6:43:25 AM PST
*/

public final class ActivatorHolder implements org.omg.CORBA.portable.Streamable
{
  public com.sun.corba.se.spi.activation.Activator value = null;

  public ActivatorHolder ()
  {
  }

  public ActivatorHolder (com.sun.corba.se.spi.activation.Activator initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = com.sun.corba.se.spi.activation.ActivatorHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    com.sun.corba.se.spi.activation.ActivatorHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return com.sun.corba.se.spi.activation.ActivatorHelper.type ();
  }

}
