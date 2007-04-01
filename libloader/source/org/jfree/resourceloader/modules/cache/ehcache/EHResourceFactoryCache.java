/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://reporting.pentaho.org/libloader/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.modules.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.cache.ResourceFactoryCache;

/**
 * Creation-Date: 13.04.2006, 16:30:34
 *
 * @author Thomas Morgner
 */
public class EHResourceFactoryCache implements ResourceFactoryCache
{
  private Cache factoryCache;

  public EHResourceFactoryCache(final Cache factoryCache)
  {
    if (factoryCache == null)
    {
      throw new NullPointerException();
    }
    this.factoryCache = factoryCache;
  }

  public Resource get(ResourceKey key)
  {
    try
    {
      final Element element = factoryCache.get(key);
      return (Resource) element.getObjectValue();
    }
    catch (CacheException e)
    {
      return null;
    }
  }

  public void put(final Resource resource)
  {
    final ResourceKey source = resource.getSource();
    try
    {
      factoryCache.put(new Element(source, resource));
    }
    catch(Exception e)
    {
      // ignore ... the object is not serializable ..
    }
  }

  public void remove(final Resource resource)
  {
    final ResourceKey source = resource.getSource();

    factoryCache.remove(source);
  }

  public void clear()
  {
    try
    {
      factoryCache.removeAll();
    }
    catch (Exception e)
    {
      // ignore ..
    }
  }
}
