/**
 * Date: Jan 18, 2003
 * Time: 7:23:00 PM
 *
 * $Id: TableGridPosition.java,v 1.4 2003/01/29 18:37:13 taqua Exp $
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;

public final class TableGridPosition
{
  private TableCellData element;
  private int colSpan;
  private int rowSpan;
  private int col;
  private int row;

  // this flag is set by the Layouter to mark a cell as invalid
  // A invalid cell must not be painted, (it was placed in a cell
  // already populated by an other table grid position).
  // but f.i in html it is important for the layout that these cells
  // are not ignored. They should be printed as empty cells.
  private boolean invalidCell;

  public TableGridPosition(TableCellData element)
  {
    if (element == null) throw new NullPointerException();
    this.element = element;
    this.colSpan = -1;
    this.rowSpan = -1;
    this.invalidCell = false;
  }

  public TableCellData getElement()
  {
    return element;
  }

  public void setColSpan(int colSpan)
  {
    this.colSpan = colSpan;
  }

  public void setRowSpan(int rowSpan)
  {
    this.rowSpan = rowSpan;
  }

  public int getColSpan()
  {
    return colSpan;
  }

  public int getRowSpan()
  {
    return rowSpan;
  }

  public int getCol()
  {
    return col;
  }

  public void setCol(int col)
  {
    this.col = col;
  }

  public int getRow()
  {
    return row;
  }

  public void setRow(int row)
  {
    this.row = row;
  }

  public Rectangle2D getBounds()
  {
    return element.getBounds();
  }

  public boolean contains (TableGridPosition pos)
  {
    if (row > pos.getRow())
    {
      //Log.debug ("Contains Row: " + row + " -> " + pos.getRow());
      return false;
    }
    if (col > pos.getCol())
    {
      //Log.debug ("Contains Column: " + col + " -> " + pos.getCol());
      return false;
    }

    if ((col + colSpan) < (pos.getCol() + pos.getColSpan()))
    {
      //Log.debug ("Contains ColSpan: " + (col + colSpan) + " -> " + (pos.getCol() + pos.getColSpan()));
      return false;
    }

    if ((row + rowSpan) < (pos.getRow() + pos.getRowSpan()))
    {
      //Log.debug ("Contains RowSpan: " + (row + rowSpan) + " -> " + (pos.getRow() + pos.getRowSpan()));
      return false;
    }

    return true;
  }

  public boolean isOrigin (int x, int y)
  {
    return (x == col) && (y == row);
  }

  public String toString ()
  {
    StringBuffer buffer  = new StringBuffer();
    buffer.append("TableGridPosition={col=");
    buffer.append(getCol());
    buffer.append("; row=");
    buffer.append(getRow());
    buffer.append("; colspan=");
    buffer.append(getColSpan());
    buffer.append("; rowspan=");
    buffer.append(getRowSpan());
    buffer.append("; element=");
    buffer.append(getElement());
    buffer.append("}");
    return buffer.toString();
  }

  public boolean isInvalidCell()
  {
    return invalidCell;
  }

  public void setInvalidCell(boolean invalidCell)
  {
    this.invalidCell = invalidCell;
  }
}
