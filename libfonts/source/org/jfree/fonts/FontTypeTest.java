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
 * $Id: FontTypeTest.java,v 1.3 2005/12/07 22:57:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-11-07 : Initial version
 */
package org.jfree.fonts;

import java.awt.Font;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.truetype.TrueTypeFontRegistry;

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
    else if (record.isItalic() || record.isOblique())
    {
      System.out.println("  " + record.getName() + " it:" + record.isItalic() + " ob:" + record.isOblique());
    }
  }

  public static void main(String[] args) throws IOException
  {
//    String file = ("/usr/X11R6/lib/X11/fonts/truetype/arial.ttf");
//    TrueTypeFont font = new TrueTypeFont(new File(file));
//    NameTable table = (NameTable) font.getTable(NameTable.TABLE_ID);
//    System.out.println("Name: " + table.getPrimaryName(NameTable.NAME_FAMILY));
//    OS2Table otable = (OS2Table) font.getTable(OS2Table.TABLE_ID);
//
//    TrueTypeFontMetrics tfm = new TrueTypeFontMetrics(font, 120);
//
//    Font awtFont = new Font ("Arial", Font.PLAIN, 120);
//    AttributedCharacterIterator.Attribute[] att = awtFont.getAvailableAttributes();
//    FontRenderContext frc = new FontRenderContext(null, false, true);
//    BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
//    FontMetrics fm = img.createGraphics().getFontMetrics(awtFont);
//    int ascent = fm.getAscent();
//    int descent = fm.getDescent();
//    int leading = fm.getLeading();
//
//    System.out.println("Name: " + table.getPrimaryName(NameTable.NAME_FAMILY));
//
//
//    byte[] data = { -2, -100 };
//    final int tx = ByteAccessUtilities.readShort(data, 0);
//    if (tx == 0) throw new IllegalStateException();
//
//
    final TrueTypeFontRegistry registry = new TrueTypeFontRegistry();
    registry.initialize();
//    AWTFontRegistry registry = new AWTFontRegistry();
//    registry.registerDefaultFontPath();
//    registry.registerFontPath(new File ("/home/user/fonts"));
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

    System.out.println (registry.getFontFamily("Skolle"));
    System.out.println (registry.getFontFamily("Tahoma2"));
  }
}
