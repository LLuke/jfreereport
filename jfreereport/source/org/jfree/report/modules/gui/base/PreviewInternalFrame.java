/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -------------------------
 * PreviewInternalFrame.java
 * -------------------------
 * (C)opyright 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PreviewInternalFrame.java,v 1.15 2005/01/25 00:01:17 taqua Exp $
 *
 * Changes
 * -------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 * 08-Oct-2003 : Removed event mapping from WindowClosing to CloseAction
 *
 */

package org.jfree.report.modules.gui.base;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import javax.swing.Action;
import javax.swing.JInternalFrame;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.util.ResourceBundleSupport;

/**
 * An internal frame that is used to preview reports.
 *
 * @author Thomas Morgner.
 */
public class PreviewInternalFrame extends JInternalFrame implements PreviewProxy
{
  /**
   * Default 'close' action for the frame.
   */
  private class DefaultCloseAction extends CloseAction
  {
    /**
     * Creates a 'close' action.
     */
    public DefaultCloseAction()
    {
      super(getResources());
    }

    /**
     * Closes the preview frame if the default close operation is set to dispose
     * so this frame is reusable.
     *
     * @param e The action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      if (getDefaultCloseOperation() == DISPOSE_ON_CLOSE)
      {
        dispose();
      }
      else
      {
        setVisible(false);
      }
      try
      {
        setClosed(true);
      }
      catch (PropertyVetoException pe)
      {
        // Ignored..
      }
    }
  }

  /** A preview proxy. */
  private PreviewProxyBase base;

  /** Localised resources. */
  private ResourceBundleSupport resources;

  /**
   * Constructs a preview frame that displays the specified report.
   *
   * @param report  the report to be displayed.
   *
   * @throws org.jfree.report.ReportProcessingException if there is a problem processing the report.
   */
  public PreviewInternalFrame(final JFreeReport report) throws ReportProcessingException
  {
    init(report);
  }

  /**
   * Initialise.
   *
   * @param report  the report.
   *
   * @throws org.jfree.report.ReportProcessingException if there is a problem processing the report.
   */
  private void init(final JFreeReport report) throws ReportProcessingException
  {
    base = createPreviewProxyBase();
    base.setReport(report);
    setContentPane(base);
  }

  protected PreviewProxyBase createPreviewProxyBase ()
  {
    return new PreviewProxyBase(this);
  }

  /**
   * Retrieves the resources for this PreviewFrame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  public ResourceBundleSupport getResources()
  {
    if (resources == null)
    {
      resources = new ResourceBundleSupport(PreviewBaseModule.RESOURCES_BASE_NAME);
    }
    return resources;
  }

  /**
   * Creates a default close action.
   *
   * @return The action.
   */
  public Action createDefaultCloseAction()
  {
    return new DefaultCloseAction();
  }

  /**
   * Disposes the frame.
   */
  public void dispose()
  {
    super.dispose();
    base.dispose();
  }

  /**
   * Shuts down the preview component. Once the component is closed, it
   * cannot be reactivated anymore.
   */
  public void close()
  {
    base.close();
  }

  /**
   * Returns the preview proxy.
   *
   * @return The proxy.
   */
  public PreviewProxyBase getBase()
  {
    return base;
  }

}
