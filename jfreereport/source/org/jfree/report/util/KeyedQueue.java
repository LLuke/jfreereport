/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * KeyedQueue.java
 * ---------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: KeyedQueue.java,v 1.3 2004/05/07 08:14:23 mungady Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 10-Dec-2002 : Minor Javadoc updates (DG);
 *
 */
package org.jfree.report.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A keyed queue is a hashtable like structure which will store a certain number of
 * elements. If the defined element size is exceeded, the firstly stored element gets removed.
 *
 * @author Thomas Morgner
 */
public class KeyedQueue implements Serializable, Cloneable
{
  /** Ordered storage for the queued items. */
  private LinkedList list;

  /** Keyed storage for the queued items. */
  private HashMap table;

  /** The maximum number of items in the queue. */
  private int limit;

  /**
   * Creates a KeyedQueue with an initial limit of 10 items.
   */
  public KeyedQueue()
  {
    this(10);
  }

  /**
   * Creates a KeyedQueue with an initial limit if <code>limit</code> items.
   *
   * @param limit  the maximum number of items.
   */
  public KeyedQueue(final int limit)
  {
    table = new HashMap();
    list = new LinkedList();
    setLimit(limit);
  }

  /**
   * Defines the maximal number of elements in the queue.
   *
   * @param limit  the maximum number of items.
   */
  public void setLimit(final int limit)
  {
    if (limit < 0)
    {
      throw new IllegalArgumentException("Limit must be at least 0.");
    }
    this.limit = limit;
  }

  /**
   * Returns the maximum number of elements in the queue.
   *
   * @return the maximum number of elements in the queue.
   */
  public int getLimit()
  {
    return limit;
  }

  /**
   * Adds a new key/value pair to the queue. If the pair is already contained in the
   * list, it is moved to the first position so that is gets removed last.
   *
   * @param key  the key.
   * @param ob  the value.
   */
  public void put(final Object key, final Object ob)
  {
    if (key == null)
    {
      throw new NullPointerException("Key must not be null");
    }
    if (ob == null)
    {
      throw new NullPointerException("Value must not be null");
    }

    final Object oldval = table.put(key, ob);
    if (oldval != null)
    {
      list.remove(oldval);
    }
    list.add(ob);

    if (getLimit() != 0 && list.size() > getLimit())
    {
      removeLast();
    }
  }

  /**
   * Queries the queue for the value stored under the given key.
   *
   * @param key  the key.
   *
   * @return the value.
   */
  public Object get(final Object key)
  {
    if (key == null)
    {
      throw new NullPointerException("Key must not be null");
    }

    return table.get(key);
  }

  /**
   * Removes the entry stored under the given key.
   *
   * @param key  the key.
   */
  public void remove(final Object key)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    table.remove(key);
    list.remove(key);
  }

  /**
   * Removes the last element in the queue.
   */
  public void removeLast()
  {
    final Object o = list.getLast();
    table.remove(o);
    list.remove(o);
  }

  /**
   * Removes all elements in the queue.
   */
  public void clear()
  {
    table.clear();
    list.clear();
  }

  /**
   * Clones the queue.
   *
   * @return  a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final KeyedQueue q = (KeyedQueue) super.clone();
    q.list = (LinkedList) list.clone();
    q.table = (HashMap) table.clone();
    return q;
  }

}
