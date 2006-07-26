/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * TableRowRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableRowRenderBox.java,v 1.5 2006/07/24 12:18:56 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import java.util.ArrayList;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.BorderEdge;
import org.jfree.layouting.renderer.border.BorderFactory;
import org.jfree.layouting.renderer.border.EmptyBorderCorner;
import org.jfree.layouting.renderer.border.EmptyBorderEdge;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.EmptyBoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderNodeState;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnGroup;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.model.table.rows.TableRow;
import org.jfree.layouting.renderer.model.table.rows.TableRowModel;
import org.jfree.layouting.util.geom.StrictInsets;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.util.Log;

/**
 * A table section box does not much rendering or layouting at all. It
 * represents one of the three possible sections and behaves like any other
 * block box. But (here it comes!) it refuses to be added to anything else than
 * a TableRenderBox (a small check to save me a lot of insanity ..).
 * <p/>
 * For a valid layout, the major and minor axes need to be flipped.
 *
 * @author Thomas Morgner
 */
public class TableRowRenderBox extends BlockRenderBox
{
  private TableCell[] cells;
  private boolean autoGenerated;
  private boolean structureValidated;

  public TableRowRenderBox(final BoxDefinition boxDefinition,
                           final boolean autoGenerated)
  {
    super(boxDefinition, VerticalAlign.TOP);
    this.autoGenerated = autoGenerated;
    this.cells = new TableCell[0];

    /** Cells are laid out from left to right. */
    setMajorAxis(HORIZONTAL_AXIS);
    setMinorAxis(VERTICAL_AXIS);
  }

  /**
   * If that method returns true, the element will not be used for rendering.
   * For the purpose of computing sizes or performing the layouting (in the
   * validate() step), this element will treated as if it is not there.
   * <p/>
   * If the element reports itself as non-empty, however, it will affect the
   * margin computation.
   *
   * @return
   */
  public boolean isIgnorableForRendering()
  {
    if (autoGenerated)
    {
      return false;
    }
    return super.isIgnorableForRendering();
  }

  public TableRenderBox getTable()
  {
    RenderBox parent = getParent();
    if (parent instanceof TableSectionRenderBox)
    {
      final TableSectionRenderBox tableSectionRenderBox =
              (TableSectionRenderBox) parent;
      return tableSectionRenderBox.getTable();
    }
    return null;
  }

  protected long getPreferredSize(int axis)
  {
//    if (isStructureDirty())
//    {
//      Log.warn ("Table-Row is invalid.");
//      return 0;
//    }
//
//    if (axis == HORIZONTAL_AXIS)
//    {
//      return 0;
//    }
//    else
//    {
//      final long leadingPaddings = getLeadingInsets(getMinorAxis());
//      final long trailingPaddings = getTrailingInsets(getMinorAxis());
//      return leadingPaddings + trailingPaddings + computeRowHeight(0);
//    }
    if (axis == HORIZONTAL_AXIS)
    {
      // The width computation is handled by the column model, which gets
      // built if someone calls stuctValidate..
      throw new UnsupportedOperationException
              ("A table row has no own width preferences.");
    }
    else
    {
      // thw height is computed by the TableRowModel. We need a global view
      // on the table for that.
      throw new UnsupportedOperationException
              ("A table row has no own width preferences.");
    }
  }

  public void validate()
  {
    throw new UnsupportedOperationException
            ("A table-row cannot be validated directly.");
  }

