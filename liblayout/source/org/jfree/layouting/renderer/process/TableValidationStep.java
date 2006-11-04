/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * TableValidationStep.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 08.09.2006 : Initial version
 */
package org.jfree.layouting.renderer.process;

import java.util.Stack;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.EmptyBoxDefinition;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableColumnGroupNode;
import org.jfree.layouting.renderer.model.table.TableColumnNode;
import org.jfree.layouting.renderer.model.table.TableLayoutInfo;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowInfoStructure;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.model.table.rows.TableRowModel;
import org.jfree.layouting.renderer.model.table.rows.TableRow;
import org.jfree.layouting.renderer.model.table.cells.ConflictingCell;
import org.jfree.layouting.renderer.model.table.cells.DataCell;
import org.jfree.layouting.renderer.model.table.cells.PlaceHolderCell;
import org.jfree.layouting.renderer.model.table.cells.TableCell;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnGroup;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;

/**
 * Another static processing step which validates the table structure.
 *
 * @author Thomas Morgner
 */
public class TableValidationStep extends IterateStructuralProcessStep
{
  public static class TableInfoStructure
  {
    private TableRenderBox table;
    private TableColumnModel columnModel;
    private int columns;

    public TableInfoStructure(final TableRenderBox table)
    {
      this.table = table;
      this.columnModel = table.getColumnModel();
      this.columns = columnModel.getColumnCount();
    }

    public TableRenderBox getTable()
    {
      return table;
    }

    public TableColumnModel getColumnModel()
    {
      return columnModel;
    }

    public int getColumns()
    {
      return columns;
    }

    public void setColumns(final int columns)
    {
      this.columns = columns;
    }
  }

  private Stack tableStack;
  private TableInfoStructure currentTable;

  public TableValidationStep()
  {
    tableStack = new Stack();
  }

  public void validate(LogicalPageBox box)
  {
    tableStack.clear();
    currentTable = null;
    startProcessing(box);
    if (tableStack.isEmpty() == false)
    {
      throw new IllegalStateException();
    }
  }

  protected boolean startBlockBox(BlockRenderBox box)
  {
    if (box instanceof TableRenderBox)
    {
      final TableRenderBox table = (TableRenderBox) box;
      currentTable = new TableInfoStructure(table);
      tableStack.push(currentTable);

      // if the table is not open and has been validated, then ignore it ..
      if (table.isOpen() == false)
      {
        final TableLayoutInfo tableInfo = table.getTableInfo();
        if (tableInfo.isStructureValidated())
        {
          // sadly, it may contain sub-tables .. (heck, try to grab that?)
          return true;
        }
        else
        {
          validateTable();
          return true;
        }
      }
      else
      {
        // An open table always needs revalidation ...
        validateTable();
      }
    }

    return true;
  }

  protected void finishBlockBox(BlockRenderBox box)
  {
    if (box instanceof TableRenderBox)
    {
      currentTable = (TableInfoStructure) tableStack.pop();
    }
  }

  /**
   * We have to check the table as a complex structure. Writing a sequential
   * parser that waits for events to drive by does not work here, as we have to
   * go back and forward all the time ..
   */
  private void validateTable()
  {
    // first step: Check, whether the column model is complete.
    validatePredefinedColumns();
    validateRowStructure();

    final TableRenderBox table = currentTable.getTable();
    final TableLayoutInfo tableInfo = table.getTableInfo();
    if (table.isOpen() == false)
    {
      tableInfo.setStructureValidated(true);
    }
  }

