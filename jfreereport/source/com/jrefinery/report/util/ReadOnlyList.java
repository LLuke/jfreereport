/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * ReadOnlyList.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A readOnly list that will ignore all operations which could modify its contents.
 */
public class ReadOnlyList implements List
{
  private List parent;

  public ReadOnlyList (List parent)
  {
    this.parent = parent;
  }

  public int size ()
  {
    return parent.size ();
  }

  public boolean isEmpty ()
  {
    return parent.isEmpty ();
  }

  public boolean contains (Object o)
  {
    return parent.contains (o);
  }

  public Iterator iterator ()
  {
    return new ReadOnlyIterator (parent.iterator ());
  }

  public Object[] toArray ()
  {
    return parent.toArray ();
  }

  public Object[] toArray (Object[] objects)
  {
    return parent.toArray ();
  }

  public boolean add (Object o)
  {
    return false;
  }

  public boolean remove (Object o)
  {
    return false;
  }

  public boolean containsAll (Collection collection)
  {
    return parent.containsAll (collection);
  }

  public boolean addAll (Collection collection)
  {
    return false;
  }

  public boolean addAll (int i, Collection collection)
  {
    return false;
  }

  public boolean removeAll (Collection collection)
  {
    return false;
  }

  public boolean retainAll (Collection collection)
  {
    return false;
  }

  public void clear ()
  {
  }

  public Object get (int i)
  {
    return parent.get (i);
  }

  public Object set (int i, Object o)
  {
    return null;
  }

  public void add (int i, Object o)
  {
  }

  public Object remove (int i)
  {
    return null;
  }

  public int indexOf (Object o)
  {
    return parent.indexOf (o);
  }

  public int lastIndexOf (Object o)
  {
    return parent.lastIndexOf (o);
  }

  public ListIterator listIterator ()
  {
    return new ReadOnlyListIterator (parent.listIterator ());
  }

  public ListIterator listIterator (int i)
  {
    return new ReadOnlyListIterator (parent.listIterator (i));
  }

  public List subList (int i, int i1)
  {
    return new ReadOnlyList (parent.subList (i, i1));
  }
}
