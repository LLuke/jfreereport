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
 * $Id: SheetLayout.java,v 1.3 2005/01/25 00:12:35 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Feb 26, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.util.geom.StrictBounds;


/**
 * The sheet layout is used to build the background map and to collect the x- and
 * y-cell-borders.
 */
public class SheetLayout
{
  private static final boolean UPPER_BOUNDS = true;
  private static final boolean LOWER_BOUNDS = false;

  /**
   * Encapsulates X- or Y-Cuts. An auxilary CutObject can be removed on non-strict sets.
   */
  private static class BoundsCut
  {
    private int position;
    private boolean auxilary;

    public BoundsCut (final int position, final boolean auxilary)
    {
      this.position = position;
      this.auxilary = auxilary;
    }

    public boolean isAuxilary ()
    {
      return auxilary;
    }

    public int getPosition ()
    {
      return position;
    }


    public String toString ()
    {
      return "org.jfree.report.modules.output.table.base.SheetLayout.BoundsCut{" +
              "auxilary=" + auxilary +
              ", position=" + position +
              "}";
    }

    public void makePermanent ()
    {
      auxilary = false;
    }
  }

  /**
   * A flag, defining whether to use strict layout mode.
   */
  private final boolean strict;

  /**
   * The XBounds, all vertical cell boundaries (as CoordinateMappings).
   */
  private final TreeMap xBounds;

  /**
   * The YBounds, all vertical cell boundaries (as CoordinateMappings).
   */
  private final TreeMap yBounds;

  /**
   * Is a list of lists, contains the merged backgrounds ...
   */
  private GenericObjectTable backend;

  /**
   * The right border of the grid. This is needed when not being in the strict mode.
   */
  private long xMaxBounds;

  private long yMaxBounds;

  private int rowCount;
  private int colCount;

  private Long[] yKeysArray;
  private Long[] xKeysArray;
  private static final Long[] EMPTY_LONG_ARRAY = new Long[0];

