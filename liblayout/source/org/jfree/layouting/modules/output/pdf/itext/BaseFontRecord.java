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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.pdf.itext;

import com.lowagie.text.pdf.BaseFont;

/**
 * A PDF font record. The record is used to cache the generated PDF fonts. Once created
 * the base font record is immutable. The base font record does not store font sizes.
 *
 * @author Thomas Morgner
 */
public final class BaseFontRecord
{
  /**
   * The iText base font.
   */
  private BaseFont baseFont;

  /**
   * The file name.
   */
  private String fileName;

  /**
   * A flag indicating whether this font record describes an embedded PDF font.
   */
  private boolean embedded;
  // give me a marker to know whether to apply manual bold and italics styles ..
  private boolean trueTypeFont;

  private transient BaseFontRecordKey key;
  private boolean bold;
  private boolean italics;

  /**
   * Creates a new font record.
   *
   * @param logicalName the logical iText name of the font.
   * @param embedded    a flag that defines whether this font should be embedded in the
   *                    target document.
   * @param baseFont    the generated base font for the given font definition.
   */
  public BaseFontRecord (final String fileName,
                         final boolean trueTypeFont,
                         final boolean embedded,
                         final BaseFont baseFont,
                         final boolean bold,
                         final boolean italics)
  {
    if (baseFont == null)
    {
      throw new NullPointerException("iText-FontDefinition is null.");
    }
    if (fileName == null)
    {
      throw new NullPointerException("Logical font name is null.");
    }
    this.trueTypeFont = trueTypeFont;
    this.baseFont = baseFont;
    this.fileName = fileName;
    this.embedded = embedded;
    this.italics = italics;
    this.bold = bold;
  }

  public boolean isTrueTypeFont()
  {
    return trueTypeFont;
  }

  public boolean isBold()
  {
    return bold;
  }

  public boolean isItalics()
  {
    return italics;
  }

  /**
   * Creates a font record key.
   *
   * @return the font record key.
   */
  public BaseFontRecordKey createKey ()
  {
    if (key == null)
    {
      key = new BaseFontRecordKey(getFileName(), getEncoding(), isEmbedded());
    }
    return key;
  }

  /**
   * Returns the encoding.
   *
   * @return the encoding.
   */
  public String getEncoding ()
  {
    return baseFont.getEncoding();
  }

  /**
   * Returns true if the font should be embedded in the PDF output, and false if not.
   *
   * @return true or false.
   */
  public boolean isEmbedded ()
  {
    return embedded;
  }

  /**
   * Returns the logical name of the font.
   *
   * @return the logical name.
   */
  public String getFileName ()
  {
    return fileName;
  }

  /**
   * Returns the iText BaseFont.
   *
   * @return the itext BaseFont.
   */
  public BaseFont getBaseFont ()
  {
    return baseFont;
  }

}
