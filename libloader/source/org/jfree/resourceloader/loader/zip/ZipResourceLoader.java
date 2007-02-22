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
 * $Id: ZipResourceLoader.java,v 1.4 2006/12/03 16:41:16 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader.zip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoader;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.loader.LoaderUtils;

/**
 * Creation-Date: 05.04.2006, 15:53:21
 *
 * @author Thomas Morgner
 */
public class ZipResourceLoader implements ResourceLoader
{
  private ResourceManager manager;

  public ZipResourceLoader()
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
    return ZipResourceLoader.class.getName().equals(key.getSchema());
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
    if (value instanceof ZipEntryKey == false)
    {
      return null;
    }

    final ZipEntryKey entryKey = (ZipEntryKey) value;
    final ResourceKey parentKey = entryKey.getZipFile().getKey();

    return new ResourceKey(parentKey, ZipResourceLoader.class.getName(),
        entryKey.getEntryName(), factoryKeys);
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
    if (isSupportedKey(parent) == false)
    {
      throw new ResourceKeyCreationException("Assertation: Unsupported parent key type");
    }

    String entry;
    if (path != null)
    {
      entry = LoaderUtils.mergePaths((String) parent.getIdentifier(), path);
    }
    else
    {
      entry = (String) parent.getIdentifier();
    }

    Map map;
    if (factoryKeys != null)
    {
      map = new HashMap();
      map.putAll(parent.getFactoryParameters());
      map.putAll(factoryKeys);
    }
    else
    {
      map = parent.getFactoryParameters();
    }
    return new ResourceKey(parent.getParent(), parent.getSchema(), entry, map);
  }

  public URL toURL(ResourceKey key)
  {
    return null;
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    if (isSupportedKey(key) == false)
    {
      throw new ResourceLoadingException
              ("Key format is not recognized.");
    }
    return new ZipResourceData(key);
  }

  public void setResourceManager(ResourceManager manager)
  {
    this.manager = manager;
  }

  public ResourceManager getResourceManager()
  {
    return manager;
  }
}
