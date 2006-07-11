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
 * BorderWidthResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BorderWidthResolveHandler.java,v 1.3 2006/05/06 13:02:47 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver.computed.border;

import java.util.HashMap;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BorderWidth;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.layouter.model.LayoutElement;

/**
 * Creation-Date: 11.12.2005, 22:20:16
 *
 * @author Thomas Morgner
 */
public class BorderWidthResolveHandler extends ConstantsResolveHandler
{
  private HashMap keyMapping;

  public BorderWidthResolveHandler()
  {
    keyMapping = new HashMap();
    keyMapping.put(BorderStyleKeys.BORDER_TOP_WIDTH, BorderStyleKeys.BORDER_TOP_STYLE);
    keyMapping.put(BorderStyleKeys.BORDER_LEFT_WIDTH, BorderStyleKeys.BORDER_LEFT_STYLE);
    keyMapping.put(BorderStyleKeys.BORDER_BOTTOM_WIDTH, BorderStyleKeys.BORDER_BOTTOM_STYLE);
    keyMapping.put(BorderStyleKeys.BORDER_RIGHT_WIDTH, BorderStyleKeys.BORDER_RIGHT_STYLE);
    keyMapping.put(BorderStyleKeys.BORDER_BREAK_WIDTH, BorderStyleKeys.BORDER_BREAK_STYLE);

    addValue(BorderWidth.THIN, new CSSNumericValue(CSSNumericType.PT, 1));
    addValue(BorderWidth.MEDIUM, new CSSNumericValue(CSSNumericType.PT, 3));
    addValue(BorderWidth.THICK, new CSSNumericValue(CSSNumericType.PT, 5));
    setFallback(CSSNumericValue.ZERO_LENGTH);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[] {
            BorderStyleKeys.BORDER_TOP_STYLE,
            BorderStyleKeys.BORDER_LEFT_STYLE,
            BorderStyleKeys.BORDER_BOTTOM_STYLE,
            BorderStyleKeys.BORDER_RIGHT_STYLE,
            BorderStyleKeys.BORDER_BREAK_STYLE
    };
  }

  protected CSSValue resolveValue (final LayoutProcess process,
                                   final LayoutElement currentNode,
                                   final LayoutStyle style,
                                   final StyleKey key)
  {
    final StyleKey borderStyleKey = (StyleKey) keyMapping.get(key);
    if (borderStyleKey == null)
    {
      // invalid
      throw new IllegalArgumentException("This is not a valid key: " + key);
    }

    final CSSValue borderStyle = style.getValue(borderStyleKey);
    if (BorderStyle.NONE.equals(borderStyle))
    {
      return CSSNumericValue.ZERO_LENGTH;
    }

    CSSValue value = style.getValue(key);
    if (value instanceof CSSConstant)
    {
      return super.resolveValue(process, currentNode, style, key);
    }
    return value;
  }
}
