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
 * $Id: HtmlStreamExportTask.java,v 1.7 2003/11/07 18:33:53 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.html;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.table.html.HtmlProcessor;
import org.jfree.report.modules.output.table.html.StreamHtmlFilesystem;
import org.jfree.report.util.Log;

/**
 * An export task implementation that exports the report into a single HTML
 * file.
 * 
 * @author Thomas Morgner
 */
public class HtmlStreamExportTask extends ExportTask
{
  /** The progress dialog that monitors the export process. */
  private final ReportProgressDialog progressDialog;
  /** The name of the target file. */
  private final String fileName;
  /** The report that should be exported. */
  private final JFreeReport report;

  /**
   * Creates a new html export task.
   * 
   * @param fileName the name of the target file.
   * @param dialog the progress monitor component.
   * @param report the report that should be exported.
   */
  public HtmlStreamExportTask(final String fileName,
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
    this.fileName = fileName;
    this.progressDialog = dialog;
    this.report = report;
  }

  /**
   * Exports the report into a Html Directory Structure.
   */
  protected void performExport()
  {
    OutputStream out = null;
    final File file = new File(fileName);
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(file));
      final HtmlProcessor target = new HtmlProcessor(report);
      progressDialog.setModal(false);
      progressDialog.setVisible(true);
      target.addRepaginationListener(progressDialog);
      // as this is a local report generation (no servlets involved)
      // we can safely reference local files. It is up to the user to
      // define the report properly to not scatter the image files over the
      // whole local filesystem.
      target.setFilesystem
          (new StreamHtmlFilesystem(out, true, file.getParentFile().toURL()));
      target.processReport();
      out.close();
      out = null;
      target.removeRepaginationListener(progressDialog);
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
      Log.error ("Exporting failed .", re);
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
        Log.error ("Unable to close the output stream.", e);
        setTaskFailed(e);
      }
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
