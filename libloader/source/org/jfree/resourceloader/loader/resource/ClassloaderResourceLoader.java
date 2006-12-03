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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader.resource;

import java.util.HashMap;
import java.util.Map;

import org.jfree.resourceloader.AbstractResourceKey;
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

  public ResourceManager getManager()
  {
    return manager;
  }

  public String getSchema()
  {
    return "res";
  }

  public boolean isSupportedKeyValue(Map values)
  {
    final Object value = values.get(AbstractResourceKey.CONTENT_KEY);
    if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith("res://"))
      {
        return true;
      }
    }
    return false;
  }

  public ResourceKey createKey(Map values) throws ResourceKeyCreationException
  {
    final Object value = values.get(AbstractResourceKey.CONTENT_KEY);
    if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith("res://"))
      {
        return new ClassloaderResourceKey(values);
      }
    }
    throw new ResourceKeyCreationException("Unable to create the resource key");
  }

  public ResourceKey deriveKey(ResourceKey parent, Map values)
          throws ResourceKeyCreationException
  {
    if (parent instanceof ClassloaderResourceKey == false)
    {
      throw new ResourceKeyCreationException
              ("Parent key format is not recognized.");
    }
    final ClassloaderResourceKey resourceKey = (ClassloaderResourceKey) parent;
    final String parentResourceKey = resourceKey.getResource();
    final Object data = values.get(AbstractResourceKey.CONTENT_KEY);
    if (data instanceof String == false)
    {
      throw new ResourceKeyCreationException
              ("Additional parameter format is not recognized.");
    }

    final String childResource = (String) data;
    final String resource;
    if (childResource.startsWith("res://"))
    {
      resource = childResource;
    }
    else if (childResource.startsWith("/"))
    {
      resource = "res:/" + childResource;
    }
    else
    {
      resource = LoaderUtils.mergePaths(parentResourceKey, childResource);
    }
    final Map derivedValues = new HashMap (parent.getParameters());
    derivedValues.putAll(values);
    derivedValues.put(AbstractResourceKey.CONTENT_KEY, resource);
    return new ClassloaderResourceKey(derivedValues);
  }


  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    if (key instanceof ClassloaderResourceKey == false)
    {
      throw new ResourceLoadingException
              ("Key format is not recognized.");
    }
    return new ClassloaderResourceData((ClassloaderResourceKey) key);
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
