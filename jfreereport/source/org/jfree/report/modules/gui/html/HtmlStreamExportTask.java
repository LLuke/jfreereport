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
 * $Id: HtmlStreamExportTask.java,v 1.2 2003/08/25 14:29:29 taqua Exp $
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
import java.io.OutputStream;
import java.io.IOException;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.util.Log;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.table.html.HtmlProcessor;
import org.jfree.report.modules.output.table.html.StreamHtmlFilesystem;

public class HtmlStreamExportTask extends ExportTask
{
  private final ReportProgressDialog progressDialog;
  private final String fileName;
  private final JFreeReport report;

  public HtmlStreamExportTask(final String fileName,
                              final ReportProgressDialog dialog, final JFreeReport report)
  {
    this.fileName = fileName;
    this.progressDialog = dialog;
    this.report = report;
  }

  /**
   * Exports the report into a Html Directory Structure.
   */
  public void run()
  {
    OutputStream out = null;
    File file = new File(fileName);
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(file));
      final HtmlProcessor target = new HtmlProcessor(report);
      target.addRepaginationListener(progressDialog);
      target.setFilesystem(new StreamHtmlFilesystem(out));
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
