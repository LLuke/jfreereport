/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ActionConcentrator.java
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
 * 24.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.base.components;

import java.util.ArrayList;
import javax.swing.Action;

public class ActionConcentrator
{
  private ArrayList actions;

  public ActionConcentrator()
  {
    actions = new ArrayList();
  }

  public void addAction (Action a)
  {
    actions.add(a);
  }

  public void removeAction (Action a)
  {
    actions.remove(a);
  }

  public void setEnabled (boolean b)
  {
    for (int i = 0; i < actions.size(); i++)
    {
      Action a = (Action) actions.get(i);
      a.setEnabled(b);
    }
  }

  public boolean isEnabled ()
  {
    for (int i = 0; i < actions.size(); i++)
    {
      Action a = (Action) actions.get(i);
      if (a.isEnabled())
      {
        return true;
      }
    }
    return false;
  }
}
