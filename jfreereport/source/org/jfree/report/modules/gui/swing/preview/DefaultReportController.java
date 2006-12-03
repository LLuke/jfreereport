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
 * DefaultReportControler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultReportController.java,v 1.1 2006/11/20 21:17:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.gui.swing.preview;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;

public class DefaultReportController extends JPanel implements ReportController
{
  /**
   * Creates a new <code>JPanel</code> with a double buffer and a flow layout.
   */
  public DefaultReportController ()
  {
  }

  /**
   * Returns the graphical representation of the controler. This component will be added
   * between the menu bar and the toolbar.
   * <p/>
   * Changes to this property are not detected automaticly, you have to call
   * "refreshControler" whenever you want to display a completly new control panel.
   *
   * @return the controler component.
   */
  public JComponent getControlPanel ()
  {
    return this;
  }

  /**
   * The default implementation has no menus.
   *
   * @return an empty array.
   */
  public JMenu[] getMenus ()
  {
    return new JMenu[0];
  }

  /**
   * Returns the location for the report controler, one of BorderLayout.NORTH,
   * BorderLayout.SOUTH, BorderLayout.EAST or BorderLayout.WEST.
   *
   * @return the location;
   */
  public String getControllerLocation ()
  {
    return BorderLayout.NORTH;
  }

  /**
   * Defines, whether the controler component is placed between the report pane and the
   * toolbar.
   *
   * @return true, if this is a inne component.
   */
  public boolean isInnerComponent ()
  {
    return false;
  }

  public void initialize(PreviewPane pane)
  {

  }
}
