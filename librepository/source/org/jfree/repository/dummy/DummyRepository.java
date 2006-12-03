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
 * DummyRepository.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository.dummy;

import org.jfree.repository.Repository;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.MimeRegistry;
import org.jfree.repository.DefaultMimeRegistry;

/**
 * Creation-Date: 13.11.2006, 17:12:54
 *
 * @author Thomas Morgner
 */
public class DummyRepository implements Repository
{
  private DummyContentLocation location;
  private MimeRegistry mimeRegistry;

  public DummyRepository()
  {
    location = new DummyContentLocation(this, "");
    mimeRegistry = new DefaultMimeRegistry();
  }

  public ContentLocation getRoot()
  {
    return location;
  }

  public MimeRegistry getMimeRegistry()
  {
    return mimeRegistry;
  }
}
