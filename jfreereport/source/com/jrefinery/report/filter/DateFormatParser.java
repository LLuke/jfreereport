/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * DateFormatParser.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.filter;

import java.text.SimpleDateFormat;
import java.text.Format;
import java.text.DateFormat;
import java.util.Date;

/**
 * Parses a String into a java.util.Date. The string is read from the given datasource
 * and then parsed by the dateformat contained in this FormatParser.
 * <p>
 */
public class DateFormatParser extends FormatParser
{
  /**
   * DefaultConstructor
   * <p>
   * initializes this format
   */
  public DateFormatParser ()
  {
    setDateFormat(DateFormat.getInstance());
  }

  /**
   * returns the format for this filter. The format object is returned as DateFormat.
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   */
  public DateFormat getDateFormat ()
  {
    return (DateFormat) getFormatter ();
  }

  /**
   * Sets the format for the filter.
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   */
  public void setDateFormat (DateFormat format)
  {
    super.setFormatter(format);
  }

  /**
   * Sets the format for the filter. The formater is required to be of type DateFormat.
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   * @throws ClassCastException if an invalid formater is set.
   */
  public void setFormatter (Format format)
  {
    super.setFormatter((DateFormat) format);
  }

  /**
   * Sets the value that will be displayed if the data source supplies a null value.
   * The nullValue itself can be null to cover the case when no reasonable default value
   * can be defined.
   * <p>
   * The null value for date format parsers is required to be either null or a java.util.Date.
   *
   * @param nullvalue the nullvalue returned when parsing failed.
   * @throws ClassCastException if the value is no date or not null.
   */
  public void setNullValue (Object nullvalue)
  {
    super.setNullValue ((Date)nullvalue);
  }

  /**
   * Checks whether the given value is already a valid result. IF the datasource already returned
   * a valid value, and no parsing is required, a parser can skip the parsing process by returning
   * true in this function.
   *
   * @returns true, if the given value is already an instance of date.
   */
  protected boolean isValidOutput (Object o)
  {
    return o instanceof Date;
  }
}
