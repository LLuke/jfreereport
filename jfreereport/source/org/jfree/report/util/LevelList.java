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
 * ---------------
 * LevelList.java
 * ---------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: LevelList.java,v 1.12 2005/02/23 21:06:05 taqua Exp $
 *
 * Changes
 * -------
 * 12-Nov-2002 : Added Javadocs (DG).
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * A list that associates a level (instance of <code>Integer</code>) with each element in
 * the list.
 *
 * @author Thomas Morgner
 */
public class LevelList implements Cloneable
{
  /**
   * Constant for level zero.
   */
  private static final Integer ZERO = new Integer(0);

  /**
   * A treeset to build the iterator.
   */
  private transient TreeSet iteratorSetAsc;

  /**
   * A treeset to build the iterator.
   */
  private transient TreeSet iteratorSetDesc;

  /**
   * A treeset to cache the level iterator.
   */
  private HashMap iteratorCache;

  /**
   * A comparator for levels in descending order.
   */
  private static final class DescendingComparator implements Comparator
  {
    /**
     * Default constructor.
     */
    private DescendingComparator ()
    {
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a
     * positive integer as the first argument is less than, equal to, or greater than the
     * second.<p>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is
     *         less than, equal to, or greater than the second.
     *
     * @throws ClassCastException if the arguments' types prevent them from being compared
     *                            by this Comparator.
     */
    public int compare (final Object o1, final Object o2)
    {
      if ((o1 instanceof Comparable) == false)
      {
        throw new ClassCastException("Need comparable Elements");
      }
      if ((o2 instanceof Comparable) == false)
      {
        throw new ClassCastException("Need comparable Elements");
      }
      final Comparable c1 = (Comparable) o1;
      final Comparable c2 = (Comparable) o2;
      return -1 * c1.compareTo(c2);
    }
  }

  /**
   * An list that caches all elements for a certain level.
   */
  private static final class ElementLevelList
  {
    /**
     * The level list.
     */
    private ArrayList datalist;

    /**
     * Creates an iterator that provides access to all the elements in a list at the
     * specified level.
     *
     * @param list  the list (null not permitted).
     * @param level the level.
     */
    private ElementLevelList (final LevelList list, final int level)
    {
      if (list == null)
      {
        throw new NullPointerException();
      }

      final Object[] rawElements = list.getRawElements();
      final Integer[] rawLevels = list.getRawLevels();

      datalist = new ArrayList(rawElements.length);
      for (int i = 0; i < rawElements.length; i++)
      {
        final Object iNext = rawElements[i];
        final Integer iLevel = rawLevels[i];
        if (iLevel.intValue() == level)
        {
          datalist.add(iNext);
        }
      }
    }

    /**
     * Returns the data for this level as object array.
     *
     * @return the data for this level as object array.
     */
    protected Object[] getData ()
    {
      return datalist.toArray();
    }

    /**
     * Returns the data for this level as object array.
     *
     * @param target object array that should receive the contents
     * @return the data for this level as object array.
     */
    protected Object[] getData (final Object[] target)
    {
      return datalist.toArray(target);
    }

    /**
     * Returns the size if the list.
     *
     * @return the size.
     */
    protected int size ()
    {
      return datalist.size();
    }
  }

  /**
   * The elements.
   */
  private ArrayList elements;

  /**
   * The levels.
   */
  private ArrayList levels;

  /**
   * Creates a new list (initially empty).
   */
  public LevelList ()
  {
    this.elements = new ArrayList();
    this.levels = new ArrayList();
    this.iteratorCache = new HashMap();
  }

  /**
   * Returns the number of elements in the list.
   *
   * @return the element count.
   */
  public int size ()
  {
    return elements.size();
  }

  /**
   * Returns an iterator that iterates through the levels in ascending order.
   *
   * @return an iterator.
   */
  public synchronized Iterator getLevelsAscending ()
  {
    if (iteratorSetAsc == null)
    {
      iteratorSetAsc = new TreeSet();
      final Integer[] ilevels = (Integer[]) levels.toArray(new Integer[levels.size()]);
      for (int i = 0; i < ilevels.length; i++)
      {
        if (iteratorSetAsc.contains(ilevels[i]) == false)
        {
          iteratorSetAsc.add(ilevels[i]);
        }
      }
    }
    return iteratorSetAsc.iterator();
  }

  public synchronized Integer[] getLevelsDescendingArray ()
  {
    if (iteratorSetDesc == null)
    {
      iteratorSetDesc = new TreeSet(new DescendingComparator());
      final Integer[] ilevels = (Integer[]) levels.toArray(new Integer[levels.size()]);
      for (int i = 0; i < ilevels.length; i++)
      {
        if (iteratorSetDesc.contains(ilevels[i]) == false)
        {
          iteratorSetDesc.add(ilevels[i]);
        }
      }
    }
    return (Integer[]) iteratorSetDesc.toArray(new Integer[iteratorSetDesc.size()]);
  }

