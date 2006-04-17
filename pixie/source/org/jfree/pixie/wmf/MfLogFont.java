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
 * MfLogFont.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
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
package org.jfree.pixie.wmf;

import java.awt.Font;
import java.awt.geom.AffineTransform;

/**
 * A Windows metafile logical font object.
 */
public class MfLogFont implements WmfObject
{
  private String face;
  private int size;
  private int style;
  private boolean strikeout;
  private boolean underline;
  private double rotation;

  /**
   * Construct from a metafile record.
   */
  public MfLogFont ()
  {
  }

  public void setFace (final String face)
  {
    this.face = face;
  }

  /**
   * The name of the font face.
   */
  public String getFace ()
  {
    return face;
  }

  public void setSize (final int size)
  {
    this.size = size;
  }


  /**
   * The size, in logical units.
   */
  public int getSize ()
  {
    return size;
  }

  /**
   * The font style.
   */
  public int getStyle ()
  {
    return style;
  }

  public void setStyle (final int style)
  {
    this.style = style;
  }

  /**
   * True if this is an underlined font.
   */
  public boolean isUnderline ()
  {
    return underline;
  }

  public void setUnderline (final boolean underline)
  {
    this.underline = underline;
  }

  public boolean isStrikeOut ()
  {
    return strikeout;
  }

  public void setStrikeOut (final boolean b)
  {
    this.strikeout = b;
  }

  public Font createFont ()
  {
    final Font retfont = new Font(getFace(), getStyle(), getSize());
    final double rot = Math.sin(Math.toRadians(-rotation));
    return retfont.deriveFont(AffineTransform.getRotateInstance(rot));
  }

  public int getType ()
  {
    return OBJ_FONT;
  }

  public double getRotation ()
  {
    return rotation;
  }

  public void setRotation (final double d)
  {
    this.rotation = d;
  }
}
