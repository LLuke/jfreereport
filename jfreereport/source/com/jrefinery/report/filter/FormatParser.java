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
 * FormatParser.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.filter;

import java.text.Format;
import java.text.ParseException;

public class FormatParser implements DataFilter
{
  private Format format;
  private DataSource datasource;
  private Object nullvalue;

  public void setFormatter (Format format)
  {
    if (format == null) throw new NullPointerException();
    this.format = format;
  }

  public Format getFormatter ()
  {
    return this.format;
  }

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
      return f.parseObject(String.valueOf(o));
    }
    catch (ParseException e)
    {
      return null;
    }
  }

  public DataSource getDataSource ()
  {
    return datasource;
  }

  public void setDataSource (DataSource ds)
  {
    if (ds == null) throw new NullPointerException();
    this.datasource = ds;
  }

  public void setNullValue (Object nullvalue)
   {
     this.nullvalue = nullvalue;
   }

   public Object getNullValue ()
   {
     return nullvalue;
   }
}
