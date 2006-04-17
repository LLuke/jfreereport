/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * Arc2DSerializer.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18.02.2006 : Initial version
 */
package org.jfree.serializer.methods;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.awt.geom.Arc2D;

import org.jfree.serializer.SerializeMethod;

/**
 * Creation-Date: 18.02.2006, 21:52:04
 *
 * @author Thomas Morgner
 */
public class Arc2DSerializer implements SerializeMethod
{
  public Arc2DSerializer()
  {
  }

  /**
   * Writes a serializable object description to the given object output
   * stream.
   *
   * @param o   the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject(Object o, ObjectOutputStream stream) throws IOException
  {
    final Arc2D arc = (Arc2D) o;
    stream.writeDouble(arc.getX());
    stream.writeDouble(arc.getY());
    stream.writeDouble(arc.getWidth());
    stream.writeDouble(arc.getHeight());
    stream.writeDouble(arc.getAngleStart());
    stream.writeDouble(arc.getAngleExtent());
    stream.writeInt(arc.getArcType());
  }

  /**
   * Reads the object from the object input stream.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   * @throws IOException            if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject(ObjectInputStream stream)
          throws IOException, ClassNotFoundException
  {
    final double x = stream.readDouble();
    final double y = stream.readDouble();
    final double w = stream.readDouble();
    final double h = stream.readDouble();
    final double as = stream.readDouble(); // Angle Start
    final double ae = stream.readDouble(); // Angle Extent
    final int at = stream.readInt();       // Arc type
    return new Arc2D.Double(x, y, w, h, as, ae, at);
  }

  /**
   * The class of the object, which this object can serialize.
   *
   * @return the class of the object type, which this method handles.
   */
  public Class getObjectClass()
  {
    return Arc2D.class;
  }
}
