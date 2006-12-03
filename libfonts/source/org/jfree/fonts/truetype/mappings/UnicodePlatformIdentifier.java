/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.truetype.mappings;

/**
 * Creation-Date: 06.11.2005, 21:44:21
 *
 * @author Thomas Morgner
 */
public class UnicodePlatformIdentifier extends PlatformIdentifier
{
  public UnicodePlatformIdentifier()
  {
    super (0);
  }

  /**
   * According to the Apple OpenType specifications, all Unicode characters
   * must be encoded using UTF-16. Depending on the encodingId, some blocks
   * may be interpreted differently. LibFont ignores that and uses the
   * Java-Default UTF-16 mapping.
   *
   * @param encodingId
   * @param language
   * @return the encoding, always "UTF-16"
   * @see http://developer.apple.com/fonts/TTRefMan/RM06/Chap6name.html#ID
   */
  public String getEncoding(int encodingId, int language)
  {
    return "UTF-16";
  }
}
