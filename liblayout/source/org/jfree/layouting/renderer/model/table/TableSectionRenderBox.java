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
 * $Id: TableSectionRenderBox.java,v 1.5 2006/07/24 12:18:56 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import java.util.ArrayList;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;
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
    super(boxDefinition, VerticalAlign.TOP);
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
    final TableColumnModel columnModel = getTable().getColumnModel();

    boolean needsRestart = true;
    while (needsRestart)
    {
      Log.debug ("Starting validation process..");
      needsRestart = false;
      int rowCount = 0;
      RenderNode node = getFirstChild();
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
            Log.debug ("Structural change detected; restarting.");
            needsRestart = true;
          }
        }

        rowCount += 1;
        if (rowCount > rowModel.getRowCount())
        {
          // generate a new row in the model.
          final BoxDefinition boxDef = row.getBoxDefinition();
          rowModel.addRow(new TableRow
                  (boxDef.getPreferredHeight(), boxDef.getBorder()));
        }
        Log.debug ("Row validated.");
        node = node.getNext();
      }
    }
    Log.debug ("Finished validation process..");

  }

  public void structValidateRowSizes ()
  {
    Log.debug ("Starting size-validation process..");
    RenderNode node = getFirstChild();
    ArrayList rows = new ArrayList();
    while (node != null)
    {
      if (node instanceof TableRowRenderBox == false)
      {
        node = node.getNext();
        continue;
      }
      rows.add(node);
      node = node.getNext();
    }

    for (int i = 0; i < rows.size(); i++)
    {
      final TableRowRenderBox row = (TableRowRenderBox) rows.get(i);
      row.validateCellSizes(rowModel.getRow(i), i == 0, i == rows.size() - 1);
    }

    rowModel.validateSizes();
    Log.debug ("Finished size-validation process..");
  }

  public void validate()
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

    validateMargins();

    Log.debug("TABLE-SECTION: Begin Validate");

    final TableRenderBox table = getTable();
    final long borderSpacing = table.getRowSpacing();

    long nodePos = getPosition(getMajorAxis());
    final long minorAxisNodePos = getPosition(getMinorAxis());
    final long defaultNodeWidth = Math.max(0, getDimension(getMinorAxis()));

    // first run: Validate all rows under the condition that the width is
    // given. This produces the cell heights and a prelimentary a row-height.
    // If there were no rowspans, this rowheight would be the valid final height.
    rowModel.clear();

    RenderNode node = getFirstChild();
    boolean worked = false;
    int rowCount = 0;
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
      TableRowRenderBox rowBox = (TableRowRenderBox) node;

      final TableRow row = rowModel.getRow(rowCount);

      node.setPosition(getMajorAxis(), nodePos);
      node.setPosition(getMinorAxis(), minorAxisNodePos);
      node.setDimension(getMinorAxis(), defaultNodeWidth);
      node.setDimension(getMajorAxis(), 0);
      rowBox.validate(rowModel, row, rowCount, true);

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

    // Read in the computed sizes.
    node = getFirstChild();
    rowCount = 0;
    while (node != null)
    {
      if (node.isIgnorableForRendering() ||
          node instanceof TableRowRenderBox == false)
      {
        node = node.getNext();
        continue;
      }
      TableRow row = rowModel.getRow(rowCount);
      Log.debug ("Validating: " + row);
      TableRowRenderBox rowBox = (TableRowRenderBox) node;
      RenderNode cell = rowBox.getFirstChild();
      while (cell != null)
      {
        if (cell instanceof TableCellRenderBox)
        {
          TableCellRenderBox tcb = (TableCellRenderBox) cell;
          final long height = tcb.getHeight();
          final long refp = tcb.getReferencePoint(VERTICAL_AXIS);
          final long lead = tcb.getLeadingSpace(VERTICAL_AXIS);
          row.updateValidatedSize(tcb.getRowSpan(),
                  lead + refp, height - lead - refp);
          Log.debug ("Validated: " + tcb.getRowSpan() + " " +
                  height + " " + cell);
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
      TableRowRenderBox rowBox = (TableRowRenderBox) node;
      final TableRow row = rowModel.getRow(rowCount);
      node.setPosition(getMajorAxis(), nodePos);
      node.setPosition(getMinorAxis(), minorAxisNodePos);

      node.setDimension(getMinorAxis(), defaultNodeWidth);
      node.setDimension(getMajorAxis(), row.getValidateSize());
      rowBox.validate(rowModel, row, rowCount, false);

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

  protected long getPreferredSize(int axis)
  {
    throw new UnsupportedOperationException
            ("A table section has no preferred size.");
  }

  public TableSectionRenderBox getPreviousSection()
  {
    RenderNode node = getPrev();
    while (node != null)
    {
      if (node instanceof TableSectionRenderBox)
      {
        return (TableSectionRenderBox) node;
      }
      node = node.getPrev();
    }
    return null;
  }

  public TableSectionRenderBox getNextSection()
  {
    RenderNode node = getNext();
    while (node != null)
    {
      if (node instanceof TableSectionRenderBox)
      {
        return (TableSectionRenderBox) node;
      }
      node = node.getNext();
    }
    return null;
  }
}
