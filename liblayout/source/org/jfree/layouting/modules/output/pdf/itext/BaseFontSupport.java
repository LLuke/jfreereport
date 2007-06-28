/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * $Id: BaseFontSupport.java,v 1.6 2007/05/14 09:01:01 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.pdf.itext;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontMapper;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontSource;
import org.jfree.fonts.truetype.TrueTypeFontRecord;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.ExtendedConfiguration;
import org.jfree.util.Log;
import org.jfree.util.StringUtils;

/**
 * iText font support.
 *
 * @author Thomas Morgner
 */
public class BaseFontSupport implements FontMapper
{
  private static ResourceManager resourceManager;

  protected static synchronized ResourceManager getResourceManager ()
  {
    if (resourceManager == null)
    {
      resourceManager = new ResourceManager();
      resourceManager.registerDefaults();
      resourceManager.registerFactory(new BaseFontResourceFactory());
    }
    return resourceManager;
  }

  /**
   * Storage for BaseFont objects created.
   */
  private final Map baseFonts;

  private String defaultEncoding;

  private boolean useGlobalCache;
  private boolean embedFonts;
  private static BaseFontFactory baseFontFactory;

  /**
   * Creates a new support instance.
   */
  public BaseFontSupport ()
  {
    this(BaseFont.IDENTITY_H);
  }

  public BaseFontSupport (final String defaultEncoding)
  {
    this.baseFonts = new HashMap();
    this.defaultEncoding = defaultEncoding;
    final ExtendedConfiguration extendedConfig = LibLayoutBoot.getInstance()
            .getExtendedConfig();
    this.useGlobalCache = extendedConfig.getBoolProperty
            ("org.jfree.layouting.modules.output.pdf.itext.UseGlobalFontCache");

    synchronized(BaseFontSupport.class)
    {
      if (baseFontFactory == null)
      {
        baseFontFactory = new BaseFontFactory();
        baseFontFactory.registerDefaultFontPath(defaultEncoding);
      }
    }
  }



  public String getDefaultEncoding ()
  {
    return defaultEncoding;
  }

  public void setDefaultEncoding (final String defaultEncoding)
  {
    if (defaultEncoding == null)
    {
      throw new NullPointerException("DefaultEncoding is null.");
    }
    this.defaultEncoding = defaultEncoding;
  }

  public boolean isEmbedFonts()
  {
    return embedFonts;
  }

  public void setEmbedFonts(final boolean embedFonts)
  {
    this.embedFonts = embedFonts;
  }

  /**
   * Close the font support.
   */
  public void close ()
  {
    this.baseFonts.clear();
  }

