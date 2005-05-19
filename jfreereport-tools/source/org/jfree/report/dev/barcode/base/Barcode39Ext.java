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
 * Barcode39Ext.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: $
 *
 * Changes (from 2005-05-17) (CP)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.base;

import org.jfree.report.dev.barcode.BarcodeException;

/**
 * Encodes a string into code39 extended specifications
 * <p/>
 * Symbols allowed: 0-9 A-Z a-z $ % * + - . / space non-printables (ASCII128)<br> Start
 * character: yes, in the symbols table (*)<br> Stop character: yes, in the symbols table
 * (*)<br> Check character: available
 *
 * @author Mimil
 */
public class Barcode39Ext extends Barcode39
{

  /**
   * New characters allowed
   */
  protected static final String[] EXTENDEDTABLE = {
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
    "%P", "%Q", "%R", "%S", "%T"

  };

  public Barcode39Ext (final String code)
  {
    super(transform(code));
  }

  /**
   * Transforms <code>code</code> into code39 readable characters
   *
   * @param code The old code
   * @return The new code
   *
   * @throws NullPointerException If the input code is null.
   * @throws BarcodeException     If the code contains illegal characters.
   */
  public static String transform (final String code)
  {
    if (code == null)
    {
      throw new NullPointerException("The input code to convert cannot be null.");
    }

    if (!isValideCode39ExtendedInput(code))
    {
      throw new BarcodeException("The code is not valid according to the code39 extended specification.");
    }

    final StringBuffer buf = new StringBuffer();
    int i = 0;

    try
    {
      for (i = 0; i < code.length(); i++)
      {
        buf.append(EXTENDEDTABLE[code.charAt(i)]);
      }
    }
    catch (Exception exp)
    {
      throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 39 extended.");
    }

    return buf.toString();
  }

  /**
   * Tells if the input code passed as argument is a valid code for the code 39 extended
   * specifications.
   *
   * @param code The input code to check.
   * @return Boolean
   *
   * @throws NullPointerException If <code>code</code> is null.
   */
  public static boolean isValideCode39ExtendedInput (final String code)
  {
    if (code == null)
    {
      throw new NullPointerException("Cannot check a null input code.");
    }

    for (int i = 0; i < code.length(); i++)
    {
      final char c = code.charAt(i);
      if (c < 0)
      {
        return false;
      }

      if (c > 127)
      {
        return false;
      }
    }

    return true;
  }
}
