/**
 * Date: Jan 15, 2003
 * Time: 5:06:02 PM
 *
 * $Id: NumericExcelCellData.java,v 1.1 2003/01/15 16:54:52 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.awt.geom.Rectangle2D;

import com.jrefinery.report.targets.table.excel.ExcelCellData;

public class NumericExcelCellData extends ExcelCellData
{
  private Number number;
  private String format;

  public NumericExcelCellData(Rectangle2D elementBounds,
                           HSSFCellStyle style,
                           Number value, String format)
  {
    super(elementBounds, style);
    this.number = value;
    this.format = format;
  }

  public void applyCell(HSSFCell cell)
  {
    cell.setCellStyle(getStyle());
    if (number != null)
      cell.setCellValue(number.doubleValue());
  }

  public boolean isEmpty()
  {
    if (number == null)
      return true;
    else
      return false;
  }
}
