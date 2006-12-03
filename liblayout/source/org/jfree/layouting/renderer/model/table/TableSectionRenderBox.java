/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.model.table.rows.SeparateRowModel;
import org.jfree.layouting.renderer.model.table.rows.TableRowModel;

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
  private boolean structureValidated;

  public TableSectionRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
    this.rowModel = new SeparateRowModel(this);
  }

  public void appyStyle(LayoutContext context, OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);
    this.displayRole = context.getValue(BoxStyleKeys.DISPLAY_ROLE);
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

  public TableRowModel getRowModel()
  {
    return rowModel;
  }
//
//  public void structValidateRows ()
//  {
//    final TableColumnModel columnModel = getTable().getColumnModel();
//
//    boolean needsRestart = true;
//    while (needsRestart)
//    {
//      Log.debug ("Starting validation process..");
//      needsRestart = false;
//      int rowCount = 0;
//      RenderNode node = getFirstChild();
//      while (node != null)
//      {
//        if (node instanceof TableRowRenderBox == false)
//        {
//          node = node.getNext();
//          continue;
//        }
//
//        // A row.
//        TableRowRenderBox row = (TableRowRenderBox) node;
//        if (row.isStructureDirty())
//        {
//          final int size = columnModel.getColumnCount();
//          row.validateStructure();
//          if (columnModel.getColumnCount() != size)
//          {
//            // a structural change. Great, we have to restart.
//            Log.debug ("Structural change detected; restarting.");
//            needsRestart = true;
//          }
//        }
//
//        rowCount += 1;
//        if (rowCount > rowModel.getRowCount())
//        {
//          // generate a new row in the model.
//          final BoxDefinition boxDef = row.getBoxDefinition();
//          rowModel.addRow(new TableRow
//                  (boxDef.getBorder()));
//        }
//        Log.debug ("Row validated.");
//        node = node.getNext();
//      }
//    }
//    Log.debug ("Finished validation process..");
//
//  }

  public boolean isStructureValidated()
  {
    return structureValidated;
  }

  public void setStructureValidated(final boolean structureValidated)
  {
    this.structureValidated = structureValidated;
  }
}
