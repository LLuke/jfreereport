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
 * ZipResourceLoader.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ZipResourceLoader.java,v 1.2 2006/05/16 17:13:30 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.loader.zip;

import java.util.Map;
import java.util.HashMap;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoader;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.AbstractResourceKey;
import org.jfree.resourceloader.loader.LoaderUtils;
import org.jfree.resourceloader.loader.URLResourceKey;

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

  public String getSchema()
  {
    return "zip";
  }

  public boolean isSupportedKeyValue(Map values)
  {
    try
    {
      final ResourceKey parent = (ResourceKey) values.get(AbstractResourceKey.PARENT_KEY);
      final String entry = (String) values.get(AbstractResourceKey.CONTENT_KEY);
      if (parent != null && entry != null) return true;
    }
    catch(ClassCastException cce)
    {
      return false;
    }
    return false;
  }

  public ResourceKey createKey(Map values) throws ResourceKeyCreationException
  {
    return new ZipResourceKey(values);
  }

  public ResourceKey deriveKey(ResourceKey parent, Map values)
          throws ResourceKeyCreationException
  {
    // shall we allow to dive into a zip file contained in a zip file using
    // this method?
    // For that we have to check, whether one of the intermediate entries
    // will point to a zip file and then we will have to dive into that file
    // This sounds like hard work, so leave it out for now ...
    if (parent instanceof ZipResourceKey == false)
    {
      throw new ResourceKeyCreationException
              ("Key format is not recognized.");
    }
    ZipResourceKey zrk = (ZipResourceKey) parent;
    final ResourceKey declaredParent = (ResourceKey)
            values.get(AbstractResourceKey.PARENT_KEY);
    final Object entryName = values.get(AbstractResourceKey.CONTENT_KEY);
    if (entryName instanceof String == false)
    {
      throw new ResourceKeyCreationException
              ("Parameter format is not recognized.");
    }

    if (declaredParent == null || declaredParent.equals(parent))
    {
      final String entry = LoaderUtils.mergePaths
              (zrk.getEntryName(), String.valueOf(entryName));
      final Map derivedValues = new HashMap (parent.getParameters());
      derivedValues.putAll(values);
      derivedValues.put(AbstractResourceKey.PARENT_KEY, zrk);
      derivedValues.put(AbstractResourceKey.CONTENT_KEY, entry);
      return new ZipResourceKey(derivedValues);
    }
    else
    {
      return new ZipResourceKey(values);
    }
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    if (key instanceof ZipResourceKey == false)
    {
      throw new ResourceLoadingException
              ("Key format is not recognized.");
    }
    return new ZipResourceData((ZipResourceKey) key);
  }

  public void setResourceManager(ResourceManager manager)
  {
    this.manager = manager;
  }

  public ResourceManager getManager()
  {
    return manager;
  }
}
