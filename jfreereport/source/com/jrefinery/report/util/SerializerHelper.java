/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * SerializerHelper.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SerializerHelper.java,v 1.1 2003/05/30 18:47:48 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.05.2003 : Initial version
 *
 */

package com.jrefinery.report.util;

import java.util.HashMap;
import java.util.Iterator;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.NotSerializableException;

import org.jfree.util.Log;
import org.jfree.xml.factory.objects.ClassComparator;
import com.jrefinery.report.util.serializers.BasicStrokeSerializer;
import com.jrefinery.report.util.serializers.ColorSerializer;
import com.jrefinery.report.util.serializers.Dimension2DSerializer;
import com.jrefinery.report.util.serializers.Ellipse2DSerializer;
import com.jrefinery.report.util.serializers.Line2DSerializer;
import com.jrefinery.report.util.serializers.Point2DSerializer;
import com.jrefinery.report.util.serializers.Rectangle2DSerializer;
import com.jrefinery.report.util.serializers.BandLayoutManagerSerializer;
import com.jrefinery.report.util.serializers.PageFormatSerializer;

public class SerializerHelper
{
  private static SerializerHelper singleton;

  public static SerializerHelper getInstance()
  {
    if (singleton == null)
    {
      singleton = new SerializerHelper();
      singleton.registerMethod(new BasicStrokeSerializer());
      singleton.registerMethod(new ColorSerializer());
      singleton.registerMethod(new Dimension2DSerializer());
      singleton.registerMethod(new Ellipse2DSerializer());
      singleton.registerMethod(new Line2DSerializer());
      singleton.registerMethod(new Point2DSerializer());
      singleton.registerMethod(new Rectangle2DSerializer());
      singleton.registerMethod(new BandLayoutManagerSerializer());
      singleton.registerMethod(new PageFormatSerializer());
    }
    return singleton;
  }

  protected static void setInstance (SerializerHelper helper)
  {
    singleton = helper;
  }

  private HashMap methods;
  /** A class comparator for searching the super class */
  private ClassComparator comparator;

  protected SerializerHelper()
  {
    this.comparator = new ClassComparator();
    this.methods = new HashMap();
  }

  public void registerMethod (SerializeMethod method)
  {
    this.methods.put(method.getObjectClass(), method);
  }

  public void unregisterMethod (SerializeMethod method)
  {
    this.methods.remove(method.getObjectClass());
  }

  protected HashMap getMethods()
  {
    return methods;
  }

  protected ClassComparator getComparator()
  {
    return comparator;
  }



  protected SerializeMethod getSerializer(Class c)
  {
    SerializeMethod sm = (SerializeMethod) methods.get(c);
    if (sm != null)
    {
      return sm;
    }
    return getSuperClassObjectDescription(c, null);
  }

  protected SerializeMethod getSuperClassObjectDescription
      (Class d, SerializeMethod knownSuperClass)
  {
    Iterator enum = methods.keySet().iterator();
    while (enum.hasNext())
    {
      Class keyClass = (Class) enum.next();
      if (keyClass.isAssignableFrom(d))
      {
        SerializeMethod od = (SerializeMethod) methods.get(keyClass);
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


  public void writeObject(Object o, ObjectOutputStream out) throws IOException
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

    SerializeMethod m = getSerializer(o.getClass());
    if (m == null)
    {
      throw new NotSerializableException (o.getClass().getName());
    }
    out.writeByte(2);
    out.writeObject(m.getObjectClass());
    m.writeObject(o, out);
  }

  public Object readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    int type = in.readByte();
    if (type == 0)
    {
      return null;
    }
    if (type == 1)
    {
      return in.readObject();
    }
    Class c = (Class) in.readObject();
    SerializeMethod m = getSerializer(c);
    if (m == null)
    {
      throw new NotSerializableException (c.getName());
    }
    return m.readObject(in);
  }
}
