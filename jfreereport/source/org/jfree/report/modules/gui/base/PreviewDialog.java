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
 * ------------------
 * PreviewDialog.java
 * ------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PreviewDialog.java,v 1.8 2003/10/08 19:32:27 taqua Exp $
 *
 * Changes (from 4-Dec-2002)
 * -------------------------
 * 04-Dec-2002 : Forked from PreviewFrame (TM);
 * 10-Dec-2002 : Updated Javadocs (DG);
 * 08-Oct-2003 : Removed event mapping from WindowClosing to CloseAction
 *
 */

package org.jfree.report.modules.gui.base;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.JDialog;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;

/**
 * A standard print preview dialog for any JFreeReport.  Allows the user to page back and forward
 * through the report, zoom in and out, and send the output to the printer.
 * <P>
 * You can also save the report in PDF format (thanks to the iText library).
 * <p>
 * When including this PreviewDialog in your own programs, you should override the provided
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
    }
  }

  /** A preview proxy. */
  private PreviewProxyBase base;

  /** Localised resources. */
  private ResourceBundle resources;

  /**
   * Creates a new preview dialog for a report.
   *
   * @param report  the report.
   *
   * @throws org.jfree.report.ReportProcessingException if there is a problem processing the report.
   */
  public PreviewDialog(final JFreeReport report)
      throws ReportProcessingException
  {
    init(report);
  }

  /**
   * Creates a new preview dialog for a report.
   *
   * @param report  the report.
   * @param owner  the owner frame.
   *
   * @throws org.jfree.report.ReportProcessingException if there is a problem processing the report.
   */
  public PreviewDialog(final JFreeReport report, final Frame owner)
      throws ReportProcessingException
  {
    super(owner);
    init(report);
  }

  /**
   * Creates a new preview dialog for a report.
   *
   * @param report  the report.
   * @param owner  the owner frame.
   * @param modal  modal or non-modal?
   *
   * @throws org.jfree.report.ReportProcessingException if there is a problem processing the report.
   */
  public PreviewDialog(final JFreeReport report, final Frame owner, final boolean modal)
      throws ReportProcessingException
  {
    super(owner, modal);
    init(report);
  }

  /**
   * Creates a new preview dialog for a report.
   *
   * @param report  the report.
   * @param owner  the owner dialog.
   *
   * @throws org.jfree.report.ReportProcessingException if there is a problem processing the report.
   */
  public PreviewDialog(final JFreeReport report, final Dialog owner)
      throws ReportProcessingException
  {
    super(owner);
    init(report);
  }

  /**
   * Creates a new preview dialog for a report.
   *
   * @param report  the report.
   * @param owner  the owner dialog.
   * @param modal  modal or non-modal?
   *
   * @throws org.jfree.report.ReportProcessingException if there is a problem processing the report.
   */
  public PreviewDialog(final JFreeReport report, final Dialog owner, final boolean modal)
      throws ReportProcessingException
  {
    super(owner, modal);
    init(report);
  }

  /**
   * Initialise.
   *
   * @param report  the report
   *
   * @throws org.jfree.report.ReportProcessingException if there is a problem processing the report.
   */
  private void init(final JFreeReport report) throws ReportProcessingException
  {
    base = new PreviewProxyBase(this);
    base.setReport(report);
    setContentPane(base);
  }

  /**
   * Creates the default close action.
   *
   * @return The action.
   */
  public Action createDefaultCloseAction()
  {
    return new DefaultCloseAction();
  }

  /**
   * Disposes the dialog.
   */
  public void dispose()
  {
    base.dispose();
    super.dispose();
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
   * Returns the preview proxy.
   *
   * @return The proxy.
   */
  public PreviewProxyBase getBase()
  {
    return base;
  }
}
