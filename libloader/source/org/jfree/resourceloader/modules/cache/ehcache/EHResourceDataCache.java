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
 * EHResourceDataCache.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: EHResourceDataCache.java,v 1.2 2006/05/16 17:13:30 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.modules.cache.ehcache;

import java.io.IOException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.cache.CachingResourceData;
import org.jfree.resourceloader.cache.DefaultResourceDataCacheEntry;
import org.jfree.resourceloader.cache.ResourceDataCache;
import org.jfree.resourceloader.cache.ResourceDataCacheEntry;

/**
 * Creation-Date: 13.04.2006, 16:30:34
 *
 * @author Thomas Morgner
 */
public class EHResourceDataCache implements ResourceDataCache
{
  private Cache dataCache;

  public EHResourceDataCache(final Cache dataCache)
  {
    if (dataCache == null)
    {
      throw new NullPointerException();
    }
    this.dataCache = dataCache;
  }

  /**
   * Retrieves the given data from the cache.
   *
   * @param data
   */
  public ResourceDataCacheEntry get(ResourceKey key)
  {
    final String ext = key.toExternalForm();
    if (ext == null)
    {
      return null;
    }

    try
    {
      return (ResourceDataCacheEntry) dataCache.get(ext);
    }
    catch (CacheException e)
    {
      return null;
    }
  }

  /**
   * Stores the given data on the cache. The data is registered by its primary
   * key. The cache has to store the current version of the data.
   *
   * @param data the data to be stored in the cache
   * @return the resource data object, possibly wrapped by a cache-specific
   *         implementation.
   */
  public ResourceData put(ResourceManager caller, ResourceData data) throws ResourceLoadingException
  {
    final ResourceData cdata = CachingResourceData.createCached(data);
    final String ext = data.getKey().toExternalForm();
    if (ext == null)
    {
      return cdata;
    }
    dataCache.put(new Element(ext, new DefaultResourceDataCacheEntry(cdata, caller)));
    return cdata;
  }

  public void remove(ResourceData data)
  {
    final String ext = data.getKey().toExternalForm();
    if (ext == null)
    {
      return;
    }
    dataCache.remove(ext);
  }

  /**
   * Remove all cached entries. This should be called after the cache has become
   * invalid or after it has been removed from a resource manager.
   */
  public void clear()
  {
    try
    {
      dataCache.removeAll();
    }
    catch (IOException e)
    {
      // ignore 
    }
  }

}
