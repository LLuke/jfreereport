/**
 * Date: Jan 14, 2003
 * Time: 4:14:39 PM
 *
 * $Id: HSSFFontWrapper.java,v 1.2 2003/01/25 02:47:10 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.StringUtil;
import com.jrefinery.report.util.Log;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.awt.Color;

public class HSSFFontWrapper
{
  /** scale between Excel and awt. With this value it looks fine. */
  public static final int FONT_FACTOR = 20;

  private String fontName;
  private short colorIndex;
  private int fontHeight;
  private boolean bold;
  private boolean italic;
  private boolean underline;
  private boolean strikethrough;
  private HSSFFont font;

  public HSSFFontWrapper(FontDefinition font, Color color)
  {
    Log.debug ("Added Font: " + font);
    String fName = font.getFontName();
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
    
    colorIndex = ExcelToolLibrary.getNearestColor(color).getIndex();
    fontHeight = (short) (font.getFontSize());
    bold = font.isBold();
    italic = font.isItalic();
    underline = font.isUnderline();
    strikethrough = font.isStrikeThrough();
    this.font = null;
  }

  public HSSFFontWrapper(HSSFFont font)
  {
    fontName = font.getFontName();
    colorIndex = font.getColor();
    fontHeight = font.getFontHeightInPoints();
    italic = font.getItalic();
    bold = (font.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD);
    underline = (font.getUnderline() != HSSFFont.U_NONE);
    strikethrough = font.getStrikeout();
    this.font = font;
  }

  public HSSFFont getFont (HSSFWorkbook workbook)
  {
    if (font == null)
    {
      font = workbook.createFont();
      if (bold)
      {
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      }
      else
      {
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
      }
      font.setColor(colorIndex);
      font.setFontName(fontName);
      font.setFontHeightInPoints((short) fontHeight);
      font.setItalic(italic);
      font.setStrikeout(strikethrough);
      if (underline)
      {
        font.setUnderline(HSSFFont.U_SINGLE);
      }
      else
      {
        font.setUnderline(HSSFFont.U_NONE);
      }
    }
    return font;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof HSSFFontWrapper)) return false;

    final HSSFFontWrapper wrapper = (HSSFFontWrapper) o;

    if (bold != wrapper.bold) return false;
    if (underline != wrapper.strikethrough) return false;
    if (strikethrough != wrapper.strikethrough) return false;
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
