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
 * VerticalLayout.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: VerticalLayout.java,v 1.3 2003/09/30 19:47:29 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 31.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

/**
 * A simple layout manager, which aligns all components in a vertical
 * flow layout.
 * 
 * @author Thomas Morgner
 */
public class VerticalLayout implements LayoutManager
{

  private boolean useSizeFromParent;

  /**
   * DefaultConstructor.
   */
  public VerticalLayout()
  {
    this (true);
  }

  /**
   * DefaultConstructor.
   */
  public VerticalLayout(boolean useParent)
  {
    this.useSizeFromParent = useParent;
  }

  /**
   * Adds the specified component with the specified name to
   * the layout.
   * @param name the component name
   * @param comp the component to be added
   */
  public void addLayoutComponent(String name, Component comp)
  {
  }

  /**
   * Removes the specified component from the layout.
   * @param comp the component to be removed
   */
  public void removeLayoutComponent(Component comp)
  {
  }

  /**
   * Calculates the preferred size dimensions for the specified
   * panel given the components in the specified parent container.
   * @param parent the component to be laid out
   * @return the preferred layout size
   * @see #minimumLayoutSize
   */
  public Dimension preferredLayoutSize(Container parent)
  {
    synchronized (parent.getTreeLock())
    {
      Insets ins = parent.getInsets();
      Component[] comps = parent.getComponents();
      int height = 0;
      int width = 0;
      for (int i = 0; i < comps.length; i++)
      {
        if (comps[i].isVisible() == false)
        {
          continue;
        }
        Dimension pref = comps[i].getPreferredSize();
        height += pref.height;
        if (pref.width > width)
        {
          width = pref.width;
        }
      }
//      Log.debug ("PreferredSize in VLayout: " + new Dimension(width + ins.left + ins.right,
//          height + ins.top + ins.bottom));
      return new Dimension(width + ins.left + ins.right,
          height + ins.top + ins.bottom);
    }
  }

  /**
   * Calculates the minimum size dimensions for the specified
   * panel given the components in the specified parent container.
   * @param parent the component to be laid out
   * @return the minimul layoutsize
   * @see #preferredLayoutSize
   */
  public Dimension minimumLayoutSize(Container parent)
  {
    synchronized (parent.getTreeLock())
    {
      Insets ins = parent.getInsets();
      Component[] comps = parent.getComponents();
      int height = 0;
      int width = 0;
      for (int i = 0; i < comps.length; i++)
      {
        if (comps[i].isVisible() == false)
        {
          continue;
        }
        Dimension min = comps[i].getMinimumSize();
        height += min.height;
        if (min.width > width)
        {
          width = min.width;
        }
      }
      return new Dimension(width + ins.left + ins.right,
          height + ins.top + ins.bottom);
    }
  }

  public boolean isUseSizeFromParent()
  {
    return useSizeFromParent;
  }

  /**
   * Lays out the container in the specified panel.
   * @param parent the component which needs to be laid out
   */
  public void layoutContainer(Container parent)
  {
    synchronized (parent.getTreeLock())
    {
      Insets ins = parent.getInsets();
      int insHorizontal = ins.left + ins.right;

      int width;
      if (isUseSizeFromParent())
      {
        Rectangle bounds = parent.getBounds();
        width = bounds.width - insHorizontal;
      }
      else
      {
        width = preferredLayoutSize(parent).width - insHorizontal;
      }
      Component[] comps = parent.getComponents();

      //final int x = bounds.x + ins.left;
      int y = /*bounds.y + */ ins.top;

      for (int i = 0; i < comps.length; i++)
      {
        Component c = comps[i];
        if (c.isVisible() == false)
        {
          continue;
        }
        Dimension dim = c.getPreferredSize();
        c.setBounds(0, y, width, dim.height);
        y += dim.height;
      }
    }
  }
}
