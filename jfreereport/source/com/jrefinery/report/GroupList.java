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
 * $Id: GroupList.java,v 1.27 2003/06/23 14:36:56 taqua Exp $
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
 * 31-May-2003 : Removed isValid() test, as the compare implementation of group cannot create
 *               invalid group lists.
 */

package com.jrefinery.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;

import com.jrefinery.report.targets.style.StyleSheetCollection;
import com.jrefinery.report.targets.style.StyleSheetCollectionHelper;
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
  private static class GroupListStyleSheetCollectionHelper extends StyleSheetCollectionHelper
  {
    private GroupList groupList;

    public GroupListStyleSheetCollectionHelper(GroupList groupList)
    {
      this.groupList = groupList;
    }

    protected void handleRegisterStyleSheetCollection()
    {
      if (groupList.cache == null)
      {
        groupList.cache = groupList.backend.toArray();
      }
      for (int i = 0; i < groupList.cache.length; i++)
      {
        Group g = (Group) groupList.cache[i];
        g.registerStyleSheetCollection(getStyleSheetCollection());
      }
    }

    protected void handleUnregisterStyleSheetCollection()
    {
      if (groupList.cache == null)
      {
        groupList.cache = groupList.backend.toArray();
      }
      for (int i = 0; i < groupList.cache.length; i++)
      {
        Group g = (Group) groupList.cache[i];
        g.unregisterStyleSheetCollection(null);
      }
    }
  }

  /** Cache (this is a set, we need list functionality, but creating Iterators is expensive). */
  private Object[] cache;
  private ArrayList backend;
  private GroupListStyleSheetCollectionHelper styleSheetCollectionHelper;

  /**
   * Constructs a new empty group list.
   */
  public GroupList()
  {
    this.backend = new ArrayList();
    styleSheetCollectionHelper = new GroupListStyleSheetCollectionHelper(this);
  }

  /**
   * Creates a new group list.
   *
   * @param list  groups to add to the list.
   */
  public GroupList(GroupList list)
  {
    this();
    backend.addAll(list.backend);
    if (list.getStyleSheetCollection() != null)
    {
      registerStyleSheetCollection(list.getStyleSheetCollection());
    }
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
  public boolean remove(Group o)
  {
    cache = null;
    int idxOf = findGroup(o);
    if (idxOf == -1)
    {
      // the object was not in the list ...
      return false;
    }
    Group go = (Group) backend.get(idxOf);
    if (getStyleSheetCollection() != null)
    {
      go.unregisterStyleSheetCollection(getStyleSheetCollection());
    }
    backend.remove(idxOf);
    return true;
  }

  private int findGroup (Group group)
  {
    for (int i = 0; i < backend.size(); i++)
    {
      Group gList = (Group) backend.get(i);
      if (gList.compareTo(group) == 0)
      {
        return i;
      }
    }
    return -1;
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
   * Adds a group to the list.
   *
   * @param o  the group object.
   */
  public void add(Group o)
  {
    if (o == null)
    {
      throw new NullPointerException("Try to add null");
    }
    cache = null;
    if (findGroup(o) != -1)
    {
      remove(o);
    }
    if (getStyleSheetCollection() != null)
    {
      o.registerStyleSheetCollection(getStyleSheetCollection());
    }
    backend.add(o);
    Collections.sort(backend);
  }

  public void addAll (Collection c)
  {
    Iterator it = c.iterator();
    while (it.hasNext())
    {
      add((Group) it.next());
    }
  }

  /**
   * Clones the list.
   *
   * @return a clone.
   */
  public Object clone()
  {
    try
    {
      GroupList l = (GroupList) super.clone();
      l.styleSheetCollectionHelper = new GroupListStyleSheetCollectionHelper(l);
      l.backend = new ArrayList();
      l.clear();
      for (int i = 0; i < backend.size(); i++)
      {
        l.backend.add(get(i).clone());
      }
      return l;
    }
    catch (CloneNotSupportedException cne)
    {
      Log.error ("GroupsList was not cloned.");
      throw new IllegalStateException("GroupList was not cloneable.");
    }
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

  public StyleSheetCollection getStyleSheetCollection()
  {
    return styleSheetCollectionHelper.getStyleSheetCollection();
  }

  public void registerStyleSheetCollection(StyleSheetCollection styleSheetCollection)
  {
    styleSheetCollectionHelper.registerStyleSheetCollection(styleSheetCollection);
  }

  public void unregisterStyleSheetCollection(StyleSheetCollection styleSheetCollection)
  {
    styleSheetCollectionHelper.unregisterStyleSheetCollection(styleSheetCollection);
  }

  public void updateStyleSheetCollection(StyleSheetCollection styleSheetCollection)
  {
    if (cache == null)
    {
      cache = backend.toArray();
    }
    for (int i = 0; i < cache.length; i++)
    {
      Group g = (Group) cache[i];
      g.updateStyleSheetCollection(styleSheetCollection);
    }
  }
}
