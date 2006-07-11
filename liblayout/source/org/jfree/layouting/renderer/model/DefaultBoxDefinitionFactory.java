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
 * DefaultBoxDefinitionFactory.java
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
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.color.CSSSystemColors;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.BorderFactory;
import org.jfree.layouting.renderer.border.RenderLength;

/**
 * Creation-Date: 25.06.2006, 15:46:01
 *
 * @author Thomas Morgner
 */
public class DefaultBoxDefinitionFactory implements BoxDefinitionFactory
{
  private BorderFactory borderFactory;

  public DefaultBoxDefinitionFactory(BorderFactory borderFactory)
  {
    this.borderFactory = borderFactory;
  }

  public BoxDefinition createBlockBoxDefinition(LayoutContext boxContext,
                                                OutputProcessorMetaData metaData)
  {
    final Border border = borderFactory.createBorder(boxContext, metaData);
    DefaultBoxDefinition boxDefinition = new DefaultBoxDefinition();
    boxDefinition.setBorder(border);
    final CSSValue value = boxContext.getStyle().getValue(BorderStyleKeys.BACKGROUND_COLOR);
    if (value instanceof CSSColorValue)
    {
      boxDefinition.setBackgroundColor((CSSColorValue) value);
    }
    else
    {
      boxDefinition.setBackgroundColor(CSSSystemColors.TRANSPARENT);
    }

    final LayoutStyle style = boxContext.getStyle();

    fillHorizontalPadding(boxDefinition, style, boxContext, metaData);

    boxDefinition.setPreferredWidth(computeWidth
            (style.getValue(BoxStyleKeys.WIDTH), boxContext, metaData, true, true));
    boxDefinition.setMarginLeft(computeWidth
            (style.getValue(BoxStyleKeys.MARGIN_LEFT), boxContext, metaData, true, true));
    boxDefinition.setMarginRight(computeWidth
            (style.getValue(BoxStyleKeys.MARGIN_RIGHT), boxContext, metaData, true, true));

    fillVerticalModel(boxDefinition, style, boxContext, metaData);

    return boxDefinition;
  }

  public BoxDefinition createInlineBoxDefinition(LayoutContext boxContext,
                                                 OutputProcessorMetaData metaData)
  {
    final Border border = borderFactory.createBorder(boxContext, metaData);
    DefaultBoxDefinition boxDefinition = new DefaultBoxDefinition();
    boxDefinition.setBorder(border);
    final CSSValue value = boxContext.getStyle().getValue(BorderStyleKeys.BACKGROUND_COLOR);
    if (value instanceof CSSColorValue)
    {
      boxDefinition.setBackgroundColor((CSSColorValue) value);
    }
    else
    {
      boxDefinition.setBackgroundColor(CSSSystemColors.TRANSPARENT);
    }

    final LayoutStyle style = boxContext.getStyle();

    fillHorizontalPadding(boxDefinition, style, boxContext, metaData);

    // inline-elements have no way to define the width. (10.3.1 of CSS2.1)
    boxDefinition.setPreferredWidth(RenderLength.EMPTY);
    boxDefinition.setMarginLeft(computeWidth
            (style.getValue(BoxStyleKeys.MARGIN_LEFT), boxContext, metaData, false, true));
    boxDefinition.setMarginRight(computeWidth
            (style.getValue(BoxStyleKeys.MARGIN_RIGHT), boxContext, metaData, false, true));

    // second, the vertical model.
    fillVerticalModel(boxDefinition, style, boxContext, metaData);

    return boxDefinition;
  }

  private void fillVerticalModel(final DefaultBoxDefinition boxDefinition,
                                 final LayoutStyle style,
                                 final LayoutContext boxContext,
                                 final OutputProcessorMetaData metaData)
  {
    boxDefinition.setPaddingTop(computeWidth
            (style.getValue(BoxStyleKeys.PADDING_TOP), boxContext, metaData, false, false));
    boxDefinition.setPaddingBottom(computeWidth
            (style.getValue(BoxStyleKeys.PADDING_BOTTOM), boxContext, metaData, false, false));

    boxDefinition.setMarginTop(computeWidth
            (style.getValue(BoxStyleKeys.MARGIN_TOP), boxContext, metaData, false, true));
    boxDefinition.setMarginBottom(computeWidth
            (style.getValue(BoxStyleKeys.MARGIN_BOTTOM), boxContext, metaData, false, true));

    // I dont believe in Voodoo, therefore I dont follow Section 10.6.3 on the
    // height computation. Auto-Values are now generally accepted, if no length
    // has been specified explicitly. In that case we simply compute what ever
    // comes in and do not overflow in any case.
    boxDefinition.setPreferredHeight(computeWidth
            (style.getValue(BoxStyleKeys.HEIGHT), boxContext, metaData, true, true));
  }

  private void fillHorizontalPadding(final DefaultBoxDefinition boxDefinition,
                                     final LayoutStyle style,
                                     final LayoutContext boxContext,
                                     final OutputProcessorMetaData metaData)
  {
    // first, the horizontal model.
    boxDefinition.setPaddingLeft(computeWidth
            (style.getValue(BoxStyleKeys.PADDING_LEFT),
                    boxContext, metaData, false, false));
    boxDefinition.setPaddingRight(computeWidth
            (style.getValue(BoxStyleKeys.PADDING_RIGHT),
                    boxContext, metaData, false, false));
    boxDefinition.setMaximumWidth(computeWidth
            (style.getValue(BoxStyleKeys.MAX_WIDTH),
                    boxContext, metaData, false, false));
    boxDefinition.setMaximumHeight(computeWidth
            (style.getValue(BoxStyleKeys.MAX_HEIGHT),
                    boxContext, metaData, false, false));
    boxDefinition.setMinimumWidth(computeWidth
            (style.getValue(BoxStyleKeys.MIN_WIDTH),
                    boxContext, metaData, false, false));
    boxDefinition.setMinimumHeight(computeWidth
            (style.getValue(BoxStyleKeys.MIN_HEIGHT),
                    boxContext, metaData, false, false));
  }

  private RenderLength computeWidth(CSSValue widthValue,
                                    LayoutContext boxContext,
                                    OutputProcessorMetaData metaData,
                                    boolean allowAuto,
                                    boolean allowNegativeValues)
  {
    if (allowAuto && CSSAutoValue.getInstance().equals(widthValue))
    {
      return RenderLength.AUTO;
    }
    else if (widthValue instanceof CSSNumericValue == false)
    {
      return RenderLength.EMPTY;
    }
    else
    {
      final CSSNumericValue nval = (CSSNumericValue) widthValue;
      if (nval.getValue() < 0 && allowNegativeValues == true)
      {
        return RenderLength.convertToInternal(widthValue, boxContext, metaData);
      }
      else if (nval.getValue() > 0)
      {
        final RenderLength renderLength = RenderLength.convertToInternal(widthValue, boxContext, metaData);
        if (renderLength.getValue() > 0)
        {
          return renderLength;
        }
      }
      return RenderLength.EMPTY;
    }
  }

}
