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
 * TrueTypeFontMetrics.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ScalableTrueTypeFontMetrics.java,v 1.1 2006/01/27 20:38:37 taqua Exp $
 *
 * Changes
 * -------------------------
 * 15.12.2005 : Initial version
 */
package org.jfree.fonts.truetype;

import java.io.IOException;

import org.jfree.fonts.LibFontsDefaults;


/**
 * This is the scalable backend for truetype fonts. To make any use of it,
 * you have to apply the font size to these metrics.
 *
 * @author Thomas Morgner
 */
public class ScalableTrueTypeFontMetrics
{
  private TrueTypeFont font;
  private double ascent;
  private double descent;
  private double leading;
  private double xHeight;
  private double strikethroughPosition;

  private static final boolean preferMacSettings = false;

  private double overlinePosition;
  private double underlinePosition;

  public ScalableTrueTypeFontMetrics(final TrueTypeFont font)
          throws IOException
  {
    if (font == null)
    {
      throw new NullPointerException("The font must not be null");
    }
    this.font = font;
    final FontHeaderTable head = (FontHeaderTable) font.getTable(
            FontHeaderTable.TABLE_ID);
    final int unitsPerEm = head.getUnitsPerEm();
    // prefer the mac table, as at least for the old version of Arial
    // I use, the mac table is consistent with the Java-Font-Metrics
    if (preferMacSettings)
    {
      final HorizontalHeaderTable hhea = (HorizontalHeaderTable) font.getTable(
              HorizontalHeaderTable.TABLE_ID);
      if (hhea == null)
      {
        final OS2Table table = (OS2Table) font.getTable(OS2Table.TABLE_ID);
        if (table != null)
        {
          computeWindowsMetrics(table, unitsPerEm);
        }
        else
        {
          createFallbackMetrics();
        }
      }
      else
      {
        createMacMetrics(hhea, unitsPerEm);
      }
    }
    else
    {
      final OS2Table table = (OS2Table) font.getTable(OS2Table.TABLE_ID);
      if (table == null)
      {
        final HorizontalHeaderTable hhea = (HorizontalHeaderTable) font.getTable(
                HorizontalHeaderTable.TABLE_ID);
        if (hhea != null)
        {
          createMacMetrics(hhea, unitsPerEm);
        }
        else
        {
          createFallbackMetrics();
        }
      }
      else
      {
        computeWindowsMetrics(table, unitsPerEm);
      }
    }
    font.dispose();
  }

  private void createMacMetrics(final HorizontalHeaderTable hhea,
                                final double unitsPerEm)
  {
    this.ascent = (hhea.getAscender()) / unitsPerEm;
    this.descent = (hhea.getDescender()) / unitsPerEm;
    this.leading = (hhea.getLineGap()) / unitsPerEm;
    this.xHeight = ascent *
            (LibFontsDefaults.DEFAULT_XHEIGHT_SIZE / LibFontsDefaults.DEFAULT_ASCENT_SIZE);
    this.strikethroughPosition = this.xHeight * LibFontsDefaults.DEFAULT_STRIKETHROUGH_POSITION;
  }

  private void createFallbackMetrics()
  {
    this.ascent = LibFontsDefaults.DEFAULT_ASCENT_SIZE;
    this.descent = LibFontsDefaults.DEFAULT_DESCENT_SIZE;
    this.xHeight = LibFontsDefaults.DEFAULT_XHEIGHT_SIZE;
    this.strikethroughPosition = this.xHeight * LibFontsDefaults.DEFAULT_STRIKETHROUGH_POSITION;
    this.leading = 0;
  }

  private void computeWindowsMetrics(final OS2Table table,
                                     final double unitsPerEm)
  {
    int ascentRaw = table.getTypoAscender();
    if (ascentRaw == 0)
    {
      // ok, fall back ..
      this.ascent = ((table.getWinAscent()) / unitsPerEm);
    }
    else
    {
      this.ascent = (ascentRaw) / unitsPerEm;
    }

    int descentRaw = table.getTypoDescender();
    if (descentRaw == 0)
    {
      // ok, fall back ..
      // the windows Descent is always positive, so make it negative.
      this.descent = -(table.getWinDescent() / unitsPerEm);
    }
    else
    {
      this.descent = descentRaw / unitsPerEm;
    }

    int leadingRaw = table.getTypoLineGap();
    // leading can be zero
    this.leading = leadingRaw / unitsPerEm;

    short xHeightRaw = table.getxHeight();
    if (xHeightRaw == 0)
    {
      // assume that an x is half as high as the total font size
      this.xHeight = ascent *
           (LibFontsDefaults.DEFAULT_XHEIGHT_SIZE / LibFontsDefaults.DEFAULT_ASCENT_SIZE);
    }
    else
    {
      this.xHeight = (xHeightRaw) / unitsPerEm;
    }

    short strikethroughPosition = table.getyStrikeoutPosition();
    if (strikethroughPosition == 0)
    {
      this.strikethroughPosition = this.xHeight * LibFontsDefaults.DEFAULT_STRIKETHROUGH_POSITION;
    }
    else
    {
      this.strikethroughPosition = strikethroughPosition / unitsPerEm;
    }
  }

  /**
   * From the baseline to the
   *
   * @return
   */
  public double getAscent()
  {
    return ascent;
  }

  public double getDescent()
  {
    return descent;
  }

  public double getLeading()
  {
    return leading;
  }

  public double getXHeight()
  {
    return xHeight;
  }

  public double getOverlinePosition()
  {
    return overlinePosition;
  }

  public double getUnderlinePosition()
  {
    return underlinePosition;
  }

  public double getStrikeThroughPosition()
  {
    return strikethroughPosition;
  }

  public TrueTypeFont getFont()
  {
    return font;
  }
}
