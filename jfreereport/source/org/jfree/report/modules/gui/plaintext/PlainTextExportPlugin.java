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
 * PlainTextExportPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextExportPlugin.java,v 1.12.4.2 2004/11/21 16:39:25 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.plaintext;

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
 * Encapsulates the PlainTextExportDialog into a separate plugin.
 *
 * @author Thomas Morgner
 */
public class PlainTextExportPlugin extends AbstractExportPlugin
{
  /** The plain text export dialog. */
  private PlainTextExportDialog exportDialog;

  /** Localised resources. */
  private final ResourceBundleSupport resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
          "org.jfree.report.modules.gui.plaintext.resources/plaintext-export-resources";

  public static final String PROGRESS_DIALOG_ENABLE_KEY =
      "org.jfree.report.modules.gui.plaintext.ProgressDialogEnabled";

  /**
   * DefaultConstructor.
   */
  public PlainTextExportPlugin()
  {
    resources = new ResourceBundleSupport(BASE_RESOURCE_CLASS);
  }

  /** 
   * Creates the progress dialog that monitors the export process. 
   *
   * @return the progress monitor dialog. 
   */
  protected ReportProgressDialog createProgressDialog()
  {
    final ReportProgressDialog progressDialog = super.createProgressDialog();
    progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setTitle(resources.getString("plaintext-export.progressdialog.title"));
    progressDialog.setMessage(resources.getString("plaintext-export.progressdialog.message"));
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
    return progressDialog;
  }

  /**
   * Initializes the plugin to work with the given PreviewProxy.
   *
   * @param proxy the preview proxy that created this plugin.
   */
  public void init(final PreviewProxy proxy)
  {
    super.init(proxy);
    if (proxy instanceof Frame)
    {
      exportDialog = new PlainTextExportDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      exportDialog = new PlainTextExportDialog((Dialog) proxy);
    }
    else
    {
      exportDialog = new PlainTextExportDialog();
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

    final PlainTextExportTask task = new PlainTextExportTask
        (exportDialog.getFilename(), progressDialog,
            exportDialog.getSelectedPrinter(), report,
            exportDialog.getSelectedPrinterModel());
    task.addExportTaskListener(new DefaultExportTaskListener ());
    delegateTask(task);
    return handleExportResult(task);
  }

  /**
   * Returns the display name for the action.
   *
   * @return The display name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.export-to-plaintext.name");
  }

  /**
   * Returns the short description for the action.
   *
   * @return The short description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.export-to-plaintext.description");
  }

  /**
   * Returns the small icon for the action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    return resources.getIcon("action.export-to-plaintext.small-icon");
  }

  /**
   * Returns the large icon for an action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    return resources.getIcon("action.export-to-plaintext.icon");
  }

  /**
   * Returns the accelerator key.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return resources.getKeyStroke("action.export-to-plaintext.accelerator");
  }

  /**
   * Returns the mnemonic key.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey()
  {
    return resources.getMnemonic("action.export-to-plaintext.mnemonic");
  }


  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return true, if the plugin should be added to the toolbar, false otherwise.
   */
  public boolean isAddToToolbar()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
        ("org.jfree.report.modules.gui.plaintext.AddToToolbar", "false").equals("true");
  }

  /**
   * Returns true if the action is separated, and false otherwise. A separated
   * action starts a new action group and will be spearated from previous actions
   * on the menu and toolbar.
   *
   * @return true, if the action should be separated from previous actions,
   * false otherwise.
   */
  public boolean isSeparated()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
        ("org.jfree.report.modules.gui.plaintext.Separated", "false").equals("true");
  }

  /**
   * Returns the dialog used to query the export parameters from the user.
   * @return the export dialog.
   */
  protected PlainTextExportDialog getExportDialog()
  {
    return exportDialog;
  }

  /**
   * Returns the resourcebundle to be used to translate strings into
   * localized content.
   *
   * @return the resourcebundle for the localisation.
   */
  protected ResourceBundleSupport getResources()
  {
    return resources;
  }
}
