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
 * $Id: PrintExportTask.java,v 1.5 2003/09/09 21:31:48 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.print;

import java.awt.print.Pageable;
import java.awt.print.PrinterJob;

import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.util.Log;

/**
 * An export task implementation that prints a report using the AWT 
 * printing API.
 * 
 * @author Thomas Morgner
 */
public class PrintExportTask extends ExportTask
{
  /** The pageable that is used to create the printer content. */ 
  private Pageable pageable;
  /** The progress dialog that is used to monitor the export progress. */
  private ReportProgressDialog progressDialog;
  /** The desired printer job name. */
  private String jobname;

  /**
   * Creates a new print export task.
   * 
   * @param pageable the pageable that should be printed.
   * @param progressDialog the progress dialog that will monitor the progress.
   * @param jobname the desired jobname (or null, if undefined)
   */
  public PrintExportTask(final Pageable pageable, 
                         final ReportProgressDialog progressDialog,
                         final String jobname)
  {
    if (progressDialog == null)
    {
      throw new NullPointerException("Progress dialog is null.");
    }
    if (pageable == null)
    {
      throw new NullPointerException("Pageable is null.");
    }
    this.progressDialog = progressDialog;
    this.pageable = pageable;
    this.jobname = jobname;
    setTaskDone(false);
  }

  /**
   * Displays the print dialog and executes the printing in a spearate thread. 
   * This is a workaround for an java bug.
   */
  public void run()
  {
    final PrinterJob pj = PrinterJob.getPrinterJob();
    if (jobname != null)
    {
      pj.setJobName(jobname);
    }
    pj.setPageable(pageable);

    if (pj.printDialog())
    {
      try
      {
        pj.print();
        setReturnValue(RETURN_SUCCESS);
      }
      catch (Exception e)
      {
        setReturnValue(RETURN_FAILED);
        setException(e);
        Log.error ("Printing export failed", e);
      }
    }
    else
    {
      setReturnValue(RETURN_ABORT);
    }
    setTaskDone(true);
    progressDialog.setVisible(false);
  }
}

