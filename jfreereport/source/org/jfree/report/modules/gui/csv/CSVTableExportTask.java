/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CSVTableExportTask.java,v 1.12 2005/03/24 22:24:54 taqua Exp $
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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.table.csv.CSVTableProcessor;
import org.jfree.report.util.Log;

/**
 * An export task implementation that writes an report into a CSV file, and uses the table
 * target to create layouted content.
 *
 * @author Thomas Morgner
 */
public class CSVTableExportTask extends ExportTask
{
  /**
   * The progress dialog that monitors the export process.
   */
  private final ReportProgressDialog progressDialog;
  /**
   * The name of the output file.
   */
  private final String fileName;
  /**
   * The encoding to be used for the file.
   */
  private final String encoding;
  /**
   * The report that should be exported.
   */
  private final JFreeReport report;

  /**
   * Creates a new CSV export task.
   *
   * @param fileName the filename of the target file
   * @param encoding the encoding for the generated output
   * @param dialog   the progress monitor
   * @param report   the report that should be exported.
   */
  public CSVTableExportTask
          (final String fileName, final String encoding,
           final ReportProgressDialog dialog,
           final JFreeReport report)
  {
    if (fileName == null)
    {
      throw new NullPointerException("File name is null.");
    }
    if (report == null)
    {
      throw new NullPointerException("Report is null.");
    }
    if (encoding == null)
    {
      throw new NullPointerException("Encoding is null.");
    }
    this.fileName = fileName;
    this.progressDialog = dialog;
    this.report = report;
    this.encoding = encoding;
  }

  /**
   * Exports the report into a CSV file.
   */
  protected void performExport ()
  {
    Writer out = null;
    final File file = new File(fileName);
    try
    {
      final File directory = file.getAbsoluteFile().getParentFile();
      if (directory != null)
      {
        if (directory.exists() == false)
        {
          if (directory.mkdirs() == false)
          {
            Log.warn("Can't create directories. Hoping and praying now..");
          }
        }
      }
      out = new BufferedWriter
              (new OutputStreamWriter(new FileOutputStream(file), encoding));
      final CSVTableProcessor target = new CSVTableProcessor(report);
      if (progressDialog != null)
      {
        progressDialog.setModal(false);
        progressDialog.setVisible(true);
        target.addRepaginationListener(progressDialog);
      }
      target.setWriter(out);
      target.processReport();
      out.close();
      out = null;
      if (progressDialog != null)
      {
        target.removeRepaginationListener(progressDialog);
      }
      setTaskDone();
    }
    catch (ReportInterruptedException re)
    {
      setTaskAborted();
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
      Log.error("Exporting failed .", re);
      setTaskFailed(re);
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
        Log.error("Unable to close the output stream.", e);
        // if there is already another error, this exception is
        // just a minor obstactle. Something big crashed before ...
      }
    }
    if (progressDialog != null)
    {
      progressDialog.setVisible(false);
    }
  }

  /**
   * Remove all listeners and prepare the finalization.
   */
  protected void dispose ()
  {
    super.dispose();
    if (progressDialog != null)
    {
      progressDialog.dispose();
    }
  }
}
