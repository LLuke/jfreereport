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
 * FontSizeResolveHandler.java
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
import org.jfree.layouting.layouter.style.resolver.percentages.LengthResolverUtility;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.LayoutContext;
import org.jfree.layouting.model.font.FontSpecification;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.fonts.LibFontsDefaults;

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
   * @param style
   * @param currentNode
   */
  public void resolve(LayoutProcess process,
                         LayoutNode currentNode,
                         LayoutStyle style,
                         StyleKey key)
  {
    final CSSValue value = style.getValue(key);
    final LayoutNode parent = currentNode.getParent();
    final FontSpecification fontSpecification =
            currentNode.getLayoutContext().getFontSpecification();

    if (value instanceof CSSNumericValue == false)
    {
      if (parent == null)
      {
        fontSpecification.setFontSize(this.baseFontSize);
        style.setValue(key, new CSSNumericValue(CSSNumericType.PT, baseFontSize));
      }
      else
      {
        final LayoutContext parentContext = parent.getLayoutContext();
        final FontSpecification parentFont = parentContext.getFontSpecification();
        final double fontSize = parentFont.getFontSize();
        fontSpecification.setFontSize(fontSize);
        style.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
      }
      return;
    }

    CSSNumericValue nval = (CSSNumericValue) value;
    if (LengthResolverUtility.isAbsoluteValue(nval))
    {
      final long fsize = LengthResolverUtility.convertLengthToInternal
              (nval,currentNode, process.getOutputMetaData());
      final double fontSize = fsize / 1000d;
      fontSpecification.setFontSize(fontSize);
      style.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
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
      style.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
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
      style.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
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
      style.setValue(key, new CSSNumericValue(CSSNumericType.PT, fontSize));
    }
    else
    {
      fontSpecification.setFontSize(this.baseFontSize);
      style.setValue(key, new CSSNumericValue(CSSNumericType.PT, this.baseFontSize));
    }
  }
}
