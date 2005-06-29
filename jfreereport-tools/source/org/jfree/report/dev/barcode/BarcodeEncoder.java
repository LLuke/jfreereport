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
 * BarcodeEncoder.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: $
 *
 * Changes (from 2005-07-05)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode;

//todo: use a state design pattern, or simply pass the barcode as argument to have its states by accessing
//todo: its methodes

/**
 *
 */
public interface BarcodeEncoder
{
  /**
   * Tells if the character is valid in this symbology.
   *
   * @param character The character to check.
   * @return Boolean.
   */
  public boolean isValid (char character);

  /**
   * Encodes the character in a symbol where <code>byte[]</code> is a symbol and the array
   * is a list of symbols because a character can be translated in more than one symbol.
   *
   * @param character The valid character.
   * @return The symbols.
   */
  public byte[][] encode (char character);

  /**
   * Returns the weight of this character. It is usualy needed for checksum computations.
   *
   * @param character The valid character.
   * @return Its weight.
   */
  public int weight (char character);

  /**
   * Returns the character at a specified index from the characters table.
   *
   * @param index The character index.
   * @return The character.
   */
  public char charAt (int index);
}
