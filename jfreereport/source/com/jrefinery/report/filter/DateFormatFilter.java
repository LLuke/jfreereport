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
 * -----------------------
 * DateFormatFilter.java
 * -----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DateFormatFilter.java,v 1.3 2002/06/06 16:00:59 mungady Exp $
 *
 * Changes
 * -------
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 *
 */
package com.jrefinery.report.filter;

import java.text.SimpleDateFormat;
import java.text.Format;
import java.text.DateFormat;

/**
 * A filter that creates string from dates. This filter will format java.util.
 * Date objects using a java.text.DateFormat to create the string representation for
 * the date obtained from the datasource.
 * <p>
 * If the object read from the datasource is no date, the NullValue defined by
 * setNullValue(Object) is returned.
 *
 * @see java.text.DateFormat
 */
public class DateFormatFilter extends FormatFilter
{
  /**
   * Default constructor.  Creates a new filter using the default date format for the current
   * locale.
   */
  public DateFormatFilter ()
  {
    setFormatter(DateFormat.getInstance());
  }

  /**
   * Returns the date format object.
   *
   * @return The date format object.
   */
  public DateFormat getDateFormat ()
  {
    return (DateFormat) getFormatter ();
  }

  /**
   * Sets the date format for the filter.
   *
   * @param format The format.
   * @throws NullPointerException if the format given is null
   */
  public void setDateFormat (DateFormat format)
  {
    super.setFormatter(format);
  }

  /**
   * Sets the formatter.
   *
   * @param format The format.
   * @throws ClassCastException if the format given is no DateFormat
   * @throws NullPointerException if the format given is null
   */
  public void setFormatter (Format format)
  {
    super.setFormatter((DateFormat) format);
  }

}
