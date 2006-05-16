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
 * CachingResourceData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CachingResourceData.java,v 1.1.1.1 2006/04/17 16:48:44 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.cache;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * A very simple implementation which is suitable for smaller objects. The
 * complete data is read into memory.
 *
 * @author Thomas Morgner
 */
public class CachingResourceData implements ResourceData, Serializable
{
  private ResourceData data;
  private HashMap attributes;
  // The cached raw data. This is stored on the serialized stream as well
  // so that we can cache that stuff.
  private byte[] rawData;

  public CachingResourceData(final ResourceData data)
  {
    if (data == null)
    {
      throw new NullPointerException();
    }
    this.data = data;
  }

  public InputStream getResourceAsStream(ResourceManager caller) throws ResourceLoadingException
  {
    final byte[] data = getResource(caller);
    return new ByteArrayInputStream(data);
  }

  public synchronized byte[] getResource(ResourceManager caller) throws ResourceLoadingException
  {
    if (rawData == null)
    {
      rawData = data.getResource(caller);
    }
    return rawData;
  }

  public synchronized Object getAttribute(String key)
  {
    if (attributes == null)
    {
      attributes = new HashMap();
    }
    else
    {
      final Object cached = attributes.get(key);
      if (cached != null)
      {
        return cached;
      }
    }

    final Object value = data.getAttribute(key);
    if (value != null)
    {
      attributes.put(key, value);
    }
    return value;
  }

  public ResourceKey getKey()
  {
    return data.getKey();
  }

  public long getVersion(ResourceManager caller)
          throws ResourceLoadingException
  {
    return data.getVersion(caller);
  }
}
