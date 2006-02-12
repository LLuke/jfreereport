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
 * AbstractOutputProcessorMetaData.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: AbstractOutputProcessorMetaData.java,v 1.1 2006/02/12 21:40:16 taqua Exp $
 *
 * Changes
 * -------------------------
 * 02.01.2006 : Initial version
 */
package org.jfree.layouting.output;

import java.util.HashMap;
import java.util.HashSet;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.input.style.keys.font.FontSizeConstant;
import org.jfree.util.ExtendedConfiguration;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRegistry;

/**
 * Creation-Date: 02.01.2006, 18:39:46
 *
 * @author Thomas Morgner
 */
public abstract class AbstractOutputProcessorMetaData
        implements OutputProcessorMetaData
{
  private HashSet features;
  private int defaultFontSize;
  private HashMap fontSizes;
  private HashMap numericFeatures;
  private HashMap fontFamilies;
  private FontRegistry fontRegistry;

  protected AbstractOutputProcessorMetaData(final FontRegistry fontRegistry)
  {
    this.fontRegistry = fontRegistry;
    this.features = new HashSet();
    this.numericFeatures = new HashMap();

    ExtendedConfiguration extendedConfig =
            LibLayoutBoot.getInstance().getExtendedConfig();
    defaultFontSize =
            extendedConfig.getIntProperty(
                    "org.jfree.layouting.defaults.FontSize", 12);

    final int xxSmall = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.xx-small", 60);
    final int xSmall = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.x-small", 75);
    final int small = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.small", 89);
    final int medium = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.medium", 100);
    final int large = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.large", 120);
    final int xLarge = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.x-large", 150);
    final int xxLarge = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.xx-large", 200);

    fontSizes = new HashMap();
    fontSizes.put(FontSizeConstant.XX_SMALL,
            new Double(defaultFontSize * xxSmall / 100d));
    fontSizes.put(FontSizeConstant.X_SMALL,
            new Double(defaultFontSize * xSmall / 100d));
    fontSizes.put(FontSizeConstant.SMALL,
            new Double(defaultFontSize * small / 100d));
    fontSizes.put(FontSizeConstant.MEDIUM,
            new Double(defaultFontSize * medium / 100d));
    fontSizes.put(FontSizeConstant.LARGE,
            new Double(defaultFontSize * large / 100d));
    fontSizes.put(FontSizeConstant.X_LARGE,
            new Double(defaultFontSize * xLarge / 100d));
    fontSizes.put(FontSizeConstant.XX_LARGE,
            new Double(defaultFontSize * xxLarge / 100d));


    fontFamilies = new HashMap();

    setNumericFeatureValue(OutputProcessorFeature.DEFAULT_FONT_SIZE,
            defaultFontSize);

  }

  protected void setFamilyMapping(FontFamilyValues family, String name)
  {
    if (family == null)
    {
      throw new NullPointerException();
    }
    if (name == null)
    {
      throw new NullPointerException();
    }
    fontFamilies.put(family, name);
  }

  public double getFontSize(FontSizeConstant constant)
  {
    Double d = (Double) fontSizes.get(constant);
    if (d == null)
    {
      return defaultFontSize;
    }
    return d.doubleValue();
  }

  protected void addFeature
          (OutputProcessorFeature.BooleanOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    this.features.add(feature);
  }

  public boolean isFeatureSupported
          (OutputProcessorFeature.BooleanOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    return this.features.contains(feature);
  }

  protected void setNumericFeatureValue
          (OutputProcessorFeature.NumericOutputProcessorFeature feature,
           double value)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    numericFeatures.put(feature, new Double(value));
  }

  public double getNumericFeatureValue
          (OutputProcessorFeature.NumericOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    final Double d = (Double) numericFeatures.get(feature);
    if (d == null)
    {
      return 0;
    }
    return d.doubleValue();
  }

  public FontFamily getFontFamily(FontFamilyValues genericName)
  {
    if (FontFamilyValues.NONE.equals(genericName))
    {
      return null;
    }

    final String name = (String) fontFamilies.get(genericName);
    if (name == null)
    {
      return getDefaultFontFamily();
    }

    FontFamily ff = fontRegistry.getFontFamily(name);
    if (ff != null)
    {
      return ff;
    }
    return getDefaultFontFamily();
  }

  protected FontRegistry getFontRegistry()
  {
    return fontRegistry;
  }
}
