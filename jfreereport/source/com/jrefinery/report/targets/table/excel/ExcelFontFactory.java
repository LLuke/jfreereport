/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * $Id: ExcelFontFactory.java,v 1.7 2003/05/02 12:40:39 taqua Exp $
 *
 * Changes
 * -------
 * 06-Jan-2002 : initial version
 */
package com.jrefinery.report.targets.table.excel;

import java.awt.Color;
import java.util.ArrayList;

import com.jrefinery.report.targets.FontDefinition;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This class keeps track of all fonts that we have used so far in our Excel file.
 * 
 * @author Heiko Evermann
 */
public class ExcelFontFactory
{
  /** The list of fonts that we have used so far. */
  private ArrayList fontList = null;

  /** The workbook that is used to create the font. */
  private HSSFWorkbook workbook;

  /**
   * Constructor for ExcelFontFactory.
   * 
   * @param workbook  the workbook.
   */
  public ExcelFontFactory(HSSFWorkbook workbook)
  {
    fontList = new ArrayList();
    this.workbook = workbook;
  }

  /**
   * Creates a HSSFFont. The created font is cached and reused later,
   * if a similiar font is requested.
   *
   * @param font the font definition.
   * @param forecolor the font color
   * @return the created or a cached HSSFFont
   */
  public HSSFFont getExcelFont(FontDefinition font, Color forecolor)
  {
    HSSFFontWrapper wrapper = new HSSFFontWrapper(font, forecolor);
    if (fontList.contains(wrapper))
    {
      HSSFFontWrapper cached = (HSSFFontWrapper) fontList.get(fontList.indexOf(wrapper));
      if (cached != null)
      {
        return cached.getFont(workbook);
      }
    }

    // ok, we need a new one ...
    HSSFFont excelFont = wrapper.getFont(workbook);
    fontList.add(excelFont);
    return excelFont;
  }
 
}
