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
 * FontSpecification.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: FontSpecification.java,v 1.1 2006/02/12 21:43:08 taqua Exp $
 *
 * Changes
 * -------------------------
 * 15.12.2005 : Initial version
 */
package org.jfree.layouting.model.font;

import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.output.OutputProcessorFeature;
import org.jfree.layouting.input.style.keys.font.FontStretch;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 15.12.2005, 11:44:22
 *
 * @author Thomas Morgner
 */
public class FontSpecification implements FontContext
{
  private String fontFamily;
  private int fontWeight;
  private boolean italic;
  private boolean oblique;
  private double fontSize; // in point
  private double minimumFontSize;
  private double maximumFontSize;

  private boolean fractionalMetrics;
  private boolean antiAliased;
  private boolean allowAntiAliasing;
  private boolean smallCaps;
  private OutputProcessor outputProcessor;
  private FontMetrics fontMetrics;
  private FontStretch fontStretch;

  public FontSpecification(final OutputProcessor outputProcessor)
  {
    this.outputProcessor = outputProcessor;
    this.fractionalMetrics =
            outputProcessor.getMetaData().isFeatureSupported
                    (OutputProcessorFeature.FONT_FRACTIONAL_METRICS);

    this.allowAntiAliasing =
            outputProcessor.getMetaData().isFeatureSupported
                    (OutputProcessorFeature.FONT_SUPPORTS_ANTI_ALIASING);
  }

  public OutputProcessor getOutputProcessor()
  {
    return outputProcessor;
  }

  public boolean isValid()
  {
    final FontStorage storage = outputProcessor.getFontStorage();
    final FontRegistry registry = storage.getFontRegistry();
    final String fontFamily = getFontFamily();
    if (fontFamily == null)
    {
      return false;
    }
    final FontFamily family = registry.getFontFamily(fontFamily);
    return family != null;
  }

  public FontMetrics getFontMetrics()
  {
    if (this.fontMetrics == null)
    {
      final String fontFamily = getFontFamily();
      if (fontFamily == null)
      {
        return null;
      }
      final FontStorage storage = outputProcessor.getFontStorage();
      final FontRegistry registry = storage.getFontRegistry();
      final FontFamily family = registry.getFontFamily(fontFamily);
      if (family == null)
      {
        return null;
      }
      final FontRecord record = family.getFontRecord
              (getFontWeight() > 600, isItalic() || isOblique());
      this.fontMetrics = storage.getFontMetrics(record.getIdentifier(), this);
    }
    return this.fontMetrics;
  }

  /**
   * This is controlled by the output target and the stylesheet. If the output
   * target does not support aliasing, it makes no sense to enable it and all
   * such requests are ignored.
   *
   * @return
   */
  public boolean isAntiAliased()
  {
    return antiAliased;
  }

  public void setAntiAliased(final boolean antiAliased)
  {
    boolean oldValue = this.antiAliased;
    this.antiAliased = allowAntiAliasing && antiAliased;
    if (oldValue != this.antiAliased)
    {
      this.fontMetrics = null;
    }
  }

  /**
   * This is defined by the output target. This is not controlled by the
   * stylesheet.
   *
   * @return
   */
  public boolean isFractionalMetrics()
  {
    return fractionalMetrics;
  }

  /**
   * The requested font size. A font may have a fractional font size (ie. 8.5
   * point). The font size may be influenced by the output target.
   *
   * @return the font size.
   */
  public double getFontSize()
  {
    return fontSize;
  }

  public void setFontSize(final double fontSize)
  {
    double oldValue = this.fontSize;
    this.fontSize = fontSize;
    if (oldValue != fontSize)
    {
      this.fontMetrics = null;
    }
  }

  public String getFontFamily()
  {
    return fontFamily;
  }

  public void setFontFamily(final String fontFamily)
  {
    String oldValue = this.fontFamily;
    this.fontFamily = fontFamily;
    if (ObjectUtilities.equal(oldValue, fontFamily) == false)
    {
      this.fontMetrics = null;
    }
  }

  public int getFontWeight()
  {
    return fontWeight;
  }

  public void setFontWeight(final int fontWeight)
  {
    int oldValue = this.fontWeight;
    this.fontWeight = fontWeight;
    if (oldValue != this.fontWeight)
    {
      this.fontMetrics = null;
    }
  }

  public boolean isItalic()
  {
    return italic;
  }

  public void setItalic(final boolean italic)
  {
    boolean oldValue = this.italic;
    this.italic = italic;
    if (oldValue != this.italic)
    {
      this.fontMetrics = null;
    }
  }

  public boolean isOblique()
  {
    return oblique;
  }

  public void setOblique(final boolean oblique)
  {
    boolean oldValue = this.oblique;
    this.oblique = oblique;
    if (oldValue != this.oblique)
    {
      this.fontMetrics = null;
    }
  }

  public FontStretch getFontStretch()
  {
    return fontStretch;
  }

  public void setFontStretch(final FontStretch fontStretch)
  {
    this.fontStretch = fontStretch;
  }

  public boolean isSmallCaps()
  {
    return smallCaps;
  }

  public void setSmallCaps(final boolean smallCaps)
  {
    this.smallCaps = smallCaps;
  }

  public double getMinimumFontSize()
  {
    return minimumFontSize;
  }

  public void setMinimumFontSize(final double minimumFontSize)
  {
    this.minimumFontSize = minimumFontSize;
  }

  public double getMaximumFontSize()
  {
    return maximumFontSize;
  }

  public void setMaximumFontSize(final double maximumFontSize)
  {
    this.maximumFontSize = maximumFontSize;
  }
}
