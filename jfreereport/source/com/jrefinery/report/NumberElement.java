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
 * ------------------
 * NumberElement.java
 * ------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: NumberElement.java,v 1.3 2002/05/21 23:06:18 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 10-May-2002 : removed all but the default constructor. Added accessor functions for all properties.
 * 20-May-2002 : Declared deprecated. This class is no longer used. The ItemFactory produces
 *               TextElements instead which get different filters attached.
 *
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DataFilter;
import com.jrefinery.report.filter.NumberFormatFilter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A data element that displays numerical information.
 */
public class NumberElement extends DataElement
{
  /** The formatting object for this data element. */
  private NumberFormatFilter formatter;

  /**
   * Constructs a number element using float coordinates.
   * @param name The name of the element.
   * @param x The x-coordinate of the element (within its band).
   * @param y The y-coordinate of the element (within its band).
   * @param w The width of the element.
   * @param h The height of the element.
   * @param field The name of the field used to populate this element with data.
   * @param format The format string for the number.
   */
  public NumberElement ()
  {
    formatter = new NumberFormatFilter();
    setDecimalFormatString (null);
    DataFilter df = getTextFilter ();
    df.setDataSource (formatter);
    formatter.setDataSource (getReportDataSource ());
  }

  /**
   * Returns the numberformater of this element. Initialy this is a DecimalFormat.
   */
  public NumberFormat getFormatter ()
  {
    return formatter.getNumberFormat ();
  }

  /**
   * Sets the NumberFormater for this element. The formater must not be null, or a
   * NullPointerException is thrown.
   */
  public void setFormatter (NumberFormat nf)
  {
    if (nf == null)
      throw new NullPointerException ("NumberFormat may not be null");

    this.formatter.setNumberFormat (nf);
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
}
