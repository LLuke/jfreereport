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
 * $Id: TableRenderBox.java,v 1.3 2006/07/18 17:26:32 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.input.style.keys.table.BorderCollapse;
import org.jfree.layouting.input.style.keys.table.EmptyCells;
import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.keys.table.TableLayout;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderNodeState;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnGroup;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;

/**
 * A table render box contains table header, table footer and the table body.
 * The table body itself may also contain table header cells - which get
 * repeated after pagebreaks.
 * <p/>
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
  private boolean displayEmptyCells;
  private boolean collapsingBorderModel;
  private boolean autoLayout;

  public TableRenderBox(final BoxDefinition boxDefinition,
                        final LayoutContext layoutContext)
  {
    super(boxDefinition);

    CSSValue emptyCellsVal = layoutContext.getStyle().getValue
            (TableStyleKeys.EMPTY_CELLS);
    this.displayEmptyCells = EmptyCells.SHOW.equals(emptyCellsVal);

    final CSSValue borderModel =
            layoutContext.getStyle().getValue(TableStyleKeys.BORDER_COLLAPSE);
    this.collapsingBorderModel = BorderCollapse.COLLAPSE.equals(borderModel);


    final CSSValue layoutModel =
            layoutContext.getStyle().getValue(TableStyleKeys.TABLE_LAYOUT);
    this.autoLayout = TableLayout.AUTO.equals(layoutModel);

    this.columnModel = new TableColumnModel(this);

  }

  public boolean isAutoLayout()
  {
    return autoLayout;
  }

  public void validate()
  {
    if (RenderNodeState.FINISHED.equals(getState()))
    {
      return;
    }

    prune();
    buildColumnModel();

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

    columnModel.validate();

    super.validate();

    setState(RenderNodeState.FINISHED);
  }

  private void buildColumnModel()
  {
    if (columnModelBuilt)
    {
      return;
    }

    if (isDataAvailable() == false)
    {
      return;
    }

    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node instanceof TableColumnGroupNode)
      {
        final TableColumnGroupNode groupNode = (TableColumnGroupNode) node;
        final TableColumnGroup group =
                new TableColumnGroup(groupNode.getBorder());

        // prefer the declared columns instead of the colspan attribute.
        RenderNode cnode = groupNode.getFirstChild();
        boolean added = false;
        while (cnode != null)
        {
          if (cnode instanceof TableColumnNode)
          {
            final TableColumnNode column = (TableColumnNode) cnode;
            final RenderLength preferredWidth =
                    column.getDefinition().getPreferredWidth();
            final Border border = column.getDefinition().getBorder();
            int colspan = column.getColspan();
            for (int i = 0; i < colspan; i++)
            {
              added = true;
              group.addColumn(new TableColumn(preferredWidth, border));
            }
          }

          cnode = cnode.getNext();
        }

        if (added == false)
        {
          final RenderLength preferredWidth =
                  groupNode.getBoxDefinition().getPreferredWidth();
          final int colspan = groupNode.getColSpan();
          for (int i = 0; i < colspan; i++)
          {
            group.addColumn(new TableColumn(preferredWidth, group.getBorder()));
          }
        }

        columnModel.addColumnGroup(group);
        node = node.getNext();
        remove(groupNode);
      }
      else if (node instanceof TableColumnNode)
      {
        final TableColumnNode column = (TableColumnNode) node;
        final RenderLength preferredWidth =
                column.getDefinition().getPreferredWidth();
        final Border border = column.getDefinition().getBorder();
        final int colspan = column.getColspan();
        final TableColumnGroup group =
                new TableColumnGroup(Border.createEmptyBorder());

        for (int i = 0; i < colspan; i++)
        {
          group.addColumn(new TableColumn(preferredWidth, border));
        }
        columnModel.addColumnGroup(group);
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

    columnModel.validate();
    columnModelBuilt = true;
  }

  public long getPreferredSize(int axis)
  {
    prune();
    buildColumnModel();
    if (axis == HORIZONTAL_AXIS)
    {
      return columnModel.getPreferredSize();
    }
    return super.getPreferredSize(axis);
  }

  public long getMinimumChunkSize(int axis)
  {
    prune();
    buildColumnModel();
    if (axis == HORIZONTAL_AXIS)
    {
      return columnModel.getMinimumChunkSize();
    }
    return super.getMinimumChunkSize(axis);
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
    return collapsingBorderModel;
  }

  public Border getBorder()
  {
    if (isCollapsingBorderModel())
    {
      // ignore all borders.
      return Border.createEmptyBorder();
    }

    return super.getBorder();
  }

  public boolean isDisplayEmptyCells()
  {
    return displayEmptyCells;
  }

  /**
   * This needs to be adjusted to support vertical flows as well.
   *
   * @param axis
   * @param node
   * @return
   */
  public long getEffectiveLayoutSize(int axis)
  {
    // 17.5.2 Table width algorithms: the 'table-layout' property
    //
    // Note that this section overrides the rules that apply to
    // calculating widths as described in section 10.3.

//    if (isAutoLayout())
//    {
//
//    }
    // We assume Auto-Layout for now. Yes, that needs a change in the future.
    final RenderLength preferredWidth =
            getBoxDefinition().getPreferredWidth();
    final long minChunk = columnModel.getMinimumChunkSize();
    final long prefSize = columnModel.getPreferredSize();
    final long contextWidth = getComputedBlockContextWidth();
    final long nodeWidth;
    if (RenderLength.AUTO.equals(preferredWidth))
    {
      if (prefSize < contextWidth)
      {
        nodeWidth = prefSize;
      }
      else
      {
        nodeWidth = Math.min (minChunk, contextWidth);
      }
    }
    else
    {
      nodeWidth = Math.min (minChunk, contextWidth);
    }

    final long margins = getLeadingSpace(axis) + getTrailingSpace(axis);
    final long l = (nodeWidth - margins);
    if (l < 0)
    {
      return 0;
    }
    return l;
  }


}
