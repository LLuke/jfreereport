/**
 * Date: Feb 1, 2003
 * Time: 9:21:29 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.support.itext;

import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.report.util.StringUtil;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * The BaseFontFactory is used to find and register all TrueTypeFonts for embedding them
 * in the PDF-File.
 */
public class BaseFontFactory extends DefaultFontMapper
{
  /** Fonts stored by name. */
  private Hashtable fontsByName;
  private static BaseFontFactory fontFactory;

  /**
   * Creates a new factory.
   */
  private BaseFontFactory()
  {
    fontsByName = new Hashtable();
  }

  /**
   * Register os-specific font paths to the PDF-FontFactory. For unix-like operating
   * systems, X11 is searched in /usr/X11R6 and the default truetype fontpath is added.
   * For windows the system font path is added (%windir%/fonts)
   */
  public void registerDefaultFontPath()
  {
    String encoding = getDefaultFontEncoding();
    // Correct the encoding for truetype fonts
    if (encoding.equals(BaseFont.IDENTITY_H) || encoding.equals(BaseFont.IDENTITY_V))
    {
      // is this correct?
      encoding = "iso-8859-1";
    }

    String osname = System.getProperty("os.name");
    String jrepath = System.getProperty("java.home");
    String fontPath = null;
    String fs = System.getProperty("file.separator");

    Log.debug("Running on operating system: " + osname);
    Log.debug("Character encoding used as default: " + encoding);

    if (!StringUtil.startsWithIgnoreCase(osname, "windows"))
    {
      Log.debug("Assuming unix like file structures");
      // Assume X11 is installed in the default location.
      fontPath = "/usr/X11R6/lib/X11/fonts/truetype";
    }
    else
    {
      Log.debug("Found windows in os name, assuming DOS/Win32 structures");
      // Assume windows
      // If you are not using windows, ignore this. This just checks if a windows system
      // directory exist and includes a font dir.

      String windirs = System.getProperty("java.library.path");
      if (windirs != null)
      {
        StringTokenizer strtok
            = new StringTokenizer(windirs, System.getProperty("path.separator"));
        while (strtok.hasMoreTokens())
        {
          String token = strtok.nextToken();

          if (token.endsWith("System32"))
          {
            // found windows folder ;-)
            int lastBackslash = token.lastIndexOf(fs);
            fontPath = token.substring(0, lastBackslash) + fs + "Fonts";

            break;
          }
        }
      }
    }

    Log.debug("Fonts located in \"" + fontPath + "\"");
    if (fontPath != null)
    {
      registerFontPath(fontPath, encoding);
    }
    registerFontPath(new File(jrepath, "lib" + fs + "fonts").toString(), encoding);
  }

  /**
   * Register all fonts (*.ttf files) in the given path.
   *
   * @param path  the path.
   * @param encoding  the encoding.
   */
  public void registerFontPath(String path, String encoding)
  {
    File file = new File(path);
    if (file.exists() && file.isDirectory() && file.canRead())
    {
      File[] files = file.listFiles();
      for (int i = 0; i < files.length; i++)
      {
        registerFontFile(files[i].toString(), encoding);
      }
    }
    file = null;
    System.gc();
  }

  /**
   * Register the font (must end this *.ttf) to the FontFactory.
   *
   * @param filename  the filename.
   * @param encoding  the encoding.
   */
  public void registerFontFile(String filename, String encoding)
  {
    if (!filename.toLowerCase().endsWith("ttf"))
    {
      return;
    }
    File file = new File(filename);
    if (file.exists() && file.isFile() && file.canRead())
    {
      try
      {
        addFont(filename, encoding);
      }
      catch (Exception e)
      {
        Log.warn("Font " + filename + " is invalid. Message:", e);
      }
    }
  }

  /**
   * Adds the fontname by creating the basefont object
   *
   * @param font  the font name.
   * @param encoding  the encoding.
   *
   * @throws com.lowagie.text.DocumentException ??
   * @throws java.io.IOException ??
   */
  private void addFont(String font, String encoding)
      throws DocumentException, IOException
  {
    if (fontsByName.containsValue(font))
    {
      return; // already in there
    }
    BaseFont bfont = BaseFont.createFont(font, encoding, true, false, null, null);
    String[][] fi = bfont.getFullFontName();
    for (int i = 0; i < fi.length; i++)
    {
      String[] ffi = fi[i];
      if (fontsByName.containsKey(ffi[3]) == false)
      {
        fontsByName.put(ffi[3], font);
        Log.debug("Registered truetype font " + ffi[3] + "; File=" + font);
      }
    }
  }

  /**
   * Returns all registered fonts as enumeration.
   *
   * @return an enumeration of the registered fonts.
   */
  public Enumeration getRegisteredFonts()
  {
    return fontsByName.keys();
  }

  /**
   * Returns the name of the font file by looking up the name.
   *
   * @param font  the font name
   *
   * @return the font file name.
   */
  public String getFontfileForName(String font)
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
   * of  <code>"com.jrefinery.report.targets.pageable.output.PDFOutputTarget.AutoInit"</code> is
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
