/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.border;

import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.context.LayoutContext;
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
    CSSValue borderTopStyle = layoutContext.getValue(BorderStyleKeys.BORDER_TOP_STYLE);
    CSSValue borderTopWidth = layoutContext.getValue(BorderStyleKeys.BORDER_TOP_WIDTH);
    CSSValue borderTopColor = layoutContext.getValue(BorderStyleKeys.BORDER_TOP_COLOR);
    BorderEdge topEdge = createEdge(borderTopStyle, borderTopWidth,
            borderTopColor, layoutContext, metaData);

    CSSValue borderLeftStyle = layoutContext.getValue(BorderStyleKeys.BORDER_LEFT_STYLE);
    CSSValue borderLeftWidth = layoutContext.getValue(BorderStyleKeys.BORDER_LEFT_WIDTH);
    CSSValue borderLeftColor = layoutContext.getValue(BorderStyleKeys.BORDER_LEFT_COLOR);
    BorderEdge leftEdge = createEdge (borderLeftStyle, borderLeftWidth,
            borderLeftColor, layoutContext, metaData);

    CSSValue borderBottomStyle = layoutContext.getValue(BorderStyleKeys.BORDER_BOTTOM_STYLE);
    CSSValue borderBottomWidth = layoutContext.getValue(BorderStyleKeys.BORDER_BOTTOM_WIDTH);
    CSSValue borderBottomColor = layoutContext.getValue(BorderStyleKeys.BORDER_BOTTOM_COLOR);
    BorderEdge bottomEdge = createEdge(borderBottomStyle, borderBottomWidth,
            borderBottomColor, layoutContext, metaData);

    CSSValue borderRightStyle = layoutContext.getValue(BorderStyleKeys.BORDER_RIGHT_STYLE);
    CSSValue borderRightWidth = layoutContext.getValue(BorderStyleKeys.BORDER_RIGHT_WIDTH);
    CSSValue borderRightColor = layoutContext.getValue(BorderStyleKeys.BORDER_RIGHT_COLOR);
    BorderEdge rightEdge = createEdge(borderRightStyle, borderRightWidth,
            borderRightColor, layoutContext, metaData);

    CSSValue borderBreakStyle = layoutContext.getValue(BorderStyleKeys.BORDER_BREAK_STYLE);
    CSSValue borderBreakWidth = layoutContext.getValue(BorderStyleKeys.BORDER_BREAK_WIDTH);
    CSSValue borderBreakColor = layoutContext.getValue(BorderStyleKeys.BORDER_BREAK_COLOR);
    BorderEdge breakEdge = createEdge(borderBreakStyle, borderBreakWidth,
            borderBreakColor, layoutContext, metaData);

    CSSValue topLeftRadius = layoutContext.getValue(BorderStyleKeys.BORDER_TOP_LEFT_RADIUS);
    CSSValue topRightRadius = layoutContext.getValue(BorderStyleKeys.BORDER_TOP_RIGHT_RADIUS);
    CSSValue bottomLeftRadius = layoutContext.getValue(BorderStyleKeys.BORDER_BOTTOM_LEFT_RADIUS);
    CSSValue bottomRightRadius = layoutContext.getValue(BorderStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS);
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
      return EmptyBorderCorner.getInstance();
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
      return EmptyBorderCorner.getInstance();
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
      return EmptyBorderEdge.getInstance();
    }
    if (color instanceof CSSColorValue == false)
    {
      return EmptyBorderEdge.getInstance();
    }
    if (BorderStyle.NONE.equals(style))
    {
      return EmptyBorderEdge.getInstance();
    }
    RenderLength width = RenderLength.convertToInternal
            (widthVal, layoutContext, metaData);
    if (width.getValue() <= 0)
    {
      return EmptyBorderEdge.getInstance();
    }

    return new StyledBorderEdge(style, (CSSColorValue) color, width);
  }

  public static BorderEdge merge (BorderEdge first,
                                  BorderEdge second,
                                  long computedParentWidth)
  {
    // Borders with the 'border-style' of 'hidden' take precedence over
    // all other conflicting borders. Any border with this value suppresses
    // all borders at this location.
    final CSSValue firstStyle = first.getBorderStyle();
    if (BorderStyle.HIDDEN.equals(firstStyle))
    {
      return first;
    }

    final CSSValue secondStyle = second.getBorderStyle();
    if (BorderStyle.HIDDEN.equals(secondStyle))
    {
      return second;
    }

    // Borders with a style of 'none' have the lowest priority. Only if the
    // border properties of all the elements meeting at this edge are
    // 'none' will the border be omitted (but note that 'none' is the
    // default value for the border style.)
    if (BorderStyle.NONE.equals(first))
    {
      return second;
    }
    if (BorderStyle.NONE.equals(second))
    {
      return first;
    }

    // If none of the styles are 'hidden' and at least one of them is not
    // 'none', then narrow borders are discarded in favor of wider ones.
    final long firstWidth = first.getWidth().resolve(computedParentWidth);
    final long secondWidth = second.getWidth().resolve(computedParentWidth);
    if (firstWidth < secondWidth)
    {
      return second;
    }
    else if (secondWidth < firstWidth)
    {
      return first;
    }

    // If several have the same 'border-width' then styles are preferred in
    // this order: 'double', 'solid', 'dashed', 'dotted', 'ridge', 'outset',
    // 'groove', and the lowest: 'inset'.
    final int firstStyleWeight = getStylePreferrence(firstStyle);
    final int secondStyleWeight = getStylePreferrence(secondStyle);
    if (firstStyleWeight > secondStyleWeight)
    {
      return first;
    }
    return second;
  }

  private static int getStylePreferrence (CSSValue value)
  {
    if (BorderStyle.DOUBLE.equals(value))
    {
      return 10;
    }
    if (BorderStyle.SOLID.equals(value))
    {
      return 9;
    }
    if (BorderStyle.DASHED.equals(value))
    {
      return 8;
    }
    if (BorderStyle.DOT_DASH.equals(value))
    {
      return 7;
    }
    if (BorderStyle.DOT_DOT_DASH.equals(value))
    {
      return 6;
    }
    if (BorderStyle.DOTTED.equals(value))
    {
      return 5;
    }
    if (BorderStyle.RIDGE.equals(value))
    {
      return 4;
    }
    if (BorderStyle.OUTSET.equals(value))
    {
      return 3;
    }
    if (BorderStyle.GROOVE.equals(value))
    {
      return 2;
    }
    if (BorderStyle.INSET.equals(value))
    {
      return 1;
    }
    return 0;
  }
}
