/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * HSSFFontWrapper.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Heiko Evermann
 * Contributor(s):   Thomas Morgner; David Gilbert (for Simba Management Limited);
 *
 * $Id: HSSFFontWrapper.java,v 1.11 2003/06/27 14:25:25 taqua Exp $
 *
 * Changes
 * -------
 * 14-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.excel;

import java.awt.Color;

import com.jrefinery.report.targets.FontDefinition;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * The HSSFFontWrapper is used to store excel style font informations.
 *
 * @author Heiko Evermann
 */
public class HSSFFontWrapper
{
  /** scale between Excel and awt. With this value it looks fine. */
  public static final int FONT_FACTOR = 20;

  /** the font name. */
  private String fontName;

  /** the excel color index. */
  private short colorIndex;

  /** the font size. */
  private int fontHeight;

  /** the font's bold flag. */
  private boolean bold;

  /** the font's italic flag. */
  private boolean italic;

  /** the font's underline flag. */
  private boolean underline;

  /** the font's strikethrough flag. */
  private boolean strikethrough;

  /** the excel font. */
  private HSSFFont font;

  /**
   * Creates a new HSSFFontWrapper for the given font and color.
   *
   * @param font the wrapped font.
   * @param color the foreground color.
   */
  public HSSFFontWrapper(final FontDefinition font, final Color color)
  {
    final String fName = font.getFontName();
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

  /**
   * Creates a HSSFFontWrapper for the excel font.
   *
   * @param font the font.
   */
  public HSSFFontWrapper(final HSSFFont font)
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

  /**
   * Returns the excel font stored in this wrapper.
   *
   * @param workbook the workbook, that will be used to create the font.
   * @return the created font.
   */
  public HSSFFont getFont(final HSSFWorkbook workbook)
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

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param o the compared object.
   * @return true, if the font wrapper contains the same font definition, false otherwise.
   */
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof HSSFFontWrapper))
    {
      return false;
    }

    final HSSFFontWrapper wrapper = (HSSFFontWrapper) o;

    if (bold != wrapper.bold)
    {
      return false;
    }
    if (underline != wrapper.strikethrough)
    {
      return false;
    }
    if (strikethrough != wrapper.strikethrough)
    {
      return false;
    }
    if (colorIndex != wrapper.colorIndex)
    {
      return false;
    }
    if (fontHeight != wrapper.fontHeight)
    {
      return false;
    }
    if (italic != wrapper.italic)
    {
      return false;
    }
    if (!fontName.equals(wrapper.fontName))
    {
      return false;
    }

    return true;
  }

  /**
   * Returns a hash code value for the object. This method is
   * supported for the benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   *
   * @return the hash code.
   */
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
