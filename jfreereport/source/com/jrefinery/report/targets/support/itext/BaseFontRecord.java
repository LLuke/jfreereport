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
 * ------------------
 * BaseFontRecord.java
 * ------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BaseFontRecord.java,v 1.1 2003/02/01 22:10:36 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 * 01-Feb-2002 : Refactoring moved this class from package
 *               com.jefinery.report.targets.pageable.output
 *
 */

package com.jrefinery.report.targets.support.itext;

import com.jrefinery.report.targets.FontDefinition;
import com.lowagie.text.pdf.BaseFont;

/**
 * A PDF font record.
 *
 * @author Thomas Morgner
 */
public class BaseFontRecord
{
  /** The AWT font. */
  private FontDefinition awtFont;
  
  /** The iText base font. */
  private BaseFont baseFont;
  
  /** The logical name. */
  private String logicalName;
  
  /** Embedded? */
  private boolean embedded;
  
  /** The encoding. */
  private String encoding;

  /** 
   * Creates a new font record.
   */
  public BaseFontRecord()
  {
  }

  /**
   * Creates a font record key.
   *
   * @return the font record key.
   */
  public BaseFontRecordKey createKey ()
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
    return encoding;
  }

  /**
   * Sets the encoding.
   *
   * @param encoding  the encoding.
   */
  public void setEncoding(String encoding)
  {
    this.encoding = encoding;
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
   * Sets the flag that controls whether or not this font will be embedded in the PDF output.
   *
   * @param embedded  the flag.
   */
  public void setEmbedded(boolean embedded)
  {
    this.embedded = embedded;
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
   * Sets the logical name of the font.
   *
   * @param logicalName  the logical name.
   */
  public void setLogicalName(String logicalName)
  {
    this.logicalName = logicalName;
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
   * Sets the AWT font.
   *
   * @param awtFont  the AWT font.
   */
  public void setFontDefinition(FontDefinition awtFont)
  {
    this.awtFont = awtFont;
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
   * Sets the iText BaseFont.
   *
   * @param baseFont  the iText BaseFont.
   */
  public void setBaseFont(BaseFont baseFont)
  {
    this.baseFont = baseFont;
  }

  /**
   * Returns the font height.
   *
   * @return the font height.
   */
  public float getFontHeight()
  {
    if (awtFont == null) 
    {
      return -1;
    }
    return getFontDefinition().getFont().getSize2D();
  }
}
