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
 * ExcelExportTask.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExcelExportTask.java,v 1.1 2003/08/24 15:08:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.xls;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.table.xls.ExcelProcessor;

public class ExcelExportTask extends ExportTask
{
  private final ReportProgressDialog progressDialog;
  private final String fileName;
  private final JFreeReport report;

  public ExcelExportTask
      (final String fileName, final ReportProgressDialog dialog,
       final JFreeReport report)
  {
    this.fileName = fileName;
    this.progressDialog = dialog;
    this.report = report;
  }

  /**
   * Exports the repotr into a PDF file.
   */
  public void run()
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
      final ExcelProcessor target = new ExcelProcessor(report);
      target.addRepaginationListener(progressDialog);
      target.setOutputStream(out);
      target.processReport();
      out.close();
      target.removeRepaginationListener(progressDialog);
      setReturnValue(RETURN_SUCCESS);
    }
    catch (Exception re)
    {
      setReturnValue(RETURN_FAILED);
      setException(re);
    }
    finally
    {
      try
      {
        if (out != null)
        {
          out.close();
        }
      }
      catch (Exception e)
      {
        setReturnValue(RETURN_FAILED);
        // if there is already another error, this exception is
        // just a minor obstactle. Something big crashed before ...
        if (getException() == null)
        {
          setException(e);
        }
      }
    }
    setTaskDone(true);
    progressDialog.setVisible(false);
  }
}
