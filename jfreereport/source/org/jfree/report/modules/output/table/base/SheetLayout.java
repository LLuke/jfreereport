/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * TableLayout.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: SheetLayout.java,v 1.1 2004/03/16 15:43:41 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Feb 26, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jfree.report.modules.output.meta.MetaElement;

/**
 * The sheet layout is used to build the background map and to collect
 * the x- and y-cell-borders.
 */
public class SheetLayout
{
  private static final boolean UPPER_BOUNDS = true;
  private static final boolean LOWER_BOUNDS = false;

  /** A flag, defining whether to use strict layout mode. */
  private final boolean strict;

  /** The XBounds, all vertical cell boundaries (as CoordinateMappings). */
  private final TreeMap xBounds;

  /** The YBounds, all vertical cell boundaries (as CoordinateMappings). */
  private final TreeMap yBounds;

  /** Is a list of lists, contains the merged backgrounds ... */
  private GenericObjectTable backend;

  /** The right border of the grid. This is needed when not being in the strict mode. */
  private int xMaxBounds;

  private int yMaxBounds;

  private int rowCount;
  private int colCount;

  private transient Integer[] yKeysArray;
  private transient Integer[] xKeysArray;

  /**
   * Creates a new TableGrid-object. If strict mode is enabled, all cell bounds are
   * used to create the table grid, resulting in a more complex layout.
   *
   * @param strict the strict mode for the layout.
   */
  public SheetLayout(final boolean strict)
  {
    xBounds = new TreeMap();
    yBounds = new TreeMap();
    this.strict = strict;
    this.xMaxBounds = 0;
    this.yMaxBounds = 0;
    this.rowCount = 0;
    this.colCount = 0;
    this.backend = new GenericObjectTable();
  }

  /**
   * Adds the bounds of the given TableCellData to the grid. The bounds
   * given must be the same as the bounds of the element, or the layouting
   * might produce surprising results.
   *
   * @param element the position that should be added to the grid (might be null).
   * @throws NullPointerException if the bounds are null
   */
  public void add(final MetaElement element)
  {
    final Rectangle2D bounds = element.getBounds();

    // collect the bounds and add them to the xBounds and yBounds collection
    // if necessary...
    ensureXMapping((int) bounds.getX());
    ensureYMapping((int) bounds.getY());
    final int elementWidth = (int) (bounds.getWidth() + bounds.getX());
    final int elementHeight = (int) (bounds.getHeight() + bounds.getY());
    if (isStrict())
    {
      ensureXMapping(elementWidth);
      ensureYMapping(elementHeight);
    }

    if (element instanceof TableCellBackground)
    {
      final TableCellBackground background = (TableCellBackground) element;
      // now add the new element to the table ...
      final SortedMap ySet = yBounds.subMap(new Integer((int)bounds.getY()), new Integer(elementHeight + 1));
      final SortedMap xSet = xBounds.subMap(new Integer((int)bounds.getX()), new Integer(elementWidth + 1));

      final Object[] yKeys = ySet.keySet().toArray();
      final Object[] xKeys = xSet.keySet().toArray();

      // don't merge twice ...
      TableCellBackground savedOldBackground = null;
      TableCellBackground savedNewBackground = null;

      // we iterate over all rows ..
      for (int y = 0; y < yKeys.length; y++)
      {
        // get the index of the current row in the backend-table ...
        final Integer currentRowValue = (Integer) yBounds.get (yKeys[y]);
        final int currentRowIndex = currentRowValue.intValue();

        // for every row we iterate over all columns ...
        for (int x = 0; x < xKeys.length; x++)
        {
          // again get the column index for the backend table ...
          final Integer currentColumnValue = (Integer) xBounds.get (xKeys[x]);
          final int currentColumnIndex = currentColumnValue.intValue();

          // get the old background ... we will merge this one with the new ..
          final TableCellBackground oldBackground =
              (TableCellBackground) backend.getObject(currentRowIndex, currentColumnValue.intValue());
          if (oldBackground == null)
          {
            // hey, we have no old background, so no merging is necessary ...
            backend.setObject(currentRowIndex, currentColumnIndex, background);
          }
          // the background changed ... this means we have two elements occupying
          // the space of the new element ...
          else if (oldBackground != savedOldBackground)
          {
            // merge both backgrounds and start to add that background definition
            // to the backend table.
            savedOldBackground = oldBackground;
            savedNewBackground = oldBackground.merge(background);
            // we get a new instance if the merging worked ... or the old instance
            // if merging did not change anything ...
            if (savedNewBackground != oldBackground)
            {
              backend.setObject(currentRowIndex, currentColumnIndex, savedNewBackground);
            }
          }
          // the current background goes on .. replace all occurences of the
          // old background with the newly created one ..
          else if (oldBackground != savedNewBackground)
          {
            backend.setObject(currentRowIndex, currentColumnIndex, savedNewBackground);
          }
        }
      }
    }

    // finally update the collected maximums
    if (xMaxBounds < elementWidth)
    {
      xMaxBounds = elementWidth;
    }
    if (yMaxBounds < elementHeight)
    {
      yMaxBounds = elementHeight;
    }
  }

