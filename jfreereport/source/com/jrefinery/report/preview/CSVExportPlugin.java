/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: CSVExportPlugin.java,v 1.1 2003/06/13 22:54:00 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 13-Jun-2003 : Initial version
 *  
 */

package com.jrefinery.report.preview;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import com.jrefinery.report.JFreeReport;

public class CSVExportPlugin extends AbstractExportPlugin
{
  private CSVExportDialog exportDialog;

  /** Localised resources. */
  private ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";


  public CSVExportPlugin()
  {
    resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
  }

  /**
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into an
   * Excel file.
   *
   * @param report  the report being processed.
   *
   * @return true or false.
   */
  public boolean performExport(JFreeReport report)
  {
    return exportDialog.performExport(report);
  }

  /**
   * Returns the display name for the CSV dialog.
   *
   * @return The name.
   */
  public String getDisplayName()
  {
    return resources.getString ("action.export-to-csv.name");
  }

  /**
   * Returns a short description for the CSV dialog.
   *
   * @return The description.
   */
  public String getShortDescription()
  {
    return resources.getString ("action.export-to-csv.description");
  }

  /**
   * Returns the small icon for the dialog.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    return (Icon) resources.getObject ("action.export-to-csv.small-icon");
  }

  /**
   * Returns the large icon for the dialog.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    return (Icon) resources.getObject ("action.export-to-csv.icon");
  }

  /**
   * Returns the accelerator key for the action associated with the dialog.
   *
   * @return The key stroke.
   */
  public KeyStroke getAcceleratorKey()
  {
    return (KeyStroke) resources.getObject ("action.export-to-csv.accelerator");
  }

  /**
   * Returns the mnemonic key code for the action associated with the dialog.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey()
  {
    return (Integer) resources.getObject ("action.export-to-csv.mnemonic");
  }

  /**
   * Initializes the plugin to work with the given PreviewProxy.
   *
   * @param proxy
   */
  public void init(PreviewProxy proxy)
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
