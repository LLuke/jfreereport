/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * -------------------------
 * StraightToEverything.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StraightToEverything.java,v 1.4 2003/02/25 14:10:24 taqua Exp $
 *
 * Changes
 * -------
 * 13-Dec-2002 : Version 1 (TM);
 *
 */

package com.jrefinery.report.demo;

import java.awt.print.PageFormat;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;

import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.output.PDFOutputTarget;
import com.jrefinery.report.targets.pageable.output.PlainTextOutputTarget;
import com.jrefinery.report.targets.pageable.output.PrinterCommandSet;
import com.jrefinery.report.targets.table.csv.CSVTableProcessor;
import com.jrefinery.report.targets.table.excel.ExcelProcessor;
import com.jrefinery.report.targets.table.html.DirectoryHtmlFilesystem;
import com.jrefinery.report.targets.table.html.HtmlProcessor;
import com.jrefinery.report.targets.table.html.StreamHtmlFilesystem;
import com.jrefinery.report.targets.table.html.ZIPHtmlFilesystem;
import com.jrefinery.report.targets.table.rtf.RTFProcessor;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;

/**
 * A demonstration that shows how to generate a report and save it to PDF without displaying
 * the print preview or the PDF save-as dialog. The methods to save the report to the various
 * file formats are also implemented in 
 * {@link com.jrefinery.report.targets.support.ReportProcessorUtil}.
 *
 * @author Thomas Morgner
 */
public class StraightToEverything
{

  /**
   * Creates a new demo application.
   *
   * @param filename  the output filename.
   */
  public StraightToEverything(String filename)
  {
    URL in  = getClass().getResource("/com/jrefinery/report/demo/OpenSourceDemo.xml");
    JFreeReport report = parseReport(in);
    TableModel data = new OpenSourceProjects();
    report.setData(data);
    createPDF(report, filename + ".pdf");
    try
    {
      createCSV(report, filename + ".csv");
      createDirectoryHTML(report, filename + ".html");
      createPlainText(report, filename + ".txt");
      createRTF(report, filename + ".rtf");
      createStreamHTML(report, filename + ".html");
      createXLS(report, filename + ".xls");
      createZIPHTML(report, filename + ".zip");
    }
    catch (Exception e)
    {
      Log.error ("Failed to write report", e);
    }
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL  the template location.
   *
   * @return a report.
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
      Log.error ("Failed to parse the report", e);
    }
    return result;
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report  the report.
   * @param fileName target file name.
   *
   * @return true or false.
   */
  public static boolean createPDF(JFreeReport report, String fileName)
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
      PageFormat pf = report.getDefaultPageFormat();
      PDFOutputTarget target = new PDFOutputTarget(out, pf, true);
      target.configure(report.getReportConfiguration());
      target.open();

      PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setOutputTarget(target);
      proc.processReport();

      target.close();
      return true;
    }
    catch (Exception e)
    {
      Log.error ("Writing PDF failed.", e);
      return false;
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
        Log.error("Saving PDF failed.", e);
      }
    }
  }

  /**
   * Saves a report to plain text format.
   *
   * @param report  the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createPlainText (JFreeReport report, String filename)
    throws Exception
  {
    PageableReportProcessor pr = new PageableReportProcessor(report);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    PrinterCommandSet pc = new PrinterCommandSet(fout, report.getDefaultPageFormat(), 6, 10);
    PlainTextOutputTarget target = new PlainTextOutputTarget(report.getDefaultPageFormat(), pc);
    pr.setOutputTarget(target);
    target.open();
    pr.processReport();
    target.close();
    fout.close();
  }

  /**
   * Saves a report to rich-text format (RTF).
   *
   * @param report  the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
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

  /**
   * Saves a report to CSV format.
   *
   * @param report  the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
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

  /**
   * Saves a report to Excel format.
   *
   * @param report  the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
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

  /**
   * Saves a report into a single HTML format.
   *
   * @param report  the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
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

  /**
   * Saves a report to HTML. The HTML file is stored in a directory.
   *
   * @param report  the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createDirectoryHTML (JFreeReport report, String filename)
    throws Exception
  {
    HtmlProcessor pr = new HtmlProcessor(report);
    pr.setFilesystem(new DirectoryHtmlFilesystem (new File(filename)));
    pr.processReport();
  }

  /**
   * Saves a report in a ZIP file. The zip file contains a HTML document.
   *
   * @param report  the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createZIPHTML (JFreeReport report, String filename)
    throws Exception
  {
    HtmlProcessor pr = new HtmlProcessor(report);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setFilesystem(new ZIPHtmlFilesystem (fout, "data"));
    pr.processReport();
    fout.close();
  }

  /**
   * Demo starting point.
   *
   * @param args  ignored.
   */
  public static void main(String args[])
  {
    ReportConfiguration.getGlobalConfig().setLogLevel("Warn");
    ReportConfiguration.getGlobalConfig().setPDFTargetAutoInit(false);
    StraightToEverything demo = new StraightToEverything(System.getProperty("user.home") 
                                                         + "/test-everything");
    System.exit(0);
  }

}