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
 * --------------
 * TableGrid.java
 * --------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableGrid.java,v 1.7 2003/10/10 17:16:26 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 *
 */

package org.jfree.report.modules.output.table.base;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.jfree.report.util.Log;

/**
 * The TableGrid is used to collect all table cells and to finally create the
 * TableGridLayout.
 * <p>
 * The TableGrid stores TableCellData elements and collects their boundaries.
 * The CellData-bounds are used to define the positions of the cells of the generated
 * table.
 * <p>
 * The TableGrid has two modes of operation. In the strict layoutmode, all bounds
 * of the cells are used to define the generated cells. The strict layout mode tries
 * to layout the cells in a way, that the generated results nearly equals the printed
 * layout.
 * <p>
 * If strict mode is disabled, only the origin of the TableCellData is used to
 * define the generated cells. This reduces the table complexity, the table appears
 * cleaner, unnecessary cell boundaries are removed. The layout of the table contents
 * can be slightly different to the printed results.
 * <p>
 * If you plan to print the generated file, then the strict layout is for you.
 * If you need to change the tables (edit contents f.i.), then the non-strict layout
 * is more useful for you.
 *
 * @see TableGridLayout
 *
 * @author Thomas Morgner
 *
 */
public class TableGrid
{
  /** The elements stored in the table grid. */
  private final ArrayList elements;

  /** The table grid bounds contain the precomputed table cell boundaries. */
  private final TableGridBounds bounds;

  /** The YBounds, all horizontal cell boundaries. */
  private final TreeSet yBounds;

  /** The lower boundary. */
  private int yBoundsMax;

  /**
   * Creates a new TableGrid for the pagination process.
   * If strict mode is enabled, all cell bounds are used to create the
   * table grid, resulting in a more complex layout.
   *
   * @param strict the strict mode for the layout.
   */
  public TableGrid(final boolean strict)
  {
    this.bounds = new TableGridBounds(strict);
    elements = new ArrayList();
    yBounds = new TreeSet();
    yBoundsMax = 0;
  }

  /**
   * Creates a table grid that uses precomputed bounds to generate
   * the table layout.
   *
   * @param bounds the precomputed bounds from the pagination process.
   */
  public TableGrid(final TableGridBounds bounds)
  {
    this.bounds = new TableGridBounds(bounds);
    elements = new ArrayList();
    yBounds = new TreeSet();
    yBoundsMax = 0;
  }

  /**
   * Adds a TableCellData to the grid.
   *
   * @param pos the position that should be added to the grid.
   * @throws NullPointerException if the given position is null
   */
  public void addData(final TableCellData pos)
  {
    bounds.addData(pos);
    elements.add(pos);
    // collect the bounds and add them to the xBounds and yBounds collection.
    final Rectangle2D bounds = pos.getBounds();
    final Integer y = new Integer((int) bounds.getY());
    yBounds.add(y);

    final int yWidth = (int) (bounds.getY() + bounds.getHeight());
    if (this.bounds.isStrict())
    {
      yBounds.add(new Integer(yWidth));
    }
    if (yBoundsMax < yWidth)
    {
      yBoundsMax = yWidth;
    }
  }

  /**
   * Create a TableGridLayout based on the contents of this table grid.
   *
   * @return the new TableGridLayout.
   */
  public TableGridLayout performLayout()
  {
    final TableCellData[] positions =
        (TableCellData[]) elements.toArray(new TableCellData[elements.size()]);

    // Log.debug ("Performing Layout for " + positions.length + " elements");
    final TableGridLayout layout = new TableGridLayout(bounds.getXCuts(), getYCuts(), positions);
    return layout;
  }

  /**
   * Removes all elements from the grid and removes all previously found bounds.
   */
  public void clear()
  {
    // bounds.clear();
    elements.clear();
    yBounds.clear();
  }

  /**
   * Returns the number of table cell data elements in this grid.
   *
   * @return the number of element in the grid.
   */
  public int size()
  {
    return elements.size();
  }

  /**
   * Returns the vertical boundaries of the table cells. The array contains
   * the start positions of the cells. If this is a strict grid, this array
   * will also contain the cell end positions. In either case the cell end
   * of the last cell is returned.
   *
   * @return the vertical start position of the table cells.
   */
  public int[] getYCuts()
  {
    boolean isEndContained = yBounds.contains(new Integer(yBoundsMax));
    final int[] yBoundsArray;
    if (isEndContained)
    {
      yBoundsArray = new int[yBounds.size()];
    }
    else
    {
      yBoundsArray = new int[yBounds.size() + 1];
      yBoundsArray[yBoundsArray.length - 1] = yBoundsMax;
    }

    final Iterator it = yBounds.iterator();
    int count = 0;
    while (it.hasNext())
    {
      final Integer i = (Integer) it.next();
      yBoundsArray[count] = i.intValue();
      count += 1;
    }

//    if (!bounds.isStrict())
//    {
//
//      return yBoundsArray;
//    }
//    // in strict mode, all boundaries are added. The last boundry does
//    // not define a start of a cell, so it is removed.
//
//    final int[] retval = new int[yBoundsArray.length - 1];
//    System.arraycopy(yBoundsArray, 0, retval, 0, retval.length);
    return yBoundsArray;
  }

}