  /**
   * Creates a BaseFontRecord for an AWT font.  If no basefont could be created, an
   * OutputTargetException is thrown.
   *
   * @param font     the new font (null not permitted).
   * @param encoding the encoding.
   * @param embedded a flag indicating whether to embed the font glyphs in the generated
   *                 documents.
   * @return the base font record.
   *
   * @throws BaseFontCreateException if there was a problem setting the font for the
   *                                 target.
   */
  public BaseFontRecord createBaseFont (final String logicalName,
                                        final boolean bold,
                                        final boolean italic,
                                        final String encoding,
                                        final boolean embedded)
          throws BaseFontCreateException
  {
    if (logicalName == null)
    {
      throw new NullPointerException("Font definition is null.");
    }
    if (encoding == null)
    {
      throw new NullPointerException("Encoding is null");
    }

    final boolean builtInFont;
    final String fontKey;
    if (isCourier(logicalName))
    {
      fontKey = createCourierName(bold, italic);
      builtInFont = true;
    }
    else if (isSerif(logicalName))
    {
      fontKey = createSerifName(bold, italic);
      builtInFont = true;
    }
    else if (isSansSerif(logicalName))
    { // default, this catches Dialog and SansSerif
      fontKey = createSansSerifName(bold, italic);
      builtInFont = true;
    }
    else
    {
      fontKey = logicalName;
      builtInFont = false;
    }

    // iText uses some weird mapping between IDENTY-H/V and java supported encoding, IDENTITY-H/V is
    // used to recognize TrueType fonts, but the real JavaEncoding is used to encode Type1 fonts
    String stringEncoding = encoding;

    // Correct the encoding for truetype fonts
    // iText will crash if IDENTITY_H is used to create a base font ...
    if (encoding.equalsIgnoreCase(BaseFont.IDENTITY_H) ||
        encoding.equalsIgnoreCase(BaseFont.IDENTITY_V))
    {
      //changed to UTF to support all unicode characters ..
      stringEncoding = "utf-8";
    }

    try
    {
      final FontRecord registryFontRecord =
              baseFontFactory.getFontForName(fontKey, bold, italic);
      if (registryFontRecord != null && builtInFont == false)
      {
        // Try one: We do have a registered TrueType file. The cache is checked
        // first, creating fonts is expensive. If no cached version is found,
        // we create one..

        boolean embeddedOverride = embedded;
        if (embedded == true && registryFontRecord instanceof FontSource)
        {
          final FontSource source = (FontSource) registryFontRecord;
          if (source.isEmbeddable() == false)
          {
            Log.warn("License of font forbids embedded usage for font: " + fontKey);
            // strict mode here?
            embeddedOverride = false;
          }
        }
        final BaseFontRecord fontRecord = createFontFromTTF
                (registryFontRecord, bold, italic,
                        encoding, stringEncoding, embeddedOverride);
        if (fontRecord != null)
        {
          return fontRecord;
        }
      }
      else
      {
        // Alternative: No Registered TrueType font was found. OK; don't panic,
        // we try to create a font anyway..

        BaseFontRecord fontRecord = getFromCache(fontKey, encoding, embedded);
        if (fontRecord != null)
        {
          return fontRecord;
        }
        fontRecord = getFromCache(fontKey, stringEncoding, embedded);
        if (fontRecord != null)
        {
          return fontRecord;
        }

        // filename is null, so no ttf file registered for the fontname, maybe this is
        // one of the internal fonts ...
        final BaseFont f = BaseFont.createFont(fontKey, stringEncoding, embedded,
                useGlobalCache, null, null);
        if (f != null)
        {
          fontRecord = new BaseFontRecord(fontKey, false, embedded, f, bold, italic);
          putToCache(fontRecord);
          return fontRecord;
        }
      }
    }
    catch (Exception e)
    {
      if (Log.isDebugEnabled())
      {
        Log.debug(new Log.SimpleMessage
              ("BaseFont.createFont failed. Key = ", fontKey, ": ", e.getMessage()), e);
      }
      else if (Log.isWarningEnabled())
      {
        Log.warn(new Log.SimpleMessage
              ("BaseFont.createFont failed. Key = ", fontKey, ": ", e.getMessage()));
      }
    }
    // fallback .. use BaseFont.HELVETICA as default
    try
    {
      // check, whether HELVETICA is already created - yes, then return cached instance instead
      BaseFontRecord fontRecord = getFromCache(BaseFont.HELVETICA, stringEncoding, embedded);
      if (fontRecord != null)
      {
        // map all font references of the invalid font to the default font..
        // this might be not very nice, but at least the report can go on..
        putToCache(new BaseFontRecordKey(fontKey, encoding, embedded), fontRecord);
        return fontRecord;
      }

      // no helvetica created, so do this now ...
      final BaseFont f = BaseFont.createFont(BaseFont.HELVETICA, stringEncoding, embedded,
              useGlobalCache, null, null);
      if (f != null)
      {
        fontRecord = new BaseFontRecord
                (BaseFont.HELVETICA, false, embedded, f, bold, italic);
        putToCache(fontRecord);
        putToCache(new BaseFontRecordKey(fontKey, encoding, embedded), fontRecord);
        return fontRecord;
      }
    }
    catch (Exception e)
    {
      Log.warn("BaseFont.createFont for FALLBACK failed.", e);
      throw new BaseFontCreateException("Null font = " + fontKey);
    }
    throw new BaseFontCreateException("BaseFont creation failed, null font: " + fontKey);
  }

