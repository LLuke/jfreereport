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
 * StringFilter.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.filter;

public class StringFilter implements DataFilter
{
  private DataSource source;
  private String nullvalue;

  public void setNullValue (String nullvalue)
  {
    if (nullvalue == null) throw new NullPointerException();
    this.nullvalue = nullvalue;
  }

  public String getNullValue ()
  {
    return nullvalue;
  }

  public Object getValue ()
  {
    DataSource ds = getDataSource();
    if (ds == null)
    {
      return getNullValue();
    }
    Object o = ds.getValue();
    if (o == null) return getNullValue();
    return String.valueOf(o);
  }

  public DataSource getDataSource ()
  {
    return source;
  }

  public void setDataSource (DataSource ds)
  {
    if (ds == null) throw new NullPointerException();
    source = ds;
  }
}
