/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libloader/
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
 *
 * ------------
 * $Id: OSResourceFactoryCache.java,v 1.2 2006/12/03 16:41:16 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.modules.cache.oscache;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.cache.ResourceFactoryCache;
import org.jfree.resourceloader.cache.CacheUtility;

/**
 * Creation-Date: 13.04.2006, 16:30:34
 *
 * @author Thomas Morgner
 */
public class OSResourceFactoryCache implements ResourceFactoryCache
{
  private GeneralCacheAdministrator factoryCache;

  public OSResourceFactoryCache(final GeneralCacheAdministrator factoryCache)
  {
    if (factoryCache == null)
    {
      throw new NullPointerException();
    }
    this.factoryCache = factoryCache;
  }

  public Resource get(ResourceKey key)
  {
    final String ext = CacheUtility.externalizeKey(key);
    if (ext == null)
    {
      return null;
    }

    try
    {
      return (Resource) factoryCache.getFromCache(ext);
    }
    catch (NeedsRefreshException e)
    {
      factoryCache.cancelUpdate(ext);
      factoryCache.removeEntry(ext);
      return null;
    }
  }

  public void put(final Resource resource)
  {
    final ResourceKey source = resource.getSource();
    final String ext = CacheUtility.externalizeKey(source);
    if (ext == null)
    {
      return;
    }
    factoryCache.putInCache(ext, resource);
  }

  public void remove(final Resource resource)
  {
    final ResourceKey source = resource.getSource();
    final String ext = CacheUtility.externalizeKey(source);
    if (ext == null)
    {
      return;
    }

    factoryCache.removeEntry(ext);
  }

  public void clear()
  {
    factoryCache.flushAll();
  }
}
