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
 * TableSectionRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableSectionRenderBox.java,v 1.3 2006/07/20 17:50:52 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderNodeState;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.model.table.rows.SeparateRowModel;
import org.jfree.layouting.renderer.model.table.rows.TableRow;
import org.jfree.layouting.renderer.model.table.rows.TableRowModel;
import org.jfree.layouting.util.geom.StrictInsets;
import org.jfree.util.Log;

/**
 * A table section box does not much rendering or layouting at all. It
 * represents one of the three possible sections and behaves like any other
 * block box. But (here it comes!) it refuses to be added to anything else than
 * a TableRenderBox (a small check to save me a lot of insanity ..).
 *
 * @author Thomas Morgner
 */
public class TableSectionRenderBox extends BlockRenderBox
{
  private CSSValue displayRole;
  private TableRowModel rowModel;

  public TableSectionRenderBox(final BoxDefinition boxDefinition,
                               final CSSValue displayRole)
  {
    super(boxDefinition);
    this.displayRole = displayRole;
    this.rowModel = new SeparateRowModel(this);
  }

  public CSSValue getDisplayRole()
  {
    return displayRole;
  }

  public TableRenderBox getTable()
  {
    RenderBox parent = getParent();
    if (parent instanceof TableRenderBox)
    {
      return (TableRenderBox) parent;
    }
    throw new IllegalStateException
          ("A table section without a table is invalid.");
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

  public TableRowModel getRowModel()
  {
    return rowModel;
  }

  public void structValidateRows ()
  {
//    TableRowRenderBox row = findLastRow();
//    if (row == null)
//    {
//      return;
//    }
//
//    while (row.isRowSpanned())
//    {
//      final TableRowRenderBox newLastRow =
//              new TableRowRenderBox(new EmptyBoxDefinition(), true);
//      insertAfter(row, newLastRow);
//      row = newLastRow;
//    }

    final TableColumnModel columnModel = getTable().getColumnModel();

    RenderNode node = getFirstChild();
    Log.debug ("Starting validation process..");
    int rowCount = 0;
    while (node != null)
    {
      if (node instanceof TableRowRenderBox == false)
      {
        node = node.getNext();
        continue;
      }

      // A row.
      TableRowRenderBox row = (TableRowRenderBox) node;
      if (row.isStructureDirty())
      {
        final int size = columnModel.getColumnCount();
        row.structureValidateCells();
        if (columnModel.getColumnCount() != size)
        {
          // a structural change. Great, we have to restart.
          node = getFirstChild();
          rowCount = 0;
          Log.debug ("Structural change detected; restarting.");
          continue;
        }
      }
      if (rowCount >= rowModel.getRowCount())
      {
        // generate a new row in the model.
        final BoxDefinition boxDef = row.getBoxDefinition();
        rowModel.addRow(new TableRow
                (boxDef.getPreferredHeight(), boxDef.getBorder()));
      }
      rowCount += 1;
      Log.debug ("Row validated.");
      node = node.getNext();
    }

    node = getFirstChild();
    rowCount = 0;
    Log.debug ("Starting size-validation process..");
    while (node != null)
    {
      if (node instanceof TableRowRenderBox == false)
      {
        node = node.getNext();
        continue;
      }

      TableRowRenderBox row = (TableRowRenderBox) node;
      row.validateCellSizes(rowModel.getRow(rowCount));
      node = node.getNext();
      rowCount += 1;
    }

    rowModel.validateSizes();
    columnModel.validateSizes();
    Log.debug ("Finished size-validation process..");
  }

  public void validate()
  {
    // we need to validate our rows ...
    structValidateRows();


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

    validateMargins();

    Log.debug("TABLE-SECTION: Begin Validate");

    final TableRenderBox table = getTable();
    final long borderSpacing = table.getRowSpacing();

    long nodePos = getPosition(getMajorAxis());
    final long minorAxisNodePos = getPosition(getMinorAxis());
    final long defaultNodeWidth = Math.max(0, getDimension(getMinorAxis()));

    // first run: Validate all rows under the condition that the width is
    // given.
    RenderNode node = getFirstChild();
    boolean worked = false;
    while (node != null)
    {
      if (node.isIgnorableForRendering() ||
          node instanceof TableRowRenderBox == false)
      {
        // Ignore all empty childs. However, give it an position.
        node.setPosition(getMajorAxis(), nodePos);
        node.setPosition(getMinorAxis(), minorAxisNodePos);
        node.setDimension(getMinorAxis(), 0);
        node.setDimension(getMajorAxis(), 0);
        node = node.getNext();
        continue;
      }

      worked = true;

      node.setPosition(getMajorAxis(), nodePos);
      node.setPosition(getMinorAxis(), minorAxisNodePos);
      node.setDimension(getMinorAxis(), defaultNodeWidth);
      node.setDimension(getMajorAxis(), 0);
      node.validate();

      nodePos += node.getDimension(getMajorAxis());
      nodePos += borderSpacing;
      node = node.getNext();
    }

    if (worked)
    {
      // undo the last border spacing ...
      nodePos -= borderSpacing;
    }

    // fill the sizes
    node = getFirstChild();
    int rowCount = 0;
    while (node != null)
    {
      if (node.isIgnorableForRendering() ||
          node instanceof TableRowRenderBox == false)
      {
        node = node.getNext();
        continue;
      }
      TableRow row = rowModel.getRow(rowCount);
      TableRowRenderBox rowBox = (TableRowRenderBox) node;
      RenderNode cell = rowBox.getFirstChild();
      while (cell != null)
      {
        if (cell instanceof TableCellRenderBox)
        {
          TableCellRenderBox tcb = (TableCellRenderBox) cell;
          row.updateValidatedSize(tcb.getRowSpan(), tcb.getHeight());
        }
        cell = cell.getNext();
      }
      rowCount += 1;
      node = node.getNext();
    }

    // now, compute the best heights for the rows.
    rowModel.validateActualSizes();

    // Second run: Validate all rows with a proper height.
    node = getFirstChild();
    worked = false;
    rowCount = 0;
    nodePos = getPosition(getMajorAxis());
    
    while (node != null)
    {
      if (node.isIgnorableForRendering() ||
          node instanceof TableRowRenderBox == false)
      {
        // Ignore all empty childs. However, give it an position.
        node.setPosition(getMajorAxis(), nodePos);
        node.setPosition(getMinorAxis(), minorAxisNodePos);
        node.setDimension(getMinorAxis(), 0);
        node.setDimension(getMajorAxis(), 0);
        node = node.getNext();
        continue;
      }

      worked = true;

      node.setPosition(getMajorAxis(), nodePos);
      node.setPosition(getMinorAxis(), minorAxisNodePos);
      node.setDimension(getMinorAxis(), defaultNodeWidth);
      node.setDimension(getMajorAxis(), rowModel.getRow(rowCount).getValidateSize());
      node.validate();

      nodePos += node.getDimension(getMajorAxis());
      nodePos += borderSpacing;
      node = node.getNext();
      rowCount += 1;
    }

    if (worked)
    {
      // undo the last border spacing ...
      nodePos -= borderSpacing;
    }

    final long trailingInsets = getTrailingInsets(getMajorAxis());
    setDimension(getMajorAxis(),
            (nodePos + trailingInsets) - getPosition(getMajorAxis()));

    Log.debug("TABLE-SECTION: Leave Validate: " + defaultNodeWidth);
    setState(RenderNodeState.FINISHED);
  }


  private long computeRowWidth(final TableRowModel rowModel,
                               final int rowCounter,
                               final long givenExtraSpace,
                               final long expectedExtraSpace)
  {
    final TableRow row = rowModel.getRow(rowCounter);
    final long cellPrefWidth = row.getPreferredSize();
    final long cellMinChunk = row.getMinimumChunkSize();
    final long cellExpExtraSpace = cellPrefWidth - cellMinChunk;
    if (expectedExtraSpace == 0)
    {
      return row.getMinimumChunkSize();
    }
    else
    {
      return row.getMinimumChunkSize() +
              givenExtraSpace * cellExpExtraSpace / expectedExtraSpace;
    }
  }
  public long getPreferredSize(int axis)
  {
    throw new UnsupportedOperationException
            ("A table section has no preferred size.");
//    structValidateRows();
//    return super.getPreferredSize(axis);
  }
}
