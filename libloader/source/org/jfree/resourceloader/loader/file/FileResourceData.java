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
 * $Id: FileResourceData.java,v 1.3 2006/12/03 16:41:16 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.loader.AbstractResourceData;

/**
 * A simple file reader. This class, as all core implementation, does not hold
 * any references to the data read from the file. Caching is left to the cache
 * provider.
 *
 * @author Thomas Morgner
 */
public class FileResourceData extends AbstractResourceData
{
  private ResourceKey key;
  private File file;

  public FileResourceData(final ResourceKey key) throws ResourceLoadingException
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    final File file = (File) key.getIdentifier();
    if (file.isFile() == false)
    {
      throw new ResourceLoadingException
              ("File-handle given does not point to a regular file.");
    }
    if (file.canRead() == false)
    {
      throw new ResourceLoadingException
              ("File '" + file + "' is not readable.");
    }
    this.key = key;
    this.file = file;
  }

  public InputStream getResourceAsStream(ResourceManager caller) throws ResourceLoadingException
  {
    try
    {
      return new BufferedInputStream(new FileInputStream(file));
    }
    catch (FileNotFoundException e)
    {
      throw new ResourceLoadingException("Unable to open Stream: ", e);
    }
  }

  public Object getAttribute(String attrkey)
  {
    if (attrkey.equals(ResourceData.FILENAME))
    {
      return file.getName();
    }
    if (attrkey.equals(ResourceData.CONTENT_LENGTH))
    {
      return new Long(file.length());
    }
    return null;
  }

  public long getVersion(ResourceManager caller)
          throws ResourceLoadingException
  {
    return file.lastModified();
  }

  public ResourceKey getKey()
  {
    return key;
  }
}
