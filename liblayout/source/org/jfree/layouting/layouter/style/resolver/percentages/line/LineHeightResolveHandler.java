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
 * LineHeightResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LineHeightResolveHandler.java,v 1.3 2006/07/11 13:29:54 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.style.resolver.percentages.line;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineHeight;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;

public class LineHeightResolveHandler implements ResolveHandler
{
  public LineHeightResolveHandler ()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[] {
            FontStyleKeys.FONT_SIZE,
            FontStyleKeys.FONT_SIZE_ADJUST,

    };
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve (LayoutProcess process,
                       LayoutElement currentNode,
                       LayoutStyle style,
                       StyleKey key)
  {
    CSSValue value = style.getValue(key);
    if (LineHeight.NONE.equals(value))
    {
      // query the anchestor, if there's one ..
      handleNone(currentNode, style);
      return;
    }

    if (LineHeight.NORMAL.equals(value))
    {
      handleNormal(currentNode, style);
      return;
    }

    if (value instanceof CSSNumericValue == false)
    {
      // fall back to normal ..
      handleNormal(currentNode, style);
      return;
    }
    CSSNumericValue nval = (CSSNumericValue) value;
    final LayoutContext layoutContext = currentNode.getLayoutContext();

    if (CSSValueResolverUtility.isLengthValue(nval))
    {
      style.setValue(LineStyleKeys.LINE_HEIGHT, nval);
      return;
    }

    final double factor;
    if (nval.getType().equals(CSSNumericType.PERCENTAGE))
    {
      factor = nval.getValue() / 100d;
    }
    else if (nval.getType().equals(CSSNumericType.NUMBER))
    {
      factor = nval.getValue();
    }
    else
    {
      handleNormal(currentNode, style);
      return;
    }


    final double fontSize =
            layoutContext.getFontSpecification().getFontSize();
    style.setValue(LineStyleKeys.LINE_HEIGHT,
            new CSSNumericValue(CSSNumericType.PT, fontSize * factor));

  }

  private void handleNormal (LayoutElement currentNode,
                             LayoutStyle style)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final double fontSize =
            layoutContext.getFontSpecification().getFontSize();
    if (fontSize < 10)
    {
      style.setValue(LineStyleKeys.LINE_HEIGHT,
              new CSSNumericValue(CSSNumericType.PT, fontSize * 1.2));
    }
    else if (fontSize < 24)
    {
      style.setValue(LineStyleKeys.LINE_HEIGHT,
              new CSSNumericValue(CSSNumericType.PT, fontSize * 1.1));
    }
    else
    {
      style.setValue(LineStyleKeys.LINE_HEIGHT,
              new CSSNumericValue(CSSNumericType.PT, fontSize * 1.05));
    }

  }

  private void handleNone (LayoutElement currentNode,
                           LayoutStyle style)
  {
    final double fontSize;
    final LayoutElement parent = currentNode.getParent();
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    if (parent == null)
    {
      // fall back to normal;
      fontSize = layoutContext.getFontSpecification().getFontSize();
    }
    else
    {
      fontSize = parent.getLayoutContext().getFontSpecification().getFontSize();
    }
    style.setValue(LineStyleKeys.LINE_HEIGHT,
            new CSSNumericValue(CSSNumericType.PT, fontSize));
  }
}
