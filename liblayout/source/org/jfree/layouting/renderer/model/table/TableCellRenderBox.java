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
 * TableCellRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableCellRenderBox.java,v 1.9 2006/07/29 18:57:13 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * A table section box does not much rendering or layouting at all. It
 * represents one of the three possible sections and behaves like any other
 * block box. But (here it comes!) it refuses to be added to anything else than
 * a TableRenderBox (a small check to save me a lot of insanity ..).
 *
 * @author Thomas Morgner
 */
public class TableCellRenderBox extends BlockRenderBox
{
  private int colSpan;
  private int rowSpan;
  private boolean autoGenerated;
  private Border effectiveBorder;
  private int columnIndex;

  public TableCellRenderBox(final BoxDefinition boxDefinition,
                            boolean autoGenerated)
  {
    super(boxDefinition);
    this.autoGenerated = autoGenerated;
    this.colSpan = 1;
    this.rowSpan = 1;
    this.columnIndex = -1;
  }

  public TableCellRenderBox(final BoxDefinition boxDefinition)
  {
    this(boxDefinition, false);
  }

  public void appyStyle(LayoutContext context, OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);
    // This one is a special value here ... 

    final CSSValue csValue = context.getStyle().getValue(TableStyleKeys.COL_SPAN);
    this.colSpan = (int) CSSValueResolverUtility.getNumericValue(csValue, 1);

    final CSSValue rsValue = context.getStyle().getValue(TableStyleKeys.ROW_SPAN);
    this.rowSpan = (int) CSSValueResolverUtility.getNumericValue(rsValue, 1);
  }

  protected CSSValue normalizeAlignment(CSSValue verticalAlignment)
  {
    if (VerticalAlign.BOTTOM.equals(verticalAlignment))
    {
      return verticalAlignment;
    }
    if (VerticalAlign.TOP.equals(verticalAlignment))
    {
      return verticalAlignment;
    }
    if (VerticalAlign.MIDDLE.equals(verticalAlignment))
    {
      return verticalAlignment;
    }
    return VerticalAlign.BASELINE;
  }

  public boolean isAutoGenerated()
  {
    return autoGenerated;
  }

  public RenderNode getCellNode()
  {
    return this;
  }

  public TableRenderBox getTable()
  {
    RenderBox parent = getParent();
    if (parent instanceof TableRowRenderBox)
    {
      final TableRowRenderBox rowRenderBox =
              (TableRowRenderBox) parent;
      return rowRenderBox.getTable();
    }
    return null;
  }

  public int getColSpan()
  {
    return colSpan;
  }

  public int getRowSpan()
  {
    return rowSpan;
  }

  /**
   * If that method returns true, the element will not be used for rendering.
   * For the purpose of computing sizes or performing the layouting (in the
   * validate() step), this element will treated as if it is not there.
   * <p/>
   * If the element reports itself as non-empty, however, it will affect the
   * margin computation.
   *
   * @return
   */
  public boolean isIgnorableForRendering()
  {
    return false;
  }

  public Border getEffectiveBorder()
  {
    return effectiveBorder;
  }

  public void setEffectiveBorder(final Border effectiveBorder)
  {
    this.effectiveBorder = effectiveBorder;
  }

  public Border getOriginalBorder()
  {
    return getBoxDefinition().getBorder();
  }

  public int getColumnIndex()
  {
    return columnIndex;
  }

  public void setColumnIndex(final int columnIndex)
  {
    this.columnIndex = columnIndex;
  }
}
