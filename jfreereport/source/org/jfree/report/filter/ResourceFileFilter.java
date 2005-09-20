/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ResourceFileFilter.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner.
 *
 * $Id: ResourceFileFilter.java,v 1.9 2005/08/08 15:36:29 taqua Exp $
 *
 * ChangeLog
 * ---------
 * 24-Jan-2003 : Initial version
 * 22-Feb-2003 : Documentation.
 */
package org.jfree.report.filter;

import java.io.Serializable;
import java.util.ResourceBundle;

import org.jfree.report.ReportDefinition;
import org.jfree.util.Log;

/**
 * Lookup a key from a datasource using a ResourceBundle.
 * <p/>
 * Filters a given datasource and uses the datasource value as key for a ResourceBundle.
 *
 * @author Thomas Morgner
 */
public class ResourceFileFilter
        implements DataFilter, Serializable, ReportConnectable
{
  /** the used resource bundle. */
  private String resourceIdentifier;

  /** the filtered data source. */
  private DataSource dataSource;

  /** The report definition registered to this connectable. */
  private transient ReportDefinition reportDefinition;

  /**
   * Creates a new ResourceFileFilter.
   */
  public ResourceFileFilter ()
  {
  }

  public String getResourceIdentifier ()
  {
    return resourceIdentifier;
  }

  public void setResourceIdentifier (final String resourceIdentifier)
  {
    this.resourceIdentifier = resourceIdentifier;
  }

  /**
   * Returns the current value for the data source. The method will return null, if no
   * datasource or no resource bundle is defined or if the datasource's value is null.
   * <p/>
   * The value read from the dataSource is looked up in the given resourcebundle using the
   * <code>ResourceBundle.getObject()</code> method. If the lookup fails, null is
   * returned.
   *
   * @return the value or null, if the value could not be looked up.
   */
  public Object getValue ()
  {
    if (dataSource == null)
    {
      return null;
    }
    if (reportDefinition == null)
    {
      return null;
    }
    final Object value = dataSource.getValue();
    if (value == null)
    {
      return null;
    }
    final String svalue = String.valueOf(value);

    try
    {
      final String resourceId;
      if (resourceIdentifier != null)
      {
        resourceId = resourceIdentifier;
      }
      else
      {
        resourceId = reportDefinition.getReportConfiguration().getConfigProperty
                ("org.jfree.report.ResourceBundle");
      }

      final ResourceBundle bundle = reportDefinition.getResourceBundle(resourceId);
      if (bundle != null)
      {
        return bundle.getObject(svalue);
      }
    }
    catch (Exception e)
    {
      // on errors return null.
      Log.info(new Log.SimpleMessage("Failed to retrive the value for key", svalue), e);
    }
    return null;
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final ResourceFileFilter filter = (ResourceFileFilter) super.clone();
    filter.dataSource = (DataSource) dataSource.clone();
    filter.reportDefinition = null;
    return filter;
  }

  /**
   * Returns the assigned DataSource for this Target.
   *
   * @return The datasource.
   */
  public DataSource getDataSource ()
  {
    return dataSource;
  }

  /**
   * Assigns a DataSource for this Target.
   *
   * @param ds The data source.
   */
  public void setDataSource (final DataSource ds)
  {
    this.dataSource = ds;
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