  private void ensureXMapping (final int coordinate)
  {
    final Integer key = new Integer (coordinate);
    if (xBounds.containsKey(key) == false)
    {
      final int result = colCount;
      xBounds.put(key, new Integer(result));
      colCount += 1;
    }
  }

  private void ensureYMapping (final int y)
  {
    final Integer key = new Integer (y);
    if (yBounds.containsKey(key) == false)
    {
      final int result = rowCount;
      yBounds.put(key, new Integer(result));
      rowCount += 1;
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

  protected GenericObjectTable getLayoutBackend ()
  {
    return backend;
  }

  public boolean isEmpty()
  {
    return ((backend.getColumnCount() == 0) && (backend.getRowCount() == 0));
  }

  public TableRectangle getTableBounds (final MetaElement e, TableRectangle rect)
  {
    if (rect == null)
    {
      rect = new TableRectangle();
    }
    final Rectangle2D bounds = e.getBounds();
    final int x1 = findXValue((int) bounds.getX(), LOWER_BOUNDS);
    final int y1 = findYValue((int) bounds.getY(), LOWER_BOUNDS);
    final int x2 = findXValue((int) (bounds.getX() + bounds.getWidth()), UPPER_BOUNDS);
    final int y2 = findYValue((int) (bounds.getY() + bounds.getHeight()), UPPER_BOUNDS);
    rect.setRect(x1, y1, x2, y2);
    return rect;
  }


  /**
   * Tries to find the cell position of the value <code>value</code>. If the position
   * was not found in the data array, then the position of the first element greater or
   * equal or the value is returned.
   *
   * @param value the value that is searched in the data array.
   * @param upperLimit set to true, if index of the first element greater or equal to
   * the given value is returned, else the first element lesser or equal the value is
   * returned.
   * @return the position of the value in the array or the next lower position.
   */
  private int findXValue(final int value, final boolean upperLimit)
  {
    final Integer[] cuts = getXCuts();
    final int pos = Arrays.binarySearch(cuts, new Integer(value));
    if (pos == cuts.length)
    {
      return xMaxBounds;
    }
    if (pos >= 0)
    {
      return cuts[pos].intValue();
    }
    else if (upperLimit)
    {
      return cuts[-pos - 1].intValue();
    }
    else
    {
      return cuts[-pos - 2].intValue();
    }
  }

  /**
   * Tries to find the cell position of the value <code>value</code>. If the position
   * was not found in the data array, then the position of the first element greater or
   * equal or the value is returned.
   *
   * @param value the value that is searched in the data array.
   * @param upperLimit set to true, if index of the first element greater or equal to
   * the given value is returned, else the first element lesser or equal the value is
   * returned.
   * @return the position of the value in the array or the next lower position.
   */
  private int findYValue(final int value, final boolean upperLimit)
  {
    final Integer[] cuts = getYCuts();
    final int pos = Arrays.binarySearch(cuts, new Integer(value));
    if (pos == cuts.length)
    {
      return yMaxBounds;
    }
    if (pos >= 0)
    {
      return cuts[pos].intValue();
    }
    else if (upperLimit)
    {
      return cuts[-pos - 1].intValue();
    }
    else
    {
      return cuts[-pos - 2].intValue();
    }
  }

  /**
   * Returns the vertical boundaries of the table cells. The array contains
   * the start positions of the cells. If this is a strict grid, this array
   * will also contain the cell end positions. In either case the cell end
   * of the last cell is returned. Do not modify the array, or funny things
   * may happen.
   *
   * @return the vertical start position of the table cells.
   */
  protected Integer[] getYCuts()
  {
    if (yKeysArray != null)
    {
      return yKeysArray;
    }

    final Integer yMaxKey = new Integer(yMaxBounds);
    final boolean isEndContained = yBounds.containsKey(yMaxKey);
    if (isEndContained)
    {
      yKeysArray = new Integer[yBounds.size()];
    }
    else
    {
      yKeysArray = new Integer[yBounds.size() + 1];
      yKeysArray[yKeysArray.length - 1] = yMaxKey;
    }

    yKeysArray = (Integer[]) yBounds.keySet().toArray(yKeysArray);
    return yKeysArray;
  }

  /**
   * Returns the horizontal boundaries of the table cells. The array contains
   * the start positions of the cells. Do not modify the array, or funny things
   * may happen.
   *
   * @return the horizontal start position of the table cells.
   */
  protected Integer[] getXCuts()
  {
    if (xKeysArray != null)
    {
      return xKeysArray;
    }
    if (xBounds.size() == 0)
    {
      return new Integer[0];
    }

    final Integer xMaxKey = new Integer(xMaxBounds);
    final boolean isEndContained = xBounds.containsKey(xMaxKey);
    if (isEndContained)
    {
      xKeysArray = new Integer[xBounds.size()];
    }
    else
    {
      xKeysArray = new Integer[xBounds.size() + 1];
      xKeysArray[xKeysArray.length - 1] = xMaxKey;
    }

    xKeysArray = (Integer[]) xBounds.keySet().toArray(xKeysArray);
    return xKeysArray;
  }

  /**
   * A Callback method to inform the sheet layout, that the current
   * page is complete, and no more content will be added.
   */
  public void pageCompleted ()
  {
  }

  /**
   * Returns the element at grid-position (x,y). This returns the
   * cell background for a certain cell, or null, if there is no
   * background at that cell.
   *
   * @param row
   * @param column
   * @return
   */
  public TableCellBackground getElementAt (final int row, final int column)
  {
    final Integer[] xCuts = getXCuts();
    final Integer[] yCuts = getYCuts();

    return (TableCellBackground) backend.getObject
            (yCuts[row].intValue(), xCuts[column].intValue());
  }

  /**
   * Computes the height of the given row.
   *
   * @param row the row, for which the height should be computed.
   * @return the height of the row.
   * @throws IndexOutOfBoundsException if the row is invalid.
   */
  public float getRowHeight (final int row)
  {
    final Integer[] yCuts = getYCuts();
    return yCuts[row+1].floatValue() - yCuts[row].floatValue();
  }

  /**
   * Computes the height of the given row.
   *
   * @param startCell the first cell in the range
   * @param endCell the last cell included in the cell range
   * @return the height of the row.
   * @throws IndexOutOfBoundsException if the row is invalid.
   */
  public float getCellWidth (final int startCell, final int endCell)
  {
    final Integer[] yCuts = getYCuts();
    return yCuts[startCell].floatValue() - yCuts[endCell].floatValue();
  }
}
