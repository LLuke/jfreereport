/**
 * Date: Jan 24, 2003
 * Time: 6:14:20 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.filter.ResourceFileFilter;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

public class ResourceLabelTemplate extends AbstractTemplate
{
  private StaticDataSource staticDataSource;
  private StringFilter stringFilter;
  private ResourceFileFilter resourceFilter;
  private String resourceClassName;

  public ResourceLabelTemplate()
  {
    staticDataSource = new StaticDataSource();
    resourceFilter = new ResourceFileFilter();
    resourceFilter.setDataSource(staticDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(resourceFilter);
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

  public void setContent(String content)
  {
    staticDataSource.setValue(content);
  }

  public String getContent()
  {
    return (String) (staticDataSource.getValue());
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
    ResourceLabelTemplate template = (ResourceLabelTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.resourceFilter = (ResourceFileFilter) template.stringFilter.getDataSource();
    template.staticDataSource = (StaticDataSource) template.resourceFilter.getDataSource();
    return template;
  }

}
