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

/**
 * The HashNMap can be used to store multiple values by a single key value. The values stored
 * can be retrieved using a direct query or by creating an enumeration over the stored elements.
 */
public class HashNMap
{
	private Hashtable table = null;
	
	public HashNMap ()
	{
		table = new Hashtable ();
	}
	
	public void put (Object key, Object val)
	{
		List v = new ArrayList ();
		v.add (val);
		table.put (key, v);
	}
	
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
	
	public Object get (Object key)
	{
		List v = (List) table.get (key);
		if (v == null)
		{
			return null;
		}
		return v.get(0);
	}
	
	public Iterator getAll (Object key)
	{
		List v = (List) table.get (key);
		if (v == null)
		{
			return null;
		}
		return v.iterator ();
	}
	
	public Enumeration keys ()
	{
		return table.keys ();
	}
	
	public Set keySet ()
	{
		return table.keySet ();
	}
	
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
	
	public void removeAll (Object key)
	{
		table.remove (key);
	}
	
	public void clear ()
	{
		table.clear ();
	}
	
	public boolean containsKey (Object key)
	{
		return table.containsKey (key);
	}
	
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
	
	public boolean contains (Object value)
	{
		if (containsKey (value) == true)
		{
			return true;
		}
		return containsValue (value);
	}
}
