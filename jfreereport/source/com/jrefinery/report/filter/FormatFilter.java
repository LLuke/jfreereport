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
 * FormatFilter.java
 * -----------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 *
 */

package com.jrefinery.report.filter;

import java.text.Format;

/**
 * The base class for filters that format data.  Data is received from a DataSource and formatted.
 * The data source might be a field in the TableModel or a report function, or even another
 * format filter (since this class implements the DataSource interface).
 */
public class FormatFilter implements DataFilter
{
  /** The format. */
  private Format format;

  /** The datasource. */
  private DataSource datasource;

  /** The string used to represent null. */
  private String nullvalue;

  /**
   * Default constructor.
   */
  protected FormatFilter ()
  {
  }

  /**
   * Sets the format for the filter.
   *
   * @param format The format.
   */
  public void setFormatter (Format format)
  {
    if (format == null) throw new NullPointerException();
    this.format = format;
  }

  /**
   * Returns the format for the filter.
   *
   * @return The format.
   */
  public Format getFormatter ()
  {
    return this.format;
  }

  /**
   * Returns the formatted value.
   *
   * @return The formatted value.
   */
  public Object getValue ()
  {
    Format f = getFormatter();
    if (f == null) return getNullValue();

    DataSource ds = getDataSource();
    if (ds == null) return getNullValue();

    Object o = ds.getValue();
    if (o == null)  return getNullValue();

    try
    {
      return f.format(o);
    }
    catch (IllegalArgumentException e)
    {
      return getNullValue();
    }
  }

  /**
   * Sets the value that will be displayed if the data source supplies a null value.
   *
   * @param nullvalue The string.
   */
  public void setNullValue (String nullvalue)
  {
    if (nullvalue == null) throw new NullPointerException();
    this.nullvalue = nullvalue;
  }

  /**
   * Returns the string representing a null value from the data source.
   *
   * @return The string.
   */
  public String getNullValue ()
  {
    return nullvalue;
  }

  /**
   * Returns the data source for the filter.
   *
   * @return The data source.
   */
  public DataSource getDataSource ()
  {
    return datasource;
  }

  /**
   * Sets the data source.
   *
   * @param ds The data source.
   */
  public void setDataSource (DataSource ds)
  {
    if (ds == null) throw new NullPointerException();
    this.datasource = ds;
  }

}

