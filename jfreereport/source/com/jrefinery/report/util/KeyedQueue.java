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
 * KeyedQueue.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 20-May-2002 : Initial version
 */
package com.jrefinery.report.util;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * A keyed queue is a hashtable like structure which will store a certain number of
 * elements. If the defined element size is exceed, the firstly stored element gets removed.
 */
public class KeyedQueue implements Serializable, Cloneable
{
  private LinkedList list;
  private Hashtable table;
  private int limit;

  /**
   * creates a KeyedQueue with an initial limit of 10
   */
  public KeyedQueue ()
  {
    this (10);
  }

  /**
   * creates a KeyedQueue with an initial limit if <code>limit</code>
   */
  public KeyedQueue (int limit)
  {
    table = new Hashtable ();
    list = new LinkedList ();
    setLimit (limit);
  }

  /**
   * defines the maximal number of elements in the list.
   */
  public void setLimit (int limit)
  {
    if (limit < 1) throw new IllegalArgumentException ();
    this.limit = limit;
  }

  /**
   * @returns the maximal number of elements in the queue
   */
  public int getLimit ()
  {
    return limit;
  }

  /**
   * adds a new key/value pair to the queue. If the pair is already contained in the
   * list, it is moved to the first position so that is gets removed last.
   */
  public void put (Object key, Object ob)
  {
    if (key == null) throw new NullPointerException ("Key must not be null");
    if (ob == null) throw new NullPointerException ("Value must not be null");

    Object oldval = table.put (key, ob);
    if (oldval != null) list.remove (oldval);
    list.add (ob);

    if (list.size () > getLimit ())
      removeLast ();
  }

  /**
   * queries the queue for the value stored under the given key.
   */
  public Object get (Object key)
  {
    if (key == null) throw new NullPointerException ("Key must not be null");

    return table.get (key);
  }

  /**
   * removes the entry stored under the given key.
   */
  public void remove (Object key)
  {
    if (key == null) throw new NullPointerException ();
    table.remove (key);
    list.remove (key);
  }

  /**
   * removes the last element in the queue
   */
  public void removeLast ()
  {
    Object o = list.getLast ();
    table.remove (o);
    list.remove (o);
  }

  /**
   * removes all elements in the queue
   */
  public void clear ()
  {
    table.clear ();
    list.clear ();
  }

  public Object clone () throws CloneNotSupportedException
  {
    System.out.println ("Cloned QUEUE: " + list.size());
    KeyedQueue q = (KeyedQueue) super.clone ();
    q.list = (LinkedList) list.clone ();
    q.table = (Hashtable) table.clone ();
    return q;
  }


}
