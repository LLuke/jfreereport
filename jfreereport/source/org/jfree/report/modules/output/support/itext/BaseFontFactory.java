/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: BaseFontFactory.java,v 1.7 2003/08/24 15:06:42 taqua Exp $
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
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import org.jfree.report.modules.misc.configstore.base.ConfigFactory;
import org.jfree.report.modules.misc.configstore.base.ConfigStorage;
import org.jfree.report.modules.misc.configstore.base.ConfigStoreException;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.StringUtil;

/**
 * The BaseFontFactory is used to find and register all TrueType fonts for embedding them
 * in the PDF file.
 *
 * @author Thomas Morgner
 */
public final class BaseFontFactory extends DefaultFontMapper
{
  /** The 'PDF auto init' property key. */
  public static final String ITEXT_FONT_AUTOINIT
      = "org.jfree.report.modules.output.support.itext.AutoInit";

  /** The default 'PDF auto init' property value. */
  public static final String ITEXT_FONT_AUTOINIT_ONINIT = "onInit";

  /** The default 'PDF auto init' property value. */
  public static final String ITEXT_FONT_AUTOINIT_LAZY = "lazy";

  /** The default 'PDF auto init' property value. */
  public static final String ITEXT_FONT_AUTOINIT_NEVER = "never";

  /** The default 'PDF auto init' property value. */
  public static final String ITEXT_FONT_AUTOINIT_DEFAULT = ITEXT_FONT_AUTOINIT_LAZY;

  /**
   * The iText font encoding specifies how to encode the text of iText documents
   * for the given font.
   */
  public static final String ITEXT_FONT_ENCODING
      = "org.jfree.report.modules.output.support.itext.Encoding";

  /** The default 'PDF encoding' property value. */
  public static final String ITEXT_FONT_ENCODING_DEFAULT
      = ReportConfiguration.getPlatformDefaultEncoding();

  /** Singleton instance of the BaseFontFactory. */
  private static BaseFontFactory fontFactory;

  /** Fonts stored by name. */
  private final Properties fontsByName;

  /** A flag to check whether this factory is initialized. */
  private boolean initialized;

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
   * The storage path for the config storage provider to cache the registered
   * font names.
   */
  private static final String FONTS_STORAGE_PATH = "registered_itext_fonts";

  /**
   * Creates a new factory.
   */
  private BaseFontFactory()
  {
    fontsByName = new Properties();
  }

  /**
   * Register os-specific font paths to the PDF-FontFactory. For unix-like operating
   * systems, X11 is searched in /usr/X11R6 and the default truetype fontpath is added.
   * For windows the system font path is added (%windir%/fonts)
   */
  public synchronized void registerDefaultFontPath()
  {
    final ConfigStorage store = ConfigFactory.getInstance().getSystemStorage();
    if (store.existsProperties(FONTS_STORAGE_PATH))
    {
      try
      {
        fontsByName.putAll(store.loadProperties(FONTS_STORAGE_PATH, null));
        return;
      }
      catch (ConfigStoreException cse)
      {
        Log.info("Unable to load font configuration, rebuilding.");
      }
    }

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

    try
    {
      store.storeProperties(FONTS_STORAGE_PATH, fontsByName);
    }
    catch (ConfigStoreException cse)
    {
      Log.info("Failed to store font configuration. This error is non-fatal, " +
          "the font configuration will be rebuild from scratch, if necessary.");
    }
    initialized = true;
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
        fontsByName.setProperty(ffi[3], font);
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
    if (isInitialized() == false)
    {
      if (getPDFTargetAutoInit().equals(ITEXT_FONT_AUTOINIT_LAZY))
      {
        registerDefaultFontPath();
      }
    }
    return (String) fontsByName.get(font);
  }

  /**
   * Checks, whether the factory is initialized.
   *
   * @return true, if the factory is initalized, false otherwise.
   */
  public boolean isInitialized()
  {
    return initialized;
  }

  /**
   * Returns the BaseFont encoding property value.
   *
   * @return the BaseFont encoding property value.
   */
  public static final String getDefaultFontEncoding()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
        (ITEXT_FONT_ENCODING, ITEXT_FONT_ENCODING_DEFAULT);
  }

  /**
   * Sets the BaseFont encoding property value.
   *
   * @param encoding the new encoding.
   */
  public static final void setDefaultFontEncoding(final String encoding)
  {
    ReportConfiguration.getGlobalConfig().setConfigProperty
        (ITEXT_FONT_ENCODING, encoding);
  }


  /**
   * Returns whether to search for ttf-fonts when the PDFOutputTarget is loaded.
   *
   * @return the PDFOutputTarget autoinitialisation value.
   */
  public String getPDFTargetAutoInit()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
        (ITEXT_FONT_AUTOINIT, ITEXT_FONT_AUTOINIT_DEFAULT);
  }

  /**
   * Sets the PDF target auto init status.
   *
   * @param autoInit  the new status.
   */
  public void setPDFTargetAutoInit(final String autoInit)
  {
    if (autoInit != null)
    {
      if ((autoInit.equals(ITEXT_FONT_AUTOINIT_LAZY) == false) &&
          (autoInit.equals(ITEXT_FONT_AUTOINIT_NEVER) == false) &&
          (autoInit.equals(ITEXT_FONT_AUTOINIT_ONINIT) == false))
      {
        throw new IllegalArgumentException("Invalid autoinit value.");
      }
    }
    ReportConfiguration.getGlobalConfig().setConfigProperty
        (ITEXT_FONT_AUTOINIT, String.valueOf(autoInit));
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
