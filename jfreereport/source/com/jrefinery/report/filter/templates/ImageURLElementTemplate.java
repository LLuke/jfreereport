/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ----------------------------
 * ImageURLElementTemplate.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageURLElementTemplate.java,v 1.7 2003/06/27 14:25:18 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.filter.templates;

import java.net.URL;

import com.jrefinery.report.filter.ImageLoadFilter;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.URLFilter;

/**
 * An image URL element template, which reads the image from a static URL.
 *
 * @author Thomas Morgner
 */
public class ImageURLElementTemplate extends AbstractTemplate
{
  /** The image load filter. */
  private ImageLoadFilter imageLoadFilter;

  /** A static datasource. */
  private StaticDataSource staticDataSource;

  /** A URL filter. */
  private URLFilter urlFilter;

  /**
   * Creates a new template.
   */
  public ImageURLElementTemplate()
  {
    staticDataSource = new StaticDataSource();
    urlFilter = new URLFilter();
    urlFilter.setDataSource(staticDataSource);
    imageLoadFilter = new ImageLoadFilter();
    imageLoadFilter.setDataSource(urlFilter);
  }

  /**
   * Sets the URL for the template.
   *
   * @param content  the URL.
   */
  public void setContent(final String content)
  {
    staticDataSource.setValue(content);
  }

  /**
   * Returns the URL text for the template.
   *
   * @return The URL text.
   */
  public String getContent()
  {
    return (String) (staticDataSource.getValue());
  }

  /**
   * Returns the base URL.
   *
   * @return The URL.
   */
  public URL getBaseURL()
  {
    return urlFilter.getBaseURL();
  }

  /**
   * Sets the base URL.
   *
   * @param baseURL  the URL.
   */
  public void setBaseURL(final URL baseURL)
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
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final ImageURLElementTemplate template = (ImageURLElementTemplate) super.clone();
    template.imageLoadFilter = (ImageLoadFilter) imageLoadFilter.clone();
    template.urlFilter = (URLFilter) template.imageLoadFilter.getDataSource();
    template.staticDataSource = (StaticDataSource) template.urlFilter.getDataSource();
    return template;
  }

}
