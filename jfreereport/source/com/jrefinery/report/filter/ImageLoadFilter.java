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
 * ImageLoadFilter.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.filter;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.KeyedQueue;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Hashtable;

public class ImageLoadFilter implements DataFilter
{
  private KeyedQueue imageCache;

  public ImageLoadFilter ()
  {
    imageCache = new KeyedQueue();
  }

  private DataSource source;

  public Object getValue ()
  {
    DataSource ds = getDataSource();
    if (ds == null) return null;
    Object o = ds.getValue();
    if (o == null) return null;

    URL url = null;
    if (o instanceof URL)
    {
      url = (URL) o;
    }
    else if (o instanceof String)
    {
      try
      {
        url = new URL((String) o);
      }
      catch (MalformedURLException mfuel)
      {
        return null;
      }
    }
    else if (o instanceof File)
    {
      try
      {
        File f = (File) o;
        url = f.toURL();
      }
      catch (MalformedURLException mfuel)
      {
        return null;
      }
    }
    if (url == null) return null;

    Object retval = imageCache.get(url);
    if (retval == null)
    {
      try
      {
        retval = new ImageReference (url);
        imageCache.put(url, retval);
      }
      catch (IOException ioe)
      {
        return null;
      }
    }
    return retval;
  }

  public DataSource getDataSource ()
  {
    return source;
  }

  public void setDataSource (DataSource ds)
  {
    if (ds == null) throw new NullPointerException();

    source = ds;
  }
}
