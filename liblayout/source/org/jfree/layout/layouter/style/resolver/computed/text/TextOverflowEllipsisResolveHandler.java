/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * TextOverflowModeResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 21.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.computed.text;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.text.TextSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

/**
 * Creation-Date: 21.12.2005, 16:48:23
 *
 * @author Thomas Morgner
 */
public class TextOverflowEllipsisResolveHandler implements ResolveHandler
{
  public TextOverflowEllipsisResolveHandler()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[0];
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve(final LayoutProcess process,
                      LayoutNode currentNode,
                      LayoutStyle style,
                      StyleKey key)
  {
    CSSValue value = style.getValue(key);
    CSSStringValue lineEllipsis = null;
    CSSStringValue blockEllipsis = null;
    if (value instanceof CSSValueList)
    {
      CSSValueList vlist = (CSSValueList) value;
      if (vlist.getLength() == 2)
      {
        lineEllipsis = filterString(vlist.getItem(0));
        blockEllipsis = filterString(vlist.getItem(1));
      }
      else if (vlist.getLength() == 1)
      {
        lineEllipsis = filterString(vlist.getItem(0));
        blockEllipsis = filterString(vlist.getItem(0));
      }
    }
    if (lineEllipsis == null)
    {
      lineEllipsis = new CSSStringValue(CSSStringType.STRING, "..");
    }
    if (blockEllipsis == null)
    {
      blockEllipsis = new CSSStringValue(CSSStringType.STRING, "..");
    }

    final TextSpecification ts =
            currentNode.getLayoutContext().getTextSpecification();
    ts.getOverflowSpecification().setLineOverflowText(lineEllipsis);
    ts.getOverflowSpecification().setBoxOverflowText(blockEllipsis);
  }

  private CSSStringValue filterString (CSSValue value)
  {
    if (value instanceof CSSStringValue == false)
    {
      return null;
    }
    return (CSSStringValue) value;
  }
}
