/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -------------------
 * ActionMenuItem.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ActionMenuItem.java,v 1.12 2003/05/14 22:26:40 taqua Exp $
 *
 * ChangeLog
 * ---------
 * 30-Aug-2002 : Initial version
 * 01-Aug-2002 : Documentation
 * 10-Dec-2002 : Minor Javadoc updates (DG);
 *
 */

package com.jrefinery.report.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * The ActionMenuItem is used to connect an Action and its properties to an MenuItem.
 * <P>
 * This functionality is already implemented in JDK 1.3 but needed for JDK 1.2.2 compatibility.
 *
 * @author Thomas Morgner
 */
public class ActionMenuItem extends JMenuItem
{
  /** The action. */
  private Action action;

  /** The property change handler. */
  private ActionEnablePropertyChangeHandler propertyChangeHandler;

  /**
   * Helperclass to handle the property change event raised by the action. Changed properties in
   * the action will affect the button.
   */
  private class ActionEnablePropertyChangeHandler implements PropertyChangeListener
  {
    /**
     * Receives notification of a property change event.
     *
     * @param event  the property change event.
     */
    public void propertyChange(PropertyChangeEvent event)
    {
      try
      {
        if (event.getPropertyName().equals("enabled"))
        {
          setEnabled(getAction().isEnabled());
        }
        else if (event.getPropertyName().equals(Action.SMALL_ICON))
        {
          setIcon((Icon) getAction().getValue(Action.SMALL_ICON));
        }
        else if (event.getPropertyName().equals(Action.NAME))
        {
          setText((String) getAction().getValue(Action.NAME));
        }
        else if (event.getPropertyName().equals(Action.SHORT_DESCRIPTION))
        {
          ActionMenuItem.this.setToolTipText((String)
              getAction().getValue(Action.SHORT_DESCRIPTION));
        }

        Action ac = getAction();
        if (event.getPropertyName().equals(ActionDowngrade.ACCELERATOR_KEY))
        {
          KeyStroke oldVal = (KeyStroke) event.getOldValue();
          if (oldVal != null)
          {
            unregisterKeyboardAction(oldVal);
          }
          Object o = ac.getValue(ActionDowngrade.ACCELERATOR_KEY);
          if (o instanceof KeyStroke && o != null)
          {
            KeyStroke k = (KeyStroke) o;
            registerKeyboardAction(ac, k, WHEN_IN_FOCUSED_WINDOW);
          }
        }
        else if (event.getPropertyName().equals(ActionDowngrade.MNEMONIC_KEY))
        {
          Object o = ac.getValue(ActionDowngrade.MNEMONIC_KEY);
          if (o != null)
          {
            if (o instanceof Character)
            {
              Character c = (Character) o;
              setMnemonic(c.charValue());
            }
            else if (o instanceof Integer)
            {
              Integer c = (Integer) o;
              setMnemonic(c.intValue());
            }
          }
        }
      }
      catch (Exception e)
      {
        Log.warn("Error on PropertyChange in ActionButton: ", e);
      }
    }
  }

  /**
   * Default constructor.
   */
  public ActionMenuItem()
  {
  }

  /**
   * Creates a menu item with the specified icon.
   *
   * @param icon  the icon.
   */
  public ActionMenuItem(Icon icon)
  {
    super(icon);
  }

  /**
   * Creates a menu item with the specified label.
   *
   * @param text  the label.
   */
  public ActionMenuItem(String text)
  {
    super(text);
  }

  /**
   * Creates a menu item with the specified label and icon.
   *
   * @param text  the label.
   * @param icon  the icon.
   */
  public ActionMenuItem(String text, Icon icon)
  {
    super(text, icon);
  }

  /**
   * Creates a new menu item with the specified label and mnemonic.
   *
   * @param text  the label.
   * @param i  the mnemonic.
   */
  public ActionMenuItem(String text, int i)
  {
    super(text, i);
  }

  /**
   * Creates a new menu item based on the specified action.
   *
   * @param action  the action.
   */
  public ActionMenuItem(Action action)
  {
    setAction(action);
  }

  /**
   * Returns the assigned action or null if no action has been assigned.
   *
   * @return the action.
   */
  public Action getAction()
  {
    return action;
  }

  /**
   * Returns and initializes the PropertyChangehandler for this ActionMenuItem.
   * The PropertyChangeHandler monitors the action and updates the menuitem if necessary.
   *
   * @return the property change handler.
   */
  private ActionEnablePropertyChangeHandler getPropertyChangeHandler()
  {
    if (propertyChangeHandler == null)
    {
      propertyChangeHandler = new ActionEnablePropertyChangeHandler();
    }
    return propertyChangeHandler;
  }

  /**
   * Enables and disables this button and if an action is assigned to this menuitem the
   * propertychange is forwarded to the assigned action.
   *
   * @param b the new enable-state of this menuitem
   */
  public void setEnabled(boolean b)
  {
    super.setEnabled(b);
    if (getAction() != null)
    {
      getAction().setEnabled(b);
    }
  }

  /**
   * Assigns the given action to this menuitem. The properties of the action will be assigned to
   * the menuitem. If an previous action was set, the old action is unregistered.
   * <p>
   * <ul>
   * <li>NAME - specifies the menuitem text
   * <li>SMALL_ICON - specifies the menuitems icon
   * <li>MNEMONIC_KEY - specifies the menuitems mnemonic key
   * <li>ACCELERATOR_KEY - specifies the menuitems accelerator
   * </ul>
   *
   * @param newAction the new action
   */
  public void setAction(Action newAction)
  {
    Action oldAction = getAction();
    if (oldAction != null)
    {
      removeActionListener(oldAction);
      oldAction.removePropertyChangeListener(getPropertyChangeHandler());

      Object o = oldAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
      if (o instanceof KeyStroke && o != null)
      {
        KeyStroke k = (KeyStroke) o;
        unregisterKeyboardAction(k);
      }
    }
    this.action = newAction;
    if (action != null)
    {
      addActionListener(newAction);
      newAction.addPropertyChangeListener(getPropertyChangeHandler());

      setText((String) (newAction.getValue(Action.NAME)));
      setToolTipText((String) (newAction.getValue(Action.SHORT_DESCRIPTION)));
      setIcon((Icon) newAction.getValue(Action.SMALL_ICON));
      setEnabled(action.isEnabled());

      Object o = newAction.getValue(ActionDowngrade.MNEMONIC_KEY);
      if (o != null)
      {
        if (o instanceof Character)
        {
          Character c = (Character) o;
          setMnemonic(c.charValue());
        }
        else if (o instanceof Integer)
        {
          Integer c = (Integer) o;
          setMnemonic(c.intValue());
        }
      }

      o = newAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
      if (o instanceof KeyStroke && o != null)
      {
        KeyStroke k = (KeyStroke) o;
        registerKeyboardAction(newAction, k, WHEN_IN_FOCUSED_WINDOW);
      }
    }
  }
}
