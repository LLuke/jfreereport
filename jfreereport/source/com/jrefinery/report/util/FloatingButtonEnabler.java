/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * FloatingButtonEnabler.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.util;

import javax.swing.AbstractButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Enables a button to have a simple floating effect. The border of the button is only visible,
 * when the mouse pointer is floating over the button.
 */
public class FloatingButtonEnabler extends MouseAdapter
{
  private static FloatingButtonEnabler singleton;

  protected FloatingButtonEnabler ()
  {
  }

  /**
   * returns a default instance of this enabler
   */
  public static FloatingButtonEnabler getInstance ()
  {
    if (singleton == null)
    {
      singleton = new FloatingButtonEnabler ();
    }
    return singleton;
  }

  /**
   * Adds a button to this enabler.
   */
  public void addButton (AbstractButton button)
  {
    button.addMouseListener (this);
    button.setBorderPainted (false);
  }

  /**
   * removes a button from the enabler
   */
  public void removeButton (AbstractButton button)
  {
    button.addMouseListener (this);
    button.setBorderPainted (true);
  }

  /**
   * Triggers the drawing of the border when the mouse entered the button area
   */
  public void mouseEntered (MouseEvent e)
  {
    if (e.getSource () instanceof AbstractButton)
    {
      AbstractButton button = (AbstractButton) e.getSource ();
      if (button.isEnabled ())
        button.setBorderPainted (true);
    }
  }

  /**
   * disables the drawing of the border when the mouse leaves the button area
   */
  public void mouseExited (MouseEvent e)
  {
    if (e.getSource () instanceof AbstractButton)
    {
      AbstractButton button = (AbstractButton) e.getSource ();
      button.setBorderPainted (false);
    }
  }


}
