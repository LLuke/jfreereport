/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * $Id: GroupList.java,v 1.32 2003/07/03 15:59:28 taqua Exp $
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

import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.style.StyleSheetCollectionHelper;
import org.jfree.report.util.Log;
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
 *
 * @author Thomas Morgner
 */
public class GroupList implements Cloneable, Serializable
{
  /**
   * Internal helper class to handle the style sheet collection properly.
   */
  private static class GroupListStyleSheetCollectionHelper extends StyleSheetCollectionHelper
  {
    /** The group list for which we handle the stylesheet collection. */
    private GroupList groupList;

    /**
     * Creates a new helper for the given group list.
     *
     * @param groupList the group list whose stylesheet colllection should be managed.
     */
    public GroupListStyleSheetCollectionHelper(final GroupList groupList)
    {
      this.groupList = groupList;
    }

    /**
     * Handles the stylesheet collection registration for the group list and
     * all groups.
     */
    protected void handleRegisterStyleSheetCollection()
    {
      Group[] cache = groupList.getGroupCache();
      for (int i = 0; i < cache.length; i++)
      {
        final Group g = cache[i];
        g.registerStyleSheetCollection(this.getStyleSheetCollection());
      }
    }

    /**
     * Handles the stylesheet collection unregistration for the group list and
     * all groups.
     */
    protected void handleUnregisterStyleSheetCollection()
    {
      Group[] cache = groupList.getGroupCache();
      for (int i = 0; i < cache.length; i++)
      {
        final Group g = cache[i];
        g.unregisterStyleSheetCollection(null);
      }
    }
  }

  /** Cache (this is a set, we need list functionality, but creating Iterators is expensive). */
  private transient Group[] cache;
  /** The backend to store the groups. */
  private ArrayList backend;
  /** The stylesheet collection helper for managing the stylesheet collection of this list. */
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
   * Creates a new group list and copies the contents of the given grouplist.
   * If the given group list was assigned with an stylesheet collection, then the
   * new group list will share that registration.
   *
   * @param list  groups to add to the list.
   */
  public GroupList(final GroupList list)
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
    final int idxOf = findGroup(o);
    if (idxOf == -1)
    {
      // the object was not in the list ...
      return false;
    }
    final Group go = (Group) backend.get(idxOf);
    if (getStyleSheetCollection() != null)
    {
      go.unregisterStyleSheetCollection(getStyleSheetCollection());
    }
    backend.remove(idxOf);
    return true;
  }

  /**
   * Tries to find the group in the group list. This compares the group by using
   * the compareTo function instead of using equals(), as we don't care about
   * group names here.
   *
   * @param group the group that is searched.
   * @return the index of the group or -1 if this group is not contained in that list.
   */
  private int findGroup(final Group group)
  {
    for (int i = 0; i < backend.size(); i++)
    {
      final Group gList = (Group) backend.get(i);
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
  public void add(final Group o)
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
  public Object clone()
  {
    try
    {
      final GroupList l = (GroupList) super.clone();
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
      Log.error("GroupsList was not cloned.");
      throw new IllegalStateException("GroupList was not cloneable.");
    }
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
   * Returns the stylesheet collection which is assigned with this group and
   * all stylesheets of this group.
   *
   * @return the stylesheet collection or null, if no collection is assigned.
   */
  public StyleSheetCollection getStyleSheetCollection()
  {
    return styleSheetCollectionHelper.getStyleSheetCollection();
  }

  /**
   * Registers the given StyleSheet collection with this group list. If there is already
   * another stylesheet collection registered, this method will throw an
   * <code>InvalidStyleSheetCollectionException</code>.
   *
   * @param styleSheetCollection the stylesheet collection that should be registered.
   * @throws org.jfree.report.style.InvalidStyleSheetCollectionException
   * if there is already an other stylesheet registered.
   * @throws NullPointerException if the given stylesheet collection is null.
   */
  public void registerStyleSheetCollection(final StyleSheetCollection styleSheetCollection)
  {
    styleSheetCollectionHelper.registerStyleSheetCollection(styleSheetCollection);
  }

  /**
   * Unregisters the given stylesheet collection from this group list. If this stylesheet
   * collection is not registered with this group list, this method will throw an
   * <code>InvalidStyleSheetCollectionException</code>
   *
   * @param styleSheetCollection the stylesheet collection that should be unregistered.
   * @throws org.jfree.report.style.InvalidStyleSheetCollectionException
   * @throws NullPointerException if the given stylesheet collection is null.
   */
  public void unregisterStyleSheetCollection(final StyleSheetCollection styleSheetCollection)
  {
    styleSheetCollectionHelper.unregisterStyleSheetCollection(styleSheetCollection);
  }

  /**
   * Updates the stylesheet collection for this group list and all group in this list.
   * This method must be called after the group list was cloned, to make sure that
   * all stylesheets are registered properly.
   * <p>
   * If you don't call this function after cloning prepare to be doomed.
   * This method will replace all inherited stylesheets with clones from the stylesheet
   * collection.
   *
   * @param styleSheetCollection the stylesheet collection that contains the updated
   * information and that should be assigned with that element.
   * @throws NullPointerException if the given stylesheet collection is null.
   * @throws org.jfree.report.style.InvalidStyleSheetCollectionException if
   * there is an other stylesheet collection already registered with that element.
   */
  public void updateStyleSheetCollection(final StyleSheetCollection styleSheetCollection)
  {
    if (cache == null)
    {
      cache = (Group[]) backend.toArray(new Group[backend.size()]);
    }
    for (int i = 0; i < cache.length; i++)
    {
      final Group g = cache[i];
      g.updateStyleSheetCollection(styleSheetCollection);
    }
  }
  
  /**
   * Returns a direct reference to the group cache.
   * 
   * @return the groups of this list as array.
   */
  protected Group[] getGroupCache ()
  {
    if (cache == null)
    {
      cache = (Group[]) backend.toArray(new Group[backend.size()]);
    }
    return cache;
  } 
  
}
