/**
 * Date: Jan 14, 2003
 * Time: 2:53:00 PM
 *
 * $Id: ExcelCellData.java,v 1.1 2003/01/14 21:13:11 taqua Exp $
 */
package com.jrefinery.report.targets.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.awt.geom.Rectangle2D;

public abstract class ExcelCellData
{
  /** The position of the outer bounds of the cell */
  private Rectangle2D outerBounds;

  private HSSFCellStyle style;

  /**
   * @param elementBounds are calculated outside
   */
  public ExcelCellData(Rectangle2D elementBounds, HSSFCellStyle style)
  {
    if (elementBounds == null) throw new NullPointerException("OuterBounds is null");
    this.outerBounds = elementBounds;
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
        + outerBounds;
  }

  public Rectangle2D getBounds()
  {
    return outerBounds;
  }

  public HSSFCellStyle getStyle()
  {
    return style;
  }

  public abstract void applyCell (HSSFCell cell);

  public abstract boolean isEmpty ();

}
