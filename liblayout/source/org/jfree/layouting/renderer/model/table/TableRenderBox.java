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
 * $Id: TableRenderBox.java,v 1.5 2006/07/22 15:28:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import java.util.ArrayList;

import org.jfree.layouting.input.style.keys.table.BorderCollapse;
import org.jfree.layouting.input.style.keys.table.EmptyCells;
import org.jfree.layouting.input.style.keys.table.TableLayout;
import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
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
import org.jfree.layouting.renderer.model.table.cols.SpearateColumnModel;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.model.table.rows.TableRowModel;
import org.jfree.layouting.util.geom.StrictInsets;
import org.jfree.util.Log;

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
  private RenderLength borderSpacing;
  private RenderLength rowSpacing;
  private long preferredHeight;
  private long minimumChunkSize;

  public TableRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
    this.columnModel = new SpearateColumnModel(this);
    this.borderSpacing = RenderLength.EMPTY;
  }

  public TableRenderBox(final BoxDefinition boxDefinition,
                        final LayoutContext layoutContext,
                        final RenderLength borderSpacing,
                        final RenderLength rowSpacing)
  {
    this(boxDefinition);

    CSSValue emptyCellsVal = layoutContext.getStyle().getValue
            (TableStyleKeys.EMPTY_CELLS);
    this.displayEmptyCells = EmptyCells.SHOW.equals(emptyCellsVal);

    final CSSValue borderModel =
            layoutContext.getStyle().getValue(TableStyleKeys.BORDER_COLLAPSE);
    this.collapsingBorderModel = BorderCollapse.COLLAPSE.equals(borderModel);

    final CSSValue layoutModel =
            layoutContext.getStyle().getValue(TableStyleKeys.TABLE_LAYOUT);
    this.autoLayout = TableLayout.AUTO.equals(layoutModel);
    this.borderSpacing = borderSpacing;
    this.rowSpacing = rowSpacing;
  }

  public RenderLength getBorderSpacing()
  {
    return borderSpacing;
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

    validateSections();

    setState(RenderNodeState.FINISHED);
  }

  protected void validateSections()
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

    Log.debug("TABLE: Begin Validate");

    final long borderSpacing = getRowSpacing();
    final long leadingPaddings = getLeadingInsets(getMinorAxis());
    final long trailingPaddings = getTrailingInsets(getMinorAxis());

    long nodePos =
            getPosition(getMajorAxis()) + getLeadingInsets(getMajorAxis());
    final long minorAxisNodePos =
            getPosition(getMinorAxis()) + leadingPaddings;


    final long defaultNodeWidth = Math.max(0,
            getDimension(getMinorAxis()) - leadingPaddings - trailingPaddings);

    RenderNode[][] sortedNodes = collectBoxes();
    RenderNode[] discarded = sortedNodes[0];
    for (int i = 0; i < discarded.length; i++)
    {
      RenderNode node = discarded[i];
      node.setPosition(getMajorAxis(), nodePos);
      node.setPosition(getMinorAxis(), minorAxisNodePos);
      node.setDimension(getMinorAxis(), 0);
      node.setDimension(getMajorAxis(), 0);
    }

    nodePos = validateSection(sortedNodes[1],
            defaultNodeWidth, minorAxisNodePos, borderSpacing, nodePos);
    nodePos = validateSection(sortedNodes[3],
            defaultNodeWidth, minorAxisNodePos, borderSpacing, nodePos);
    nodePos = validateSection(sortedNodes[2],
            defaultNodeWidth, minorAxisNodePos, borderSpacing, nodePos);

    final long trailingInsets = getTrailingInsets(getMajorAxis());
    setDimension(getMajorAxis(), (nodePos + trailingInsets) - getPosition(getMajorAxis()));
    setDimension(getMinorAxis(),
            defaultNodeWidth + leadingPaddings + trailingPaddings);

    Log.debug("TABLE: Leave Validate: " + defaultNodeWidth + " " +
            leadingPaddings + " " + trailingPaddings);
    setState(RenderNodeState.FINISHED);
  }

  private long validateSection (RenderNode[] nodes,
                                long defaultNodeWidth,
                                long minorAxisNodePos,
                                long borderSpacing,
                                long nodePos)
  {
    for (int i = 0; i < nodes.length; i++)
    {
      RenderNode node = nodes[i];

      node.setPosition(getMajorAxis(), nodePos);
      node.setPosition(getMinorAxis(), minorAxisNodePos);
      node.setDimension(getMinorAxis(), defaultNodeWidth);
//      node.setDimension(getMajorAxis(), node.getEffectiveLayoutSize(getMajorAxis()));
      node.validate();

      nodePos += node.getDimension(getMajorAxis());
      nodePos += borderSpacing;
    }
    if (nodes.length != 0)
    {
      nodePos -= borderSpacing;
    }
    return nodePos;
  }

  private RenderNode[][] collectBoxes()
  {
    ArrayList headers = new ArrayList();
    ArrayList footers = new ArrayList();
    ArrayList bodies = new ArrayList();
    ArrayList others = new ArrayList();

    int collectState = 0;
    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering() ||
          node instanceof TableSectionRenderBox == false)
      {
        // Ignore all empty childs. However, give it an position.
        others.add(node);
        node = node.getNext();
        continue;
      }

      TableSectionRenderBox renderBox = (TableSectionRenderBox) node;
      CSSValue displayRole = renderBox.getDisplayRole();
      if (collectState == 0 &&
          DisplayRole.TABLE_HEADER_GROUP.equals(displayRole))
      {
        headers.add(renderBox);
      }
      else if (collectState <= 1 &&
          DisplayRole.TABLE_FOOTER_GROUP.equals(displayRole))
      {
        footers.add(renderBox);
        collectState = 1;
      }
      else
      {
        bodies.add(renderBox);
        collectState = 2;
      }
      node = node.getNext();
    }

    RenderNode[][] retval = new RenderNode[4][];
    retval[0] = (RenderNode[]) others.toArray(new RenderNode[others.size()]);
    retval[1] = (RenderNode[]) headers.toArray(new RenderNode[headers.size()]);
    retval[2] = (RenderNode[]) footers.toArray(new RenderNode[footers.size()]);
    retval[3] = (RenderNode[]) bodies.toArray(new RenderNode[bodies.size()]);
    return retval;
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

  private void validateSizes()
  {
    // validate the columns ...
    preferredHeight = 0;
    minimumChunkSize = 0;
    RenderNode[][] allNodes = collectBoxes();
    for (int i = 1; i < allNodes.length; i++)
    {
      RenderNode[] sections = allNodes[i];
      for (int j = 0; j < sections.length; j++)
      {
        TableSectionRenderBox node = (TableSectionRenderBox) sections[j];
        node.structValidateRows();
      }
    }

    for (int i = 1; i < allNodes.length; i++)
    {
      RenderNode[] sections = allNodes[i];
      for (int j = 0; j < sections.length; j++)
      {
        TableSectionRenderBox node = (TableSectionRenderBox) sections[j];
        node.structValidateRowSizes();
        final TableRowModel rowModel = node.getRowModel();
        preferredHeight = rowModel.getPreferredSize();
        minimumChunkSize = Math.max
                (minimumChunkSize, rowModel.getMinimumChunkSize());
      }
    }

    columnModel.validateSizes();
  }

  public long getPreferredSize(int axis)
  {
    prune();
    buildColumnModel();
    validateSizes();

    final long insets = getLeadingInsets(axis) + getTrailingInsets(axis);

    if (axis == HORIZONTAL_AXIS)
    {
      return columnModel.getPreferredSize() + insets;
    }
    else
    {
      return preferredHeight  + insets;
    }
  }

  public long getMinimumChunkSize(int axis)
  {
    prune();
    buildColumnModel();
    validateSizes();

    if (axis == HORIZONTAL_AXIS)
    {
      final long insets = getLeadingInsets(axis) + getTrailingInsets(axis);
      return insets + columnModel.getMinimumChunkSize();
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

  protected void validateBorders()
  {
    if (isCollapsingBorderModel() == false)
    {
      // In the separate border model, tables may have their own borders.
      super.validateBorders();
      return;
    }

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
    if (isCollapsingBorderModel() == false)
    {
      // In the separate border model, tables may have their own padding.
      super.validateBorders();
      return;
    }

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
    if (axis == VERTICAL_AXIS)
    {
      return getPreferredSize(axis);
    }

    prune();
    buildColumnModel();
    validateSizes();

    // 17.5.2 Table width algorithms: the 'table-layout' property
    //
    // Note that this section overrides the rules that apply to
    // calculating widths as described in section 10.3.

    //    if (isAutoLayout())
    //    {
    //
    //    }
    // We assume Auto-Layout for now. Yes, that needs a change in the future.
    final long insets = getLeadingInsets(axis) + getTrailingInsets(axis);

    final RenderLength preferredWidth =
            getBoxDefinition().getPreferredWidth();

    final long minChunk = insets + columnModel.getMinimumChunkSize();
    final long prefSize = insets + columnModel.getPreferredSize();
    final long contextWidth = getComputedBlockContextWidth();
    final long nodeWidth;
    if (RenderLength.AUTO.equals(preferredWidth))
    {
      if (prefSize <= contextWidth)
      {
        nodeWidth = prefSize;
      }
      else
      {
        nodeWidth = Math.max (minChunk, contextWidth);
      }
    }
    else
    {
      nodeWidth = Math.max (minChunk, contextWidth);
    }

    final long margins = getLeadingSpace(axis) + getTrailingSpace(axis);
    final long l = (nodeWidth - margins);
    if (l < 0)
    {
      return 0;
    }
    return l;
  }

  /**
   * Make this method public, so that the model can access it ..
   * @return
   */
  public long getComputedBlockContextWidth()
  {
    return super.getComputedBlockContextWidth();
  }

  public long getRowSpacing()
  {
    return rowSpacing.resolve(getComputedBlockContextWidth());
  }
}
