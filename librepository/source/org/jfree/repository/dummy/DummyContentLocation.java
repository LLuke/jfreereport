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
 * DummyContentLocation.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.dummy;

import org.jfree.repository.ContentLocation;
import org.jfree.repository.ContentEntity;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentCreationException;
import org.jfree.repository.Repository;

/**
 * Creation-Date: 13.11.2006, 17:13:14
 *
 * @author Thomas Morgner
 */
public class DummyContentLocation implements ContentLocation
{
  private String name;
  private ContentLocation parent;
  private Repository repository;

  public DummyContentLocation(final ContentLocation parent, final String name)
  {
    this.repository = parent.getRepository();
    this.parent = parent;
    this.name = name;
  }

  public DummyContentLocation(final Repository repository, final String name)
  {
    this.repository = repository;
    this.name = name;
  }

  public ContentEntity[] listContents() throws ContentIOException
  {
    return new ContentEntity[0];
  }

  public ContentEntity getEntry(String name) throws ContentIOException
  {
    throw new ContentIOException();
  }

  public ContentItem createItem(String name) throws ContentCreationException
  {
    throw new ContentCreationException("Cannot create item");
  }

  public ContentLocation createLocation(String name)
      throws ContentCreationException
  {
    return new DummyContentLocation(this, name);
  }

  public boolean exists(final String name)
  {
    return false;
  }

  public String getName()
  {
    return name;
  }

  public Object getContentId()
  {
    return name;
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
