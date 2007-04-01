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
package org.jfree.resourceloader.loader.raw;

import java.util.Map;
import java.util.HashMap;
import java.net.URL;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoader;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 12.04.2006, 15:19:03
 *
 * @author Thomas Morgner
 */
public class RawResourceLoader implements ResourceLoader
{
  private ResourceManager resourceManager;

  public RawResourceLoader()
  {
  }

  /**
   * Checks, whether this resource loader implementation was responsible for
   * creating this key.
   *
   * @param key
   * @return
   */
  public boolean isSupportedKey(ResourceKey key)
  {
    if (RawResourceLoader.class.getName().equals(key.getSchema()))
    {
      return true;
    }
    return false;
  }

  /**
   * Creates a new resource key from the given object and the factory keys.
   *
   * @param value
   * @param factoryKeys
   * @return the created key.
   * @throws org.jfree.resourceloader.ResourceKeyCreationException
   *          if creating the key failed.
   */
  public ResourceKey createKey(Object value, Map factoryKeys)
      throws ResourceKeyCreationException
  {
    if (value instanceof byte[] == false)
    {
      return null;
    }

    return new ResourceKey(RawResourceLoader.class.getName(), value, factoryKeys);
  }

  /**
   * Derives a new resource key from the given key. If neither a path nor new
   * factory-keys are given, the parent key is returned.
   *
   * @param parent      the parent
   * @param path        the derived path (can be null).
   * @param factoryKeys the optional factory keys (can be null).
   * @return the derived key.
   * @throws org.jfree.resourceloader.ResourceKeyCreationException
   *          if the key cannot be derived for any reason.
   */
  public ResourceKey deriveKey(ResourceKey parent, String path, Map factoryKeys)
      throws ResourceKeyCreationException
  {
    if (path != null)
    {
      throw new ResourceKeyCreationException("Unable to derive key for new path.");
    }
    if (isSupportedKey(parent) == false)
    {
      throw new ResourceKeyCreationException("Assertation: Unsupported parent key type");
    }

    if (factoryKeys == null) return parent;

    HashMap map = new HashMap();
    map.putAll(parent.getFactoryParameters());
    map.putAll(factoryKeys);
    return new ResourceKey(parent.getSchema(), parent.getIdentifier(), map);
  }

  public URL toURL(ResourceKey key)
  {
    // not supported ..
    return null;
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    if (isSupportedKey(key) == false)
    {
      throw new ResourceLoadingException("The key type is not supported.");
    }
    return new RawResourceData(key);
  }

  public ResourceManager getResourceManager()
  {
    return resourceManager;
  }

  public void setResourceManager(ResourceManager manager)
  {
    this.resourceManager = manager;
  }
}
