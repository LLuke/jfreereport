/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * --------------------------
 * ResourceFieldTemplate.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ResourceFieldTemplate.java,v 1.5 2003/03/07 16:55:54 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.filter.templates;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.filter.DataRowConnectable;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.ResourceFileFilter;
import com.jrefinery.report.filter.StringFilter;

/**
 * A resource field template, which reads a String value from a ResourceBundle.
 * 
 * @author Thomas Morgner
 */
public class ResourceFieldTemplate extends AbstractTemplate implements DataRowConnectable
{
  /** A data-row accessor. */
  private DataRowDataSource dataRowDataSource;
  
  /** A string filter. */
  private StringFilter stringFilter;
  
  /** A resource file filter. */
  private ResourceFileFilter resourceFilter;
  
  /** The resource class name. */
  private String resourceClassName;

  /**
   * Creates a new template.
   */
  public ResourceFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    resourceFilter = new ResourceFileFilter();
    resourceFilter.setDataSource(dataRowDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(resourceFilter);
  }

  /**
   * Returns the field name.
   * 
   * @return The field name.
   */
  public String getField()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   * 
   * @param field  the field name.
   */
  public void setField(String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  /**
   * Returns the resource class name.
   * 
   * @return The resource class name.
   */
  public String getResourceClassName()
  {
    return resourceClassName;
  }

  /**
   * Sets the resource class name.
   * 
   * @param resourceClassName  the resource class name.
   * 
   * @throws MissingResourceException if the resource is missing.
   */
  public void setResourceClassName(String resourceClassName)
    throws MissingResourceException
  {
    resourceFilter.setResources(ResourceBundle.getBundle(resourceClassName));
    this.resourceClassName = resourceClassName;
  }

  /**
   * Returns the string that represents a <code>null</code> value.
   * 
   * @return The string that represents a <code>null</code> value.
   */
  public String getNullValue()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the string that represents a <code>null</code> value.
   * 
   * @param nullValue  the string that represents a <code>null</code> value.
   */
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
   * Clones the template.
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
   * Connects a {@link DataRow} to the data source.
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
