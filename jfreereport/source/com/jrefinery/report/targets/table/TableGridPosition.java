/**
 * Date: Jan 18, 2003
 * Time: 7:23:00 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table;

public class TableGridPosition
{
  private TableCellData element;
  private long width;
  private long height;
  private int colSpan;
  private int rowSpan;

  public TableGridPosition(
      TableCellData element,
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

  public TableCellData getElement()
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
