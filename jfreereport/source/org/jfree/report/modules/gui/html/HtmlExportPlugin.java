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
 * HtmlExportPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlExportPlugin.java,v 1.4 2003/08/25 14:29:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.html;

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
import org.jfree.report.modules.gui.html.resources.HtmlExportResources;
import org.jfree.ui.RefineryUtilities;

/**
 * Encapsulates the HtmlExportDialog into a separate plugin.
 *
 * @author Thomas Morgner
 */
public class HtmlExportPlugin extends AbstractExportPlugin
{
  /** The html export dialog which handles the export. */
  private HtmlExportDialog exportDialog;

  /** Localised resources. */
  private final ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      HtmlExportResources.class.getName();

  private final ReportProgressDialog progressDialog;

  /**
   * DefaultConstructor.
   */
  public HtmlExportPlugin()
  {
    resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    progressDialog = new ReportProgressDialog();
    progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setTitle(resources.getString("html-export.progressdialog.title"));
    progressDialog.setMessage(resources.getString("html-export.progressdialog.message"));
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
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
      exportDialog = new HtmlExportDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      exportDialog = new HtmlExportDialog((Dialog) proxy);
    }
    else
    {
      exportDialog = new HtmlExportDialog();
    }
    exportDialog.pack();
  }

  /**
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into an
   * Excel file.
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

    ExportTask task;
    switch (exportDialog.getSelectedExportMethod())
    {
      case HtmlExportDialog.EXPORT_DIR:
        {
          task = new HtmlDirExportTask
              (exportDialog.getDirFilename(), exportDialog.getDirDataFilename(),
                  progressDialog, report);
          break;
        }
      case HtmlExportDialog.EXPORT_STREAM:
        {
          task = new HtmlStreamExportTask
              (exportDialog.getStreamFilename(), progressDialog, report);
          break;
        }
      case HtmlExportDialog.EXPORT_ZIP:
        {
          task = new HtmlZipExportTask
              (exportDialog.getZipFilename(), exportDialog.getZipDataFilename(),
                  progressDialog, report);
          break;
        }
      default:
        throw new IllegalArgumentException("Unexpected export method found.");
    }
    delegateTask(task);
    synchronized (task)
    {
      if (task.isTaskDone() == false)
      {
        progressDialog.setVisible(true);
      }
    }
    return handleExportResult(task);
  }

  /**
   * Returns the action display name.
   *
   * @return The display name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.export-to-html.name");
  }

  /**
   * Returns the short description for the action.
   *
   * @return The short description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.export-to-html.description");
  }

  /**
   * Returns the small icon for the action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    return (Icon) resources.getObject("action.export-to-html.small-icon");
  }

  /**
   * Returns the large icon for the action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    return (Icon) resources.getObject("action.export-to-html.icon");
  }

  /**
   * Returns the accelerator key for the action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return (KeyStroke) resources.getObject("action.export-to-html.accelerator");
  }

  /**
   * Returns the mnemonic key code for the action.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey()
  {
    return (Integer) resources.getObject("action.export-to-html.mnemonic");
  }
}
