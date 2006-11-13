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

import java.io.InputStream;
import java.io.OutputStream;

import org.jfree.repository.ContentLocation;
import org.jfree.repository.ContentEntity;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentCreationException;
import org.jfree.repository.Repository;

/**
 * Creation-Date: 13.11.2006, 17:23:40
 *
 * @author Thomas Morgner
 */
public class StreamContentLocation implements ContentLocation
{
  private ContentItem contentItem;
  private Repository repository;

  public StreamContentLocation(Repository repository)
  {
    this.repository = repository;
  }

  public ContentEntity[] listContents() throws ContentIOException
  {
    return new ContentEntity[0];
  }

  public ContentEntity getEntry(String name) throws ContentIOException
  {
    return null;
  }

  public ContentItem createItem(String name) throws ContentCreationException
  {
    return null;
  }

  public ContentLocation createLocation(String name)
      throws ContentCreationException
  {
    return null;
  }

  public boolean exists(final String name)
  {
    return false;
  }

  public String getName()
  {
    return null;
  }

  public Object getContentId()
  {
    return null;
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
    return null;
  }

  public boolean delete()
  {
    return false;
  }
}
