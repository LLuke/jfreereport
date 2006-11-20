/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.renderer;

import java.io.Serializable;
import java.util.HashMap;

/**
 * For the first throw, the content remains very simple. We support the 4 modes:
 * start - the initial content is used. first - the first value set in this page
 * is used (else the initial content) last - the last value is used. last-except
 * - the last value is used on the next page. (Contrary to the specification, we
 * fall back to the start-value instead of using an empty value).
 *
 * The string store is used for all counter, counters and string properties.
 *
 * @author Thomas Morgner
 */
public class CounterStore implements Cloneable, Serializable
{
  private HashMap initialSet;
  private HashMap firstSet;
  private HashMap lastSet;

  public CounterStore()
  {
    initialSet = new HashMap();
    firstSet = new HashMap();
    lastSet = new HashMap();
  }

  public void add(String name, int counterValue)
  {
    if (firstSet.containsKey(name) == false)
    {
      firstSet.put(name, new Integer(counterValue));
    }
    lastSet.put(name, new Integer(counterValue));
  }

  public Integer getLast(String name)
  {
    if (lastSet.containsKey(name))
    {
      return (Integer) lastSet.get(name);
    }
    return (Integer) initialSet.get(name);
  }

  public Integer getFirst(String name)
  {
    if (firstSet.containsKey(name))
    {
      return (Integer) firstSet.get(name);
    }
    return (Integer) initialSet.get(name);
  }

  public CounterStore derive()
  {
    final CounterStore contentStore = new CounterStore();
    contentStore.initialSet.putAll(lastSet);
    return contentStore;
  }

  public Object clone () throws CloneNotSupportedException
  {
    CounterStore store = (CounterStore) super.clone();
    store.firstSet = (HashMap) firstSet.clone();
    store.lastSet = (HashMap) lastSet.clone();
    // initial set is immutable.
    store.initialSet = initialSet;
    return store;
  }
}
