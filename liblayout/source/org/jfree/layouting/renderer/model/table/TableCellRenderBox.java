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
 * $Id: TableCellRenderBox.java,v 1.2 2006/07/18 17:26:32 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.model.table.cols.TableCell;

/**
 * A table section box does not much rendering or layouting at all. It represents
 * one of the three possible sections and behaves like any other block box.
 * But (here it comes!) it refuses to be added to anything else than a
 * TableRenderBox (a small check to save me a lot of insanity ..).
 *
 * @author Thomas Morgner
 */
public class TableCellRenderBox extends BlockRenderBox implements TableCell
{
  private int colSpan;
  private int rowSpan;

  public TableCellRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
    colSpan = 1;
    rowSpan = 1;
  }

  public TableCellRenderBox(final BoxDefinition boxDefinition,
                            final LayoutContext context)
  {
    this(boxDefinition);
    final CSSValue csValue = context.getStyle().getValue(TableStyleKeys.COL_SPAN);
    this.colSpan = (int) CSSValueResolverUtility.getNumericValue(csValue, 1);

    final CSSValue rsValue = context.getStyle().getValue(TableStyleKeys.ROW_SPAN);
    this.rowSpan = (int) CSSValueResolverUtility.getNumericValue(rsValue, 1);

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

  public TableColumnModel getColumnModel()
  {
    final TableRenderBox table = getTable();
    if (table == null)
    {
      return null;
    }
    return table.getColumnModel();
  }

  public void validate()
  {
    super.validate();
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
}
