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
package org.jfree.fonts.registry;

import org.jfree.fonts.LibFontsDefaults;

/**
 * An placeholder metrics for buggy fonts.
 *
 * @author Thomas Morgner
 */
public class EmptyFontMetrics implements FontMetrics
{
  private double baseSize;
  private double baseWidth;

  public EmptyFontMetrics(final double baseHeight, final double baseWidth)
  {
    this.baseSize = baseHeight;
    this.baseWidth = baseWidth;
  }

  public EmptyFontMetrics()
  {
  }

  /**
   * From the baseline to the
   *
   * @return
   */
  public double getAscent()
  {
    return baseSize * LibFontsDefaults.DEFAULT_ASCENT_SIZE;
  }

  public double getDescent()
  {
    return baseSize * LibFontsDefaults.DEFAULT_DESCENT_SIZE;
  }

  public double getLeading()
  {
    return 0;
  }

  /**
   * The height of the lowercase 'x'. This is used as hint, which size the
   * lowercase characters will have.
   *
   * @return
   */
  public double getXHeight()
  {
    return baseSize * LibFontsDefaults.DEFAULT_XHEIGHT_SIZE;
  }

  public double getOverlinePosition()
  {
    return 0;
  }

  public double getUnderlinePosition()
  {
    return getAscent();
  }

  public double getStrikeThroughPosition()
  {
    return getXHeight() * LibFontsDefaults.DEFAULT_STRIKETHROUGH_POSITION;
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
    return baseSize;
  }

  public double getMaxCharAdvance()
  {
    return baseWidth;
  }

  public double getCharWidth(int codePoint)
  {
    return baseWidth;
  }

  public double getKerning(int previous, int codePoint)
  {
    return 0;
  }

  /**
   * Baselines are defined for scripts, not glyphs. A glyph carries script
   * information most of the time (unless it is a neutral characters or just
   * weird).
   *
   * @param c
   * @return
   */
  public BaselineInfo getBaselines(int c, BaselineInfo info)
  {
    if (info == null)
    {
      info = new BaselineInfo();
    }

    // this is the most dilletantic baseline computation on this planet.
    // But without any font metrics, it is also the base baseline computation :)

    // The ascent is local - but we need the global baseline, relative to the
    // MaxAscent.
    final double maxAscent = getMaxAscent();
    info.setBaseline(BaselineInfo.MATHEMATICAL,
            maxAscent - getXHeight());
    info.setBaseline(BaselineInfo.IDEOGRAPHIC, getMaxHeight());
    info.setBaseline(BaselineInfo.MIDDLE, maxAscent / 2);
    info.setBaseline(BaselineInfo.ALPHABETIC, maxAscent);
    info.setBaseline(BaselineInfo.CENTRAL, maxAscent / 2);
    info.setBaseline(BaselineInfo.HANGING, maxAscent - getXHeight());
    info.setDominantBaseline(BaselineInfo.ALPHABETIC);

    return info;
  }
}
