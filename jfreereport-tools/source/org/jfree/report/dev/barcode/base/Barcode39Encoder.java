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
 * Barcode39Encoder.java
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

import org.jfree.report.dev.barcode.BarcodeEncoder;

public class Barcode39Encoder implements BarcodeEncoder
{
  /**
   * Characters allowed.
   */
  public static final String CHARTABLE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*";

  /**
   * Symbols allowed
   */
  public static final byte TABLE[][] = {
    {0, 0, 0, 1, 1, 0, 1, 0, 0}, //'0'
    {1, 0, 0, 1, 0, 0, 0, 0, 1},
    {0, 0, 1, 1, 0, 0, 0, 0, 1},
    {1, 0, 1, 1, 0, 0, 0, 0, 0},
    {0, 0, 0, 1, 1, 0, 0, 0, 1},
    {1, 0, 0, 1, 1, 0, 0, 0, 0},
    {0, 0, 1, 1, 1, 0, 0, 0, 0},
    {0, 0, 0, 1, 0, 0, 1, 0, 1},
    {1, 0, 0, 1, 0, 0, 1, 0, 0},
    {0, 0, 1, 1, 0, 0, 1, 0, 0},
    {1, 0, 0, 0, 0, 1, 0, 0, 1},
    {0, 0, 1, 0, 0, 1, 0, 0, 1},
    {1, 0, 1, 0, 0, 1, 0, 0, 0},
    {0, 0, 0, 0, 1, 1, 0, 0, 1},
    {1, 0, 0, 0, 1, 1, 0, 0, 0},
    {0, 0, 1, 0, 1, 1, 0, 0, 0},
    {0, 0, 0, 0, 0, 1, 1, 0, 1},
    {1, 0, 0, 0, 0, 1, 1, 0, 0},
    {0, 0, 1, 0, 0, 1, 1, 0, 0},
    {0, 0, 0, 0, 1, 1, 1, 0, 0},
    {1, 0, 0, 0, 0, 0, 0, 1, 1},
    {0, 0, 1, 0, 0, 0, 0, 1, 1},
    {1, 0, 1, 0, 0, 0, 0, 1, 0},
    {0, 0, 0, 0, 1, 0, 0, 1, 1},
    {1, 0, 0, 0, 1, 0, 0, 1, 0},
    {0, 0, 1, 0, 1, 0, 0, 1, 0},
    {0, 0, 0, 0, 0, 0, 1, 1, 1},
    {1, 0, 0, 0, 0, 0, 1, 1, 0},
    {0, 0, 1, 0, 0, 0, 1, 1, 0},
    {0, 0, 0, 0, 1, 0, 1, 1, 0},
    {1, 1, 0, 0, 0, 0, 0, 0, 1},
    {0, 1, 1, 0, 0, 0, 0, 0, 1},
    {1, 1, 1, 0, 0, 0, 0, 0, 0},
    {0, 1, 0, 0, 1, 0, 0, 0, 1},
    {1, 1, 0, 0, 1, 0, 0, 0, 0},
    {0, 1, 1, 0, 1, 0, 0, 0, 0},
    {0, 1, 0, 0, 0, 0, 1, 0, 1},
    {1, 1, 0, 0, 0, 0, 1, 0, 0},
    {0, 1, 1, 0, 0, 0, 1, 0, 0},
    {0, 1, 0, 1, 0, 1, 0, 0, 0},
    {0, 1, 0, 1, 0, 0, 0, 1, 0},
    {0, 1, 0, 0, 0, 1, 0, 1, 0},
    {0, 0, 0, 1, 0, 1, 0, 1, 0},
    {0, 1, 0, 0, 1, 0, 1, 0, 0}   //'*'
  };

  /**
   * Tells if the character is valid in this symbology.
   *
   * @param character The character to check.
   * @return Boolean.
   */
  public boolean isValid (char character)
  {
    final int index = CHARTABLE.indexOf(character);
    if (index != -1 && index != CHARTABLE.length())
    {
      return true;
    }
    return false;
  }

  /**
   * Encodes the charAt in symbols.
   *
   * @param character The valid charAt.
   * @return The symbols.
   */
  public byte[][] encode (char character)
  {
    return new byte[][]{TABLE[CHARTABLE.indexOf(character)]};
  }

  /**
   * Returns the weight of this charAt. It is usualy needed for checksum compuation.
   *
   * @param character The valid charAt.
   * @return Its weight.
   */
  public int weight (char character)
  {
    return CHARTABLE.indexOf(character);
  }

  /**
   * Returns the charAt at a specified index from the characters table.
   *
   * @param index The charAt index.
   * @return The charAt.
   */
  public char charAt (int index)
  {
    return CHARTABLE.charAt(index);
  }
}
