/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * --------------------------
 * NumberFunctionElement.java
 * --------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: NumberFunctionElement.java,v 1.1.1.1 2002/04/25 17:02:15 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 10-May-2002 : Removed all complex constructors
 */

package com.jrefinery.report;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Presentation Element for numerical functions.
 */
public class NumberFunctionElement extends FunctionElement
{

  /** Useful constant for zero. */
  private static final Number ZERO = new Double (0.0);

  /** The formatting object for this data element. */
  private NumberFormat formatter;

  /**
   * Constructs a number element using float coordinates.
   */
  public NumberFunctionElement ()
  {
    setDecimalFormatString(null);
  }

  /**
   * Returns the numberformater of this element. Initialy this is a DecimalFormat.
   */
  public NumberFormat getFormatter ()
  {
    return formatter;
  }

  /**
   * Sets the NumberFormater for this element. The formater must not be null, or a
   * NullPointerException is thrown.
   */
  public void setFormatter (NumberFormat nf)
  {
    if (nf == null)
      throw new NullPointerException ("NumberFormat may not be null");

    this.formatter = nf;
  }

  /**
   * Defines the numberformater for this element using a DecimalFormat and initializing it
   * with the given format string. If the format string is null, a reasonable default value
   * is choosen.
   */
  public void setDecimalFormatString (String df)
  {
    if (df == null)
    {
      df = "0";
    }
    setFormatter (new DecimalFormat (df));
  }

  /**
   * Returns a formatted version of the number.
   * @return A formatted version of the number.
   */
  public String getFormattedText ()
  {

    String result = "";

    Object value = getValue ();
    if (value instanceof Number)
    {
      result = getFormatter ().format (value);
    }
    else
    {
      String.valueOf (value);
    }

    return result;

  }

}