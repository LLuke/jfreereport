/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
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
 * StringStore.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StringStore.java,v 1.1 2006/10/27 18:28:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer;

import java.util.HashMap;
import java.io.Serializable;

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
public class StringStore implements Cloneable, Serializable
{
  private HashMap initialSet;
  private HashMap firstSet;
  private HashMap lastSet;

  public StringStore()
  {
    initialSet = new HashMap();
    firstSet = new HashMap();
    lastSet = new HashMap();
  }

  public void add(String name, String contents)
  {
    if (firstSet.containsKey(name) == false)
    {
      firstSet.put(name, contents);
    }
    lastSet.put(name, contents);
  }

  public String getLast(String name)
  {
    if (lastSet.containsKey(name))
    {
      return (String) lastSet.get(name);
    }
    return (String) initialSet.get(name);
  }

  public String getFirst(String name)
  {
    if (firstSet.containsKey(name))
    {
      return (String) firstSet.get(name);
    }
    return (String) initialSet.get(name);
  }

  public StringStore derive()
  {
    final StringStore contentStore = new StringStore();
    contentStore.initialSet.putAll(lastSet);
    return contentStore;
  }

  public Object clone () throws CloneNotSupportedException
  {
    StringStore store = (StringStore) super.clone();
    store.firstSet = (HashMap) firstSet.clone();
    store.lastSet = (HashMap) lastSet.clone();
    // initial set is immutable.
    store.initialSet = initialSet;
    return store;
  }
}
