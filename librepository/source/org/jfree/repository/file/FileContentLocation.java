/**
 * ===========================================================
 * LibRepository : a free Java content repository access layer
 * ===========================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/librepository/
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * FileContentLocation.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.repository.file;

import java.io.File;
import java.io.IOException;

import org.jfree.io.IOUtils;
import org.jfree.repository.ContentCreationException;
import org.jfree.repository.ContentEntity;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.Repository;

/**
 * Creation-Date: 13.11.2006, 12:01:11
 *
 * @author Thomas Morgner
 */
public class FileContentLocation extends FileContentEntity
  implements ContentLocation
{
  public FileContentLocation(final ContentLocation parent, final File backend)
  {
    super(parent, backend);
  }

  public FileContentLocation(final Repository repository, final File backend)
  {
    super(repository, backend);
  }

  public ContentEntity[] listContents() throws ContentIOException
  {
    final File file = getBackend();
    final File[] files = file.listFiles();
    final ContentEntity[] entities = new ContentEntity[files.length];
    for (int i = 0; i < files.length; i++)
    {
      final File child = files[i];
      if (child.isDirectory())
      {
        entities[i] = new FileContentLocation(this, child);
      }
      else if (child.isFile())
      {
        entities[i] = new FileContentLocation(this, child);
      }
    }
    return entities;
  }

  public ContentEntity getEntry(String name) throws ContentIOException
  {
    final File file = getBackend();
    final File child = new File (file, name);
    if (child.exists() == false)
    {
      throw new ContentIOException("Not found.");
    }
    try
    {
      if (IOUtils.getInstance().isSubDirectory(file, child))
      {
        throw new ContentIOException("Not sub-directory");
      }
    }
    catch (IOException e)
    {
      throw new ContentIOException("IO Error.");
    }

    if (child.isDirectory())
    {
      return new FileContentLocation(this, child);
    }
    else if (child.isFile())
    {
      return new FileContentItem(this, child);
    }
    else
    {
      throw new ContentIOException("Not File nor directory.");
    }
  }

  public ContentItem createItem(String name) throws ContentCreationException
  {
    final File file = getBackend();
    final File child = new File (file, name);
    if (child.exists())
    {
      throw new ContentCreationException("File already exists.");
    }
    try
    {
      if (child.createNewFile() == false)
      {
        throw new ContentCreationException("Unable to create");
      }
      return new FileContentItem(this, child);
    }
    catch (IOException e)
    {
      throw new ContentCreationException("IOError while create");
    }
  }

  public ContentLocation createLocation(String name)
      throws ContentCreationException
  {
    final File file = getBackend();
    final File child = new File (file, name);
    if (child.exists())
    {
      throw new ContentCreationException("File already exists.");
    }
    if (child.mkdir() == false)
    {
      throw new ContentCreationException("Unable to create");
    }
    return new FileContentLocation(this, child);
  }

  public boolean exists(final String name)
  {
    final File file = getBackend();
    final File child = new File (file, name);
    return (child.exists());
  }
}
