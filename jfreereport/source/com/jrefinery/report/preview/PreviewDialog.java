/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------
 * PreviewDialog.java
 * ------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PreviewDialog.java,v 1.6 2002/12/12 12:26:56 mungady Exp $
 *
 * Changes (from 4-Dec-2002)
 * -------------------------
 * 04-Dec-2002 : Forked from PreviewFrame (TM);
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.preview;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.action.CloseAction;

import javax.swing.Action;
import javax.swing.JDialog;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

/**
 * A standard print preview dialog for any JFreeReport.  Allows the user to page back and forward
 * through the report, zoom in and out, and send the output to the printer.
 * <P>
 * You can also save the report in PDF format (thanks to the iText library).
 * <p>
 * When including this PreviewDialog in yuor own programs, you should override the provided
 * createXXXAction methods to include your customized actions.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class PreviewDialog extends JDialog implements PreviewProxy
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

  private PreviewProxyBase base;
  private ResourceBundle resources;

  /**
   * Creates a non-modal dialog without a title and without
   * a specified Frame owner.  A shared, hidden frame will be
   * set as the owner of the Dialog.
   */
  public PreviewDialog(JFreeReport report)
      throws ReportProcessingException
  {
    init (report);
  }

  /**
   * Creates a non-modal dialog without a title with the
   * specifed Frame as its owner.
   *
   * @param owner the Frame from which the dialog is displayed
   */
  public PreviewDialog(JFreeReport report, Frame owner)
      throws ReportProcessingException
  {
    super(owner);
    init (report);
  }

  /**
   * Creates a modal or non-modal dialog without a title and
   * with the specified owner frame.
   *
   * @param owner the Frame from which the dialog is displayed
   * @param modal  true for a modal dialog, false for one that allows
   *               others windows to be active at the same time
   */
  public PreviewDialog(JFreeReport report, Frame owner, boolean modal)
      throws ReportProcessingException
  {
    super(owner, modal);
    init (report);
  }

  /**
   * Creates a non-modal dialog without a title with the
   * specifed Dialog as its owner.
   *
   * @param owner the Dialog from which the dialog is displayed
   */
  public PreviewDialog(JFreeReport report, Dialog owner)
      throws ReportProcessingException
  {
    super(owner);
    init (report);
  }

  /**
   * Creates a modal or non-modal dialog without a title and
   * with the specified owner dialog.
   * <p>
   *
   * @param owner the Dialog from which the dialog is displayed
   * @param modal  true for a modal dialog, false for one that allows
   *               others windows to be active at the same time
   */
  public PreviewDialog(JFreeReport report, Dialog owner, boolean modal)
    throws ReportProcessingException
  {
    super(owner, modal);
    init (report);
  }

  private void init(JFreeReport report) throws ReportProcessingException
  {
    base = new PreviewProxyBase(report, this);
    registerCloseActions();
    setContentPane(base);
  }

  public Action createDefaultCloseAction()
  {
    return new DefaultCloseAction();
  }

  public void dispose()
  {
    base.dispose();
    super.dispose();
  }

  protected void registerCloseActions()
  {
    addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(WindowEvent e)
      {
        base.getCloseAction().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                                         "CloseFrame"));
      }
    }
    );
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

  public PreviewProxyBase getBase()
  {
    return base;
  }
}
