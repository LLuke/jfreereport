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
 * ------------------------------
 * TableGridBounds.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableGridBounds.java,v 1.2 2003/08/20 17:24:34 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 12-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * The table grid bounds are used to collect the cell boundaries during
 * the repagination process and to recall these boundries later during the
 * content creation process.
 * 
 * @author Thomas Morgner
 */
public final class TableGridBounds
{
  /** A flag, defining whether to use strict layout mode. */
  private boolean strict;

  /** The XBounds, all vertical cell boundaries. */
  private TreeSet xBounds;

  /**
   * Creates a new TableGrid-object. If strict mode is enabled, all cell bounds are
   * used to create the table grid, resulting in a more complex layout.
   *
   * @param strict the strict mode for the layout.
   */
  public TableGridBounds(final boolean strict)
  {
    xBounds = new TreeSet();
    this.strict = strict;
  }

  /**
   * Creates a new TableGrid-object and reuses the grid bounds from the given
   * copy object. 
   *
   * @param copy the precomputed table grid bounds.
   */
  public TableGridBounds(final TableGridBounds copy)
  {
    xBounds = new TreeSet(copy.xBounds);
    this.strict = copy.strict;
  }

  /**
   * Adds the bounds of the given TableCellData to the grid.
   *
   * @param pos the position that should be added to the grid.
   * @throws NullPointerException if the given position is null
   */
  public void addData (TableCellData pos)
  {
    if (pos == null)
    {
      throw new NullPointerException();
    }

    // collect the bounds and add them to the xBounds and yBounds collection.
    final Rectangle2D bounds = pos.getBounds();
    final Integer x = new Integer((int) bounds.getX());
    xBounds.add(x);

    if (isStrict())
    {
      final Integer xW = new Integer((int) (bounds.getX() + bounds.getWidth()));
      xBounds.add(xW);
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
   * Removes all elements from the grid and removes all previously found bounds.
   */
  public void clear()
  {
    xBounds.clear();
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
    final int[] xBoundsArray = new int[xBounds.size()];
    final Iterator it = xBounds.iterator();
    int count = 0;
    while (it.hasNext())
    {
      final Integer i = (Integer) it.next();
      xBoundsArray[count] = i.intValue();
      count += 1;
    }

    if (!strict)
    {
      return xBoundsArray;
    }
    // in strict mode, all boundaries are added. The last boundry does
    // not define a start of a cell, so it is removed.

    final int[] retval = new int[xBoundsArray.length - 1];
    System.arraycopy(xBoundsArray, 0, retval, 0, retval.length);
    return retval;
  }

}
