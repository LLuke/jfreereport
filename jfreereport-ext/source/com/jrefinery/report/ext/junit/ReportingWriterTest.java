/**
 * Date: Jan 25, 2003
 * Time: 4:25:02 PM
 *
 * $Id: ReportingWriterTest.java,v 1.1 2003/03/01 15:00:14 taqua Exp $
 */
package com.jrefinery.report.ext.junit;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.demo.SampleData1;
import com.jrefinery.report.demo.SampleData2;
import com.jrefinery.report.demo.SampleData3;
import com.jrefinery.report.demo.SwingIconsDemoTableModel;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.output.EpsonPrinterCommandSet;
import com.jrefinery.report.targets.pageable.output.PlainTextOutputTarget;
import com.jrefinery.report.targets.pageable.output.PrinterCommandSet;
import com.jrefinery.report.targets.table.csv.CSVTableProcessor;
import com.jrefinery.report.targets.table.excel.ExcelProcessor;
import com.jrefinery.report.targets.table.html.DirectoryHtmlFilesystem;
import com.jrefinery.report.targets.table.html.HtmlProcessor;
import com.jrefinery.report.targets.table.html.StreamHtmlFilesystem;
import com.jrefinery.report.targets.table.html.ZIPHtmlFilesystem;
import com.jrefinery.report.targets.table.rtf.RTFProcessor;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;

public class ReportingWriterTest
{
  /**
   * Displays a print preview screen for the sample report.
   */
  protected JFreeReport previewReport2()
  {
    JFreeReport report = null;
    URL in = getClass().getResource("/com/jrefinery/report/demo/report2.xml");
    report = parseReport(in);
    report.setData(new SampleData2 ());

    return report;
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected JFreeReport previewReport1()
  {
    JFreeReport report = null;
    URL in = getClass().getResource("/com/jrefinery/report/demo/report1.xml");
    report = parseReport(in);
    report.setData(new SampleData1 ());

    return report;
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected JFreeReport previewReportFirst()
  {
    JFreeReport report = null;
    URL in = getClass().getResource("/com/jrefinery/report/demo/first.xml");
    report = parseReport(in);
    report.setData(new SwingIconsDemoTableModel());

    return report;
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected JFreeReport previewReport3()
  {
    JFreeReport report = null;
    URL in = getClass().getResource("/com/jrefinery/report/demo/report3.xml");
    report = parseReport(in);
    report.setData(new SampleData3 ());

    return report;
  }


  /**
   * Reads the report from the first.xml report template.
   *
   * @param templateURL The template location.
   *
   * @return A report.
   */
  private JFreeReport parseReport(URL templateURL)
  {

    JFreeReport result = null;
    ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      result = generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      ExceptionDialog.showExceptionDialog("Error on parsing",
                                          "Error while parsing " + templateURL, e);
    }
    return result;

  }


  public static void main (String [] args)
    throws Throwable
  {
    try
    {
      ReportingWriterTest t = new ReportingWriterTest();
      JFreeReport report = t.previewReport2();


      Log.debug ("report.pageHeader " + report.getPageHeader().getElementCount());
      Log.debug ("report.reportHeader " + report.getReportHeader().getElementCount());

      //report.setData(new DefaultTableModel());
      //createXLS(report);
      createPlainText(report, "c:/test.txt");
      //createZIPHTML(report);
      //createStreamHTML(report);
      //createRTF(report);
      //createCSV(report, "test.csv");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.exit(0);
  }

  public static void createPlainText (JFreeReport report, String filename)
    throws Exception
  {
    PageableReportProcessor pr = new PageableReportProcessor(report);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    PrinterCommandSet pc = new EpsonPrinterCommandSet(fout, report.getDefaultPageFormat(), 10, 15);
    PlainTextOutputTarget target = new PlainTextOutputTarget(report.getDefaultPageFormat(), pc);
    
    pr.setOutputTarget(target);
    target.open();
    pr.processReport();
    target.close();
    fout.close();
  }

  public static void createRTF (JFreeReport report, String filename)
    throws Exception
  {
    RTFProcessor pr = new RTFProcessor(report);
    pr.setStrictLayout(false);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

  public static void createCSV (JFreeReport report, String filename)
    throws Exception
  {
    CSVTableProcessor pr = new CSVTableProcessor(report);
    pr.setStrictLayout(false);
    Writer fout = new BufferedWriter(new FileWriter(filename));
    pr.setWriter(fout);
    pr.processReport();
    fout.close();
  }

  public static void createXLS (JFreeReport report, String filename)
    throws Exception
  {
    ExcelProcessor pr = new ExcelProcessor(report);
    pr.setStrictLayout(false);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

  public static void createStreamHTML (JFreeReport report, String filename)
    throws Exception
  {
    HtmlProcessor pr = new HtmlProcessor(report);
    pr.setStrictLayout(false);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setFilesystem(new StreamHtmlFilesystem (fout));
    pr.processReport();
    fout.close();
  }

  public static void createDirectoryHTML (JFreeReport report, String filename)
    throws Exception
  {
    HtmlProcessor pr = new HtmlProcessor(report);
    pr.setFilesystem(new DirectoryHtmlFilesystem (new File(filename)));
    pr.processReport();
  }

  public static void createZIPHTML (JFreeReport report, String filename)
    throws Exception
  {
    HtmlProcessor pr = new HtmlProcessor(report);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setFilesystem(new ZIPHtmlFilesystem (fout, "data"));
    pr.processReport();
    fout.close();
  }
}
