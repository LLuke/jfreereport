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
 * AWTFontFamily.java
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

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;


/**
 * Creation-Date: 16.12.2005, 20:44:11
 *
 * @author Thomas Morgner
 */
public class AWTFontFamily implements FontFamily
{
  private String fontName;
  private AWTFontRecord[] fonts;

  public AWTFontFamily(final String fontName)
  {
    this.fontName = fontName;
    this.fonts = new AWTFontRecord[4];
  }

  /**
   * Returns the name of the font family (in english).
   *
   * @return
   */
  public String getFamilyName()
  {
    return fontName;
  }

  public String[] getAllNames()
  {
    return new String[] { fontName };
  }

  /**
   * This selects the most suitable font in that family. Italics fonts are
   * preferred over oblique fonts.
   *
   * @param bold
   * @param italics
   * @return
   */
  public FontRecord getFontRecord(final boolean bold, final boolean italics)
  {

    int index = 0;
    if (bold)
    {
      index += 1;
    }
    if (italics)
    {
      index += 2;
    }
    if (fonts[index] != null) return fonts[index];
    fonts[index] = new AWTFontRecord(this, bold, italics);
    return fonts[index];
  }
}
