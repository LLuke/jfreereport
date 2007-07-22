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
 * $Id: ListFonts.java,v 1.6 2007/05/13 12:44:09 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.tools;

import org.jfree.fonts.LibFontBoot;
import org.jfree.fonts.afm.AfmFontRegistry;
import org.jfree.fonts.pfm.PfmFontRegistry;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontSource;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.truetype.TrueTypeFontRegistry;

public class ListFonts
{
  private ListFonts()
  {
  }

  private static void printRecord(final FontRecord record)
  {
    if (record == null)
    {
      System.out.println("  - (there is no font defined for that style and family.)");
      return;
    }
    if (record instanceof FontSource)
    {
      FontSource fs = (FontSource) record;
      System.out.println("  " + record.getFamily().getFamilyName() + " italics:" + record.isItalic() + " oblique:" + record.isOblique() + " bold: " + record.isBold() + " " + fs.getFontFile());
    }
    else
    {
      System.out.println("  " + record.getFamily().getFamilyName() + " italics:" + record.isItalic() + " oblique:" + record.isOblique() + " bold: " + record.isBold());
    }
  }

  public static void main (String[] args)
  {
    LibFontBoot.getInstance().start();

//    final TrueTypeFontRegistry registry = new TrueTypeFontRegistry();
//    registry.initialize();
//    final String[] fontFamilies = registry.getRegisteredFamilies();
//    for (int i = 0; i < fontFamilies.length; i++)
//    {
//      String fontFamily = fontFamilies[i];
//      final FontFamily family = registry.getFontFamily(fontFamily);
//      String[] names = family.getAllNames();
//      printRecord(family.getFontRecord(true, false));
//    }
//

    listFontS(new AfmFontRegistry());
    listFontS(new PfmFontRegistry());
  }

  private static void listFontS(final FontRegistry registry)
  {
    registry.initialize();
    final String[] fontFamilies = registry.getRegisteredFamilies();
    for (int i = 0; i < fontFamilies.length; i++)
    {
      String fontFamily = fontFamilies[i];
      final FontFamily family = registry.getFontFamily(fontFamily);
      String[] names = family.getAllNames();
      printRecord(family.getFontRecord(false, false));
      printRecord(family.getFontRecord(true, false));
      printRecord(family.getFontRecord(false, true));
      printRecord(family.getFontRecord(true, true));
    }
  }
}
