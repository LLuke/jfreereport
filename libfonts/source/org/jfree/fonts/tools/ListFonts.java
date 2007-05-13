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
 * $Id: ListFonts.java,v 1.5 2006/12/03 18:11:59 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.tools;

import org.jfree.fonts.truetype.TrueTypeFontRegistry;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontSource;
import org.jfree.fonts.LibFontBoot;

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
    System.out.println("  " + record.getFamily().getFamilyName() + " italics:" + record.isItalic() + " oblique:" + record.isOblique() + " bold: " + record.isBold());
    if (record instanceof FontSource)
    {
      final FontSource source = (FontSource) record;
      final String[] allNames = source.getAllNames();
      for (int i = 0; i < allNames.length; i++)
      {
        final String name = allNames[i];
        System.out.println("  Alias: " + i + " Name:" + name);
      }

      final String[] allVariants = source.getAllVariants();
      for (int i = 0; i < allVariants.length; i++)
      {
        final String name = allVariants[i];
        System.out.println("  Variant: " + i + " Name:" + name);
      }
    }
  }

  public static void main (String[] args)
  {
    LibFontBoot.getInstance().start();

    final TrueTypeFontRegistry registry = new TrueTypeFontRegistry();
    registry.initialize();
    final String[] fontFamilies = registry.getRegisteredFamilies();
    for (int i = 0; i < fontFamilies.length; i++)
    {
      String fontFamily = fontFamilies[i];
      final FontFamily family = registry.getFontFamily(fontFamily);
      String[] names = family.getAllNames();
//      for (int j = 0; j < names.length; j++)
//      {
//        String name = names[j];
//        System.out.println("  Alias: " + j + " Name:" + name);
//      }

//      printRecord(family.getFontRecord(false, false));
      printRecord(family.getFontRecord(true, false));
//      printRecord(family.getFontRecord(false, true));
//      printRecord(family.getFontRecord(true, true));

    }
  }
}
