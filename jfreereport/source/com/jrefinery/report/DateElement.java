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
 * ----------------
 * DateElement.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: DateElement.java,v 1.12 2002/09/05 08:31:51 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 10-May-2002 : Removed all compelex constructors
 * 20-May-2002 : Declared deprecated. This class is no longer used. The ItemFactory produces
 *               TextElements instead which get different filters attached.
 * 04-May-2002 : cleanup, documentation.
 * 04-Jul-2002 : Serializable and Cloneable
 * 05-Sep-2002 : Documentation
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DateFormatFilter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A data element that handles java.util.Date objects.
 *
 * @author DG
 *
 * @deprecated form this element by stacking it together by using filters
 */
public class DateElement extends DataElement
{

  /** The formatting object for this data element. */
  private DateFormatFilter formatter;

  /**
   * Constructs a date element using float coordinates.
   *
   * @deprecated form this element by stacking it together by using filters
   */
  public DateElement ()
  {
    formatter = new DateFormatFilter ();
    setFormatString (null);
    setDataSource (formatter);
    formatter.setDataSource (getReportDataSource ());
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
   * Returns the formatter for the element.
   * <P>
   * This function will never return null.
   *
   * @return the current formatter for this element.
   *
   * @deprecated form this element by stacking it together by using filters
   */
  public DateFormat getFormatter ()
  {
    return formatter.getDateFormat ();
  }

  /**
   * Defines the current formatter for the element. If the formater is null,
   * an exception is thrown.
   *
   * @param format  the formatter.
   *
   * @deprecated form this element by stacking it together by using filters
   */
  public void setFormatter (DateFormat format)
  {
    if (format == null)
    {
      throw new NullPointerException ("Given format may not be null");
    }
    if (format instanceof SimpleDateFormat)
    {
      this.formatter.setDateFormat ((SimpleDateFormat) format);
    }
    else
    {
      throw new ClassCastException ("DEPRECATION: Only simple dateformat supported by default"
                                   + " implementation");
    }
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
      throw new CloneNotSupportedException ("Modified function element is not clonable");
    }
    DateElement e = (DateElement) super.clone ();
    e.formatter = (DateFormatFilter) formatter.clone();
    e.setDataSource (e.formatter);
    e.formatter.setDataSource (e.getReportDataSource());
    return e;
  }

}