  /**
   * Creates a PDF font record from a true type font.
   *
   * @param font           the font.
   * @param filename       the filename.
   * @param encoding       the encoding.
   * @param stringEncoding the string encoding.
   * @param embedded       a flag indicating whether to embed the font glyphs in the
   *                       generated documents.
   * @return the PDF font record.
   *
   * @throws IOException       if there is an I/O problem.
   * @throws DocumentException if the BaseFont could not be created.
   */
  private BaseFontRecord createFontFromTTF (final FontRecord fontRecord,
                                            final boolean bold,
                                            final boolean italic,
                                            final String encoding,
                                            final String stringEncoding,
                                            final boolean embedded)
          throws DocumentException
  {
    final String rawFilename;
    if (fontRecord instanceof TrueTypeFontRecord)
    {
      final TrueTypeFontRecord ttfRecord = (TrueTypeFontRecord) fontRecord;
      if (ttfRecord.getCollectionIndex() >= 0)
      {
        rawFilename = ttfRecord.getFontFile() + "," + ttfRecord.getCollectionIndex();
      }
      else
      {
        rawFilename = ttfRecord.getFontFile();
      }
    }
    else if (fontRecord instanceof FontSource)
    {
      final FontSource source = (FontSource) fontRecord;
      rawFilename = source.getFontFile();
    }
    else
    {
      return null;
    }

    final String filename;
    // check, whether the the physical font does not provide some of the
    // required styles. We have to synthesize them, if neccessary
    if ((fontRecord.isBold() == false && bold) &&
        (fontRecord.isItalic() == false && italic))
    {
      filename = rawFilename + ",BoldItalic";
    }
    else if (fontRecord.isBold() == false && bold)
    {
      filename = rawFilename + ",Bold";
    }
    else if (fontRecord.isItalic() == false && italic)
    {
      filename = rawFilename + ",Italic";
    }
    else
    {
      filename = rawFilename;
    }

    final BaseFontRecord fontRec = getFromCache(filename, encoding, embedded);
    if (fontRec != null)
    {
      return fontRec;
    }

    BaseFont f;
    try
    {
      try
      {
        f = BaseFont.createFont(filename, encoding, embedded, false, null, null);
      }
      catch (DocumentException e)
      {
        f = BaseFont.createFont(filename, stringEncoding, embedded, false, null, null);
      }
    }
    catch(IOException ioe)
    {
      throw new DocumentException("Failed to read the font: " + ioe);
    }

    // no, we have to create a new instance
    final BaseFontRecord record = new BaseFontRecord
            (filename, true, embedded, f, fontRecord.isBold(), fontRecord.isItalic());
    putToCache(record);
    return record;
  }

  /**
   * Stores a record in the cache.
   *
   * @param record the record.
   */
  private void putToCache (final BaseFontRecord record)
  {
    final BaseFontRecordKey key = record.createKey();
    putToCache(key, record);
  }

  private void putToCache (final BaseFontRecordKey key, final BaseFontRecord record)
  {
    baseFonts.put(key, record);
  }

  /**
   * Retrieves a record from the cache.
   *
   * @param fontKey  the font key; never null.
   * @param encoding the encoding; never null.
   * @return the PDF font record or null, if not found.
   */
  private BaseFontRecord getFromCache (final String fileName,
                                       final String encoding,
                                       final boolean embedded)
  {
    final Object key = new BaseFontRecordKey(fileName, encoding, embedded);
    final BaseFontRecord r = (BaseFontRecord) baseFonts.get(key);
    if (r != null)
    {
      return r;
    }
    return null;
  }

  /**
   * Creates a sans-serif font name.
   *
   * @param bold   bold?
   * @param italic italic?
   * @return the font name.
   */
  private String createSansSerifName (final boolean bold, final boolean italic)
  {
    if (bold && italic)
    {
      return BaseFont.HELVETICA_BOLDOBLIQUE;
    }
    else if (bold)
    {
      return BaseFont.HELVETICA_BOLD;
    }
    else if (italic)
    {
      return BaseFont.HELVETICA_OBLIQUE;
    }
    else
    {
      return BaseFont.HELVETICA;
    }
  }

  /**
   * Creates a serif font name.
   *
   * @param bold   bold?
   * @param italic italic?
   * @return the font name.
   */
  private String createSerifName (final boolean bold, final boolean italic)
  {
    if (bold && italic)
    {
      return BaseFont.TIMES_BOLDITALIC;
    }
    else if (bold)
    {
      return BaseFont.TIMES_BOLD;
    }
    else if (italic)
    {
      return BaseFont.TIMES_ITALIC;
    }
    else
    {
      return BaseFont.TIMES_ROMAN;
    }
  }

