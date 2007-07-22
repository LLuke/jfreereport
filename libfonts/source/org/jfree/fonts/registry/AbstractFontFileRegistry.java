package org.jfree.fonts.registry;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.StringTokenizer;

import org.jfree.util.Log;
import org.jfree.util.StringUtils;
import org.jfree.fonts.LibFontBoot;

/**
 * Creation-Date: 21.07.2007, 17:01:15
 *
 * @author Thomas Morgner
 */
public abstract class AbstractFontFileRegistry  implements FontRegistry
{
  public AbstractFontFileRegistry()
  {
  }

  protected abstract FileFilter getFileFilter();

  public void initialize()
  {
    registerDefaultFontPath();
  }

  protected String getDefaultEncoding()
  {
    return LibFontBoot.getInstance().getGlobalConfig().getConfigProperty
        ("org.jfree.fonts.itext.FontEncoding");
  }

  /**
   * Register os-specific font paths to the PDF-FontFactory. For unix-like
   * operating systems, X11 is searched in /usr/X11R6 and the default truetype
   * fontpath is added. For windows the system font path is added
   * (%windir%/fonts)
   */
  public synchronized void registerDefaultFontPath()
  {
    final String encoding = getDefaultEncoding();

    final String osname = safeSystemGetProperty("os.name", "<protected by system security>");
    final String jrepath = safeSystemGetProperty("java.home", ".");
    final String fs = safeSystemGetProperty("file.separator", File.separator);

    Log.debug("Running on operating system: " + osname);
    Log.debug("Character encoding used as default: " + encoding);

    if (safeSystemGetProperty("mrj.version", null) != null)
    {
      final String userhome = safeSystemGetProperty("user.home", ".");
      Log.debug("Detected MacOS (Property 'mrj.version' is present.");
      registerFontPath(new File(userhome + "/Library/Fonts"), encoding);
      registerFontPath(new File("/Library/Fonts"), encoding);
      registerFontPath(new File("/Network/Library/Fonts"), encoding);
      registerFontPath(new File("/System/Library/Fonts"), encoding);
    }
    else if (StringUtils.startsWithIgnoreCase(osname, "windows"))
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

    Log.info("Completed font registration.");
  }


  /**
   * Registers the default windows font path. Once a font was found in the old
   * seenFiles map and confirmed, that this font still exists, it gets copied
   * into the confirmedFiles map.
   *
   * @param encoding   the default font encoding.
   * @param knownFonts a map containing all known fonts
   * @param seenFiles  a map containing all known font files.
   */
  private void registerWindowsFontPath(final String encoding)
  {
    Log.debug("Found windows in os name, assuming DOS/Win32 structures");
    // Assume windows
    // If you are not using windows, ignore this. This just checks if a windows system
    // directory exist and includes a font dir.

    String fontPath = null;
    final String windirs = safeSystemGetProperty("java.library.path", null);
    final String fs = safeSystemGetProperty("file.separator", File.separator);

    if (windirs != null)
    {
      final StringTokenizer strtok
              = new StringTokenizer(windirs, safeSystemGetProperty(
              "path.separator", File.pathSeparator));
      while (strtok.hasMoreTokens())
      {
        final String token = strtok.nextToken();

        if (StringUtils.endsWithIgnoreCase(token, "System32"))
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
   * @param file       the directory that contains the font files.
   * @param encoding   the encoding for the given font.
   */
  public synchronized void registerFontPath (final File file, final String encoding)
  {
    if (file.exists() && file.isDirectory() && file.canRead())
    {
      final File[] files = file.listFiles(getFileFilter());
      for (int i = 0; i < files.length; i++)
      {
        final File currentFile = files[i];
        if (currentFile.isDirectory())
        {
          registerFontPath(currentFile, encoding);
        }
        else
        {
          if (isCached(currentFile) == false)
          {
            registerFontFile(currentFile, encoding);
          }
        }
      }
    }
  }

  protected boolean isCached (final File file)
  {
    return false;
  }

  /**
   * Register the font (must end this *.ttf) to the FontFactory.
   *
   * @param filename the filename.
   * @param encoding the encoding.
   */
  public synchronized void registerFontFile(final String filename,
                                            final String encoding)
  {
    final File file = new File(filename);
    registerFontFile(file, encoding);
  }

  public void registerFontFile(final File file, final String encoding)
  {
    if (getFileFilter().accept(file) && file.exists() && file.isFile() && file.canRead())
    {
      try
      {
        addFont(file, encoding);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        Log.warn(new Log.SimpleMessage("Font ", file, " is invalid. Message:", e.getMessage()));
      }
    }
  }



  /**
   * Adds the fontname by creating the basefont object. This method tries to
   * load the fonts as embeddable fonts, if this fails, it repeats the loading
   * with the embedded-flag set to false.
   *
   * @param font     the font file name.
   * @param encoding the encoding.
   * @throws java.io.IOException       if the base font file could not be read.
   */
  protected abstract void addFont(final File font, final String encoding)
          throws IOException;


  protected String safeSystemGetProperty(final String name,
                                         final String defaultValue)
  {
    try
    {
      return System.getProperty(name, defaultValue);
    }
    catch (SecurityException se)
    {
      return defaultValue;
    }
  }

}

