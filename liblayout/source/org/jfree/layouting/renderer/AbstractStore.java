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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Creation-Date: 29.11.2006, 18:30:08
 *
 * @author Thomas Morgner
 */
public class AbstractStore implements Cloneable, Serializable
{
  private Map initialSet;
  private HashMap firstSet;
  private HashMap lastSet;

  public AbstractStore()
  {
    initialSet = new HashMap();
    firstSet = new HashMap();
    lastSet = new HashMap();
  }

  protected void addInternal(String name, Object contents)
  {
    if (firstSet.containsKey(name) == false)
    {
      firstSet.put(name, contents);
    }
    lastSet.put(name, contents);
  }

  protected Object getLastInternal(String name)
  {
    final Object lastObject = lastSet.get(name);
    if (lastObject != null)
    {
      return lastSet.get(name);
    }
    return initialSet.get(name);
  }

  protected Object getFirstInternal(String name)
  {
    Object firstVal = firstSet.get(name);
    if (firstVal != null)
    {
      return firstSet.get(name);
    }
    return initialSet.get(name);
  }

  protected Object getInitialInternal (String name)
  {
    return initialSet.get(name);
  }

  public AbstractStore derive()
  {
    final AbstractStore contentStore = new AbstractStore();
    contentStore.initialSet = Collections.unmodifiableMap(lastSet);
    return contentStore;
  }

  public Object clone () throws CloneNotSupportedException
  {
    AbstractStore store = (AbstractStore) super.clone();
    store.firstSet = (HashMap) firstSet.clone();
    store.lastSet = (HashMap) lastSet.clone();
    // initial set is immutable.
    store.initialSet = initialSet;
    return store;
  }

}
