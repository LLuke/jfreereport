/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * --------------
 * TableGrid.java
 * --------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableGrid.java,v 1.10 2003/03/13 17:43:08 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 *
 */

package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

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
  /** The XBounds, all vertical cell boundaries. */
  private TreeSet xBounds;

  /** The YBounds, all horizontal cell boundaries. */
  private TreeSet yBounds;

  /** The elements stored in the table grid. */
  private ArrayList elements;

  /** A flag, defining whether to use strict layout mode. */
  private boolean strict;

  /**
   * Creates a new TableGrid. If strict mode is enabled, all cell bounds are
   * used to create the table grid, resulting in a more complex layout.
   *
   * @param strict the strict mode for the layout.
   */
  public TableGrid(boolean strict)
  {
    xBounds = new TreeSet();
    yBounds = new TreeSet();
    elements = new ArrayList();
    this.strict = strict;
  }

  /**
   * Adds a TableCellData to the grid.
   *
   * @param pos the position that should be added to the grid.
   * @throws NullPointerException if the given position is null
   */
  public void addData(TableCellData pos)
  {
    if (pos == null)
    {
      throw new NullPointerException();
    }
    elements.add(pos);

    // collect the bounds and add them to the xBounds and yBounds collection.
    Rectangle2D bounds = pos.getBounds();
    Integer x = new Integer((int) bounds.getX());
    Integer y = new Integer((int) bounds.getY());
    xBounds.add(x);
    yBounds.add(y);

    if (isStrict())
    {
      Integer xW = new Integer((int) (bounds.getX() + bounds.getWidth()));
      Integer yW = new Integer((int) (bounds.getY() + bounds.getHeight()));
      xBounds.add(xW);
      yBounds.add(yW);
    }
  }

  /**
   * Gets the strict mode flag.
   *
   * @return true, if strict mode is enabled, false otherwise.
   */
  public boolean isStrict()
  {
    return strict;
  }

  /**
   * Create a TableGridLayout based on the contents of this table grid.
   *
   * @return the new TableGridLayout.
   */
  public TableGridLayout performLayout()
  {
    TableCellData[] positions =
        (TableCellData[]) elements.toArray(new TableCellData[elements.size()]);

    //Log.debug ("Performing Layout: " + positions.length);
    TableGridLayout layout = new TableGridLayout(getXCuts(), getYCuts(), positions);
    return layout;
  }

  /**
   * Removes all elements from the grid and removes all previously found bounds.
   */
  public void clear()
  {
    xBounds.clear();
    yBounds.clear();
    elements.clear();
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
   * Returns the horizontal boundaries of the table cells. The array contains
   * the start positions of the cells.
   *
   * @return the horizontal start position of the table cells.
   */
  public int[] getXCuts()
  {
    if (xBounds.size() == 0)
    {
      return new int[0];
    }
    int[] xBoundsArray = new int[xBounds.size()];
    Iterator it = xBounds.iterator();
    int count = 0;
    while (it.hasNext())
    {
      Integer i = (Integer) it.next();
      xBoundsArray[count] = i.intValue();
      count += 1;
    }

    if (!strict)
    {
      return xBoundsArray;
    }
    // in strict mode, all boundaries are added. The last boundry does
    // not define a start of a cell, so it is removed.

    int[] retval = new int[xBoundsArray.length - 1];
    System.arraycopy(xBoundsArray, 0, retval, 0, retval.length);
    return retval;
  }

  /**
   * Returns the vertical boundaries of the table cells. The array contains
   * the start positions of the cells.
   *
   * @return the vertical start position of the table cells.
   */
  public int[] getYCuts()
  {
    int[] yBoundsArray = new int[yBounds.size()];
    Iterator it = yBounds.iterator();
    int count = 0;
    while (it.hasNext())
    {
      Integer i = (Integer) it.next();
      yBoundsArray[count] = i.intValue();
      count += 1;
    }

    if (!strict)
    {
      return yBoundsArray;
    }
    // in strict mode, all boundaries are added. The last boundry does
    // not define a start of a cell, so it is removed.

    int[] retval = new int[yBoundsArray.length - 1];
    System.arraycopy(yBoundsArray, 0, retval, 0, retval.length);
    return retval;
  }
}
