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
 * EHCacheProvider.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.modules.cache.ehcache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import org.jfree.resourceloader.cache.ResourceDataCache;
import org.jfree.resourceloader.cache.ResourceDataCacheProvider;
import org.jfree.resourceloader.cache.ResourceFactoryCache;
import org.jfree.resourceloader.cache.ResourceFactoryCacheProvider;

/**
 * Creation-Date: 13.04.2006, 16:32:20
 *
 * @author Thomas Morgner
 */
public class EHCacheProvider implements ResourceDataCacheProvider,
        ResourceFactoryCacheProvider
{
  private static CacheManager cacheManager;

  public static CacheManager getCacheManager() throws CacheException
  {
    if (cacheManager == null)
    {
      cacheManager = CacheManager.create();
    }
    return cacheManager;
  }

  public EHCacheProvider()
  {
  }

  public ResourceDataCache createDataCache()
  {
    try
    {
      CacheManager manager = getCacheManager();
      synchronized(manager)
      {
        if (manager.cacheExists("liblayout-data") == false)
        {
          manager.addCache("liblayout-data");
        }
        return new EHResourceDataCache(manager.getCache("liblayout-data"));
      }
    }
    catch (CacheException e)
    {
      return null;
    }
  }

  public ResourceFactoryCache createFactoryCache()
  {
    try
    {
      CacheManager manager = getCacheManager();
      synchronized(manager)
      {
        if (manager.cacheExists("liblayout-factory") == false)
        {
          manager.addCache("liblayout-factory");
        }
        return new EHResourceFactoryCache(manager.getCache("liblayout-factory"));
      }
    }
    catch (CacheException e)
    {
      return null;
    }
  }
}
