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
 * $Id: PlainTextExportPlugin.java,v 1.1 2003/07/07 22:44:06 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.plaintext;

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
import org.jfree.report.modules.gui.plaintext.resources.PlainTextExportResources;
import org.jfree.ui.RefineryUtilities;

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
  private ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      PlainTextExportResources.class.getName();

  private ReportProgressDialog progressDialog;

  /**
   * DefaultConstructor.
   */
  public PlainTextExportPlugin()
  {
    resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    progressDialog = new ReportProgressDialog();
    progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setTitle(resources.getString("plaintext-export.progressdialog.title"));
    progressDialog.setMessage(resources.getString("plaintext-export.progressdialog.message"));
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
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
    boolean result = exportDialog.performQueryForExport(report);
    if (result == false)
    {
      // user canceled the dialog ...
      return handleExportResult(true);
    }

    PlainTextExportTask task = new PlainTextExportTask
        (exportDialog.getFilename(), progressDialog,
            exportDialog.getSelectedPrinter(), report);
    delegateTask(task);
    synchronized (task)
    {
      if (task.isTaskDone() == false)
      {
        progressDialog.setVisible(true);
      }
    }
    if (task.getReturnValue() != ExportTask.RETURN_SUCCESS)
    {
      return handleExportResult(false);
    }
    return handleExportResult(true);
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
    return (Icon) resources.getObject("action.export-to-plaintext.small-icon");
  }

  /**
   * Returns the large icon for an action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    return (Icon) resources.getObject("action.export-to-plaintext.icon");
  }

  /**
   * Returns the accelerator key.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return (KeyStroke) resources.getObject("action.export-to-plaintext.accelerator");
  }

  /**
   * Returns the mnemonic key.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey()
  {
    return (Integer) resources.getObject("action.export-to-plaintext.mnemonic");
  }
}
