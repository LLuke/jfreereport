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
 * ExcelFontFactory.java
 * ---------------------
 * (C)opyright 2003, by Hawesko GmbH & Co KG
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG)
 * based on ideas and code from JRXlsExporter.java of JasperReports
 * Contributor(s):   -;
 *
 * $Id: ExcelFontFactory.java,v 1.3 2005/01/25 00:17:15 taqua Exp $
 *
 * Changes
 * -------
 * 06-Jan-2002 : initial version
 */
package org.jfree.report.modules.output.table.xls.util;

import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.report.modules.output.table.xls.HSSFFontWrapper;

/**
 * This class keeps track of all fonts that we have used so far in our Excel file.
 * <p/>
 * Excel fonts should never be created directly, as excel does not like the idea of having
 * too many font definitions.
 *
 * @author Heiko Evermann
 */
public class ExcelFontFactory
{
  /**
   * The list of fonts that we have used so far.
   */
  private HashMap fonts;

  /**
   * The workbook that is used to create the font.
   */
  private final HSSFWorkbook workbook;

  /**
   * Constructor for ExcelFontFactory.
   *
   * @param workbook the workbook.
   */
  public ExcelFontFactory (final HSSFWorkbook workbook)
  {
    this.fonts = new HashMap();
    this.workbook = workbook;
  }

  /**
   * Creates a HSSFFont. The created font is cached and reused later, if a similiar font
   * is requested.
   *
   * @param wrapper the font information that should be used to produce the excel font
   * @return the created or a cached HSSFFont instance
   */
  public HSSFFont getExcelFont (final HSSFFontWrapper wrapper)
  {
    if (fonts.containsKey(wrapper))
    {
      return (HSSFFont) fonts.get(wrapper);
    }

    // ok, we need a new one ...
    final HSSFFont excelFont = createFont(wrapper);
    fonts.put(wrapper, excelFont);
    return excelFont;
  }

  /**
   * Returns the excel font stored in this wrapper.
   *
   * @return the created font.
   */
  private HSSFFont createFont (final HSSFFontWrapper wrapper)
  {
    final HSSFFont font = workbook.createFont();
    if (wrapper.isBold())
    {
      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    }
    else
    {
      font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    }
    font.setColor(wrapper.getColorIndex());
    font.setFontName(wrapper.getFontName());
    font.setFontHeightInPoints((short) wrapper.getFontHeight());
    font.setItalic(wrapper.isItalic());
    font.setStrikeout(wrapper.isStrikethrough());
    if (wrapper.isUnderline())
    {
      font.setUnderline(HSSFFont.U_SINGLE);
    }
    else
    {
      font.setUnderline(HSSFFont.U_NONE);
    }
    return font;
  }

}
