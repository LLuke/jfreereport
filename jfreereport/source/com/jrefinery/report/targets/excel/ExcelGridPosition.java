/**
 * Date: Jan 14, 2003
 * Time: 2:54:30 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.excel;

public class ExcelGridPosition
{
  private ExcelCellData element;
  private long width;
  private long height;
  private int colSpan;
  private int rowSpan;

  public ExcelGridPosition(
      ExcelCellData element,
      long width,
      long height,
      int colSpan,
      int rowSpan)
  {
    this.element = element;
    this.width = width;
    this.height = height;
    this.colSpan = colSpan;
    this.rowSpan = rowSpan;
  }

  public ExcelCellData getElement()
  {
    return element;
  }

  public long getWidth()
  {
    return width;
  }

  public long getHeight()
  {
    return height;
  }

  public int getColSpan()
  {
    return colSpan;
  }

  public int getRowSpan()
  {
    return rowSpan;
  }
}
