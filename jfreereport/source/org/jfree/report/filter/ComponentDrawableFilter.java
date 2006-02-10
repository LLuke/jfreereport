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
 * $Id: ComponentDrawableFilter.java,v 1.2 2005/12/11 12:47:06 taqua Exp $
 *
 * Changes
 * -------
 * 11-Oct-2005: Initial version.
 */
package org.jfree.report.filter;

import java.awt.Component;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportDefinition;
import org.jfree.report.util.ComponentDrawable;

/**
 * Creation-Date: 11.10.2005, 14:58:34
 *
 * @author Thomas Morgner
 */
public class ComponentDrawableFilter implements DataFilter, ReportConnectable
{
  /** The datasource from where to read the urls. */
  private DataSource source;
  private ReportDefinition reportDefinition;

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
    final String allowOwnPeer =
            reportDefinition.getReportConfiguration().getConfigProperty
                    ("org.jfree.report.AllowOwnPeerForComponentDrawable");
    cd.setAllowOwnPeer ("true".equals(allowOwnPeer));
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
    il.reportDefinition = null;
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


  /**
   * Connects the connectable to the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   * @throws IllegalStateException if this instance is already connected to a
   * report definition.
   */
  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != null)
    {
      throw new IllegalStateException("Already connected.");
    }
    if (reportDefinition == null)
    {
      throw new NullPointerException("The given report definition is null");
    }
    this.reportDefinition = reportDefinition;
  }

  /**
   * Disconnects the connectable from the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   * @throws IllegalStateException if this instance is already connected to a
   * report definition.
   */
  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != reportDefinition)
    {
      throw new IllegalStateException("This report definition is not registered.");
    }
    this.reportDefinition = null;
  }

}
