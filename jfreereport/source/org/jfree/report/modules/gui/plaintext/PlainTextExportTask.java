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
 * $Id: PlainTextExportTask.java,v 1.5 2003/08/31 21:06:09 taqua Exp $
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
import java.io.OutputStream;
import java.io.IOException;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.util.StringUtil;
import org.jfree.report.util.Log;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.plaintext.EpsonPrinterCommandSet;
import org.jfree.report.modules.output.pageable.plaintext.IBMPrinterCommandSet;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PrinterCommandSet;

public class PlainTextExportTask extends ExportTask
{
  private final ReportProgressDialog progressDialog;
  private final String fileName;
  private final JFreeReport report;
  private final int exportType;
  private final int charPerInch;
  private final int linesPerInch;

  public PlainTextExportTask
      (final String fileName, final ReportProgressDialog dialog,
       final int exportType, final JFreeReport report)
  {
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
   * Returns the printer command set.
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
          return new PrinterCommandSet(out, report.getDefaultPageFormat(),
              charPerInch, linesPerInch);
        }
      case PlainTextExportDialog.TYPE_IBM_OUTPUT:
        {
          return new IBMPrinterCommandSet(out, report.getDefaultPageFormat(),
              charPerInch, linesPerInch);
        }
      case PlainTextExportDialog.TYPE_EPSON_OUTPUT:
        {
          return new EpsonPrinterCommandSet(out, report.getDefaultPageFormat(),
              charPerInch, linesPerInch);
        }
      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * Exports the repotr into a PDF file.
   */
  public void run()
  {
    OutputStream out = null;
    File file = new File (fileName);
    try
    {
      out = new BufferedOutputStream(
          new FileOutputStream(file));
      final PrinterCommandSet pc = getPrinterCommandSet(out, report);
      final PlainTextOutputTarget target =
        new PlainTextOutputTarget(report.getDefaultPageFormat(), pc);
      target.configure(report.getReportConfiguration());

      final PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setHandleInterruptedState(false);
      proc.addRepaginationListener(progressDialog);
      proc.setOutputTarget(target);

      target.open();
      proc.processReport();
      target.close();
      proc.removeRepaginationListener(progressDialog);
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
      Log.error ("PlainText export failed", re);
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
