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
 * EHResourceFactoryCache.java
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

import java.io.IOException;

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
    final String ext = key.toExternalForm();
    if (ext == null)
    {
      return null;
    }

    try
    {
      return (Resource) factoryCache.get(ext);
    }
    catch (CacheException e)
    {
      return null;
    }
  }

  public void put(final Resource resource)
  {
    final ResourceKey source = resource.getSource();
    final String ext = source.toExternalForm();
    if (ext == null)
    {
      return;
    }
    try
    {
      factoryCache.put(new Element(ext, resource));
    }
    catch(Exception e)
    {
      // ignore ... the object is not serializable ..
    }
  }

  public void remove(final Resource resource)
  {
    final ResourceKey source = resource.getSource();
    final String ext = source.toExternalForm();
    if (ext == null)
    {
      return;
    }

    factoryCache.remove(ext);
  }

  public void clear()
  {
    try
    {
      factoryCache.removeAll();
    }
    catch (IOException e)
    {
      // ignore ..
    }
  }
}
