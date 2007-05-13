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
 * $Id: TrueTypeFontRegistryTest.java,v 1.4 2006/12/03 18:11:59 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.truetype.junit;

import java.awt.GraphicsEnvironment;
import java.io.IOException;

import junit.framework.TestCase;
import org.jfree.fonts.LibFontBoot;
import org.jfree.fonts.io.FontDataInputSource;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontSource;
import org.jfree.fonts.truetype.FontHeaderTable;
import org.jfree.fonts.truetype.NameTable;
import org.jfree.fonts.truetype.TrueTypeFont;
import org.jfree.fonts.truetype.TrueTypeFontRegistry;

/**
 * Creation-Date: 22.03.2006, 17:41:58
 *
 * @author Thomas Morgner
 */
public class TrueTypeFontRegistryTest extends TestCase
{
  public TrueTypeFontRegistryTest()
  {
    LibFontBoot.getInstance().start();
  }

  public TrueTypeFontRegistryTest(String string)
  {
    super(string);
    LibFontBoot.getInstance().start();
  }

  public void testFontRegistration () throws IOException
  {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] names = ge.getAvailableFontFamilyNames();

    TrueTypeFontRegistry tfr = new TrueTypeFontRegistry();
    tfr.registerDefaultFontPath();
    for (int i = 0; i < names.length; i++)
    {
      final String name = names[i];
      final FontFamily fofam = tfr.getFontFamily(name);
      if (name.equals("AmerType Md BT"))
      {
        FontSource fr = (FontSource) fofam.getFontRecord(false, false);
        FontDataInputSource fs = fr.getFontInputSource();
        TrueTypeFont ttf = new TrueTypeFont(fs);
        NameTable nt = (NameTable) ttf.getTable(NameTable.TABLE_ID);
        //PostscriptInformationTable pst = ttf.getTable(PostscriptInformationTable.TABLE_ID);
        FontHeaderTable fht = (FontHeaderTable) ttf.getTable(FontHeaderTable.TABLE_ID);
        /*
        TrueTypeFontMetricsFactory tfmf = new TrueTypeFontMetricsFactory();
        FontMetrics fm =
                tfmf.createMetrics(fr, new DefaultFontContext(14, false, false));
        */
        System.out.println ("HERE!");

      }
      if (fofam == null)
      {
        System.out.println("Warning: Font not known " + name);
      }
      else
      {
        System.out.println("Font registered " + name);
      }
    }
  }

}
