/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Point2DSerializer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: Dimension2DSerializer.java,v 1.2 2003/08/24 15:13:23 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.05.2003 : Initial version
 *
 */

package org.jfree.report.util.serializers;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.report.util.SerializeMethod;
import org.jfree.ui.FloatDimension;

/**
 * A SerializeMethod implementation that handles Dimension2D objects.
 *
 * @author Thomas Morgner
 * @see java.awt.geom.Dimension2D
 */
public class Dimension2DSerializer implements SerializeMethod
{
  /**
   * Default Constructor.
   */
  public Dimension2DSerializer()
  {
  }

  /**
   * Writes a serializable object description to the given object output stream.
   * This method writes the width and the height of the dimension into the stream.
   *
   * @param o the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject(final Object o, final ObjectOutputStream out) throws IOException
  {
    final Dimension2D dim = (Dimension2D) o;
    out.writeDouble(dim.getWidth());
    out.writeDouble(dim.getHeight());
  }

  /**
   * Reads the object from the object input stream. This read the width and
   * height and constructs a new FloatDimension object.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   * @throws IOException if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    final double w = in.readDouble();
    final double h = in.readDouble();
    return new FloatDimension((float) w, (float) h);
  }

  /**
   * Returns the class of the object, which this object can serialize.
   *
   * @return the class of java.awt.geom.Dimension2D.
   */
  public Class getObjectClass()
  {
    return Point2D.class;
  }
}
