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
 * BaseFontSupport.java
 * --------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BaseFontSupport.java,v 1.2 2003/07/14 20:16:06 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 * 29-Jan-2003 : moved from pageable.output to support.itext package.
 * 07-Feb-2003 : Documentation updated
 *
 */

package org.jfree.report.modules.output.support.itext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.Log;
import org.jfree.report.util.StringUtil;

/**
 * iText font support.
 *
 * @author Thomas Morgner
 */
public class BaseFontSupport
{
  /** Storage for BaseFont objects created. */
  private Map baseFonts;

  /**
   * Creates a new support instance.
   */
  public BaseFontSupport()
  {
    this.baseFonts = new HashMap();
  }

  /**
   * Close the font support.
   */
  public void close()
  {
    this.baseFonts.clear();
  }

  /**
   * Creates a BaseFontRecord for an AWT font.  If no basefont could be created, an
   * OutputTargetException is thrown.
   *
   * @param font  the new font (null not permitted).
   * @param encoding  the encoding.
   * @param embedded a flag indicating whether to embed the font glyphs in the generated documents.
   *
   * @return the base font record.
   *
   * @throws BaseFontCreateException if there was a problem setting the font for the target.
   */
  public BaseFontRecord createBaseFont(final FontDefinition font, final String encoding, final boolean embedded)
      throws BaseFontCreateException
  {
    if (font == null)
    {
      throw new NullPointerException();
    }

    //Log.debug ("Create Font: " + font + " Encoding: " + encoding + " Embedd: " + embedFonts);

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
      stringEncoding = "iso-8859-1";
    }

    BaseFontRecord fontRecord = getFromCache(fontKey, encoding);
    if (fontRecord != null)
    {
      return fontRecord;
    }
    fontRecord = getFromCache(fontKey, stringEncoding);
    if (fontRecord != null)
    {
      return fontRecord;
    }

    try
    {
      final String filename = BaseFontFactory.getFontFactory().getFontfileForName(fontKey);
      if (filename != null)
      {
        fontRecord = createFontFromTTF(font, filename, encoding, stringEncoding, embedded);
        if (fontRecord != null)
        {
          return fontRecord;
        }
      }
      else
      {
        // filename is null, so no ttf file registered for the fontname, maybe this is
        // one of the internal fonts ...
        final BaseFont f = BaseFont.createFont(fontKey, stringEncoding, embedded,
            false, null, null);

        if (f != null)
        {
          fontRecord = new BaseFontRecord();
          fontRecord.setFontDefinition(font);
          fontRecord.setBaseFont(f);
          fontRecord.setEmbedded(embedded);
          fontRecord.setEncoding(stringEncoding);
          fontRecord.setLogicalName(fontKey);
          putToCache(fontRecord);
          return fontRecord;
        }
      }
    }
    catch (Exception e)
    {
      Log.warn(new Log.SimpleMessage("BaseFont.createFont failed. Key = ",fontKey, e.getMessage()));
    }
    // fallback .. use BaseFont.HELVETICA as default
    try
    {
      final BaseFont f = BaseFont.createFont(BaseFont.HELVETICA, stringEncoding, embedded,
          false, null, null);
      if (f != null)
      {
        fontRecord = new BaseFontRecord();
        fontRecord.setFontDefinition(font);
        fontRecord.setBaseFont(f);
        fontRecord.setEmbedded(embedded);
        fontRecord.setEncoding(stringEncoding);
        fontRecord.setLogicalName(fontKey);
        putToCache(fontRecord);
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
   * @param font  the font.
   * @param filename  the filename.
   * @param encoding  the encoding.
   * @param stringEncoding  the string encoding.
   * @param embedded a flag indicating whether to embed the font glyphs in the generated documents.
   *
   * @return the PDF font record.
   *
   * @throws IOException if there is an I/O problem.
   * @throws DocumentException if the BaseFont could not be created.
   */
  private BaseFontRecord createFontFromTTF(final FontDefinition font, final String filename,
                                           final String encoding, final String stringEncoding,
                                           final boolean embedded)
      throws IOException, DocumentException
  {

    // TrueType fonts need extra handling if the font is a symbolic font.
    if ((StringUtil.endsWithIgnoreCase(filename, ".ttf")
        || StringUtil.endsWithIgnoreCase(filename, ".ttc")) == false)
    {
      return null;
    }

    String fontKey = filename;

    if (font.isBold() && font.isItalic())
    {
      fontKey += ",BoldItalic";
    }
    else if (font.isBold())
    {
      fontKey += ",Bold";
    }
    else if (font.isItalic())
    {
      fontKey += ",Italic";
    }

    // check if this font is in the cache ...
    //Log.warn ("TrueTypeFontKey : " + fontKey + " Font: " + font.isItalic() + " Encoding: "
    //          + encoding);
    final BaseFontRecord fontRec = getFromCache(fontKey, encoding);
    if (fontRec != null)
    {
      return fontRec;
    }

    // no, we have to create a new instance
    final BaseFontRecord record = new BaseFontRecord();
    record.setFontDefinition(font);
    record.setEmbedded(embedded);
    record.setLogicalName(fontKey);
    try
    {
      final BaseFont f = BaseFont.createFont(fontKey, encoding, embedded, false, null, null);
      record.setBaseFont(f);
      record.setEncoding(encoding);
    }
    catch (DocumentException de)
    {
      // Fallback to iso8859-1 encoding (!this is not IDENTITY-H)
      final BaseFont f = BaseFont.createFont(fontKey, stringEncoding, embedded, false, null, null);
      record.setBaseFont(f);
      record.setEncoding(stringEncoding);
    }
    if (record.getBaseFont() != null)
    {
      putToCache(record);
    }
    return record;
  }

  /**
   * Stores a record in the cache.
   *
   * @param record  the record.
   */
  private void putToCache(final BaseFontRecord record)
  {
    baseFonts.put(record.createKey(), record);
  }

  /**
   * Retrieves a record from the cache.
   *
   * @param fontKey  the font key.
   * @param encoding  the encoding.
   *
   * @return the PDF font record.
   */
  private BaseFontRecord getFromCache(final String fontKey, final String encoding)
  {
    final BaseFontRecord r = (BaseFontRecord) baseFonts.get(new BaseFontRecordKey(fontKey, encoding));
    return r;
  }

  /**
   * Creates a sans-serif font name.
   *
   * @param bold  bold?
   * @param italic  italic?
   *
   * @return the font name.
   */
  private String createSansSerifName(final boolean bold, final boolean italic)
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
   * @param bold  bold?
   * @param italic  italic?
   *
   * @return the font name.
   */
  private String createSerifName(final boolean bold, final boolean italic)
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
   * @param bold  bold?
   * @param italic  italic?
   *
   * @return the font name.
   */
  private String createCourierName(final boolean bold, final boolean italic)
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

}
