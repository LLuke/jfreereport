/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -------------------------
 * NumericExcelCellData.java
 * -------------------------
 * (C)opyright 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann
 * Contributor(s):   Thomas Morgner; David Gilbert (for Simba Management Limited);
 *
 * $Id: NumericExcelCellData.java,v 1.1 2003/07/07 22:44:07 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.xls;

import java.awt.geom.Rectangle2D;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * A excel cell that stores a number.
 *
 * @author Heiko Evermann
 */
public class NumericExcelCellData extends ExcelCellData
{
  /** the number stored in this cell. */
  private Number number;

  /**
   * Creates a new NumericExcelCellData.
   *
   * @param elementBounds the bounds of the cell.
   * @param style the cell style.
   * @param value the number value.
   */
  public NumericExcelCellData(final Rectangle2D elementBounds,
                              final ExcelDataCellStyle style,
                              final Number value)
  {
    super(elementBounds, style);
    this.number = value;
  }

  /**
   * Applies the cells content and formats to the given HSSFCell.
   *
   * @param cell the cell, that should be formated.
   */
  public void applyContent(final HSSFCell cell)
  {
    if (number != null)
    {
      cell.setCellValue(number.doubleValue());
    }
  }

  /**
   * Tests, whether the cell is empty. Empty cells may transport
   * a format, but they don't contain data.
   *
   * @return true, if the cell is empty, false otherwise.
   */
  public boolean isEmpty()
  {
    if (number == null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
}
