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
 * CSVRawExportTask.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVTableExportTask.java,v 1.3 2003/08/31 21:06:09 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.util.Log;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.table.csv.CSVTableProcessor;

public class CSVTableExportTask extends ExportTask
{
  private final ReportProgressDialog progressDialog;
  private final String fileName;
  private final String encoding;
  private final JFreeReport report;

  public CSVTableExportTask
      (final String fileName, final String encoding,
       final ReportProgressDialog dialog,
       final JFreeReport report)
  {
    this.fileName = fileName;
    this.progressDialog = dialog;
    this.report = report;
    this.encoding = encoding;
  }

  /**
   * Exports the repotr into a PDF file.
   */
  public void run()
  {
    Writer out = null;
    File file = new File (fileName);
    try
    {
      out = new BufferedWriter
          (new OutputStreamWriter(new FileOutputStream(file), encoding));
      final CSVTableProcessor target = new CSVTableProcessor(report);
      target.addRepaginationListener(progressDialog);
      target.setWriter(out);
      target.processReport();
      out.close();
      target.removeRepaginationListener(progressDialog);
      setReturnValue(RETURN_SUCCESS);
    }
    catch (ReportInterruptedException re)
    {
      setReturnValue(RETURN_ABORT);
      try
      {
        out.close();
        out = null;
        if (file.delete() == false)
        {
          Log.warn(new Log.SimpleMessage("Unable to delete incomplete export:", file));
        }
      }
      catch (SecurityException se)
      {
        // ignore me
      }
      catch (IOException ioe)
      {
        // ignore me...
      }
    }
    catch (Exception re)
    {
      setReturnValue(RETURN_FAILED);
      setException(re);
      Log.error ("Exporting failed .", re);
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
        Log.error ("Unable to close the output stream.", e);
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
