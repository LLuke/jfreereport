/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * --------------------
 * TableGridLayout.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableGridLayout.java,v 1.1 2003/07/07 22:44:07 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package org.jfree.report.modules.output.table.base;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The table grid layout is used to layout the collected TableCellData object from
 * the TableGrid into the table. The cells position is calculated by comparing the cell
 * bounds with the collected x and y-cuts.
 *
 * @author Thomas Morgner
 */
public class TableGridLayout
{
  /**
   * The Element class encapsulates all TableCellData-object within a single grid cell.
   * When the report is badly designed, cells may overlay. This will cause trouble when
   * the table is printed later, so we do not accept multiple non-background
   * TableCellData objects.
   * <p>
   * TableCellData backgrounds can be combined to create complex
   * background structures. Usually, the cell backgrounds must be merged before the
   * cellbackground can be applied to the generated table. As this is dependent
   * on the table implementation, we do not assume anything here, and just collect all
   * backgrounds in the list.
   *
   * @see TableCellData#isBackground
   */
  public static class Element
  {
    /** The root position, a data carrying TableCellData. */
    private TableGridPosition root;
    /** The backgrounds for the data cell. */
    private ArrayList backGrounds;

    /**
     * Creates a new element. The element has no initial background or
     * cell data.
     */
    public Element()
    {
      backGrounds = new ArrayList();
    }

    /**
     * Adds a new TableGridPosition to the element.
     * <p>
     * If the new position contains a data cell: <br>
     * if no data cell has been set so far, the cell is added as new
     * data element. if there is a data cell already set, the new cell data
     * is ignored.
     * <p>
     * If the new position contains a background information: <br>
     * if no data cell has been set so far, the cell is added to the list of
     * available backgrounds. When a new data cell is added, the backgrounds
     * are validated. A cell background is valid, if it fully contains the
     * data cell area. <br>
     * If there is a data cell already set, the background is validated imediatly
     * and added if valid.
     * <p>
     * Invalid backgrounds or duplicate data cells are discarded.
     *
     * @param pos the new TableGridPosition to be added to this element.
     * @throws NullPointerException if the given position is null
     */
    public void add(final TableGridPosition pos)
    {
      if (pos == null)
      {
        throw new NullPointerException();
      }

      if (root == null)
      {
        if (pos.getElement().isBackground())
        {
          backGrounds.add(0, pos);
          //Log.debug ("backGrounds -> added " + backGrounds.size());
        }
        else
        {
          root = pos;
          final Iterator it = backGrounds.iterator();
          while (it.hasNext())
          {
            final TableGridPosition gpos = (TableGridPosition) it.next();
            if (gpos.contains(root) == false)
            {
              it.remove();
            }
          }
        }
      }
      else
      {
        if (pos.getElement().isBackground())
        {
          if (pos.contains(root))
          {
            backGrounds.add(0, pos);
          }
        }
        else
        {
          /*
          Log.warn (new Log.SimpleMessage("Root already added: " , pos.getElement().getBounds()));
          Log.warn (new Log.SimpleMessage("+            added: " , root.getElement().getBounds()));
          Log.warn (new Log.SimpleMessage("+            added: " , pos.getElement().debugChunk));
          Log.warn (new Log.SimpleMessage("+            added: Col=" , new Integer(root.getCol()),
                                                            "  Row=" , new Integer(root.getRow())));
          */
          pos.setInvalidCell(true);
        }
      }
    }

    /**
     * Returns the list of backgrounds of this element.
     *
     * @return the collected backgrounds of the element.
     */
    public List getBackground()
    {
      return backGrounds;
    }

    /**
     * Returns the data cell for this element. This can be null if no data cell
     * has been defined.
     *
     * @return the data cell or null, if this element does not have any data defined.
     */
    public TableGridPosition getRoot()
    {
      return root;
    }

    /**
     * Creates a string representation of this Element.
     *
     * @return a string representation of the element for debugging purposes.
     */
    public String toString()
    {
      final StringBuffer buffer = new StringBuffer();
      buffer.append("TableGridLayout.Element={root=");
      buffer.append(root);
      buffer.append(", backgrounds=");
      buffer.append(backGrounds);
      buffer.append("}");
      return buffer.toString();
    }
  }

