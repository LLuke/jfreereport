/**
 * Date: Jan 14, 2003
 * Time: 2:53:00 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.excel;

import com.jrefinery.report.Element;

import java.awt.geom.Rectangle2D;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

public class ExcelCellData
{
  /** The text to be used for the cell */
  private String text;

  /** The position of the outer bounds of the cell */
  private Rectangle2D outerBounds;

  private HSSFCellStyle style;
  /**
   * @param element
   * @param elementBounds are calculated outside
   */
  public ExcelCellData(Element element, Rectangle2D elementBounds, HSSFCellStyle style)
  {
    this.text = String.valueOf(element.getValue());
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
    return "CellData: text=>>"
        + text
        + " outer bounds= "
        + outerBounds;
  }

  public String getText()
  {
    return text;
  }

  public Rectangle2D getBounds()
  {
    return outerBounds;
  }

  public HSSFCellStyle getStyle()
  {
    return style;
  }
}
