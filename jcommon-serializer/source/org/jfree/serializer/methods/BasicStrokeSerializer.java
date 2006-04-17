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
 * BasicStrokeSerializer.java
 * ------------
 * (C) Copyright 2006, by Object Refinery Limited and Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.serializer.methods;

import java.awt.BasicStroke;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.serializer.SerializeMethod;

/**
 * A SerializeMethod implementation that handles BasicStrokes.
 *
 * @author Thomas Morgner
 * @see BasicStroke
 */
public class BasicStrokeSerializer implements SerializeMethod
{
  /**
   * Default Constructor.
   */
  public BasicStrokeSerializer ()
  {
  }

  /**
   * Writes a serializable object description to the given object output stream.
   *
   * @param o   the to be serialized object.
   * @param stream the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject (final Object o, final ObjectOutputStream stream)
          throws IOException
  {
    final BasicStroke s = (BasicStroke) o;
    stream.writeFloat(s.getLineWidth());
    stream.writeInt(s.getEndCap());
    stream.writeInt(s.getLineJoin());
    stream.writeFloat(s.getMiterLimit());
    stream.writeObject(s.getDashArray());
    stream.writeFloat(s.getDashPhase());
  }

  /**
   * Reads the object from the object input stream.
   *
   * @param stream the object input stream from where to read the serialized data.
   * @return the generated object.
   *
   * @throws IOException            if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject (final ObjectInputStream stream)
          throws IOException, ClassNotFoundException
  {
    final float width = stream.readFloat();
    final int cap = stream.readInt();
    final int join = stream.readInt();
    final float miterLimit = stream.readFloat();
    final float[] dash = (float[]) stream.readObject();
    final float dashPhase = stream.readFloat();
    return new BasicStroke(width, cap, join, miterLimit, dash, dashPhase);
  }

  /**
   * The class of the object, which this object can serialize.
   *
   * @return the class <code>java.awt.BasicStroke</code>.
   */
  public Class getObjectClass ()
  {
    return BasicStroke.class;
  }
}
