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
 * ZipResourceData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ZipResourceData.java,v 1.1.1.1 2006/04/17 16:48:33 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.loader.zip;

import java.io.InputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.loader.AbstractResourceData;
import org.jfree.resourceloader.loader.LoaderUtils;

/**
 * Creation-Date: 05.04.2006, 15:44:07
 *
 * @author Thomas Morgner
 */
public class ZipResourceData extends AbstractResourceData
{
  private ZipResourceKey key;

  public ZipResourceData(ZipResourceKey key)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    this.key = key;
  }

  public InputStream getResourceAsStream(ResourceManager caller) throws ResourceLoadingException
  {
    // again, this is going to hurt the performance.
    final ResourceKey parentKey = key.getZipFile();
    final ResourceData data = caller.load(parentKey);

    final ZipInputStream zin = new ZipInputStream(data.getResourceAsStream(caller));
    try
    {
      ZipEntry zipEntry = zin.getNextEntry();
      while (zipEntry != null)
      {
        if (zipEntry.getName().equals(key.getEntryName()) == false)
        {
          zipEntry = zin.getNextEntry();
          continue;
        }
        // read from here ..
        return zin;
      }
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException
              ("Reading the zip-file failed.", e);
    }

    throw new ResourceLoadingException
            ("The zip-file did not contain the specified entry");
  }

  public Object getAttribute(String key)
  {
    if (key.equals(ResourceData.FILENAME))
    {
      return LoaderUtils.getFileName(this.key.getEntryName());
    }
    return null;
  }

  public ResourceKey getKey()
  {
    return key;
  }

  public long getVersion(ResourceManager caller)
          throws ResourceLoadingException
  {
    final ResourceKey parentKey = key.getZipFile();
    final ResourceData data = caller.load(parentKey);
    return data.getVersion(caller);
  }
}
