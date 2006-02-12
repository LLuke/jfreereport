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
 * FontSizeResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.computed.fonts;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontSizeConstant;
import org.jfree.layouting.input.style.keys.font.RelativeFontSize;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

/**
 * Creation-Date: 18.12.2005, 17:03:15
 *
 * @author Thomas Morgner
 */
public class FontSizeResolveHandler extends ConstantsResolveHandler
{
  private static final String SIZE_FACTOR_PREFIX =
          "org.jfree.layouting.defaults.FontSizeFactor.";
  private double fontSize;
  private CSSNumericValue[] predefinedSizes;
  private double[] predefinedScalingFactors;

  public FontSizeResolveHandler()
  {
    fontSize = parseDouble("org.jfree.layouting.defaults.FontSize", 12);
    predefinedSizes = new CSSNumericValue[7];
    predefinedSizes[0] = computePredefinedSize(FontSizeConstant.XX_SMALL);
    predefinedSizes[1] = computePredefinedSize(FontSizeConstant.X_SMALL);
    predefinedSizes[2] = computePredefinedSize(FontSizeConstant.SMALL);
    predefinedSizes[3] = computePredefinedSize(FontSizeConstant.MEDIUM);
    predefinedSizes[4] = computePredefinedSize(FontSizeConstant.LARGE);
    predefinedSizes[5] = computePredefinedSize(FontSizeConstant.X_LARGE);
    predefinedSizes[6] = computePredefinedSize(FontSizeConstant.XX_LARGE);

    predefinedScalingFactors = new double[7];
    predefinedScalingFactors[0] = computePredefinedScalingFactor(FontSizeConstant.XX_SMALL);
    predefinedScalingFactors[1] = computePredefinedScalingFactor(FontSizeConstant.X_SMALL);
    predefinedScalingFactors[2] = computePredefinedScalingFactor(FontSizeConstant.SMALL);
    predefinedScalingFactors[3] = computePredefinedScalingFactor(FontSizeConstant.MEDIUM);
    predefinedScalingFactors[4] = computePredefinedScalingFactor(FontSizeConstant.LARGE);
    predefinedScalingFactors[5] = computePredefinedScalingFactor(FontSizeConstant.X_LARGE);
    predefinedScalingFactors[6] = computePredefinedScalingFactor(FontSizeConstant.XX_LARGE);

    addValue(FontSizeConstant.XX_SMALL, predefinedSizes[0]);
    addValue(FontSizeConstant.X_SMALL, predefinedSizes[1]);
    addValue(FontSizeConstant.SMALL, predefinedSizes[2]);
    addValue(FontSizeConstant.MEDIUM, predefinedSizes[3]);
    addValue(FontSizeConstant.LARGE, predefinedSizes[4]);
    addValue(FontSizeConstant.X_LARGE, predefinedSizes[5]);
    addValue(FontSizeConstant.XX_LARGE, predefinedSizes[6]);
  }

  private CSSNumericValue computePredefinedSize(FontSizeConstant c)
  {
    String key = SIZE_FACTOR_PREFIX + c.getCSSText();
    double scaling = parseDouble(key, 100);
    return new CSSNumericValue(CSSNumericType.PT, fontSize * scaling / 100d);
  }

  private double computePredefinedScalingFactor(FontSizeConstant c)
  {
    String key = SIZE_FACTOR_PREFIX + c.getCSSText();
    return parseDouble(key, 100);
  }

  private double parseDouble(String configKey, double defaultValue)
  {
    String value = LibLayoutBoot.getInstance().getGlobalConfig()
            .getConfigProperty(configKey);
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return Double.parseDouble(value);
    }
    catch (NumberFormatException nfe)
    {
      return defaultValue;
    }
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
    CSSValue value = style.getValue(key);
    if (value instanceof CSSConstant == false)
    {
      // fine, we're done here ...
      return;
    }
    CSSConstant constant = (CSSConstant) value;
    final LayoutNode parent = currentNode.getParent();
    if (parent != null)
    {
      final double parentFontSize =
              parent.getLayoutContext().getFontSpecification().getFontSize();
      if (RelativeFontSize.LARGER.equals(value))
      {
        final double scaleFactor = getScaleLargerFactor(parentFontSize);
        style.setValue(key, new CSSNumericValue(CSSNumericType.PERCENTAGE, scaleFactor));
        return;
      }

      if (RelativeFontSize.SMALLER.equals(value))
      {
        final double scaleFactor = getScaleSmallerFactor(parentFontSize);
        style.setValue(key, new CSSNumericValue(CSSNumericType.PERCENTAGE, scaleFactor));
        return;
      }
    }
    else
    {
      // we might not have a parent, but that won't stop us ..
      if (RelativeFontSize.LARGER.equals(value))
      {
        style.setValue(key, new CSSNumericValue(CSSNumericType.PERCENTAGE, 120));
        return ;
      }

      if (RelativeFontSize.SMALLER.equals(value))
      {
        style.setValue(key, new CSSNumericValue(CSSNumericType.PERCENTAGE, 85));
        return;
      }
    }

    final CSSValue resolvedValue = lookupValue(constant);
    if (resolvedValue != null)
    {
      style.setValue(key, resolvedValue);
      return;
    }
    
    // do not change the font size..
    style.setValue(key, new CSSNumericValue(CSSNumericType.PERCENTAGE, 100));
  }

  public double getScaleLargerFactor(double parentSize)
  {
    for (int i = 0; i < predefinedSizes.length; i++)
    {
      final CSSNumericValue size = predefinedSizes[i];
      if (parentSize < size.getValue())
      {
        return predefinedScalingFactors[i];
      }
    }
    return predefinedScalingFactors[6];
  }

  public double getScaleSmallerFactor(double parentSize)
  {
    for (int i = predefinedSizes.length; i >= 0; i--)
    {
      final CSSNumericValue size = predefinedSizes[i];
      if (parentSize > size.getValue())
      {
        return predefinedScalingFactors[i];
      }
    }
    return predefinedScalingFactors[0];
  }
}
