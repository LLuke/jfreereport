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
 * PaintSerializer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 30.05.2003 : Initial version
 *  
 */

package com.jrefinery.report.util.serializers;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.awt.Color;
import java.awt.color.ColorSpace;

import com.jrefinery.report.util.SerializeMethod;

public class ColorSerializer implements SerializeMethod
{
  public void writeObject(Object o, ObjectOutputStream out) throws IOException
  {
    Color c = (Color) o;
    float[] components = c.getColorComponents(null);
    float alpha = c.getAlpha() / 255.0f;
    out.writeObject(c.getColorSpace());
    out.writeObject(components);
    out.writeFloat(alpha);
  }

  public Object readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    ColorSpace csp = (ColorSpace) in.readObject();
    float[] components = (float[]) in.readObject();
    float alpha = in.readFloat();
    return new Color (csp, components, alpha);
  }

  public Class getObjectClass()
  {
    return Color.class;
  }
}
