/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.monospace;

import org.jfree.fonts.LibFontsDefaults;
import org.jfree.fonts.registry.BaselineInfo;
import org.jfree.fonts.registry.FontMetrics;

/**
 * Creation-Date: 13.05.2007, 13:14:25
 *
 * @author Thomas Morgner
 */
public class MonospaceFontMetrics implements FontMetrics
{
  private double charHeight;
  private double charWidth;

  public MonospaceFontMetrics(final int cpi, final int lpi)
  {
    charHeight = (72.0 / lpi);
    charWidth = (72.0 / cpi);
  }

  /**
   * From the baseline to the
   *
   * @return
   */
  public double getAscent()
  {
    return LibFontsDefaults.DEFAULT_ASCENT_SIZE * charHeight;
  }

  public double getDescent()
  {
    return LibFontsDefaults.DEFAULT_DESCENT_SIZE * charHeight;
  }

  public double getLeading()
  {
    return 0;
  }

  /**
   * The height of the lowercase 'x'. This is used as hint, which size the lowercase characters will have.
   *
   * @return
   */
  public double getXHeight()
  {
    return LibFontsDefaults.DEFAULT_XHEIGHT_SIZE * charHeight;
  }

  public double getOverlinePosition()
  {
    return getAscent();
  }

  public double getUnderlinePosition()
  {
    return getAscent() + 0.5 * getDescent();
  }

  public double getStrikeThroughPosition()
  {
    return LibFontsDefaults.DEFAULT_STRIKETHROUGH_POSITION * getXHeight();
  }

  public double getMaxAscent()
  {
    return getAscent();
  }

  public double getMaxDescent()
  {
    return getDescent();
  }

  public double getMaxLeading()
  {
    return getLeading();
  }

  public double getMaxHeight()
  {
    return charHeight;
  }

  public double getMaxCharAdvance()
  {
    return charWidth;
  }

  public double getCharWidth(final int codePoint)
  {
    return charWidth;
  }

  public double getKerning(final int previous, final int codePoint)
  {
    return 0;
  }

  /**
   * Baselines are defined for scripts, not glyphs. A glyph carries script information most of the time (unless it is a
   * neutral characters or just weird).
   *
   * @param c
   * @return
   */
  public BaselineInfo getBaselines(final int codePoint, BaselineInfo info)
  {
    if (info == null)
    {
      info = new BaselineInfo();
    }


    info.setBaseline(BaselineInfo.HANGING, 0);
    info.setBaseline(BaselineInfo.MATHEMATICAL, charHeight * 0.5);
    info.setBaseline(BaselineInfo.CENTRAL, charHeight * 0.5);
    info.setBaseline(BaselineInfo.MIDDLE, charHeight * 0.5);
    info.setBaseline(BaselineInfo.ALPHABETIC, getMaxAscent());
    info.setBaseline(BaselineInfo.IDEOGRAPHIC, getMaxHeight());
    return info;
  }
}
