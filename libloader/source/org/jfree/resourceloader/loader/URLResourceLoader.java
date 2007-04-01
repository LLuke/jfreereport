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
package org.jfree.resourceloader.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoader;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 05.04.2006, 15:32:36
 *
 * @author Thomas Morgner
 */
public class URLResourceLoader implements ResourceLoader
{
  private ResourceManager manager;

  public URLResourceLoader()
  {
  }

  public void setResourceManager(ResourceManager manager)
  {
    this.manager = manager;
  }

  public ResourceManager getResourceManager()
  {
    return manager;
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
    if (URLResourceLoader.class.getName().equals(key.getSchema()))
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
    if (value instanceof URL)
    {
      return new ResourceKey
          (URLResourceLoader.class.getName(), value, factoryKeys);
    }
    if (value instanceof String)
    {
      String valueString = (String) value;
      // the 'file' schema is defined to have double-slashes, but the JDK
      // ignores RFC 1738 in that case. So we have to check for these beasts
      // directly ..
      if (valueString.indexOf("://") >= 0 ||
          valueString.startsWith("file:/"))
      {
        try
        {
          return new ResourceKey(URLResourceLoader.class.getName(),
              new URL(valueString), factoryKeys);

        }
        catch (MalformedURLException mfue)
        {
          // we dont take this easy!
          throw new ResourceKeyCreationException("Malformed value: " + value);
        }
      }
    }

    return null;
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

    try
    {
      URL url;
      if (path != null)
      {
        url = new URL((URL) parent.getIdentifier(), path);
      }
      else
      {
        url = (URL) parent.getIdentifier();
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
      return new ResourceKey(parent.getSchema(), url, map);
    }
    catch (MalformedURLException e)
    {
      throw new ResourceKeyCreationException("Malformed value: " + path);
    }
  }

  public URL toURL(ResourceKey key)
  {
    return (URL) key.getIdentifier();
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    if (isSupportedKey(key) == false)
    {
      throw new ResourceLoadingException
          ("Key format is not recognized.");
    }
    return new URLResourceData(key);
  }
}
