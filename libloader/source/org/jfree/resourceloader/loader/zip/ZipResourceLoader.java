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
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.loader.zip;

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

  public String getSchema()
  {
    return "zip";
  }

  public boolean isSupportedKeyValue(Object value)
  {
    // going the easy way ..
    if (value instanceof ZipEntryKey)
    {
      return true;
    }
    return false;
  }

  public ResourceKey createKey(Object value) throws ResourceKeyCreationException
  {
    if (value instanceof ZipEntryKey == false)
    {
      throw new ResourceKeyCreationException();
    }
    final ZipEntryKey ek = (ZipEntryKey) value;
    return new ZipResourceKey(ek.getZipFile(), ek.getEntryName());
  }

  public ResourceKey deriveKey(ResourceKey parent, Object data)
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
    if (data instanceof ZipEntryKey)
    {
      final ZipEntryKey ek = (ZipEntryKey) data;
      return new ZipResourceKey(ek.getZipFile(), ek.getEntryName());
    }
    if (data instanceof String == false)
    {
      throw new ResourceKeyCreationException
              ("Parameter format is not recognized.");
    }
    String entryName = (String) data;
    String entry = LoaderUtils.mergePaths(zrk.getEntryName(), entryName);
    return new ZipResourceKey(zrk.getZipFile(), entry);
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
