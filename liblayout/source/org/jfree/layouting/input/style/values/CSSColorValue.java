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
 * CSSColorType.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: CSSColorValue.java,v 1.1 2006/02/12 21:54:28 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.values;

import java.awt.Color;

/**
 * Creation-Date: 23.11.2005, 12:01:04
 *
 * @author Thomas Morgner
 */
public class CSSColorValue extends Color implements CSSValue
{
  public CSSColorValue(int r, int g, int b, int a)
  {
    super(r, g, b, a);
  }

  public CSSColorValue(int rgba, boolean hasalpha)
  {
    super(rgba, hasalpha);
  }

  public CSSColorValue(float r, float g, float b, float a)
  {
    super(r, g, b, a);
  }

  public CSSColorValue(float r, float g, float b)
  {
    super(r, g, b);
  }

  public CSSColorValue(int r, int g, int b)
  {
    super(r, g, b);
  }

  public String getCSSText()
  {
    return "rgb(" + getRed() + "," + getGreen() + "," + getBlue()+")";
  }

  /**
   * Returns a string representation of this <code>Color</code>. This method is intended
   * to be used only for debugging purposes.  The content and format of the returned
   * string might vary between implementations. The returned string might be empty but
   * cannot be <code>null</code>.
   *
   * @return a string representation of this <code>Color</code>.
   */
  public String toString ()
  {
    return getCSSText();
  }
}