  /**
   * Creates a new TableGrid-object. If strict mode is enabled, all cell bounds are used
   * to create the table grid, resulting in a more complex layout.
   *
   * @param strict the strict mode for the layout.
   */
  public SheetLayout (final boolean strict)
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
   * Adds the bounds of the given TableCellData to the grid. The bounds given must be the
   * same as the bounds of the element, or the layouting might produce surprising
   * results.
   *
   * @param element the position that should be added to the grid (might be null).
   * @throws NullPointerException if the bounds are null
   */
  public void add (final MetaElement element)
  {
    final StrictBounds bounds = element.getBounds();

    // collect the bounds and add them to the xBounds and yBounds collection
    // if necessary...
    ensureXMapping(bounds.getX(), false);
    ensureYMapping(bounds.getY(), false);

    final long elementWidth = (bounds.getWidth() + bounds.getX());
    final long elementHeight = (bounds.getHeight() + bounds.getY());
    final boolean isBackground = (element instanceof TableCellBackground);

    // an end cut is auxilary, if it is not a background and the layout is not strict
    final boolean aux = (isBackground == false) && (isStrict() == false);
    ensureXMapping(elementWidth, aux);
    ensureYMapping(elementHeight, aux);

    if (isBackground)
    {
      final TableCellBackground background = (TableCellBackground) element;
      // now add the new element to the table ...
      final SortedMap ySet = yBounds.subMap(new Long(bounds.getY()), new Long(elementHeight + 1));
      final SortedMap xSet = xBounds.subMap(new Long(bounds.getX()), new Long(elementWidth + 1));

      final Object[] yKeys = ySet.keySet().toArray();
      final Object[] xKeys = xSet.keySet().toArray();

      // don't merge twice ...
      TableCellBackground savedOldBackground = null;
      TableCellBackground savedNewBackground = null;

      // we iterate over all rows ..
      // the yCuts also contains the End-Bounds (y+height);
      // the EB's for the last element do not hold any content.
      for (int y = 0; y < yKeys.length - 1; y++)
      {
        // get the index of the current row in the backend-table ...
        final BoundsCut currentRowValue = (BoundsCut) yBounds.get(yKeys[y]);
        final int currentRowIndex = currentRowValue.getPosition();

        // for every row we iterate over all columns ...
        for (int x = 0; x < xKeys.length - 1; x++)
        {
          // again get the column index for the backend table ...
          final BoundsCut currentColumnValue = (BoundsCut) xBounds.get(xKeys[x]);
          final int currentColumnIndex = currentColumnValue.getPosition();

          // get the old background ... we will merge this one with the new ..
          final TableCellBackground oldBackground =
                  (TableCellBackground) backend.getObject(currentRowIndex, currentColumnIndex);
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
          // the current 'old' background continues .. replace all occurences of the
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

  private void ensureXMapping (final long coordinate, final boolean aux)
  {
    final Long key = new Long(coordinate);
    final BoundsCut cut = (BoundsCut) xBounds.get(key);
    if (cut == null)
    {
      final int result = colCount;
      xBounds.put(key, new BoundsCut(result, aux));
      colCount += 1;
      xKeysArray = null;
      // backend copy ...
      final int oldColumn = getPreviousColumn(coordinate);
      columnInserted(coordinate, oldColumn, result);
    }
    else if (cut.isAuxilary() && aux == false)
    {
      cut.makePermanent();
    }
  }

  protected void columnInserted (final long coordinate, final int oldColumn,
                                 final int newColumn)
  {
    if (oldColumn != -1)
    {
      // now copy all entries from old column to new column
      backend.copyColumn(oldColumn, newColumn);
    }
  }

  private int getPreviousColumn (final long coordinate)
  {
    // first, find the column preceding this coordinate
    final SortedMap map = xBounds.headMap(new Long(coordinate));
    if (map.isEmpty())
    {
      return -1;
    }
    final Object lastKey = map.lastKey();
    final BoundsCut cuts = (BoundsCut) map.get(lastKey);
    final int oldColumn = cuts.getPosition();
    return oldColumn;
  }

  protected void rowInserted (final long coordinate, final int oldRow, final int newRow)
  {
    // now copy all entries from old column to new column
    backend.copyRow(oldRow, newRow);
  }

  private int getPreviousRow (final long coordinate)
  {
    // first, find the column preceding this coordinate
    final SortedMap map = yBounds.headMap(new Long(coordinate));
    if (map.isEmpty())
    {
      return -1;
    }
    final Object lastKey = map.lastKey();
    final BoundsCut cuts = (BoundsCut) map.get(lastKey);
    final int oldRow = cuts.getPosition();
    return oldRow;
  }

  private void ensureYMapping (final long coordinate, final boolean aux)
  {
    final Long key = new Long(coordinate);
    final BoundsCut cut = (BoundsCut) yBounds.get(key);
    if (cut == null)
    {
      final int result = rowCount;
      yBounds.put(key, new BoundsCut(result, aux));
      yKeysArray = null;
      rowCount += 1;
      final int oldRow = getPreviousRow(coordinate);
      rowInserted(coordinate, oldRow, result);
    }
    else if (cut.isAuxilary() && aux == false)
    {
      cut.makePermanent();
    }
  }

  /**
   * Gets the strict mode flag.
   *
   * @return true, if strict mode is enabled, false otherwise.
   */
  public boolean isStrict ()
  {
    return strict;
  }

  protected GenericObjectTable getLayoutBackend ()
  {
    return backend;
  }

  public boolean isEmpty ()
  {
    return ((backend.getColumnCount() == 0) &&
            (backend.getRowCount() == 0) &&
            xMaxBounds == 0 &&
            yMaxBounds == 0);
  }

  /**
   * Returns the position of the given element within the table. The TableRectangle
   * contains row and cell indices, no layout coordinates.
   *
   * @param e    the element for which the table bounds should be found.
   * @param rect the returned rectangle or null, if a new instance should be created
   * @return the filled table rectangle.
   */
  public TableRectangle getTableBounds (final MetaElement e, TableRectangle rect)
  {
    if (rect == null)
    {
      rect = new TableRectangle();
    }
    final StrictBounds bounds = e.getBounds();
    final int x1 = findXPosition(bounds.getX(), LOWER_BOUNDS);
    final int y1 = findYPosition(bounds.getY(), LOWER_BOUNDS);
    final int x2 = findXPosition(bounds.getX() + bounds.getWidth(), UPPER_BOUNDS);
    final int y2 = findYPosition(bounds.getY() + bounds.getHeight(), UPPER_BOUNDS);
    rect.setRect(x1, y1, x2, y2);
    return rect;
  }

  protected int mapColumn (final int xCutIndex)
  {
    final Long[] xcuts = getXCuts();
    final BoundsCut boundsCut = (BoundsCut) xBounds.get(xcuts[xCutIndex]);
    return boundsCut.getPosition();
  }

  protected int mapRow (final int yCutIndex)
  {
    final Long[] ycuts = getYCuts();
    final BoundsCut boundsCut = (BoundsCut) yBounds.get(ycuts[yCutIndex]);
    return boundsCut.getPosition();
  }

  /**
   * Tries to find the cell position of the value <code>value</code>. If the position was
   * not found in the data array, then the position of the first element greater or equal
   * or the value is returned.
   *
   * @param coordinate the value that is searched in the data array.
   * @param upperLimit set to true, if index of the first element greater or equal to the
   *                   given value is returned, else the first element lesser or equal the
   *                   value is returned.
   * @return the position of the value in the array or the next lower position.
   */
  private int findXPosition (final long coordinate, final boolean upperLimit)
  {
    final Long[] cuts = getXCuts();

    final int pos = Arrays.binarySearch(cuts, new Long(coordinate));
    if (pos == cuts.length)
    {
      //return xMaxBounds;
      // warning: This might be stupid
      return cuts.length - 1;
    }
    if (pos >= 0)
    {
      return pos;
    }
    else if (upperLimit)
    {
      return (-pos - 1);
    }
    else
    {
      return (-pos - 2);
    }
  }

  /**
   * Tries to find the cell position of the value <code>value</code>. If the position was
   * not found in the data array, then the position of the first element greater or equal
   * or the value is returned.
   *
   * @param coordinate the value that is searched in the data array.
   * @param upperLimit set to true, if index of the first element greater or equal to the
   *                   given value is returned, else the first element lesser or equal the
   *                   value is returned.
   * @return the position of the value in the array or the next lower position.
   */
  private int findYPosition (final long coordinate, final boolean upperLimit)
  {
    final Long[] cuts = getYCuts();
    final int pos = Arrays.binarySearch(cuts, new Long(coordinate));
    if (pos == cuts.length)
    {
      return cuts.length - 1;
    }
    if (pos >= 0)
    {
      return pos;
    }
    else if (upperLimit)
    {
      return (-pos - 1);
    }
    else
    {
      return (-pos - 2);
    }
  }

  /**
   * Returns the vertical boundaries of the table cells. The array contains the start
   * positions of the cells. If this is a strict grid, this array will also contain the
   * cell end positions. In either case the cell end of the last cell is returned. Do not
   * modify the array, or funny things may happen.
   *
   * @return the vertical start position of the table cells.
   */
  protected Long[] getYCuts ()
  {
    if (yKeysArray != null)
    {
      return yKeysArray;
    }
    if (yBounds.size() == 0)
    {
      return EMPTY_LONG_ARRAY;
    }

    final Long yMaxKey = new Long(yMaxBounds);
    final boolean isEndContained = yBounds.containsKey(yMaxKey);
    if (isEndContained)
    {
      yKeysArray = new Long[yBounds.size()];
    }
    else
    {
      yKeysArray = new Long[yBounds.size() + 1];
    }

    yKeysArray = (Long[]) yBounds.keySet().toArray(yKeysArray);
    if (!isEndContained)
    {
      yKeysArray[yKeysArray.length - 1] = yMaxKey;
    }
    return yKeysArray;
  }

  /**
   * Returns the horizontal boundaries of the table cells. The array contains the start
   * positions of the cells. Do not modify the array, or funny things may happen.
   *
   * @return the horizontal start position of the table cells.
   */
  protected Long[] getXCuts ()
  {
    if (xKeysArray != null)
    {
      return xKeysArray;
    }
    if (xBounds.size() == 0)
    {
      return EMPTY_LONG_ARRAY;
    }

    final Long xMaxKey = new Long(xMaxBounds);
    final boolean isEndContained = xBounds.containsKey(xMaxKey);
    if (isEndContained)
    {
      xKeysArray = new Long[xBounds.size()];
    }
    else
    {
      xKeysArray = new Long[xBounds.size() + 1];
      xKeysArray[xKeysArray.length - 1] = xMaxKey;
    }

    xKeysArray = (Long[]) xBounds.keySet().toArray(xKeysArray);
    if (!isEndContained)
    {
      xKeysArray[xKeysArray.length - 1] = xMaxKey;
    }
    return xKeysArray;
  }

  /**
   * A Callback method to inform the sheet layout, that the current page is complete, and
   * no more content will be added.
   */
  public void pageCompleted ()
  {
    final Iterator itX = xBounds.entrySet().iterator();
    while (itX.hasNext())
    {
      final Map.Entry entry = (Map.Entry) itX.next();
      final BoundsCut cut = (BoundsCut) entry.getValue();
      if (cut.isAuxilary())
      {
        itX.remove();
      }
    }

    final Iterator itY = yBounds.entrySet().iterator();
    while (itY.hasNext())
    {
      final Map.Entry entry = (Map.Entry) itY.next();
      final BoundsCut cut = (BoundsCut) entry.getValue();
      if (cut.isAuxilary())
      {
        itY.remove();
      }
    }
  }

  /**
   * Returns the element at grid-position (x,y). This returns the cell background for a
   * certain cell, or null, if there is no background at that cell.
   *
   * @param row
   * @param column
   * @return
   */
  public TableCellBackground getElementAt (final int row, final int column)
  {
    return (TableCellBackground) backend.getObject(mapRow(row), mapColumn(column));
  }

  /**
   * Computes the height of the given row.
   *
   * @param row the row, for which the height should be computed.
   * @return the height of the row.
   *
   * @throws IndexOutOfBoundsException if the row is invalid.
   */
  public long getRowHeight (final int row)
  {
    if (row >= rowCount)
    {
      throw new IndexOutOfBoundsException
              ("Row " + row + " is invalid. Max rows is " + rowCount);
    }
    final Long[] yCuts = getYCuts();
    if (row + 1 < yCuts.length)
    {
      return yCuts[row + 1].longValue() - yCuts[row].longValue();
    }

    final Long lastElement = yCuts[yCuts.length - 1];
    if (lastElement.longValue() < yMaxBounds)
    {
      // yMaxBounds is not contained in the treeset, therefore we
      // can compute a valid height
      final long retval = yMaxBounds - yCuts[row].longValue();
      return retval;
    }

    throw new IndexOutOfBoundsException("RowHeight: " + (row + 1) + " >= " + yCuts.length);
  }

  /**
   * Computes the height of the given row.
   *
   * @param startCell the first cell in the range
   * @param endCell   the last cell included in the cell range
   * @return the height of the row.
   *
   * @throws IndexOutOfBoundsException if the row is invalid.
   */
  public long getCellWidth (final int startCell, final int endCell)
  {
    final Long[] xCuts = getXCuts();
    return xCuts[endCell].longValue() - xCuts[startCell].longValue();
  }

  /**
   * The current number of columns. Of course, this value begins to be reliable, once the
   * number of columns is known (that is at the end of the layouting process).
   *
   * @return the number columns.
   */
  public int getColumnCount ()
  {
    final Long[] xCuts = getXCuts();
    // do not include the EndOfTable marker
    return xCuts.length - 1;
  }

  /**
   * The current number of rows. Of course, this value begins to be reliable, once the
   * number of rows is known (that is at the end of the layouting process).
   *
   * @return the number columns.
   */
  public int getRowCount ()
  {
    final Long[] yCuts = getYCuts();
    // do not include the EndOfTable marker
    return yCuts.length - 1;
  }
}