  /** The table data as table. */
  private Object data[][];
  /** the collected xcuts, sorted. */
  private int[] xCuts;
  /** the collected ycuts, sorted. */
  private int[] yCuts;
  /** the maximum horizontal position the table ever reached. */
  private int maxX;
  /** the maximum vertical position the table ever reached. */
  private int maxY;

  /**
   * Creates a new TableGridLayout.
   *
   * @param pxCuts the collected horizontal cell bounds from the TableGrid,.
   * @param pyCuts the collected vertical cell bounds from the TableGrid.
   * @param positions the positions collected by the table grid.
   */
  public TableGridLayout(final int[] pxCuts, final int[] pyCuts, final TableCellData[] positions)
  {
    this.xCuts = new int[pxCuts.length];
    this.yCuts = new int[pyCuts.length];

    System.arraycopy(pxCuts, 0, xCuts, 0, pxCuts.length);
    System.arraycopy(pyCuts, 0, yCuts, 0, pyCuts.length);

    Arrays.sort(xCuts);
    Arrays.sort(yCuts);

    // +1 for outer boundry ...
    final int width = xCuts.length;
    final int height = yCuts.length;
/*
    Log.info ("Created GridLayout with " + width + ", " + height);

    for (int i = 0; i < xCuts.length; i++)
    {
      Log.info ("X-Cuts: " + xCuts[i]);
    }
*/
    data = new Object[width][height];

    for (int i = 0; i < positions.length; i++)
    {
      final TableCellData pos = positions[i];
      add(pos);
    }
  }

  /**
   * Adds the table cell data position into the table grid. The coordinates are
   * calculated by using the sorted x- and y-cuts.
   *
   * @param pos the new position that should be added into the grid
   */
  protected void add(final TableCellData pos)
  {
    final Rectangle2D bounds = pos.getBounds();

    final int maxBoundsX = (int) (bounds.getX() + bounds.getWidth());
    final int maxBoundsY = (int) (bounds.getY() + bounds.getHeight());

    final int col = findBoundary(xCuts, (int) bounds.getX());
    final int row = findBoundary(yCuts, (int) bounds.getY());
    final int colspan = Math.max(1, findBoundary(xCuts, maxBoundsX, true) - col);
    final int rowspan = Math.max(1, findBoundary(yCuts, maxBoundsY, true) - row);
    final TableGridPosition gPos = new TableGridPosition(pos, col, row, colspan, rowspan);

/*
    Log.info ("AddTablePos: Col=" + gPos.getCol() +
              "; Row= " + gPos.getRow() +
              "; ColSpan=" + gPos.getColSpan() +
              "; RowSpan=" + gPos.getRowSpan() + "; -> " + pos.debugChunk + " Bounds: "+ bounds);
*/
/*
    if (pos instanceof TableBandArea)
    {
      Log.debug ("DebugChunk: " + pos.debugChunk);
      Log.debug ("gPos.getCol: " + gPos.getCol() + " -> " + getColumnStart(gPos.getCol()));
      Log.debug ("gPos.getRow: " + gPos.getRow() + " -> " + getRowStart(gPos.getRow()));
      Log.debug ("gPos.getColSpan: " + gPos.getColSpan() + " -> "
                 + getColumnEnd(gPos.getColSpan() + gPos.getCol() - 1));
      Log.debug ("gPos.getRowSpan: " + gPos.getRowSpan() + " -> "
                 + getRowEnd(gPos.getRowSpan() + gPos.getRow() - 1));
    }
*/
    final int startY = gPos.getRow();
    final int endY = gPos.getRow() + gPos.getRowSpan();
    // calculated the x and y position in the table, now add it to the element.
    for (int posY = startY; posY < endY; posY++)
    {
      final int startX = gPos.getCol();
      final int endX = gPos.getCol() + gPos.getColSpan();
      for (int posX = startX; posX < endX; posX++)
      {
        addToGrid(posX, posY, gPos);
      }
    }

    this.maxX = Math.max(this.maxX, maxBoundsX);
    this.maxY = Math.max(this.maxY, maxBoundsY);
  }

