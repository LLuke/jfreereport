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
 * ResourceLabelTemplate.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.filter.templates;

import com.jrefinery.report.filter.ResourceFileFilter;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.StringFilter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A resource label template.
 * 
 * @author Thomas Morgner
 */
public class ResourceLabelTemplate extends AbstractTemplate
{
  /** A static datasource. */
  private StaticDataSource staticDataSource;
  
  /** A string filter. */  
  private StringFilter stringFilter;
  
  /** A resource file filter. */
  private ResourceFileFilter resourceFilter;
  
  /** The resource class name. */
  private String resourceClassName;

  /**
   * Creates a new template.
   */
  public ResourceLabelTemplate()
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
  public String getResourceClassName()
  {
    return resourceClassName;
  }

  /**
   * Sets the resource class name.
   * 
   * @param resourceClassName  the class name.
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
   * Sets the content.
   * 
   * @param content  the content.
   */
  public void setContent(String content)
  {
    staticDataSource.setValue(content);
  }

  /**
   * Returns the content.
   * 
   * @return The content.
   */
  public String getContent()
  {
    return (String) (staticDataSource.getValue());
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
   * @param nullValue  The string that represents a <code>null</code> value.
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
    ResourceLabelTemplate template = (ResourceLabelTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.resourceFilter = (ResourceFileFilter) template.stringFilter.getDataSource();
    template.staticDataSource = (StaticDataSource) template.resourceFilter.getDataSource();
    return template;
  }

}
