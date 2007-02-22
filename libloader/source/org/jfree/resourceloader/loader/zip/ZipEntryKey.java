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
 * $Id: ZipEntryKey.java,v 1.2 2006/12/03 16:41:16 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader.zip;

import java.io.File;
import java.net.URL;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * An external zip key.
 *
 * @author Thomas Morgner
 */
public class ZipEntryKey 
{
  private ResourceData zipFile;
  private String entryName;

  public ZipEntryKey(final ResourceData zipFile,
                     final String entryName)
  {
    if (zipFile == null)
    {
      throw new NullPointerException();
    }
    if (entryName == null)
    {
      throw new NullPointerException();
    }
    this.zipFile = zipFile;
    this.entryName = entryName;
  }

  public ZipEntryKey(final ResourceManager manager,
                     final String zipFile,
                     final String entryName)
          throws ResourceKeyCreationException, ResourceLoadingException
  {
    if (zipFile == null)
    {
      throw new NullPointerException();
    }
    if (entryName == null)
    {
      throw new NullPointerException();
    }
    this.zipFile = manager.load(manager.createKey(zipFile));
    this.entryName = entryName;
  }


  public ZipEntryKey(final ResourceManager manager,
                     final URL zipFile,
                     final String entryName)
          throws ResourceKeyCreationException, ResourceLoadingException
  {
    if (zipFile == null)
    {
      throw new NullPointerException();
    }
    if (entryName == null)
    {
      throw new NullPointerException();
    }
    this.zipFile = manager.load(manager.createKey(zipFile));
    this.entryName = entryName;
  }

  public ZipEntryKey(final ResourceManager manager,
                     final File zipFile,
                     final String entryName)
          throws ResourceKeyCreationException, ResourceLoadingException
  {
    if (zipFile == null)
    {
      throw new NullPointerException();
    }
    if (entryName == null)
    {
      throw new NullPointerException();
    }
    this.zipFile = manager.load(manager.createKey(zipFile));
    this.entryName = entryName;
  }

  public ResourceData getZipFile()
  {
    return zipFile;
  }

  public String getEntryName()
  {
    return entryName;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final ZipEntryKey that = (ZipEntryKey) o;

    if (!entryName.equals(that.entryName))
    {
      return false;
    }
    if (!zipFile.getKey().equals(that.zipFile.getKey()))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = zipFile.getKey().hashCode();
    result = 29 * result + entryName.hashCode();
    return result;
  }
}
