/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * Changes:
 * --------
 * 11-May-2002 : Version 1 (TM);
 * 16-May-2002 : Added Javadoc comments (DG);
 * 29-Aug-2002 : TreeSet does no cloning in JDK 1.2.2, it returns a "new TreeSet()".
 *               Why would a same programmer mess up the source like this?
 */

package com.jrefinery.report;

import com.jrefinery.report.util.Log;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * The group list is used to store groups in a ordered way. The less specific groups are
 * guaranteed to be listed before any more specific subgroup.
 * <p>
 * Groups are ordered by comparing the declared fieldnames for the groups.
 *
 * @author TM
 */
public class GroupList extends TreeSet implements Cloneable, Serializable
{
  /** Cache (this is a set, we need list functionality, but creating Iterators is expensive). */
  private Object[] cache;

  /**
   * A comparator that orders Group objects.
   */
  private static class GroupComparator implements Comparator
  {
    /**
     * Compares two objects (required to be instances of the Group class).
     *
     * @param o1 The first group.
     * @param o2 The second group.
     *
     * @return An integer indicating the relative ordering of the two groups.
     */
    public int compare (Object o1, Object o2)
    {
      Group g1 = (Group) o1;
      Group g2 = (Group) o2;

      List c1 = g1.getFields ();
      List c2 = g2.getFields ();

      int maxIdx = Math.min (c1.size (), c2.size ());
      for (int i = 0; i < maxIdx; i++)
      {
        String s1 = (String) c1.get (i);
        String s2 = (String) c2.get (i);

        int compare = s1.compareTo (s2);
        if (compare != 0)
        {
          return compare;
        }
      }
      if (c1.size () == c2.size ())
      {
        return 0;
      }
      else if (c1.size () < c2.size ())
      {
        return -1;
      }
      return 1;
    }

    /**
     * Returns true if this comparator is equal to an object.
     *
     * @param obj The object.
     *
     * @return a boolean indicating equality or otherwise.
     */
    public boolean equals (Object obj)
    {
      return (obj instanceof GroupComparator);
    }
  }

  /**
   * Constructs a new empty group list.
   */
  public GroupList ()
  {
    super (new GroupComparator ());
  }

  /**
   * Returns the group at a position in the list.
   *
   * @param i The position index (zero-based).
   *
   * @return the report group.
   */
  public Group get (int i)
  {
    if (cache == null)
    {
      cache = toArray ();
    }
    return (Group) cache[i];
  }

  /**
   * Removes an object from the list.
   *
   * @param o The object.
   *
   * @return A boolean indicating whether or not the object was removed.
   */
  public boolean remove (Object o)
  {
    cache = null;
    return super.remove (o);
  }

  /**
   * Clears the list.
   */
  public void clear ()
  {
    super.clear ();
    cache = null;
  }

  /**
   * Adds an object to the list.
   *
   * @param o The object (must be an instance of the Group class).
   *
   * @return true if the list did not already contain the specified element.
   */
  public boolean add (Object o)
  {
    if (o instanceof Group)
    {
      cache = null;
      if (super.add (o) == false)
      {
        super.remove (o);
        return super.add (o);
      }
      return true;
    }
    else
    {
      if (o == null)
      {
        throw new NullPointerException ("Try to add null");
      }

      throw new ClassCastException ("Group required, was " + o.getClass ().getName ());
    }
  }

  /**
   * Warning: No real cloning involved due to a bug in JDK 1.2.2; TreeSet does not clone, so
   * we can't too.
   *
   * @return a clone.
   */
  public Object clone ()
  {
    GroupList l = new GroupList();
    l.clear();
    for (int i = 0; i < size(); i++)
    {
      try
      {
        l.add (get (i).clone());
      }
      catch (CloneNotSupportedException ce)
      {
        Log.debug ("Clone error ", ce);
        return null;
      }
    }
    return l;
  }
}
