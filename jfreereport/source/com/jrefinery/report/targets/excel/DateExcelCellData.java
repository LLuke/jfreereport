/**
 * Date: Jan 15, 2003
 * Time: 5:09:51 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.awt.geom.Rectangle2D;
import java.util.Date;

public class DateExcelCellData extends ExcelCellData
{
  private Date date;
  private String format;

  public DateExcelCellData(Rectangle2D elementBounds,
                           HSSFCellStyle style,
                           Date value, String format)
  {
    super(elementBounds, style);
    this.date = value;
    this.format = format;
  }

  public void applyCell(HSSFCell cell)
  {
    cell.setCellStyle(getStyle());
    cell.setCellValue(date);
  }

  public boolean isEmpty()
  {
    if (date == null)
      return true;
    else
      return false;
  }
}
