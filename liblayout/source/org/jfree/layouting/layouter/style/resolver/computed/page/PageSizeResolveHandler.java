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
 * PageSizeResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PageSizeResolveHandler.java,v 1.2 2006/07/26 16:59:47 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver.computed.page;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.keys.page.PageSizeFactory;
import org.jfree.layouting.input.style.keys.page.PageStyleKeys;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.context.LayoutContext;

/**
 * Creation-Date: 16.06.2006, 13:56:31
 *
 * @author Thomas Morgner
 */
public class PageSizeResolveHandler implements ResolveHandler
{
  public PageSizeResolveHandler()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[0];
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve(LayoutProcess process,
                      LayoutElement currentNode,
                      StyleKey key)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    CSSValue value = layoutContext.getValue(PageStyleKeys.SIZE);

    String name = null;
    if (value instanceof CSSStringValue)
    {
      CSSStringValue sval = (CSSStringValue) value;
      name = sval.getValue();
    }
    else if (value instanceof CSSConstant)
    {
      name = value.toString();
    }

    PageSize ps = null;
    if (name != null)
    {
      ps = PageSizeFactory.getInstance().getPageSizeByName(name);
    }

    if (ps == null)
    {
      ps = process.getOutputMetaData().getDefaultPageSize();
    }
    // if it is stll null, then the output target is not valid.
    // We will crash in that case ..
    CSSValue page =
        new CSSValuePair(CSSNumericValue.createPtValue(ps.getWidth()),
            CSSNumericValue.createPtValue(ps.getHeight()));
    layoutContext.setValue(PageStyleKeys.SIZE, page);
  }
}
