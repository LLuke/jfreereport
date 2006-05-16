/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libloader/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * URLResourceData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: URLResourceData.java,v 1.1.1.1 2006/04/17 16:48:30 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.jfree.io.IOUtils;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * A generic read handler for URL resources.
 *
 * @author Thomas Morgner
 */
public class URLResourceData extends AbstractResourceData
{
  private long modificationDate;
  private String filename;
  private Long contentLength;
  private String contentType;
  private boolean metaDataOK;
  private URL url;
  private URLResourceKey key;

  public URLResourceData(final URLResourceKey key)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }

    this.key = key;
    this.url = key.getUrl();
    // for the ease of implementation, we take the file name from the URL.
    // Feel free to add a 'Content-Disposition' parser with all details :)
    this.filename = IOUtils.getInstance().getFileName(url);
  }

  private void readMetaData() throws IOException
  {
    URLConnection c = url.openConnection();
    c.setDoOutput(false);
    c.setAllowUserInteraction(false);
    if (c instanceof HttpURLConnection)
    {
      HttpURLConnection httpURLConnection = (HttpURLConnection) c;
      httpURLConnection.setRequestMethod("HEAD");
    }
    c.connect();
    modificationDate = c.getDate();
    contentLength = new Long(c.getContentLength());
    contentType = c.getHeaderField("content-type");
    c.getInputStream().close();
    metaDataOK = true;
  }

  public InputStream getResourceAsStream(ResourceManager caller) throws ResourceLoadingException
  {
    try
    {
      URLConnection c = url.openConnection();
      c.setDoOutput(false);
      c.setAllowUserInteraction(false);
      return c.getInputStream();
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Failed to open URL connection", e);
    }
  }

  public Object getAttribute(String key)
  {
    if (key.equals(ResourceData.FILENAME))
    {
      return filename;
    }
    if (key.equals(ResourceData.CONTENT_LENGTH))
    {
      try
      {
        if (metaDataOK == false)
        {
          readMetaData();
        }
        return contentLength;
      }
      catch (IOException e)
      {
        return null;
      }
    }
    if (key.equals(ResourceData.CONTENT_TYPE))
    {
      try
      {
        if (metaDataOK == false)
        {
          readMetaData();
        }
        return contentType;
      }
      catch (IOException e)
      {
        return null;
      }
    }
    return null;
  }

  public long getVersion(ResourceManager caller)
          throws ResourceLoadingException
  {
    try
    {
      // always read the new date .. sorry, this is expensive, but needed here
      // else the cache would not be in sync ...
      readMetaData();
      return modificationDate;
    }
    catch (IOException e)
    {
      return -1;
    }
  }

  public ResourceKey getKey()
  {
    return key;
  }
}
