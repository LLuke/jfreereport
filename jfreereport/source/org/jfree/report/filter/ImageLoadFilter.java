/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * --------------------
 * ImageLoadFilter.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: ImageLoadFilter.java,v 1.1 2003/07/07 22:44:04 taqua Exp $
 *
 * ChangeLog
 * --------------------------------------
 * 21-May-2002 : Initial version
 * 06-Jun-2002 : Documentation
 * 03-Jul-2002 : Serializable, Cloneable implemented
 * 28-Aug-2002 : Documentation again
 */
package org.jfree.report.filter;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.jfree.report.ImageReference;
import org.jfree.report.util.KeyedQueue;
import org.jfree.report.util.Log;

/**
 * The image load filter is used to load images during the report generation process.
 * This filter expects its datasource to return a java.net.URL. If the datasource does
 * not return an URL, <code>null</code> is returned.
 * <p>
 * This filter is mostly used in conjunction with the URLFilter, which creates URLs
 * from Strings and files if nessesary.
 * <p>
 * The url is used to create a new imagereference which is returned to the caller.
 * The loaded/created imagereference is stored in an internal cache.
 * <p>
 * This filter can be used to dynamically change images of a report, a very nice feature
 * for photo albums and catalogs for instance.
 * <p>
 * This filter will return null, if something else than an URL was retrieved from the
 * assigned datasource
 *
 * @author Thomas Morgner
 */
public class ImageLoadFilter implements DataFilter, Serializable
{
  /**
   * the cache for previously loaded images. If the maximum size of the cache reached,
   *
   */
  private KeyedQueue imageCache;

  /**
   * The datasource from where to read the urls.
   */
  private DataSource source;

  /**
   * creates a new ImageLoadFilter with a cache size of 10.
   */
  public ImageLoadFilter()
  {
    this(1);
  }

  /**
   * Creates a new ImageLoadFilter with the defined cache size.
   *
   * @param cacheSize  the cache size.
   */
  public ImageLoadFilter(final int cacheSize)
  {
    imageCache = new KeyedQueue(cacheSize);
  }

  /**
   * Reads this filter's datasource and if the source returned an URL, tries to form
   * a imagereference. If the image is loaded in a previous run and is still in the cache,
   * no new reference is created and the previously loaded reference is returned.
   *
   * @return  the current value for this filter.
   */
  public Object getValue()
  {
    final DataSource ds = getDataSource();
    if (ds == null)
    {
      return null;
    }
    final Object o = ds.getValue();
    if (o == null)
    {
      return null;
    }

    if (o instanceof URL == false)
    {
      return null;
    }

    // a valid url is found, lookup the url in the cache, maybe the image is loaded and
    // still there.
    final URL url = (URL) o;
    Object retval = imageCache.get(url);
    if (retval == null)
    {
      try
      {
        retval = new ImageReference(url);
      }
      catch (IOException ioe)
      {
        Log.warn("Error while loading the image from " + url, ioe);
        return null;
      }
    }
    // update the cache and put the image at the top of the list
    imageCache.put(url, retval);
    return retval;
  }

  /**
   * Returns the data source for the filter.
   *
   * @return The data source.
   */
  public DataSource getDataSource()
  {
    return source;
  }

  /**
   * Sets the data source.
   *
   * @param ds The data source.
   */
  public void setDataSource(final DataSource ds)
  {
    if (ds == null)
    {
      throw new NullPointerException();
    }

    source = ds;
  }

  /**
   * Clones the filter.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final ImageLoadFilter il = (ImageLoadFilter) super.clone();
    il.imageCache = (KeyedQueue) imageCache.clone();
    if (source != null)
    {
      il.source = (DataSource) source.clone();
    }
    return il;
  }

}
