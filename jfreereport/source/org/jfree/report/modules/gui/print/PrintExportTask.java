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
 * PrintExportTask.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PrintExportTask.java,v 1.2 2003/08/25 14:29:30 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.print;

import java.awt.print.PrinterJob;

import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.ReportInterruptedException;

public class PrintExportTask extends ExportTask
{
  private final PrinterJob printerJob;
  private final ReportProgressDialog progressDialog;

  public PrintExportTask(final PrinterJob printJob, final ReportProgressDialog progressDialog)
  {
    this.printerJob = printJob;
    this.progressDialog = progressDialog;
    setTaskDone(false);
  }

  /**
   * Executes the printing in a spearate thread ...
   */
  public void run()
  {
    try
    {
      printerJob.print();
      setReturnValue(RETURN_SUCCESS);
    }
    catch (Exception e)
    {
      setReturnValue(RETURN_FAILED);
      setException(e);
    }
    setTaskDone(true);
    progressDialog.setVisible(false);
  }
}

