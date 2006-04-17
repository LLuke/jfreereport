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
 * FileResourceData.java
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
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
  private FileResourceKey key;

  public FileResourceData(final FileResourceKey key) throws ResourceLoadingException
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    final File file = key.getFile();
    if (file.isDirectory())
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
  }

  public InputStream getResourceAsStream() throws ResourceLoadingException
  {
    try
    {
      return new BufferedInputStream(new FileInputStream(key.getFile()));
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
      return key.getFile().getName();
    }
    if (attrkey.equals(ResourceData.CONTENT_LENGTH))
    {
      return new Long(key.getFile().length());
    }
    return null;
  }

  public long getVersion()
  {
    return key.getFile().lastModified();
  }

  public ResourceKey getKey()
  {
    return key;
  }
}
