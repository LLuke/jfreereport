/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * PaintSerializer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ColorSerializer.java,v 1.1 2003/07/07 22:44:09 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.05.2003 : Initial version
 *
 */

package org.jfree.report.util.serializers;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.report.util.SerializeMethod;

/**
 * A SerializeMethod implementation that handles Colors.
 *
 * @author Thomas Morgner
 * @see Color
 */
public class ColorSerializer implements SerializeMethod
{
  /**
   * Default Constructor.
   */
  public ColorSerializer()
  {
  }

  /**
   * Writes a serializable object description to the given object output stream.
   * This writes the color components, the alpha channel and the color space.
   *
   * @param o the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject(final Object o, final ObjectOutputStream out) throws IOException
  {
    final Color c = (Color) o;
    final float[] components = c.getColorComponents(null);
    final float alpha = c.getAlpha() / 255.0f;
    out.writeObject(c.getColorSpace());
    out.writeObject(components);
    out.writeFloat(alpha);
  }

  /**
   * Reads the object from the object input stream. This reads the color components,
   * the alpha channel and the color space and constructs a new java.awt.Color instance.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   * @throws IOException if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    final ColorSpace csp = (ColorSpace) in.readObject();
    final float[] components = (float[]) in.readObject();
    final float alpha = in.readFloat();
    return new Color(csp, components, alpha);
  }

  /**
   * Returns the class of the object, which this object can serialize.
   *
   * @return the class of java.awt.Color.
   */
  public Class getObjectClass()
  {
    return Color.class;
  }
}
