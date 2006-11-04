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
 * TableRowHeightStep.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import java.util.Stack;

import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.model.table.rows.TableRow;
import org.jfree.layouting.renderer.model.table.rows.TableRowModel;

/**
 * Creation-Date: 10.10.2006, 14:10:08
 *
 * @author Thomas Morgner
 */
public class TableRowHeightStep extends IterateVisualProcessStep
{
  public static class TableInfoStructure
  {
    private TableRenderBox table;
    private TableColumnModel columnModel;
    private TableRowModel rowModel;
    private int rowNumber;
    private long position;

    public TableInfoStructure(final TableRenderBox table)
    {
      this.table = table;
      this.columnModel = table.getColumnModel();
    }

    public TableRow getRow()
    {
      return rowModel.getRow(rowNumber);
    }

    public long getPosition()
    {
      return position;
    }

    public void setPosition(final long position)
    {
      this.position = position;
    }

    public TableRenderBox getTable()
    {
      return table;
    }

    public TableColumnModel getColumnModel()
    {
      return columnModel;
    }

    public TableRowModel getRowModel()
    {
      return rowModel;
    }

    public void setRowModel(final TableRowModel rowModel)
    {
      this.rowModel = rowModel;
      this.rowNumber = 0;
    }

    public int getRowNumber()
    {
      return rowNumber;
    }

    public void increaseRowNumber()
    {
      this.rowNumber += 1;
    }
  }

  private Stack tableStack;
  private TableInfoStructure currentTable;
  private long shiftDistance;

  public TableRowHeightStep()
  {
    tableStack = new Stack();
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    box.setY(box.getY() + shiftDistance);
    return true;
  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    node.setY(node.getY() + shiftDistance);
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    node.setY(node.getY() + shiftDistance);
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
//    Log.debug ("Shifting: " + shiftDistance + " : " + box);
    box.setY(box.getY() + shiftDistance);
    if (box instanceof TableRenderBox)
    {
      final TableRenderBox table = (TableRenderBox) box;
      currentTable = new TableInfoStructure(table);
      currentTable.setPosition(table.getY());

      tableStack.push(currentTable);
    }
    else if (box instanceof TableSectionRenderBox)
    {
      final TableSectionRenderBox sectionBox = (TableSectionRenderBox) box;
      currentTable.setRowModel(sectionBox.getRowModel());
    }

    // caching would be cool?
    return true;
  }

  protected void finishBlockLevelBox(final RenderBox box)
  {
    if (box instanceof TableCellRenderBox)
    {
      TableCellRenderBox cellBox = (TableCellRenderBox) box;
      final int rowSpan = cellBox.getRowSpan();
      currentTable.getRow().updateValidatedSize(rowSpan, 0, cellBox.getHeight());
    }
    else if (box instanceof TableRowRenderBox)
    {
      currentTable.increaseRowNumber();
    }
    else if (box instanceof TableSectionRenderBox)
    {
      finishSection((TableSectionRenderBox) box);
      currentTable.setRowModel(null);
    }
    else if (box instanceof TableRenderBox)
    {
      final long oldHeight = box.getHeight();
      final long newHeight = currentTable.getPosition() - box.getY();
      box.setHeight(newHeight);

      currentTable = (TableInfoStructure) tableStack.pop();

      // And finally: Shift everything down ..
      final long shift = newHeight - oldHeight;
//      Log.debug ("Shifting everything after the table by: " + shift +
//          " (" + shiftDistance + ")");
      this.shiftDistance += shift;
    }
  }

  protected void finishSection(TableSectionRenderBox section)
  {
    // OK; a complete section is a coolness factor. Lets compute something.
    // Grab the model of all available rows ..
    final TableRowModel rowModel = section.getRowModel();
    rowModel.validateActualSizes();
    long position = currentTable.getPosition();

    // Second step: Apply the row heights to all cells.
    // + Align all cells.
    final TableRow[] rows = rowModel.getRows();
    RenderNode rowNode = section.getFirstChild();
    boolean firstRow = true;
    while (rowNode != null)
    {
      if (rowNode instanceof TableRowRenderBox == false)
      {
        rowNode = rowNode.getNext();
        continue;
      }

      final TableRowRenderBox rowBox = (TableRowRenderBox) rowNode;
      final int rowNumber = rowBox.getRowInfoStructure().getRowNumber();
      final TableRow row = rows[rowNumber];
      final long validatedRowHeight = row.getValidateSize();

      if (firstRow)
      {
        firstRow = false;
      }
      else
      {
        position += rowModel.getRowSpacing();
      }

      final long oldPosition = rowBox.getY();
      final long shift = position - oldPosition;
      shift(rowBox, shift);
      shiftDistance += shift;
//      Log.debug ("Row shifts: " + shift + " (" + shiftDistance + ")");

      RenderNode cellNode = rowBox.getFirstChild();
      while (cellNode != null)
      {
        if (cellNode instanceof TableCellRenderBox == false)
        {
          cellNode = cellNode.getNext();
          continue;
        }

        final TableCellRenderBox cellBox = (TableCellRenderBox) cellNode;
        final long cellShift = position - cellBox.getY();
        if (cellShift != 0)
        {
          shift(cellBox, cellShift);
          // this is an inner shift and therefore it has no influence on the
          // global shiftdistance
        }

        cellBox.setHeight(validatedRowHeight);
        // Todo: now align all the childs of the cellbox.

        cellNode = cellNode.getNext();
      }

      rowBox.setHeight(validatedRowHeight);
      position += validatedRowHeight;
      rowNode = rowNode.getNext();
    }

    // finally shift down all the content that comes afterwards.
    // We have to update our parent's height as well as we extended the height
    // of the table ..
    final long newHeight = position - section.getY();
    final long extendedHeight = newHeight - section.getHeight();
    section.setHeight(newHeight);

    RenderNode parent = section.getParent();
    while (parent != null)
    {
      parent.setHeight(parent.getHeight() + extendedHeight);
      parent = parent.getParent();
    }

    // We do not perform a real shift, as this would be to expensive.
    // we simply store the location--
    currentTable.setPosition(position);

  }

  /**
   * Shifts the cell and all direct childs.
   *
   * @param node
   * @param shift
   */
  private void shift(final RenderNode node, final long shift)
  {
    final long y = node.getY();
    node.setY(y + shift);

    if (node instanceof RenderBox == false)
    {
      return;
    }

    // Argghhh, a render box. Shift all childs too..
    RenderBox box = (RenderBox) node;
    RenderNode child = box.getFirstChild();
    while (child != null)
    {
      shift(child, shift);
      child = child.getNext();
    }
  }

  public void compute(final LogicalPageBox box)
  {
    shiftDistance = 0;
    startProcessing(box);
    shiftDistance = 0;
  }
}