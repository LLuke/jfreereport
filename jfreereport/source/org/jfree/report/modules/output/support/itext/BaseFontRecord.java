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
 * -------------------
 * BaseFontRecord.java
 * -------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: BaseFontRecord.java,v 1.5 2003/11/07 18:33:55 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 * 01-Feb-2002 : Refactoring moved this class from package
 *               com.jefinery.report.targets.pageable.output
 *
 */

package org.jfree.report.modules.output.support.itext;

import com.lowagie.text.pdf.BaseFont;
import org.jfree.report.style.FontDefinition;

/**
 * A PDF font record. The record is used to cache the generated PDF fonts. 
 * Once created the base font record is immutable.
 *
 * @author Thomas Morgner
 */
public final class BaseFontRecord
{
  /** The AWT font. */
  private FontDefinition awtFont;

  /** The iText base font. */
  private BaseFont baseFont;

  /** The logical name. */
  private String logicalName;

  /** A flag indicating whether this font record describes an embedded PDF font. */
  private boolean embedded;

  /**
   * Creates a new font record.
   * 
   * @param font the font definition stored in this record.
   * @param logicalName the logical iText name of the font.
   * @param embedded a flag that defines whether this font should be embedded
   * in the target document.
   * @param baseFont the generated base font for the given font definition.
   */
  public BaseFontRecord(final FontDefinition font, final String logicalName,
                        final boolean embedded, final BaseFont baseFont)
  {
    if (font == null)
    {
      throw new NullPointerException("AWT-FontDefinition is null.");
    }
    if (baseFont == null)
    {
      throw new NullPointerException("iText-FontDefinition is null.");
    }
    if (logicalName == null)
    {
      throw new NullPointerException("Logical font name is null.");
    }
    this.awtFont = font;
    this.baseFont = baseFont;
    this.logicalName = logicalName;
    this.embedded = embedded;
  }

  /**
   * Creates a font record key.
   *
   * @return the font record key.
   */
  public BaseFontRecordKey createKey()
  {
    return new BaseFontRecordKey(getLogicalName(), getEncoding());
  }

  /**
   * Returns the encoding.
   *
   * @return the encoding.
   */
  public String getEncoding()
  {
    return baseFont.getEncoding();
  }

  /**
   * Returns true if the font should be embedded in the PDF output, and false if not.
   *
   * @return true or false.
   */
  public boolean isEmbedded()
  {
    return embedded;
  }

  /**
   * Returns the logical name of the font.
   *
   * @return the logical name.
   */
  public String getLogicalName()
  {
    return logicalName;
  }

  /**
   * Returns the AWT font.
   *
   * @return the AWT font.
   */
  public FontDefinition getFontDefinition()
  {
    return awtFont;
  }

  /**
   * Returns the iText BaseFont.
   *
   * @return the itext BaseFont.
   */
  public BaseFont getBaseFont()
  {
    return baseFont;
  }

  /**
   * Returns the font height.
   *
   * @return the font height.
   */
  public float getFontHeight()
  {
    return getFontDefinition().getFont().getSize2D();
  }
}
