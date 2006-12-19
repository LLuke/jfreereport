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
 * $Id: FileResourceLoader.java,v 1.4 2006/12/08 08:03:52 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jfree.resourceloader.AbstractResourceKey;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoader;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 05.04.2006, 14:17:56
 *
 * @author Thomas Morgner
 */
public class FileResourceLoader implements ResourceLoader
{
  private ResourceManager manager;

  public FileResourceLoader()
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
    return "file";
  }

  public boolean isSupportedKeyValue(Map values)
  {
    final Object value = values.get(AbstractResourceKey.CONTENT_KEY);
    if (value instanceof File)
    {
      return true;
    }
    if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith("file://"))
      {
        return true;
      }
    }
    return false;
  }

  public ResourceKey createKey(Map values) throws ResourceKeyCreationException
  {
    final Object value = values.get(AbstractResourceKey.CONTENT_KEY);
    if (value instanceof File)
    {
      return new FileResourceKey(values);
    }

    if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith("file://"))
      {
        valueString = valueString.substring(7);
      }

      // we accept all strings, which actually point to a file.
      File f = new File(valueString);
      final Map derivedValues = new HashMap(values);
      derivedValues.put(AbstractResourceKey.CONTENT_KEY, f);
      return new FileResourceKey(derivedValues);
    }
    throw new ResourceKeyCreationException
        ("FileResourceLoader: This does not look like a valid file");
  }

  public ResourceKey deriveKey(ResourceKey parent, Map data)
      throws ResourceKeyCreationException
  {
    if (parent instanceof FileResourceKey == false)
    {
      throw new ResourceKeyCreationException
          ("Parent key format is not recognized.");
    }
    FileResourceKey key = (FileResourceKey) parent;

    final Object deriveUrl = data.get(AbstractResourceKey.CONTENT_KEY);
    if (deriveUrl instanceof String == false)
    {
      throw new ResourceKeyCreationException
          ("Additional parameter format is not recognized.");
    }
    final Map derivedValues = new HashMap(parent.getParameters());
    derivedValues.putAll(data);
    derivedValues.put(AbstractResourceKey.CONTENT_KEY,
        new File(key.getFile().getParentFile(), (String) deriveUrl));
    return new FileResourceKey(derivedValues);
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    if (key instanceof FileResourceKey == false)
    {
      throw new ResourceLoadingException
          ("Key format is not recognized.");
    }
    return new FileResourceData((FileResourceKey) key);
  }
}
