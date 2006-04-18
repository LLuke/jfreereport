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
 * ResourceDataCache.java
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
package org.jfree.resourceloader.cache;

import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceData;

/**
 * Creation-Date: 06.04.2006, 09:30:28
 *
 * @author Thomas Morgner
 */
public interface ResourceDataCache
{
  /**
   * Retrieves the given data from the cache.
   *
   * @param data
   */
  public ResourceDataCacheEntry get (ResourceKey key);

  /**
   * Stores the given data on the cache. The data is registered by its
   * primary key. The cache has to store the current version of the data.
   *
   * @param data the data to be stored in the cache
   * @return the resource data object, possibly wrapped by a cache-specific
   * implementation.
   */
  public ResourceData put (ResourceData data);

  public void remove (ResourceData data);

  /**
   * Remove all cached entries. This should be called after the cache has
   * become invalid or after it has been removed from a resource manager.
   */
  public void clear();
}