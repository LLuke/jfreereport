/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: ImageURLElementTemplate.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.ImageLoadFilter;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.URLFilter;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplate;
import com.jrefinery.report.io.ext.factory.objects.ObjectFactoryException;

import java.net.URL;

public class ImageURLElementTemplate extends AbstractTemplate
{
  public ImageURLElementTemplate(String name)
  {
    super(name);
    setParameterDefinition(VALUE_PARAMETER, Object.class);
    setParameterDefinition(BASE_URL_PARAMETER, URL.class);
  }

  public DataSource createDataSource()
  {
    Object value = getParameter(VALUE_PARAMETER);
    URL baseURL = (URL) getParameter(BASE_URL_PARAMETER);

    URLFilter urlFilter = new URLFilter();
    urlFilter.setDataSource(new StaticDataSource(value));
    urlFilter.setBaseURL(baseURL);
    ImageLoadFilter filter = new ImageLoadFilter();
    filter.setDataSource(urlFilter);
    return filter;
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
  }
}
