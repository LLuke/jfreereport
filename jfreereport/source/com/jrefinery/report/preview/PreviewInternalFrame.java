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
 * -------------------------
 * PreviewInternalFrame.java
 * -------------------------
 * (C)opyright 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PreviewInternalFrame.java,v 1.4 2003/06/13 22:54:00 taqua Exp $
 *
 * Changes
 * -------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.preview;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.action.CloseAction;

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
    public void actionPerformed(ActionEvent e)
    {
      if (getDefaultCloseOperation() == DISPOSE_ON_CLOSE)
      {
        dispose();
      }
      else
      {
        setVisible(false);
      }
    }
  }

  /** A preview proxy. */
  private PreviewProxyBase base;

  /** Localised resources. */
  private ResourceBundle resources;

  /**
   * Constructs a preview frame that displays the specified report.
   *
   * @param report  the report to be displayed.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public PreviewInternalFrame(JFreeReport report) throws ReportProcessingException
  {
    init(report);
  }

  /**
   * Initialise.
   *
   * @param report  the report.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  private void init(JFreeReport report) throws ReportProcessingException
  {
    base = new PreviewProxyBase(this);
    base.init(report);
    registerCloseActions();
    setContentPane(base);
  }

  /**
   * Retrieves the resources for this PreviewFrame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  public ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(PreviewProxyBase.BASE_RESOURCE_CLASS);
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
    base.dispose();
    super.dispose();
  }

  /**
   * Registers close actions.
   */
  protected void registerCloseActions()
  {
    addInternalFrameListener(new InternalFrameAdapter()
    {
      /**
       * Invoked when an internal frame is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void internalFrameClosing(InternalFrameEvent e)
      {
        base.getCloseAction().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
            "CloseFrame"));
      }
    }
    );
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
