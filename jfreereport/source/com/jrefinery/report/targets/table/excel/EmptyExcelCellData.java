/**
 * Date: Jan 25, 2003
 * Time: 2:31:15 PM
 *
 * $Id: EmptyExcelCellData.java,v 1.1 2003/01/25 20:38:33 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.awt.geom.Rectangle2D;

public class EmptyExcelCellData extends ExcelCellData
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
