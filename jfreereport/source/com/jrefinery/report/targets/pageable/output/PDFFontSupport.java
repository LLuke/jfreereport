/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------
 * PDFFontSupport.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id:  $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 *
 */
package com.jrefinery.report.targets.pageable.output;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;
import com.jrefinery.report.util.StringUtil;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.pageable.OutputTargetException;

import java.awt.Font;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * PDF font support.
 *
 * @author Thomas Morgner
 */
public class PDFFontSupport
{
  /** Storage for BaseFont objects created. */
  private Map baseFonts;
  
  /** Embed fonts? */
  private boolean embedFonts;

  /**
   * Creates a new support instance.
   */
  public PDFFontSupport()
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
   * Returns the flag that controls whether or not the fonts are embedded in the PDF output.
   *
   * @return true or false.
   */
  public boolean isEmbedFonts()
  {
    return embedFonts;
  }

  /**
   * Sets the flag that controls whether or not the fonts are embedded in the PDF output.
   *
   * @param embedFonts  embed?
   */
  public void setEmbedFonts(boolean embedFonts)
  {
    this.embedFonts = embedFonts;
  }

  /**
   * Creates a PDFFontRecord for an AWT font.  If no basefont could be created, an 
   * OutputTargetException is thrown.
   *
   * @param font  the new font (null not permitted).
   * @param encoding  the encoding.
   *
   * @return the PDF font record.
   *
   * @throws OutputTargetException if there was a problem setting the font for the target.
   */
  public PDFFontRecord createBaseFont(Font font, String encoding) throws OutputTargetException
  {
    if (font == null)
    {
      throw new NullPointerException();
    }

    // use the Java logical font name to map to a predefined iText font.
    String fontKey = null;
    String logicalName = font.getName();
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

    if (isCourier(logicalName))
    {
      fontKey = createCourierName(bold, italic);
    }
    else if (isSerif(logicalName))
    {
      fontKey = createSerifName(bold, italic);
    }
    else if (isSansSerif (logicalName))
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
    if (encoding.equals(BaseFont.IDENTITY_H) || encoding.equals(BaseFont.IDENTITY_V))
    {
      stringEncoding = "iso-8859-1";
    }

    PDFFontRecord fontRecord = getFromCache(fontKey, encoding);
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
      String filename = PDFOutputTarget.getFontFactory().getFontfileForName(fontKey);
      if (filename != null)
      {
        fontRecord = createFontFromTTF(font, filename, encoding, stringEncoding);
        if (fontRecord != null)
        {
          return fontRecord;
        }
      }
      else
      {
        // filename is null, so no ttf file registered for the fontname, maybe this is
        // one of the internal fonts ...
        BaseFont f = BaseFont.createFont(fontKey, stringEncoding, isEmbedFonts(), 
                                         false, null, null);

        if (f != null)
        {
          fontRecord = new PDFFontRecord();
          fontRecord.setAwtFont(font);
          fontRecord.setBaseFont(f);
          fontRecord.setEmbedded(isEmbedFonts());
          fontRecord.setEncoding(stringEncoding);
          fontRecord.setLogicalName(fontKey);
          putToCache(fontRecord);
          return fontRecord;
        }
      }
    }
    catch (Exception e)
    {
      Log.warn("BaseFont.createFont failed. Key = " + fontKey, e);
    }
    // fallback .. use BaseFont.HELVETICA as default
    try
    {
      BaseFont f = BaseFont.createFont(BaseFont.HELVETICA, stringEncoding, isEmbedFonts(), 
                                       false, null, null);
      if (f != null)
      {
        fontRecord = new PDFFontRecord();
        fontRecord.setAwtFont(font);
        fontRecord.setBaseFont(f);
        fontRecord.setEmbedded(isEmbedFonts());
        fontRecord.setEncoding(stringEncoding);
        fontRecord.setLogicalName(fontKey);
        putToCache(fontRecord);
        return fontRecord;
      }
    }
    catch (Exception e)
    {
      Log.warn("BaseFont.createFont for FALLBACK failed.", e);
      throw new OutputTargetException("Null font = " + fontKey);
    }
    throw new OutputTargetException("BaseFont creation failed, null font: " + fontKey);
  }

  /**
   * Creates a PDF font record from a true type font.
   *
   * @param font  the font.
   * @param filename  the filename.
   * @param encoding  the encoding.
   * @param stringEncoding  the string encoding.
   *
   * @return the PDF font record.
   *
   * @throws IOException if there is an I/O problem.
   * @throws DocumentException ??.
   */
  private PDFFontRecord createFontFromTTF (Font font, String filename, 
                                           String encoding, String stringEncoding)
    throws IOException, DocumentException
  {

    // TrueType fonts need extra handling if the font is a symbolic font.
    if ((StringUtil.endsWithIgnoreCase(filename, ".ttf") 
         || StringUtil.endsWithIgnoreCase(filename, ".ttc")) == false)
    {
      return null;
    }

    String fontKey = null;

    if (font.isBold() && font.isItalic())
    {
      fontKey = filename + ",BoldItalic";
    }
    else if (font.isBold())
    {
      fontKey = filename + ",Bold";
    }
    else if (font.isItalic())
    {
      fontKey = filename + ",Italic";
    }
    else
    {
      fontKey = filename;
    }

    // check if this font is in the cache ...
    //Log.warn ("TrueTypeFontKey : " + fontKey + " Font: " + font.isItalic() + " Encoding: " 
    //          + encoding);
    PDFFontRecord fontRec = getFromCache(fontKey, encoding);
    if (fontRec != null) 
    {
      return fontRec;
    }

    // no, we have to create a new instance
    PDFFontRecord record = new PDFFontRecord();
    record.setAwtFont(font);
    record.setEmbedded(isEmbedFonts());
    record.setLogicalName(fontKey);
    try
    {
      BaseFont f = BaseFont.createFont(fontKey, encoding, isEmbedFonts(), false, null, null);
      record.setBaseFont(f);
      record.setEncoding(encoding);
    }
    catch (DocumentException de)
    {
      // Fallback to iso8859-1 encoding (!this is not IDENTITY-H)
      BaseFont f = BaseFont.createFont(fontKey, stringEncoding, isEmbedFonts(), false, null, null);
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
  private void putToCache (PDFFontRecord record)
  {
    baseFonts.put (record.createKey(), record);
    Log.debug (record.createKey().equals(record.createKey()) + " is equal");
  }

  /**
   * Retrieves a record from the cache.
   *
   * @param fontKey  the font key.
   * @param encoding  the encoding.
   *
   * @return the PDF font record.
   */
  private PDFFontRecord getFromCache (String fontKey, String encoding)
  {
    PDFFontRecord r = (PDFFontRecord) baseFonts.get (new PDFFontRecordKey(fontKey, encoding));
    return r;
  }

  /**
   * Returns true if the logical font name is equivalent to 'Courier', and false otherwise.
   *
   * @param logicalName  the logical font name.
   *
   * @return true or false.
   */
  private boolean isCourier (String logicalName)
  {
     return (StringUtil.startsWithIgnoreCase(logicalName, "dialoginput")
        || StringUtil.startsWithIgnoreCase(logicalName, "monospaced"));
  }

  /**
   * Returns true if the logical font name is equivalent to 'Serif', and false otherwise.
   *
   * @param logicalName  the logical font name.
   *
   * @return true or false.
   */
  private boolean isSerif (String logicalName)
  {
    return (StringUtil.startsWithIgnoreCase(logicalName, "serif"));
  }

  /**
   * Returns true if the logical font name is equivalent to 'SansSerif', and false otherwise.
   *
   * @param logicalName  the logical font name.
   *
   * @return true or false.
   */
  private boolean isSansSerif (String logicalName)
  {
    return StringUtil.startsWithIgnoreCase(logicalName, "SansSerif")
        || StringUtil.startsWithIgnoreCase(logicalName, "Dialog");
  }

  /**
   * Creates a sans-serif font name.
   *
   * @param bold  bold?
   * @param italic  italic?
   *
   * @return the font name.
   */
  private String createSansSerifName (boolean bold, boolean italic)
  {
    String fontKey = null;
    if (bold && italic)
    {
      fontKey = BaseFont.HELVETICA_BOLDOBLIQUE;
    }
    else if (bold)
    {
      fontKey = BaseFont.HELVETICA_BOLD;
    }
    else if (italic)
    {
      fontKey = BaseFont.HELVETICA_OBLIQUE;
    }
    else
    {
      fontKey = BaseFont.HELVETICA;
    }

    return fontKey;
  }

  /**
   * Creates a serif font name.
   *
   * @param bold  bold?
   * @param italic  italic?
   *
   * @return the font name.
   */
  private String createSerifName (boolean bold, boolean italic)
  {
    String fontKey = null;
    if (bold && italic)
    {
      fontKey = BaseFont.TIMES_BOLDITALIC;
    }
    else if (bold)
    {
      fontKey = BaseFont.TIMES_BOLD;
    }
    else if (italic)
    {
      fontKey = BaseFont.TIMES_ITALIC;
    }
    else
    {
      fontKey = BaseFont.TIMES_ROMAN;
    }
    return fontKey;
  }

  /**
   * Creates a courier font name.
   *
   * @param bold  bold?
   * @param italic  italic?
   *
   * @return the font name.
   */
  private String createCourierName (boolean bold, boolean italic)
  {
    String fontKey = null;
    if (bold && italic)
    {
      fontKey = BaseFont.COURIER_BOLDOBLIQUE;
    }
    else if (bold)
    {
      fontKey = BaseFont.COURIER_BOLD;
    }
    else if (italic)
    {
      fontKey = BaseFont.COURIER_OBLIQUE;
    }
    else
    {
      fontKey = BaseFont.COURIER;
    }
    return fontKey;
  }

}
