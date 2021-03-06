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

package org.jfree.fonts.monospace;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontType;

/**
 * Creation-Date: 13.05.2007, 13:14:16
 *
 * @author Thomas Morgner
 */
public class MonospaceFontRecord implements FontRecord, FontIdentifier
{
  private MonospaceFontFamily fontFamily;
  private boolean bold;
  private boolean italics;

  public MonospaceFontRecord(final MonospaceFontFamily fontFamily, final boolean bold, final boolean italics)
  {
    if (fontFamily == null)
    {
      throw new NullPointerException();
    }
    this.fontFamily = fontFamily;
    this.bold = bold;
    this.italics = italics;
  }

  /**
   * Returns the family for this record.
   *
   * @return the font family.
   */
  public FontFamily getFamily()
  {
    return fontFamily;
  }

  /**
   * Returns true, if this font corresponds to a bold version of the font. A font that does not provide a bold face must
   * emulate the boldness using other means.
   *
   * @return true, if the font provides bold glyphs, false otherwise.
   */
  public boolean isBold()
  {
    return bold;
  }

  /**
   * Returns true, if this font includes italic glyphs. Italics is different from oblique, as certain glyphs (most
   * notably the lowercase 'f') will have a different appearance, making the font look more like a script font.
   *
   * @return true, if the font is italic.
   */
  public boolean isItalic()
  {
    return false;
  }

  /**
   * Returns tue, if this font's italic mode is in fact some sort of being oblique. An oblique font's glyphs are
   * sheared, but they are not made to look more script like.
   *
   * @return true, if the font is oblique. All italic fonts are also oblique.
   */
  public boolean isOblique()
  {
    return italics;
  }

  /**
   * Defines, whether the font identifier represents a scalable font type. Such fonts usually create one font metric
   * object for each physical font, and apply the font size afterwards.
   *
   * @return true, if the font is scalable, false otherwise
   */
  public boolean isScalable()
  {
    return false;
  }

  /**
   * Returns the general type of this font identifier. This is for debugging, not for the real world.
   *
   * @return
   */
  public FontType getFontType()
  {
    return FontType.MONOSPACE;
  }

  public FontIdentifier getIdentifier()
  {
    return this;
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

    final MonospaceFontRecord that = (MonospaceFontRecord) o;

    if (bold != that.bold)
    {
      return false;
    }
    if (italics != that.italics)
    {
      return false;
    }
    if (!fontFamily.equals(that.fontFamily))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = fontFamily.hashCode();
    result = 29 * result + (bold ? 1 : 0);
    result = 29 * result + (italics ? 1 : 0);
    return result;
  }
}