  public void validate(TableRowModel rowModel,
                       TableRow row,
                       int rowIndex,
                       final boolean justGuessing)
  {
    final RenderNodeState state = getState();
    if (state == RenderNodeState.FINISHED)
    {
      return;
    }
    if (state == RenderNodeState.UNCLEAN)
    {
      setState(RenderNodeState.PENDING);
    }
    if (state == RenderNodeState.PENDING)
    {
      validateBorders();
      validatePaddings();
      setState(RenderNodeState.LAYOUTING);
    }

    if (isStructureDirty())
    {
      Log.warn("This row needs cell-validation first.");
      return;
    }
    validateMargins();

    Log.debug("TABLE-ROW: Begin Validate");

    long nodePos = getPosition(getMajorAxis());
    final long minorAxisNodePos = getPosition(getMinorAxis());

    final TableColumnModel columnModel = getColumnModel();
    final long totalMinChunkSize = columnModel.getMinimumChunkSize();
    final long givenExtraSpace = getWidth() - totalMinChunkSize;
    final long expectedExtraSpace =
            columnModel.getPreferredSize() - totalMinChunkSize;
    final long borderSpacing = columnModel.getBorderSpacing();

    final long heightAbove = row.getValidatedLeadingSize();
    if (!justGuessing)
    {
      if (getReferencePoint(getMinorAxis()) != heightAbove)
      {
        throw new IllegalStateException();
      }
    }
    int cellCounter = 0;
    RenderNode node = getFirstChild();
    boolean worked = false;
    while (node != null)
    {
      if (node instanceof TableCell == false)
      {
        // Ignore all empty childs. However, give it an position.
        node.setPosition(getMajorAxis(), nodePos);
        node.setPosition(getMinorAxis(),
                minorAxisNodePos + row.getValidatedLeadingSize());
        node.setDimension(getMinorAxis(), 0);
        node.setDimension(getMajorAxis(), 0);
        node = node.getNext();
        continue;
      }



      TableCell cell = (TableCell) node;
      worked = true;
      long nativeCellSize = computeColWidth
              (columnModel, cellCounter, givenExtraSpace, expectedExtraSpace);

      if (cell instanceof SpannedCellPlaceholder)
      {
        node.setPosition(getMajorAxis(), nodePos);
        node.setPosition(getMinorAxis(), minorAxisNodePos);
        node.setDimension(getMinorAxis(), 0);
        node.setDimension(getMajorAxis(), 0);
      }
      else
      {
        long cellSize = nativeCellSize;
        for (int i = 1; i < cell.getColSpan(); i += 1)
        {
          Log.debug("Validating cell " + (cellCounter + i) + ": " + cell);
          cellSize += computeColWidth
                  (columnModel, cellCounter + i, givenExtraSpace, expectedExtraSpace);
        }
        cellSize += (cell.getColSpan() - 1) * borderSpacing;

        node.setPosition(getMajorAxis(), nodePos);
        node.setPosition(getMinorAxis(), minorAxisNodePos);

        long rowSize = 0;
        final int rowSpan = Math.min
                (cell.getRowSpan(), rowModel.getRowCount() - rowIndex);
        for (int i = 0; i < rowSpan; i += 1)
        {
          TableRow rowx = rowModel.getRow(i + rowIndex);
          rowSize += rowx.getValidateSize();
        }

        node.setDimension(getMinorAxis(), rowSize);
        node.setDimension(getMajorAxis(), cellSize);
        TableCellRenderBox tcb = (TableCellRenderBox) node;
        tcb.validate(justGuessing, row);
        if (justGuessing == false)
        {
          node.setDimension(getMinorAxis(), rowSize);
        }
      }

      nodePos += nativeCellSize;
      nodePos += borderSpacing;
      node = node.getNext();
      cellCounter += 1;//cell.getColSpan();
    }

    if (worked)
    {
      // undo the last border spacing ...
      nodePos -= borderSpacing;
    }

    setDimension(getMajorAxis(), nodePos - getPosition(getMajorAxis()));
    if (justGuessing)
    {
      setDimension(getMinorAxis(), row.getPreferredSize());
    }
    else
    {
      setDimension(getMinorAxis(), row.getValidateSize());
      setState(RenderNodeState.FINISHED);
    }
    Log.debug("TABLE-ROW: Leave Validate: " + row.getPreferredSize());

  }

  private long computeColWidth(final TableColumnModel columnModel,
                               final int cellCounter,
                               final long givenExtraSpace,
                               final long expectedExtraSpace)
  {
    final TableColumn firstColumn = columnModel.getColumn(cellCounter);
    final long cellPrefWidth = firstColumn.getPreferredSize();
    final long cellMinChunk = firstColumn.getMinimumChunkSize();
    final long cellExpExtraSpace = cellPrefWidth - cellMinChunk;
    if (expectedExtraSpace == 0)
    {
      return firstColumn.getMinimumChunkSize();
    }
    else
    {
      return firstColumn.getMinimumChunkSize() +
              givenExtraSpace * cellExpExtraSpace / expectedExtraSpace;
    }
  }

