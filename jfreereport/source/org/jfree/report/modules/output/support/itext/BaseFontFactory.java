/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * --------------------
 * BaseFontFactory.java
 * --------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BaseFontFactory.java,v 1.17 2003/06/29 16:59:29 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 * 01-Feb-2003 : Refactoring moved this class from package
 *               com.jefinery.report.targets.pageable.output
 *
 */
package org.jfree.report.modules.output.support.itext;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.StringUtil;

/**
 * The BaseFontFactory is used to find and register all TrueType fonts for embedding them
 * in the PDF file.
 *
 * @author Thomas Morgner
 */
public class BaseFontFactory extends DefaultFontMapper
{
  /** Singleton instance of the BaseFontFactory. */
  private static BaseFontFactory fontFactory;

  /** Fonts stored by name. */
  private HashMap fontsByName;

  /**
   * The font path filter is used to collect font files and directories
   * during the font path registration.
   */
  private static class FontPathFilter implements FileFilter
  {
    /**
     * Default Constructor.
     */
    public FontPathFilter()
    {
    }

    /**
     * Tests whether or not the specified abstract pathname should be
     * included in a pathname list.
     *
     * @param  pathname  The abstract pathname to be tested
     * @return  <code>true</code> if and only if <code>pathname</code>
     *          should be included
     */
    public boolean accept(final File pathname)
    {
      if (pathname.canRead() == false)
      {
        return false;
      }
      if (pathname.isDirectory())
      {
        return true;
      }
      final String name = pathname.getName();
      if (StringUtil.endsWithIgnoreCase(name, ".afm"))
      {
        return true;
      }
      if (StringUtil.endsWithIgnoreCase(name, ".pfb"))
      {
        return true;
      }
      if (StringUtil.endsWithIgnoreCase(name, ".ttf"))
      {
        return true;
      }
      if (StringUtil.endsWithIgnoreCase(name, ".ttc"))
      {
        return true;
      }
      if (StringUtil.endsWithIgnoreCase(name, ".otf"))
      {
        return true;
      }
      return false;
    }

  }

  /** The singleton instance of the font path filter. */
  private static final FontPathFilter FONTPATHFILTER = new FontPathFilter();

  /**
   * Creates a new factory.
   */
  private BaseFontFactory()
  {
    fontsByName = new HashMap();
  }

  /**
   * Register os-specific font paths to the PDF-FontFactory. For unix-like operating
   * systems, X11 is searched in /usr/X11R6 and the default truetype fontpath is added.
   * For windows the system font path is added (%windir%/fonts)
   */
  public synchronized void registerDefaultFontPath()
  {
    String encoding = getDefaultFontEncoding();
    // Correct the encoding for truetype fonts
    if (encoding.equals(BaseFont.IDENTITY_H) || encoding.equals(BaseFont.IDENTITY_V))
    {
      // is this correct?
      //encoding = "iso-8859-1";
      encoding = "UTF-16";
    }

    final String osname = System.getProperty("os.name");
    final String jrepath = System.getProperty("java.home");
    final String fs = System.getProperty("file.separator");

    Log.debug("Running on operating system: " + osname);
    Log.debug("Character encoding used as default: " + encoding);

    if (System.getProperty("mrj.version") != null)
    {
      final String userhome = System.getProperty("user.home");
      Log.debug("Detected MacOS (Property 'mrj.version' is present.");
      registerFontPath(new File(userhome + "/Library/Fonts"), encoding);
      registerFontPath(new File("/Library/Fonts"), encoding);
      registerFontPath(new File("/Network/Library/Fonts"), encoding);
      registerFontPath(new File("/System/Library/Fonts"), encoding);
    }
    else if (StringUtil.startsWithIgnoreCase(osname, "windows"))
    {
      registerWindowsFontPath(encoding);
    }
    else
    {
      Log.debug("Assuming unix like file structures");
      // Assume X11 is installed in the default location.
      registerFontPath(new File("/usr/X11R6/lib/X11/fonts"), encoding);
      registerFontPath(new File("/usr/share/fonts"), encoding);
    }
    registerFontPath(new File(jrepath, "lib" + fs + "fonts"), encoding);
  }

