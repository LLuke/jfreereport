/**
 * Date: Jan 14, 2003
 * Time: 6:18:08 PM
 *
 * $Id: ImageURLElementTemplate.java,v 1.1 2003/01/14 21:04:55 taqua Exp $
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.filter.ImageLoadFilter;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.URLFilter;

import java.net.URL;

public class ImageURLElementTemplate extends AbstractTemplate
{
  private ImageLoadFilter imageLoadFilter;
  private StaticDataSource staticDataSource;
  private URLFilter urlFilter;

  public ImageURLElementTemplate()
  {
    staticDataSource = new StaticDataSource();
    urlFilter = new URLFilter();
    urlFilter.setDataSource(staticDataSource);
    imageLoadFilter = new ImageLoadFilter();
    imageLoadFilter.setDataSource(urlFilter);
  }

  public void setContent(String content)
  {
    staticDataSource.setValue(content);
  }

  public String getContent()
  {
    return (String) (staticDataSource.getValue());
  }

  public URL getBaseURL()
  {
    return urlFilter.getBaseURL();
  }

  public void setBaseURL(URL baseURL)
  {
    urlFilter.setBaseURL(baseURL);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return imageLoadFilter.getValue();
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
    ImageURLElementTemplate template = (ImageURLElementTemplate) super.clone();
    template.imageLoadFilter = (ImageLoadFilter) imageLoadFilter.clone();
    template.urlFilter = (URLFilter) template.imageLoadFilter.getDataSource();
    template.staticDataSource = (StaticDataSource) template.urlFilter.getDataSource();
    return template;
  }
}
