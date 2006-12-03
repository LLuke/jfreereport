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
 * ZipContentItem.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.zipwriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.Repository;
import org.jfree.repository.RepositoryUtilities;

/**
 * Creation-Date: 01.12.2006, 21:23:25
 *
 * @author Thomas Morgner
 */
public class ZipContentItem implements ContentItem
{
  private boolean newItem;
  private String name;
  private String contentId;
  private ZipRepository repository;
  private ZipContentLocation parent;

  public ZipContentItem(final String name,
                        final ZipRepository repository,
                        final ZipContentLocation parent)
  {
    this.name = name;
    this.repository = repository;
    this.parent = parent;
    this.contentId = RepositoryUtilities.buildName(this, "/");
    this.newItem = true;
  }

  public String getMimeType() throws ContentIOException
  {
    return getRepository().getMimeRegistry().getMimeType(this);
  }

  public OutputStream getOutputStream() throws ContentIOException, IOException
  {
    if (newItem == false)
    {
      throw new ContentIOException("This item is no longer writeable.");
    }
    newItem = false;
    return new ZipEntryOutputStream(this);
  }

  public InputStream getInputStream() throws ContentIOException, IOException
  {
    throw new ContentIOException("This item is not readable.");
  }

  public boolean isReadable()
  {
    return false;
  }

  public boolean isWriteable()
  {
    return newItem;
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

  public Repository getRepository()
  {
    return repository;
  }

  public ContentLocation getParent()
  {
    return parent;
  }

  public boolean delete()
  {
    return false;
  }
}
