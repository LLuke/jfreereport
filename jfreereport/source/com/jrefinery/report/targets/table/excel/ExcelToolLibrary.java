/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * -------------------
 * ExcelToolLibrary.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Heiko Evermann
 * Contributor(s):   Thomas Morgner; David Gilbert (for Simba Management Limited);
 *
 * $Id: ExcelToolLibrary.java,v 1.3 2003/02/04 17:56:32 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.util.Log;
import org.apache.poi.hssf.util.HSSFColor;

import java.awt.Color;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * POI Excel utility methods.
 */
public class ExcelToolLibrary
{
  /** the pre-defined excel color triplets. */
  private static Hashtable triplets;

  /**
   * Find a suitable color for the cell.
   *
   * The algorithm searches all available triplets, weighted by tripletvalue and
   * trippletdifference to the other triplets. The color wins, which has the
   * smallest tripplet difference and where all tripplets are nearest to the
   * requested color. Damn, why couldn't these guys from microsoft implement
   * a real color system.
   */
  public static short getNearestColor(Color awtColor)
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

    Collection keys = triplets.keySet();
    if (keys != null && keys.size() > 0)
    {
      long minDiff = Long.MAX_VALUE;

      // get the color without the alpha chanel
      int colorValue = awtColor.getRGB() & 0x00ffffff;

      int cdRG = awtColor.getRed() - awtColor.getGreen();
      int cdGB = awtColor.getGreen() - awtColor.getBlue();
      int cdBR = awtColor.getBlue() - awtColor.getRed();

      Enumeration elements = triplets.elements();
      while (elements.hasMoreElements())
      {
        HSSFColor crtColor = (HSSFColor) elements.nextElement();
        short[] rgb = crtColor.getTriplet();

        int xlRG = rgb[0] - rgb[1];
        int xlGB = rgb[1] - rgb[2];
        int xlBR = rgb[2] - rgb[0];

        int deltaRG = Math.abs(xlRG - cdRG);
        int deltaGB = Math.abs(xlGB - cdGB);
        int deltaBR = Math.abs(xlBR - cdBR);

        long delta = deltaBR + deltaGB + deltaRG;

        long excelColor = (delta << 24) +
            (rgb[0] << 16) +
            (rgb[1] << 8) +
            rgb[2];

        long diff = Math.abs(colorValue - excelColor);

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
