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
 * DummyContentItem.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.dummy;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.Repository;

/**
 * Creation-Date: 13.11.2006, 17:16:02
 *
 * @author Thomas Morgner
 */
public class DummyContentItem implements ContentItem
{
  private ContentLocation parent;
  private String name;

  public DummyContentItem(final ContentLocation parent, final String name)
  {
    this.parent = parent;
    this.name = name;
  }

  public String getMimeType() throws ContentIOException
  {
    return getRepository().getMimeRegistry().getMimeType(this);
  }

  public OutputStream getOutputStream() throws ContentIOException, IOException
  {
    return new NullOutputStream();
  }

  public InputStream getInputStream() throws ContentIOException, IOException
  {
    return new ByteArrayInputStream(new byte[0]);
  }

  public boolean isReadable()
  {
    return false;
  }

  public boolean isWriteable()
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
    return parent.getRepository();
  }

  public boolean delete()
  {
    return false;
  }
}
