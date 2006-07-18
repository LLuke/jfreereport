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
 * TableRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableRenderBox.java,v 1.2 2006/07/18 14:40:28 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderNodeState;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.keys.table.BorderCollapse;

/**
 * A table render box contains table header, table footer and the table body.
 * The table body itself may also contain table header cells - which get repeated
 * after pagebreaks.
 *
 * Tables contain more than just rows, in fact, they are separated into three
 * sections.
 *
 * @author Thomas Morgner
 */
public class TableRenderBox extends BlockRenderBox
{
  private TableColumnModel columnModel;
  private boolean columnModelBuilt;
  private boolean needsPruning;
  private LayoutContext layoutContext;

  public TableRenderBox(final BoxDefinition boxDefinition,
                        final LayoutContext layoutContext)
  {
    super(boxDefinition);
    this.layoutContext = layoutContext;
    columnModel = new TableColumnModel();
  }

  public void validate()
  {
    if (RenderNodeState.FINISHED.equals(getState()))
    {
      return;
    }

    prune();

    if (!columnModelBuilt)
    {
      if (isDataAvailable())
      {
        // process the columns ..
        buildColumnModel();
        columnModelBuilt = true;
      }
    }


    // The layouting here is rather simple. We do a simple block layouting;
    // and it is guaranteed, that the various sections do not overlap. It
    // is not possible for a table-cell of the header section to interact
    // with the cells of the body- or footer-sections.
    if (columnModel.isIncrementalModeSupported() == false)
    {
      // If we are not in the incremental mode, then we cannot continue unless
      // we've seen everything.
      if (isOpen())
      {
        return;
      }
    }


    setState(RenderNodeState.FINISHED);
  }

  private void buildColumnModel()
  {
    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node instanceof TableColumnGroupNode)
      {
        final TableColumnGroupNode group = (TableColumnGroupNode) node;

        // prefer the declared columns instead of the colspan attribute.
        RenderNode cnode = group.getFirstChild();
        boolean added = false;
        while (cnode != null)
        {
          if (cnode instanceof TableColumnNode)
          {
            final TableColumnNode column = (TableColumnNode) cnode;
            int colspan = column.getColSpan();
            for (int i = 0; i < colspan; i++)
            {
              columnModel.addColumn(column);
              added = true;
            }
          }
          cnode = cnode.getNext();
        }

        if (added == false)
        {
          final int colspan = group.getColSpan();
          for (int i = 0; i < colspan; i++)
          {
            columnModel.addColumn(new TableColumnNode
                    (group.getBoxDefinition(), group.getContext()));
          }
        }

        node = node.getNext();
        remove(group);
      }
      else if (node instanceof TableColumnNode)
      {
        final TableColumnNode column = (TableColumnNode) node;
        int colspan = column.getColSpan();
        for (int i = 0; i < colspan; i++)
        {
          columnModel.addColumn(column);
        }
        node = node.getNext();
        remove(column);
      }
      else if (node instanceof TableSectionRenderBox)
      {
        break;
      }
      else
      {
        node = node.getNext();
      }
    }
  }

  public long getPreferredSize(int axis)
  {
    prune();
    return super.getPreferredSize(axis);
  }

  public TableColumnModel getColumnModel()
  {
    return columnModel;
  }

  private void prune()
  {
    if (needsPruning == false)
    {
      return;
    }

    RenderNode last = getLastChild();
    while (last != null)
    {
      if (last.isOpen() ||
          last instanceof TableSectionRenderBox ||
          last instanceof TableColumnGroupNode ||
          last instanceof TableColumnNode)
      {
        last = last.getPrev();
      }
      else
      {
        RenderNode prev = last.getPrev();
        remove(last);
        last = prev;
      }
    }

    needsPruning = false;
  }

  public void addChild(final RenderNode child)
  {
    super.addChild(child);
    needsPruning = true;
  }

  public boolean isLayoutable()
  {
    return columnModel.isIncrementalModeSupported();
  }

  private boolean isDataAvailable()
  {
    RenderNode last = getLastChild();
    while (last != null)
    {
      if (last instanceof TableSectionRenderBox)
      {
        return true;
      }
      last = last.getPrev();
    }
    return false;
  }

  public boolean isCollapsingBorderModel()
  {
    final CSSValue borderModel =
            layoutContext.getStyle().getValue(TableStyleKeys.BORDER_COLLAPSE);
    return BorderCollapse.COLLAPSE.equals(borderModel);
  }
}
