/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * --------------
 * GroupList.java
 * --------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: GroupList.java,v 1.6 2003/08/25 14:29:28 taqua Exp $
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

package org.jfree.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.jfree.report.util.ReadOnlyIterator;

/**
 * The group list is used to store groups in a ordered way. The less specific groups are
 * guaranteed to be listed before any more specific subgroup.
 * <p>
 * Groups are ordered by comparing the declared fieldnames for the groups.
 * A subgroup of an group must contain all fields from its parent plus at least one
 * new field.
 * <p>
 * This implementation is not synchronized.
 * <p>
 * The group list cannot be empty. JFreeReport needs at least one group instance to work
 * as expected. By default, this default instance does not define any fields (and therefore
 * contains the complete report) and has no Bands defined (rendering it invisible).
 * You cannot remove that group. Every attempt to remove the last group will recreate a
 * new default group.
 *
 * @author Thomas Morgner
 */
public class GroupList implements Cloneable, Serializable
{
  /** Cache (this is a set, we need list functionality, but creating Iterators is expensive). */
  private transient Group[] cache;
  /** The backend to store the groups. */
  private ArrayList backend;

  private ReportDefinition reportDefinition;

  /**
   * Constructs a new empty group list.
   */
  public GroupList()
  {
    backend = new ArrayList();
    final Group defaultGroup = new Group();
    defaultGroup.setName("default");
    add(defaultGroup);
  }

  /**
   * Creates a new group list and copies the contents of the given grouplist.
   * If the given group list was assigned with an stylesheet collection, then the
   * new group list will share that registration.
   *
   * @param list  groups to add to the list.
   */
  protected GroupList(final GroupList list)
  {
    backend = new ArrayList();
    backend.addAll(list.backend);
  }

  /**
   * Returns the group at a position in the list.
   *
   * @param i  the position index (zero-based).
   *
   * @return the report group.
   */
  public Group get(final int i)
  {
    if (cache == null)
    {
      cache = (Group[]) backend.toArray(new Group[backend.size()]);
    }
    return cache[i];
  }

  /**
   * Removes an group from the list.
   *
   * @param o  the group that should be removed.
   * @return a boolean indicating whether or not the object was removed.
   * @throws NullPointerException if the given group object is null.
   */
  public boolean remove(final Group o)
  {
    if (o == null)
    {
      throw new NullPointerException();
    }
    cache = null;
    final int idxOf = backend.indexOf(o);
    if (idxOf == -1)
    {
      // the object was not in the list ...
      return false;
    }

    // it might as well be a group that looks like the one we have in the list
    // so be sure that you modify the one, that was removed, and not the one given
    // to us.
    final Group g = (Group) backend.remove(idxOf);
    g.setReportDefinition(null);

    if (backend.size() == 0)
    {
      final Group defaultGroup = new Group();
      defaultGroup.setName("default");
      add(defaultGroup);
    }
    return true;
  }

  /**
   * Clears the list.
   */
  public void clear()
  {
    backend.clear();
    final Group defaultGroup = new Group();
    defaultGroup.setName("default");
    add(defaultGroup);
    cache = null;
  }

  /**
   * Adds a group to the list.
   *
   * @param o  the group object.
   */
  public void add(final Group o)
  {
    if (o == null)
    {
      throw new NullPointerException("Try to add null");
    }
    cache = null;
    final int idxOf = backend.indexOf(o);
    if (idxOf != -1)
    {
      // it might as well be a group that looks like the one we have in the list
      // so be sure that you modify the one, that was removed, and not the one given
      // to us.
      final Group g = (Group) backend.remove(idxOf);
      g.setReportDefinition(null);
    }

    backend.add(o);
    o.setReportDefinition(reportDefinition);
    Collections.sort(backend);
  }

  /**
   * Adds all groups of the collection to this group list. This method will
   * result in a ClassCastException if the collection does not contain Group objects.
   *
   * @param c the collection that contains the groups.
   * @throws NullPointerException if the given collection is null.
   * @throws ClassCastException if the collection does not contain groups.
   */
  public void addAll(final Collection c)
  {
    final Iterator it = c.iterator();
    while (it.hasNext())
    {
      add((Group) it.next());
    }
  }

  /**
   * Clones the group list and all contained groups.
   *
   * @return a clone of this list.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final GroupList l = (GroupList) super.clone();
    l.backend = new ArrayList();
    l.reportDefinition = null;
    l.cache = new Group[backend.size()];
    for (int i = 0; i < backend.size(); i++)
    {
      final Group group = (Group) get(i).clone();
      group.setReportDefinition(null);
      l.backend.add(group);
      l.cache[i] = group;
    }
    return l;
  }

  /**
   * Returns an iterator for the groups of the list.
   *
   * @return An iterator over all groups of the list.
   */
  public Iterator iterator()
  {
    return new ReadOnlyIterator(backend.iterator());
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
  public String toString()
  {
    final StringBuffer b = new StringBuffer();
    b.append("GroupList={backend='");
    b.append(backend);
    b.append("'} ");
    return b.toString();
  }

  /**
   * Returns a direct reference to the group cache.
   *
   * @return the groups of this list as array.
   */
  protected Group[] getGroupCache()
  {
    if (cache == null)
    {
      cache = (Group[]) backend.toArray(new Group[backend.size()]);
    }
    return cache;
  }

  /**
   * Searches a group by its defined name. This method returns null, if the
   * group was not found.
   *
   * @param name the name of the group.
   * @return the group or null if not found.
   */
  public Group getGroupByName(final String name)
  {
    final Group[] cache = getGroupCache();
    for (int i = 0; i < cache.length; i++)
    {
      if (cache[i].getName().equals(name))
      {
        return cache[i];
      }
    }
    return null;
  }

  public void setReportDefinition (final ReportDefinition reportDefinition)
  {
    this.reportDefinition = reportDefinition;
    for (int i = 0; i < backend.size(); i++)
    {
      final Group group = (Group) backend.get(i);
      group.setReportDefinition(reportDefinition);
    }
  }

  public ReportDefinition getReportDefinition()
  {
    return reportDefinition;
  }

}
