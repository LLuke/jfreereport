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
 * HtmlDirExportTask.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlDirExportTask.java,v 1.3 2003/08/31 21:06:09 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.html;

import java.io.File;
import java.io.IOException;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.util.Log;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.table.html.DirectoryHtmlFilesystem;
import org.jfree.report.modules.output.table.html.HtmlProcessor;

public class HtmlDirExportTask extends ExportTask
{
  private final ReportProgressDialog progressDialog;
  private final String fileName;
  private final String dataDirectory;
  private final JFreeReport report;

  public HtmlDirExportTask(final String fileName, final String dataDirectory,
                           final ReportProgressDialog dialog, final JFreeReport report)
  {
    this.fileName = fileName;
    this.progressDialog = dialog;
    this.dataDirectory = dataDirectory;
    this.report = report;
  }

  /**
   * Exports the report into a Html Directory Structure.
   */
  public void run()
  {
    try
    {
      final File targetFile = new File(fileName);
      File targetDataFile = new File(dataDirectory);
      if (targetDataFile.isAbsolute() == false)
      {
        targetDataFile = new File(targetFile.getParentFile(), targetDataFile.getPath());
      }
      if (targetDataFile.mkdirs() == false)
      {
        if ((targetDataFile.exists() && targetDataFile.isDirectory()) == false)
        {
          throw new IOException("Unable to create the mssing directories.");
        }
      }
      final DirectoryHtmlFilesystem fs = new DirectoryHtmlFilesystem(targetFile, targetDataFile);
      final HtmlProcessor target = new HtmlProcessor(report);
      target.addRepaginationListener(progressDialog);
      target.setFilesystem(fs);
      target.processReport();
      target.removeRepaginationListener(progressDialog);
      setReturnValue(RETURN_SUCCESS);
    }
    catch (ReportInterruptedException re)
    {
      setReturnValue(RETURN_ABORT);
      Log.warn(new Log.SimpleMessage
          ("Unable to delete incomplete export: File ", fileName, " DataDir: ", dataDirectory));
    }
    catch (Exception re)
    {
      setReturnValue(RETURN_FAILED);
      setException(re);
      Log.error ("Exporting failed .", re);
    }
    setTaskDone(true);
    progressDialog.setVisible(false);
  }
}
