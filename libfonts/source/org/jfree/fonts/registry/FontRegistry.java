package org.jfree.fonts.registry;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.jfree.fonts.StringUtilities;
import org.jfree.fonts.truetype.NameTable;
import org.jfree.fonts.truetype.TrueTypeFont;
import org.jfree.fonts.truetype.TrueTypeCollection;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.LogContext;

/**
 * Creation-Date: 07.11.2005, 19:05:46
 *
 * @author Thomas Morgner
 */
public class FontRegistry implements Serializable
{
  private static class FontFileRecord implements Serializable
  {
    private long lastAccessTime;
    private long fileSize;
    private String filename;

    public FontFileRecord(final File file) throws IOException
    {
      this(file.getCanonicalPath(), file.length(), file.lastModified());
    }

    public FontFileRecord(final String filename,
                          final long fileSize,
                          final long lastAccessTime)
    {
      if (filename == null)
      {
        throw new NullPointerException();
      }
      this.filename = filename;
      this.fileSize = fileSize;
      this.lastAccessTime = lastAccessTime;
    }

    public long getLastAccessTime()
    {
      return lastAccessTime;
    }

    public long getFileSize()
    {
      return fileSize;
    }

    public String getFilename()
    {
      return filename;
    }

    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final FontFileRecord that = (FontFileRecord) o;

      if (fileSize != that.fileSize)
      {
        return false;
      }
      if (lastAccessTime != that.lastAccessTime)
      {
        return false;
      }
      if (!filename.equals(that.filename))
      {
        return false;
      }

      return true;
    }

