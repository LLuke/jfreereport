/**
 * Date: Jan 15, 2003
 * Time: 5:09:51 PM
 *
 * $Id: DateExcelCellData.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.awt.geom.Rectangle2D;
import java.util.Date;

public class DateExcelCellData extends ExcelContentCellData
{
  private Date date;
  private String format;

  public DateExcelCellData(Rectangle2D elementBounds,
                           ExcelDataCellStyle style,
                           Date value, String format)
  {
    super(elementBounds, style);
    this.date = value;
    this.format = format;
  }

  public void applyContent(HSSFCell cell)
  {
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