  public TableColumnModel getColumnModel()
  {
    final TableRenderBox table = getTable();
    if (table == null)
    {
      return null;
    }
    return table.getColumnModel();
  }

  protected void validateBorders()
  {
    if (isBordersValidated())
    {
      return;
    }

    StrictInsets borders = getBordersInternal();
    borders.setTop(0);
    borders.setBottom(0);
    borders.setLeft(0);
    borders.setRight(0);

    setBordersValidated(true);
  }

  protected void validatePaddings()
  {
    if (isPaddingsValidated())
    {
      return;
    }

    StrictInsets paddings = getPaddingsInternal();
    paddings.setTop(0);
    paddings.setBottom(0);
    paddings.setLeft(0);
    paddings.setRight(0);

    setPaddingsValidated(true);
  }

  protected void validateMargins()
  {
    if (isMarginsValidated())
    {
      return;
    }

    StrictInsets margins = getAbsoluteMarginsInternal();
    margins.setTop(0);
    margins.setBottom(0);
    margins.setLeft(0);
    margins.setRight(0);

    StrictInsets effectiveMargins = getEffectiveMarginsInternal();
    effectiveMargins.setTop(0);
    effectiveMargins.setBottom(0);
    effectiveMargins.setLeft(0);
    effectiveMargins.setRight(0);

    setMarginsValidated(true);
  }

  public boolean isStructureDirty()
  {
    final TableColumnModel columnModel = getColumnModel();
    if (cells.length != columnModel.getColumnCount())
    {
      // we always validate empty tables; just in case the user did not
      // define any columns at all.
      return true;
    }

    // todo: check, whether we have some cells defined.
    return structureValidated == false;
  }

  public void structureValidateCells()
  {

    final TableRowRenderBox prev = getPrevRow();
    if (prev == null)
    {
      structureValidateFirstRow();
    }
    else
    {
      structureValidateInnerRow(prev);
    }

    // we have performed the validation at least once ..
    structureValidated = true;
  }

  public void validateCellSizes(TableRow tableRow,
                                boolean firstRow,
                                boolean lastRow)
  {
    if (isStructureDirty())
    {
      throw new IllegalStateException
              ("Cannot validate sizes on dirty structures.");
    }

    final TableSectionRenderBox parent = (TableSectionRenderBox) getParent();
    if (parent == null)
    {
      throw new IllegalStateException("Parent cannot and must not be null");
    }
    final boolean prevRows;
    final boolean nextRows;
    final boolean collapsingBorderModel = getTable().isCollapsingBorderModel();
    if (collapsingBorderModel)
    {
      final TableSectionRenderBox prevSection = parent.getPreviousSection();
      if (prevSection == null)
      {
        prevRows = false;
      }
      else
      {
        prevRows = prevSection.getRowModel().getRowCount() > 0;
      }

      final TableSectionRenderBox nextSection = parent.getNextSection();
      if (nextSection == null)
      {
        nextRows = false;
      }
      else
      {
        nextRows = nextSection.getRowModel().getRowCount() > 0;
      }
    }
    else
    {
      prevRows = false;
      nextRows = false;
    }

    final TableColumnModel columnModel = getColumnModel();
    // Now update the sizes.
    for (int i = 0; i < cells.length; i++)
    {
      TableCell cell = cells[i];
      final TableColumn column = columnModel.getColumn(i);
      if (collapsingBorderModel)
      {
        validateEffectiveBorder(prevRows, nextRows,
                tableRow, firstRow, lastRow,
                columnModel.getGroupForIndex(i),
                column, i == 0, i == (cells.length - 1), cell);
      }
      else if (cell instanceof TableCellRenderBox)
      {
        cell.setEffectiveBorder(cell.getOriginalBorder());
      }
      else
      {
        cell.setEffectiveBorder(Border.createEmptyBorder());
      }

      if (cell instanceof TableCellRenderBox)
      {
        final TableCellRenderBox cellRenderBox = (TableCellRenderBox) cell;
        final long chunkSize = cellRenderBox.getMinimumChunkSize(HORIZONTAL_AXIS);
        final long preferredSize = cellRenderBox.getEffectiveLayoutSize(HORIZONTAL_AXIS);
        column.updateSizes(cell.getColSpan(), preferredSize, chunkSize);

        final long chunkHeight = cellRenderBox.getMinimumChunkSize(VERTICAL_AXIS);
        final long preferredHeight = cellRenderBox.getEffectiveLayoutSize(VERTICAL_AXIS);
        tableRow.updateSizes(cell.getRowSpan(), preferredHeight, chunkHeight);
      }
    }
    Log.debug("HERE");
  }

