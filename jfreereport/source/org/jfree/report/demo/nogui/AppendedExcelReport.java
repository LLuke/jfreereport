package org.jfree.report.demo.nogui;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.opensource.OpenSourceXMLDemoHandler;
import org.jfree.report.demo.world.CountryReportXMLDemoHandler;
import org.jfree.report.modules.output.table.xls.AppendingExcelProcessor;
import org.jfree.util.Log;

/**
 * Creation-Date: 04.09.2005, 20:04:12
 *
 * @author: Thomas Morgner
 */
public class AppendedExcelReport
{
  private String filename;

  private AppendedExcelReport(final String filename)
  {
    this.filename = filename;
  }

  private void perform ()
          throws ReportDefinitionException, ReportProcessingException, IOException
  {
    final JFreeReport report = new OpenSourceXMLDemoHandler().createReport();
    final JFreeReport second = new CountryReportXMLDemoHandler().createReport();

    final AppendingExcelProcessor pr = new AppendingExcelProcessor(report);
    pr.addReport(second);

    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

  /**
   * Demo starting point.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // disable PDF target autoinit must be done outside ...
    try
    {
      final String folder;
      if (args.length == 0)
      {
        folder = System.getProperty("user.home");
      }
      else
      {
        folder = args[0];
      }
      //final StraightToEverything demo =
      new AppendedExcelReport(folder + "/Appending-Demo").perform();
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}
