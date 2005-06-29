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
 * ------------------------------
 * Barcode39ExtEncoder.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: $
 *
 * Changes (from YYYY-MM-DD)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.base;

public class Barcode39ExtEncoder extends Barcode39Encoder
{
  /**
   * New characters allowed
   */
  public static final String[] EXTENDED_TABLE = {
    "%U",
    "$A", "$B", "$C", "$D", "$E", "$F", "$G", "$H", "$I", "$J", "$K", "$L", "$M", "$N", "$O", "$P", "$Q", "$R", "$S", "$T", "$U", "$V", "$W", "$X", "$Y", "$Z",
    "%A", "%B", "%C", "%D", "%E",
    " ",
    "/A", "/B", "/C", "/D", "/E", "/F", "/G", "/H", "/I", "/J", "/K", "/L", "-", ".", "/O",
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
    "/Z",
    "%F", "%G", "%H", "%I", "%J", "%V",
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
    "%K", "%L", "%M", "%N", "%O", "%W",
    "+A", "+B", "+C", "+D", "+E", "+F", "+G", "+H", "+I", "+J", "+K", "+L", "+M", "+N", "+O", "+P", "+Q", "+R", "+S", "+T", "+U", "+V", "+W", "+X", "+Y", "+Z",
    "%P", "%Q", "%R", "%S", "%T",
  };

  /**
   * Tells if the character is valid in this symbology.
   *
   * @param character The character to check.
   * @return Boolean.
   */
  public boolean isValid (char character)
  {
    if (character >= 0 && character <= 127)
    {
      return true;
    }
    return false;
  }

  /**
   * Encodes the character in symbols.
   *
   * @param character The valid charAt.
   * @return The symbols.
   */
  public byte[][] encode (char character)
  {
    final String s = EXTENDED_TABLE[character];
    final byte[][] symbols = new byte[s.length()][];

    for (int i = 0; i < s.length(); i++)
    {
      symbols[i] = super.encode(s.charAt(i))[0];
    }

    return symbols;
  }
}
