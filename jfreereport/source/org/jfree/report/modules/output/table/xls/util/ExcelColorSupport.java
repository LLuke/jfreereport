/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ---------------------
 * ExcelToolLibrary.java
 * ---------------------
 * (C)opyright 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann
 * Contributor(s):   Thomas Morgner;
 *                   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ExcelColorSupport.java,v 1.2.2.1 2004/12/13 19:27:12 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 *
 */

package org.jfree.report.modules.output.table.xls.util;

import java.awt.Color;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.poi.hssf.util.HSSFColor;
import org.jfree.report.util.Log;

/**
 * POI Excel utility methods.
 *
 * @author Heiko Evermann
 */
public final class ExcelColorSupport
{
  /**
   * DefaultConstructor.
   *
   */
  private ExcelColorSupport()
  {
  }

  /** the pre-defined excel color triplets. */
  private static Hashtable triplets;

  /**
   * Find a suitable color for the cell.
   *
   * The algorithm searches all available triplets, weighted by tripletvalue and
   * tripletdifference to the other triplets. The color wins, which has the
   * smallest triplet difference and where all triplets are nearest to the
   * requested color. Damn, why couldn't these guys from microsoft implement
   * a real color system.
   *
   * @param awtColor the awt color that should be transformed into an Excel color.
   * @return the excel color index that is nearest to the supplied color.
   */
  public static short getNearestColor(final Color awtColor)
  {
    short color = HSSFColor.BLACK.index;

    if (triplets == null)
    {
      triplets = HSSFColor.getTripletHash();
    }

    if (triplets == null)
    {
      Log.warn("Unable to get triplet hashtable");
      return color;
    }

    final Collection keys = triplets.keySet();
    if (keys != null && keys.size() > 0)
    {
      long minDiff = Long.MAX_VALUE;

      // get the color without the alpha chanel
      final int colorValue = awtColor.getRGB() & 0x00ffffff;

      final int cdRG = awtColor.getRed() - awtColor.getGreen();
      final int cdGB = awtColor.getGreen() - awtColor.getBlue();
      final int cdBR = awtColor.getBlue() - awtColor.getRed();

      final Enumeration elements = triplets.elements();
      while (elements.hasMoreElements())
      {
        final HSSFColor crtColor = (HSSFColor) elements.nextElement();
        final short[] rgb = crtColor.getTriplet();

        final int xlRG = rgb[0] - rgb[1];
        final int xlGB = rgb[1] - rgb[2];
        final int xlBR = rgb[2] - rgb[0];

        final int deltaRG = Math.abs(xlRG - cdRG);
        final int deltaGB = Math.abs(xlGB - cdGB);
        final int deltaBR = Math.abs(xlBR - cdBR);

        final long delta = deltaBR + deltaGB + deltaRG;

        final long excelColor = (delta << 24) + (rgb[0] << 16) + (rgb[1] << 8) + rgb[2];

        final long diff = Math.abs(colorValue - excelColor);

        if (diff < minDiff)
        {
          minDiff = diff;
          if (minDiff == 0)
          {
            // we found the color ...
            return crtColor.getIndex();
          }
          color = crtColor.getIndex();
        }
      }
    }


    return color;
  }
}
