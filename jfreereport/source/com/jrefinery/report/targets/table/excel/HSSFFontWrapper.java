/**
 * Date: Jan 14, 2003
 * Time: 4:14:39 PM
 *
 * $Id: HSSFFontWrapper.java,v 1.1 2003/01/14 21:14:15 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import java.awt.Font;
import java.awt.Color;
import java.util.Map;
import java.util.Collection;
import java.util.Iterator;

import com.jrefinery.report.util.StringUtil;

public class HSSFFontWrapper
{
  /** scale between Excel and awt. With this value it looks fine. */
  public static final int FONT_FACTOR = 20;

  private String fontName;
  private short colorIndex;
  private int fontHeight;
  private boolean bold;
  private boolean italic;
  private HSSFFont font;

  public HSSFFontWrapper(Font font, Color color)
  {
    String fName = font.getName();
    if (isSansSerif(fName))
    {
      fontName = "Arial";
    }
    else if (isCourier(fName))
    {
      fontName = "Courier New";
    }
    else if (isSerif(fName))
    {
      fontName = "Times New Roman";
    }
    else
    {
      fontName = fName;
    }
    
    colorIndex = getNearestColor(color).getIndex();
    fontHeight = (short) (font.getSize());
    bold = font.isBold();
    italic = font.isItalic();
    this.font = null;
  }

  public HSSFFontWrapper(HSSFFont font)
  {
    fontName = font.getFontName();
    colorIndex = font.getColor();
    fontHeight = font.getFontHeightInPoints();
    italic = font.getItalic();
    bold = (font.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD);
    this.font = font;
  }

  public HSSFFont getFont (HSSFWorkbook workbook)
  {
    if (font == null)
    {
      font = workbook.createFont();
      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      font.setColor(colorIndex);
      font.setFontName(fontName);
      font.setFontHeightInPoints((short) fontHeight);
      font.setItalic(italic);
    }
    return font;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof HSSFFontWrapper)) return false;

    final HSSFFontWrapper wrapper = (HSSFFontWrapper) o;

    if (bold != wrapper.bold) return false;
    if (colorIndex != wrapper.colorIndex) return false;
    if (fontHeight != wrapper.fontHeight) return false;
    if (italic != wrapper.italic) return false;
    if (!fontName.equals(wrapper.fontName)) return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = fontName.hashCode();
    result = 29 * result + (int) colorIndex;
    result = 29 * result + fontHeight;
    result = 29 * result + (bold ? 1 : 0);
    result = 29 * result + (italic ? 1 : 0);
    return result;
  }

  /**
   * Find a suitable color for the cell
   */
  private static HSSFColor getNearestColor(Color awtColor)
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


  /**
   * Returns true if the logical font name is equivalent to 'Courier', and false otherwise.
   *
   * @param logicalName  the logical font name.
   *
   * @return true or false.
   */
  private boolean isCourier (String logicalName)
  {
     return (StringUtil.startsWithIgnoreCase(logicalName, "dialoginput")
        || StringUtil.startsWithIgnoreCase(logicalName, "monospaced"));
  }

  /**
   * Returns true if the logical font name is equivalent to 'Serif', and false otherwise.
   *
   * @param logicalName  the logical font name.
   *
   * @return true or false.
   */
  private boolean isSerif (String logicalName)
  {
    return (StringUtil.startsWithIgnoreCase(logicalName, "serif"));
  }

  /**
   * Returns true if the logical font name is equivalent to 'SansSerif', and false otherwise.
   *
   * @param logicalName  the logical font name.
   *
   * @return true or false.
   */
  private boolean isSansSerif (String logicalName)
  {
    return StringUtil.startsWithIgnoreCase(logicalName, "SansSerif")
        || StringUtil.startsWithIgnoreCase(logicalName, "Dialog");
  }
}
