/**
 * Date: Jan 14, 2003
 * Time: 2:53:00 PM
 *
 * $Id: ExcelCellData.java,v 1.2 2003/01/15 16:54:10 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.awt.geom.Rectangle2D;

import com.jrefinery.report.targets.table.TableCellData;

public abstract class ExcelCellData extends TableCellData
{
  private HSSFCellStyle style;

  /**
   * @param elementBounds are calculated outside
   */
  public ExcelCellData(Rectangle2D elementBounds, HSSFCellStyle style)
  {
    super(elementBounds);

    if (style == null) throw new NullPointerException("Style is null");
    this.style = style;
  }


  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "CellData: " + getClass()
        + " outer bounds= "
        + getBounds();
  }

  public HSSFCellStyle getStyle()
  {
    return style;
  }

  public abstract void applyCell (HSSFCell cell);

  public abstract boolean isEmpty ();
}
