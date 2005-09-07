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
 * Java14PrintingPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Java14PrintingPlugin.java,v 1.10 2003/12/21 23:49:23 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 23.07.2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.java14print;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.attribute.PrintRequestAttributeSet;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.gui.print.PrintingPlugin;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.util.Log;

/**
 * A replacement to use the JDK 1.4 printing API. This class does
 * nothing special yet.
 * 
 * @author Thomas Morgner
 */
public class Java14PrintingPlugin extends PrintingPlugin
{
  /** A clear text failure description for the error message of the dialog. */
  private String failureDescription;

  /**
   * Default constructor.
   */
  public Java14PrintingPlugin()
  {
  }

  /**
   * Returns a description of the last error. 
   * @see org.jfree.report.modules.gui.base.ExportPlugin#getFailureDescription()
   * 
   * @return the failure description.
   */
  public String getFailureDescription()
  {
    return failureDescription;
  }

  /**
   * Sets the failure description.
   * @param failureDescription the new failure description.
   */
  private void setFailureDescription(String failureDescription)
  {
    this.failureDescription = failureDescription;
  }

  /**
   * Exports a report.
   *
   * @param report  the report.
   *
   * @return true, if the export was successfull, false otherwise.
   */
  public boolean performExport(JFreeReport report)
  {

    PrintService[] services = PrintServiceLookup.lookupPrintServices(
                               DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    if (services.length == 0)
    {
      setFailureDescription("Unable to find a matching print service implementation.");
      return false;
    }
    PrintRequestAttributeSet attributes =
            Java14PrintUtil.copyConfiguration(null, report);
    attributes = Java14PrintUtil.copyAuxillaryAttributes(attributes, report);

    PrintService service = ServiceUI.printDialog
        (null, 50, 50, services, services[0],
            DocFlavor.SERVICE_FORMATTED.PAGEABLE, attributes);
    if (service == null)
    {
      setFailureDescription("Print job cancelled.");
      return true;
    }
    else
    {
      // the user may have changed the PageFormat here ... we have to check
      // that and may have to reissue the print dialog

      final int compare = Java14PrintUtil.isValidConfiguration(attributes, report);
      if (compare != Java14PrintUtil.CONFIGURATION_VALID)
      {
        // todo: For full support, we should build or own Print-Dialog..
        // This would add better support for multiple pages and would
        // support the page-ranges a lot better than now.
        Log.warn ("Printing with JDK 1.4: We need to repaginage the report ... stay tuned.");
        ReportProgressDialog progressDialog = createProgressDialog();
        getBase().addRepaginationListener(progressDialog);
        Java14RepaginateAndPrintExportTask task =
                new Java14RepaginateAndPrintExportTask
            (progressDialog, service, getBase(), attributes);
        task.addExportTaskListener(new DefaultExportTaskListener());
        delegateTask(task);
        return handleExportResult(task);
      }
      else
      {
        Log.warn ("Printing with JDK 1.4: great, we can print without repagination.");
        ReportProgressDialog progressDialog = createProgressDialog();
        getBase().addRepaginationListener(progressDialog);
        Java14PrintExportTask task = new Java14PrintExportTask
            (progressDialog, service, getBase().getReportPane(), attributes);
        task.addExportTaskListener(new DefaultExportTaskListener());
        delegateTask(task);
        return handleExportResult(task);
      }
    }
  }


  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return true, if the plugin should be added to the toolbar, false otherwise.
   */
  public boolean isAddToToolbar()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
        ("org.jfree.report.ext.modules.java14print.AddToToolbar", "false").equals("true");
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
        ("org.jfree.report.ext.modules.java14print.Separated", "false").equals("true");
  }

}
