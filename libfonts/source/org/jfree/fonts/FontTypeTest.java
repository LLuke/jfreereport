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
      System.out.println("  " + record.getName() + " " + record.getFontFile());
    }
  }

  public static void main(String[] args) throws IOException
  {
    String file = ("/usr/X11R6/lib/X11/fonts/truetype/GARAIT.ttf");
    TrueTypeFont font = new TrueTypeFont(new File(file));
    NameTable table = (NameTable) font.getTable(NameTable.TABLE_ID);
    System.out.println("Name: " + table.getPrimaryName(NameTable.NAME_FAMILY));

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
  }
}
