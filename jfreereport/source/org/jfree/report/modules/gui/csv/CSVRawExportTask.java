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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 24.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.csv;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;

import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.csv.CSVProcessor;
import org.jfree.report.JFreeReport;

public class CSVRawExportTask extends ExportTask
{
  private ReportProgressDialog progressDialog;
  private String fileName;
  private String encoding;
  private JFreeReport report;

  public CSVRawExportTask
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
    try
    {

      out = new BufferedWriter(
          new OutputStreamWriter(
              new FileOutputStream(
                  new File(fileName)), encoding));

      final CSVProcessor target = new CSVProcessor(report);
      target.setWriter(out);
      target.processReport();
      out.close();
      setReturnValue(RETURN_SUCCESS);
    }
    catch (Exception re)
    {
      setException(re);
      setReturnValue(RETURN_FAILED);
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
      }
    }
    setTaskDone(true);
    progressDialog.setVisible(false);
  }
}
