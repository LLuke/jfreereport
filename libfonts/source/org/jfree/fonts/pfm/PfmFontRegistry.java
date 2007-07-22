package org.jfree.fonts.pfm;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;

import org.jfree.fonts.registry.AbstractFontFileRegistry;
import org.jfree.fonts.registry.DefaultFontFamily;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontMetricsFactory;
import org.jfree.util.Log;
import org.jfree.util.StringUtils;

/**
 * Creation-Date: 21.07.2007, 16:58:06
 *
 * @author Thomas Morgner
 */
public class PfmFontRegistry extends AbstractFontFileRegistry
{
  /**
   * The font path filter is used to collect font files and directories during
   * the font path registration.
   */
  private static class FontPathFilter implements FileFilter
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
      return StringUtils.endsWithIgnoreCase(name, ".pfm");
    }

  }

  /** The singleton instance of the font path filter. */
  private static final FontPathFilter FONTPATHFILTER = new FontPathFilter();
  /** Fonts stored by name. */

  private HashMap fontFamilies;
  private HashMap alternateFamilyNames;
  private HashMap fullFontNames;
  private HashMap seenFiles;
  private boolean itextCompatibleChecks;

  public PfmFontRegistry()
  {
    this.fontFamilies = new HashMap();
    this.alternateFamilyNames = new HashMap();
    this.fullFontNames = new HashMap();
    this.seenFiles = new HashMap();
    this.itextCompatibleChecks = true;
  }

  public boolean isItextCompatibleChecks()
  {
    return itextCompatibleChecks;
  }

  public void setItextCompatibleChecks(final boolean itextCompatibleChecks)
  {
    this.itextCompatibleChecks = itextCompatibleChecks;
  }

  protected FileFilter getFileFilter()
  {
    return FONTPATHFILTER;
  }

  public FontMetricsFactory createMetricsFactory()
  {
    // this is a todo - for now we rely on itext
    throw new UnsupportedOperationException();
  }

  protected boolean isCached(final File file)
  {
    return super.isCached(file);
  }

  /**
   * Adds the fontname by creating the basefont object. This method tries to
   * load the fonts as embeddable fonts, if this fails, it repeats the loading
   * with the embedded-flag set to false.
   *
   * @param font     the font file name.
   * @param encoding the encoding.
   * @throws com.lowagie.text.DocumentException if the base font could not be created
   * @throws java.io.IOException       if the base font file could not be read.
   */
  public void addFont(final File font, final String encoding) throws IOException
  {
    if (seenFiles.containsKey(font))
    {
      return; // already in there
    }

    final String fileName = font.getCanonicalPath();
    final String filePfbName = fileName.substring(0, fileName.length() - 3) + "pfb";
    final File filePfb = new File(filePfbName);
    boolean embedded = true;
    if (filePfb.exists() == false ||
        filePfb.isFile() == false ||
        filePfb.canRead() == false)
    {
      Log.warn("Cannot embedd font: " + filePfb + " is missing for " + font);
      embedded = false;
    }

    final PfmFont pfmFont = new PfmFont(font, embedded);
    if (itextCompatibleChecks)
    {
      if (pfmFont.isItextCompatible() == false)
      {
        Log.warn("Cannot embedd font: pfb-file for " + font + " is not valid (according to iText).");
      }
    }
    registerFont (pfmFont);
    pfmFont.dispose();
  }

  private void registerFont(final PfmFont font) throws IOException
  {
    final String windowsName = font.getFamilyName();
    final String postscriptName = font.getFontName();

    final DefaultFontFamily fontFamily = createFamily(windowsName);
    this.alternateFamilyNames.put(windowsName, fontFamily);
    this.alternateFamilyNames.put(postscriptName, fontFamily);

    this.fullFontNames.put(windowsName, fontFamily);
    this.fullFontNames.put(postscriptName, fontFamily);

    final PfmFontRecord record = new PfmFontRecord(font, fontFamily);
    fontFamily.addFontRecord(record);

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
}