  private void validateFinalRows(final TableSectionRenderBox section)
  {
    // check the last row for row-spanning cells ..

    // first, find the last row of the section ..
    RenderNode rowNode = section.getLastChild();
    TableRowRenderBox lastRow = null;
    while (rowNode != null)
    {
      if (rowNode instanceof TableRowRenderBox)
      {
        lastRow = (TableRowRenderBox) rowNode;
        break;
      }
      rowNode = rowNode.getPrev();
    }

    final TableRowModel rowModel = section.getRowModel();
    // There is no last row - so this sections is empty.
    while (lastRow != null)
    {

      TableRowRenderBox autoGeneratedRow = null;
      TableRowInfoStructure autoRowInfo = null;

      final TableRowInfoStructure rowInfo = lastRow.getRowInfoStructure();
      for (int i = 0; i < rowInfo.getCellCount(); i++)
      {
        // seek row-spanning cells ..
        final TableCell cell = rowInfo.getCellAt(i);
        if (cell.getRowSpan() > 1)
        {
          // that's one, for instance ...
          if (autoGeneratedRow == null)
          {
            autoGeneratedRow =
                    new TableRowRenderBox(new EmptyBoxDefinition(), true);
            rowModel.addRow(new TableRow());

            autoRowInfo = autoGeneratedRow.getRowInfoStructure();
            for (int emptyCellCnt = 0; emptyCellCnt < i; emptyCellCnt += 1)
            {
              final TableCellRenderBox autoCell =
                      new TableCellRenderBox(new EmptyBoxDefinition(), true);
              autoCell.close();
              autoCell.setColumnIndex(emptyCellCnt);
              final DataCell dataCell = new DataCell(autoCell, 1, 1);
              autoRowInfo.addCell(dataCell);
              autoGeneratedRow.addChild(autoCell);
            }
          }

          final PlaceHolderCell placeHolder;
          if (cell instanceof DataCell)
          {
            placeHolder = new PlaceHolderCell
                    ((DataCell) cell, cell.getRowSpan() - 1, cell.getColSpan());
          }
          else if (cell instanceof PlaceHolderCell)
          {
            PlaceHolderCell prevCell = (PlaceHolderCell) cell;
            placeHolder = new PlaceHolderCell
                    (prevCell.getSourceCell(), cell.getRowSpan() - 1, cell.getColSpan());
          }
          else
          {
            throw new IllegalStateException("Unexpected cell type.");
          }
          autoRowInfo.addCell(placeHolder);
        }
        else if (autoGeneratedRow != null)
        {
          // only add, if the auto-row-box has been already generated ..
          // this prevents the generation of totally empty rows ..
          final TableCellRenderBox autoCell =
                  new TableCellRenderBox(new EmptyBoxDefinition(), true);
          autoCell.close();
          autoCell.setColumnIndex(i);
          final DataCell dataCell = new DataCell(autoCell, 1, 1);
          autoRowInfo.addCell(dataCell);
          autoGeneratedRow.addChild(autoCell);
        }
      }

      if (autoGeneratedRow != null)
      {
        autoGeneratedRow.close();
        autoRowInfo.setValidationDone(true);
        section.addGeneratedChild(autoGeneratedRow);
      }

      // no matter what we do, this one can never generate new columns;
      // it only continues existing ones ..
      lastRow = autoGeneratedRow;
    }
  }

  private void validateRowStructure()
  {
    // check all the newly closed rows and look at their number of columns
    // (cache that one for faster access)
    final TableRenderBox table = currentTable.getTable();
    if (table.getTableInfo().isPredefinedColumnsValidated() == false)
    {
      // the predefined columns must be validated first ...
      return;
    }

    final TableColumnModel columnModel = currentTable.getColumnModel();
    // ok, the real work starts here. Forward-Traverse the table by its table-
    // sections.
    RenderNode node = table.getFirstChild();
    while (node != null)
    {
      if (node instanceof TableSectionRenderBox == false)
      {
        node = node.getNext();
        continue;
      }
      // OK; nice we are done. All other column definitions will be ignored
      final TableSectionRenderBox section = (TableSectionRenderBox) node;

      if (section.isStructureValidated() == false)
      {
        // we start with an empty prev-struct; that signals that there are no
        // spanned cells in a first row.
        TableRowInfoStructure prevInfoStruct =
                new TableRowInfoStructure();
        RenderNode rowNode = section.getFirstChild();
        while (rowNode != null)
        {
          if (rowNode.isOpen())
          {
            throw new IllegalStateException
                    ("An open row cannot be part of a layoutable model.");
          }

          // OK, we got a non open row. Now we traverse through the table from
          // top to bottom building the validated row model.
          if (rowNode instanceof TableRowRenderBox == false)
          {
            rowNode = rowNode.getNext();
            continue;
          }

          final TableRowInfoStructure infoStruct =
                  validateRow(section, rowNode, prevInfoStruct);
          final int cellCount = infoStruct.getCellCount();
          if (currentTable.getColumns() < cellCount)
          {
            currentTable.setColumns(cellCount);
          }
          // And finally ... add the new columns to the model ..
          while (cellCount > columnModel.getColumnCount())
          {
            columnModel.addAutoColumn();
          }

          rowNode = rowNode.getNext();
          prevInfoStruct = infoStruct;
        }

        if (section.isOpen() == false)
        {
          // when the section has been closed, we can stop the revalidation ...
          validateFinalRows(section);
          section.setStructureValidated(true);
        }
      }

      node = node.getNext();
    }
  }

