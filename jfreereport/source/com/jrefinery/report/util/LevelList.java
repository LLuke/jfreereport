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
 * ---------------
 * LevelList.java
 * ---------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.util;

import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;

public class LevelList implements Cloneable
{
  private static final Integer ZERO = new Integer (0);

  private static class DescendingComparator implements Comparator
  {
    public DescendingComparator()
    {
    }

    public int compare(Object o1, Object o2)
    {
      if ((o1 instanceof Comparable) == false) throw new ClassCastException("Need comparable Elements");
      if ((o2 instanceof Comparable) == false) throw new ClassCastException("Need comparable Elements");
      Comparable c1 = (Comparable) o1;
      Comparable c2 = (Comparable) o2;
      return -1  * c1.compareTo(c2);
    }
  }

  private static class ElementLevelIterator implements Iterator
  {
    private LevelList list;
    private int level;
    private Object next;
    private int currentIndex;

    public ElementLevelIterator(LevelList list, int level)
    {
      if (list == null) throw new NullPointerException();

      this.list = list;
      this.level = level;
      this.currentIndex = 0;
      searchNext();
    }

    private void searchNext ()
    {
      next = null;
      while ((currentIndex < list.size()) && next == null)
      {
        Object iNext = list.elements.get(currentIndex);
        Integer iLevel = (Integer) list.levels.get(currentIndex);
        if (iLevel.intValue() == level)
        {
          next = iNext;
        }
        currentIndex++;
      }
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
      return (next != null);
    }

    /**
     * Returns the next element in the interation.
     *
     * @return the next element in the iteration.
     * @exception NoSuchElementException iteration has no more elements.
     */
    public Object next()
    {
      Object cnext = next;
      searchNext();
      return cnext;
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
     *		  operation is not supported by this Iterator.

     * @exception IllegalStateException if the <tt>next</tt> method has not
     *		  yet been called, or the <tt>remove</tt> method has already
     *		  been called after the last call to the <tt>next</tt>
     *		  method.
     */
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  private ArrayList elements;
  private ArrayList levels;

  public LevelList ()
  {
    elements = new ArrayList();
    levels = new ArrayList();
  }

  public int size ()
  {
    return elements.size();
  }

  public synchronized Iterator getLevelsAscending()
  {
    TreeSet ts = new TreeSet ();
    Integer[] ilevels = (Integer[]) levels.toArray(new Integer[levels.size()]);
    for (int i = 0; i < ilevels.length; i++)
    {
      if (ts.contains(ilevels[i]) == false)
      {
        ts.add(ilevels[i]);
      }
    }
    return ts.iterator();
  }

  // Sorted descending
  public synchronized Iterator getLevelsDescending ()
  {
    TreeSet ts = new TreeSet (new DescendingComparator());
    Integer[] ilevels = (Integer[]) levels.toArray(new Integer[levels.size()]);
    for (int i = 0; i < ilevels.length; i++)
    {
      if (ts.contains(ilevels[i]) == false)
      {
        ts.add(ilevels[i]);
      }
    }
    return ts.iterator();
  }

  public synchronized Object[] toArray ()
  {
    return elements.toArray();
  }

  public Iterator getElementsForLevel (int level)
  {
    return new ElementLevelIterator(this, level);
  }

  public synchronized Object get(int index)
  {
    return elements.get(index);
  }

  public synchronized void add (Object o)
  {
    elements.add(o);
    levels.add(ZERO);
  }

  public synchronized void add (Object o, int level)
  {
    elements.add(o);
    levels.add(new Integer (level));
  }

  public void setLevel (int index, int level)
  {
    levels.set(index, new Integer (level));
  }

  public int getLevel (int index)
  {
    return ((Integer)levels.get (index)).intValue();
  }

  public int indexOf (Object o)
  {
    return elements.indexOf(o);
  }

  public int getLevel (Object o)
  {
    return getLevel(indexOf(o));
  }

  public void setLevel (Object o, int level)
  {
    setLevel(indexOf(o), level);
  }

  public Object clone() throws CloneNotSupportedException
  {
    LevelList l = (LevelList) super.clone();
    l.elements = (ArrayList) elements.clone();
    l.levels = (ArrayList) levels.clone();
    return l;
  }

  public void clear ()
  {
    elements.clear();
    levels.clear();
  }
}
