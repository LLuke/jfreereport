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
 * ExcelExportPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFExportPlugin.java,v 1.1 2005/03/04 13:17:22 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.rtf;

import java.awt.Dialog;
import java.awt.Frame;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.AbstractExportPlugin;
import org.jfree.report.modules.gui.base.PreviewProxy;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ResourceBundleSupport;

/**
 * Encapsulates the ExcelExportDialog into a separate plugin.
 *
 * @author Thomas Morgner
 */
public class RTFExportPlugin extends AbstractExportPlugin
{
  /**
   * The export dialog which handles the export.
   */
  private RTFExportDialog exportDialog;

  /**
   * Localised resources.
   */
  private final ResourceBundleSupport resources;

  /**
   * The base resource class.
   */
  public static final String BASE_RESOURCE_CLASS =
          "org.jfree.report.modules.gui.rtf.resources.rtf-export-resources";
  
  public static final String PROGRESS_DIALOG_ENABLE_KEY =
          "org.jfree.report.modules.gui.rtf.ProgressDialogEnabled";

  /**
   * DefaultConstructor.
   */
  public RTFExportPlugin ()
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
    progressDialog.setTitle(resources.getString("rtf-export.progressdialog.title"));
    progressDialog.setMessage(resources.getString("rtf-export.progressdialog.message"));
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
    final RTFExportDialog exportDialog = getExportDialog();
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

    final RTFExportTask task =
            new RTFExportTask(exportDialog.getFilename(), progressDialog, report);
    task.addExportTaskListener(new DefaultExportTaskListener());
    delegateTask(task);
    return handleExportResult(task);
  }

  /**
   * Returns a short description for the Excel dialog.
   *
   * @return The description.
   */
  public String getShortDescription ()
  {
    return resources.getString("action.export-to-rtf.description");
  }

  /**
   * Returns the small icon for the dialog.
   *
   * @return The icon.
   */
  public Icon getSmallIcon ()
  {
    return resources.getIcon("action.export-to-rtf.small-icon");
  }

  /**
   * Returns the large icon for the dialog.
   *
   * @return The icon.
   */
  public Icon getLargeIcon ()
  {
    return resources.getIcon("action.export-to-rtf.icon");
  }

  /**
   * Returns the accelerator key for the action associated with the dialog.
   *
   * @return The key stroke.
   */
  public KeyStroke getAcceleratorKey ()
  {
    return resources.getKeyStroke("action.export-to-rtf.accelerator");
  }

  /**
   * Returns the mnemonic key code for the action associated with the dialog.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey ()
  {
    return resources.getMnemonic("action.export-to-rtf.mnemonic");
  }

  /**
   * Returns the display name.
   *
   * @return The display name.
   */
  public String getDisplayName ()
  {
    return resources.getString("action.export-to-rtf.name");
  }


  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return true, if the plugin should be added to the toolbar, false otherwise.
   */
  public boolean isAddToToolbar ()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
            ("org.jfree.report.modules.gui.rtf.AddToToolbar", "false").equals("true");
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
    return ReportConfiguration.getGlobalConfig().getConfigProperty
            ("org.jfree.report.modules.gui.rtf.Separated", "false").equals("true");
  }

  /**
   * Returns the Export dialog used to query the required export parameters from the
   * user.
   *
   * @return the default export dialog.
   */
  protected RTFExportDialog getExportDialog ()
  {
    if (exportDialog == null)
    {
      final PreviewProxy proxy = super.getProxy();
      if (proxy instanceof Frame)
      {
        exportDialog = new RTFExportDialog((Frame) proxy);
      }
      else if (proxy instanceof Dialog)
      {
        exportDialog = new RTFExportDialog((Dialog) proxy);
      }
      else
      {
        exportDialog = new RTFExportDialog();
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
