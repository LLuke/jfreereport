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
 * ------------------------------
 * ExportTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExportTest.java,v 1.1 2003/06/01 20:43:38 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.output.EpsonPrinterCommandSet;
import com.jrefinery.report.targets.pageable.output.PlainTextOutputTarget;
import com.jrefinery.report.targets.pageable.output.PrinterCommandSet;
import com.jrefinery.report.targets.table.csv.CSVTableProcessor;
import com.jrefinery.report.targets.table.excel.ExcelProcessor;
import com.jrefinery.report.targets.table.html.HtmlProcessor;
import com.jrefinery.report.targets.table.html.StreamHtmlFilesystem;
import com.jrefinery.report.targets.table.html.ZIPHtmlFilesystem;
import com.jrefinery.report.targets.table.rtf.RTFProcessor;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.NullOutputStream;
import junit.framework.TestCase;

public class ExportTest extends TestCase
{
  public ExportTest(String s)
  {
    super(s);
  }

  public final static String[] REPORTS = ParseTest.REPORTS;

  public void testConvertReport() throws Exception
  {
    try
    {
      for (int i = 0; i < REPORTS.length; i++)
      {
        URL url = this.getClass().getResource(REPORTS[i]);
        assertNotNull(url);
        Log.debug("Processing: " + url);
        JFreeReport report = ReportGenerator.getInstance().parseReport(url);
        Log.debug("   CSV ..");
        createCSV(report);
        Log.debug("   PLAIN_TEXT ..");
        createPlainText(report);
        Log.debug("   RTF ..");
        createRTF(report);
        Log.debug("   STREAM_HTML ..");
        createStreamHTML(report);
        Log.debug("   EXCEL ..");
        createXLS(report);
        Log.debug("   ZIP_HTML ..");
        createZIPHTML(report);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }
  }

  public static void createPlainText(JFreeReport report)
      throws Exception
  {
    PageableReportProcessor pr = new PageableReportProcessor(report);
    OutputStream fout = new BufferedOutputStream(new NullOutputStream());
    PrinterCommandSet pc = new EpsonPrinterCommandSet(fout, report.getDefaultPageFormat(), 10, 15);
    PlainTextOutputTarget target = new PlainTextOutputTarget(report.getDefaultPageFormat(), pc);

    pr.setOutputTarget(target);
    target.open();
    pr.processReport();
    target.close();
    fout.close();
  }

  public static void createRTF(JFreeReport report)
      throws Exception
  {
    RTFProcessor pr = new RTFProcessor(report);
    pr.setStrictLayout(false);
    OutputStream fout = new BufferedOutputStream(new NullOutputStream());
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

  public static void createCSV(JFreeReport report)
      throws Exception
  {
    CSVTableProcessor pr = new CSVTableProcessor(report);
    pr.setStrictLayout(false);
    Writer fout = new BufferedWriter(new OutputStreamWriter(new NullOutputStream()));
    pr.setWriter(fout);
    pr.processReport();
    fout.close();
  }

  public static void createXLS(JFreeReport report)
      throws Exception
  {
    ExcelProcessor pr = new ExcelProcessor(report);
    pr.setStrictLayout(false);
    OutputStream fout = new BufferedOutputStream(new NullOutputStream());
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

  public static void createStreamHTML(JFreeReport report)
      throws Exception
  {
    HtmlProcessor pr = new HtmlProcessor(report);
    pr.setStrictLayout(false);
    OutputStream fout = new BufferedOutputStream(new NullOutputStream());
    pr.setFilesystem(new StreamHtmlFilesystem(fout));
    pr.processReport();
    fout.close();
  }

  public static void createZIPHTML(JFreeReport report)
      throws Exception
  {
    HtmlProcessor pr = new HtmlProcessor(report);
    OutputStream fout = new BufferedOutputStream(new NullOutputStream());
    pr.setFilesystem(new ZIPHtmlFilesystem(fout, "data"));
    pr.processReport();
    fout.close();
  }

}
