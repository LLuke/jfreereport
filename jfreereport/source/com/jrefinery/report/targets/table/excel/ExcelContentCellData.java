/**
 * Date: Jan 25, 2003
 * Time: 1:52:39 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.awt.geom.Rectangle2D;

public abstract class ExcelContentCellData extends ExcelCellData
{
  private ExcelDataCellStyle style;

  public ExcelContentCellData(Rectangle2D elementBounds, ExcelDataCellStyle style)
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
}
