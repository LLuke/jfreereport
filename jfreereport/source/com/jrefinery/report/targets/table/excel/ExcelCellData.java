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
 * ------------------
 * ExcelCellData.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Heiko Evermann;
 * Contributor(s):   Thomas Morgner, David Gilbert (for Simba Management Limited);
 *
 * $Id: ExcelCellData.java,v 1.7 2003/05/02 12:40:38 taqua Exp $
 *
 * Changes
 * -------
 * 14-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.excel;

import java.awt.geom.Rectangle2D;

import com.jrefinery.report.targets.table.TableCellData;
import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * Abstract base class for all Excel data cells.
 * <p>
 * The Excel cell style is used to define excel specific format
 * informations, such as Colors, FormatStrings, Fonts etc.
 * <p>
 * The cell implementation is responsible for updating the externaly
 * created HSSFCell, so that the format and the data applied to the
 * cell.
 *
 * @author Heiko Evermann.
 */
public abstract class ExcelCellData extends TableCellData
{
  /** holds the excel style information for the cell. */
  private ExcelDataCellStyle style;

  /**
   * Creates new ExcelCellData.
   *
   * @param elementBounds are calculated outside
   * @param style the assigned cell style.
   */
  public ExcelCellData(Rectangle2D elementBounds, ExcelDataCellStyle style)
  {
    super(elementBounds);
    if (style == null)
    {
      throw new NullPointerException();
    }
    this.style = style;
  }

  /**
   * Applies the cells content and formats to the given HSSFCell.
   *
   * @param cell the cell, that should be formated.
   */
  public abstract void applyContent(HSSFCell cell);

  /**
   * Gets the defined style for this cell.
   *
   * @return the defined style for this cell.
   */
  public ExcelDataCellStyle getExcelCellStyle()
  {
    return style;
  }

  /**
   * Returns always false, as this is a data cell.
   *
   * @return always false.
   */
  public final boolean isBackground()
  {
    return false;
  }

  /**
   * Returns a string representation of this cell.
   *
   * @see java.lang.Object#toString()
   * @return a string representation of the cell.
   */
  public String toString()
  {
    return "CellData: " + getClass()
        + " outer bounds= "
        + getBounds();
  }

  /**
   * Tests, whether the cell is empty. Empty cells may transport
   * a format, but they don't contain data.
   *
   * @return true, if the cell is empty, false otherwise.
   */
  public abstract boolean isEmpty();
}
