/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * -----------------
 * ActionButton.java
 * -----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ActionRadioButton.java,v 1.2 2003/08/24 15:08:18 taqua Exp $
 *
 * ChangeLog
 * ---------
 * 30-Aug-2002 : Initial version
 * 01-Sep-2002 : Documentation
 * 10-Dec-2002 : Minor Javadoc updates (DG);
 *
 */

package org.jfree.report.modules.gui.base.components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import org.jfree.report.util.Log;

/**
 * The ActionRadioButton is used to connect an Action and its properties to a JRadioButton.
 * This functionality is already implemented in JDK 1.3 but needed for JDK 1.2.2 compatibility.
 *
 * @author Thomas Morgner
 */
public class ActionRadioButton extends JRadioButton
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
    public void propertyChange(final PropertyChangeEvent event)
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
          setText((String) getAction().getValue
              (Action.NAME));
        }
        else if (event.getPropertyName().equals(Action.SHORT_DESCRIPTION))
        {
          ActionRadioButton.this.setToolTipText((String)
              getAction().getValue(Action.SHORT_DESCRIPTION));
        }

        final Action ac = getAction();
        if (event.getPropertyName().equals(ActionDowngrade.ACCELERATOR_KEY))
        {
          final KeyStroke oldVal = (KeyStroke) event.getOldValue();
          if (oldVal != null)
          {
            unregisterKeyboardAction
                (oldVal);
          }
          final Object o = ac.getValue(ActionDowngrade.ACCELERATOR_KEY);
          if (o instanceof KeyStroke && o != null)
          {
            final KeyStroke k = (KeyStroke) o;
            registerKeyboardAction(ac, k, WHEN_IN_FOCUSED_WINDOW);
          }
        }
        else if (event.getPropertyName().equals(ActionDowngrade.MNEMONIC_KEY))
        {
          final Object o = ac.getValue(ActionDowngrade.MNEMONIC_KEY);
          if (o != null)
          {
            if (o instanceof Character)
            {
              final Character c = (Character) o;
              setMnemonic(c.charValue());
            }
            else if (o instanceof Integer)
            {
              final Integer c = (Integer) o;
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
   * Creates a Button without any text and without an assigned Action.
   */
  public ActionRadioButton()
  {
    super();
  }

  /**
   * Creates a Button and set the given text as label.
   *
   * @param text  the label for the new button.
   */
  public ActionRadioButton(final String text)
  {
    super(text);
  }

  /**
   * Creates an ActionButton and sets the given text and icon on the button.
   *
   * @param text  the label for the new button.
   * @param icon  the icon for the button.
   */
  public ActionRadioButton(final String text, final Icon icon)
  {
    super(text, icon);
  }


  /**
   * Creates an ActionButton and sets the given icon on the button.
   *
   * @param icon  the icon for the button.
   */
  public ActionRadioButton(final Icon icon)
  {
    super(icon);
  }

  /**
   * Nreates an ActionButton and assigns the given action with the button.
   *
   * @param action  the action.
   */
  public ActionRadioButton(final Action action)
  {
    setAction(action);
  }

  /**
   * Returns the assigned action or null if no action has been assigned.
   *
   * @return the action (possibly null).
   */
  public Action getAction()
  {
    return action;
  }


  /**
   * Returns and initializes the PropertyChangehandler for this ActionButton.
   * The PropertyChangeHandler monitors the action and updates the button if necessary.
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
   * Enables and disables this button and if an action is assigned to this button the
   * propertychange is forwarded to the assigned action.
   *
   * @param b the new enable-state of this button
   */
  public void setEnabled(final boolean b)
  {
    super.setEnabled(b);
    if (getAction() != null)
    {
      getAction().setEnabled(b);
    }
  }

  /**
   * Assigns the given action to this button. The properties of the action will be assigned to
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
  public void setAction(final Action newAction)
  {
    final Action oldAction = getAction();
    if (oldAction != null)
    {
      removeActionListener(oldAction);
      oldAction.removePropertyChangeListener(getPropertyChangeHandler());

      final Object o = oldAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
      if (o instanceof KeyStroke && o != null)
      {
        final KeyStroke k = (KeyStroke) o;
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
          final Character c = (Character) o;
          setMnemonic(c.charValue());
        }
        else if (o instanceof Integer)
        {
          final Integer c = (Integer) o;
          setMnemonic(c.intValue());
        }
      }
      o = newAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
      if (o instanceof KeyStroke && o != null)
      {
        final KeyStroke k = (KeyStroke) o;
        registerKeyboardAction(newAction, k, WHEN_IN_FOCUSED_WINDOW);
      }
    }
  }
}

