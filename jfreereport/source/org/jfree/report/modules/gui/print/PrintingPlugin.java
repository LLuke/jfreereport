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
 * $Id: PrintingPlugin.java,v 1.19 2005/09/07 14:25:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.print;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.base.AbstractExportPlugin;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ResourceBundleSupport;

/**
 * An export plugin for the <code>java.awt.print</code> API.
 * <p/>
 *
 * @author Thomas Morgner
 */
public class PrintingPlugin extends AbstractExportPlugin
{
  /**
   * Localised resources.
   */
  private final ResourceBundleSupport resources;

  /**
   * The base resource class.
   */
  public static final String BASE_RESOURCE_CLASS =
          "org.jfree.report.modules.gui.print.resources.print-export-resources";
  public static final String PROGRESS_DIALOG_ENABLE_KEY =
          "org.jfree.report.modules.gui.print.ProgressDialogEnabled";

  /**
   * DefaultConstructor.
   */
  public PrintingPlugin ()
  {
    resources = new ResourceBundleSupport(BASE_RESOURCE_CLASS);
  }

  /**
   * Returns the resourcebundle used to translate strings.
   *
   * @return the resourcebundle.
   */
  protected ResourceBundleSupport getResources ()
  {
    return resources;
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
    progressDialog.setTitle(resources.getString("printing-export.progressdialog.title"));
    progressDialog.setMessage(resources.getString("printing-export.progressdialog.message"));
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
    return progressDialog;
  }

  /**
   * Exports a report.
   *
   * @param report the report.
   * @return true, if the export was successfull, false otherwise.
   */
  public boolean performExport (final JFreeReport report)
  {
    // need to connect to the report pane to receive state updates ...
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

    final String jobName = report.getReportConfiguration().getConfigProperty
            ("org.jfree.report.modules.gui.print.JobName", report.getName());
    final PrintExportTask task = new PrintExportTask
            (getBase().getReportPane(), progressDialog, jobName);
    task.addExportTaskListener(new DefaultExportTaskListener());
    delegateTask(task);
    return handleExportResult(task);
  }

  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName ()
  {
    return (resources.getString("action.print.name"));
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription ()
  {
    return (resources.getString("action.print.description"));
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon ()
  {
    return getSkin().getIcon("action.print.small-icon", true, false);
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon ()
  {
    return getSkin().getIcon("action.print.icon", true, true);
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey ()
  {
    return (resources.getKeyStroke("action.print.accelerator"));
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey ()
  {
    return (resources.getMnemonic("action.print.mnemonic"));
  }


  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return true, if the plugin should be added to the toolbar, false otherwise.
   */
  public boolean isAddToToolbar ()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            ("org.jfree.report.modules.gui.print.AddToToolbar", "false").equals("true");
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
            ("org.jfree.report.modules.gui.print.Separated", "false").equals("true");
  }

}
