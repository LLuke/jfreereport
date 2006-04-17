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
 * GradientPaintSerializer.java
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
import java.awt.Color;
import java.awt.GradientPaint;

import org.jfree.serializer.SerializeMethod;

/**
 * Creation-Date: 18.02.2006, 21:46:36
 *
 * @author Thomas Morgner
 */
public class GradientPaintSerializer implements SerializeMethod
{
  public GradientPaintSerializer()
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
    final GradientPaint gp = (GradientPaint) o;
    stream.writeFloat((float) gp.getPoint1().getX());
    stream.writeFloat((float) gp.getPoint1().getY());
    stream.writeObject(gp.getColor1());
    stream.writeFloat((float) gp.getPoint2().getX());
    stream.writeFloat((float) gp.getPoint2().getY());
    stream.writeObject(gp.getColor2());
    stream.writeBoolean(gp.isCyclic());
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
    final float x1 = stream.readFloat();
    final float y1 = stream.readFloat();
    final Color c1 = (Color) stream.readObject();
    final float x2 = stream.readFloat();
    final float y2 = stream.readFloat();
    final Color c2 = (Color) stream.readObject();
    final boolean isCyclic = stream.readBoolean();
    return new GradientPaint(x1, y1, c1, x2, y2, c2, isCyclic);
  }

  /**
   * The class of the object, which this object can serialize.
   *
   * @return the class of the object type, which this method handles.
   */
  public Class getObjectClass()
  {
    return GradientPaint.class;
  }
}
