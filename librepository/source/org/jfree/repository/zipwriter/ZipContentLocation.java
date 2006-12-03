/**
 * ===========================================================
 * LibRepository : a free Java content repository access layer
 * ===========================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/librepository/
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
 * ZipContentLocation.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.zipwriter;

import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.io.IOException;

import org.jfree.repository.ContentLocation;
import org.jfree.repository.ContentEntity;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentCreationException;
import org.jfree.repository.Repository;
import org.jfree.repository.RepositoryUtilities;

/**
 * Creation-Date: 01.12.2006, 21:13:24
 *
 * @author Thomas Morgner
 */
public class ZipContentLocation implements ContentLocation
{
  private HashMap entries;
  private String name;
  private String contentId;
  private ContentLocation parent;
  private ZipRepository repository;

  public ZipContentLocation(final ZipRepository repository,
                            final ContentLocation parent,
                            final String name)
  {
    this.repository = repository;
    this.parent = parent;
    this.name = name;
    this.entries = new HashMap();
    this.contentId = RepositoryUtilities.buildName(this, "/") + "/";
  }

  public ContentEntity[] listContents() throws ContentIOException
  {
    return (ContentEntity[]) entries.values().toArray
        (new ContentEntity[entries.size()]);
  }

  public ContentEntity getEntry(String name) throws ContentIOException
  {
    return (ContentEntity) entries.get(name);
  }

  /**
   * Creates a new data item in the current location. This method must never
   * return null.
   *
   * @param name
   * @return
   * @throws org.jfree.repository.ContentCreationException
   *          if the item could not be created.
   */
  public ContentItem createItem(String name) throws ContentCreationException
  {
    if (entries.containsKey(name))
    {
      throw new ContentCreationException("Entry already exists");
    }

    final ZipContentItem item = new ZipContentItem(name, repository, this);
    entries.put (name, item);
    return item;
  }

  public ContentLocation createLocation(String name)
      throws ContentCreationException
  {
    if (entries.containsKey(name))
    {
      throw new ContentCreationException("Entry already exists");
    }

    final ZipContentLocation item = new ZipContentLocation(repository, this, name);
    entries.put (name, item);

    try
    {
      final ZipEntry entry = new ZipEntry(contentId);
      repository.writeDirectory(entry);
      return item;
    }
    catch (IOException e)
    {
      throw new ContentCreationException("Failed to create directory.");
    }
  }

  public boolean exists(final String name)
  {
    return entries.containsKey(name);
  }

  public String getName()
  {
    return name;
  }

  public Object getContentId()
  {
    return contentId;
  }

  public Object getAttribute(String domain, String key)
  {
    return null;
  }

  public boolean setAttribute(String domain, String key, Object value)
  {
    return false;
  }

  public ContentLocation getParent()
  {
    return parent;
  }

  public Repository getRepository()
  {
    return repository;
  }

  public boolean delete()
  {
    return false;
  }
}
