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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 28.10.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.base.components;

import java.util.HashMap;
import javax.swing.Action;

/**
 * An actionmap, which is JDK 1.2.2 compatible.
 * <p>
 * This implementation does not implement the ActionMap interface of
 * JDK 1.3 or higher to maintain the compatibility with JDK 1.2 which
 * does not know this interface.
 * <p>
 * The usage is still the same.
 */
public class DowngradeActionMap
{
  private HashMap actionMap;
  private DowngradeActionMap parent;

  public DowngradeActionMap()
  {
    actionMap = new HashMap();
  }

  public void setParent(DowngradeActionMap map)
  {
    this.parent = map;
  }

  public DowngradeActionMap getParent()
  {
    return parent;
  }

  public void put(Object key, Action action)
  {
    if (action == null)
    {
      remove(key);
    }
    else
    {
      this.actionMap.put(key, action);
    }
  }

  public Action get(Object key)
  {
    Action retval = (Action) actionMap.get(key);
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

  public void remove(Object key)
  {
    actionMap.remove(key);
  }

  public void clear()
  {
    actionMap.clear();
  }

  public Object[] keys()
  {
    return actionMap.keySet().toArray();
  }

  public int size()
  {
    return actionMap.size();
  }

  public Object[] allKeys()
  {
    if (parent == null)
    {
      return keys();
    }
    Object[] parentKeys = parent.allKeys();
    Object[] key = keys();
    Object[] retval = new Object[parentKeys.length + key.length];
    System.arraycopy(key, 0, retval, 0, key.length);
    System.arraycopy(retval, 0, retval, key.length, retval.length);
    return retval;
  }

}
