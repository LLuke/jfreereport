/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libfonts/
 * Project Lead:  Thomas Morgner;
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
 * FontRecord.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.registry;

import java.io.Serializable;

import org.jfree.fonts.io.FontDataInputSource;

/**
 * Creation-Date: 07.11.2005, 19:07:09
 *
 * @author Thomas Morgner
 */
public interface FontRecord extends Serializable
{
  /**
   * Returns the family for this record.
   *
   * @return the font family.
   */
  public FontFamily getFamily();

  /**
   * Returns true, if this font corresponds to a bold version of the font. A
   * font that does not provide a bold face must emulate the boldness using
   * other means.
   *
   * @return true, if the font provides bold glyphs, false otherwise.
   */
  public boolean isBold();

  /**
   * Returns true, if this font includes italic glyphs. Italics is different
   * from oblique, as certain glyphs (most notably the lowercase 'f') will
   * have a different appearance, making the font look more like a script font.
   *
   * @return true, if the font is italic.
   */
  public boolean isItalic ();

  /**
   * Returns tue, if this font's italic mode is in fact some sort of being oblique.
   * An oblique font's glyphs are sheared, but they are not made to look more
   * script like.
   *
   * @return true, if the font is oblique. All italic fonts are also oblique.
   */
  public boolean isOblique ();

  /**
   * Returns the file name used to load the font.
   *
   * @return this is needed for iText.
   */
  public String getFontFile ();

  public FontDataInputSource getFontInputSource();

  public boolean isEmbeddable();

  public String getName ();

  public String[] getAllNames ();

  public String getVariant ();

  public String[] getAllVariants ();

  public FontIdentifier getIdentifier();
}
