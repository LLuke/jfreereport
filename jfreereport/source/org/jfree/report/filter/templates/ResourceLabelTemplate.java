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
 * ResourceLabelTemplate.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ResourceLabelTemplate.java,v 1.7 2005/02/23 21:04:46 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.filter.templates;

import java.util.MissingResourceException;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.filter.ResourceFileFilter;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.filter.StringFilter;

/**
 * A resource label template.
 *
 * @author Thomas Morgner
 */
public class ResourceLabelTemplate extends AbstractTemplate
        implements ReportConnectable
{
  /**
   * A static datasource.
   */
  private StaticDataSource staticDataSource;

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
  public ResourceLabelTemplate ()
  {
    staticDataSource = new StaticDataSource();
    resourceFilter = new ResourceFileFilter();
    resourceFilter.setDataSource(staticDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(resourceFilter);
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
   * @param resourceClassName the class name.
   * @throws MissingResourceException if the resource is missing.
   * @throws NullPointerException     if the resource class name is null.
   */
  public void setResourceIdentifier (final String resourceClassName)
          throws MissingResourceException
  {
    resourceFilter.setResourceIdentifier(resourceClassName);
  }

  /**
   * Sets the content.
   *
   * @param content the content.
   */
  public void setContent (final String content)
  {
    staticDataSource.setValue(content);
  }

  /**
   * Returns the content.
   *
   * @return The content.
   */
  public String getContent ()
  {
    return (String) (staticDataSource.getValue());
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
   * @param nullValue The string that represents a <code>null</code> value.
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
    final ResourceLabelTemplate template = (ResourceLabelTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.resourceFilter = (ResourceFileFilter) template.stringFilter.getDataSource();
    template.staticDataSource = (StaticDataSource) template.resourceFilter.getDataSource();
    return template;
  }


  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    resourceFilter.registerReportDefinition(reportDefinition);
  }

  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    resourceFilter.unregisterReportDefinition(reportDefinition);
  }

}
