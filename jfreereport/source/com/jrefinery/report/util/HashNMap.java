/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * -------------
 * HashNMap.java
 * -------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HashNMap.java,v 1.11 2002/12/11 00:41:42 mungady Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 10-Dec-2002 : Minor Javadoc updates (DG);
 *
 */

package com.jrefinery.report.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The HashNMap can be used to store multiple values by a single key value. The values stored
 * can be retrieved using a direct query or by creating an enumeration over the stored elements.
 *
 * @author Thomas Morgner
 */
public class HashNMap implements Serializable, Cloneable
{
  /** The underlying storage. */
  private HashMap table = null;

  /**
   * Default constructor.
   */
  public HashNMap ()
  {
    table = new HashMap();
  }

  /**
   * Inserts a new key/value pair into the map.  If such a pair already exists, it gets replaced
   * with the given values.
   *
   * @param key  the key.
   * @param val  the value.
   */
  public void put (Object key, Object val)
  {
    List v = new ArrayList ();
    v.add (val);
    table.put (key, v);
  }

  /**
   * Adds a new key/value pair into this map. If the key is not yet in the map, it gets added
   * to the map and the call is equal to put(Object,Object).
   *
   * @param key  the key.
   * @param val  the value.
   */
  public void add (Object key, Object val)
  {
    List v = (List) table.get (key);
    if (v == null)
    {
      put (key, val);
    }
    else
    {
      v.add (val);
    }
  }

  /**
   * Retrieves the first value registered for an key or null if there was no such key
   * in the list.
   *
   * @param key  the key.
   *
   * @return the value.
   */
  public Object get (Object key)
  {
    return get (key, 0);
  }

  /**
   * Retrieves the n-th value registered for an key or null if there was no such key
   * in the list. An index out of bounds exception is thrown if there are less than
   * n elements registered to this key.
   *
   * @param key  the key.
   * @param n  the index.
   *
   * @return the object.
   */
  public Object get (Object key, int n)
  {
    List v = (List) table.get (key);
    if (v == null)
    {
      return null;
    }
    return v.get (n);
  }

  /**
   * Returns an iterator over all elements registered to the given key.
   *
   * @param key  the key.
   *
   * @return an iterator.
   */
  public Iterator getAll (Object key)
  {
    List v = (List) table.get (key);
    if (v == null)
    {
      return null;
    }
    return v.iterator ();
  }

  /**
   * Returns all registered keys as an enumeration.
   *
   * @return an enumeration of the keys.
   */
  public Iterator keys ()
  {
    return table.keySet().iterator();
  }

  /**
   * Returns all registered keys as set.
   *
   * @return a set of keys.
   */
  public Set keySet ()
  {
    return table.keySet ();
  }

  /**
   * Removes the key/value pair from the map. If the removed entry was the last entry
   * for this key, the key gets also removed.
   *
   * @param key  the key.
   * @param value  the value.
   */
  public void remove (Object key, Object value)
  {
    List v = (List) table.get (key);
    if (v == null)
    {
      return;
    }

    v.remove (value);
    if (v.size () == 0)
    {
      table.remove (key);
    }
  }

  /**
   * Removes all elements for the given key.
   *
   * @param key  the key.
   */
  public void removeAll (Object key)
  {
    table.remove (key);
  }

  /**
   * Clears all keys and values of this map.
   */
  public void clear ()
  {
    table.clear ();
  }

  /**
   * Tests whether this map contains the given key.
   *
   * @param key  the key.
   *
   * @return true if the key is contained in the map
   */
  public boolean containsKey (Object key)
  {
    return table.containsKey (key);
  }

  /**
   * Tests whether this map contains the given value.
   *
   * @param value  the value.
   *
   * @return true if the value is registered in the map for an key.
   */
  public boolean containsValue (Object value)
  {
    Iterator e = keys ();
    boolean found = false;
    while (e.hasNext() && !found)
    {
      List v = (List) e.next();
      found = v.contains (value);
    }
    return found;
  }

  /**
   * Tests whether this map contains the given key or value.
   *
   * @param value  the value.
   *
   * @return true if the key or value is contained in the map
   */
  public boolean contains (Object value)
  {
    if (containsKey (value) == true)
    {
      return true;
    }
    return containsValue (value);
  }

  /**
   * Creates a deep copy of this HashNMap.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    HashNMap map = (HashNMap) super.clone ();
    map.table = new HashMap ();
    Iterator enum = keys ();
    while (enum.hasNext())
    {
      Object key = enum.next();
      Iterator it = getAll (key);
      while (it.hasNext ())
      {
        map.add (key, it.next ());
      }
    }
    return map;
  }
}
