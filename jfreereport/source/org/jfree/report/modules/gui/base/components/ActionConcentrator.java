/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ActionConcentrator.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ActionConcentrator.java,v 1.3 2003/09/09 21:31:48 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.base.components;

import java.util.ArrayList;
import javax.swing.Action;

/**
 * This class is used to collect actions to be enabled or disabled
 * by a sinle call.
 * 
 * @author Thomas Morgner
 */
public class ActionConcentrator
{
  /** The collection used to store the actions of this concentrator. */
  private final ArrayList actions;

  /**
   * DefaultConstructor.
   */
  public ActionConcentrator()
  {
    actions = new ArrayList();
  }

  /**
   * Adds the action to this concentrator.
   * 
   * @param a the action to be added.
   */
  public void addAction(final Action a)
  {
    if (a == null)
    {
      throw new NullPointerException();
    }
    actions.add(a);
  }

  /**
   * Removes the action from this concentrator.
   * 
   * @param a the action to be removed.
   */
  public void removeAction(final Action a)
  {
    if (a == null)
    {
      throw new NullPointerException();
    }
    actions.remove(a);
  }

  /**
   * Defines the state for all actions. 
   * 
   * @param b the new state for all actions.
   */
  public void setEnabled(final boolean b)
  {
    for (int i = 0; i < actions.size(); i++)
    {
      final Action a = (Action) actions.get(i);
      a.setEnabled(b);
    }
  }

  /**
   * Returns, whether all actions are disabled.
   * If one action is enabled, then this method will return
   * true.
   * 
   * @return true, if at least one action is enabled, false
   * otherwise.
   */
  public boolean isEnabled()
  {
    for (int i = 0; i < actions.size(); i++)
    {
      final Action a = (Action) actions.get(i);
      if (a.isEnabled())
      {
        return true;
      }
    }
    return false;
  }
}