  /**
   * Creates a courier font name.
   *
   * @param bold   bold?
   * @param italic italic?
   * @return the font name.
   */
  private String createCourierName (final boolean bold, final boolean italic)
  {
    if (bold && italic)
    {
      return BaseFont.COURIER_BOLDOBLIQUE;
    }
    else if (bold)
    {
      return BaseFont.COURIER_BOLD;
    }
    else if (italic)
    {
      return BaseFont.COURIER_OBLIQUE;
    }
    else
    {
      return BaseFont.COURIER;
    }
  }

  /**
   * Returns a BaseFont which can be used to represent the given AWT Font
   *
   * @param	font		the font to be converted
   * @return	a BaseFont which has similar properties to the provided Font
   */

  public BaseFont awtToPdf (final Font font)
  {
    // this has to be defined in the element, an has to set as a default...
    final boolean embed = isEmbedFonts();
    final String encoding = getDefaultEncoding();

    try
    {
      final BaseFontRecord record = createBaseFont
          (font.getName(), font.isBold(), font.isItalic(), encoding, embed);
      return record.getBaseFont();
    }
    catch (Exception e)
    {
      // unable to handle font creation exceptions properly, all we can
      // do is throw a runtime exception and hope the best ..
      throw new BaseFontCreateException("Unable to create font: " + font, e);
    }
  }

  /**
   * Returns an AWT Font which can be used to represent the given BaseFont
   *
   * @param	font		the font to be converted
   * @param	size		the desired point size of the resulting font
   * @return	a Font which has similar properties to the provided BaseFont
   */

  public Font pdfToAwt (final BaseFont font, final int size)
  {
    final String logicalName = getFontName(font);
    final int style;
    if (StringUtils.endsWithIgnoreCase(logicalName, "bolditalic"))
    {
      style = Font.ITALIC | Font.BOLD;
    }
    else if (StringUtils.endsWithIgnoreCase(logicalName, "bold"))
    {
      style = Font.BOLD;
    }
    else if (StringUtils.endsWithIgnoreCase(logicalName, "italic"))
    {
      style = Font.ITALIC;
    }
    else
    {
      style = Font.PLAIN;
    }

    return new Font(logicalName, style, size);
  }

  private String getFontName (final BaseFont font)
  {
    final String[][] names = font.getFullFontName();
    if (names.length == 1)
    {
      return names[0][3];
    }

    String nameExtr = null;
    for (int k = 0; k < names.length; ++k)
    {
      final String[] name = names[k];
      // Macintosh language english
      if ("1".equals(name[0]) && "0".equals(name[1]))
      {
        nameExtr = name[3];
      }
      // Microsoft language code for US-English ...
      else if ("1033".equals(name[2]))
      {
        nameExtr = name[3];
        break;
      }
    }

    if (nameExtr != null)
    {
      return nameExtr;
    }
    return names[0][3];
  }


  /**
   * Returns true if the logical font name is equivalent to 'SansSerif', and false
   * otherwise.
   *
   * @return true or false.
   */
  public boolean isSansSerif (final String fontName)
  {
    return StringUtils.startsWithIgnoreCase(fontName, "SansSerif")
            || StringUtils.startsWithIgnoreCase(fontName, "Dialog")
            || StringUtils.startsWithIgnoreCase(fontName, "SanSerif");
    // is it a bug? Somewhere in the JDK this name is used (typo, but heck, we accept it anyway).
  }

  /**
   * Returns true if the logical font name is equivalent to 'Courier', and false
   * otherwise.
   *
   * @return true or false.
   */
  public boolean isCourier (final String fontName)
  {
    return (StringUtils.startsWithIgnoreCase(fontName, "dialoginput")
            || StringUtils.startsWithIgnoreCase(fontName, "monospaced"));
  }

  /**
   * Returns true if the logical font name is equivalent to 'Serif', and false otherwise.
   *
   * @return true or false.
   */
  public boolean isSerif (final String fontName)
  {
    return (StringUtils.startsWithIgnoreCase(fontName, "serif"));
  }

}
