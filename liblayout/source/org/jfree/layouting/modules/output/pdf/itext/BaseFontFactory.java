/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.modules.output.pdf.itext;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeSet;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.truetype.TrueTypeFontRecord;
import org.jfree.fonts.truetype.TrueTypeFontRegistry;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.util.HashNMap;
import org.jfree.util.Log;
import org.jfree.util.StringUtils;

/**
 * The BaseFontFactory is used to find and register all TrueType fonts for
 * embedding them in the PDF file.
 *
 * @author Thomas Morgner
 */
public final class BaseFontFactory extends DefaultFontMapper
{
  /**
   * The name of the report property, which defines, whether the
   * GarbageCollector should be run after the font registration.
   */
  public static final String GC_AFTER_REGISTER =
          "org.jfree.layouting.modules.output.pdf.itext.GCAfterRegister";

  /**
   * The font path filter is used to collect font files and directories during
   * the font path registration.
   */
  private static class FontPathFilter implements FileFilter
  {
    /** Default Constructor. */
    public FontPathFilter()
    {
    }

    /**
     * Tests whether or not the specified abstract pathname should be included
     * in a pathname list.
     *
     * @param pathname The abstract pathname to be tested
     * @return <code>true</code> if and only if <code>pathname</code> should be
     *         included
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
      return StringUtils.endsWithIgnoreCase(name, ".afm") ||
              StringUtils.endsWithIgnoreCase(name, ".pfm");
    }

  }

  /** The singleton instance of the font path filter. */
  private static final FontPathFilter FONTPATHFILTER = new FontPathFilter();

  /**
   * A set of fonts which have licence restrictions and may not be embedded.
   * This map is keyed by the font file name. We prefer unrestricted fonts.
   */
  private final Properties notEmbeddedFonts;

  /** Fonts stored by name. */
  private final Properties fontsByName;

  /** A flag to check whether this factory is initialized. */
  private boolean initialized;
  /** A map of all confirmed (existent or seen) files. */
  private Properties confirmedFiles;

  private TrueTypeFontRegistry registry;

  /** Creates a new factory. */
  public BaseFontFactory()
  {
    fontsByName = new Properties();
    notEmbeddedFonts = new Properties();
    registry = new TrueTypeFontRegistry();
  }

  /**
   * Register os-specific font paths to the PDF-FontFactory. For unix-like
   * operating systems, X11 is searched in /usr/X11R6 and the default truetype
   * fontpath is added. For windows the system font path is added
   * (%windir%/fonts)
   */
  public synchronized void registerDefaultFontPath(String encoding)
  {
    final HashNMap knownFonts = new HashNMap();
    Properties seenFiles = new Properties();
    confirmedFiles = new Properties();
    registry.initialize();

    // Correct the encoding for truetype fonts
    if (encoding.equals(BaseFont.IDENTITY_H) || encoding.equals(
            BaseFont.IDENTITY_V))
    {
      // is this correct?
      //encoding = "iso-8859-1";
      encoding = "UTF-16";
    }

    final String osname = safeSystemGetProperty("os.name",
            "<protected by system security>");
    final String jrepath = safeSystemGetProperty("java.home", ".");
    final String fs = safeSystemGetProperty("file.separator", File.separator);

    Log.debug("Running on operating system: " + osname);
    Log.debug("Character encoding used as default: " + encoding);

    if (safeSystemGetProperty("mrj.version", null) != null)
    {
      final String userhome = safeSystemGetProperty("user.home", ".");
      Log.debug("Detected MacOS (Property 'mrj.version' is present.");
      registerFontPath(new File(userhome + "/Library/Fonts"), encoding,
              knownFonts, seenFiles);
      registerFontPath(new File("/Library/Fonts"), encoding, knownFonts,
              seenFiles);
      registerFontPath(new File("/Network/Library/Fonts"), encoding, knownFonts,
              seenFiles);
      registerFontPath(new File("/System/Library/Fonts"), encoding, knownFonts,
              seenFiles);
    }
    else if (StringUtils.startsWithIgnoreCase(osname, "windows"))
    {
      registerWindowsFontPath(encoding, knownFonts, seenFiles);
    }
    else
    {
      Log.debug("Assuming unix like file structures");
      // Assume X11 is installed in the default location.
      registerFontPath(new File("/usr/X11R6/lib/X11/fonts"), encoding,
              knownFonts, seenFiles);
      registerFontPath(new File("/usr/share/fonts"), encoding, knownFonts,
              seenFiles);
    }
    registerFontPath(new File(jrepath, "lib" + fs + "fonts"), encoding,
            knownFonts, seenFiles);

    Log.info("Completed font registration.");
    initialized = true;
  }

