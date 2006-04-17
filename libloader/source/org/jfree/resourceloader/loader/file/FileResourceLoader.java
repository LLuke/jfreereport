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
 * FileResourceLoader.java
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
package org.jfree.resourceloader.loader.file;

import java.io.File;
import java.net.URL;

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

  public boolean isSupportedKeyValue(Object value)
  {
    if (value instanceof File)
    {
      return true;
    }
    if (value instanceof URL)
    {
      URL url = (URL) value;
      if ("file".equalsIgnoreCase(url.getProtocol()))
      {
        return true;
      }
    }
    if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith("file://"))
      {
        return true;
      }
      // we accept all strings, which actually point to a file.
      // this needs reconsidering: Shall we allow sloopy programming?
      // After all, it should not be that hard for users to stick to a clean
      // file name specification using URLs or file objects instead of plain
      // string filenames, shouldn't it?
//      File f = new File (valueString);
//      if (f.canRead() && f.isDirectory() == false)
//      {
//        return true;
//      }
    }
    return false;
  }

  public ResourceKey createKey(Object value) throws ResourceKeyCreationException
  {
    final File file = createFile(value);
    if (file != null)
    {
      return new FileResourceKey(file);
    }
    throw new ResourceKeyCreationException
            ("FileResourceLoader: This does not look like a valid file");
  }

  private File createFile (Object value)
  {
    if (value instanceof File)
    {
      return (File) value;
    }
    else if (value instanceof URL)
    {
      URL url = (URL) value;
      if ("file".equalsIgnoreCase(url.getProtocol()))
      {
        final String file = getPath(url);
        return new File (file);
      }
    }
    else if (value instanceof String)
    {
      String valueString = (String) value;
      if (valueString.startsWith("file://"))
      {
        final String file = valueString.substring(7);
        return new File (file);
      }
      // we accept all strings, which actually point to a file.
      File f = new File (valueString);
      if (f.canRead() && f.isDirectory() == false)
      {
        final String file = valueString.substring(7);
        return new File (file);
      }
    }
    return null;
  }


  /**
   * Implements the JDK 1.3 method URL.getPath(). The path is defined
   * as URL.getFile() minus the (optional) query.
   *
   * @param url the URL
   * @return the path
   */
  private String getPath (final URL url) {
      final String file = url.getFile();
      final int queryIndex = file.indexOf('?');
      if (queryIndex == -1) {
          return file;
      }
      return file.substring(0, queryIndex);
  }

  public ResourceKey deriveKey(ResourceKey parent, Object data)
          throws ResourceKeyCreationException
  {
    if (parent instanceof FileResourceKey == false)
    {
      throw new ResourceKeyCreationException
              ("Parent key format is not recognized.");
    }
    FileResourceKey key = (FileResourceKey) parent;
    if (data instanceof String)
    {
      return new FileResourceKey
              (new File (key.getFile().getParentFile(), (String) data));
    }
    throw new ResourceKeyCreationException
            ("Additional parameter format is not recognized.");
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
