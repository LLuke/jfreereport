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
 * Point2DSerializer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Point2DSerializer.java,v 1.1 2003/05/30 18:47:49 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 30.05.2003 : Initial version
 *  
 */

package com.jrefinery.report.util.serializers;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jrefinery.report.util.SerializeMethod;

/**
 * A SerializeMethod implementation that handles Point2D objects.
 *
 * @author Thomas Morgner
 * @see java.awt.geom.Point2D
 */
public class Point2DSerializer implements SerializeMethod
{
  /**
   * Default Constructor.
   */
  public Point2DSerializer()
  {
  }

  /**
   * Writes a serializable object description to the given object output stream.
   *
   * @param o the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject(Object o, ObjectOutputStream out) throws IOException
  {
    Point2D point = (Point2D) o;
    out.writeDouble(point.getX());
    out.writeDouble(point.getY());
  }

  /**
   * Reads the object from the object input stream.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   * @throws IOException if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    double x = in.readDouble();
    double y = in.readDouble();
    return new Point2D.Double(x, y);
  }

  /**
   * Returns the class of the object, which this object can serialize.
   *
   * @return the class of java.awt.geom.Point2D.
   */
  public Class getObjectClass()
  {
    return Point2D.class;
  }
}