  private String safeSystemGetProperty(final String name,
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

  /**
   * Registers the default windows font path. Once a font was found in the old
   * seenFiles map and confirmed, that this font still exists, it gets copied
   * into the confirmedFiles map.
   *
   * @param encoding   the default font encoding.
   * @param knownFonts a map containing all known fonts
   * @param seenFiles  a map containing all known font files.
   */
  private void registerWindowsFontPath(final String encoding,
                                       final HashNMap knownFonts,
                                       final Properties seenFiles)
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
      registerFontPath(file, encoding, knownFonts, seenFiles);
    }
  }

  /**
   * Register all fonts (*.ttf files) in the given path.
   *
   * @param file     the directory that contains the font files.
   * @param encoding the encoding for the given font.
   */
  public synchronized void registerFontPath
          (final File file, final String encoding)
  {
    this.registerFontPath(file, encoding, new HashNMap(), new Properties());
    this.registry.registerFontPath(file);
  }

  /**
   * Register all fonts (*.ttf files) in the given path.
   *
   * @param file       the directory that contains the font files.
   * @param encoding   the encoding for the given font.
   * @param knownFonts a map containing all known fonts
   * @param seenFiles  a map containing all known font files.
   */
  private synchronized void registerFontPath
          (final File file, final String encoding,
           final HashNMap knownFonts, final Properties seenFiles)
  {
    if (file.exists() && file.isDirectory() && file.canRead())
    {
      final File[] files = file.listFiles(FONTPATHFILTER);
      for (int i = 0; i < files.length; i++)
      {
        final File currentFile = files[i];
        if (currentFile.isDirectory())
        {
          registerFontPath(currentFile, encoding, knownFonts, seenFiles);
        }
        else
        {
          final String fileName = currentFile.toString();
          final String cachedAccessTime = seenFiles.getProperty(fileName);
          final String newAccessTime =
                  String.valueOf(currentFile.lastModified() + "," + currentFile
                          .length());

          // the font file is not known ... or has changed.
          // or is not contained in the list of embedable fonts
          // then register the font
          if (newAccessTime.equals(cachedAccessTime) == false ||
                  notEmbeddedFonts.containsKey(fileName) == false)
          {
            registerFontFile(fileName, encoding);
          }
          else
          {
            final Iterator it = knownFonts.getAll(fileName);
            while (it.hasNext())
            {
              final String fontName = (String) it.next();
              fontsByName.put(fontName, fileName);
            }
            confirmedFiles.put(fileName, newAccessTime);
          }
        }
      }
    }
    if (LibLayoutBoot.getInstance().getGlobalConfig().getConfigProperty
            (GC_AFTER_REGISTER, "true").equals("true"))
    {
      // clean up after the registering ...
      System.gc();
    }
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
    if (filename.toLowerCase().endsWith(".ttf") ||
            filename.toLowerCase().endsWith(".ttc") ||
            filename.toLowerCase().endsWith(".otf"))
    {
      try
      {
        this.registry.registerFontFile(new File(filename));
      }
      catch (IOException e)
      {
        // ignore ..
      }
      return;
    }

    if (filename.toLowerCase().endsWith(".afm") ||
            filename.toLowerCase().endsWith(".pfm"))
    {
      final File file = new File(filename);
      if (file.exists() && file.isFile() && file.canRead())
      {
        final String newAccessTime =
                String.valueOf(file.lastModified() + "," + file.length());
        confirmedFiles.put(filename, newAccessTime);
        try
        {
          addFont(filename, encoding);
        }
        catch (Exception e)
        {
          Log.warn(new Log.SimpleMessage("Font ", filename,
                  " is invalid. Message:", e.getMessage()));
          notEmbeddedFonts.setProperty(filename, "false");
        }
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
   * @throws DocumentException if the base font could not be created
   * @throws IOException       if the base font file could not be read.
   */
  private void addFont(final String font, final String encoding)
          throws DocumentException, IOException
  {
    if (fontsByName.containsValue(font))
    {
      return; // already in there
    }

    final String filePfbName = font.substring(0, font.length() - 3) + "pfb";
    final File filePfb = new File(filePfbName);
    boolean embedded = true;
    if (filePfb.exists() == false || filePfb.isFile() == false || filePfb
            .canRead() == false)
    {
      Log.warn("Cannot embedd font: " + filePfb + " is missing for " + font);
      embedded = false;
    }
    BaseFont bfont;
    try
    {
      bfont = BaseFont.createFont(font, encoding, embedded, false, null, null);
    }
    catch (DocumentException de)
    {
      if (embedded == false)
      {
        throw de;
      }
      // failed to load the font as embedded font, try not-embedded.
      bfont = BaseFont.createFont(font, encoding, false, false, null, null);
      embedded = false;
      Log.info(new Log.SimpleMessage
              ("Font ", font, "  cannot be used as embedded font " +
                      "due to licensing restrictions (PFB)."));
    }

    String embeddedText = String.valueOf(embedded);
    final String[][] fi = bfont.getFullFontName();
    for (int i = 0; i < fi.length; i++)
    {
      final String[] ffi = fi[i];
      final String knownFontEmbeddedState = notEmbeddedFonts.getProperty(font,
              "false");

      // if unknown or the known font is a restricted version and this one is none,
      // then register the new font
      final String logicalFontname = ffi[3];
      notEmbeddedFonts.setProperty(font, embeddedText);
      if ((fontsByName.containsKey(logicalFontname) == false) ||
              ((knownFontEmbeddedState.equals("true") == false) &&
                      embeddedText.equals(knownFontEmbeddedState) == false))
      {
        fontsByName.setProperty(logicalFontname, font);
        Log.debug(new Log.SimpleMessage
                ("Registered font (primary name):", logicalFontname,
                        "; embedded=",
                        embeddedText, new Log.SimpleMessage(", file=", font)));
      }
    }

    final String[][] afi = bfont.getFamilyFontName();
    for (int i = 0; i < afi.length; i++)
    {
      String[] strings = afi[i];
      Log.debug(new Log.SimpleMessage
              ("Registered font (alternate name):", strings[3], "; embedded=",
                      embeddedText, new Log.SimpleMessage(", file=", font)));
      // also register the alternate names, if there are any ..
      fontsByName.setProperty(strings[3], font);
    }
  }

  /**
   * Returns all registered fonts as enumeration.
   *
   * @return an enumeration of the registered fonts.
   */
  public Iterator getRegisteredFonts()
  {
    final TreeSet treeSet = new TreeSet();
    treeSet.addAll(fontsByName.keySet());
    treeSet.addAll(Arrays.asList(registry.getRegisteredFamilies()));
    return treeSet.iterator();
  }

  /**
   * Returns the name of the font file by looking up the name.
   *
   * @param font the font name
   * @return the font file name.
   * @deprecated this method does not support font styles.
   */
  public String getFontfileForName(final String font)
  {
    final FontRecord record = getFontForName(font, false, false);
    return record.getFontFile();
  }

  public FontRecord getFontForName(final String font,
                                   final boolean bold,
                                   final boolean italics)
  {
    String retval = fontsByName.getProperty(font);
    if (retval != null)
    {
      boolean embedded = notEmbeddedFonts.getProperty
                      (retval, "false").equalsIgnoreCase("true");
      return new MinimalFontRecord(font, retval, bold, italics, embedded);
    }
    FontFamily family = registry.getFontFamily(font);
    if (family == null)
    {
      return null;
    }

    final FontRecord record = family.getFontRecord(bold, italics);
    if (record instanceof TrueTypeFontRecord)
    {
      // yes, thats a bit hacky, but we have to deal with iTexts enforced
      // OS2table requirement here.
      TrueTypeFontRecord ttfRecord = (TrueTypeFontRecord) record;
      if (ttfRecord.isNonWindows())
      {
        // itext is not able to handle fonts which dont have an OS/2 table.
        return null;
      }
    }
    return record;
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
}