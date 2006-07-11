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
 * BorderFactory.java
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
package org.jfree.layouting.renderer.border;

import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * Creation-Date: 23.06.2006, 16:52:43
 *
 * @author Thomas Morgner
 */
public class BorderFactory
{
  public BorderFactory()
  {
  }

  public Border createBorder(final LayoutContext layoutContext,
                             final OutputProcessorMetaData metaData)
  {
    final LayoutStyle style = layoutContext.getStyle();
    CSSValue borderTopStyle = style.getValue(BorderStyleKeys.BORDER_TOP_STYLE);
    CSSValue borderTopWidth = style.getValue(BorderStyleKeys.BORDER_TOP_WIDTH);
    CSSValue borderTopColor = style.getValue(BorderStyleKeys.BORDER_TOP_COLOR);
    BorderEdge topEdge = createEdge(borderTopStyle, borderTopWidth,
            borderTopColor, layoutContext, metaData);

    CSSValue borderLeftStyle = style.getValue(BorderStyleKeys.BORDER_LEFT_STYLE);
    CSSValue borderLeftWidth = style.getValue(BorderStyleKeys.BORDER_LEFT_WIDTH);
    CSSValue borderLeftColor = style.getValue(BorderStyleKeys.BORDER_LEFT_COLOR);
    BorderEdge leftEdge = createEdge (borderLeftStyle, borderLeftWidth,
            borderLeftColor, layoutContext, metaData);

    CSSValue borderBottomStyle = style.getValue(BorderStyleKeys.BORDER_BOTTOM_STYLE);
    CSSValue borderBottomWidth = style.getValue(BorderStyleKeys.BORDER_BOTTOM_WIDTH);
    CSSValue borderBottomColor = style.getValue(BorderStyleKeys.BORDER_BOTTOM_COLOR);
    BorderEdge bottomEdge = createEdge(borderBottomStyle, borderBottomWidth,
            borderBottomColor, layoutContext, metaData);

    CSSValue borderRightStyle = style.getValue(BorderStyleKeys.BORDER_RIGHT_STYLE);
    CSSValue borderRightWidth = style.getValue(BorderStyleKeys.BORDER_RIGHT_WIDTH);
    CSSValue borderRightColor = style.getValue(BorderStyleKeys.BORDER_RIGHT_COLOR);
    BorderEdge rightEdge = createEdge(borderRightStyle, borderRightWidth,
            borderRightColor, layoutContext, metaData);

    CSSValue borderBreakStyle = style.getValue(BorderStyleKeys.BORDER_BREAK_STYLE);
    CSSValue borderBreakWidth = style.getValue(BorderStyleKeys.BORDER_BREAK_WIDTH);
    CSSValue borderBreakColor = style.getValue(BorderStyleKeys.BORDER_BREAK_COLOR);
    BorderEdge breakEdge = createEdge(borderBreakStyle, borderBreakWidth,
            borderBreakColor, layoutContext, metaData);

    CSSValue topLeftRadius = style.getValue(BorderStyleKeys.BORDER_TOP_LEFT_RADIUS);
    CSSValue topRightRadius = style.getValue(BorderStyleKeys.BORDER_TOP_RIGHT_RADIUS);
    CSSValue bottomLeftRadius = style.getValue(BorderStyleKeys.BORDER_BOTTOM_LEFT_RADIUS);
    CSSValue bottomRightRadius = style.getValue(BorderStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS);
    BorderCorner topLeftCorner = createCorner(topLeftRadius, layoutContext, metaData);
    BorderCorner topRightCorner = createCorner(topRightRadius, layoutContext, metaData);
    BorderCorner bottomLeftCorner = createCorner(bottomLeftRadius, layoutContext, metaData);
    BorderCorner bottomRightCorner = createCorner(bottomRightRadius, layoutContext, metaData);

    return new Border(topEdge, leftEdge, bottomEdge, rightEdge, breakEdge,
            topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner);
  }

  private BorderCorner createCorner (final CSSValue value,
                                     final LayoutContext layoutContext,
                                     final OutputProcessorMetaData metaData)
  {
    if (value instanceof CSSValuePair == false)
    {
      return new EmptyBorderCorner();
    }

    CSSValuePair vp = (CSSValuePair) value;
    CSSValue first = vp.getFirstValue();
    CSSValue second = vp.getSecondValue();

    RenderLength width = RenderLength.convertToInternal(first, layoutContext, metaData);
    RenderLength height = RenderLength.convertToInternal(second, layoutContext, metaData);
    if (width.getValue() > 0 && height.getValue() > 0)
    {
      return new RoundedBorderCorner(width, height);
    }
    else
    {
      return new EmptyBorderCorner();
    }
  }

  private BorderEdge createEdge (CSSValue style,
                                 CSSValue widthVal,
                                 CSSValue color,
                                 final LayoutContext layoutContext,
                                 final OutputProcessorMetaData metaData)
  {
    if (style instanceof CSSConstant == false)
    {
      return new EmptyBorderEdge();
    }
    if (color instanceof CSSColorValue == false)
    {
      return new EmptyBorderEdge();
    }
    if (BorderStyle.NONE.equals(style))
    {
      return new EmptyBorderEdge();
    }
    RenderLength width = RenderLength.convertToInternal
            (widthVal, layoutContext, metaData);
    if (width.getValue() <= 0)
    {
      return new EmptyBorderEdge();
    }

    return new StyledBorderEdge(style, (CSSColorValue) color, width);
  }
}
