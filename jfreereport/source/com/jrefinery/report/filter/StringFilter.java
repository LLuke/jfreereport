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
 * StringFilter.java
 * -----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StringFilter.java,v 1.5 2002/06/06 16:00:59 mungady Exp $
 *
 * Changes
 * -------
 * 04-Jun-2002 : Officially documented. Added a public constructor and set the default null
 *               value.
 * 06-Jun-2002 : Added Javadoc comments (DG);
 *
 */

package com.jrefinery.report.filter;

/**
 * A filter that returns the value from a data source as a String.
 */
public class StringFilter implements DataFilter
{
  /** The data source for this filter. */
  private DataSource source;

  /** The string used to represent a null value. */
  private String nullvalue;

  /**
   * Default constructor.
   */
  public StringFilter ()
  {
    nullvalue = "null";
  }

  /**
   * Sets the string used to represent a null value.
   *
   * @param nullvalue The null value.
   */
  public void setNullValue (String nullvalue)
  {
    if (nullvalue == null) throw new NullPointerException();
    this.nullvalue = nullvalue;
  }

  /**
   * Returns the string used to represent a null value.
   *
   * @return The string.
   */
  public String getNullValue ()
  {
    return nullvalue;
  }

  /**
   * Returns the value obtained from the data source.
   * <P>
   * The filter ensures that the returned value is a String, even though the return type is Object.
   *
   * @return The string.
   */
  public Object getValue ()
  {
    DataSource ds = getDataSource();
    if (ds == null)
    {
      return getNullValue();
    }
    Object o = ds.getValue();
    if (o == null) return getNullValue();
    if (o instanceof String) return (String) o;
    return String.valueOf(o);
  }

  /**
   * Returns the data source for this filter.
   *
   * @return The data source.
   */
  public DataSource getDataSource ()
  {
    return source;
  }

  /**
   * Sets the data source for this filter.
   *
   * @param ds The data source.
   */
  public void setDataSource (DataSource ds)
  {
    if (ds == null) throw new NullPointerException();
    source = ds;
  }

  public Object clone () throws CloneNotSupportedException
  {
    StringFilter f = (StringFilter) super.clone();
    f.source = (DataSource) source.clone();
    return f;
  }
}
