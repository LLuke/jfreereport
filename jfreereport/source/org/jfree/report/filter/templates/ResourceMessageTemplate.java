/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * ResourceMessageTemplate.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ResourceMessageTemplate.java,v 1.1 2006/01/24 19:01:08 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.filter.templates;

import java.util.MissingResourceException;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.ResourceMessageFormatFilter;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.filter.ReportConnectable;

/**
 * Creation-Date: 24.01.2006, 16:33:54
 *
 * @author Thomas Morgner
 */
public class ResourceMessageTemplate extends AbstractTemplate
        implements ReportConnectable
{
  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * A resource file filter.
   */
  private ResourceMessageFormatFilter resourceFilter;

  /**
   * Creates a new template.
   */
  public ResourceMessageTemplate ()
  {
    resourceFilter = new ResourceMessageFormatFilter();
    stringFilter = new StringFilter();
    stringFilter.setDataSource(resourceFilter);
  }

  public String getFormatKey()
  {
    return resourceFilter.getFormatKey();
  }

  public void setFormatKey(final String formatKey)
  {
    resourceFilter.setFormatKey(formatKey);
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
    final ResourceMessageTemplate template = (ResourceMessageTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.resourceFilter = (ResourceMessageFormatFilter) template.stringFilter.getDataSource();
    return template;
  }

  /**
   * Connects the connectable to the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   */
  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    resourceFilter.registerReportDefinition(reportDefinition);
  }

  /**
   * Disconnects this ReportConnectable from the report definition.
   *
   * @param reportDefinition the ReportDefinition.
   */
  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    resourceFilter.unregisterReportDefinition(reportDefinition);
  }

}