  private TableRowInfoStructure validateRow(final TableSectionRenderBox section,
                                            final RenderNode rowNode,
                                            final TableRowInfoStructure prevInfoStruct)
  {
    final TableRowRenderBox rowBox = (TableRowRenderBox) rowNode;
    final TableRowInfoStructure infoStruct =
            rowBox.getRowInfoStructure();

    // As we validate only closed tables (and as we do not allow changes once
    // the table as closed (except for adding auto-cells, of course, which is
    // a necessary evil exception to the rule), we have to do this only once
    // per row ..
    if (infoStruct.isValidationDone())
    {
      return infoStruct;
    }

    RenderNode cellNode = rowBox.getFirstChild();
    int cellPosition = 0;

    while (cellNode != null)
    {
      if (cellNode instanceof TableCellRenderBox == false)
      {
        cellNode = cellNode.getNext();
        continue;
      }

      TableCellRenderBox cellBox = (TableCellRenderBox) cellNode;
      // we got a cell. Now check our info-struct ..
      if (cellPosition >= prevInfoStruct.getCellCount())
      {
        cellBox.setColumnIndex(cellPosition);
        cellPosition = validateSafeCellPos(cellBox, infoStruct, cellPosition);
      }
      else
      {
        cellPosition = findCellPosition(cellPosition, prevInfoStruct, infoStruct);

        // OK, so we have the insertation point. Lets add our cell there ..
        final int colSpan = cellBox.getColSpan();
        final int rowSpan = cellBox.getRowSpan();
        DataCell dataCell = new DataCell(cellBox, rowSpan, colSpan);
        infoStruct.addCell(dataCell);
        cellBox.setColumnIndex(cellPosition);

        cellPosition += 1;
        for (int i = 1; i < colSpan; i++)
        {
          // if there's a previous cell at the current position - check it
          // maybe we have a conflicting instruction here

          if (cellPosition < prevInfoStruct.getCellCount())
          {
            final TableCell prevCell = prevInfoStruct.getCellAt(cellPosition);
            if (prevCell.getRowSpan() > 1)
            {
              // thats a conflict. Oh, no!
              final ConflictingCell conflictingCell =
                      new ConflictingCell(dataCell, rowSpan, colSpan - i);
              if (prevCell instanceof ConflictingCell)
              {
                // Oh, there's already a conflict? Coooool...
                // (damn, users, fix your table definitions!)
                ConflictingCell prevConflictCell = (ConflictingCell) prevCell;
                final int count = prevConflictCell.getConflictingCellCount();
                for (int x = 0; x < count; x++)
                {
                  PlaceHolderCell phc = prevConflictCell.getConflictingCell(x);
                  if (phc.getRowSpan() > 1)
                  {
                    final PlaceHolderCell placeHolderCell =
                            new PlaceHolderCell(phc.getSourceCell(),
                                    phc.getRowSpan() - 1,
                                    phc.getColSpan());
                    conflictingCell.addConflictingCell(placeHolderCell);
                  }
                }
              }
              else if (prevCell instanceof PlaceHolderCell)
              {
                PlaceHolderCell prevPHCell = (PlaceHolderCell) prevCell;
                // a new conflict ...
                final PlaceHolderCell placeHolderCell =
                        new PlaceHolderCell(prevPHCell.getSourceCell(),
                                prevCell.getRowSpan() - 1,
                                prevCell.getColSpan());
                conflictingCell.addConflictingCell(placeHolderCell);
              }
              else if (prevCell instanceof DataCell)
              {
                final PlaceHolderCell placeHolderCell =
                        new PlaceHolderCell((DataCell) prevCell,
                                prevCell.getRowSpan() - 1,
                                prevCell.getColSpan());
                conflictingCell.addConflictingCell(placeHolderCell);
              }
              else
              {
                throw new IllegalStateException("Unexpected cell type.");
              }

              infoStruct.addCell(conflictingCell);
            }
            else
            {
              final PlaceHolderCell placeHolderCell =
                      new PlaceHolderCell(dataCell, rowSpan, colSpan - i);
              // a safe position. No conflicts ..
              infoStruct.addCell(placeHolderCell);
            }
          }
          else
          {
            final PlaceHolderCell placeHolderCell =
                    new PlaceHolderCell(dataCell, rowSpan, colSpan - i);
            // no cells means: no conflicts. Thats always the easiest thing
            infoStruct.addCell(placeHolderCell);
          }
          cellPosition += 1;
        }
      }

      cellNode = cellNode.getNext();
    }

    final TableRow row = new TableRow(rowBox.getBorder());
    section.getRowModel().addRow(row);

    infoStruct.setValidationDone(true);

    return infoStruct;
  }

