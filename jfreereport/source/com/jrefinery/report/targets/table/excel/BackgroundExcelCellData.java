/**
 * Date: Jan 25, 2003
 * Time: 10:38:11 AM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.excel;

import java.awt.geom.Rectangle2D;

public class BackgroundExcelCellData extends ExcelCellData
{
  private ExcelBackgroundCellStyle style;

  public BackgroundExcelCellData(Rectangle2D elementBounds, ExcelBackgroundCellStyle style)
  {
    super(elementBounds);
    this.style = style;
  }

  public boolean isEmpty()
  {
    return false;
  }

  public ExcelBackgroundCellStyle getStyle()
  {
    return style;
  }

  public boolean isBackground()
  {
    return true;
  }
}
