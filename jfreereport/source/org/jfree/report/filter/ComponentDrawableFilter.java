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
 * ComponentDrawableFilter.java
 * -----------------
 * (C)opyright 2005, by Object Refinery Limited.
 *
 * $Id: FormatParser.java,v 1.4 2005/02/23 21:04:45 taqua Exp $
 *
 * Changes
 * -------
 * 11-Oct-2005: Initial version.
 */
package org.jfree.report.filter;

import java.awt.Component;

import org.jfree.report.util.ComponentDrawable;
import org.jfree.report.JFreeReportBoot;

/**
 * Creation-Date: 11.10.2005, 14:58:34
 *
 * @author Thomas Morgner
 */
public class ComponentDrawableFilter implements DataFilter
{
  /** The datasource from where to read the urls. */
  private DataSource source;

  public ComponentDrawableFilter()
  {
  }

  public Object getValue()
  {
    if (isHeadless())
    {
      return null;
    }

    final DataSource ds = getDataSource();
    if (ds == null)
    {
      return null;
    }
    final Object o = ds.getValue();
    if (o == null)
    {
      return null;
    }

    if (o instanceof Component == false)
    {
      return null;
    }
    ComponentDrawable cd = new ComponentDrawable();
    cd.setComponent((Component) o);
    return cd;
  }

  protected static boolean isHeadless()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            ("java.awt.headless", "false").equals("true");
  }

  public Object clone() throws CloneNotSupportedException
  {
    final ComponentDrawableFilter il = (ComponentDrawableFilter) super.clone();
    if (source != null)
    {
      il.source = (DataSource) source.clone();
    }
    return il;
  }

  public DataSource getDataSource()
  {
    return source;
  }

  public void setDataSource(DataSource ds)
  {
    this.source = ds;
  }
}
