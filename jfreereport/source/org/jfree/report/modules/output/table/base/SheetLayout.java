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
 * $Id: SheetLayout.java,v 1.8 2005/04/09 17:43:13 taqua Exp $
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
import java.util.ArrayList;
import java.awt.Color;

import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.util.Log;
import org.jfree.report.util.InstanceID;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.content.EmptyContent;


/**
 * The sheet layout is used to build the background map and to collect the x- and
 * y-cell-borders.
 */
public class SheetLayout
{
  /**
   * How backgrounds for cells get computed
   * --------------------------------------
   *
   * JFreeReport handles 4 background types:
   *
   * Bands
   * -----
   * Bands are no real backgrounds, as they do not influence the output,
   * but they will be used to simplify the computation.
   *
   * Rectangles
   * ----------
   * Define the cell background (fill = true) and all 4 borders of a cell
   * (draw == true).
   *
   * Horizontal & Vertical Lines
   * ----------------
   * These lines define the Top or Left border or a cell. Bottom and right
   * borders get mapped into the Top/Left borders of the next cells.
   *
   * Joining
   * -------
   * When a background element is added, JFreeReport first checks, whether
   * a background has been already defined for that cell position. If not,
   * the given background is used as is.
   *
   * If there is a background defined, a merge operation starts. JFreeReport
   * will try to join both background definitions.
   * (This is done while adding the TableCellBackground to the SheetLayout)
   *
   * New Elements overwrite old elements. That means if there are two
   * conflicting borders or backgrounds at a given position, any old border
   * or background will be replaced as soon as a more current value appears.
   *
   * Lines, which make up the bottom most or right most borders, are held in
   * a zero-width column or zero-height row. These columns are always there,
   * if there is at least one background reaching to right or bottom of the report.
   * A flag indicates, whether these cells are significant. (for validity this
   * flag should mirror the result of an test "All Cells in these Row/Column are
   * empty".
   *
   * These lines are not mapped into bottom cell lines, as the resulting
   * merge would not be predictable and would depend on the order of the
   * split operations. A predictable merge implementation would be by far
   * more complex than this 'hack'.
   */

  /** An internal flag indicating that the upper or left bounds should be used. */
  private static final boolean UPPER_BOUNDS = true;
  private static final boolean LOWER_BOUNDS = false;

  /**
   * Encapsulates X- or Y-Cuts. An auxilary CutObject can be removed on non-strict sets.
   */
  private static class BoundsCut
  {
    private long coordinate;
    private int position;
    private boolean auxilary;

    public BoundsCut (final long coordinate, final int position, final boolean auxilary)
    {
      this.coordinate = coordinate;
      this.position = position;
      this.auxilary = auxilary;
    }

