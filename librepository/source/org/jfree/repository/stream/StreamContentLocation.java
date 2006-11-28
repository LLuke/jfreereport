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
 * StreamContentLocation.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.repository.stream;

import org.jfree.repository.ContentCreationException;
import org.jfree.repository.ContentEntity;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.Repository;

/**
 * Creation-Date: 13.11.2006, 17:23:40
 *
 * @author Thomas Morgner
 */
public class StreamContentLocation implements ContentLocation
{
  private ContentItem contentItem;
  private StreamRepository repository;

  public StreamContentLocation(final StreamRepository repository)
  {
    this.repository = repository;
  }

  public ContentEntity[] listContents() throws ContentIOException
  {
    if (contentItem == null)
    {
      return new ContentEntity[0];
    }
    else
    {
      return new ContentEntity[]{contentItem};
    }
  }

  public ContentEntity getEntry(String name) throws ContentIOException
  {
    if (contentItem == null)
    {
      throw new ContentIOException("No such item");
    }
    if (contentItem.getName().equals(name))
    {
      return contentItem;
    }
    throw new ContentIOException("No such item");
  }

  public ContentItem createItem(String name) throws ContentCreationException
  {
    if (contentItem == null)
    {
      contentItem = new StreamContentItem(name, this,
          repository.getInputStream(), repository.getOutputStream());
      return contentItem;
    }
    throw new ContentCreationException
        ("Failed to create the item. Item already there");
  }

  public ContentLocation createLocation(String name)
      throws ContentCreationException
  {
    throw new ContentCreationException
        ("Failed to create the item. Item already there");
  }

  public boolean exists(final String name)
  {
    if (contentItem == null)
    {
      return false;
    }
    return contentItem.getName().equals(name);
  }

  public String getName()
  {
    return "root";
  }

  public Object getContentId()
  {
    return getName();
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
    return null;
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
