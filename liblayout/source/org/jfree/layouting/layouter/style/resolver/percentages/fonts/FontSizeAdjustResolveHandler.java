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
 * FontSizeAdjustResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver.percentages.fonts;

import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.font.FontSpecification;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.fonts.registry.FontMetrics;

/**
 * Creation-Date: 18.12.2005, 19:46:43
 *
 * @author Thomas Morgner
 */
public class FontSizeAdjustResolveHandler implements ResolveHandler
{
  public FontSizeAdjustResolveHandler()
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
    return new StyleKey[] {
            FontStyleKeys.FONT_SIZE,
            FontStyleKeys.FONT_FAMILY,
            FontStyleKeys.FONT_EFFECT,
            FontStyleKeys.FONT_SMOOTH,
            FontStyleKeys.FONT_STRETCH,
            FontStyleKeys.FONT_VARIANT,
            FontStyleKeys.FONT_WEIGHT,
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
    CSSValue value = style.getValue(key);
    if (value instanceof CSSNumericValue == false)
    {
      return; // do nothing
    }
    CSSNumericValue nval = (CSSNumericValue) value;
    if (CSSNumericType.NUMBER.equals(nval.getType()) == false)
    {
      return; // syntax error, do nothing
    }
    final LayoutNode parent = currentNode.getParent();
    if (parent == null)
    {
      return; // no parent to resolve against ...
    }

    final double adjustFactor = nval.getValue();
    final FontSpecification fontSpecification =
            currentNode.getLayoutContext().getFontSpecification();
    final FontMetrics fontMetrics = fontSpecification.getFontMetrics();
    if (fontMetrics == null)
    {
      return; // no font metrics means no valid font...
    }

    final double actualFontXHeight = fontMetrics.getXHeight();

    final double fontSize = fontSpecification.getFontSize();
    final double aspectRatio = actualFontXHeight / fontSize;
    final double result = fontSize * (adjustFactor / aspectRatio);
    fontSpecification.setFontSize(result);
  }
}
