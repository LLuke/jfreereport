/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * DowngradeActionMap.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DowngradeActionMap.java,v 1.1 2003/11/07 16:26:17 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28.10.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.base.components;

import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.Action;

/**
 * An actionmap, which is JDK 1.2.2 compatible.
 * <p>
 * This implementation does not implement the ActionMap interface of
 * JDK 1.3 or higher to maintain the compatibility with JDK 1.2 which
 * does not know this interface.
 * <p>
 * The usage is still the same.
 *
 * @author Thomas Morger
 */
public class DowngradeActionMap
{
  /** A map containing the key to action mapping. */
  private final HashMap actionMap;
  /** A list containing the actionkeys in their order of addition. */
  private final ArrayList actionList;
  /** The parent of this action map. */
  private DowngradeActionMap parent;

  /**
   * Default Constructor. Creates a new empty map.
   */
  public DowngradeActionMap()
  {
    actionMap = new HashMap();
    actionList = new ArrayList();
  }

  /**
   * Sets this <code>ActionMap</code>'s parent.
   *
   * @param map  the <code>ActionMap</code> that is the parent of this one
   */
  public void setParent(final DowngradeActionMap map)
  {
    this.parent = map;
  }

  /**
   * Returns this <code>ActionMap</code>'s parent.
   *
   * @return the <code>ActionMap</code> that is the parent of this one,
   *         or null if this <code>ActionMap</code> has no parent
   */
  public DowngradeActionMap getParent()
  {
    return parent;
  }

  /**
   * Adds a binding for <code>key</code> to <code>action</code>.
   * If <code>action</code> is null, this removes the current binding
   * for <code>key</code>.
   * <p>In most instances, <code>key</code> will be
   * <code>action.getValue(NAME)</code>.
   *
   * @param key the key for the action.
   * @param action the action to be added.
   */
  public void put(final Object key, final Action action)
  {
    if (action == null)
    {
      remove(key);
    }
    else
    {
      if (actionMap.containsKey(key))
      {
        remove(key);
      }
      this.actionMap.put(key, action);
      this.actionList.add (key);
    }
  }

  /**
   * Returns the binding for <code>key</code>, messaging the
   * parent <code>ActionMap</code> if the binding is not locally defined.
   *
   * @param key the key to be queried.
   * @return the action for this key, or null if there is no such action.
   */
  public Action get(final Object key)
  {
    final Action retval = (Action) actionMap.get(key);
    if (retval != null)
    {
      return retval;
    }
    if (parent != null)
    {
      return parent.get(key);
    }
    return null;
  }

  /**
   * Removes the binding for <code>key</code> from this <code>ActionMap</code>.
   *
   * @param key the key to be removed.
   */
  public void remove(final Object key)
  {
    actionMap.remove(key);
    actionList.remove(key);
  }

  /**
   * Removes all the mappings from this <code>ActionMap</code>.
   */
  public void clear()
  {
    actionMap.clear();
    actionList.clear();
  }

  /**
   * Returns the <code>Action</code> names that are bound in this <code>ActionMap</code>.
   *
   * @return the keys which are directly bound to this map.
   */
  public Object[] keys()
  {
    return actionList.toArray();
  }

  /**
   * Returns the number of bindings.
   *
   * @return the number of entries in this map.
   */
  public int size()
  {
    return actionMap.size();
  }

  /**
   * Returns an array of the keys defined in this <code>ActionMap</code> and
   * its parent. This method differs from <code>keys()</code> in that
   * this method includes the keys defined in the parent.
   *
   * @return all keys of this map and all parents.
   */
  public Object[] allKeys()
  {
    if (parent == null)
    {
      return keys();
    }
    final Object[] parentKeys = parent.allKeys();
    final Object[] key = keys();
    final Object[] retval = new Object[parentKeys.length + key.length];
    System.arraycopy(key, 0, retval, 0, key.length);
    System.arraycopy(retval, 0, retval, key.length, retval.length);
    return retval;
  }

}