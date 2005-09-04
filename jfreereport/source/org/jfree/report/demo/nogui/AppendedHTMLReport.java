package org.jfree.report.demo.nogui;

import java.io.File;
import java.io.IOException;

import org.jfree.report.modules.output.table.html.HtmlProcessor;
import org.jfree.report.modules.output.table.html.DirectoryHtmlFilesystem;
import org.jfree.report.modules.output.table.html.AppendingHtmlProcessor;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.opensource.OpenSourceXMLDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.world.CountryReportXMLDemoHandler;
import org.jfree.util.Log;

/**
 * Creation-Date: 04.09.2005, 20:04:12
 *
 * @author: Thomas Morgner
 */
public class AppendedHTMLReport
{
  private String filename;

  private AppendedHTMLReport(final String filename)
  {
    this.filename = filename;
  }

  private void perform ()
          throws ReportDefinitionException, ReportProcessingException, IOException
  {
    final JFreeReport report = new OpenSourceXMLDemoHandler().createReport();
    final JFreeReport second = new CountryReportXMLDemoHandler().createReport();

    final AppendingHtmlProcessor pr = new AppendingHtmlProcessor(report);
    pr.addReport(second);

    pr.setFilesystem(new DirectoryHtmlFilesystem(new File(filename + ".html")));
    pr.processReport();
  }

  /**
   * Demo starting point.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    ReportConfiguration.getGlobalConfig().setLogLevel("Warn");
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
      new AppendedHTMLReport(folder + "/Appending-Demo").perform();
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}
