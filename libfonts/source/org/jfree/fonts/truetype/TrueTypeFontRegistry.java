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
 * $Id: TrueTypeFontRegistry.java,v 1.7 2006/12/03 18:11:59 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.truetype;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import org.jfree.fonts.FontException;
import org.jfree.fonts.registry.AbstractFontFileRegistry;
import org.jfree.fonts.registry.DefaultFontFamily;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontMetricsFactory;
import org.jfree.util.Log;
import org.jfree.util.StringUtils;

/**
 * Creation-Date: 07.11.2005, 19:05:46
 *
 * @author Thomas Morgner
 */
public class TrueTypeFontRegistry extends AbstractFontFileRegistry
{
  private static class FontFileRecord implements Serializable
  {
    private long lastAccessTime;
    private long fileSize;
    private String filename;

    protected FontFileRecord(final File file) throws IOException
    {
      this(file.getCanonicalPath(), file.length(), file.lastModified());
    }

    protected FontFileRecord(final String filename,
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
      int result = (int) (lastAccessTime ^ (lastAccessTime >>> 32));
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
    protected FontPathFilter()
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
      if (StringUtils.endsWithIgnoreCase(name, ".ttf"))
      {
        return true;
      }
      if (StringUtils.endsWithIgnoreCase(name, ".ttc"))
      {
        return true;
      }
      if (StringUtils.endsWithIgnoreCase(name, ".otf"))
      {
        return true;
      }
      return false;
    }

  }

  /** The singleton instance of the font path filter. */
  private static final FontPathFilter FONTPATHFILTER = new FontPathFilter();

  private HashMap seenFiles;
  private HashMap fontFamilies;
  private HashMap alternateFamilyNames;
  private HashMap fullFontNames;

  public TrueTypeFontRegistry()
  {
    this.seenFiles = new HashMap();
    this.fontFamilies = new HashMap();
    this.alternateFamilyNames = new HashMap();
    this.fullFontNames = new HashMap();
  }

  protected FileFilter getFileFilter()
  {
    return FONTPATHFILTER;
  }

  protected boolean isCached(final File file)
  {
    try
    {
      final FontFileRecord stored = (FontFileRecord) seenFiles.get(file.getCanonicalPath());
      if (stored == null)
      {
        return false;
      }

      final FontFileRecord rec = new FontFileRecord(file);
      if (stored.equals(rec) == false)
      {
        seenFiles.remove(rec);
        return false;
      }
      return true;
    }
    catch (IOException e)
    {
      return false;
    }
  }

  protected void addFont(final File file, final String encoding) throws IOException
  {
    try
    {
      if (StringUtils.endsWithIgnoreCase(file.getName(), ".ttc"))
      {
        final TrueTypeCollection ttc = new TrueTypeCollection(file);
        for (int i = 0; i < ttc.getNumFonts(); i++)
        {
          final TrueTypeFont font = ttc.getFont(i);
          registerTrueTypeFont(font);
          font.dispose();
        }
      }
      else
      {
        final TrueTypeFont font = new TrueTypeFont(file);
        registerTrueTypeFont(font);
        font.dispose();
      }
      final FontFileRecord value = new FontFileRecord(file);
      seenFiles.put(file.getCanonicalPath(), value);
    }
    catch (Exception e)
    {
      // An error must not stop us on our holy mission to find an register
      // all fonts :)
    }
  }

  private void registerTrueTypeFont(final TrueTypeFont font)
          throws IOException
  {
    final NameTable table = (NameTable) font.getTable(NameTable.TABLE_ID);
    if (table == null)
    {
      throw new IOException(
              "The NameTable is required for all conforming fonts.");
    }

    final String familyName = table.getPrimaryName(NameTable.NAME_FAMILY);
    final String[] allNames = table.getAllNames(NameTable.NAME_FAMILY);

    final DefaultFontFamily fontFamily = createFamily(familyName);
    alternateFamilyNames.put(familyName, fontFamily);

    for (int i = 0; i < allNames.length; i++)
    {
      final String name = allNames[i];
      fontFamily.addName(name);
      alternateFamilyNames.put(name, fontFamily);
    }

    final String[] allFullNames = table.getAllNames(NameTable.NAME_FULLNAME);
    for (int i = 0; i < allFullNames.length; i++)
    {
      final String name = allFullNames[i];
      this.fullFontNames.put(name, fontFamily);
    }

    try
    {
      final TrueTypeFontRecord record = new TrueTypeFontRecord(font, fontFamily);
      fontFamily.addFontRecord(record);
    }
    catch (FontException e)
    {
      Log.info("The font '" + font.getFilename() + "' is invalid.");
    }
  }

  private DefaultFontFamily createFamily(final String name)
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

  public String[] getRegisteredFamilies()
  {
    return (String[]) fontFamilies.keySet().toArray
            (new String[fontFamilies.size()]);
  }

  public String[] getAllRegisteredFamilies()
  {
    return (String[]) alternateFamilyNames.keySet().toArray
            (new String[alternateFamilyNames.size()]);
  }

  public FontFamily getFontFamily(final String name)
  {
    final FontFamily primary = (FontFamily) this.fontFamilies.get(name);
    if (primary != null)
    {
      return primary;
    }
    final FontFamily secondary = (FontFamily)
            this.alternateFamilyNames.get(name);
    if (secondary != null)
    {
      return secondary;
    }
    return (FontFamily) this.fullFontNames.get(name);
  }

  /**
   * Creates a new font metrics factory. That factory is specific to a certain
   * font registry and is not required to handle font records from foreign font
   * registries.
   * <p/>
   * A font metrics factory should never be used on its own. It should be
   * embedded into and used by a FontStorage implementation.
   *
   * @return a new FontMetricsFactory instance
   */
  public FontMetricsFactory createMetricsFactory()
  {
    return new TrueTypeFontMetricsFactory();
  }
}
