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
 * BorderWidthResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: MarginAndPaddingResolveHandler.java,v 1.1 2006/02/12 21:49:33 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.percentages.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.text.BlockProgression;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueSupport;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.text.TextSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.resolver.percentages.LengthResolverUtility;

/**
 * Creation-Date: 11.12.2005, 22:20:16
 *
 * @author Thomas Morgner
 */
public class MarginAndPaddingResolveHandler implements ResolveHandler
{
  public MarginAndPaddingResolveHandler ()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return blah
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[]{
            FontStyleKeys.FONT_SIZE_ADJUST,
            FontStyleKeys.FONT_SIZE,
            TextStyleKeys.BLOCK_PROGRESSION,
    };
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
    if (value instanceof CSSNumericValue)
    {
      CSSNumericValue nval = (CSSNumericValue) value;
      if (CSSValueSupport.isNumericType(CSSNumericType.PERCENTAGE, nval))
      {
        // handle the percentage ...
        final CSSValue size = getParentWidth(currentNode, nval.getValue());
        style.setValue(key, size);
      }
    }
  }


  protected CSSValue getParentWidth(LayoutNode currNode, double percentage)
  {
    LayoutNode parent = currNode.getParent();
    if (parent == null)
    {
      return new CSSNumericValue(CSSNumericType.PT, 0); // no parent, so no percentage
    }

    final TextSpecification textSpecification =
            currNode.getLayoutContext().getTextSpecification();
    final BlockProgression blockProgression =
            textSpecification.getLayoutSpecification().getBlockProgression();
    if (BlockProgression.TB.equals(blockProgression))
    {
      // grab the width ..
      CSSValue width = parent.getStyle().getValue(BoxStyleKeys.WIDTH);
      CSSNumericValue nval = LengthResolverUtility.getLength(width);
      if (nval != null)
      {
        return new CSSNumericValue(nval.getType(), nval.getValue() * percentage / 100d);
      }
    }
    else
    {
      // grab the width ..
      CSSValue height = parent.getStyle().getValue(BoxStyleKeys.WIDTH);
      CSSNumericValue nval = LengthResolverUtility.getLength(height);
      if (nval != null)
      {
        return new CSSNumericValue(nval.getType(), nval.getValue() * percentage / 100d);
      }
    }
    return new CSSNumericValue(CSSNumericType.PT, 0); // no parent, so no percentage
  }

}
