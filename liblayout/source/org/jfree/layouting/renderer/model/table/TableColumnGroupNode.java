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
 * TableColumnGroupNode.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableColumnGroupNode.java,v 1.7 2006/10/17 16:39:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * Creation-Date: 17.07.2006, 18:32:42
 *
 * @author Thomas Morgner
 */
public class TableColumnGroupNode extends RenderBox
{
  private int colspan;

  public TableColumnGroupNode(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
  }

  public void appyStyle(LayoutContext context, OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);
    final CSSValue value = context.getValue(TableStyleKeys.COL_SPAN);
    this.colspan = (int) CSSValueResolverUtility.getNumericValue(value, 1);
  }

  public int getColSpan ()
  {
    return colspan;
  }

  public boolean isDiscardable()
  {
    return false;
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
    return true;
  }

}
