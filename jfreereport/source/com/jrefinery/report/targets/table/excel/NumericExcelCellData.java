/**
 * Date: Jan 15, 2003
 * Time: 5:06:02 PM
 *
 * $Id: NumericExcelCellData.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.awt.geom.Rectangle2D;

public class NumericExcelCellData extends ExcelContentCellData
{
  private Number number;
  private String format;

  public NumericExcelCellData(Rectangle2D elementBounds,
                              ExcelDataCellStyle style,
                           Number value, String format)
  {
    super(elementBounds, style);
    this.number = value;
    this.format = format;
  }

  public void applyContent(HSSFCell cell)
  {
    if (number != null)
    {
      cell.setCellValue(number.doubleValue());
    }
  }

  public boolean isEmpty()
  {
    if (number == null)
      return true;
    else
      return false;
  }
}
