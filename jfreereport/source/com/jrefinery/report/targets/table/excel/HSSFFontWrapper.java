/**
 * Date: Jan 14, 2003
 * Time: 4:14:39 PM
 *
 * $Id: HSSFFontWrapper.java,v 1.5 2003/01/28 22:05:32 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.targets.FontDefinition;
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
    String fName = font.getFontName();
    if (font.isSansSerif())
    {
      fontName = "Arial";
    }
    else if (font.isCourier())
    {
      fontName = "Courier New";
    }
    else if (font.isSerif())
    {
      fontName = "Times New Roman";
    }
    else
    {
      fontName = fName;
    }
    
    colorIndex = ExcelToolLibrary.getNearestColor(color);
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
}
