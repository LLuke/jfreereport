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
 * Ellipse2DSerializer.java
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

import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.serializer.SerializeMethod;

/**
 * A SerializeMethod implementation that handles Ellipse2D objects.
 *
 * @author Thomas Morgner
 * @see java.awt.geom.Ellipse2D
 */
public class Ellipse2DSerializer implements SerializeMethod
{
  /**
   * Default Constructor.
   */
  public Ellipse2DSerializer ()
  {
  }

  /**
   * Writes a serializable object description to the given object output stream.
   *
   * @param o   the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject (final Object o, final ObjectOutputStream out)
          throws IOException
  {
    final Ellipse2D ellipse = (Ellipse2D) o;
    out.writeDouble(ellipse.getX());
    out.writeDouble(ellipse.getY());
    out.writeDouble(ellipse.getWidth());
    out.writeDouble(ellipse.getHeight());
  }

  /**
   * Reads the object from the object input stream.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   *
   * @throws IOException            if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    final double x = in.readDouble();
    final double y = in.readDouble();
    final double w = in.readDouble();
    final double h = in.readDouble();
    return new Ellipse2D.Double(x, y, w, h);
  }

  /**
   * Returns the class of the object, which this object can serialize.
   *
   * @return the class of java.awt.geom.Ellipse2D.
   */
  public Class getObjectClass ()
  {
    return Ellipse2D.class;
  }
}
