package org.jfree.report.modules.gui.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.base.ReportPane;

/**
 * Creation-Date: 05.09.2005, 18:36:03
 *
 * @author: Thomas Morgner
 */
public class PrintUtil
{
  public static final String PRINTER_JOB_NAME_KEY =
          "org.jfree.report.modules.output.pageable.graphics.print.JobName";

  private PrintUtil()
  {
  }

  public static void printDirectly (final JFreeReport report)
          throws PrinterException, ReportProcessingException
  {
    final String jobName = report.getReportConfiguration().getConfigProperty
            (PRINTER_JOB_NAME_KEY, report.getName());

    final PrinterJob printerJob = PrinterJob.getPrinterJob();
    printerJob.setJobName(jobName);
    printerJob.setPageable(new ReportPane(report));
    printerJob.print();
  }

  public static boolean print (final JFreeReport report)
          throws PrinterException, ReportProcessingException
  {
    final String jobName = report.getReportConfiguration().getConfigProperty
            (PRINTER_JOB_NAME_KEY, report.getName());

    final PrinterJob printerJob = PrinterJob.getPrinterJob();
    printerJob.setJobName(jobName);
    printerJob.setPageable(new ReportPane(report));
    if (printerJob.printDialog())
    {
      printerJob.print();
      return true;
    }
    return false;
  }
}
