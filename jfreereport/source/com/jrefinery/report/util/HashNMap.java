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
 * HashNMap.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 20-May-2002 : Initial version
 */
package com.jrefinery.report.util;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Set;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * The HashNMap can be used to store multiple values by a single key value. The values stored
 * can be retrieved using a direct query or by creating an enumeration over the stored elements.
 */
public class HashNMap implements Serializable, Cloneable
{
	private Hashtable table = null;

  /**
   * Default Constructor
   */
	public HashNMap ()
	{
		table = new Hashtable ();
	}

  /**
   * Inserts a new key/value pair into the map. If such a pair already exists, it gets replaced
   * with the given values.
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
   * retrieves the first value registered for an key or null if there was no such key
   * in the list.
   */
	public Object get (Object key)
	{
    return get (key, 0);
	}

  /**
   * retrieves the n-th value registered for an key or null if there was no such key
   * in the list. An index out of bounds exception is thrown if there are less than
   * n elements registered to this key.
   */
  public Object get (Object key, int i)
  {
    List v = (List) table.get (key);
		if (v == null)
		{
			return null;
		}
		return v.get(i);
  }

  /**
   * returns an iterator over all elements registered to the given key.
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
   * returns all registered keys as enumeration.
   */
	public Enumeration keys ()
	{
		return table.keys ();
	}

  /**
   * returns all registered keys as set.
   */
	public Set keySet ()
	{
		return table.keySet ();
	}

  /**
   * removes the key/value pair from the map. If the removed entry was the last entry
   * for this key, the key gets also removed.
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
   * removes all elements for the given key.
   */
	public void removeAll (Object key)
	{
		table.remove (key);
	}

  /**
   * clears all keys and values of this map
   */
	public void clear ()
	{
		table.clear ();
	}

  /**
   * tests whether this map contains the given key.
   *
   * @returns true if the key is contained in the map
   */
	public boolean containsKey (Object key)
	{
		return table.containsKey (key);
	}
	
  /**
   * tests whether this map contains the given value.
   *
   * @returns true if the value is registered in the map for an key
   */
	public boolean containsValue (Object value)
	{
		Enumeration e = keys ();
		boolean found = false;
		while (e.hasMoreElements () && !found)
		{
			List v = (List) e.nextElement ();
			found = v.contains (value);
		}
		return found;
	}
	
  /**
   * tests whether this map contains the given key or value.
   *
   * @returns true if the key or value is contained in the map
   */
	public boolean contains (Object value)
	{
		if (containsKey (value) == true)
		{
			return true;
		}
		return containsValue (value);
	}

  public Object clone () throws CloneNotSupportedException
  {
    HashNMap map = (HashNMap) super.clone ();
    map.table = new Hashtable();
    Enumeration enum = keys();
    while (enum.hasMoreElements())
    {
      Object key = enum.nextElement();
      Iterator it = getAll(key);
      while (it.hasNext())
      {
        map.add (key, it.next());
      }
    }
    return map;
  }
}
