/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: ImageURLFieldTemplate.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.ImageLoadFilter;
import com.jrefinery.report.filter.URLFilter;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplate;
import com.jrefinery.report.io.ext.factory.objects.ObjectFactoryException;

import java.net.URL;

public class ImageURLFieldTemplate extends AbstractTemplate
{
  public ImageURLFieldTemplate(String name)
  {
    super(name);
    setParameterDefinition(FIELD_PARAMETER, String.class);
    setParameterDefinition(BASE_URL_PARAMETER, URL.class);
  }

  public DataSource createDataSource()
  {
    String field = (String) getParameter(FIELD_PARAMETER);
    URL baseURL = (URL) getParameter(BASE_URL_PARAMETER);

    URLFilter urlFilter = new URLFilter();
    urlFilter.setDataSource(new DataRowDataSource(field));
    urlFilter.setBaseURL(baseURL);
    ImageLoadFilter filter = new ImageLoadFilter();
    filter.setDataSource(urlFilter);
    return filter;
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
  }
}
