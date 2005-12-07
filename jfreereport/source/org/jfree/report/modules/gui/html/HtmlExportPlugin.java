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
 * $Id: HtmlExportPlugin.java,v 1.19 2005/09/07 14:25:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.html;

import java.awt.Dialog;
import java.awt.Frame;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.base.AbstractExportPlugin;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.PreviewProxy;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ResourceBundleSupport;

/**
 * Encapsulates the HtmlExportDialog into a separate plugin.
 *
 * @author Thomas Morgner
 */
public class HtmlExportPlugin extends AbstractExportPlugin
{
  /**
   * The html export dialog which handles the export.
   */
  private HtmlExportDialog exportDialog;

  /**
   * Localised resources.
   */
  private final ResourceBundleSupport resources;

  /**
   * The base resource class.
   */
  public static final String BASE_RESOURCE_CLASS =
          "org.jfree.report.modules.gui.html.resources.html-export-resources";
  public static final String PROGRESS_DIALOG_ENABLE_KEY =
          "org.jfree.report.modules.gui.html.ProgressDialogEnabled";

  /**
   * DefaultConstructor.
   */
  public HtmlExportPlugin ()
  {
    resources = new ResourceBundleSupport(BASE_RESOURCE_CLASS);
  }

  /**
   * Creates the progress dialog that monitors the export process.
   *
   * @return the progress monitor dialog.
   */
  protected ReportProgressDialog createProgressDialog ()
  {
    final ReportProgressDialog progressDialog = super.createProgressDialog();
    progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setTitle(resources.getString("html-export.progressdialog.title"));
    progressDialog.setMessage(resources.getString("html-export.progressdialog.message"));
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
    return progressDialog;
  }

  /**
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into an
   * Excel file.
   *
   * @param report the report being processed.
   * @return true or false.
   */
  public boolean performExport (final JFreeReport report)
  {
    final HtmlExportDialog exportDialog = getExportDialog();
    final boolean result = exportDialog.performQueryForExport(report);
    if (result == false)
    {
      // user canceled the dialog ...
      return handleExportResult(true);
    }

    final ReportProgressDialog progressDialog;
    if (report.getReportConfiguration().getConfigProperty
            (PROGRESS_DIALOG_ENABLE_KEY,
                    "false").equals("true"))
    {
      progressDialog = createProgressDialog();
    }
    else
    {
      progressDialog = null;
    }

    final ExportTask task;
    switch (exportDialog.getSelectedExportMethod())
    {
      case HtmlExportDialog.EXPORT_DIR:
        {
          task = new HtmlDirExportTask
                  (exportDialog.getFilename(), exportDialog.getDataFilename(),
                          progressDialog, report);
          break;
        }
      case HtmlExportDialog.EXPORT_STREAM:
        {
          task = new HtmlStreamExportTask
                  (exportDialog.getFilename(), progressDialog, report);
          break;
        }
      case HtmlExportDialog.EXPORT_ZIP:
        {
          task = new HtmlZipExportTask
                  (exportDialog.getFilename(), exportDialog.getDataFilename(),
                          progressDialog, report);
          break;
        }
      default:
        throw new IllegalArgumentException("Unexpected export method found.");
    }
    task.addExportTaskListener(new DefaultExportTaskListener());
    delegateTask(task);
    return handleExportResult(task);
  }

  /**
   * Returns the action display name.
   *
   * @return The display name.
   */
  public String getDisplayName ()
  {
    return resources.getString("action.export-to-html.name");
  }

  /**
   * Returns the short description for the action.
   *
   * @return The short description.
   */
  public String getShortDescription ()
  {
    return resources.getString("action.export-to-html.description");
  }

  /**
   * Returns the small icon for the action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon ()
  {
    return getSkin().getIcon("action.export-to-html.small-icon", true, false);
  }

  /**
   * Returns the large icon for the action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon ()
  {
    return getSkin().getIcon("action.export-to-html.icon", true, true);
  }

  /**
   * Returns the accelerator key for the action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey ()
  {
    return resources.getKeyStroke("action.export-to-html.accelerator");
  }

  /**
   * Returns the mnemonic key code for the action.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey ()
  {
    return resources.getMnemonic("action.export-to-html.mnemonic");
  }


  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return true, if the plugin should be added to the toolbar, false otherwise.
   */
  public boolean isAddToToolbar ()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            ("org.jfree.report.modules.gui.html.AddToToolbar", "false").equals("true");
  }

  /**
   * Returns true if the action is separated, and false otherwise. A separated action
   * starts a new action group and will be spearated from previous actions on the menu and
   * toolbar.
   *
   * @return true, if the action should be separated from previous actions, false
   *         otherwise.
   */
  public boolean isSeparated ()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            ("org.jfree.report.modules.gui.html.Separated", "false").equals("true");
  }

  /**
   * Returns the dialog used to query the export parameters from the user.
   *
   * @return the export dialog.
   */
  protected HtmlExportDialog getExportDialog ()
  {
    if (exportDialog == null)
    {
      final PreviewProxy proxy = super.getProxy();
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
    return exportDialog;
  }

  /**
   * Returns the resourcebundle to be used to translate strings into localized content.
   *
   * @return the resourcebundle for the localisation.
   */
  protected ResourceBundleSupport getResources ()
  {
    return resources;
  }
}
