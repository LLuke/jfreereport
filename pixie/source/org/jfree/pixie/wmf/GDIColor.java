/**
 * ========================================
 * Pixie : a free Java vector image library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/pixie/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * GDIColor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: GDIColor.java,v 1.5 2006/04/17 15:03:24 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.pixie.wmf;

import java.awt.Color;

/**
 * A color implementation, that supports some additional flags defined by the Windows API,
 * but has no Alpha-Channel. This is a BGR color implementation, the flags are stored in
 * the highest byte.
 */
public class GDIColor extends Color
{
  /**
   * The color flags.
   */
  private int flags;

  public static final int PC_RESERVED = 0x01;
  public static final int PC_EXPLICIT = 0x02;
  public static final int PC_NOCOLLAPSE = 0x04;

  /**
   * Creates a new GDI color instance by parsing the color reference.
   *
   * @param colorref the integer color reference.
   */
  public GDIColor (final int colorref)
  {
    this(getR(colorref), getG(colorref), getB(colorref), getFlags(colorref));
  }

  /**
   * Creates a new GDI Color instance.
   *
   * @param r     the red channel.
   * @param g     the green channel.
   * @param b     the blue channel.
   * @param flags the Windows Color flags.
   */
  public GDIColor (final int r, final int g, final int b, final int flags)
  {
    super(r, g, b);
    this.flags = flags;
  }

  /**
   * Extracts the RED channel from the given ColorReference.
   *
   * @param ref the color reference.
   * @return the red channel.
   */
  private static int getR (final int ref)
  {
    int retval = (ref & 0x000000ff);
    if (retval < 0)
    {
      retval = (retval + 256);
    }
    return retval;
  }

  /**
   * Extracts the GREEN channel from the given ColorReference.
   *
   * @param ref the color reference.
   * @return the green channel.
   */
  private static int getG (final int ref)
  {
    final int retval = (ref & 0x0000ff00) >> 8;
    return retval;
  }

  /**
   * Extracts the BLUE channel from the given ColorReference.
   *
   * @param ref the color reference.
   * @return the blue channel.
   */
  private static int getB (final int ref)
  {
    final int retval = (ref & 0x00ff0000) >> 16;
    return retval;
  }

  /**
   * Extracts the Color Flags from the given ColorReference.
   *
   * @param ref the color reference.
   * @return the color flags.
   */
  private static int getFlags (final int ref)
  {
    return (ref & 0xff000000) >> 24;
  }

  /**
   * Returns the PC_RESERVED flag state for this color.
   *
   * @return true, if PC_RESERVED is set, false otherwise.
   */
  public boolean isReserved ()
  {
    return (this.flags & PC_RESERVED) == PC_RESERVED;
  }

  /**
   * Returns the PC_EXPLICIT flag state for this color.
   *
   * @return true, if PC_EXPLICIT is set, false otherwise.
   */
  public boolean isExplicit ()
  {
    return (this.flags & PC_EXPLICIT) == PC_EXPLICIT;
  }

  /**
   * Returns the PC_NOCOLLAPSE flag state for this color.
   *
   * @return true, if PC_NOCOLLAPSE is set, false otherwise.
   */
  public boolean isNoCollapse ()
  {
    return (this.flags & PC_NOCOLLAPSE) == PC_NOCOLLAPSE;
  }

  /**
   * Gets the assigned flag for the color.
   *
   * @return the flags.
   */
  public int getFlags ()
  {
    return flags;
  }

  /**
   * Translates the given color instance into a GDI color reference.
   *
   * @param c the color that should be translated.
   * @return the created color reference.
   */
  public static int translateColor (final Color c)
  {
    final int red = c.getRed();
    final int green = c.getGreen();
    final int blue = c.getBlue();
    int flags = 0;

    if (c instanceof GDIColor)
    {
      final GDIColor gc = (GDIColor) c;
      flags = gc.getFlags();
    }

    int retval = flags;
    retval = (retval << 8) + blue;
    retval = (retval << 8) + green;
    retval = (retval << 8) + red;
    return retval;
  }
}
