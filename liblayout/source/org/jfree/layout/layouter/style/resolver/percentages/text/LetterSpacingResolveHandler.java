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
 * WhitespaceCollapseResolveHandler.java
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
package org.jfree.layouting.layouter.style.resolver.percentages.text;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.text.SpacingValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.text.AbsoluteSpacingValue;
import org.jfree.layouting.model.text.TextSpacingSpecification;
import org.jfree.layouting.model.text.TextSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
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
   * @param style
   * @param currentNode
   */
  public void resolve(final LayoutProcess process,
                      LayoutNode currentNode,
                      LayoutStyle style,
                      StyleKey key)
  {

    final TextSpecification textSpecification =
            currentNode.getLayoutContext().getTextSpecification();
    final TextSpacingSpecification spacingSpecification =
            textSpecification.getSpacingSpecification();

    final CSSValue rawvalue = style.getValue(key);
    if (rawvalue instanceof SpacingValue == false)
    {
      // fall back to the default, which is simply zero ---
      spacingSpecification.setLetterSpacing
              (new AbsoluteSpacingValue(0, 0, Integer.MAX_VALUE));
      return;
    }

    final SpacingValue value = (SpacingValue) rawvalue;
    final long optimum = resolveValue(value.getOptimumSpacing(), 0);
    final long minimum = resolveValue(value.getMinimumSpacing(), 0);
    final long maximum = resolveValue(value.getMaximumSpacing(), 0);
    spacingSpecification.setLetterSpacing
            (new AbsoluteSpacingValue(optimum, minimum, maximum));

  }

  private long resolveValue (CSSValue value, final long normalSpace)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return 0;
    }
    CSSNumericValue nval = (CSSNumericValue) value;
    final double dval = nval.getValue();
    return (long) (normalSpace * (dval/ 100));
  }
}