  private void structureValidateInnerRow(final TableRowRenderBox prev)
  {
    final TableColumnModel columnModel = getColumnModel();
    Log.debug("TABLE-STRUCT BEGIN: Inner-Row-ValidateCells " + this);
    // This may easily lead to a StackOverflow error in case we have very
    // large tables. We certainly have to deal with that later.
    // prev.validateCells();

    final int initialColCount = columnModel.getColumnCount();
    // Syncronize the previous row with this row. Generate empty cells where
    // necessary.
    final TableCell[] prevList = prev.cells;
    final ArrayList collectedCells = collectCells();
    final ArrayList generatedCells = new ArrayList();

    int colIdx = 0;
    TableCellRenderBox lastCell = null;
    for (int prevIdx = 0; prevIdx < prevList.length; prevIdx++)
    {
      final TableCell cell = prevList[prevIdx];
      if (cell.getRowSpan() > 1)
      {

        if (colIdx < collectedCells.size())
        {
          TableCell myCell = (TableCell) collectedCells.get(colIdx);
          if (myCell instanceof SpannedCellPlaceholder)
          {

            // Oh, we have a spanned cell at this position.
            // good, so lets do some validation ..
            if ((myCell.getRowSpan() - 1) == cell.getRowSpan())
            {
              colIdx += 1;
              continue;
            }

            // Oh, no, a conflict. The cell from the previous row overlaps
            // with my cell.
            Log.warn("TABLE-ROW: Conflicting cell-definition detected.");
            SpannedCellPlaceholder org = (SpannedCellPlaceholder) myCell;
            final int rows = Math.max(org.getRowSpan(), cell.getRowSpan() - 1);
            final int cols = Math.max(org.getColSpan(), cell.getColSpan());
            final Border border = mergeConflictingBorder(cell.getOriginalBorder(),
                    org.getOriginalBorder());
            SpannedCellPlaceholder conflict =
                    new SpannedCellPlaceholder(rows, cols, border);
            replaceChild(org, conflict);
            colIdx += 1;
            continue;
          }
        }

        if (cell instanceof TableCellRenderBox)
        {
          TableCellRenderBox renderBox = (TableCellRenderBox) cell;
          final SpannedCellPlaceholder spanned = new SpannedCellPlaceholder
                  (renderBox, cell.getRowSpan() - 1, cell.getColSpan(), true, false);
          generatedCells.add(spanned);
          insertAfter(lastCell, spanned);
        }
        else if (cell instanceof SpannedCellPlaceholder)
        {
          SpannedCellPlaceholder span = (SpannedCellPlaceholder) cell;
          final SpannedCellPlaceholder spanned = new SpannedCellPlaceholder(span);
          generatedCells.add(spanned);
          insertAfter(lastCell, spanned);
        }
        else
        {
          throw new IllegalStateException("Invalid cell-type detected.");
        }

        continue;
      }

      // OK, so we've reached the non-spanned cell. This leaves us open
      // to process one of this row's cells.
      if (colIdx < collectedCells.size())
      {
        // We have a cell.
        TableCell myCell = (TableCell) collectedCells.get(colIdx);
        if (myCell instanceof TableCellRenderBox == false)
        {
          throw new IllegalStateException
                  ("Model is out of sync: There are more placeholders " +
                          "than spanned cells: " + colIdx);
        }
        TableCellRenderBox renderBox = (TableCellRenderBox) myCell;
        lastCell = renderBox;
        generatedCells.add(renderBox);
        colIdx += 1;

        if (myCell.getColSpan() < 2)
        {
          continue;
        }

        // So, now it gets a bit messy. You may want to wait outside ..
        // Ok, not. Then be prepared.

        // We have to syncronize against three datasources now.
        // (1) The table cell itself. We have not yet generated spanned
        //     placeholders during the first run.
        // (2) The previous row.
        // (3) The generated placeholders.

        // First, we syncronize the generated placeholders against the
        // parent row. This will cover the later cases and allows us to
        // reuse the placeholder boxes (performance!)

        // This call counts and validates the previous row with the spacers
        // found in the current row. It will not validate more than the
        // given number of columns.
        final int spacerSize = validateSpacers
                (collectedCells, colIdx, prevList, prevIdx, myCell.getColSpan());

        // Skip the validated columns.
        prevIdx += spacerSize;
        colIdx += spacerSize;

        TableCell insertationPoint;
        if (spacerSize == 0)
        {
          insertationPoint = renderBox;
        }
        else
        {
          insertationPoint = (TableCell) collectedCells.get(colIdx - 1);
        }
        // Whether we have to generate additional spacers can be determined
        // by looking at the collected spacers.
        if (spacerSize < myCell.getColSpan())
        {
          final int generateCount = myCell.getColSpan() - spacerSize - 1;
          for (int i = 0; i < generateCount; i += 1)
          {
            if (prevIdx < prevList.length)
            {
              // We have a previous element
              TableCell prevCell = prevList[prevIdx];
              prevIdx += 1;
              if (prevCell.getRowSpan() > 1)
              {
                // A conflict; again. The cell from the previous row overlaps
                // with my cell.
                Log.warn("TABLE-ROW: Conflicting cell-definition detected.");
                final int rows = Math.max(renderBox.getRowSpan(), prevCell.getRowSpan() - 1);
                final int cols = Math.max(renderBox.getColSpan(), prevCell.getColSpan());
                final Border border = mergeConflictingBorder(prevCell.getOriginalBorder(),
                        myCell.getOriginalBorder());
                SpannedCellPlaceholder conflict =
                        new SpannedCellPlaceholder(rows, cols, border);

                insertAfter(insertationPoint.getCellNode(), conflict);
                insertationPoint = conflict;
              }
            }
            else if (colIdx < collectedCells.size())
            {
              TableCell maybeSpacer = (TableCell) collectedCells.get(colIdx);
              if (maybeSpacer instanceof SpannedCellPlaceholder)
              {
                // fine, this means, that there can be no conflicts, as
                // the previous list does not contain any elements anymore
                generatedCells.add(maybeSpacer);
                continue;
              }
            }

            // There is no previous element at all. Generate a spanning
            // placeholder.
            SpannedCellPlaceholder spacer = new SpannedCellPlaceholder
                    (renderBox, renderBox.getRowSpan(), generateCount - i, false, true);
            insertAfter(insertationPoint.getCellNode(), spacer);
            generatedCells.add(spacer);
            insertationPoint = spacer;
          }
        }
      }
    }

    TableCellRenderBox pendingCell = null;
    // At this point, we have almost reached the end of this process.
    for (; colIdx < collectedCells.size(); colIdx += 1)
    {
      // ... but not yet. There are some cells which we have to process.
      final TableCell cell = (TableCell) collectedCells.get(colIdx);

      if (pendingCell != null)
      {
        final int colSpan = pendingCell.getColSpan();
        for (int x = 1; x < colSpan; x += 1)
        {

          final SpannedCellPlaceholder span = new SpannedCellPlaceholder
                  (pendingCell, pendingCell.getRowSpan(), x, false, true);
          insertAfter(pendingCell, span);
          generatedCells.add(span);
        }
      }

      generatedCells.add(cell);
      if (cell.getColSpan() > 1 && cell instanceof TableCellRenderBox)
      {
        pendingCell = (TableCellRenderBox) cell;
      }
      else
      {
        pendingCell = null;
      }
    }

    final int finalColCount = generatedCells.size();
    if (initialColCount > finalColCount)
    {
      // Add empty cells.
      for (int i = finalColCount; i < initialColCount; i++)
      {
        final TableCellRenderBox cell =
                new TableCellRenderBox(new EmptyBoxDefinition(), true);
        addGeneratedChild(cell);
        generatedCells.add(cell);
      }
    }
    else if (initialColCount < finalColCount)
    {
      // Add auto-columns ..
      for (int i = initialColCount; i < finalColCount; i++)
      {
        columnModel.addAutoColumn();
      }
    }

    cells = (TableCell[]) generatedCells.toArray
            (new TableCell[generatedCells.size()]);


    Log.debug("TABLE-STRUCT END: ValidateCells " + this);
  }

