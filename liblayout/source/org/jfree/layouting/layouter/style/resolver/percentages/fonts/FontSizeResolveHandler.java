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
package org.jfree.layouting.layouter.style.resolver.percentages.fonts;

import org.jfree.fonts.LibFontsDefaults;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

/**
 * Creation-Date: 18.12.2005, 18:06:23
 *
 * @author Thomas Morgner
 */
public class FontSizeResolveHandler implements ResolveHandler
{
  private double baseFontSize;

  public FontSizeResolveHandler()
  {
    baseFontSize = parseDouble("org.jfree.layouting.defaults.FontSize", 12);
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
   * @param currentNode
   * @param style
   */
  public void resolve(LayoutProcess process,
                         LayoutElement currentNode,
                         StyleKey key)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final CSSValue value = layoutContext.getValue(key);
    final LayoutElement parent = currentNode.getParent();
    final FontSpecification fontSpecification =
            currentNode.getLayoutContext().getFontSpecification();

    if (value instanceof CSSNumericValue == false)
    {
      if (parent == null)
      {
        fontSpecification.setFontSize(this.baseFontSize);
        layoutContext.setValue(key, new CSSNumericValue(CSSNumericType.PT, baseFontSize));
      }
      else
      {
        final LayoutContext parentContext = parent.getLayoutContext();
        final FontSpecification parentFont = parentContext.getFontSpecification();
        final double fontSize = parentFont.getFontSize();
        fontSpecification.setFontSize(fontSize);
        layoutContext.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
      }
      return;
    }

    CSSNumericValue nval = (CSSNumericValue) value;
    if (CSSValueResolverUtility.isAbsoluteValue(nval))
    {
      final CSSNumericValue fsize = CSSValueResolverUtility.convertLength
              (nval,currentNode.getLayoutContext(), process.getOutputMetaData());
      final double fontSize = fsize.getValue();
      fontSpecification.setFontSize(fontSize);
      layoutContext.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
    }
    // we encountered one of the relative values.
    else if (CSSNumericType.EM.equals(nval.getType()))
    {
      final double parentSize;
      if (parent == null)
      {
        parentSize = this.baseFontSize;
      }
      else
      {
        final LayoutContext parentContext = parent.getLayoutContext();
        final FontSpecification parentFont = parentContext.getFontSpecification();
        parentSize = parentFont.getFontSize();
      }
      final double fontSize = parentSize * nval.getValue();
      fontSpecification.setFontSize(fontSize);
      layoutContext.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
    }
    else if (CSSNumericType.EX.equals(nval.getType()))
    {
      final double parentSize;
      if (parent == null)
      {
        // if we have no parent, we create a fixed default value.
        parentSize = this.baseFontSize *
                (LibFontsDefaults.DEFAULT_XHEIGHT_SIZE /
                        LibFontsDefaults.DEFAULT_ASCENT_SIZE);
      }
      else
      {
        final LayoutContext parentContext = parent.getLayoutContext();
        final FontSpecification parentFont = parentContext.getFontSpecification();
        parentSize = parentFont.getFontSize();
      }
      final double fontSize = parentSize * nval.getValue();
      layoutContext.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
      fontSpecification.setFontSize(fontSize);
    }
    else if (CSSNumericType.PERCENTAGE.equals(nval.getType()))
    {
      final double parentSize;
      if (parent == null)
      {
        // if we have no parent, we create a fixed default value.
        parentSize = this.baseFontSize;
      }
      else
      {
        final LayoutContext parentContext = parent.getLayoutContext();
        final FontSpecification parentFont = parentContext.getFontSpecification();
        parentSize = parentFont.getFontSize();
      }
      final double fontSize = parentSize * nval.getValue() / 100d;
      fontSpecification.setFontSize(fontSize);
      layoutContext.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
    }
    else
    {
      fontSpecification.setFontSize(this.baseFontSize);
      layoutContext.setValue(key, new CSSNumericValue(CSSNumericType.PT, this.baseFontSize));
    }
  }
}
