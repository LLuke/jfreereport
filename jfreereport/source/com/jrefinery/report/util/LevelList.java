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
 * ---------------
 * LevelList.java
 * ---------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LevelList.java,v 1.9 2003/04/06 18:11:31 taqua Exp $
 *
 * Changes
 * -------
 * 12-Nov-2002 : Added Javadocs (DG).
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * A list that associates a level (instance of <code>Integer</code>) with each element in the
 * list.
 *
 * @author Thomas Morgner
 */
public class LevelList implements Cloneable
{
  /** Constant for level zero. */
  private static final Integer ZERO = new Integer (0);

  /** A treeset to build the iterator. */
  private transient TreeSet iteratorSetAsc;

  /** A treeset to build the iterator. */
  private transient TreeSet iteratorSetDesc;

  /** A treeset to cache the level iterator. */
  private HashMap iteratorCache;

  /**
   * A comparator for levels in descending order.
   */
  private static class DescendingComparator implements Comparator
  {
    /**
     * Default constructor.
     */
    public DescendingComparator()
    {
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the second.
     * @throws ClassCastException if the arguments' types prevent them from
     *         being compared by this Comparator.
     */
    public int compare(Object o1, Object o2)
    {
      if ((o1 instanceof Comparable) == false) 
      {
        throw new ClassCastException("Need comparable Elements");
      }
      if ((o2 instanceof Comparable) == false) 
      {
        throw new ClassCastException("Need comparable Elements");
      }
      Comparable c1 = (Comparable) o1;
      Comparable c2 = (Comparable) o2;
      return -1  * c1.compareTo(c2);
    }
  }

  /**
   * An iterator.
   */
  private static class ElementLevelListIterator implements Iterator
  {
    /** The list. */
    private ArrayList list;
    
    /** The current index. */
    private int index;

    /**
     * Creates an iterator for a list.
     * 
     * @param list  the list.
     */
    public ElementLevelListIterator(ArrayList list)
    {
      if (list == null)
      {
        throw new NullPointerException();
      }
      this.list = list;
      index = 0;
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext()
    {
      return (index < list.size());
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     * @exception NoSuchElementException iteration has no more elements.
     */
    public Object next()
    {
      if (index >= list.size())
      {
        throw new NoSuchElementException();
      }

      Object o = list.get(index);
      index++;
      return o;
    }

    /**
     *
     * Removes from the underlying collection the last element returned by the
     * iterator (optional operation).  This method can be called only once per
     * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
     * the underlying collection is modified while the iteration is in
     * progress in any way other than by calling this method.
     *
     * @exception UnsupportedOperationException if the <tt>remove</tt>
     *            operation is not supported by this Iterator.

     * @exception IllegalStateException if the <tt>next</tt> method has not
     *            yet been called, or the <tt>remove</tt> method has already
     *            been called after the last call to the <tt>next</tt> method.
     */
    public void remove()
    {
      throw new UnsupportedOperationException();
    }

  }
  /**
   * An list that caches all elements for a certain level.
   */
  private static class ElementLevelList
  {
    /** The level list. */
    private ArrayList datalist;
    
    /**
     * Creates an iterator that provides access to all the elements in a list at the specified
     * level.
     *
     * @param list  the list (null not permitted).
     * @param level  the level.
     */
    public ElementLevelList(LevelList list, int level)
    {
      if (list == null) 
      {
        throw new NullPointerException();
      }

      this.datalist = new ArrayList();
      for (int i = 0; i < list.size(); i++)
      {
        Object iNext = list.elements.get(i);
        Integer iLevel = (Integer) list.levels.get(i);
        if (iLevel.intValue() == level)
        {
          datalist.add(iNext);
        }
      }
    }
 
    /**
     * Creates an iterator for the elements in the list.
     * 
     * @return An iterator.
     */
    public Iterator createIterator ()
    {
      return new ElementLevelListIterator(datalist);
    }
  }

  /** The elements. */
  private ArrayList elements;

  /** The levels. */
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
  public synchronized Iterator getLevelsAscending()
  {
    if (iteratorSetAsc == null)
    {
      iteratorSetAsc = new TreeSet ();
      Integer[] ilevels = (Integer[]) levels.toArray(new Integer[levels.size()]);
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

  /**
   * Returns an iterator that iterates through the levels in descending order.
   *
   * @return an iterator.
   */
  public synchronized Iterator getLevelsDescending ()
  {
    if (iteratorSetDesc == null)
    {
      iteratorSetDesc = new TreeSet (new DescendingComparator());
      Integer[] ilevels = (Integer[]) levels.toArray(new Integer[levels.size()]);
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
   *
   * @return the iterator.
   */
  public Iterator getElementsForLevel (int level)
  {
    ElementLevelList it = (ElementLevelList) iteratorCache.get(new Integer (level));
    if (it == null)
    {
      it = new ElementLevelList(this, level);
      iteratorCache.put(new Integer(level), it);
    }
    return it.createIterator();
  }

  /**
   * Returns the element with the given index.
   *
   * @param index  the index.
   *
   * @return the element.
   */
  public synchronized Object get(int index)
  {
    return elements.get(index);
  }

  /**
   * Adds an element at level zero.
   *
   * @param o  the element.
   */
  public synchronized void add (Object o)
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
   * @param o  the element.
   * @param level the level.
   */
  public synchronized void add (Object o, int level)
  {
    elements.add(o);
    Integer i = new Integer (level);
    levels.add(i);
    iteratorCache.remove(i);
    iteratorSetAsc = null;
    iteratorSetDesc = null;
  }

  /**
   * Sets the level for an element.
   *
   * @param index  the element index.
   * @param level  the level.
   */
  public void setLevel (int index, int level)
  {
    levels.set(index, new Integer (level));
  }

  /**
   * Returns the level for an element.
   *
   * @param index  the element index.
   *
   * @return the level.
   */
  public int getLevel (int index)
  {
    return ((Integer) levels.get (index)).intValue();
  }

  /**
   * Returns the index of an element.
   *
   * @param o  the element.
   *
   * @return the index.
   */
  public int indexOf (Object o)
  {
    return elements.indexOf(o);
  }

  /**
   * Returns the level of an element.
   *
   * @param o  the element.
   *
   * @return the level.
   */
  public int getLevel (Object o)
  {
    return getLevel(indexOf(o));
  }

  /**
   * Sets the level of an element.
   *
   * @param o  the element.
   * @param level  the level.
   */
  public void setLevel (Object o, int level)
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
  public Object clone() throws CloneNotSupportedException
  {
    LevelList l = (LevelList) super.clone();
    l.elements = (ArrayList) elements.clone();
    l.levels = (ArrayList) levels.clone();
    l.iteratorCache = (HashMap) iteratorCache.clone();
    return l;
  }

  /**
   * Clears the list.
   */
  public void clear ()
  {
    elements.clear();
    levels.clear();
    iteratorCache.clear();
    iteratorSetAsc = null;
    iteratorSetDesc = null;
  }

}