  /**
   * Returns an iterator that iterates through the levels in descending order.
   *
   * @return an iterator.
   */
  public synchronized Iterator getLevelsDescending ()
  {
    if (iteratorSetDesc == null)
    {
      iteratorSetDesc = new TreeSet(new DescendingComparator());
      final Integer[] ilevels = (Integer[]) levels.toArray(new Integer[levels.size()]);
      for (int i = 0; i < ilevels.length; i++)
      {
        if (iteratorSetDesc.contains(ilevels[i]) == false)
        {
          iteratorSetDesc.add(ilevels[i]);
        }
      }
    }
    return iteratorSetDesc.iterator();
  }

  /**
   * Returns the elements as an array.
   *
   * @return the array.
   */
  public synchronized Object[] toArray ()
  {
    return elements.toArray();
  }

  /**
   * Returns an iterator for all the elements at a given level.
   *
   * @param level  the level.
   * @param target the target array that should receive the contentes
   * @return the data for the level as object array.
   */
  public Object[] getElementArrayForLevel (final int level, final Object[] target)
  {
    ElementLevelList it = (ElementLevelList) iteratorCache.get(new Integer(level));
    if (it == null)
    {
      it = new ElementLevelList(this, level);
      iteratorCache.put(new Integer(level), it);
    }
    if (target == null)
    {
      return it.getData();
    }
    else
    {
      return it.getData(target);
    }
  }

  /**
   * Returns an iterator for all the elements at a given level.
   *
   * @param level the level.
   * @return the data for the level as object array.
   */
  public Object[] getElementArrayForLevel (final int level)
  {
    return getElementArrayForLevel(level, null);
  }

  /**
   * Returns the numer of elements registered for an certain level.
   *
   * @param level the level that should be queried
   * @return the numer of elements in that level
   */
  public int getElementCountForLevel (final int level)
  {
    ElementLevelList it = (ElementLevelList) iteratorCache.get(new Integer(level));
    if (it == null)
    {
      it = new ElementLevelList(this, level);
      iteratorCache.put(new Integer(level), it);
    }
    return it.size();
  }

  /**
   * Creates an iterator for the elements in the list at the given level.
   *
   * @param level the level.
   * @return An iterator.
   *
   * @deprecated use the array methods for best performance.
   */
  protected Iterator getElementsForLevel (final int level)
  {
    return Collections.unmodifiableList(Arrays.asList(getElementArrayForLevel(level)))
            .iterator();
  }


  /**
   * Returns the element with the given index.
   *
   * @param index the index.
   * @return the element.
   */
  public synchronized Object get (final int index)
  {
    return elements.get(index);
  }

  /**
   * Adds an element at level zero.
   *
   * @param o the element.
   */
  public synchronized void add (final Object o)
  {
    elements.add(o);
    levels.add(ZERO);
    iteratorSetAsc = null;
    iteratorSetDesc = null;
    iteratorCache.remove(ZERO);
  }

  /**
   * Adds an element at a given level.
   *
   * @param o     the element.
   * @param level the level.
   */
  public synchronized void add (final Object o, final int level)
  {
    elements.add(o);
    final Integer i = new Integer(level);
    levels.add(i);
    iteratorCache.remove(i);
    iteratorSetAsc = null;
    iteratorSetDesc = null;
  }

  /**
   * Sets the level for an element.
   *
   * @param index the element index.
   * @param level the level.
   */
  public void setLevel (final int index, final int level)
  {
    levels.set(index, new Integer(level));
  }

  /**
   * Returns the level for an element.
   *
   * @param index the element index.
   * @return the level.
   */
  public int getLevel (final int index)
  {
    return ((Integer) levels.get(index)).intValue();
  }

  /**
   * Returns the index of an element.
   *
   * @param o the element.
   * @return the index.
   */
  public int indexOf (final Object o)
  {
    return elements.indexOf(o);
  }

  /**
   * Returns the level of an element.
   *
   * @param o the element.
   * @return the level.
   */
  public int getLevel (final Object o)
  {
    return getLevel(indexOf(o));
  }

  /**
   * Sets the level of an element.
   *
   * @param o     the element.
   * @param level the level.
   */
  public void setLevel (final Object o, final int level)
  {
    setLevel(indexOf(o), level);
  }

  /**
   * Clones the list.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final LevelList l = (LevelList) super.clone();
    l.elements = (ArrayList) elements.clone();
    l.levels = (ArrayList) levels.clone();
    l.iteratorCache = (HashMap) iteratorCache.clone();
    return l;
  }

  /**
   * Clears the list.
   */
  public synchronized void clear ()
  {
    elements.clear();
    levels.clear();
    iteratorCache.clear();
    iteratorSetAsc = null;
    iteratorSetDesc = null;
  }

  /**
   * Returns all stored objects as object array.
   *
   * @return all elements as object array.
   */
  protected Object[] getRawElements ()
  {
    return elements.toArray(new Object[elements.size()]);
  }

  /**
   * Returns all active levels as java.lang.Integer array.
   *
   * @return all levels as Integer array.
   */
  protected Integer[] getRawLevels ()
  {
    return (Integer[]) levels.toArray(new Integer[levels.size()]);
  }
}
