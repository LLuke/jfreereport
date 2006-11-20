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
 * FontSpecification.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FontSpecification.java,v 1.1 2006/07/11 13:38:38 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.context;

import org.jfree.layouting.input.style.keys.font.FontStretch;
import org.jfree.layouting.input.style.keys.font.FontStyle;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.font.FontVariant;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 15.12.2005, 11:44:22
 *
 * @author Thomas Morgner
 */
public class FontSpecification
{
  private LayoutStyle style;

  private FontStretch fontStretch;

  private double fontSize;
  private String fontFamily;
  private boolean antiAliasing;

  public FontSpecification(final LayoutStyle style)
  {
    if (style == null)
    {
      throw new NullPointerException("Style must not be null.");
    }

    this.style = style;
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

  /**
   * The font size is resolved once during the resolve cycle. It is more than
   * just looking up the value in the stylesheet, as we have to take the font
   * size adjustment into account.
   *
   * @param fontSize the font size as used during the rendering.
   */
  public void setFontSize(final double fontSize)
  {
    this.fontSize = fontSize;
  }

  public String getFontFamily()
  {
    return fontFamily;
  }

  /**
   * Redefines the physical font family. The resolving is done once during the
   * style resolve run, logical fonts get mapped into their physical
   * counterparts.
   *
   * @param fontFamily
   */
  public void setFontFamily(final String fontFamily)
  {
    this.fontFamily = fontFamily;
  }

  public int getFontWeight()
  {
    final CSSValue val = style.getValue(FontStyleKeys.FONT_WEIGHT);
    if (val instanceof CSSNumericValue == false)
    {
      // this should not happen, shouldnt it?
      return 0;
    }
    CSSNumericValue nval = (CSSNumericValue) val;
    return (int) nval.getValue();
  }

  public boolean isItalic()
  {
    final CSSValue value = style.getValue(FontStyleKeys.FONT_STYLE);
    return FontStyle.ITALIC.equals(value) ||
           FontStyle.OBLIQUE.equals(value);
  }

  public boolean isOblique()
  {
    final CSSValue value = style.getValue(FontStyleKeys.FONT_STYLE);
    return FontStyle.OBLIQUE.equals(value);
  }

  public boolean isSmallCaps()
  {
    return FontVariant.SMALL_CAPS.equals(style.getValue(FontStyleKeys.FONT_VARIANT));
  }

  public boolean isAntiAliasing()
  {
    return antiAliasing;
  }

  public void setAntiAliasing(final boolean antiAliasing)
  {
    this.antiAliasing = antiAliasing;
  }
}
