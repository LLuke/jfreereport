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
 * $Id: HtmlDirExportTask.java,v 1.6 2003/10/18 19:22:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.html;

import java.io.File;
import java.io.IOException;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.table.html.DirectoryHtmlFilesystem;
import org.jfree.report.modules.output.table.html.HtmlProcessor;
import org.jfree.report.util.Log;

/**
 * An export task implementation that exports the report into a HTML
 * directory structure.
 * 
 * @author Thomas Morgner
 */
public class HtmlDirExportTask extends ExportTask
{
  /** The progress dialog that monitors the export process. */
  private final ReportProgressDialog progressDialog;
  /** The name of the target file. */
  private final String fileName;
  /** The name of the data directory (relative to the target file). */
  private final String dataDirectory;
  /** The report that should be exported. */
  private final JFreeReport report;

  /**
   * Creates a new html export task.
   * 
   * @param fileName the name of the target file.
   * @param dataDirectory the name of the data directory (relative to the target file).
   * @param dialog the progress monitor component.
   * @param report the report that should be exported.
   */
  public HtmlDirExportTask(final String fileName, final String dataDirectory,
                           final ReportProgressDialog dialog, final JFreeReport report)
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
    if (dataDirectory == null)
    {
      throw new NullPointerException("DataDirectory is null.");
    }
    this.fileName = fileName;
    this.progressDialog = dialog;
    this.dataDirectory = dataDirectory;
    this.report = report;
  }

  /**
   * Exports the report into a Html Directory Structure.
   */
  protected void performExport()
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
      progressDialog.setModal(false);
      progressDialog.setVisible(true);
      target.addRepaginationListener(progressDialog);
      target.setFilesystem(fs);
      target.processReport();
      target.removeRepaginationListener(progressDialog);
      setTaskDone();
    }
    catch (ReportInterruptedException re)
    {
      setTaskAborted();
      Log.warn(new Log.SimpleMessage
          ("Unable to delete incomplete export: File ", fileName, " DataDir: ", dataDirectory));
    }
    catch (Exception re)
    {
      Log.error ("Exporting failed .", re);
      setTaskFailed(re);
    }
    progressDialog.setVisible(false);
  }

  /**
   * Remove all listeners and prepare the finalization.
   */
  protected void dispose()
  {
    super.dispose();
    progressDialog.dispose();
  }
}
