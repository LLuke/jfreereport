/**
 * ----------------------
 * ActionButton.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.util;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ActionButton extends JButton
{
  // as defined in JDK 1.3
  public static final String KEYSTROKE = "keystroke";
  // as defined in JDK 1.3
  public static final String MNEMONIC_PROPERTY = "mnemonic";

  private Action action;
  private ActionEnablePropertyChangeHandler propertyChangeHandler;

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
        else if (event.getPropertyName().equals
            (Action.SHORT_DESCRIPTION))
        {
          ActionButton.this.setToolTipText((String)
              getAction().getValue(Action.SHORT_DESCRIPTION));
        }

        Action ac = getAction();
        if (event.getPropertyName().equals
            (KEYSTROKE))
        {
          KeyStroke oldVal = (KeyStroke)
              event.getOldValue();
          if (oldVal != null)
          {
            unregisterKeyboardAction
                (oldVal);
          }
          Object o = ac.getValue(KEYSTROKE);
          if (o instanceof KeyStroke && o != null)
          {
            KeyStroke k = (KeyStroke) o;
            registerKeyboardAction(ac, k, WHEN_IN_FOCUSED_WINDOW);
          }
        }
        else if (event.getPropertyName().equals(MNEMONIC_PROPERTY))
        {
          Object o = ac.getValue(MNEMONIC_PROPERTY);
          if (o != null && o instanceof Character)
          {
            Character c = (Character) o;
            setMnemonic(c.charValue());
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  protected ActionEnablePropertyChangeHandler getPropertyChangeHandler ()
  {
    if (propertyChangeHandler == null)
    {
      propertyChangeHandler = new ActionEnablePropertyChangeHandler();
    }
    return propertyChangeHandler;
  }

  public ActionButton()
  {
    super();
  }

  public ActionButton(String text)
  {
    super(text);
  }

  public ActionButton(String text, javax.swing.Icon icon)
  {
    super(text, icon);
  }

  public ActionButton(javax.swing.Icon icon)
  {
    super(icon);
  }

  public ActionButton (Action action)
  {
    setAction(action);
  }

  public Action getAction()
  {
    return action;
  }

  public void setEnabled(boolean b)
  {
    super.setEnabled(b);
    if (getAction() != null)
    {
      getAction().setEnabled(b);
    }
  }

  public void setAction(Action newAction)
  {
    Action oldAction = getAction();
    if (oldAction != null)
    {
      removeActionListener(oldAction);
      oldAction.removePropertyChangeListener(getPropertyChangeHandler());

      Object o = oldAction.getValue(KEYSTROKE);
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

      Object o = newAction.getValue(MNEMONIC_PROPERTY);
      if (o != null && o instanceof Character)
      {
        Character c = (Character) o;
        setMnemonic(c.charValue());
      }
      o = newAction.getValue(KEYSTROKE);
      if (o instanceof KeyStroke && o != null)
      {
        KeyStroke k = (KeyStroke) o;
        registerKeyboardAction(newAction, k, WHEN_IN_FOCUSED_WINDOW);
      }
    }
  }
}

