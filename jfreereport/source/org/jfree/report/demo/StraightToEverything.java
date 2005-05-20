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
 * -------------------------
 * StraightToEverything.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StraightToEverything.java,v 1.16 2005/05/18 18:38:26 taqua Exp $
 *
 * Changes
 * -------
 * 13-Dec-2002 : Version 1 (TM);
 *
 */

package org.jfree.report.demo;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.TextFilePrinterDriver;
import org.jfree.report.modules.output.table.csv.CSVTableProcessor;
import org.jfree.report.modules.output.table.html.DirectoryHtmlFilesystem;
import org.jfree.report.modules.output.table.html.HtmlProcessor;
import org.jfree.report.modules.output.table.html.StreamHtmlFilesystem;
import org.jfree.report.modules.output.table.html.ZIPHtmlFilesystem;
import org.jfree.report.modules.output.table.rtf.RTFProcessor;
import org.jfree.report.modules.output.table.xls.ExcelProcessor;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.xml.ParseException;
import org.jfree.util.ObjectUtilities;

/**
 * A demonstration that shows how to generate a report and save it to PDF without
 * displaying the print preview or the PDF save-as dialog. The methods to save the report
 * to the various file formats are also implemented in
 *
 * @author Thomas Morgner
 */
public class StraightToEverything
{

  /**
   * Creates a new demo application.
   *
   * @param filename the output filename.
   * @throws ParseException if the report could not be parsed.
   */
  public StraightToEverything (final String filename)
          throws ParseException
  {
    final URL in = ObjectUtilities.getResource
            ("org/jfree/report/demo/OpenSourceDemo.xml", StraightToEverything.class);
    final JFreeReport report = parseReport(in);
    final TableModel data = new OpenSourceProjects();
    report.setData(data);
    createPDF(report, filename + ".pdf");
    try
    {
      createCSV(report, filename + ".csv");
      createDirectoryHTML(report, filename + ".html");
      createPlainText(report, filename + ".txt");
      createRTF(report, filename + ".rtf");
      createStreamHTML(report, filename + "-single-file.html");
      createXLS(report, filename + ".xls");
      createZIPHTML(report, filename + ".zip");
    }
    catch (Exception e)
    {
      Log.error("Failed to write report", e);
    }
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL the template location.
   * @return a report.
   *
   * @throws ParseException if the report could not be parsed.
   */
  private JFreeReport parseReport (final URL templateURL)
          throws ParseException
  {
    final ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      return generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      throw new ParseException("Failed to parse the report", e);
    }
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report   the report.
   * @param fileName target file name.
   * @return true or false.
   */
  public static boolean createPDF (final JFreeReport report, final String fileName)
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
      final PDFOutputTarget target = new PDFOutputTarget(out);
      target.configure(report.getReportConfiguration());
      target.open();

      final PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setOutputTarget(target);
      proc.processReport();

      target.close();
      return true;
    }
    catch (Exception e)
    {
      Log.error("Writing PDF failed.", e);
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
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createPlainText (final JFreeReport report, final String filename)
          throws Exception
  {
    final PageableReportProcessor pr = new PageableReportProcessor(report);
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    // cpi = 10, lpi = 6
    final TextFilePrinterDriver pc = new TextFilePrinterDriver(fout, 15, 10);
    final PlainTextOutputTarget target = new PlainTextOutputTarget(pc);
    pr.setOutputTarget(target);
    target.open();
    pr.processReport();
    target.close();
    fout.close();
  }

  /**
   * Saves a report to rich-text format (RTF).
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createRTF (final JFreeReport report, final String filename)
          throws Exception
  {
    final RTFProcessor pr = new RTFProcessor(report);
    pr.setStrictLayout(false);
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

  /**
   * Saves a report to CSV format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createCSV (final JFreeReport report, final String filename)
          throws Exception
  {
    final CSVTableProcessor pr = new CSVTableProcessor(report);
    pr.setStrictLayout(false);
    final Writer fout = new BufferedWriter(new FileWriter(filename));
    pr.setWriter(fout);
    pr.processReport();
    fout.close();
  }

  /**
   * Saves a report to Excel format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createXLS (final JFreeReport report, final String filename)
          throws Exception
  {
    final ExcelProcessor pr = new ExcelProcessor(report);
    pr.setStrictLayout(false);
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

  /**
   * Saves a report into a single HTML format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createStreamHTML (final JFreeReport report, final String filename)
          throws Exception
  {
    final HtmlProcessor pr = new HtmlProcessor(report);
    pr.setStrictLayout(false);
    pr.setGenerateXHTML(true);
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setFilesystem(new StreamHtmlFilesystem(fout));
    pr.processReport();
    fout.close();
  }

  /**
   * Saves a report to HTML. The HTML file is stored in a directory.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createDirectoryHTML (final JFreeReport report,
                                          final String filename)
          throws Exception
  {
    final HtmlProcessor pr = new HtmlProcessor(report);
    pr.setFilesystem(new DirectoryHtmlFilesystem(new File(filename)));
    pr.processReport();
  }

  /**
   * Saves a report in a ZIP file. The zip file contains a HTML document.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws Exception if an error occurs.
   */
  public static void createZIPHTML (final JFreeReport report, final String filename)
          throws Exception
  {
    final HtmlProcessor pr = new HtmlProcessor(report);
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setFilesystem(new ZIPHtmlFilesystem(fout, "data"));
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
      new StraightToEverything(folder + "/OpenSource-Demo");
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}