  private int findCellPosition(int cellPosition,
                               final TableRowInfoStructure prevInfoStruct,
                               final TableRowInfoStructure infoStruct)
  {
    // lets search for a proper insertation point.
    // first, skip all row-spanning cellpositions.
    while (cellPosition < prevInfoStruct.getCellCount())
    {
      // look at the prev-cell definition
      final TableCell prevCell = prevInfoStruct.getCellAt(cellPosition);
      if (prevCell.getRowSpan() > 1)
      {
        final DataCell dataCell;
        if (prevCell instanceof DataCell)
        {
          dataCell = (DataCell) prevCell;
        }
        else if (prevCell instanceof PlaceHolderCell)
        {
          PlaceHolderCell placeHolderCell = (PlaceHolderCell) prevCell;
          dataCell = placeHolderCell.getSourceCell();
        }
        else
        {
          throw new IllegalStateException("Unexpected cell type.");
        }
        // oh no, a spanned cell that conflicts with the current cell.
        // that means: Push the insertation point to the left ..
        // and add placeholders to the current row-info-struct ..
        final int colSpan = prevCell.getColSpan();
        // reduce the rowspan by one, as this is a new row ..
        final int rowSpan = prevCell.getRowSpan() - 1;
        for (int i = 0; i < colSpan; i++)
        {
          final PlaceHolderCell placeHolderCell =
                  new PlaceHolderCell
                          (dataCell, rowSpan, colSpan - i);
          infoStruct.addCell(placeHolderCell);
        }
        cellPosition += prevCell.getColSpan();
      }
      else
      {
        // found a sweet-spot ..
        break;
      }
    }
    return cellPosition;
  }

  private int validateSafeCellPos(final TableCellRenderBox cellBox,
                                  final TableRowInfoStructure infoStruct,
                                  int cellPosition)
  {
    // no such cell at that position. Good, add the plain cell.
    final int colSpan = cellBox.getColSpan();
    final int rowSpan = cellBox.getRowSpan();
    DataCell dataCell = new DataCell(cellBox, rowSpan, colSpan);
    infoStruct.addCell(dataCell);
    cellPosition += 1;
    for (int i = 1; i < colSpan; i++)
    {
      final PlaceHolderCell placeHolderCell =
              new PlaceHolderCell(dataCell, rowSpan, colSpan - i);
      infoStruct.addCell(placeHolderCell);
      cellPosition += 1;
    }
    return cellPosition;
  }

  private void validatePredefinedColumns()
  {
    final TableRenderBox table = currentTable.getTable();
    if (table.getTableInfo().isPredefinedColumnsValidated())
    {
      // all work as been done already ...
      return;
    }

    final TableColumnModel columnModel = table.getColumnModel();

    int colCount = 0;
    RenderNode node = table.getFirstChild();
    while (node != null)
    {
      if (node instanceof TableColumnNode)
      {
        TableColumnNode columnNode = (TableColumnNode) node;
        if (colCount >= columnModel.getColumnCount())
        {
          // No column exists at that position ..
          final BoxDefinition boxDefinition = columnNode.getBoxDefinition();
          final Border border = boxDefinition.getBorder();
          final RenderLength width = boxDefinition.getPreferredWidth();
          final TableColumn column = new TableColumn(border, width, false);
          final TableColumnGroup group = new TableColumnGroup();
          group.addColumn(column);
          columnModel.addColumnGroup(group);
          colCount += 1;
        }
        else
        {
          // We do not change existing columns. That validation should be the
          // first that checks column definitions, so that state should always
          // be correct.
        }
      }
      else if (node instanceof TableColumnGroupNode)
      {
        TableColumnGroupNode groupNode = (TableColumnGroupNode) node;

        final boolean newGroupGenerated;
        final TableColumnGroup group;
        if (colCount >= columnModel.getColumnCount())
        {
          group = new TableColumnGroup(groupNode.getBorder());
          newGroupGenerated = true;
        }
        else
        {
          group = columnModel.getGroupForIndex(colCount);
          newGroupGenerated = false;
        }

        RenderNode groupColNode = groupNode.getFirstChild();
        while (groupColNode != null)
        {
          if (groupColNode instanceof TableColumnNode)
          {
            final TableColumnNode columnNode = (TableColumnNode) groupColNode;
            if (colCount >= columnModel.getColumnCount())
            {
              final BoxDefinition boxDefinition = columnNode.getBoxDefinition();
              final Border border = boxDefinition.getBorder();
              final RenderLength width = boxDefinition.getPreferredWidth();
              final TableColumn column = new TableColumn(border, width, false);
              group.addColumn(column);
            }
            else
            {
              colCount += 1;
            }
          }
          else
          {
            // ignore silently ..
          }
          groupColNode = groupColNode.getNext();
        }

        if (newGroupGenerated)
        {
          columnModel.addColumnGroup(group);
          colCount += group.getColumnCount();
        }

      }
      else if (node instanceof TableSectionRenderBox)
      {
        // OK; nice we are done. All other column definitions will be ignored
        table.getTableInfo().setPredefinedColumnsValidated(true);
      }
      else
      {
        // ignore ...
      }
      node = node.getNext();
    }
  }
}