  /**
   * Registers the default windows font path.
   *
   * @param encoding the default font encoding.
   */
  private void registerWindowsFontPath(final String encoding)
  {
    Log.debug("Found windows in os name, assuming DOS/Win32 structures");
    // Assume windows
    // If you are not using windows, ignore this. This just checks if a windows system
    // directory exist and includes a font dir.

    String fontPath = null;
    final String windirs = System.getProperty("java.library.path");
    final String fs = System.getProperty("file.separator");

    if (windirs != null)
    {
      final StringTokenizer strtok
          = new StringTokenizer(windirs, System.getProperty("path.separator"));
      while (strtok.hasMoreTokens())
      {
        final String token = strtok.nextToken();

        if (token.endsWith("System32"))
        {
          // found windows folder ;-)
          final int lastBackslash = token.lastIndexOf(fs);
          fontPath = token.substring(0, lastBackslash) + fs + "Fonts";

          break;
        }
      }
    }
    Log.debug("Fonts located in \"" + fontPath + "\"");
    if (fontPath != null)
    {
      final File file = new File(fontPath);
      registerFontPath(file, encoding);
    }
  }

  /**
   * Register all fonts (*.ttf files) in the given path.
   *
   * @param file  the directory that contains the font files.
   * @param encoding  the encoding.
   */
  public synchronized void registerFontPath(final File file, final String encoding)
  {
    if (file.exists() && file.isDirectory() && file.canRead())
    {
      final File[] files = file.listFiles(FONTPATHFILTER);
      for (int i = 0; i < files.length; i++)
      {
        if (files[i].isDirectory())
        {
          registerFontPath(files[i], encoding);
        }
        else
        {
          registerFontFile(files[i].toString(), encoding);
        }
      }
    }
    System.gc();
  }

  /**
   * Register the font (must end this *.ttf) to the FontFactory.
   *
   * @param filename  the filename.
   * @param encoding  the encoding.
   */
  public synchronized void registerFontFile(final String filename, final String encoding)
  {
    if (!filename.toLowerCase().endsWith(".ttf") &&
        !filename.toLowerCase().endsWith(".afm") &&
        !filename.toLowerCase().endsWith(".pfb"))
    {
      return;
    }
    final File file = new File(filename);
    if (file.exists() && file.isFile() && file.canRead())
    {
      try
      {
        addFont(filename, encoding);
      }
      catch (Exception e)
      {
        Log.warn(new Log.SimpleMessage("Font ", filename, " is invalid. Message:", e.getMessage()));
      }
    }
  }

  /**
   * Adds the fontname by creating the basefont object.
   *
   * @param font  the font name.
   * @param encoding  the encoding.
   *
   * @throws DocumentException if the base font could not be created
   * @throws IOException if the base font file could not be read.
   */
  private void addFont(final String font, final String encoding)
      throws DocumentException, IOException
  {
    if (fontsByName.containsValue(font))
    {
      return; // already in there
    }
    final BaseFont bfont = BaseFont.createFont(font, encoding, true, false, null, null);
    final String[][] fi = bfont.getFullFontName();
    for (int i = 0; i < fi.length; i++)
    {
      final String[] ffi = fi[i];
      if (fontsByName.containsKey(ffi[3]) == false)
      {
        fontsByName.put(ffi[3], font);
        Log.debug(new Log.SimpleMessage("Registered truetype font ", ffi[3], "; File=", font));
      }
    }
  }

  /**
   * Returns all registered fonts as enumeration.
   *
   * @return an enumeration of the registered fonts.
   */
  public Iterator getRegisteredFonts()
  {
    return fontsByName.keySet().iterator();
  }

  /**
   * Returns the name of the font file by looking up the name.
   *
   * @param font  the font name
   *
   * @return the font file name.
   */
  public String getFontfileForName(final String font)
  {
    return (String) fontsByName.get(font);
  }

  /**
   * Returns the default font encoding.
   *
   * @return the default font encoding.
   */
  public static final String getDefaultFontEncoding()
  {
    return ReportConfiguration.getGlobalConfig().getPdfTargetEncoding();
  }

  /**
   * Initialialize the font factory when this class is loaded and the system property
   * of  <code>"org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget.AutoInit"</code> is
   * set to <code>true</code>.
   */
  static
  {
    if (ReportConfiguration.getGlobalConfig().isPDFTargetAutoInit())
    {
      getFontFactory().registerDefaultFontPath();
    }
  }

  /**
   * Returns/creates the singleton font factory.
   *
   * @return the font factory.
   */
  public static BaseFontFactory getFontFactory()
  {
    if (fontFactory == null)
    {
      fontFactory = new BaseFontFactory();
    }
    return fontFactory;
  }
}
