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
 * ContentLocation.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository;

/**
 * This represents a container in the repository. If the repository is a
 * filesystem, this will be a directory.
 *
 * @author Thomas Morgner
 */
public interface ContentLocation extends ContentEntity
{
  public ContentEntity[] listContents() throws ContentIOException;

  public ContentEntity getEntry (String name) throws ContentIOException;

  /**
   * Creates a new data item in the current location. This method must never
   * return null.
   *
   * @param name
   * @return
   * @throws ContentCreationException if the item could not be created.
   */
  public ContentItem createItem (String name) throws ContentCreationException;
  public ContentLocation createLocation (String name) throws ContentCreationException;

  public boolean exists(final String name);
}