  /**
   * Adds the gridposition into the table, positionated at the cell (posX, posY).
   *
   * @param posX the x position within the tablegrid.
   * @param posY the y position within the tablegrid.
   * @param gPos the TableGridPosition that should be added to the table.
   *
   * @throws IndexOutOfBoundsException if posX or posY are invalid.
   * @throws NullPointerException if the given table grid position is invalid
   */
  protected void addToGrid(final int posX, final int posY, final TableGridPosition gPos)
  {
    if (gPos == null)
    {
      throw new NullPointerException();
    }

    if (posX >= getWidth() || posX < 0)
    {
      throw new IndexOutOfBoundsException("X: " + posX + " > " + getWidth());
    }
    if (posY >= getHeight() || posY < 0)
    {
      throw new IndexOutOfBoundsException("Y: " + posY + " > " + getHeight());
    }
    final Object o = data[posX][posY];
    if (o == null)
    {
      final Element e = new Element();
      e.add(gPos);
      data[posX][posY] = e;

    }
    else
    {
      final Element e = (Element) o;
      e.add(gPos);
    }
  }

  /**
   * Returns the element located in the specified cell.
   *
   * @param x the table column
   * @param y the table row
   * @return the element, or null, if there is no element defined.
   */
  public Element getData(final int x, final int y)
  {
    return (Element) data[x][y];
  }

  /**
   * Returns the number of columns of the table.
   *
   * @return the width of the table, the number of columns.
   */
  public int getWidth()
  {
    return xCuts.length;
  }

  /**
   * Returns the number of rows of the table.
   *
   * @return the height of the table, the number of rows.
   */
  public int getHeight()
  {
    return yCuts.length;
  }

  /**
   * Returns the start position of the given column in points.
   *
   * @param column the column
   * @return the position of the column in points
   */
  public int getColumnStart(final int column)
  {
    return xCuts[column];
  }

  /**
   * Returns the start position of the given row in points.
   *
   * @param row the row
   * @return the position of the row in points
   */
  public int getRowStart(final int row)
  {
    return yCuts[row];
  }

  /**
   * Returns the end position of the given column in points.
   *
   * @param column the column
   * @return the end position of the column in points
   */
  public int getColumnEnd(final int column)
  {
    if (column == xCuts.length - 1)
    {
      return maxX;
    }
    else
    {
      return getColumnStart(column + 1);
    }
  }

  /**
   * Returns the start position of the given row in points.
   *
   * @param row the row
   * @return the end position of the column in points
   */
  public int getRowEnd(final int row)
  {
    if (row == yCuts.length - 1)
    {
      return maxY;
    }
    else
    {
      return getRowStart(row + 1);
    }
  }

  /**
   * Tries to find the cell position of the value <code>value</code>. If the position
   * was not found in the data array, then the next lower position is returned.
   *
   * @param data the data where to find the value
   * @param value the value that is searched in the data array.
   * @return the position of the value in the array or the next lower position.
   */
  private static int findBoundary(final int[] data, final int value)
  {
    return findBoundary(data, value, false);
  }

  /**
   * Tries to find the cell position of the value <code>value</code>. If the position
   * was not found in the data array, then the position of the first element greater or
   * equal or the value is returned.
   *
   * @param data the data where to find the value
   * @param value the value that is searched in the data array.
   * @param upperLimit set to true, if index of the first element greater or equal to
   * the given value is returned, else the first element lesser or equal the value is
   * returned.
   * @return the position of the value in the array or the next lower position.
   */
  private static int findBoundary(final int[] data, final int value, final boolean upperLimit)
  {
    for (int i = 0; i < data.length; i++)
    {
      final int dV = data[i];
      if (dV == value)
      {
        return i;
      }
      if (dV > value)
      {
        if (i == 0)
        {
          return 0;
        }
        else
        {
          if (upperLimit)
          {
            return i;
          }
          else
          {
            return i - 1;
          }
        }
      }
    }
    return data.length;
  }
}
