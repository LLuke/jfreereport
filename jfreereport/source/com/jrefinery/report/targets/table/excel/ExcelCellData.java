/**
 * Date: Jan 14, 2003
 * Time: 2:53:00 PM
 *
 * $Id: ExcelCellData.java,v 1.2 2003/01/25 20:34:12 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;

import org.apache.poi.hssf.usermodel.HSSFCell;

public abstract class ExcelCellData extends TableCellData
{
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
      throw new NullPointerException();

    this.style = style;
  }

  public abstract void applyContent (HSSFCell cell);

  public ExcelDataCellStyle getExcelCellStyle()
  {
    return style;
  }

  public boolean isBackground()
  {
    return false;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "CellData: " + getClass()
        + " outer bounds= "
        + getBounds();
  }

  /**
   * Tests, whether the cell is empty.
   *
   * @return true, if the cell is empty, false otherwise.
   */
  public abstract boolean isEmpty ();
}
