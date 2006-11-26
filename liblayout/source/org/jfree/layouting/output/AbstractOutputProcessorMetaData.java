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
 * AbstractOutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AbstractOutputProcessorMetaData.java,v 1.10 2006/11/20 21:01:53 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output;

import java.awt.Image;
import java.util.HashMap;
import java.util.HashSet;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.input.style.keys.font.FontSizeConstant;
import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.ui.Drawable;
import org.jfree.util.ExtendedConfiguration;
import org.jfree.util.Log;

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
  private FontStorage fontStorage;
  private static final Class[] SUPPORTED_TYPES = new Class[] {Drawable.class, Image.class};

  protected AbstractOutputProcessorMetaData(final FontStorage fontStorage)
  {
    if (fontStorage == null)
    {
      throw new NullPointerException();
    }

    this.fontStorage = fontStorage;
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

  protected void setFamilyMapping(CSSConstant family, String name)
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

  public double getFontSize(CSSConstant constant)
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

  /**
   * Although most font systems are global, some may have some issues with
   * caching. OutputTargets may have to tweak the font storage system to their
   * needs.
   *
   * @return
   */
  public FontStorage getFontStorage()
  {
    return fontStorage;
  }

  public FontFamily getFontFamily(CSSConstant genericName)
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

    FontFamily ff = fontStorage.getFontRegistry().getFontFamily(name);
    if (ff != null)
    {
      return ff;
    }
    return getDefaultFontFamily();
  }

  protected FontRegistry getFontRegistry()
  {
    return fontStorage.getFontRegistry();
  }

  public PageSize getDefaultPageSize()
  {
//    throw new UnsupportedOperationException();
    return PageSize.A4;
    //return new PageSize(420, 200);
  }

  /**
   * Returns the vertical page span. If the value is zero or negative, no
   *
   * @return
   */
  public int getVerticalPageSpan()
  {
    return 1;
  }

  public int getHorizontalPageSpan()
  {
    return 1;
  }

  public String getMediaType()
  {
    return "print";
  }

  public boolean isValid(FontSpecification spec)
  {
    final FontRegistry registry = getFontRegistry();
    final String fontFamily = spec.getFontFamily();
    if (fontFamily == null)
    {
      return false;
    }
    final FontFamily family = registry.getFontFamily(fontFamily);
    return family != null;

  }

  public FontMetrics getFontMetrics(FontSpecification spec)
  {
    final String fontFamily = spec.getFontFamily();
    if (fontFamily == null)
    {
      Log.warn("No font family specified.");
      return null;
    }
    final FontRegistry registry = getFontRegistry();
    final FontFamily family = registry.getFontFamily(fontFamily);
    if (family == null)
    {
      Log.warn("Unable to lookup the font family.");
      return null;
    }
    final DefaultFontContext fontContext = new DefaultFontContext
                    (this, spec.isAntiAliasing(), spec.getFontSize());

    final FontRecord record = family.getFontRecord
            (spec.getFontWeight() > 600, spec.isItalic() || spec.isOblique());
    final FontMetrics fm = getFontStorage().getFontMetrics
            (record.getIdentifier(), fontContext);
    if (fm == null)
    {
      throw new NullPointerException("FontMetrics returned from factory is null.");
    }
    return fm;
  }

  public Class[] getSupportedResourceTypes()
  {
    return (Class[]) SUPPORTED_TYPES.clone();
  }
}
