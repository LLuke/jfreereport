/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: UnmodifiableGroupList.java,v 1.2 2003/04/09 15:45:48 mungady Exp $
 *
 * Changes
 * -------
 * 05.04.2003 : Initial version
 * 
 */

package com.jrefinery.report;

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
  public UnmodifiableGroupList(GroupList list)
  {
    super (list);
  }

  /**
   * Removes an object from the list.
   *
   * @param o  the object.
   *
   * @throws UnsupportedOperationException as this GroupList is not modifiable.
   */
  public boolean remove(Object o)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds an object to the list.
   *
   * @param o  the object (must be an instance of the Group class).
   * @throws UnsupportedOperationException as this GroupList is not modifiable.
   */
  public void add(Group o)
  {
    throw new UnsupportedOperationException();
  }
}
