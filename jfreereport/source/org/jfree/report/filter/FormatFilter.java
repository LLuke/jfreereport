/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * -----------------
 * FormatFilter.java
 * -----------------
 * (C)opyright 2002, by Object Refinery Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: FormatFilter.java,v 1.3 2004/05/07 08:24:42 mungady Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 * 03-Jul-2002 : Cloneable implemented
 */

package org.jfree.report.filter;

import java.io.Serializable;
import java.text.Format;

/**
 * The base class for filters that format data.  Data is received from a DataSource and
 * formatted. The data source might be a field in the TableModel or a report function, or
 * even another format filter (since filters implement the DataSource interface).
 * <p/>
 * Formating is done by a java.text.Format object. This filter will always return a String
 * object on getValue().
 * <p/>
 * If the formater does not understand the object returned by the defined datasource, the
 * defined null value is returned.
 * <p/>
 * The nullValue is set to "-" by default.
 *
 * @author Thomas Morgner
 */
public class FormatFilter implements DataFilter, Serializable
{
  /**
   * The format used to create the string representation of the data.
   */
  private Format format;

  /**
   * The datasource from where the data is obtained.
   */
  private DataSource datasource;

  /**
   * The string used to represent null.
   */
  private String nullvalue;

  /**
   * Default constructor.
   */
  public FormatFilter ()
  {
    nullvalue = null;
  }

  /**
   * Sets the format for the filter.
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   */
  public void setFormatter (final Format format)
  {
    if (format == null)
    {
      throw new NullPointerException();
    }
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
   * Returns the formatted string. The value is read using the data source given and
   * formated using the formatter of this object. The formating is guaranteed to completly
   * form the object to an string or to return the defined NullValue.
   * <p/>
   * If format, datasource or object are null, the NullValue is returned.
   *
   * @return The formatted value.
   */
  public Object getValue ()
  {
    final Format f = getFormatter();
    if (f == null)
    {
      return getNullValue();
    }

    final DataSource ds = getDataSource();
    if (ds == null)
    {
      return getNullValue();
    }

    final Object o = ds.getValue();
    if (o == null)
    {
      return getNullValue();
    }

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
  public void setNullValue (final String nullvalue)
  {
    if (nullvalue == null)
    {
      throw new NullPointerException();
    }
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
  public void setDataSource (final DataSource ds)
  {
    if (ds == null)
    {
      throw new NullPointerException();
    }
    this.datasource = ds;
  }

  /**
   * Clones the filter.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final FormatFilter f = (FormatFilter) super.clone();
    if (datasource != null)
    {
      f.datasource = (DataSource) datasource.clone();
    }
    if (format != null)
    {
      f.format = (Format) format.clone();
    }
    return f;
  }

}

