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
 * CSVExportPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVExportPlugin.java,v 1.6 2003/09/10 18:20:25 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13-Jun-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.csv;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.AbstractExportPlugin;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.PreviewProxy;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.gui.base.ExportTaskListener;
import org.jfree.report.modules.gui.csv.resources.CSVExportResources;
import org.jfree.ui.RefineryUtilities;

/**
 * Encapsulates the CSVExportDialog into a separate plugin.
 *
 * @author Thomas Morgner
 */
public class CSVExportPlugin extends AbstractExportPlugin
{
  private class CSVExportTaskListener implements ExportTaskListener
  {
    private ReportProgressDialog progressDialog;
    public CSVExportTaskListener(ReportProgressDialog progressDialog)
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

  /** The CSV export dialog. */
  private CSVExportDialog exportDialog;

  /** Localised resources. */
  private final ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      CSVExportResources.class.getName();

  /** The progress dialog that will monitor the export process. */ 

  /**
   * DefaultConstructor.
   */
  public CSVExportPlugin()
  {
    resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
  }

  protected ReportProgressDialog createProgressDialog ()
  {
    final ReportProgressDialog progressDialog = new ReportProgressDialog();
    progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setTitle(resources.getString("cvs-export.progressdialog.title"));
    progressDialog.setMessage(resources.getString("cvs-export.progressdialog.message"));
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
    return progressDialog;
  }
  /**
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into an
   * comma separated values file.
   *
   * @param report  the report being processed.
   *
   * @return true or false.
   */
  public boolean performExport(final JFreeReport report)
  {
    final boolean result = exportDialog.performQueryForExport(report);
    if (result == false)
    {
      // user canceled the dialog ...
      return handleExportResult(true);
    }

    ReportProgressDialog progressDialog = createProgressDialog();
    ExportTask task;
    if (exportDialog.isExportRawData())
    {
      task = new CSVRawExportTask
          (exportDialog.getFilename(), exportDialog.getEncoding(), report);
    }
    else
    {
      task = new CSVTableExportTask
          (exportDialog.getFilename(), exportDialog.getEncoding(), createProgressDialog(), report);
    }
    task.addExportTaskListener(new CSVExportTaskListener(progressDialog));
    delegateTask(task);
    return handleExportResult(task);
  }

  /**
   * Returns the display name for the CSV dialog.
   *
   * @return The name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.export-to-csv.name");
  }

  /**
   * Returns a short description for the CSV dialog.
   *
   * @return The description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.export-to-csv.description");
  }

  /**
   * Returns the small icon for the dialog.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    return (Icon) resources.getObject("action.export-to-csv.small-icon");
  }

  /**
   * Returns the large icon for the dialog.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    return (Icon) resources.getObject("action.export-to-csv.icon");
  }

  /**
   * Returns the accelerator key for the action associated with the dialog.
   *
   * @return The key stroke.
   */
  public KeyStroke getAcceleratorKey()
  {
    return (KeyStroke) resources.getObject("action.export-to-csv.accelerator");
  }

  /**
   * Returns the mnemonic key code for the action associated with the dialog.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey()
  {
    return (Integer) resources.getObject("action.export-to-csv.mnemonic");
  }

  /**
   * Initializes the plugin to work with the given PreviewProxy.
   *
   * @param proxy the preview proxy that is used to display the preview component.
   */
  public void init(final PreviewProxy proxy)
  {
    super.init(proxy);
    if (proxy instanceof Frame)
    {
      exportDialog = new CSVExportDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      exportDialog = new CSVExportDialog((Dialog) proxy);
    }
    else
    {
      exportDialog = new CSVExportDialog();
    }
    exportDialog.pack();
  }
}
