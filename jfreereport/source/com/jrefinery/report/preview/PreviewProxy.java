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
 * -----------------
 * PreviewProxy.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PreviewProxy.java,v 1.4 2003/06/13 22:54:00 taqua Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.preview;

import java.awt.event.ComponentListener;
import javax.swing.Action;
import javax.swing.JMenuBar;

/**
 * A proxy for the report preview component.
 *
 * @author Thomas Morgner.
 */
public interface PreviewProxy
{
  /**
   * Packs the preview component.
   */
  public void pack();

  /**
   * Disposes the preview component.
   */
  public void dispose();

  /**
   * Adds a component listener to the preview component.
   *
   * @param listener  the listener.
   */
  public void addComponentListener(ComponentListener listener);

  /**
   * Removes the component listener.
   *
   * @param listener  the listener.
   */
  public void removeComponentListener(ComponentListener listener);

  /**
   * Creates a default close action.
   *
   * @return The close action.
   */
  public Action createDefaultCloseAction();

  /**
   * Sets the menu bar for the preview component.
   *
   * @param bar  the menu bar.
   */
  public void setJMenuBar(JMenuBar bar);

  /**
   * Sets the title for the preview component.
   *
   * @param title  the title.
   */
  public void setTitle(String title);

  /**
   * Returns the proxybase used to implement the behaviour of the report
   * preview component.
   *
   * @return the proxybase of this preview component.
   */
  public PreviewProxyBase getBase();
}
