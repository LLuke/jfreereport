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
 * ValidateModelStep.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ValidateModelStep.java,v 1.1 2006/10/17 17:31:57 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowInfoStructure;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.model.table.cells.TableCell;
import org.jfree.util.Log;
import org.jfree.util.LogContext;

/**
 * This step checks, whether the model will be layoutable.
 *
 * Closed nodes are always layoutable. Nodes are non-layoutable, if they
 * contain boxes with a width or margin of 'auto', tables with auto-width
 * columns or if there is an open out-of-normal-flow element (floating or
 * positioned element).
 *
 * if the preferred width is AUTO, then we have to check the whole
 * thing. If (according to CSS3-Box) the width computes to the intrinsic
 * width, then the layout is not computable yet.
 *
 * For now, we always assume that all elements have a horizontal flow.
 * That simplifies all tests to whether the element is a flow root.
 *
 * For tables, the choice is a bit more complex. If the table uses the auto
 * table-layout algorithm, the model is layoutable, if the table has no
 * auto-width columns. If the table uses the fixed layout algorithm, the
 * width of the table is auto but all columns have a non-auto-width, then
 * the model will be layoutable at once. Columns with an auto-width in a
 * fixed table model become layoutable as soon as the first row of data has
 * been given.
 *
 * @author Thomas Morgner
 */
public class ValidateModelStep extends IterateStructuralProcessStep
{
  private boolean layoutableResult;
  private LogicalPageBox root;
  private static LogContext context = Log.createContext(ValidateModelStep.class);

  public ValidateModelStep()
  {
  }

  public synchronized boolean isLayoutable (LogicalPageBox root)
  {
    this.layoutableResult = true;
    this.root = root;
    startProcessing(root);
    this.root = null;
    return layoutableResult;
  }

  protected void finishBlockBox(BlockRenderBox box)
  {
    if (layoutableResult == false)
    {
      return;
    }

    // If we're here, we can be sure that there is no open row anymore.
    // Open rows would have triggered the unlayoutable-flag already ..
    if (box.isOpen() == false)
    {
      return;
    }

    if (box instanceof TableSectionRenderBox)
    {
      // check, if the section contains row-spanning cell declarations.
      // if it does, check whether there are enough rows to make these
      // constraints valid. If there are rows missing, then the section
      // is not layoutable..
      RenderNode node = box.getFirstChild();
      int expectedRows = 0;
      while (node != null)
      {
        if (node instanceof TableRowRenderBox == false)
        {
          node = node.getNext();
        }

        expectedRows -= 1;

        TableRowRenderBox row = (TableRowRenderBox) node;
        final TableRowInfoStructure rowInfoStructure = row.getRowInfoStructure();
        if (rowInfoStructure.isValidationDone())
        {
          // ok, we can take the shortcut ..
          final int cellCount = rowInfoStructure.getCellCount();
          for (int i = 0; i < cellCount; i++)
          {
            final TableCell cellAt = rowInfoStructure.getCellAt(i);
            expectedRows = Math.max (expectedRows, cellAt.getRowSpan() - 1);
          }
        }
        else
        {
          // the slow-lane: Look at the already declared cells ..
          RenderNode nodeCell = row.getFirstChild();
          while (nodeCell != null)
          {
            if (nodeCell instanceof TableCellRenderBox)
            {
              TableCellRenderBox cellBox = (TableCellRenderBox) nodeCell;
              expectedRows = Math.max (expectedRows, cellBox.getRowSpan() - 1);
            }
            nodeCell = nodeCell.getNext();
          }
        }
        node = node.getNext();
      }

      if (expectedRows >= 1)
      {
        layoutableResult = false;
      }
    }
  }

  protected boolean startBlockBox(BlockRenderBox box)
  {
    if (layoutableResult == false)
    {
      // no need to check any further ..
      return false;
    }

    if (box.isOpen() == false)
    {
      return false;
    }

    if (box instanceof TableRenderBox)
    {
      TableRenderBox table = (TableRenderBox) box;
      if (table.isAutoLayout())
      {
        // Auto-Layout means, we have to see the complete table.
        // Yes, that is expensive ..
        layoutableResult = false;
        return false;
      }

      // now dive deeper. Seek the first occurence of an table-body element..
      boolean foundBodyGroup = false;

      RenderNode node = table.getFirstChild();
      while (node != null)
      {
        if (node instanceof TableSectionRenderBox)
        {
          TableSectionRenderBox section = (TableSectionRenderBox) node;
          if (DisplayRole.TABLE_ROW_GROUP.equals(section.getDisplayRole()))
          {
            foundBodyGroup = true;
            boolean foundRow = false;
            // We found a tbody element ..
            // next - check whether the first row is closed ..
            RenderNode maybeRow = section.getVisibleFirst();
            while (maybeRow != null)
            {
              if (maybeRow instanceof TableRowRenderBox)
              {
                if (maybeRow.isOpen())
                {
                  // not layoutable - bail out ..
                  layoutableResult = false;
                  return false;
                }
                foundRow = true;
              }
              maybeRow = maybeRow.getNext();
            }

            if (foundRow == false)
            {
              // not layoutable - bail out ..
              layoutableResult = false;
              return false;
            }
          }
        }
        node = node.getNext();
      }
      if (foundBodyGroup == false)
      {
        // even if the table will never declare a tbody-group, it will become
        // layoutable as soon as it is closed.

        layoutableResult = false;
        return false;
      }
    }
    else if (box instanceof TableRowRenderBox)
    {
      TableRowRenderBox row = (TableRowRenderBox) box;
      final TableSectionRenderBox section = (TableSectionRenderBox) row.getParent();
      if (DisplayRole.TABLE_ROW_GROUP.equals(section.getDisplayRole()))
      {
        // we need to have at least one complete row of data (not counting header
        // and footer sections. The first row has no prev-element, so we simply
        // test whether there is one ..
        if (row.getVisiblePrev() == null)
        {
          layoutableResult = false;
          context.debug("Table: First Row not finished. Not layoutable.");
          return false;
        }
      }
    }
    else if (box instanceof ParagraphRenderBox)
    {
      // An paragraph must be complete before we can attempt to layout it.
      // Without that constraint, we cannot guarantee that inline-block-elements
      // will behave correctly.
      layoutableResult = false;
      return false;
    }
    return true;
  }

  protected boolean startInlineBox(InlineRenderBox box)
  {
    if (layoutableResult == false)
    {
      // no need to check any further ..
      return false;
    }
    return super.startInlineBox(box);
  }

  protected void startNormalFlow(final NormalFlowRenderBox box)
  {
    if (layoutableResult == false)
    {
      // no need to check any further ..
      return;
    }

    // todo: More generalization needed?
    if (root.getContentArea() != box)
    {
      // we only accept the root flow as open flow.
      if (box.isOpen())
      {
        layoutableResult = false;
        context.debug("Open NormalFlow detected. Not layoutable.");
      }
    }
  }
}
