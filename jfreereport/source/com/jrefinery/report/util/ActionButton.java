/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ----------------------
 * ActionButton.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 * 30-Aug-2002 : Initial version
 * 01-Sep-2002 : Documentation
 */
package com.jrefinery.report.util;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The ActionButton is used to connect an Action and its properties to a Button. This functionality
 * is already implemented in JDK 1.3 but needed for JDK 1.2.2 compatibility.
 */
public class ActionButton extends JButton
{
  private Action action;
  private ActionEnablePropertyChangeHandler propertyChangeHandler;

  /**
   * Helperclass to handle the property change event raised by the action. Changed properties in
   * the action will affect the button.
   */
  private class ActionEnablePropertyChangeHandler implements
      PropertyChangeListener
  {
    public void propertyChange(PropertyChangeEvent event)
    {
      try
      {
        if (event.getPropertyName().equals("enabled"))
        {
          setEnabled(getAction().isEnabled());
        }
        else if (event.getPropertyName().equals
            (Action.SMALL_ICON))
        {
          setIcon((Icon) getAction().getValue
              (Action.SMALL_ICON));
        }
        else if (event.getPropertyName().equals(Action.NAME))
        {
          setText((String) getAction().getValue
              (Action.NAME));
        }
        else if (event.getPropertyName().equals(Action.SHORT_DESCRIPTION))
        {
          ActionButton.this.setToolTipText((String)
              getAction().getValue(Action.SHORT_DESCRIPTION));
        }

        Action ac = getAction();
        if (event.getPropertyName().equals (ActionDowngrade.ACCELERATOR_KEY))
        {
          KeyStroke oldVal = (KeyStroke)
              event.getOldValue();
          if (oldVal != null)
          {
            unregisterKeyboardAction
                (oldVal);
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
          if (o != null && o instanceof Character)
          {
            Character c = (Character) o;
            setMnemonic(c.charValue());
          }
        }
      }
      catch (Exception e)
      {
        Log.warn ("Error on PropertyChange in ActionButton: ", e);
      }
    }
  }

  /**
   * Creates a Button without any text and without an assigned Action.
   */
  public ActionButton()
  {
    super();
  }

  /**
   * Creates a Button and set the given text as label.
   *
   * @param text the label for the new button
   */
  public ActionButton(String text)
  {
    super(text);
  }

  /**
   * creates a ActionButton and sets the given text and icon on the button
   *
   * @param text the label for the new button
   * @param icon the icon for the button
   */
  public ActionButton(String text, Icon icon)
  {
    super(text, icon);
  }


  /**
   * creates a ActionButton and sets the given icon on the button
   *
   * @param icon the icon for the button
   */
  public ActionButton(Icon icon)
  {
    super(icon);
  }


  /**
   * creates a ActionButton and assignes the given action with the button.
   *
   * @param text the label for the new button
   * @param icon the icon for the button
   */
  public ActionButton (Action action)
  {
    setAction(action);
  }


  /**
   * returns the assigned action or null if no action has been assigned.
   */
  public Action getAction()
  {
    return action;
  }


  /**
   * Returns and initializes the PropertyChangehandler for this ActionButton.
   * The PropertyChangeHandler monitors the action and updates the button if necessary
   */
  protected ActionEnablePropertyChangeHandler getPropertyChangeHandler ()
  {
    if (propertyChangeHandler == null)
    {
      propertyChangeHandler = new ActionEnablePropertyChangeHandler();
    }
    return propertyChangeHandler;
  }

  /**
   * Enables and disables this button and if an action is assigned to this button the
   * propertychange is forwarded to the assigned action.
   *
   * @param b the new enable-state of this button
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
   * Assignes the given action to this button. The properties of the action will be assigned to
   * the button. If an previous action was set, the old action is unregistered.
   * <p>
   * <ul>
   * <li>NAME - specifies the button text
   * <li>SMALL_ICON - specifies the buttons icon
   * <li>MNEMONIC_KEY - specifies the buttons mnemonic key
   * <li>ACCELERATOR_KEY - specifies the buttons accelerator
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
      setToolTipText((String) (newAction.getValue
          (Action.SHORT_DESCRIPTION)));
      setIcon((Icon) newAction.getValue(Action.SMALL_ICON));
      setEnabled(action.isEnabled());

      Object o = newAction.getValue(ActionDowngrade.MNEMONIC_KEY);
      if (o != null && o instanceof Character)
      {
        Character c = (Character) o;
        setMnemonic(c.charValue());
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

