/**
 * Date: Dec 10, 2002
 * Time: 10:52:06 PM
 *
 * $Id$
 */
package com.jrefinery.report.util;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Component;
import java.awt.Dimension;

public class WindowSizeLimiter implements ComponentListener
{
  private Object currentSource;
  /**
   * Invoked when the component's size changes.
   */
  public void componentResized(ComponentEvent e)
  {
    if (e.getSource() == currentSource) return;

    if (e.getSource() instanceof Component)
    {
      currentSource = e.getSource();
      Component c = (Component) e.getSource();
      Dimension d = c.getMaximumSize();
      Dimension s = c.getSize();
      if (s.width > d.width) s.width = d.width;
      if (s.height > d.height) s.height = d.height;
      c.setSize(s);
      currentSource = null;
    }

  }

  /**
   * Invoked when the component's position changes.
   */
  public void componentMoved(ComponentEvent e)
  {
  }

  /**
   * Invoked when the component has been made visible.
   */
  public void componentShown(ComponentEvent e)
  {
  }

  /**
   * Invoked when the component has been made invisible.
   */
  public void componentHidden(ComponentEvent e)
  {
  }
}
