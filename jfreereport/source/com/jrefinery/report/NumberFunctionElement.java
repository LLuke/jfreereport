/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: NumberFunctionElement.java,v 1.11 2002/09/05 09:34:53 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 10-May-2002 : Removed all complex constructors
 * 20-May-2002 : Declared deprecated. This class is no longer used. The ItemFactory produces
 *               TextElements instead which get different filters attached.
 * 04-Jun-2002 : Documentation.
 * 21-Jul-2002 : Cloning-Bug fixed.
 * 05-Sep-2002 : Documentation
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.NumberFormatFilter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Presentation Element for numerical functions.
 * <p>
 * To produce the same functionality use a TextElement and add a NumberFormatFilter and
 * a FunctionDataSource to the element.
 *
 * @author TM
 *
 * @deprecated Use a plain text element and add a number format filter to the element
 * and add a function datasource to the filter.
 */
public class NumberFunctionElement extends FunctionElement
{
  /** The number format filter. */
  private NumberFormatFilter filter;

  /**
   * Constructs a number element using float coordinates. This Element uses an DecimalFormat to
   * transform the numbers into a string by default.
   */
  public NumberFunctionElement ()
  {
    filter = new NumberFormatFilter ();
    setDecimalFormatString (null);
    setDataSource (filter);
    filter.setDataSource (getFunctionDataSource ());
  }

  /**
   * Returns the numberformater of this element. Initialy this is a DecimalFormat.
   *
   * @return the formatter.
   */
  public NumberFormat getFormatter ()
  {
    return filter.getNumberFormat ();
  }

  /**
   * Sets the NumberFormater for this element. The formater must not be null, or a
   * NullPointerException is thrown.
   *
   * @param nf  the formatter.
   */
  public void setFormatter (NumberFormat nf)
  {
    if (nf == null)
    {
      throw new NullPointerException ("NumberFormat may not be null");
    }

    filter.setNumberFormat (nf);
  }

  /**
   * Defines the numberformater for this element using a DecimalFormat and initializing it
   * with the given format string. If the format string is null, a reasonable default value
   * is choosen.
   *
   * @param df  the format string.
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
   * Clones this Element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    if ((getDataSource () instanceof NumberFormatFilter) == false)
    {
      throw new CloneNotSupportedException ("Modified function element is not clonable");
    }
    NumberFunctionElement e = (NumberFunctionElement) super.clone ();
    e.filter = (NumberFormatFilter) filter.clone ();
    e.setDataSource (e.filter);
    e.filter.setDataSource (e.getFunctionDataSource ());
    return e;
  }

}