    public long getCoordinate ()
    {
      return coordinate;
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

  public static class CellReference
  {
    private StrictBounds bounds;
    private InstanceID contentID;

    public CellReference (final StrictBounds bounds)
    {
      this.contentID = new InstanceID();
      this.bounds = (StrictBounds) bounds.clone();
    }

    public StrictBounds getBounds ()
    {
      return bounds;
    }

    public InstanceID getContentID ()
    {
      return contentID;
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
  /** Contains the references to the original data passed into this layouter. */
  private GenericObjectTable objectIdTable;

  /** A flag indicating whether the last column holds a line definition. */
  private boolean lastColumnCutIsSignificant;
  /** A flag indicating whether the last row holds a line definition. */
  private boolean lastRowCutIsSignificant;

  /**
   * The right border of the grid. This is needed when not being in the strict mode.
   */
  private long xMaxBounds;
  private long yMaxBounds;
  private Long xMaxBoundsKey;
  private Long yMaxBoundsKey;
  private transient StrictBounds workBounds;

  private Long[] yKeysArray;
  private Long[] xKeysArray;
  private static final Long[] EMPTY_LONG_ARRAY = new Long[0];
  private static final Long ZERO = new Long(0);
  /**
   * Creates a new TableGrid-object. If strict mode is enabled, all cell bounds are used
   * to create the table grid, resulting in a more complex layout.
   *
   * @param strict the strict mode for the layout.
   */
  public SheetLayout (final boolean strict)
  {
    Log.debug ("Layout is strict");
    xBounds = new TreeMap();
    yBounds = new TreeMap();
    this.strict = strict;
    this.xMaxBounds = 0;
    this.yMaxBounds = 0;
    this.yMaxBoundsKey = ZERO;
    this.xMaxBoundsKey = ZERO;
    this.backend = new GenericObjectTable();
    this.objectIdTable = new GenericObjectTable();
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

    final long elementRightX = (bounds.getWidth() + bounds.getX());
    final long elementBottomY = (bounds.getHeight() + bounds.getY());
    final boolean isBackground = (element instanceof TableCellBackground);

    // an end cut is auxilary, if it is not a background and the layout is not strict
    final boolean aux = (isBackground == false) && (isStrict() == false);
    ensureXMapping(elementRightX, aux);
    ensureYMapping(elementBottomY, aux);

    // update the collected maximums
    if (xMaxBounds < elementRightX)
    {
      xMaxBounds = elementRightX;
      xMaxBoundsKey = new Long(xMaxBounds);
    }
    if (yMaxBounds < elementBottomY)
    {
      yMaxBounds = elementBottomY;
      yMaxBoundsKey = new Long(yMaxBounds);
    }

    // now add the new element to the table ...
    // the +1 makes sure, that we include the right and bottom element borders in the set
    final SortedMap ySet = yBounds.subMap(new Long(bounds.getY()), new Long(elementBottomY + 1));
    final SortedMap xSet = xBounds.subMap(new Long(bounds.getX()), new Long(elementRightX + 1));

    final Map.Entry[] yKeys = (Map.Entry[]) ySet.entrySet().toArray(new Map.Entry[ySet.size()]);
    final Map.Entry[] xKeys = (Map.Entry[]) xSet.entrySet().toArray(new Map.Entry[xSet.size()]);

    if (isBackground)
    {
      final TableCellBackground background = (TableCellBackground) element;

      if (yKeys.length == 1)
      {
        processHorizontalLine (yKeys[0], xKeys, background);
      }
      else if (xKeys.length == 1)
      {
        processVerticalLine(yKeys, xKeys[0], background);
      }
      else
      {
        // this does nothing for yLength == 1 && xLength == 1
        processAreaBackground(yKeys, xKeys, background);
      }
    }
    else if (isContent(element))
    {
      // mark cells as occupied ..
      if (isCellAreaOccupied(yKeys, xKeys) == false)
      {
        final CellReference cellReference = new CellReference(bounds);
        for (int x = 0; x < xKeys.length - 1; x++)
        {
          final BoundsCut mappedX = (BoundsCut) xKeys[x].getValue();
          final int posX = mappedX.getPosition();
          for (int y = 0; y < yKeys.length - 1; y++)
          {
            final BoundsCut mappedY = (BoundsCut) yKeys[y].getValue();
            final int posY = mappedY.getPosition();
            objectIdTable.setObject(posY, posX, cellReference);
          }
        }
      }
    }
  }

  protected boolean isContent (final MetaElement element)
  {
    return element.getContent() instanceof EmptyContent == false;
  }

  public TableCellBackground getRegionBackground (final TableRectangle rect)
  {
    final int mappedX1 = mapColumn(rect.getX1());
    final int mappedY1 = mapRow(rect.getY1());
    final TableCellBackground bgTopLeft = (TableCellBackground) backend.getObject(mappedY1, mappedX1);
    if (bgTopLeft == null)
    {
      return null;
    }
    if (rect.getRowSpan() == 1 && rect.getColumnSpan() == 1)
    {
      return bgTopLeft;
    }
    final int mappedX2 = mapColumn(rect.getX2() - 1);
    final int mappedY2 = mapRow(rect.getY2() - 1);
    final TableCellBackground bgBottomRight = (TableCellBackground) backend.getObject(mappedY2, mappedX2);
    if (bgBottomRight == null)
    {
      return null;
    }

    final Long[] xCuts = getXCuts();
    final Long[] yCuts = getYCuts();
    final long x = xCuts[rect.getX1()].longValue();
    final long y = yCuts[rect.getY1()].longValue();
    final long width = xCuts[rect.getX2()].longValue() - x;
    final long height = yCuts[rect.getY2()].longValue() - y;
    workBounds.setRect(x, y, width, height);

    final TableCellBackground retval = bgTopLeft.createMergedInstance(workBounds);
    if (isVerticalBorderValid(rect, mappedX1, true))
    {
      retval.setBorderLeft(bgTopLeft.getColorLeft(), bgTopLeft.getBorderSizeLeft());
    }
    else
    {
      retval.setBorderLeft(null, 0);
    }
    if (isVerticalBorderValid(rect, mappedX2, false))
    {
      retval.setBorderRight(bgBottomRight.getColorRight(), bgBottomRight.getBorderSizeRight());
    }
    else
    {
      retval.setBorderRight(null, 0);
    }

    if (isHorizontalBorderValid(rect, mappedY1, true))
    {
      retval.setBorderTop(bgTopLeft.getColorTop(), bgTopLeft.getBorderSizeTop());
    }
    else
    {
      retval.setBorderTop(null, 0);
    }
    if (isHorizontalBorderValid(rect, mappedY2, false))
    {
      retval.setBorderBottom(bgBottomRight.getColorBottom(), bgBottomRight.getBorderSizeBottom());
    }
    else
    {
      retval.setBorderBottom(null, 0);
    }
    return retval;
  }

  private boolean isVerticalBorderValid (final TableRectangle rect,
                                         final int mappedX,
                                         final boolean left)
  {
    Color color = null;
    float border = 0;
    for (int row = rect.getY1(); row < rect.getY2(); row++)
    {
      final int mappedRow = mapRow(row);
      final TableCellBackground background =
              (TableCellBackground) backend.getObject(mappedRow, mappedX);
      if (background == null)
      {
        // this is definitly unexpected ..
        Log.warn ("Possible inconsistency: " + rect);
        return false;
      }
      if (left)
      {
        if (color == null)
        {
          color = background.getColorLeft();
          border = background.getBorderSizeLeft();
        }
        else if (color.equals(background.getColorLeft()) == false ||
                (border != background.getBorderSizeLeft()))
        {
          return false;
        }
      }
      else
      {
        if (color == null)
        {
          color = background.getColorRight();
          border = background.getBorderSizeRight();
        }
        else if (color.equals(background.getColorRight()) == false ||
                (border != background.getBorderSizeRight()))
        {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isHorizontalBorderValid (final TableRectangle rect,
                                         final int mappedY,
                                         final boolean top)
  {
    Color color = null;
    float border = 0;
    for (int col = rect.getX1(); col < rect.getX2(); col++)
    {
      final int mappedCol = mapColumn(col);
      final TableCellBackground background =
              (TableCellBackground) backend.getObject(mappedY, mappedCol);
      if (background == null)
      {
        // this is definitly unexpected ..
        Log.warn ("Possible inconsistency: " + rect);
        return false;
      }
      if (top)
      {
        if (color == null)
        {
          color = background.getColorTop();
          border = background.getBorderSizeTop();
        }
        else if (color.equals(background.getColorTop()) == false ||
                (border != background.getBorderSizeTop()))
        {
          return false;
        }
      }
      else
      {
        if (color == null)
        {
          color = background.getColorBottom();
          border = background.getBorderSizeBottom();
        }
        else if (color.equals(background.getColorBottom()) == false ||
                (border != background.getBorderSizeBottom()))
        {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isCellAreaOccupied (final Map.Entry[] yKeys,
                                      final Map.Entry[] xKeys)
  {
    if (xKeys.length < 2 || yKeys.length < 2)
    {
      // not an area object, and therefore not valid ..
      return false;
    }
    for (int x = 0; x < xKeys.length - 1; x++)
    {
      final BoundsCut mappedX = (BoundsCut) xKeys[x].getValue();
      final int posX = mappedX.getPosition();
      for (int y = 0; y < yKeys.length - 1; y++)
      {
        final BoundsCut mappedY = (BoundsCut) yKeys[y].getValue();
        final int posY = mappedY.getPosition();
        final Object o = objectIdTable.getObject(posY, posX);
        if (o != null)
        {
          return true;
        }
      }
    }
    return false;
  }

  private void processVerticalLine (final Map.Entry[] yKeys, final Map.Entry xKey,
                                    final TableCellBackground background)
  {
    // again get the column index for the backend table ...
    final BoundsCut currentColumnValue = (BoundsCut) xKey.getValue();
    final int currentColumnIndex = currentColumnValue.getPosition();

    // we iterate over all rows ..
    // the yCuts also contains the End-Bounds (y+height);
    // the EB's for the last element do not hold any content.
    for (int y = 0; y < yKeys.length - 1; y++)
    {
      // get the index of the current row in the backend-table ...
      final BoundsCut currentRowValue = (BoundsCut) yKeys[y].getValue();
      final int currentRowIndex = currentRowValue.getPosition();
      workBounds = getCellBounds
              (workBounds, (Long)xKey.getKey(), (Long)yKeys[y].getKey());

      performMergeCellBackground(currentRowIndex, currentColumnIndex, background, workBounds);
    }

    if (xKey.equals(xMaxBoundsKey))
    {
      lastColumnCutIsSignificant = true;
    }
  }

  private void processHorizontalLine (final Map.Entry yKey, final Map.Entry[] xKeys,
                                      final TableCellBackground background)
  {
    // don't merge twice , therefore save the state ...

    // get the index of the current row in the backend-table ...
    final BoundsCut currentRowValue = (BoundsCut) yKey.getValue();
    final int currentRowIndex = currentRowValue.getPosition();

    // for every row we iterate over all columns ...
    // but we do not touch the last column ..
    for (int x = 0; x < xKeys.length - 1; x++)
    {
      // again get the column index for the backend table ...
      final BoundsCut currentColumnValue = (BoundsCut) xKeys[x].getValue();
      final int currentColumnIndex = currentColumnValue.getPosition();
      workBounds = getCellBounds
              (workBounds, (Long)xKeys[x].getKey(), (Long)yKey.getKey());

      performMergeCellBackground(currentRowIndex, currentColumnIndex, background, workBounds);
    }

    if (yKey.equals(yMaxBoundsKey))
    {
      lastRowCutIsSignificant = true;
    }
  }

  private void processAreaBackground (final Map.Entry[] yKeys, final Map.Entry[] xKeys,
                                      final TableCellBackground background)
  {
    // we iterate over all rows ..
    // the yCuts also contains the End-Bounds (y+height);
    // the EB's for the last element do not hold any content.
    for (int y = 0; y < yKeys.length - 1; y++)
    {
      // get the index of the current row in the backend-table ...
      final BoundsCut currentRowValue = (BoundsCut) yKeys[y].getValue();
      final int currentRowIndex = currentRowValue.getPosition();

      // for every row we iterate over all columns ...
      // but we do not touch the last column ..
      for (int x = 0; x < xKeys.length - 1; x++)
      {
        // again get the column index for the backend table ...
        final BoundsCut currentColumnValue = (BoundsCut) xKeys[x].getValue();
        final int currentColumnIndex = currentColumnValue.getPosition();

        workBounds = getCellBounds
                (workBounds, (Long)xKeys[x].getKey(), (Long)yKeys[y].getKey());
        performMergeCellBackground(currentRowIndex, currentColumnIndex,
                background, workBounds);
      }
    }
  }

  private StrictBounds getCellBounds (final StrictBounds input,
                                      final Long x, final Long y)
  {
    final long xVal = x.longValue();
    final long yVal = y.longValue();
    final Long[] xCuts = getXCuts();
    final Long[] yCuts = getYCuts();

    final long x2Val = xCuts[findXPosition(xVal + 1, UPPER_BOUNDS)].longValue();
    final long y2Val = yCuts[findYPosition(yVal + 1, UPPER_BOUNDS)].longValue();
    if (input == null)
    {
      return new StrictBounds(xVal, yVal, x2Val - xVal, y2Val - yVal);
    }
    input.setRect(xVal, yVal, x2Val - xVal, y2Val - yVal);
    return input;
  }

  private void performMergeCellBackground (final int currentRowIndex,
                                           final int currentColumnIndex,
                                           final TableCellBackground background,
                                           final StrictBounds bounds)
  {
    // get the old background ... we will merge this one with the new ..
    final TableCellBackground oldBackground =
            (TableCellBackground) backend.getObject(currentRowIndex, currentColumnIndex);
    if (oldBackground == null)
    {
      if (hasBorders(background))
      {
        // split the background ...
        final TableCellBackground newBackground = background.createMergedInstance(bounds);
        backend.setObject(currentRowIndex, currentColumnIndex, newBackground);
      }
      else
      {
        backend.setObject(currentRowIndex, currentColumnIndex, background);
      }
    }
    else
    {
      final TableCellBackground newBackground = oldBackground.merge(background, bounds);
      backend.setObject(currentRowIndex, currentColumnIndex, newBackground);
    }
  }

  private boolean hasBorders (final TableCellBackground background)
  {
    if (background.getColorTop() != null)
    {
      return true;
    }
    if (background.getColorBottom() != null)
    {
      return true;
    }
    if (background.getColorLeft() != null)
    {
      return true;
    }
    if (background.getColorRight() != null)
    {
      return true;
    }
    return false;
  }

  private void ensureXMapping (final long coordinate, final boolean aux)
  {
    final Long key = new Long(coordinate);
    final BoundsCut cut = (BoundsCut) xBounds.get(key);
    if (cut == null)
    {
      final int result = xBounds.size();
      xBounds.put(key, new BoundsCut(key.longValue(), result, aux));
      xKeysArray = null;
      // backend copy ...
      final int oldColumn = getPreviousColumn(coordinate);
      if (coordinate < xMaxBounds)
      {
        columnInserted(coordinate, oldColumn, result);
      }
      else
      {
        lastColumnCutIsSignificant = false;
      }
    }
    else if (cut.isAuxilary() && aux == false)
    {
      cut.makePermanent();
    }
  }

  protected void columnInserted (final long coordinate, final int oldColumn,
                                 final int newColumn)
  {
    // now copy all entries from old column to new column
    backend.copyColumn(oldColumn, newColumn);
    objectIdTable.copyColumn(oldColumn, newColumn);

    // handle the backgrounds ..
    final Long coordinateKey = new Long (coordinate);
    StrictBounds cellBounds = null;
    final Iterator entryIterator = yBounds.entrySet().iterator();
    while (entryIterator.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entryIterator.next();
      final BoundsCut bcut = (BoundsCut) entry.getValue();
      final int i = bcut.getPosition();
      final TableCellBackground bg =
              (TableCellBackground) backend.getObject(i, newColumn);
      if (bg == null)
      {
        continue;
      }

      // a column has been inserted. We have to check, whether the background has
      // borders defined, which might be invalid now.
      final StrictBounds bounds = bg.getCellBounds();
      cellBounds = getCellBounds(cellBounds, coordinateKey, (Long) entry.getKey());
      final TableCellBackground newBackground =
              bg.createMergedInstance(cellBounds);

      // the bounds of the old background have to be adjusted too ..
      final long parentNewWidth = cellBounds.getX() - bounds.getX();
      bounds.setRect(bounds.getX(), bounds.getY(),
              Math.max (0, parentNewWidth), bounds.getHeight());
      if (bg.getColorRight() != null)
      {
        // the original cell was split into two new cells ...
        // the new right border is no longer filled ...
        // a border was found, but is invalid now.
        bg.setBorderRight(null, 0);
      }
      backend.setObject(i, newColumn, newBackground);
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
    objectIdTable.copyRow(oldRow, newRow);

    // handle the backgrounds ..
    final Long coordinateKey = new Long (coordinate);
    StrictBounds cellBounds = null;
    final Iterator entryIterator = xBounds.entrySet().iterator();
    while (entryIterator.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entryIterator.next();
      final BoundsCut bcut = (BoundsCut) entry.getValue();
      final int i = bcut.getPosition();
      final TableCellBackground bg =
              (TableCellBackground) backend.getObject(newRow, i);
      if (bg == null)
      {
        continue;
      }

      // a row has been inserted. We have to check, whether the background has
      // borders defined, which might be invalid now.
      final StrictBounds bounds = bg.getCellBounds();
      cellBounds = getCellBounds(cellBounds, (Long) entry.getKey(), coordinateKey);
      final TableCellBackground newBackground =
              bg.createMergedInstance(cellBounds);


      // the bounds of the old background have to be adjusted too ..
      final long parentNewHeight = cellBounds.getY() - bounds.getY();
      bounds.setRect(bounds.getX(), bounds.getY(),
              bounds.getWidth(), Math.max (0, parentNewHeight));
      // due to the merging it is possible, that the bottom border
      // is invalid now.
      // the Top-Border of the original background is not touched ...
      if (bg.getColorBottom() != null)
      {
        bg.setBorderBottom(null, 0);
      }
      backend.setObject(newRow, i, newBackground);
    }
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
      final int result = yBounds.size();
      yBounds.put(key, new BoundsCut(key.longValue(), result, aux));
      yKeysArray = null;
      final int oldRow = getPreviousRow(coordinate);
      if (coordinate < yMaxBounds)
      {
        // oh, an insert operation. Make sure that everyone updates its state.
        rowInserted(coordinate, oldRow, result);
      }
      // else it would be an ADD; which should never copy old backgrounds..
      else
      {
        // we got a new last Row (as this is a add-operation) so reset the flag.
        lastRowCutIsSignificant = false;
      }
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
  public TableRectangle getTableBounds (final MetaElement e, final TableRectangle rect)
  {
    return this.getTableBounds(e.getBounds(), rect);
  }

  /**
   * Returns the position of the given element within the table. The TableRectangle
   * contains row and cell indices, no layout coordinates.
   *
   * @param bounds the element bounds for which the table bounds should be found.
   * @param rect the returned rectangle or null, if a new instance should be created
   * @return the filled table rectangle.
   */
  public TableRectangle getTableBounds (final StrictBounds bounds, TableRectangle rect)
  {
    if (rect == null)
    {
      rect = new TableRectangle();
    }
    final int x1 = findXPosition(bounds.getX(), LOWER_BOUNDS);
    final int y1 = findYPosition(bounds.getY(), LOWER_BOUNDS);
    final int x2 = findXPosition(bounds.getX() + bounds.getWidth(), UPPER_BOUNDS);
    final int y2 = findYPosition(bounds.getY() + bounds.getHeight(), UPPER_BOUNDS);
    rect.setRect(x1, y1, x2, y2);
    return rect;
  }

  protected int mapColumn (final int xCutIndex)
  {
    try
    {
      final Long[] xcuts = getXCuts();
      final BoundsCut boundsCut = (BoundsCut) xBounds.get(xcuts[xCutIndex]);
      return boundsCut.getPosition();
    }
    catch (NullPointerException npe)
    {
      throw new NullPointerException();
    }
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
    if (-pos == cuts.length + 1)
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
    if (-pos == cuts.length + 1)
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

    final boolean isEndContained = yBounds.containsKey(yMaxBoundsKey);
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
      yKeysArray[yKeysArray.length - 1] = yMaxBoundsKey;
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

    final boolean isEndContained = xBounds.containsKey(xMaxBoundsKey);
    if (isEndContained)
    {
      xKeysArray = new Long[xBounds.size()];
    }
    else
    {
      xKeysArray = new Long[xBounds.size() + 1];
      xKeysArray[xKeysArray.length - 1] = xMaxBoundsKey;
    }

    xKeysArray = (Long[]) xBounds.keySet().toArray(xKeysArray);
    if (!isEndContained)
    {
      xKeysArray[xKeysArray.length - 1] = xMaxBoundsKey;
    }
    return xKeysArray;
  }

  /**
   * A Callback method to inform the sheet layout, that the current page is complete, and
   * no more content will be added.
   */
  public void pageCompleted ()
  {
    removeAuxilaryBounds();
    clearObjectIdTable();
  }

  protected void removeAuxilaryBounds ()
  {
    final ArrayList removedCuts = new ArrayList();
    final Iterator itX = xBounds.entrySet().iterator();
    while (itX.hasNext())
    {
      final Map.Entry entry = (Map.Entry) itX.next();
      final BoundsCut cut = (BoundsCut) entry.getValue();
      if (cut.isAuxilary())
      {
        itX.remove();
        removedCuts.add (cut);
        xKeysArray = null;
      }
    }

    // now join the cuts with their left neighbour ..
    for (int i = 0; i < removedCuts.size(); i++)
    {
      final BoundsCut boundsCut = (BoundsCut) removedCuts.get(i);
      final int previousCol = getPreviousColumn(boundsCut.getCoordinate());
      final int col = boundsCut.getPosition();

      for (int row = 0; row < backend.getRowCount(); row++)
      {
        final TableCellBackground leftBg =
                (TableCellBackground) backend.getObject(row, previousCol);
        final TableCellBackground rightBg =
                (TableCellBackground) backend.getObject(row, col);
        if (leftBg == null || rightBg == null)
        {
          continue;
        }
        // now join ..
        final StrictBounds leftBounds = leftBg.getBounds();
        final StrictBounds newBounds = rightBg.getBounds().createUnion(leftBounds);
        final TableCellBackground unionBg = leftBg.createMergedInstance(newBounds);
        unionBg.setBorderRight(rightBg.getColorRight(), rightBg.getBorderSizeRight());
        backend.setObject(row, previousCol, unionBg);
        backend.setObject(row, col, null);
      }
    }
    removedCuts.clear();

    final Iterator itY = yBounds.entrySet().iterator();
    while (itY.hasNext())
    {
      final Map.Entry entry = (Map.Entry) itY.next();
      final BoundsCut cut = (BoundsCut) entry.getValue();
      if (cut.isAuxilary())
      {
        itY.remove();
        removedCuts.add (cut);
        yKeysArray = null;
      }
    }
    // now join the cuts with their top neighbour ..
    for (int i = 0; i < removedCuts.size(); i++)
    {
      final BoundsCut boundsCut = (BoundsCut) removedCuts.get(i);
      final int previousRow = getPreviousRow(boundsCut.getCoordinate());
      final int row = boundsCut.getPosition();

      for (int col = 0; col < backend.getColumnCount(); col++)
      {
        final TableCellBackground topBg =
                (TableCellBackground) backend.getObject(previousRow, col);
        final TableCellBackground bottomBg =
                (TableCellBackground) backend.getObject(row, col);
        if (topBg == null || bottomBg == null)
        {
          continue;
        }
        // now join ..
        final StrictBounds topBounds = topBg.getBounds();
        final StrictBounds newBounds = bottomBg.getBounds().createUnion(topBounds);
        final TableCellBackground unionBg = topBg.createMergedInstance(newBounds);
        unionBg.setBorderBottom(bottomBg.getColorBottom(), bottomBg.getBorderSizeBottom());
        backend.setObject(previousRow, col, unionBg);
        backend.setObject(row, col, null);
      }
    }
  }

  protected void clearObjectIdTable ()
  {
    objectIdTable.clear();
    objectIdTable.ensureCapacity(backend.getRowCount(), backend.getColumnCount());
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

  public CellReference getContentAt (final int row, final int column)
  {
    return (CellReference) objectIdTable.getObject(mapRow(row), mapColumn(column));
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
    final Long[] yCuts = getYCuts();
    if (row >= yCuts.length)
    {
      throw new IndexOutOfBoundsException
              ("Row " + row + " is invalid. Max valud row is " + yCuts.length);
    }
    final long bottomBorder;
    if ((row + 1) < yCuts.length)
    {
      bottomBorder = yCuts[row + 1].longValue();
    }
    else
    {
      bottomBorder = yMaxBounds;
    }

    final long retval = bottomBorder - yCuts[row].longValue();
    return retval;
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
    final long rightBorder;
    if (endCell >= xCuts.length)
    {
      rightBorder = xMaxBounds;
    }
    else
    {
      rightBorder = xCuts[endCell].longValue();
    }
    return rightBorder - xCuts[startCell].longValue();
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
    if (lastColumnCutIsSignificant)
    {
      return xCuts.length;
    }
    else
    {
      return xCuts.length - 1;
    }
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
    if (lastRowCutIsSignificant)
    {
      return yCuts.length;
    }
    else
    {
      return yCuts.length - 1;
    }
  }

  public long getYPosition (final int row)
  {
    final Long[] yCuts = getYCuts();
    if (row >= yCuts.length)
    {
      throw new IndexOutOfBoundsException
              ("Row " + row + " is invalid. Max valud row is " + yCuts.length);
    }
    return yCuts[row].longValue();
  }
}
