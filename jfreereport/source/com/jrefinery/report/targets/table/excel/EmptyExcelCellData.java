/**
 * Date: Jan 25, 2003
 * Time: 2:31:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.awt.geom.Rectangle2D;

public class EmptyExcelCellData extends ExcelContentCellData
{
  public EmptyExcelCellData(Rectangle2D elementBounds, ExcelDataCellStyle style)
  {
    super(elementBounds, style);
  }

  public void applyContent(HSSFCell cell)
  {
  }

  public boolean isEmpty()
  {
    return true;
  }
}
