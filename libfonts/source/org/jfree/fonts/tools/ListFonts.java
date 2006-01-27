package org.jfree.fonts.tools;

import org.jfree.fonts.truetype.TrueTypeFontRegistry;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;

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
    System.out.println("  " + record.getName() + " italics:" + record.isItalic() + " oblique:" + record.isOblique());
    String[] allNames = record.getAllNames();
    for (int i = 0; i < allNames.length; i++)
    {
      String name = allNames[i];
      System.out.println("  Alias: " + i + " Name:" + name);
    }

    String[] allVariants = record.getAllVariants();
    for (int i = 0; i < allVariants.length; i++)
    {
      String name = allVariants[i];
      System.out.println("  Variant: " + i + " Name:" + name);
    }
  }

  public static void main (String[] args)
  {
    final TrueTypeFontRegistry registry = new TrueTypeFontRegistry();
    registry.initialize();
    final String[] fontFamilies = registry.getRegisteredFamilies();
    for (int i = 0; i < fontFamilies.length; i++)
    {
      String fontFamily = fontFamilies[i];
      final FontFamily family = registry.getFontFamily(fontFamily);
      String[] names = family.getAllNames();
      for (int j = 0; j < names.length; j++)
      {
        String name = names[j];
        System.out.println("  Alias: " + j + " Name:" + name);
      }
/*
      printRecord(family.getFontRecord(false, false));
      printRecord(family.getFontRecord(true, false));
      printRecord(family.getFontRecord(false, true));
      printRecord(family.getFontRecord(true, true));
*/
    }
  }
}
