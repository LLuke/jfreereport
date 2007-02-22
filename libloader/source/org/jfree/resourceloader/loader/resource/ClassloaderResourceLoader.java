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
 * $Id: ClassloaderResourceLoader.java,v 1.4 2006/12/03 16:41:16 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader.resource;

import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import java.io.File;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoader;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.loader.LoaderUtils;

/**
 * Creation-Date: 05.04.2006, 14:40:59
 *
 * @author Thomas Morgner
 */
public class ClassloaderResourceLoader implements ResourceLoader
{
  private ResourceManager manager;

  public ClassloaderResourceLoader()
  {
  }

  public void setResourceManager(ResourceManager manager)
  {
    this.manager = manager;
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
    if (ClassloaderResourceLoader.class.getName().equals(key.getSchema()))
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
    if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith("res://"))
      {
        return new ResourceKey(ClassloaderResourceLoader.class.getName(),
            value, factoryKeys);
      }
    }

    //throw new ResourceKeyCreationException("Unsupported identifier.");
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

    final String resource;
    if (path.startsWith("res://"))
    {
      resource = path;
    }
    else if (path.startsWith("/"))
    {
      resource = "res:/" + path;
    }
    else
    {
      resource = LoaderUtils.mergePaths((String) parent.getIdentifier(), path);
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
    return new ResourceKey(parent.getSchema(), resource, map);
  }

  public URL toURL(ResourceKey key)
  {
    return null;
  }

  public ResourceManager getResourceManager()
  {
    return manager;
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    if (isSupportedKey(key) == false)
    {
      throw new ResourceLoadingException
              ("Key format is not recognized.");
    }
    return new ClassloaderResourceData(key);
  }

  /**
   * A helper method to make it easier to create resource descriptions.
   *
   * @param c
   * @param resource
   * @return
   */
  public static String createResourceKey (final Class c, final String resource)
  {
    if (c == null)
    {
      // the resource given should already be absolute ..
      return "res://" + resource;
    }
    final String className = c.getName();
    final int lastDot = className.lastIndexOf('.');
    if (lastDot < 0)
    {
      return "res://" + resource;
    }
    else
    {
      final String packageName = className.substring(0, lastDot);
      final String packagePath = packageName.replace('.', '/');
      return "res://" + packageName + "/" + packagePath;
    }
  }
}
