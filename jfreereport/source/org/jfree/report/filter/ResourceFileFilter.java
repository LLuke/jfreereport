/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * $Id: ResourceFileFilter.java,v 1.1 2003/07/07 22:44:04 taqua Exp $
 *
 * ChangeLog
 * ---------
 * 24-Jan-2003 : Initial version
 * 22-Feb-2003 : Documentation.
 */
package org.jfree.report.filter;

import java.io.Serializable;
import java.util.ResourceBundle;

import org.jfree.report.util.Log;

/**
 * Lookup a key from a datasource using a ResourceBundle.
 * <p>
 * Filters a given datasource and uses the datasource value as key
 * for a ResourceBundle.
 *
 * @author Thomas Morgner
 */
public class ResourceFileFilter implements DataFilter, Serializable
{
  /** the used resource bundle. */
  private ResourceBundle resources;

  /** the filtered data source. */
  private DataSource dataSource;

  /**
   * Creates a new ResourceFileFilter.
   */
  public ResourceFileFilter()
  {
  }

  /**
   * Gets the assigned resource bundle, or null, if no resource bundle is defined.
   *
   * @return the defined ResourceBundle or null.
   */
  public ResourceBundle getResources()
  {
    return resources;
  }

  /**
   * Defines a resource bundle for this filter.
   *
   * @param resources the resource bundle used to lookup the value.
   */
  public void setResources(final ResourceBundle resources)
  {
    this.resources = resources;
  }

  /**
   * Returns the current value for the data source. The method will
   * return null, if no datasource or no resource bundle is defined
   * or if the datasource's value is null.
   * <p>
   * The value read from the dataSource is looked up in the given
   * resourcebundle using the <code>ResourceBundle.getObject()</code>
   * method. If the lookup fails, null is returned.
   *
   * @return the value or null, if the value could not be looked up.
   */
  public Object getValue()
  {
    if (dataSource == null)
    {
      return null;
    }
    if (resources == null)
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
      return resources.getObject(svalue);
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
  public Object clone() throws CloneNotSupportedException
  {
    final ResourceFileFilter filter = (ResourceFileFilter) super.clone();
    filter.dataSource = (DataSource) dataSource.clone();
    return filter;
  }

  /**
   * Returns the assigned DataSource for this Target.
   *
   * @return The datasource.
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Assigns a DataSource for this Target.
   *
   * @param ds The data source.
   */
  public void setDataSource(final DataSource ds)
  {
    this.dataSource = ds;
  }
}
