/**
 * Date: Jan 15, 2003
 * Time: 5:19:00 PM
 *
 * $Id: DefaultExcelCellData.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.awt.geom.Rectangle2D;

public class DefaultExcelCellData extends ExcelContentCellData
{
  private String value;

  public DefaultExcelCellData(Rectangle2D elementBounds, ExcelDataCellStyle style, String value)
  {
    super(elementBounds, style);
    this.value = value;
  }

  public void applyContent(HSSFCell cell)
  {
    cell.setCellValue(value);
  }

  public boolean isEmpty()
  {
    return (value == null || value.length() == 0);
  }
}
