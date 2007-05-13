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
import org.jfree.fonts.registry.FontRecord;

/**
 * Creation-Date: 13.05.2007, 13:14:25
 *
 * @author Thomas Morgner
 */
public class MonospaceFontFamily implements FontFamily
{
  private String familyName;
  private FontRecord[] fonts;

  public MonospaceFontFamily(final String familyName)
  {
    if (familyName == null)
    {
      throw new NullPointerException();
    }
    this.familyName = familyName;
    this.fonts = new FontRecord[4];
  }

  /**
   * Returns the name of the font family (in english).
   *
   * @return
   */
  public String getFamilyName()
  {
    return familyName;
  }

  public String[] getAllNames()
  {
    return new String[]{ familyName };
  }

  /**
   * This selects the most suitable font in that family. Italics fonts are preferred over oblique fonts.
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
    if (fonts[index] != null)
    {
      return fonts[index];
    }
    fonts[index] = new MonospaceFontRecord(this, bold, italics);
    return fonts[index];
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

    final MonospaceFontFamily that = (MonospaceFontFamily) o;

    if (!familyName.equals(that.familyName))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return familyName.hashCode();
  }
}
