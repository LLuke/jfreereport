/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * BaseFontSupport.java
 * --------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: BaseFontSupport.java,v 1.21 2005/09/23 10:05:38 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 * 29-Jan-2003 : moved from pageable.output to support.itext package.
 * 07-Feb-2003 : Documentation updated
 * 08-Oct-2003 : Not embeddable fonts are now used non-embedded.
 */

package org.jfree.report.modules.output.support.itext;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontMapper;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.StringUtil;
import org.jfree.util.Log;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.TrueTypeFontRecord;

/**
 * iText font support.
 *
 * @author Thomas Morgner
 */
public class BaseFontSupport implements FontMapper
{
  /**
   * Storage for BaseFont objects created.
   */
  private final Map baseFonts;

  private String defaultEncoding;

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
  public BaseFontRecord createBaseFont (final FontDefinition font,
                                        final String encoding, final boolean embedded)
          throws BaseFontCreateException
  {
    if (font == null)
    {
      throw new NullPointerException("Font definition is null.");
    }
    if (encoding == null)
    {
      throw new NullPointerException("Encoding is null");
    }

    // use the Java logical font name to map to a predefined iText font.
    final String logicalName = font.getFontName();
    boolean bold = false;
    boolean italic = false;

    if (StringUtil.endsWithIgnoreCase(logicalName, "bolditalic")
            || (font.isBold() && font.isItalic()))
    {
      bold = true;
      italic = true;
    }
    else if (StringUtil.endsWithIgnoreCase(logicalName, "bold") || (font.isBold()))
    {
      bold = true;
    }
    else if (StringUtil.endsWithIgnoreCase(logicalName, "italic") || (font.isItalic()))
    {
      italic = true;
    }

    final String fontKey;
    if (font.isCourier())
    {
      fontKey = createCourierName(bold, italic);
    }
    else if (font.isSerif())
    {
      fontKey = createSerifName(bold, italic);
    }
    else if (font.isSansSerif())
    { // default, this catches Dialog and SansSerif
      fontKey = createSansSerifName(bold, italic);
    }
    else
    {
      fontKey = logicalName;
    }

    // iText uses some weird mapping between IDENTY-H/V and java supported encoding, IDENTITY-H/V is
    // used to recognize TrueType fonts, but the real JavaEncoding is used to encode Type1 fonts
    String stringEncoding = encoding;

    // Correct the encoding for truetype fonts
    // iText will crash if IDENTITY_H is used to create a base font ...
    if (encoding.equals(BaseFont.IDENTITY_H) || encoding.equals(BaseFont.IDENTITY_V))
    {
      //changed to UTF to support all unicode characters ..
      stringEncoding = "utf-8";
    }

    final BaseFontFactory factory = BaseFontFactory.getFontFactory();
    try
    {
      final FontRecord registryFontRecord =
              factory.getFontForName(fontKey, bold, italic);
      if (registryFontRecord != null)
      {
        // Try one: We do have a registered TrueType file. The cache is checked
        // first, creating fonts is expensive. If no cached version is found,
        // we create one..

        boolean embeddedOverride = embedded;
        if (embedded == true && registryFontRecord.isEmbeddable() == false)
        {
          Log.warn("License of font forbids embedded usage for font: " + fontKey);
          // strict mode here?
          embeddedOverride = false;
        }
        BaseFontRecord fontRecord = createFontFromTTF(registryFontRecord, encoding, stringEncoding, embeddedOverride);
        if (fontRecord != null)
        {
          // store an additional logical record too..
//          putToCache(new BaseFontRecordKey(fontKey, encoding, embedded), fontRecord);
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

        // Log.debug ("The font with name " + fontKey + " was not recognized.");
        // filename is null, so no ttf file registered for the fontname, maybe this is
        // one of the internal fonts ...
        final BaseFont f = BaseFont.createFont(fontKey, stringEncoding, embedded,
                false, null, null);
        if (f != null)
        {
          fontRecord = new BaseFontRecord(fontKey, embedded, f, bold, italic);
          putToCache(fontRecord);
          return fontRecord;
        }
      }
    }
    catch (Exception e)
    {
//      final String filename = BaseFontFactory.getFontFactory().getFontfileForName(fontKey);
//      Log.debug ("BaseFont.createFont failed. " + filename); 
      Log.warn(new Log.SimpleMessage
              ("BaseFont.createFont failed. Key = ", fontKey, ": ", e.getMessage()));
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
              false, null, null);
      if (f != null)
      {
        fontRecord = new BaseFontRecord
                (BaseFont.HELVETICA, embedded, f, bold, italic);
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
                                            final String encoding,
                                            final String stringEncoding,
                                            final boolean embedded)
          throws IOException, DocumentException
  {
    // check if this font is in the cache ...
    //Log.warn ("TrueTypeFontKey : " + fontKey + " Font: " + font.isItalic() + " Encoding: "
    //          + encoding);
    final String filename;
    if (fontRecord instanceof TrueTypeFontRecord)
    {
      TrueTypeFontRecord ttfRecord = (TrueTypeFontRecord) fontRecord;
      if (ttfRecord.getCollectionIndex() >= 0)
      {
        filename = ttfRecord.getFontFile() + "," + ttfRecord.getCollectionIndex();
      }
      else
      {
        filename = ttfRecord.getFontFile();
      }
    }
    else
    {
      filename = fontRecord.getFontFile();
    }

    final BaseFontRecord fontRec = getFromCache(filename, encoding, embedded);
    if (fontRec != null)
    {
      return fontRec;
    }

    BaseFont f;
    try
    {
      f = BaseFont.createFont(filename, encoding, embedded, false, null, null);
    }
    catch (DocumentException de)
    {
      // Fallback to iso8859-1 encoding (!this is not IDENTITY-H)
      f = BaseFont.createFont(filename, stringEncoding, embedded, false, null, null);
    }

    // no, we have to create a new instance
    final BaseFontRecord record = new BaseFontRecord
            (filename, embedded, f, fontRecord.isBold(), fontRecord.isItalics());
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
    final boolean embed = false;
    final String encoding = getDefaultEncoding();
    final FontDefinition fdef = new FontDefinition
            (font.getName(), font.getSize(), font.isBold(), font.isItalic(),
                    false, false, encoding, embed);
    try
    {
      final BaseFontRecord record = createBaseFont(fdef, encoding, embed);
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
    boolean bold = false;
    boolean italic = false;

    if (StringUtil.endsWithIgnoreCase(logicalName, "bolditalic"))
    {
      bold = true;
      italic = true;
    }
    else if (StringUtil.endsWithIgnoreCase(logicalName, "bold"))
    {
      bold = true;
    }
    else if (StringUtil.endsWithIgnoreCase(logicalName, "italic"))
    {
      italic = true;
    }

    final FontDefinition fdef = new FontDefinition
            (logicalName, size, bold, italic, false, false, font.getEncoding(), font.isEmbedded());

    return fdef.getFont();
  }

  private String getFontName (final BaseFont font)
  {
    final String names[][] = font.getFullFontName();
    if (names.length == 1)
    {
      return names[0][3];
    }

    String nameExtr = null;
    for (int k = 0; k < names.length; ++k)
    {
      final String name[] = names[k];
      // Macintosh language english
      if (name[0].equals("1") && name[1].equals("0"))
      {
        nameExtr = name[3];
      }
      // Microsoft language code for US-English ...
      else if (name[2].equals("1033"))
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
}
