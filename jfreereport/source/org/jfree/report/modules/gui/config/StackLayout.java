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
 * StackLayout.java
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
 * 27.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Insets;

public class StackLayout implements LayoutManager
{
  public StackLayout()
  {
  }

  /**
   * If the layout manager uses a per-component string,
   * adds the component <code>comp</code> to the layout,
   * associating it
   * with the string specified by <code>name</code>.
   *
   * @param name the string to be associated with the component
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
   * container, given the components it contains.
   * @param parent the container to be laid out
   *
   * @see #minimumLayoutSize
   */
  public Dimension preferredLayoutSize(Container parent)
  {
//    double width = 0;
//    double height = 0;
//    Component[] comps = parent.getComponents();
//    for (int i = 0; i < comps.length; i++)
//    {
//      Dimension prefLayout = comps[i].getPreferredSize();
//      if (prefLayout.getWidth() > width)
//      {
//        width = prefLayout.getWidth();
//      }
//      if (prefLayout.getHeight() > height)
//      {
//        height = prefLayout.getHeight();
//      }
//    }
//    return new Dimension((int) width, (int) height);
    synchronized (parent.getTreeLock()) {
        Insets insets = parent.getInsets();
        int ncomponents = parent.getComponentCount();
        int w = 0;
        int h = 0;

        for (int i = 0 ; i < ncomponents ; i++) {
            Component comp = parent.getComponent(i);
            Dimension d = comp.getPreferredSize();
            if (d.width > w) {
                w = d.width;
            }
            if (d.height > h) {
                h = d.height;
            }
        }
        return new Dimension(insets.left + insets.right + w,
                             insets.top + insets.bottom + h);
    }
  }

  /**
   * Calculates the minimum size dimensions for the specified
   * container, given the components it contains.
   * @param parent the component to be laid out
   * @see #preferredLayoutSize
   */
  public Dimension minimumLayoutSize(Container parent)
  {
    synchronized (parent.getTreeLock()) {
        Insets insets = parent.getInsets();
        int ncomponents = parent.getComponentCount();
        int w = 0;
        int h = 0;

        for (int i = 0 ; i < ncomponents ; i++) {
            Component comp = parent.getComponent(i);
            Dimension d = comp.getMinimumSize();
            if (d.width > w) {
                w = d.width;
            }
            if (d.height > h) {
                h = d.height;
            }
        }
        return new Dimension(insets.left + insets.right + w ,
                             insets.top + insets.bottom + h );
    }
//    double width = 0;
//    double height = 0;
//    Component[] comps = parent.getComponents();
//    for (int i = 0; i < comps.length; i++)
//    {
//      Dimension minLayout = comps[i].getMinimumSize();
//      if (minLayout.getWidth() > width)
//      {
//        width = minLayout.getWidth();
//      }
//      if (minLayout.getHeight() > height)
//      {
//        height = minLayout.getHeight();
//      }
//    }
//    return new Dimension((int) width, (int) height);
  }

  /**
   * Lays out the specified container.
   * @param parent the container to be laid out
   */
  public void layoutContainer(Container parent)
  {
    Component[] comps = parent.getComponents();
    for (int i = 0; i < comps.length; i++)
    {
      comps[i].setSize(parent.getSize());
      comps[i].doLayout();
    }
  }
}
