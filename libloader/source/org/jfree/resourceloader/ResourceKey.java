/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libloader/
 * Project Lead:  Thomas Morgner;
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
 * ResourceKey.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader;

import java.io.Serializable;

/**
 * The key is an unique identifier for the resource. Most of the time,
 * this may be an URL, but other (especially database based) schemas are
 * possible.
 *
 * A resource key must provide an 'equals' implementation. ResourceKeys should
 * be implemented as immutable classes, so that they can be safely stored in
 * collections or on external storages (like caches).
 *
 * @author Thomas Morgner
 */
public interface ResourceKey extends Serializable
{
  /**
   *
   * @return true, if the given resource key is equal to this key.
   */
  public boolean equals(Object o);

  /**
   * Returns the schema of this resource key. The schema can be mapped to
   * a known resource loader. If no resource loader is available for the given
   * schema, the resource will be unavailable.
   *
   * @return
   */
  public String getSchema ();

  /**
   * Creates a unique identifier for this key.
   *
   * The following statement must be true for all external forms generated by
   * this method:
   * (key1.equals(key2) == key1.toExternalForm().equals(key2.toExternalForm())
   *
   * @return
   */
  public String toExternalForm();
}