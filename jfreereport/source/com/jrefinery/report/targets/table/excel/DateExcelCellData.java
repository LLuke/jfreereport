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
 * DateExcelCellData.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Heiko Evermann
 * Contributor(s):   Thomas Morgner;David Gilbert (for Simba Management Limited);
 *
 * $Id: DateExcelCellData.java,v 1.4 2003/02/19 22:14:09 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.awt.geom.Rectangle2D;
import java.util.Date;

/**
 * A Date excel cell.
 */
public class DateExcelCellData extends ExcelCellData
{
  /** the date stored in this cell */
  private Date date;
  /** the date format */
  private String format;

  /**
   * Creates a new DateExcelCellData.
   *
   * @param elementBounds the bounds of the cell.
   * @param style the cell style.
   * @param value the date value.
   * @param format the date format string.
   */
  public DateExcelCellData(Rectangle2D elementBounds,
                           ExcelDataCellStyle style,
                           Date value, String format)
  {
    super(elementBounds, style);
    this.date = value;
    this.format = format;
  }

  /**
   * Applies the cells content and formats to the given HSSFCell.
   *
   * @param cell the cell, that should be formated.
   */
  public void applyContent(HSSFCell cell)
  {
    cell.setCellValue(date);
  }

  /**
   * Tests, whether the cell is empty. Empty cells may transport
   * a format, but they don't contain data.
   *
   * @return true, if the cell is empty, false otherwise.
   */
  public boolean isEmpty()
  {
    if (date == null)
      return true;
    else
      return false;
  }
}
