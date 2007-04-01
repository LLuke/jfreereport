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

import org.jfree.fonts.io.FontDataInputSource;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontRecord;

/**
 * Creation-Date: 09.11.2005, 20:32:33
 *
 * @author Thomas Morgner
 */
public class MinimalFontRecord implements FontRecord
{
  private boolean bold;
  private boolean italics;
  private boolean embeddable;
  private String fontFile;
  private String fontName;

  public MinimalFontRecord(final String fontName,
                           final String fontFile,
                           final boolean bold,
                           final boolean italics,
                           final boolean embeddable)
  {
    this.fontName = fontName;
    this.fontFile = fontFile;
    this.bold = bold;
    this.italics = italics;
    this.embeddable = embeddable;
  }

  public boolean isBold()
  {
    return bold;
  }

  public boolean isItalic()
  {
    return italics;
  }

  /**
   * Returns tue, if this font's italic mode is in fact some sort of being
   * oblique. An oblique font's glyphs are sheared, but they are not made to
   * look more script like.
   *
   * iText or JFreeReport's current code does not use this hint, so it is safe
   * to return false here.
   *
   * @return always false
   */
  public boolean isOblique()
  {
    return false;
  }

  public String getFontFile()
  {
    return fontFile;
  }

  public FontFamily getFamily()
  {
    return null;
  }

  public boolean isEmbeddable()
  {
    return embeddable;
  }

  public String getName()
  {
    return fontName;
  }

  public String[] getAllNames()
  {
    return new String[] { fontName };
  }

  public String getVariant()
  {
    return "";
  }

  public String[] getAllVariants()
  {
    return new String[] { "" };
  }

  public FontDataInputSource getFontInputSource()
  {
    return null;
  }

  public FontIdentifier getIdentifier()
  {
    return null;
  }
}
