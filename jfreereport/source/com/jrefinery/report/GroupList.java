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
 * --------------
 * GroupList.java
 * --------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: GroupList.java,v 1.21 2003/04/06 18:10:54 taqua Exp $
 *
 * Changes:
 * --------
 * 11-May-2002 : Version 1 (TM);
 * 16-May-2002 : Added Javadoc comments (DG);
 * 29-Aug-2002 : TreeSet does no cloning in JDK 1.2.2, it returns a "new TreeSet()".
 *               Why would a sane programmer mess up the source like this?
 * 06-Dec-2002 : Added validity check to the group list.
 * 10-Dec-2002 : Updated Javadocs (DG);
 * 04-Feb-2003 : Implemented hashCode for GroupComparator
 */

package com.jrefinery.report;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReadOnlyIterator;

/**
 * The group list is used to store groups in a ordered way. The less specific groups are
 * guaranteed to be listed before any more specific subgroup.
 * <p>
 * Groups are ordered by comparing the declared fieldnames for the groups.
 * A subgroup of an group must contain all fields from its parent plus at least one
 * new field.
 * <p>
 * This implementation is not synchronized.
 *
 * @author Thomas Morgner
 */
public class GroupList implements Cloneable, Serializable
{
  /** Cache (this is a set, we need list functionality, but creating Iterators is expensive). */
  private Object[] cache;
  
  /** Storage for the groups. */
  private TreeSet backend;

  /**
   * Constructs a new empty group list.
   */
  public GroupList()
  {
    backend = new TreeSet ();
  }

  /**
   * Creates a new group list.
   * 
   * @param list  groups to add to the list.
   */
  public GroupList(GroupList list)
  {
    backend = new TreeSet();
    backend.addAll(list.backend);
  }

  /**
   * Returns the group at a position in the list.
   *
   * @param i  the position index (zero-based).
   *
   * @return the report group.
   */
  public Group get(int i)
  {
    if (cache == null)
    {
      cache = backend.toArray();
    }
    return (Group) cache[i];
  }

  /**
   * Removes an object from the list.
   *
   * @param o  the object.
   *
   * @return a boolean indicating whether or not the object was removed.
   */
  public boolean remove(Object o)
  {
    cache = null;
    return backend.remove(o);
  }

  /**
   * Clears the list.
   */
  public void clear()
  {
    backend.clear();
    cache = null;
  }

  /**
   * Adds an object to the list.
   *
   * @param o  the object (must be an instance of the Group class).
   *
   * @return true if the list did not already contain the specified element.
   * Returns always true, as the old value is removed if needed.
   */
  public boolean add(Object o)
  {
    if (o == null)
    {
      throw new NullPointerException("Try to add null");
    }
    if (o instanceof Group)
    {
      cache = null;
      if (backend.add(o) == false)
      {
        backend.remove(o);
        return backend.add(o);
      }
      return true;
    }
    else
    {
      throw new ClassCastException("Group required, was " + o.getClass().getName());
    }
  }

  /**
   * Clones the list.
   * <p>
   * Warning: No real cloning involved due to a bug in JDK 1.2.2; TreeSet does not clone, so
   * we can't do it either.
   *
   * @return a clone.
   */
  public Object clone()
  {
    GroupList l = null;
    try
    {
      l = (GroupList) super.clone();
      l.backend = new TreeSet();
      l.clear();
      for (int i = 0; i < backend.size(); i++)
      {
        l.backend.add(get(i).clone());
      }
    }
    catch (CloneNotSupportedException cne)
    {
      Log.error ("GroupsList was not cloned.");
      throw new IllegalStateException("GroupList was not cloneable.");
    }
    return l;
  }

  /**
   * Returns an iterator for the list.
   * 
   * @return An iterator for the list.
   */
  public Iterator iterator ()
  {
    return new ReadOnlyIterator (backend.iterator());
  }

  /**
   * Validates the groups contained in this list. All groups are valid,
   * when every sub group contains all fields of its parent and has more
   * elements than its parent.
   *
   * @return true, if the group list is valid, false otherwise.
   */
  public boolean isValid()
  {
    if (backend.size() == 0)
    {
      return true;
    }

    Group parent = get(0);
    for (int i = 1; i < backend.size(); i++)
    {
      Group sub = get(i);
      if (sub.getFields().containsAll(parent.getFields()) == false)
      {
        return false;
      }
      if (sub.getFields().size() == parent.getFields().size())
      {
        return false;
      }
      parent = sub;
    }
    return true;
  }

  /**
   * Returns the number of groups in the list.
   * 
   * @return The number of groups in the list.
   */
  public int size()
  {
    return backend.size();
  }

  /**
   * Returns a string representation of the list (useful for debugging).
   * 
   * @return A string.
   */
  public String toString ()
  {
    StringBuffer b = new StringBuffer();
    b.append("GroupList={backend='");
    b.append(backend);
    b.append("'} ");
    return b.toString();
  }
}