  private Border mergeConflictingBorder(Border vertical, Border horizontal)
  {
    final BorderEdge top = horizontal.getTop();
    final BorderEdge left = vertical.getLeft();
    final BorderEdge right = BorderFactory.merge
            (vertical.getRight(), horizontal.getRight(), getComputedBlockContextWidth());
    final BorderEdge bottom = BorderFactory.merge
            (vertical.getBottom(), horizontal.getBottom(), getComputedBlockContextWidth());

    final Border border = new Border(top, left, bottom, right,
            EmptyBorderEdge.getInstance(),
            EmptyBorderCorner.getInstance(), EmptyBorderCorner.getInstance(),
            EmptyBorderCorner.getInstance(), EmptyBorderCorner.getInstance());
    return border;
  }

  private int validateSpacers(final ArrayList collectedCells,
                              final int colIdx,
                              final TableCell[] prevList,
                              final int prevIdx,
                              final int limit)
  {
    //final ArrayList spacers = new ArrayList();
    int spacerCount = 0;
    int spacerColIdx = colIdx;
    int parentSpacerColIdx = prevIdx;
    int maxIdx = Math.min(colIdx + limit, collectCells().size());
    while (spacerColIdx < maxIdx)
    {
      final TableCell maybeSpacer =
              (TableCell) collectedCells.get(spacerColIdx);
      if (maybeSpacer instanceof SpannedCellPlaceholder == false)
      {
        // No spacer. We may have to generate the missing spacers.
        break;
      }

      spacerColIdx += 1;

      // OK, so we have a spacer. Lets look at the parent-row too
      SpannedCellPlaceholder spacer = (SpannedCellPlaceholder) maybeSpacer;
      if (parentSpacerColIdx >= prevList.length)
      {
        // no entry anymore? return ...
        return spacerCount;
      }

      TableCell prevSpacer = prevList[parentSpacerColIdx];
      parentSpacerColIdx += 1;

      if (spacer.getColSpan() == prevSpacer.getColSpan() &&
              (spacer.getRowSpan() - 1) == prevSpacer.getRowSpan())
      {
        // no conflict.
        //spacers.add(spacer);
        spacerCount += 1;
      }
      else
      {
        // Oh, we have a spanned cell at this position.
        // good, so lets do some validation ..
        if ((spacer.getRowSpan() - 1) != prevSpacer.getRowSpan())
        {
          // Oh, no, a conflict. The cell from the previous row overlaps
          // with my cell.
          Log.warn("TABLE-ROW: Conflicting cell-definition detected.");
          final int rows = Math.max(spacer.getRowSpan(), prevSpacer.getRowSpan() - 1);
          final int cols = Math.max(spacer.getColSpan(), prevSpacer.getColSpan());
          final Border border = mergeConflictingBorder(prevSpacer.getOriginalBorder(),
                  spacer.getOriginalBorder());
          SpannedCellPlaceholder conflict =
                  new SpannedCellPlaceholder(rows, cols, border);
          replaceChild(spacer, conflict);
        }
        spacerCount += 1;
      }
    }
    return spacerCount;
  }

