/**
 * Date: Jan 25, 2003
 * Time: 1:43:19 PM
 *
 * $Id: ExcelToolLibrary.java,v 1.2 2003/01/28 22:05:31 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.util.Log;
import org.apache.poi.hssf.util.HSSFColor;

import java.awt.Color;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

public class ExcelToolLibrary
{
  private static Hashtable triplets;

  private static int abs(int i)
  {
    if (i < 0) return -i;
    return i;
  }

  private static long abs(long i)
  {
    if (i < 0) return -i;
    return i;
  }

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

        int deltaRG = abs(xlRG - cdRG);
        int deltaGB = abs(xlGB - cdGB);
        int deltaBR = abs(xlBR - cdBR);

        long delta = deltaBR + deltaGB + deltaRG;

        long excelColor = (delta << 24) +
            (rgb[0] << 16) +
            (rgb[1] << 8) +
            rgb[2];

        long diff = abs(colorValue - excelColor);

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

  public static void main(String[] args)
  {
    getNearestColor(new Color(0x007FFF));
  }
}
