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
 * FileContentEntity.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.repository.file;

import java.io.File;
import java.util.Date;

import org.jfree.repository.ContentEntity;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.LibRepositoryBoot;
import org.jfree.repository.Repository;

/**
 * Creation-Date: 13.11.2006, 12:01:47
 *
 * @author Thomas Morgner
 */
public abstract class FileContentEntity implements ContentEntity
{
  private File backend;
  private ContentLocation parent;
  private Repository repository;

  protected FileContentEntity(final ContentLocation parent, final File backend)
  {
    this.repository = parent.getRepository();
    this.parent = parent;
    this.backend = backend;
  }

  protected FileContentEntity(final Repository repository, final File backend)
  {
    this.repository = repository;
    this.backend = backend;
  }

  public Repository getRepository()
  {
    return repository;
  }

  public String getName()
  {
    return backend.getName();
  }

  protected File getBackend()
  {
    return backend;
  }

  public Object getContentId()
  {
    return getName();
  }

  public Object getAttribute(String domain, String key)
  {
    if (LibRepositoryBoot.REPOSITORY_DOMAIN.equals(domain))
    {
      if (LibRepositoryBoot.SIZE_ATTRIBUTE.equals(key))
      {
        return new Long(backend.length());
      }
      else if (LibRepositoryBoot.VERSION_ATTRIBUTE.equals(key))
      {
        return new Date(backend.lastModified());
      }
    }
    return null;
  }

  public boolean setAttribute(String domain, String key, Object value)
  {
    if (LibRepositoryBoot.REPOSITORY_DOMAIN.equals(domain))
    {
      if (LibRepositoryBoot.VERSION_ATTRIBUTE.equals(key))
      {
        if (value instanceof Date)
        {
          Date date = (Date) value;
          return backend.setLastModified(date.getTime());
        }
        else if (value instanceof Number)
        {
          Number time = (Number) value;
          return backend.setLastModified(time.longValue());
        }
      }
    }
    return false;
  }

  public ContentLocation getParent()
  {
    return parent;
  }

  public boolean delete()
  {
    return backend.delete();
  }
}
