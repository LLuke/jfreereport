/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * --------------------------
 * UnmodifiableGroupList.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: UnmodifiableGroupList.java,v 1.1 2003/07/07 22:43:59 taqua Exp $
 *
 * Changes
 * -------
 * 05.04.2003 : Initial version
 *
 */

package org.jfree.report;

/**
 * An unmodifiable list of groups.
 *
 * @author Thomas Morgner.
 */
public class UnmodifiableGroupList extends GroupList
{
  /**
   * Creates a new Unmodifiable GroupList by copying the contents from the given
   * list.
   *
   * @param list  the base list.
   */
  public UnmodifiableGroupList(final GroupList list)
  {
    super(list);
  }

  /**
   * Removes an object from the list.
   *
   * @param o  the object.
   * @return nothing, as this method always fires a UnsupportedOperation exception.
   * @throws UnsupportedOperationException as this GroupList is not modifiable.
   */
  public boolean remove(final Object o)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds an object to the list.
   *
   * @param o  the object (must be an instance of the Group class).
   * @throws UnsupportedOperationException as this GroupList is not modifiable.
   */
  public void add(final Group o)
  {
    throw new UnsupportedOperationException();
  }
}
