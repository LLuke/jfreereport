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
 * $Id: DefaultReportControler.java,v 1.2 2005/03/29 18:31:59 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.gui.base;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;

public class DefaultReportControler extends JPanel implements ReportControler
{
  private PreviewProxyBase previewBase;

  private class EnabledUpdateHandler implements PropertyChangeListener
  {
    public EnabledUpdateHandler ()
    {
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property
     *            that has changed.
     */

    public void propertyChange (final PropertyChangeEvent evt)
    {
      if (evt.getPropertyName().equals("lockInterface") == false)
      {
        return;
      }
      DefaultReportControler.this.setEnabled(Boolean.FALSE.equals(evt.getNewValue()));
    }
  }

  private EnabledUpdateHandler enabledUpdateHandler;
  /**
   * Creates a new <code>JPanel</code> with a double buffer and a flow layout.
   */
  public DefaultReportControler ()
  {
    enabledUpdateHandler = new EnabledUpdateHandler();
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
   * Returns the assigned preview proxy base or null if the controler is not connected
   * yet.
   *
   * @return the proxy base.
   */
  public PreviewProxyBase getPreviewBase ()
  {
    return previewBase;
  }

  /**
   * Assigns the preview base to this controler. This is done automaticy by the
   * PreviewProxyBase itself.
   * <p/>
   * The proxyBase parameter will be null, if the report controler has been removed.
   *
   * @param proxyBase the proxy base.
   */
  public void setPreviewBase (final PreviewProxyBase proxyBase)
  {
    if (this.previewBase != null)
    {
      this.previewBase.removePropertyChangeListener("lockInterface", enabledUpdateHandler);
      setEnabled(false);
    }
    this.previewBase = proxyBase;
    if (this.previewBase != null)
    {
      this.previewBase.addPropertyChangeListener("lockInterface", enabledUpdateHandler);
      setEnabled(this.previewBase.isLockInterface() == false);
    }
  }

  /**
   * Returns the location for the report controler, one of BorderLayout.NORTH,
   * BorderLayout.SOUTH, BorderLayout.EAST or BorderLayout.WEST.
   *
   * @return the location;
   */
  public String getControlerLocation ()
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
}
