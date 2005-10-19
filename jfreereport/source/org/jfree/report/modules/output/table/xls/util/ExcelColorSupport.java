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
 * $Id: ExcelColorSupport.java,v 1.5 2005/08/08 15:36:35 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 *
 */

package org.jfree.report.modules.output.table.xls.util;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.poi.hssf.util.HSSFColor;
import org.jfree.util.Log;

/**
 * POI Excel utility methods.
 *
 * @author Heiko Evermann
 */
public final class ExcelColorSupport
{
  /**
   * DefaultConstructor.
   */
  private ExcelColorSupport ()
  {
  }

  /**
   * the pre-defined excel color triplets.
   */
  private static Hashtable triplets;

  /**
   * Find a suitable color for the cell.
   * <p/>
   * The algorithm searches all available triplets, weighted by tripletvalue and
   * tripletdifference to the other triplets. The color wins, which has the smallest
   * triplet difference and where all triplets are nearest to the requested color. Damn,
   * why couldn't these guys from microsoft implement a real color system.
   *
   * @param awtColor the awt color that should be transformed into an Excel color.
   * @return the excel color index that is nearest to the supplied color.
   */
  public static short getNearestColor (final Color awtColor)
  {
    if (triplets == null)
    {
      triplets = HSSFColor.getTripletHash();
    }


    if (triplets == null || triplets.isEmpty())
    {
      Log.warn("Unable to get triplet hashtable");
      return HSSFColor.BLACK.index;
    }

    short color = HSSFColor.BLACK.index;
    double minDiff = Double.MAX_VALUE;

    // get the color without the alpha chanel
    final float[] hsb = Color.RGBtoHSB
            (awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), null);

    float[] excelHsb = null;
    final Enumeration elements = triplets.elements();
    while (elements.hasMoreElements())
    {
      final HSSFColor crtColor = (HSSFColor) elements.nextElement();
      final short[] rgb = crtColor.getTriplet();
      excelHsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], excelHsb);

      final double weight = 3d * Math.abs(excelHsb[0] - hsb[0]) +
              Math.abs(excelHsb[2] - hsb[2]) +
              Math.abs(excelHsb[2] - hsb[2]);

      if (weight < minDiff)
      {
        minDiff = weight;
        if (minDiff == 0)
        {
          // we found the color ...
          return crtColor.getIndex();
        }
        color = crtColor.getIndex();
      }
    }
    return color;
  }
}
