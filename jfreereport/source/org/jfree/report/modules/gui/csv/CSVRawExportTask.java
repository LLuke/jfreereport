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
 * $Id: CSVRawExportTask.java,v 1.5 2003/09/08 18:39:33 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24-Aug-2003 : Initial version
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
import org.jfree.report.modules.output.csv.CSVProcessor;

/**
 * An export task implementation that writes an report into a CSV file,
 * and uses the raw target to create layouted content.
 * 
 * @author Thomas Morgner
 */
public class CSVRawExportTask extends ExportTask
{
  /** The progress dialog that monitors the export process. */
  private final ReportProgressDialog progressDialog;
  /** The name of the output file. */
  private final String fileName;
  /** The encoding to be used for the file. */
  private final String encoding;
  /** The report that should be exported. */
  private final JFreeReport report;

  /**
   * Creates a new CSV export task.
   * 
   * @param fileName the filename of the target file
   * @param encoding the encoding for the generated output 
   * @param dialog the progress monitor
   * @param report the report that should be exported.
   */
  public CSVRawExportTask
      (final String fileName, final String encoding,
       final ReportProgressDialog dialog,
       final JFreeReport report)
  {
    if (fileName == null)
    {
      throw new NullPointerException("File name is null.");
    }
    if (dialog == null)
    {
      throw new NullPointerException("Progress dialog is null.");
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
  public void run()
  {
    Writer out = null;
    File file = new File (fileName);
    try
    {

      out = new BufferedWriter(
          new OutputStreamWriter(
              new FileOutputStream(file), encoding));

      final CSVProcessor target = new CSVProcessor(report);
      target.setWriter(out);
      target.processReport();
      out.close();
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
      setException(re);
      setReturnValue(RETURN_FAILED);
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
      }
    }
    setTaskDone(true);
    progressDialog.setVisible(false);
  }
}