  private void structureValidateFirstRow()
  {
    final TableColumnModel columnModel = getColumnModel();
    final int initialColCount = columnModel.getColumnCount();

    // OK, so we have no previous element. Thats nice, as this means,
    // that there are no spanned cells. Make it quick and dirty.
    Log.debug("TABLE-STRUCT BEGIN: First-Row-ValidateCells " + this);

    ArrayList collectedCells = collectCells();
    if (collectedCells.isEmpty())
    {
      if (initialColCount == 0)
      {
        if (cells.length == 0)
        {
          return;
        }
        else
        {
          throw new IllegalStateException
                  ("It seems we had more cells in the past than we have now");
        }
      }
      // There are cells defined in later rows. Create empty cells.
      // Empty-cells means: NullValues.
      cells = new TableCell[initialColCount];
      return;
    }

    int predictedColCount = 0;
    int pendingSpans = 0;
    int predictedPendingSpans = 0;
    // Cound the number of cells.
    for (int i = 0; i < collectedCells.size(); i++)
    {
      TableCell cell = (TableCell) collectedCells.get(i);
      if (cell instanceof TableCellRenderBox)
      {
        if (pendingSpans > 0)
        {
          if (pendingSpans != predictedPendingSpans)
          {
            throw new IllegalStateException
                    ("Model is out-of-sync: " + pendingSpans +
                            " " + predictedPendingSpans);
          }

          predictedColCount += pendingSpans;
        }
        else
        {
          // during the first run, when there are no generated cells,
          // we have to accept the defaults.
          predictedColCount += predictedPendingSpans;
        }
        predictedColCount += 1;
        predictedPendingSpans = cell.getColSpan() - 1;
        pendingSpans = 0;
      }
      else
      {
        pendingSpans += 1;
      }
    }
    predictedColCount += pendingSpans;

    final int finalColCount = Math.max(predictedColCount, initialColCount);
    if (cells.length != finalColCount)
    {
      cells = new TableCell[finalColCount];
    }

    int columns = 0;
    TableCellRenderBox pendingCell = null;

    for (int i = 0; i < collectedCells.size(); i++)
    {
      final TableCell cell = (TableCell) collectedCells.get(i);

      if (pendingCell != null)
      {
        final int colSpan = pendingCell.getColSpan();
        for (int x = 1; x < colSpan; x += 1)
        {

          final SpannedCellPlaceholder span = new SpannedCellPlaceholder
                  (pendingCell, pendingCell.getRowSpan(), x, false, true);
          insertAfter(pendingCell, span);
          cells[columns + colSpan - x - 1] = span;
        }
        columns += (colSpan - 1);
      }

      cells[columns] = cell;
      columns += 1;
      if (cell.getColSpan() > 1 && cell instanceof TableCellRenderBox)
      {
        pendingCell = (TableCellRenderBox) cell;
      }
      else
      {
        pendingCell = null;
      }
    }

    if (columns < finalColCount)
    {
      // Add empty cells.
      for (int i = columns; i < finalColCount; i++)
      {
        final TableCellRenderBox cell =
                new TableCellRenderBox(new EmptyBoxDefinition(), true);
        addGeneratedChild(cell);
        cells[i] = cell;
      }
    }
    else if (initialColCount < finalColCount)
    {
      // Add auto-columns ..
      for (int i = initialColCount; i < finalColCount; i++)
      {
        columnModel.addAutoColumn();
      }
    }

    Log.debug("TABLE-STRUCT END: ValidateCells " + this);
  }

