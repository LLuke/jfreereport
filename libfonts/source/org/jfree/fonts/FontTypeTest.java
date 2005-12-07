/**
 * ========================================
 * libLayout : a free Java font reading library
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
 * FontTypeTest.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: FontTypeTest.java,v 1.2 2005/11/09 21:24:12 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-11-07 : Initial version
 */
package org.jfree.fonts;

import java.io.File;
import java.io.IOException;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.truetype.NameTable;
import org.jfree.fonts.truetype.TrueTypeFont;

/**
 * Creation-Date: 06.11.2005, 20:29:08
 *
 * @author Thomas Morgner
 */
public class FontTypeTest
{
  private FontTypeTest()
  {
  }

  private static void printRecord(final FontRecord record)
  {
    if (record == null)
    {
      System.out.println("  -");
    }
    else
    {
      System.out.println("  " + record.getName() + " " + record.getFontFile() + " " + record.isOblique());
    }
  }

  public static void main(String[] args) throws IOException
  {
//    String file = ("/usr/X11R6/lib/X11/fonts/truetype/GARAIT.ttf");
//    TrueTypeFont font = new TrueTypeFont(new File(file));
//    NameTable table = (NameTable) font.getTable(NameTable.TABLE_ID);
//    System.out.println("Name: " + table.getPrimaryName(NameTable.NAME_FAMILY));

    final FontRegistry registry = new FontRegistry();
    registry.registerDefaultFontPath();
    registry.registerFontPath(new File ("/home/user/fonts"));
    final String[] fontFamilies = registry.getRegisteredFamilies();
    for (int i = 0; i < fontFamilies.length; i++)
    {
      String fontFamily = fontFamilies[i];
      System.out.println("FontFamily: " + fontFamily);
      final FontFamily family = registry.getFontFamily(fontFamily);
      printRecord(family.getFontRecord(false, false));
      printRecord(family.getFontRecord(true, false));
      printRecord(family.getFontRecord(false, true));
      printRecord(family.getFontRecord(true, true));
    }

    final String[] allFontFamilies = registry.getAllRegisteredFamilies();
    for (int i = 0; i < allFontFamilies.length; i++)
    {
      String family = allFontFamilies[i];
      System.out.println("I18n: FontFamily: " + family);
    }
  }
}
