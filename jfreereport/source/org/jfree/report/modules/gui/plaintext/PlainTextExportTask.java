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
 * PlainTextExportTask.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextExportTask.java,v 1.9 2003/11/07 18:33:53 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.plaintext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.plaintext.EpsonPrinterCommandSet;
import org.jfree.report.modules.output.pageable.plaintext.IBMPrinterCommandSet;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PrinterCommandSet;
import org.jfree.report.util.Log;
import org.jfree.report.util.StringUtil;

/**
 * An export task implementation that writes the report into a plain text file.
 * 
 * @author Thomas Morgner
 */
public class PlainTextExportTask extends ExportTask
{
  /** The progress monitor component that visualizes the export progress. */
  private final ReportProgressDialog progressDialog;
  /** The name of the target file. */
  private final String fileName;
  /** The report that should be exported. */
  private final JFreeReport report;
  /** The desired export type, one of the constants defined in the PlainTextExportDialog. */
  private final int exportType;
  /** The chars per inch for the export. */
  private final int charPerInch;
  /** The lines per inch for the export. */
  private final int linesPerInch;

  /**
   * Creates a new plain text export task.
   * 
   * @param fileName the name of the target file.
   * @param dialog the progress monitor dialog.
   * @param exportType the desired export type.
   * @param report the report that should be exported.
   */
  public PlainTextExportTask
      (final String fileName, final ReportProgressDialog dialog,
       final int exportType, final JFreeReport report)
  {
    if (fileName == null)
    {
      throw new NullPointerException("File name is null.");
    }
    if (report == null)
    {
      throw new NullPointerException("Report is null.");
    }
    this.fileName = fileName;
    this.progressDialog = dialog;
    this.report = report;
    this.exportType = exportType;

    charPerInch = StringUtil.parseInt(report.getReportConfiguration().getConfigProperty
        (PlainTextOutputTarget.CONFIGURATION_PREFIX + PlainTextOutputTarget.CHARS_PER_INCH),10);
    linesPerInch = StringUtil.parseInt(report.getReportConfiguration().getConfigProperty
        (PlainTextOutputTarget.CONFIGURATION_PREFIX + PlainTextOutputTarget.LINES_PER_INCH) ,6);
  }

  /**
   * Returns the printer command set for the given report and export type.
   *
   * @param out  the output stream.
   * @param report  the report.
   *
   * @return The printer command set.
   */
  public PrinterCommandSet getPrinterCommandSet(final OutputStream out, final JFreeReport report)
  {
    switch (exportType)
    {
      case PlainTextExportDialog.TYPE_PLAIN_OUTPUT:
        {
          return new PrinterCommandSet(out, charPerInch, linesPerInch);
        }
      case PlainTextExportDialog.TYPE_IBM_OUTPUT:
        {
          return new IBMPrinterCommandSet(out, charPerInch, linesPerInch);
        }
      case PlainTextExportDialog.TYPE_EPSON_OUTPUT:
        {
          return new EpsonPrinterCommandSet(out, charPerInch, linesPerInch);
        }
      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * Exports the report into a plain text file.
   */
  protected void performExport()
  {
    OutputStream out = null;
    final File file = new File (fileName);
    try
    {
      out = new BufferedOutputStream(
          new FileOutputStream(file));
      final PrinterCommandSet pc = getPrinterCommandSet(out, report);
      final PlainTextOutputTarget target =
        new PlainTextOutputTarget(pc);
      target.configure(report.getReportConfiguration());

      final PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setHandleInterruptedState(false);
      if (progressDialog != null)
      {
        progressDialog.setModal(false);
        progressDialog.setVisible(true);
        proc.addRepaginationListener(progressDialog);
      }
      proc.setOutputTarget(target);

      target.open();
      proc.processReport();
      target.close();
      if (progressDialog != null)
      {
        proc.removeRepaginationListener(progressDialog);
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
      Log.error ("PlainText export failed", re);
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
  protected void dispose()
  {
    super.dispose();
    if (progressDialog != null)
    {
      progressDialog.dispose();
    }
  }
}