  private TableRowRenderBox getPrevRow()
  {
    RenderNode prev = getPrev();
    while (prev != null)
    {
      if (prev instanceof TableRowRenderBox)
      {
        return (TableRowRenderBox) prev;
      }
      prev = prev.getPrev();
    }
    return null;
  }

  /**
   * Returns true, if there is at least one cell in this row, which spans to the
   * next row.
   *
   * @return
   */
  public boolean isRowSpanned()
  {
    if (isStructureDirty())
    {
      throw new IllegalArgumentException("This row must be validated first.");
    }

    for (int i = 0; i < cells.length; i++)
    {
      final TableCell cell = cells[i];
      if (cell == null)
      {
        continue;
      }

      if (cell.getRowSpan() > 1)
      {
        return true;
      }
    }
    return false;
  }

  public TableCell[] getCells()
  {
    return cells;
  }

  private ArrayList collectCells()
  {
    ArrayList cells = new ArrayList();
    RenderNode node = getFirstChild();

    while (node != null)
    {
      if (node instanceof TableCell)
      {
        cells.add(node);
      }
      node = node.getNext();
    }

    return cells;
  }


  private void validateEffectiveBorder(final boolean havePrevRows,
                                       final boolean haveNextRows,
                                       final TableRow row,
                                       final boolean firstRowInSection,
                                       final boolean lastRowInSection,
                                       final TableColumnGroup colGroup,
                                       final TableColumn column,
                                       final boolean firstColumn,
                                       final boolean lastColumn,
                                       final TableCell cell)
  {
    RenderBox sectionBox = getParent();
    if (sectionBox == null)
    {
      throw new IllegalStateException("No parent section?");
    }
    RenderBox tableBox = sectionBox.getParent();
    if (tableBox == null)
    {
      throw new IllegalStateException("No parent table?");
    }

    final Border tableBorder = tableBox.getBoxDefinition().getBorder();
    final Border columnGroupBorder = colGroup.getBorder();
    final Border columnBorder = column.getBorder();
    final Border sectionBorder = sectionBox.getBoxDefinition().getBorder();
    final Border rowBorder = getBoxDefinition().getBorder();
    final Border cellBorder = cell.getOriginalBorder();

    // top border ..
    final ArrayList significantBorders = new ArrayList();
    if (firstRowInSection)
    {
      if (havePrevRows == false)
      {
        significantBorders.add(tableBorder.getTop());
        significantBorders.add(columnGroupBorder.getTop());
        significantBorders.add(columnBorder.getTop());
      }
      significantBorders.add(sectionBorder.getTop());
    }
    significantBorders.add(rowBorder.getTop());
    significantBorders.add(cellBorder.getTop());
    BorderEdge topEdge = merge(significantBorders);

    // bottom border ..
    significantBorders.clear();
    if (lastRowInSection)
    {
      if (haveNextRows == false)
      {
        significantBorders.add(tableBorder.getBottom());
        significantBorders.add(columnGroupBorder.getBottom());
        significantBorders.add(columnBorder.getBottom());
      }
      significantBorders.add(sectionBorder.getBottom());
    }
    significantBorders.add(rowBorder.getBottom());
    significantBorders.add(cellBorder.getBottom());
    BorderEdge bottomEdge = merge(significantBorders);

    final TableColumnModel columnModel = getColumnModel();
    final boolean firstColumnGroup =
            columnModel.getColumnGroup(0) == colGroup;
    final boolean lastColumnGroup = columnModel.getColumnGroup
            (columnModel.getColumnGroupCount() - 1) == colGroup;

    // left border ..
    significantBorders.clear();
    if (firstColumn && firstColumnGroup)
    {
      significantBorders.add(tableBorder.getLeft());
      significantBorders.add(columnGroupBorder.getLeft());
    }
    significantBorders.add(columnBorder.getLeft());
    if (firstColumn)
    {
      significantBorders.add(sectionBorder.getLeft());
      significantBorders.add(rowBorder.getLeft());
    }
    significantBorders.add(cellBorder.getLeft());
    BorderEdge leftEdge = merge(significantBorders);

    // right border ..
    significantBorders.clear();
    if (lastColumn && lastColumnGroup)
    {
      significantBorders.add(tableBorder.getRight());
      significantBorders.add(columnGroupBorder.getRight());
    }
    significantBorders.add(columnBorder.getRight());
    if (lastColumn)
    {
      significantBorders.add(sectionBorder.getRight());
      significantBorders.add(rowBorder.getRight());
    }
    significantBorders.add(cellBorder.getRight());
    BorderEdge rightEdge = merge(significantBorders);

    cell.setEffectiveBorder
            (new Border(topEdge, leftEdge, bottomEdge, rightEdge,
                    EmptyBorderEdge.getInstance(),
                    EmptyBorderCorner.getInstance(), EmptyBorderCorner.getInstance(),
                    EmptyBorderCorner.getInstance(), EmptyBorderCorner.getInstance()));

  }

  private BorderEdge merge(ArrayList list)
  {
    if (list.isEmpty())
    {
      return EmptyBorderEdge.getInstance();
    }
    BorderEdge edge = (BorderEdge) list.get(0);
    for (int i = 1; i < list.size(); i++)
    {
      BorderEdge borderEdge = (BorderEdge) list.get(i);
      edge = BorderFactory.merge(edge, borderEdge, getComputedBlockContextWidth());
    }
    return edge;
  }

}
