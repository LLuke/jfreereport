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
 * --------------------------
 * ResourceFieldTemplate.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ResourceFieldTemplate.java,v 1.7 2005/02/23 21:04:46 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.filter.templates;

import java.util.MissingResourceException;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.filter.ResourceFileFilter;
import org.jfree.report.filter.StringFilter;

/**
 * A resource field template, which reads a String value from a ResourceBundle.
 *
 * @author Thomas Morgner
 */
public class ResourceFieldTemplate extends AbstractTemplate
        implements ReportConnectable
{
  /**
   * A data-row accessor.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * A resource file filter.
   */
  private ResourceFileFilter resourceFilter;

  /**
   * Creates a new template.
   */
  public ResourceFieldTemplate ()
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
  public String getField ()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field the field name.
   */
  public void setField (final String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  /**
   * Returns the resource class name.
   *
   * @return The resource class name.
   */
  public String getResourceIdentifier ()
  {
    return resourceFilter.getResourceIdentifier();
  }

  /**
   * Sets the resource class name.
   *
   * @param resourceClassName the resource class name.
   * @throws MissingResourceException if the resource is missing.
   */
  public void setResourceIdentifier (final String resourceClassName)
          throws MissingResourceException
  {
    resourceFilter.setResourceIdentifier(resourceClassName);
  }

  /**
   * Returns the string that represents a <code>null</code> value.
   *
   * @return The string that represents a <code>null</code> value.
   */
  public String getNullValue ()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the string that represents a <code>null</code> value.
   *
   * @param nullValue the string that represents a <code>null</code> value.
   */
  public void setNullValue (final String nullValue)
  {
    stringFilter.setNullValue(nullValue);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue ()
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
  public Object clone ()
          throws CloneNotSupportedException
  {
    final ResourceFieldTemplate template = (ResourceFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.resourceFilter = (ResourceFileFilter) template.stringFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.resourceFilter.getDataSource();
    return template;
  }

  /**
   * Connects the connectable to the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   */
  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().registerReportDefinition(reportDefinition);
    resourceFilter.registerReportDefinition(reportDefinition);
  }

  /**
   * Disconnects this ReportConnectable from the report definition.
   *
   * @param reportDefinition the ReportDefinition.
   */
  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().unregisterReportDefinition(reportDefinition);
    resourceFilter.unregisterReportDefinition(reportDefinition);
  }

  /**
   * Returns the datarow data source used in this template.
   *
   * @return the datarow data source.
   */
  protected DataRowDataSource getDataRowDataSource ()
  {
    return dataRowDataSource;
  }
}
