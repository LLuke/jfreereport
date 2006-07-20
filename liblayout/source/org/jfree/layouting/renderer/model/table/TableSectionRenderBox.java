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
 * $Id: TableSectionRenderBox.java,v 1.2 2006/07/18 17:26:32 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
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
  public TableSectionRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
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
    if (getTable().isCollapsingBorderModel())
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
      return;
    }

    super.validateMargins();
  }

  public Border getBorder()
  {
    if (getTable().isCollapsingBorderModel())
    {
      // ignore all borders.
      return Border.createEmptyBorder();
    }

    return super.getBorder();
  }

  private void validateRows ()
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
        row.validateCells();
        if (columnModel.getColumnCount() != size)
        {
          // a structural change. Great, we have to restart.
          node = getFirstChild();
          Log.debug ("Structural change detected; restarting.");
          continue;
        }
      }

      Log.debug ("Row validated.");
      node = node.getNext();
    }

    node = getFirstChild();
    Log.debug ("Starting size-validation process..");
    while (node != null)
    {
      if (node instanceof TableRowRenderBox == false)
      {
        node = node.getNext();
        continue;
      }

      TableRowRenderBox row = (TableRowRenderBox) node;
      row.validateCellSizes();
      node = node.getNext();
    }

    columnModel.validateSizes();
    Log.debug ("Finished size-validation process..");
  }

//  private TableRowRenderBox findLastRow()
//  {
//    TableRowRenderBox row = null;
//    RenderNode node = getLastChild();
//    while (node != null)
//    {
//      if (node instanceof TableRowRenderBox)
//      {
//        row = (TableRowRenderBox) node;
//        break;
//      }
//    }
//    return row;
//  }

  public void validate()
  {
    // we need to validate our rows ...
    validateRows();
    super.validate();
  }

  public long getPreferredSize(int axis)
  {
    validateRows();
    return super.getPreferredSize(axis);
  }
}
