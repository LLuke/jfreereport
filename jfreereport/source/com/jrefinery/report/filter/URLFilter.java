/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------------
 * URLFilter.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * -------
 * 06-Jun-2002 : Outsourced from ImageLoadFilter.
 */
package com.jrefinery.report.filter;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;

/**
 * The URLFilter forms URLs from Strings ,Files and URLs. If an URL is relative, the
 * missing contents can be obtained by a default url, called the baseURL.
 * <p>
 */
public class URLFilter implements DataFilter
{
  /**
   * The datasource used to form the urls. This datasource should return
   * strings, files or urls
   */
  private DataSource source;

  /**
   * The base url is used to form the complete url if the given url is relative.
   * @see java.net.URL#URL(java.net.URL, java.lang.String)
   */
  private URL baseURL;

  /**
   * DefaultConstructor
   */
  public URLFilter ()
  {
  }

  /**
   * Returns the data source for the filter.
   *
   * @return The data source.
   */
  public DataSource getDataSource ()
  {
    return source;
  }

  /**
   * Sets the data source.
   *
   * @param ds The data source.
   */
  public void setDataSource (DataSource ds)
  {
    if (ds == null) throw new NullPointerException ();

    source = ds;
  }

  /**
   * Trys to form a url from the object returned from the datasource.
   * This function will return null if the datasource is null or returned null when
   * getValue was called.
   *
   * Null is also returned if the datasources value is not an url, a String or a file.
   * If the creation of the url failed with an MalformedURLException or the datasource
   * returned a file which is not readable, also null is returned.
   *
   * @return created url or null if something went wrong on url creation.
   */
  public Object getValue ()
  {
    if (getDataSource() == null) return null;
    Object o = getDataSource().getValue();
    if (o == null) return null;
    if (o instanceof URL) return o;

    try
    {
      if (o instanceof File)
      {
        File f = (File) o;
        if (f.canRead())
          return f.toURL();
      }
      else
      if (o instanceof String)
      {
        return new URL (getBaseURL(), (String) o);
      }
    }
    catch (MalformedURLException mfe)
    {
    }
    return null;

  }

  /**
   * @returns the base url used to complete relative urls.
   */
  public URL getBaseURL ()
  {
    return baseURL;
  }

  /**
   * defines the base url used to complete relative urls.
   */
  public void setBaseURL (URL baseURL)
  {
    this.baseURL = baseURL;
  }
}