/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/libfonts/
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
 * MacintoshPlatformIdentifier.java
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
package org.jfree.fonts.truetype.mappings;

/**
 * Creation-Date: 07.11.2005, 15:11:56
 *
 * @author Thomas Morgner
 */
public class MacintoshPlatformIdentifier extends PlatformIdentifier
{
  private static final String[] ENCODINGS = {
          "MacRoman", // Roman
          null,       // Japan
          null,       // Traditional Chinese
          null,       // Korean
          "MacArabic",// Arabic
          "MacHebrew",// Hebrew
          "MacGreek", // Greek
          "MacCyrillic", // Russian
          null,       // RSymbol
          null,       // Devanagari
          null,       // Gurmukhi
          null,       // Gujarati
          null,       // Oriya
          null,       // Bengali
          null,       // Tamil
          null,       // Telugu
          null,       // Kannada
          null,       // Malayalam
          null,       // Sinhalese
          null,       // Burmese
          null,       // Khmer
          null,       // Thai
          null,       // Laotian
          null,       // Georgian
          null,       // Armenian
          null,       // Simplified Chinese
          null,       // Tibetan
          null,       // Mongolian
          null,       // Geez
          null,       // Slavic
          null,       // Vietnamese
          null,       // Sindhi
  };


  public MacintoshPlatformIdentifier()
  {
    super(1);
  }

  public String getEncoding(int encodingId, int language)
  {
    if (encodingId < ENCODINGS.length)
    {
      final String encoding = ENCODINGS[encodingId];
      if (encoding != null)
      {
        return encoding;
      }
    }
    return "MacRoman";
  }
}
