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
package org.jfree.fonts.truetype;

import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontType;
import org.jfree.fonts.io.FontDataInputSource;

/**
 * Creation-Date: 16.12.2005, 19:35:31
 *
 * @author Thomas Morgner
 */
public class TrueTypeFontIdentifier implements FontIdentifier
{
  private FontDataInputSource inputSource;
  private String fontName;
  private String fontVariant;
  private int collectionIndex;
  private long offset;

  public TrueTypeFontIdentifier(final FontDataInputSource inputSource,
                                final String fontName,
                                final String fontVariant,
                                final int collectionIndex,
                                final long offset)
  {
    if (inputSource == null)
    {
      throw new NullPointerException();
    }
    if (fontName == null)
    {
      throw new NullPointerException();
    }
    if (fontVariant == null)
    {
      throw new NullPointerException();
    }
    this.fontVariant = fontVariant;
    this.inputSource = inputSource;
    this.fontName = fontName;
    this.collectionIndex = collectionIndex;
    this.offset = offset;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final TrueTypeFontIdentifier that = (TrueTypeFontIdentifier) o;

    if (collectionIndex != that.collectionIndex)
    {
      return false;
    }
    if (offset != that.offset)
    {
      return false;
    }
    if (!inputSource.equals(that.inputSource))
    {
      return false;
    }
    if (!fontName.equals(that.fontName))
    {
      return false;
    }
    return fontVariant.equals(that.fontVariant);

  }

  public int hashCode()
  {
    int result = fontName.hashCode();
    result = 29 * result + inputSource.hashCode();
    result = 29 * result + fontVariant.hashCode();
    result = 29 * result + collectionIndex;
    result = 29 * result + (int) (offset ^ (offset >>> 32));
    return result;
  }

  public FontDataInputSource getInputSource()
  {
    return inputSource;
  }

  public String getFontVariant()
  {
    return fontVariant;
  }

  public String getFontName()
  {
    return fontName;
  }

  public int getCollectionIndex()
  {
    return collectionIndex;
  }

  public long getOffset()
  {
    return offset;
  }

  /**
   * Defines, whether the font identifier represents a scalable font type. Such
   * fonts usually create one font metric object for each physical font, and
   * apply the font size afterwards.
   *
   * @return true, if the font is scalable, false otherwise
   */
  public boolean isScalable()
  {
    return true;
  }

  public FontType getFontType()
  {
    return FontType.OPENTYPE;
  }
}