    public int hashCode()
    {
      int result;
      result = (int) (lastAccessTime ^ (lastAccessTime >>> 32));
      result = 29 * result + (int) (fileSize ^ (fileSize >>> 32));
      result = 29 * result + filename.hashCode();
      return result;
    }
  }

  /**
   * The font path filter is used to collect font files and directories during
   * the font path registration.
   */
  private static class FontPathFilter implements FileFilter, Serializable
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
      if (StringUtilities.endsWithIgnoreCase(name, ".ttf"))
      {
        return true;
      }
      if (StringUtilities.endsWithIgnoreCase(name, ".ttc"))
      {
        return true;
      }
      if (StringUtilities.endsWithIgnoreCase(name, ".otf"))
      {
        return true;
      }
      return false;
    }

  }

  /** The singleton instance of the font path filter. */
  private static final FontPathFilter FONTPATHFILTER = new FontPathFilter();
  private static final LogContext logger = Log.createContext(FontRegistry.class);

  private HashMap seenFiles;
  private TreeMap fontFamilies;
  private TreeMap alternateFamilyNames;

  public FontRegistry()
  {
    this.seenFiles = new HashMap();
    this.fontFamilies = new TreeMap();
    this.alternateFamilyNames = new TreeMap();
  }


  /**
   * Register os-specific font paths to the PDF-FontFactory. For unix-like
   * operating systems, X11 is searched in /usr/X11R6 and the default truetype
   * fontpath is added. For windows the system font path is added
   * (%windir%/fonts)
   */
  public synchronized void registerDefaultFontPath()
  {
    final String osname = safeSystemGetProperty("os.name",
            "<protected by system security>");
    final String jrepath = safeSystemGetProperty("java.home", ".");
    final String fs = safeSystemGetProperty("file.separator", File.separator);

    logger.debug("Running on operating system: " + osname);

    if (safeSystemGetProperty("mrj.version", null) != null)
    {
      final String userhome = safeSystemGetProperty("user.home", ".");
      logger.debug("Detected MacOS (Property 'mrj.version' is present.");
      internalRegisterFontPath(new File(userhome + "/Library/Fonts"));
      internalRegisterFontPath(new File("/Library/Fonts"));
      internalRegisterFontPath(new File("/Network/Library/Fonts"));
      internalRegisterFontPath(new File("/System/Library/Fonts"));
    }
    else if (StringUtilities.startsWithIgnoreCase(osname, "windows"))
    {
      registerWindowsFontPath();
    }
    else
    {
      logger.debug("Assuming unix like file structures");
      // Assume X11 is installed in the default location.
      internalRegisterFontPath(new File("/usr/X11R6/lib/X11/fonts"));
      internalRegisterFontPath(new File("/usr/share/fonts"));
    }

    internalRegisterFontPath(new File(jrepath, "lib" + fs + "fonts"));
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
  private void registerWindowsFontPath()
  {
    logger.debug("Found windows in os name, assuming DOS/Win32 structures");
    // Assume windows
    // If you are not using windows, ignore this. This just checks if a windows system
    // directory exist and includes a font dir.

    String fontPath = null;
    final String windirs = safeSystemGetProperty("java.library.path", null);
    final String fs = safeSystemGetProperty("file.separator", File.separator);

    if (windirs != null)
    {
      final StringTokenizer strtok = new StringTokenizer
              (windirs, safeSystemGetProperty("path.separator", File.pathSeparator));
      while (strtok.hasMoreTokens())
      {
        final String token = strtok.nextToken();

        if (StringUtilities.endsWithIgnoreCase(token, "System32"))
        {
          // found windows folder ;-)
          final int lastBackslash = token.lastIndexOf(fs);
          fontPath = token.substring(0, lastBackslash) + fs + "Fonts";

          break;
        }
      }
    }
    logger.debug("Fonts located in \"" + fontPath + "\"");
    if (fontPath != null)
    {
      final File file = new File(fontPath);
      internalRegisterFontPath(file);
    }
  }

  /**
   * Register all fonts (*.ttf files) in the given path.
   *
   * @param file     the directory that contains the font files.
   * @param encoding the encoding for the given font.
   */
  public synchronized void registerFontPath(final File file)
  {
    internalRegisterFontPath(file);
  }

  /**
   * Register all fonts (*.ttf files) in the given path.
   *
   * @param file       the directory that contains the font files.
   * @param encoding   the encoding for the given font.
   * @param knownFonts a map containing all known fonts
   * @param seenFiles  a map containing all known font files.
   */
  private synchronized void internalRegisterFontPath(final File file)
  {
    if (file.exists() == false)
    {
      return;
    }
    if (file.isDirectory() == false)
    {
      return;
    }
    if (file.canRead() == false)
    {
      return;
    }

    final File[] files = file.listFiles(FONTPATHFILTER);
    for (int i = 0; i < files.length; i++)
    {
      final File currentFile = files[i];
      if (currentFile.isDirectory())
      {
        internalRegisterFontPath(currentFile);
      }
      else
      {
        try
        {
          registerFontFile(currentFile);
        }
        catch (IOException e)
        {
          // Ignore that font ...
        }
      }
    }
  }

  public void registerFontFile(final File currentFile) throws IOException {
    final FontFileRecord record = new FontFileRecord(currentFile);

    final FontFileRecord cachedRecord = (FontFileRecord)
            seenFiles.get(record.getFilename());
    if (ObjectUtilities.equal(record, cachedRecord))
    {
      return;
    }
    internalRegisterFontFile(currentFile);
    seenFiles.put(record.getFilename(), record);
  }

  private void internalRegisterFontFile(final File file) throws IOException
  {
    if (StringUtilities.endsWithIgnoreCase(file.getName(), ".ttc"))
    {
      TrueTypeCollection ttc = new TrueTypeCollection(file);
      for (int i = 0; i < ttc.getNumFonts(); i++)
      {
        final TrueTypeFont font = ttc.getFont(i);
        registerTrueTypeFont(font);
        font.dispose();
      }
    }
    else
    {
      TrueTypeFont font = new TrueTypeFont(file);
      registerTrueTypeFont(font);
      font.dispose();
    }
  }

  private void registerTrueTypeFont(final TrueTypeFont font)
          throws IOException
  {
    NameTable table = (NameTable) font.getTable(NameTable.TABLE_ID);
    if (table == null)
    {
      throw new IOException("The NameTable is required");
    }

    final String familyName = table.getPrimaryName(NameTable.NAME_FAMILY);
    final String[] allNames = table.getAllNames(NameTable.NAME_FAMILY);

    final DefaultFontFamily fontFamily = createFamily(familyName);

    for (int i = 0; i < allNames.length; i++)
    {
      final String name = allNames[i];
      fontFamily.addName(name);
      alternateFamilyNames.put(name, fontFamily);
    }
    TrueTypeFontRecord record = new TrueTypeFontRecord(font, fontFamily);
    fontFamily.setFontRecord(record);
  }

  private DefaultFontFamily createFamily(String name)
  {
    final DefaultFontFamily fontFamily = (DefaultFontFamily)
            this.fontFamilies.get(name);
    if (fontFamily != null)
    {
      return fontFamily;
    }

    final DefaultFontFamily createdFamily = new DefaultFontFamily(name);
    this.fontFamilies.put(name, createdFamily);
    return createdFamily;
  }

  public String[] getRegisteredFamilies ()
  {
    return (String[]) fontFamilies.keySet().toArray
            (new String[fontFamilies.size()]);
  }

  public FontFamily getFontFamily (String name)
  {
    return (FontFamily) this.fontFamilies.get(name);
  }
}
