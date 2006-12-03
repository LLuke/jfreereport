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

import java.util.ArrayList;

import org.jfree.layouting.input.style.keys.table.BorderCollapse;
import org.jfree.layouting.input.style.keys.table.EmptyCells;
import org.jfree.layouting.input.style.keys.table.TableLayout;
import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.DefaultBoxDefinitionFactory;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.table.cols.SpearateColumnModel;
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

  private boolean needsPruning;
  private RenderLength borderSpacing;
  private RenderLength rowSpacing;

  private boolean displayEmptyCells;
  private boolean collapsingBorderModel;
  private boolean autoLayout;

  private TableLayoutInfo tableInfo;
  private ArrayList tableColumnDefinitions;

  public TableRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);

    this.columnModel = new SpearateColumnModel(this);
    this.borderSpacing = RenderLength.EMPTY;
    this.tableInfo = new TableLayoutInfo();
    this.tableColumnDefinitions = new ArrayList();
  }

  public void appyStyle(LayoutContext layoutContext,
                        OutputProcessorMetaData metaData)
  {
    super.appyStyle(layoutContext, metaData);
    CSSValue emptyCellsVal = layoutContext.getValue
            (TableStyleKeys.EMPTY_CELLS);
    this.displayEmptyCells = EmptyCells.SHOW.equals(emptyCellsVal);

    final CSSValue borderModel =
            layoutContext.getValue(TableStyleKeys.BORDER_COLLAPSE);
    this.collapsingBorderModel = BorderCollapse.COLLAPSE.equals(borderModel);

    final CSSValue layoutModel =
            layoutContext.getValue(TableStyleKeys.TABLE_LAYOUT);
    this.autoLayout = TableLayout.AUTO.equals(layoutModel);

    final CSSValue borderSpacingVal =
            layoutContext.getValue(TableStyleKeys.BORDER_SPACING);

    if (borderSpacingVal instanceof CSSValuePair)
    {
      CSSValuePair borderSpacingPair = (CSSValuePair) borderSpacingVal;
      rowSpacing = DefaultBoxDefinitionFactory.computeWidth
              (borderSpacingPair.getFirstValue(), layoutContext, metaData, false, false);
      borderSpacing = DefaultBoxDefinitionFactory.computeWidth
              (borderSpacingPair.getSecondValue(), layoutContext, metaData, false, false);
    }
    else
    {
      borderSpacing = RenderLength.EMPTY;
      rowSpacing = RenderLength.EMPTY;
    }


  }

  public RenderLength getBorderSpacing()
  {
    return borderSpacing;
  }

  public boolean isAutoLayout()
  {
    return autoLayout;
  }

  public TableColumnModel getColumnModel()
  {
    return columnModel;
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

  public boolean isCollapsingBorderModel()
  {
    return collapsingBorderModel;
  }

  public boolean isDisplayEmptyCells()
  {
    return displayEmptyCells;
  }

  public long getRowSpacing()
  {
    return rowSpacing.resolve(0);
  }

  public TableLayoutInfo getTableInfo()
  {
    return tableInfo;
  }
}
