/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ----------------------
 * TableGridPosition.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableGridPosition.java,v 1.7 2003/02/12 21:17:18 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial Version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;

/**
 * The TableGridPositition is used to bind the layouted table position to an
 * TableCellData object.
 * 
 * @author Thomas Morgner
 */
public final class TableGridPosition
{
  /** the TableCellData element stored in this position */
  private TableCellData element;
  
  /** the number of columns spanned by this cell */
  private int colSpan;
  
  /** the number of rows spanned by this cell. */
  private int rowSpan;
  
  /** the column, where the cell starts */
  private int col;
  
  /** the row, where the cell starts */
  private int row;

  /**
   * this flag is set by the Layouter to mark a cell as invalid
   * A invalid cell must not be painted, (it was placed in a cell
   * already populated by an other table grid position).
   * but f.i in html it is important for the layout that these cells
   * are not ignored. They should be printed as empty cells.
   */
  private boolean invalidCell;

  /**
   * Creates a new TableGridPosition with the given element as bulk load.
   *
   * @param element the encapsulated element.
   * @param col the x position within the layouted grid
   * @param row the y position within the layouted grid
   * @param colSpan the width in columns within the layouted grid
   * @param rowSpan the height in rows within the layouted grid
   */
  public TableGridPosition(TableCellData element, int col, int row, int colSpan, int rowSpan)
  {
    if (element == null)
    {
      throw new NullPointerException();
    }
    this.element = element;
    this.col = col;
    this.row = row;
    this.rowSpan = rowSpan;
    this.colSpan = colSpan;
    this.invalidCell = false;
  }

  /**
   * Returns the TableCellData element stored in this grid position.
   *
   * @return the element contained in the grid position.
   */
  public TableCellData getElement()
  {
    return element;
  }

  /**
   * Returns the colspan for the cell.
   *
   * @return the cell colspan.
   */
  public int getColSpan()
  {
    return colSpan;
  }

  /**
   * Returns the rowspan for the cell.
   *
   * @return the cell rowspan.
   */
  public int getRowSpan()
  {
    return rowSpan;
  }

  /**
   * Returns the column of this cell.
   *
   * @return the column of the cell within the grid.
   */
  public int getCol()
  {
    return col;
  }

  /**
   * Returns the row of this cell.
   *
   * @return the row of the cell within the grid.
   */
  public int getRow()
  {
    return row;
  }

  /**
   * Returns the bounds of the contained element.
   *
   * @return the bounds of the contained element.
   */
  public Rectangle2D getBounds()
  {
    return element.getBounds();
  }

  /**
   * Tests, whether a given table grid position is fully contained in the position.
   *
   * @param pos the compared position
   * @return true, if the position is contained in this cells area, false otherwise.
   */
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
      //Log.debug ("Contains ColSpan: " + (col + colSpan) + " -> " + (pos.getCol() 
      //           + pos.getColSpan()));
      return false;
    }

    if ((row + rowSpan) < (pos.getRow() + pos.getRowSpan()))
    {
      //Log.debug ("Contains RowSpan: " + (row + rowSpan) + " -> " + (pos.getRow() 
      //           + pos.getRowSpan()));
      return false;
    }

    return true;
  }

  /**
   * Tests, whether the given coordinates are the origin of this table grid position.
   *
   * @param x the compared column
   * @param y the compated row
   * @return true, if row and column are the same as this grid positions row and column.
   */
  public boolean isOrigin (int x, int y)
  {
    return (x == col) && (y == row);
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that
   * "textually represents" this object.
   *
   * @return  a string representation of the object.
   */
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

  /**
   * Tests, wether the layoutmanager declared this cell as invalid. Invalid cells
   * tried to use grid cells which were already occupied.
   *
   * @return true, if the cell is invalid and is contained in the grid for
   * informational reasons.
   */
  public boolean isInvalidCell()
  {
    return invalidCell;
  }

  /**
   * Defines the invalid cell state.
   *
   * @param invalidCell true, if this cell should be invalid, false otherwise.
   */
  public void setInvalidCell(boolean invalidCell)
  {
    this.invalidCell = invalidCell;
  }
}
