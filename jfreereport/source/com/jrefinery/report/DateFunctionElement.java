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
 * ------------------------
 * DateFunctionElement.java
 * ------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DateFunctionElement.java,v 1.11 2002/09/05 08:31:51 taqua Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 10-May-2002 : Removed all complex constructors.
 * 20-May-2002 : Declared deprecated. This class is no longer used. The ItemFactory produces
 *               TextElements instead which get different filters attached.
 * 04-Jul-2002 : Serializable and Cloneable
 * 05-Sep-2002 : Documentation
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DateFormatFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Presentation element for date functions.
 *
 * @author TM
 *
 * @deprecated form this element by stacking it together by using filters
 */
public class DateFunctionElement extends FunctionElement
{

  /** The formatting object for this data element. */
  private DateFormatFilter formatter;

  /**
   * Constructs a date element using float coordinates.
   *
   * @deprecated form this element by stacking it together by using filters
   */
  public DateFunctionElement ()
  {
    formatter = new DateFormatFilter ();
    setFormatString (null);
    setDataSource (formatter);
    formatter.setDataSource (getFunctionDataSource ());
  }

  /**
   * Sets the format of the element to SimpleDate using the given formatString.
   *
   * @param s  the format string.
   *
   * @deprecated form this element by stacking it together by using filters
   */
  public void setFormatString (String s)
  {
    if (s == null)
    {
      setFormatter (new SimpleDateFormat ());
    }
    else
    {
      setFormatter (new SimpleDateFormat (s));
    }
  }

  /**
   * Returns the current formatter for this element.
   * <P>
   * This function will never return null.
   *
   * @return The formatter.
   *
   * @deprecated form this element by stacking it together by using filters.
   */
  public DateFormat getFormatter ()
  {
    return formatter.getDateFormat ();
  }

  /**
   * Defines the current formatter for the element.
   *
   * @param format  the formatter (null not permitted).
   *
   * @deprecated form this element by stacking it together by using filters
   */
  public void setFormatter (DateFormat format)
  {
    if (format == null)
    {
      throw new NullPointerException ("Given format may not be null");
    }
    this.formatter.setDateFormat (format);
  }

  /**
   * Clones this Element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    if ((getDataSource () instanceof DateFormatFilter) == false)
    {
      throw new CloneNotSupportedException ("Modified function element is not clonable: "
                                            + getDataSource());
    }
    DateFunctionElement e = (DateFunctionElement) super.clone ();
    e.formatter = (DateFormatFilter) formatter.clone();
    e.setDataSource (e.formatter);
    e.formatter.setDataSource (e.getFunctionDataSource ());
    return e;
  }

}
