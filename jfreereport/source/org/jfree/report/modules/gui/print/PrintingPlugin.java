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
 * ------------------------------
 * PrintingPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PrintingPlugin.java,v 1.5 2003/09/10 18:20:25 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.print;

import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.AbstractExportPlugin;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.gui.base.ExportTaskListener;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.print.resources.PrintExportResources;
import org.jfree.ui.RefineryUtilities;

/**
 * An export plugin for the <code>java.awt.print</code> API.
 * <p>
 * @author Thomas Morgner
 */
public class PrintingPlugin extends AbstractExportPlugin
{
  protected class PrintTaskListener implements ExportTaskListener
  {
    ReportProgressDialog progressDialog;

    public PrintTaskListener(ReportProgressDialog progressDialog)
    {
      this.progressDialog = progressDialog;
    }

    public void taskAborted(ExportTask task)
    {
      getBase().removeRepaginationListener(progressDialog);
      handleExportResult(task);
    }

    public void taskDone(ExportTask task)
    {
      getBase().removeRepaginationListener(progressDialog);
      handleExportResult(task);
    }

    public void taskFailed(ExportTask task)
    {
      getBase().removeRepaginationListener(progressDialog);
      handleExportResult(task);
    }

    public void taskWaiting(ExportTask task)
    {
    }
  }
  /** Localised resources. */
  private final ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      PrintExportResources.class.getName();

  /**
   * DefaultConstructor.
   */
  public PrintingPlugin()
  {
    resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
  }

  protected ResourceBundle getResources ()
  {
    return resources;
  }
  
  protected ReportProgressDialog createProgressDialog ()
  {
    ReportProgressDialog progressDialog = new ReportProgressDialog();
    progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setTitle(resources.getString("printing-export.progressdialog.title"));
    progressDialog.setMessage(resources.getString("printing-export.progressdialog.message"));
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
    return progressDialog;
  }

  /**
   * Exports a report.
   *
   * @param report  the report.
   *
   * @return A boolean.
   */
  public boolean performExport(final JFreeReport report)
  {
    // need to connect to the report pane to receive state updates ...
    ReportProgressDialog progressDialog = createProgressDialog();
    getBase().addRepaginationListener(progressDialog);
    PrintExportTask task = new PrintExportTask
        (getBase().getPageable(), progressDialog, 
         report.getReportConfiguration().getConfigProperty
            ("org.jfree.report.modules.gui.print.JobName"));
    task.addExportTaskListener(new PrintTaskListener(progressDialog));
    delegateTask(task);
    return handleExportResult(task);
  }

  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName()
  {
    return (resources.getString("action.print.name"));
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription()
  {
    return (resources.getString("action.print.description"));
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    return (Icon) (resources.getObject("action.print.small-icon"));
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    return (Icon) (resources.getObject("action.print.icon"));
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return (KeyStroke) (resources.getObject("action.print.accelerator"));
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey()
  {
    return (Integer) (resources.getObject("action.print.mnemonic"));
  }

  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return A boolean.
   */
  public boolean isAddToToolbar()
  {
    return true;
  }
}
