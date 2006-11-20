/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * ReportControler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportControler.java,v 1.2 2005/03/29 18:32:00 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.gui.swing.preview;

import javax.swing.JComponent;
import javax.swing.JMenu;

/**
 * A report controler. This provides some means of configuring the
 * preview components.
 * <p>
 * The controler should use the propertyChange events provided by
 * the PreviewProxyBase and the ReportPane to update its state.
 * <p>
 * To force a new repagination, use the <code>refresh</code> method of
 * the PreviewProxyBase.
 *
 * @author Thomas Morgner
 */
public interface ReportController
{
  /**
   * Returns the graphical representation of the controler.
   * This component will be added between the menu bar and
   * the toolbar.
   * <p>
   * Changes to this property are not detected automaticly,
   * you have to call "refreshControler" whenever you want to
   * display a completly new control panel.
   *
   * @return the controler component.
   */
  public JComponent getControlPanel();

  /**
   * Returns the menus that should be inserted into the menubar.
   * <p>
   * Changes to this property are not detected automaticly,
   * you have to call "refreshControler" whenever the contents
   * of the menu array changed.
   *
   * @return the menus as array, never null.
   */
  public JMenu[] getMenus();

  /**
   * Defines, whether the controler component is placed between
   * the preview pane and the toolbar.
   *
   * @return true, if this is a inne component.
   */
  public boolean isInnerComponent ();

  /**
   * Returns the location for the report controler, one of
   * BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.EAST
   * or BorderLayout.WEST.
   *
   * @return the location;
   */
  public String getControlerLocation ();

  /**
   * Enables or disables the controler.
   *
   * @param enabled
   */
  public void setEnabled (boolean enabled);

  /**
   * Checks, whether this controler is enabled.
   * @return
   */
  public boolean isEnabled();

  public void initialize (PreviewPane pane);
}
