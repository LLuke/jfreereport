/**
 * Date: Jan 25, 2003
 * Time: 1:43:19 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.util.HSSFColor;

import java.awt.Color;
import java.util.Map;
import java.util.Collection;
import java.util.Iterator;

public class ExcelToolLibrary
{
  /**
   * Find a suitable color for the cell
   */
  public static HSSFColor getNearestColor(Color awtColor)
  {
    HSSFColor color = null;

    Map triplets = HSSFColor.getTripletHash();
    if (triplets != null)
    {
      Collection keys = triplets.keySet();
      if (keys != null && keys.size() > 0)
      {
        Object key = null;
        HSSFColor crtColor = null;
        short[] rgb = null;
        int diff = 0;
        int minDiff = 999;
        for(Iterator it = keys.iterator(); it.hasNext();)
        {
          key = it.next();

          crtColor = (HSSFColor)triplets.get(key);
          rgb = crtColor.getTriplet();

          diff = Math.abs(rgb[0] - awtColor.getRed()) +
            Math.abs(rgb[1] - awtColor.getGreen()) +
            Math.abs(rgb[2] - awtColor.getBlue());

          if (diff < minDiff)
          {
            minDiff = diff;
            color = crtColor;
          }
        }
      }
    }
    return color;
  }


}
