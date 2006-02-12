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
 * AWTFontRecord.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 16.12.2005 : Initial version
 */
package org.jfree.fonts.awt;

import org.jfree.fonts.io.FontDataInputSource;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontType;

/**
 * Creation-Date: 16.12.2005, 20:06:51
 *
 * @author Thomas Morgner
 */
public class AWTFontRecord implements FontRecord, FontIdentifier
{
  private FontFamily fontFamily;
  private boolean bold;
  private boolean italics;

  public AWTFontRecord(final FontFamily fontFamily,
                       final boolean bold, final boolean italics)
  {
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
   * Returns true, if this font corresponds to a bold version of the font. A
   * font that does not provide a bold face must emulate the boldness using
   * other means.
   *
   * @return true, if the font provides bold glyphs, false otherwise.
   */
  public boolean isBold()
  {
    return bold;
  }

  /**
   * Returns true, if this font includes italic glyphs. Italics is different
   * from oblique, as certain glyphs (most notably the lowercase 'f') will have
   * a different appearance, making the font look more like a script font.
   *
   * @return true, if the font is italic.
   */
  public boolean isItalic()
  {
    return italics;
  }

  /**
   * Returns tue, if this font's italic mode is in fact some sort of being
   * oblique. An oblique font's glyphs are sheared, but they are not made to
   * look more script like.
   *
   * @return true, if the font is oblique.
   */
  public boolean isOblique()
  {
    return italics;
  }

  /**
   * Returns the file name used to load the font.
   *
   * @return
   * @deprecated this is limited to the local filesystem, use the InputSource
   *             instead
   */
  public String getFontFile()
  {
    return null;
  }

  public FontDataInputSource getFontInputSource()
  {
    // AWT fonts do not have a datasource
    return null;
  }

  public boolean isEmbeddable()
  {
    return false;
  }

  public String getName()
  {
    return fontFamily.getFamilyName();
  }

  public String[] getAllNames()
  {
    return new String[0];
  }

  public String getVariant()
  {
    if (isBold() && isItalic())
    {
      return "Bold-Italic";
    }
    if (isBold())
    {
      return "Bold";
    }
    if (isItalic())
    {
      return "Italic";
    }
    return "Plain";
  }

  public String[] getAllVariants()
  {
    return new String[] {
            getVariant()
    };
  }

  public FontIdentifier getIdentifier()
  {
    return this;
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
    return FontType.AWT;
  }
}