/**
 * Date: Jan 24, 2003
 * Time: 6:07:57 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter;

import com.jrefinery.report.util.Log;

import java.util.ResourceBundle;

public class ResourceFileFilter implements DataFilter
{
  private ResourceBundle resources;
  private DataSource dataSource;

  public ResourceFileFilter()
  {
  }

  public ResourceBundle getResources()
  {
    return resources;
  }

  public void setResources(ResourceBundle resources)
  {
    this.resources = resources;
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    if (dataSource == null)
      return null;

    if (resources == null)
      return null;

    Object value = dataSource.getValue();
    if (value == null)
      return null;
    String svalue = String.valueOf(value);

    try
    {
      return resources.getObject(svalue);
    }
    catch (Exception e)
    {
      // on errors return null.
      Log.debug (new Log.SimpleMessage("Failed to retrive the value for key", svalue), e);
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
    ResourceFileFilter filter = (ResourceFileFilter) super.clone();
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
  public void setDataSource(DataSource ds)
  {
    this.dataSource = ds;
  }
}
