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
 * LetterSpacingResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LetterSpacingResolveHandler.java,v 1.5 2006/11/20 21:01:53 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver.percentages.text;

import org.jfree.fonts.registry.FontMetrics;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

/**
 * Creation-Date: 21.12.2005, 15:12:04
 *
 * @author Thomas Morgner
 */
public class LetterSpacingResolveHandler implements ResolveHandler
{
  public LetterSpacingResolveHandler()
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
   * @param currentNode
   * @param style
   */
  public void resolve(final LayoutProcess process,
                      LayoutElement currentNode,
                      StyleKey key)
  {
    // Percentages get resolved against the width of a standard space (0x20)
    // character.
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final FontSpecification fontSpecification =
            layoutContext.getFontSpecification();
    final FontMetrics fm = process.getOutputMetaData().getFontMetrics(fontSpecification);
    if (fm == null)
    {
      // we have no font family, so return.
      layoutContext.setValue(TextStyleKeys.X_MIN_LETTER_SPACING, CSSNumericValue.ZERO_LENGTH);
      layoutContext.setValue(TextStyleKeys.X_MAX_LETTER_SPACING, CSSNumericValue.ZERO_LENGTH);
      layoutContext.setValue(TextStyleKeys.X_OPTIMUM_LETTER_SPACING, CSSNumericValue.ZERO_LENGTH);
      return;
    }

    final double width = fm.getCharWidth(0x20);
    final CSSNumericValue percentageBase =
            new CSSNumericValue(CSSNumericType.PT, width);
    final CSSNumericValue min = CSSValueResolverUtility.getLength
            (resolveValue(layoutContext, TextStyleKeys.X_MIN_LETTER_SPACING), percentageBase);
    final CSSNumericValue max = CSSValueResolverUtility.getLength
            (resolveValue(layoutContext, TextStyleKeys.X_MAX_LETTER_SPACING), percentageBase);
    final CSSNumericValue opt = CSSValueResolverUtility.getLength
            (resolveValue(layoutContext, TextStyleKeys.X_OPTIMUM_LETTER_SPACING), percentageBase);

    layoutContext.setValue(TextStyleKeys.X_MIN_LETTER_SPACING, min);
    layoutContext.setValue(TextStyleKeys.X_MAX_LETTER_SPACING, max);
    layoutContext.setValue(TextStyleKeys.X_OPTIMUM_LETTER_SPACING, opt);
  }

  private CSSNumericValue resolveValue (LayoutContext style, StyleKey key)
  {
    final CSSValue value = style.getValue(key);
    if (value instanceof CSSNumericValue == false)
    {
      // this also covers the valid 'normal' property.
      // it simply means, dont add extra space to the already existing spaces
      return CSSNumericValue.ZERO_LENGTH;
    }

    return (CSSNumericValue) value;
  }

}
