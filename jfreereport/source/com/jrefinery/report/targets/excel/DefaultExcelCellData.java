/**
 * Date: Jan 15, 2003
 * Time: 5:19:00 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.awt.geom.Rectangle2D;

public class DefaultExcelCellData extends ExcelCellData
{
  private String value;

  public DefaultExcelCellData(Rectangle2D elementBounds, HSSFCellStyle style, String value)
  {
    super(elementBounds, style);
    this.value = value;
  }

  public void applyCell(HSSFCell cell)
  {
    cell.setCellStyle(getStyle());
    cell.setCellValue(value);
  }

  public boolean isEmpty()
  {
    return (value == null || value.length() == 0);
  }
}
