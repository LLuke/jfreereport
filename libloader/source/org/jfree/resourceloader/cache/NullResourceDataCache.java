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
package org.jfree.resourceloader.cache;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;

/**
 * Disables caching. It simply returns null on every request and ignores
 * the put requests. You certainly want to use one of the other cache
 * providers in real world applications.
 *
 * @author Thomas Morgner
 */
public class NullResourceDataCache implements ResourceDataCache
{
  public NullResourceDataCache()
  {
  }

  public ResourceData put(ResourceManager caller, ResourceData data)
  {
    return CachingResourceData.createCached(data);
  }

  public ResourceDataCacheEntry get(ResourceKey key)
  {
    return null;
  }

  public void remove(ResourceData data)
  {

  }

  public void clear()
  {

  }
}
