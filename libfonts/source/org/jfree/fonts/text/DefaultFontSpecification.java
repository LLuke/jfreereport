/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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

package org.jfree.fonts.text;

/**
 * Creation-Date: 04.04.2007, 14:03:19
 *
 * @author Thomas Morgner
 */
public class DefaultFontSpecification implements FontSpecification
{
  private String fontFamily;
  private String encoding;
  private boolean embedFontData;
  private boolean antiAliasing;
  private boolean smallCaps;
  private boolean italic;
  private boolean oblique;
  private int fontWeight;
  private double fontSize;

  public DefaultFontSpecification()
  {
  }

  public DefaultFontSpecification(final String fontFamily,
                                  final double fontSize,
                                  final int fontWeight,
                                  final boolean italic,
                                  final boolean oblique,
                                  final boolean smallCaps,
                                  final boolean antiAliasing,
                                  final String encoding,
                                  final boolean embedFontData)
  {
    this.fontFamily = fontFamily;
    this.fontSize = fontSize;
    this.fontWeight = fontWeight;
    this.italic = italic;
    this.oblique = oblique;
    this.smallCaps = smallCaps;
    this.antiAliasing = antiAliasing;
    this.encoding = encoding;
    this.embedFontData = embedFontData;
  }

  public String getFontFamily()
  {
    return fontFamily;
  }

  public void setFontFamily(final String fontFamily)
  {
    this.fontFamily = fontFamily;
  }

  public boolean isAntiAliasing()
  {
    return antiAliasing;
  }

  public void setAntiAliasing(final boolean antiAliasing)
  {
    this.antiAliasing = antiAliasing;
  }

  public boolean isSmallCaps()
  {
    return smallCaps;
  }

  public void setSmallCaps(final boolean smallCaps)
  {
    this.smallCaps = smallCaps;
  }

  public boolean isItalic()
  {
    return italic;
  }

  public void setItalic(final boolean italic)
  {
    this.italic = italic;
  }

  public boolean isOblique()
  {
    return oblique;
  }

  public void setOblique(final boolean oblique)
  {
    this.oblique = oblique;
  }

  public int getFontWeight()
  {
    return fontWeight;
  }

  public void setFontWeight(final int fontWeight)
  {
    this.fontWeight = fontWeight;
  }

  public double getFontSize()
  {
    return fontSize;
  }

  public void setFontSize(final double fontSize)
  {
    this.fontSize = fontSize;
  }

  public String getEncoding()
  {
    return encoding;
  }

  public void setEncoding(final String encoding)
  {
    this.encoding = encoding;
  }

  public boolean isEmbedFontData()
  {
    return embedFontData;
  }

  public void setEmbedFontData(final boolean embedFontData)
  {
    this.embedFontData = embedFontData;
  }
}
