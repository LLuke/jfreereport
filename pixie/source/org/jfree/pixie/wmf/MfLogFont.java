/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * MfLogFont.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: MfLogFont.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

import java.awt.Font;
import java.awt.geom.AffineTransform;

/**
	A Windows metafile logical font object.
*/
public class MfLogFont implements WmfObject
{
  private String face;
  private int size;
  private int style;
  private boolean strikeout;
  private boolean underline;
  private double rotation;
  
  /** Construct from a metafile record. */
  public MfLogFont()
  {
  }

  public void setFace (final String face)
  {
    this.face = face;
  }
  
  /** The name of the font face. */
  public String getFace()
  {
    return face;
  }

  public void setSize (final int size)
  {
    this.size = size;
  }


  /** The size, in logical units. */
  public int getSize()
  {
    return size;
  }

  /** The font style. */
  public int getStyle()
  {
    return style;
  }

  public void setStyle (final int style)
  {
    this.style = style;
  }

  /** True if this is an underlined font. */
  public boolean isUnderline()
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
    final Font retfont = new Font (getFace(), getStyle(), getSize());
    final double rot = Math.sin (Math.toRadians(-rotation));
    return retfont.deriveFont (AffineTransform.getRotateInstance(rot));
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
