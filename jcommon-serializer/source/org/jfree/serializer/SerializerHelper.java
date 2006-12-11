/**
 * ===================================================
 * JCommon-Serializer : a free serialization framework
 * ===================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/jcommon-serializer/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Object Refinery Limited and Pentaho Corporation.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * SerializerHelper.java
 * ------------
 * (C) Copyright 2006, by Object Refinery Limited and Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SerializerHelper.java,v 1.2 2006/04/17 16:03:24 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.serializer;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.jfree.util.ClassComparator;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * The SerializeHelper is used to make implementing custom serialization
 * handlers easier. Handlers for certain object types need to be added to this
 * helper before this implementation is usable.
 *
 * @author Thomas Morgner
 */
public class SerializerHelper
{
  /**
   * The singleton instance of the serialize helper.
   */
  private static SerializerHelper singleton;

  /**
   * Returns or creates a new SerializerHelper. When a new instance is created
   * by this method, all known SerializeMethods are registered.
   *
   * @return the SerializerHelper singleton instance.
   */
  public synchronized static SerializerHelper getInstance()
  {
    if (singleton == null)
    {
      singleton = new SerializerHelper();
      singleton.registerMethods();
    }
    return singleton;
  }


  /**
   * This method can be used to replace the singleton instance of this helper.
   *
   * @param helper the new instance of the serialize helper.
   */
  protected static void setInstance(final SerializerHelper helper)
  {
    singleton = helper;
  }

  /**
   * A collection of the serializer methods.
   */
  private final HashMap methods;

  /**
   * A class comparator for searching the super class of an certain class.
   */
  private final ClassComparator comparator;

  /**
   * Creates a new SerializerHelper.
   */
  protected SerializerHelper()
  {
    this.comparator = new ClassComparator();
    this.methods = new HashMap();
  }

  /**
   * Registers a new SerializeMethod with this SerializerHelper.
   *
   * @param method the method that should be registered.
   */
  public synchronized void registerMethod(final SerializeMethod method)
  {
    this.methods.put(method.getObjectClass(), method);
  }

  protected void registerMethods()
  {
    final Configuration config = JCommonSerializerBoot.getInstance().getGlobalConfig();
    Iterator sit = config.findPropertyKeys("org.jfree.serializer.handler.");

    while (sit.hasNext())
    {
      final String configkey = (String) sit.next();
      final String c = config.getConfigProperty(configkey);
      Object maybeModule = ObjectUtilities.loadAndInstantiate
          (c, SerializerHelper.class, SerializeMethod.class);
      if (maybeModule != null)
      {
        SerializeMethod module = (SerializeMethod) maybeModule;
        registerMethod(module);
      }
      else
      {
        Log.warn("Invalid SerializeMethod implementation: " + c);
      }
    }
  }

  /**
   * Deregisters a new SerializeMethod with this SerializerHelper.
   *
   * @param method the method that should be deregistered.
   */
  public synchronized void unregisterMethod(final SerializeMethod method)
  {
    this.methods.remove(method.getObjectClass());
  }

  /**
   * Returns the collection of all registered serialize methods.
   *
   * @return a collection of the registered serialize methods.
   */
  protected HashMap getMethods()
  {
    return methods;
  }

  /**
   * Returns the class comparator instance used to find correct super classes.
   *
   * @return the class comparator.
   */
  protected ClassComparator getComparator()
  {
    return comparator;
  }

  /**
   * Looks up the SerializeMethod for the given class or null if there is no
   * SerializeMethod for the given class.
   *
   * @param c the class for which we want to lookup a serialize method.
   * @return the method or null, if there is no registered method for the
   *         class.
   */
  protected SerializeMethod getSerializer(final Class c)
  {
    final SerializeMethod sm = (SerializeMethod) methods.get(c);
    if (sm != null)
    {
      return sm;
    }
    return getSuperClassObjectDescription(c);
  }

  /**
   * Looks up the SerializeMethod for the given class or null if there is no
   * SerializeMethod for the given class. This method searches all
   * superclasses.
   *
   * @param d               the class for which we want to lookup a serialize
   *                        method.
   * @param knownSuperClass the known super class, if any or null.
   * @return the method or null, if there is no registered method for the
   *         class.
   */
  protected SerializeMethod getSuperClassObjectDescription
      (final Class d)
  {
    SerializeMethod knownSuperClass = null;
    final Iterator keys = methods.keySet().iterator();
    while (keys.hasNext())
    {
      final Class keyClass = (Class) keys.next();
      if (keyClass.isAssignableFrom(d))
      {
        final SerializeMethod od = (SerializeMethod) methods.get(keyClass);
        if (knownSuperClass == null)
        {
          knownSuperClass = od;
        }
        else
        {
          if (comparator.isComparable
              (knownSuperClass.getObjectClass(), od.getObjectClass()))
          {
            if (comparator.compare
                (knownSuperClass.getObjectClass(), od.getObjectClass()) < 0)
            {
              knownSuperClass = od;
            }
          }
        }
      }
    }
    return knownSuperClass;
  }


  /**
   * Writes a serializable object description to the given object output stream.
   * This method selects the best serialize helper method for the given object.
   *
   * @param o   the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public synchronized void writeObject(final Object o,
                                       final ObjectOutputStream out)
      throws IOException
  {
    if (o == null)
    {
      out.writeByte(0);
      return;
    }
    if (o instanceof Serializable)
    {
      out.writeByte(1);
      out.writeObject(o);
      return;
    }

    final SerializeMethod m = getSerializer(o.getClass());
    if (m == null)
    {
      throw new NotSerializableException(o.getClass().getName());
    }
    out.writeByte(2);
    out.writeObject(m.getObjectClass());
    m.writeObject(o, out);
  }

  /**
   * Reads the object from the object input stream. This object selects the best
   * serializer to read the object.
   * <p/>
   * Make sure, that you use the same configuration (library and class versions,
   * registered methods in the SerializerHelper) for reading as you used for
   * writing.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   * @throws IOException            if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public synchronized Object readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    final int type = in.readByte();
    if (type == 0)
    {
      return null;
    }
    if (type == 1)
    {
      return in.readObject();
    }
    final Class c = (Class) in.readObject();
    final SerializeMethod m = getSerializer(c);
    if (m == null)
    {
      throw new NotSerializableException(c.getName());
    }
    return m.readObject(in);
  }
}
