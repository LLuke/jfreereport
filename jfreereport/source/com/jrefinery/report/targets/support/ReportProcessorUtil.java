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
 * ------------------------
 * ReportProcessorUtil.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportProcessorUtil.java,v 1.3 2003/02/25 15:42:28 taqua Exp $
 *
 * Changes
 * -------
 * 12-Feb-2003 : Initial version
 *
 */
package com.jrefinery.report.targets.support;

import java.awt.print.PageFormat;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.targets.pageable.OutputTargetException;
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

/**
 * Library functions to save a report into the different output targets.
 * The functions here are provided to cover the common use cases, they are
 * not intended to be configurable in any way.
 * 
 * @author Thomas Morgner
 */
public class ReportProcessorUtil
{
  /**
   * Saves a report into a single HTML format.
   *
   * @param report  the report.
   * @param filename target file name.
   * 
   * @throws ReportProcessingException if the report processing failed.
   * @throws FunctionInitializeException if the initialisation of the report processor failed.
   * @throws IOException if there was an IOerror while processing the report.
   */
  public static void createStreamHTML (JFreeReport report, String filename)
    throws IOException, FunctionInitializeException, ReportProcessingException
  {
    HtmlProcessor pr = new HtmlProcessor(report);
    pr.setStrictLayout(false);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setFilesystem(new StreamHtmlFilesystem (fout));
    pr.processReport();
    fout.close();
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
   * 
   * @throws ReportProcessingException if the report processing failed.
   * @throws FunctionInitializeException if the initialisation of the report processor failed.
   * @throws IOException if there was an IOerror while processing the report.
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public static void createPlainText (JFreeReport report, String filename)
   throws IOException, ReportProcessingException, FunctionInitializeException, OutputTargetException
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
   * 
   * @throws ReportProcessingException if the report processing failed.
   * @throws FunctionInitializeException if the initialisation of the report processor failed.
   * @throws IOException if there was an IOerror while processing the report.
   */
  public static void createRTF (JFreeReport report, String filename)
    throws IOException, ReportProcessingException, FunctionInitializeException
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
   * 
   * @throws ReportProcessingException if the report processing failed.
   * @throws FunctionInitializeException if the initialisation of the report processor failed.
   * @throws IOException if there was an IOerror while processing the report.
   */
  public static void createCSV (JFreeReport report, String filename)
    throws ReportProcessingException, FunctionInitializeException, IOException
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
   * 
   * @throws ReportProcessingException if the report processing failed.
   * @throws FunctionInitializeException if the initialisation of the report processor failed.
   * @throws IOException if there was an IOerror while processing the report.
   */
  public static void createXLS (JFreeReport report, String filename)
    throws IOException, FunctionInitializeException, ReportProcessingException
  {
    ExcelProcessor pr = new ExcelProcessor(report);
    pr.setStrictLayout(false);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }


  /**
   * Saves a report to HTML. The HTML file is stored in a directory.
   *
   * @param report  the report.
   * @param filename target file name.
   * 
   * @throws ReportProcessingException if the report processing failed.
   * @throws FunctionInitializeException if the initialisation of the report processor failed.
   * @throws IOException if there was an IOerror while processing the report.
   */
  public static void createDirectoryHTML (JFreeReport report, String filename)
    throws IOException, ReportProcessingException, FunctionInitializeException
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
   * 
   * @throws ReportProcessingException if the report processing failed.
   * @throws FunctionInitializeException if the initialisation of the report processor failed.
   * @throws IOException if there was an IOerror while processing the report.
   */
  public static void createZIPHTML (JFreeReport report, String filename)
    throws IOException, ReportProcessingException, FunctionInitializeException
  {
    HtmlProcessor pr = new HtmlProcessor(report);
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setFilesystem(new ZIPHtmlFilesystem (fout, "data"));
    pr.processReport();
    fout.close();
  }
}
