/**
 * Date: Jan 24, 2003
 * Time: 6:14:09 PM
 *
 * $Id: ResourceFieldTemplate.java,v 1.1 2003/01/25 02:50:56 taqua Exp $
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.filter.DataRowConnectable;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.ResourceFileFilter;
import com.jrefinery.report.filter.StringFilter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceFieldTemplate extends AbstractTemplate implements DataRowConnectable
{
  private DataRowDataSource dataRowDataSource;
  private StringFilter stringFilter;
  private ResourceFileFilter resourceFilter;
  private String resourceClassName;

  public ResourceFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    resourceFilter = new ResourceFileFilter();
    resourceFilter.setDataSource(dataRowDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(resourceFilter);
  }

  public String getField()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  public void setField(String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  public String getResourceClassName()
  {
    return resourceClassName;
  }

  public void setResourceClassName(String resourceClassName)
    throws MissingResourceException
  {
    resourceFilter.setResources(ResourceBundle.getBundle(resourceClassName));
    this.resourceClassName = resourceClassName;
  }

  public String getNullValue()
  {
    return stringFilter.getNullValue();
  }

  public void setNullValue(String nullValue)
  {
    stringFilter.setNullValue(nullValue);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return stringFilter.getValue();
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
    ResourceFieldTemplate template = (ResourceFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.resourceFilter = (ResourceFileFilter) template.stringFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.resourceFilter.getDataSource();
    return template;
  }

  /**
   * Connects the DataRow to the data source.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void connectDataRow(DataRow row) throws IllegalStateException
  {
    dataRowDataSource.connectDataRow(row);
  }

  /**
   * Releases the connection to the data row.
   * <p>
   * If no data row is connected, an <code>IllegalStateException</code> is thrown to indicate the
   * programming error.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void disconnectDataRow(DataRow row) throws IllegalStateException
  {
    dataRowDataSource.disconnectDataRow(row);
  }

}
