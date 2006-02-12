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
 * FontStretchResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: FontWeightResolveHandler.java,v 1.1 2006/02/12 21:49:33 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.computed.fonts;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontWeight;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.font.FontSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

/**
 * Creation-Date: 18.12.2005, 20:33:42
 *
 * @author Thomas Morgner
 */
public class FontWeightResolveHandler implements ResolveHandler
{
  public FontWeightResolveHandler()
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
  public void resolve(LayoutProcess process,
                         LayoutNode currentNode,
                         LayoutStyle style,
                         StyleKey key)
  {
    final CSSValue value = style.getValue(key);
    final FontSpecification fs =
            currentNode.getLayoutContext().getFontSpecification();
    final int fontWeight;
    if (FontWeight.BOLD.equals(value))
    {
      // ask the parent ...
      fontWeight = 700;
    }
    else if (FontWeight.NORMAL.equals(value))
    {
      // ask the parent ...
      fontWeight = 400;
    }
    else if (FontWeight.BOLDER.equals(value))
    {
      int parentFontWeight = queryParent(currentNode.getParent());
      fontWeight = Math.max (900, parentFontWeight + 100);
    }
    else if (FontWeight.LIGHTER.equals(value))
    {
      int parentFontWeight = queryParent(currentNode.getParent());
      fontWeight = Math.min (100, parentFontWeight - 100);
    }
    else if (value instanceof CSSNumericValue)
    {
      CSSNumericValue nval = (CSSNumericValue) value;
      if (CSSNumericType.NUMBER.equals(nval.getType()) == false)
      {
        // preserve the parent's weight...
        fontWeight = queryParent(currentNode.getParent());
      }
      else
      {
        fontWeight = (int) nval.getValue();
      }
    }
    else
    {
      fontWeight = queryParent(currentNode.getParent());
    }

    fs.setFontWeight(fontWeight);
  }

  private int queryParent(final LayoutElement parent)
  {
    if (parent == null)
    {
      return 400; // Normal
    }
    return parent.getLayoutContext().getFontSpecification().getFontWeight();
  }
}