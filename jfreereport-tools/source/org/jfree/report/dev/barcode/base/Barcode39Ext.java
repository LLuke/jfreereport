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
 * $Id: Barcode39Ext.java,v 1.1 2005/05/19 00:24:08 mimil Exp $
 *
 * Changes (from 2005-05-17) (CP)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.base;


/**
 * Encodes a string into code39 extended specifications
 * <p/>
 * Symbols allowed: 0-9 A-Z a-z $ % * + - . / space non-printables (ASCII128)<br> Start
 * charAt: yes, in the symbols table (*)<br> Stop charAt: yes, in the symbols table
 * (*)<br> Check charAt: available
 *
 * @author Mimil
 */
public class Barcode39Ext extends Barcode39
{


  public Barcode39Ext ()
  {
    super();
    setEncoder(new Barcode39ExtEncoder());
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
  public boolean isValideInput (final String code)
  {
    if (code == null)
    {
      throw new NullPointerException("Cannot check a null input code.");
    }

    for (int i = 0; i < code.length(); i++)
    {
      boolean b = getEncoder().isValid(code.charAt(i));

      if (!b)
      {
        return false;
      }
    }

    return true;
  }
}
