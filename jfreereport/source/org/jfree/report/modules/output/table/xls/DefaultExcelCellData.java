/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -------------------------
 * DefaultExcelCellData.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Heiko Evermann
 * Contributor(s):   Thomas Morgner;
 *                   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultExcelCellData.java,v 1.8 2003/06/29 16:59:29 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.xls;

import java.awt.geom.Rectangle2D;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * The DefaultExcelCellData stores generic text data. The text data in the excel sheet
 * is not formated in any way. This implementation is used for POI 1.5.1, later versions
 * should use specific formats for the excel cells.
 *
 * @author Heiko Evermann
 */
public class DefaultExcelCellData extends ExcelCellData
{
  /** the cell value. */
  private String value;

  /**
   * Creates new DefaultExcelCellData. The cell data is placed in the grid
   * on the given bounds. The given ExcelDataCellStyle is assigned with this
   * data cell.
   *
   * @param elementBounds  the element bounds within the grid.
   * @param style  the assigned cell style.
   * @param value  the value stored in the cell data object.
   */
  public DefaultExcelCellData(final Rectangle2D elementBounds, final ExcelDataCellStyle style, final String value)
  {
    super(elementBounds, style);
    this.value = value;
  }

  /**
   * Applies the cell data to the given Excel cell.
   *
   * @param cell  the generated excel cell, which should be filled with the data.
   */
  public void applyContent(final HSSFCell cell)
  {
    cell.setCellValue(value);
  }

  /**
   * Tests, whether this cell is empty. The cell is empty, if the content is either
   * null or has a lenght of 0.
   *
   * @return true, if the content is empty, false otherwise.
   */
  public boolean isEmpty()
  {
    return (value == null || value.length() == 0);
  }
}
