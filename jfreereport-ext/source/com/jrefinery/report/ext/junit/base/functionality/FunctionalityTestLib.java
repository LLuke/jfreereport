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
 * FunctionalityTestLib.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionalityTestLib.java,v 1.2 2003/06/20 12:02:20 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 13.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.awt.print.PageFormat;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.jrefinery.report.EmptyReportException;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.demo.OpenSourceProjects;
import com.jrefinery.report.demo.PercentageDemo;
import com.jrefinery.report.demo.SampleData1;
import com.jrefinery.report.demo.SampleData2;
import com.jrefinery.report.demo.SampleData3;
import com.jrefinery.report.demo.SampleData4;
import com.jrefinery.report.demo.SwingIconsDemoTableModel;
import com.jrefinery.report.demo.cards.CardDemo;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceReferenceGenerator;
import com.jrefinery.report.io.ext.factory.objects.ObjectReferenceGenerator;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyReferenceGenerator;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.output.G2OutputTarget;
import com.jrefinery.report.targets.pageable.output.PDFOutputTarget;
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

public class FunctionalityTestLib
{
  public final static ReportTest[] REPORTS = {
    new ReportTest ("/com/jrefinery/report/demo/report1.xml", new SampleData1()),
    new ReportTest ("/com/jrefinery/report/demo/report1a.xml", new SampleData1()),
    new ReportTest ("/com/jrefinery/report/demo/report2.xml", new SampleData2()),
    new ReportTest ("/com/jrefinery/report/demo/report2a.xml", new SampleData2()),
    new ReportTest ("/com/jrefinery/report/demo/report2b.xml", new SampleData2()),
    new ReportTest ("/com/jrefinery/report/demo/report2c.xml", new SampleData2()),
    new ReportTest ("/com/jrefinery/report/demo/report2d.xml", new SampleData2()),
    new ReportTest ("/com/jrefinery/report/demo/report3.xml", new SampleData3()),
    new ReportTest ("/com/jrefinery/report/demo/report4.xml", new SampleData4()),
    new ReportTest ("/com/jrefinery/report/demo/report5.xml", new DefaultTableModel()),
    new ReportTest ("/com/jrefinery/report/demo/lgpl.xml", new DefaultTableModel()),
    new ReportTest ("/com/jrefinery/report/demo/OpenSourceDemo.xml", new OpenSourceProjects()),
    new ReportTest ("/com/jrefinery/report/demo/PercentageDemo.xml", PercentageDemo.createData()),
    new ReportTest ("/com/jrefinery/report/demo/shape-and-drawable.xml", new DefaultTableModel()),
    new ReportTest ("/com/jrefinery/report/demo/swing-icons.xml", new SwingIconsDemoTableModel()),
    new ReportTest ("/com/jrefinery/report/demo/cards/usercards.xml", CardDemo.createSimpleDemoModel()),
    new ReportTest ("/com/jrefinery/report/io/ext/factory/objects/ObjectReferenceReport.xml", ObjectReferenceGenerator.createData()),
    new ReportTest ("/com/jrefinery/report/io/ext/factory/stylekey/StyleKeyReferenceReport.xml", StyleKeyReferenceGenerator.createData()),
    new ReportTest ("/com/jrefinery/report/io/ext/factory/datasource/DataSourceReferenceReport.xml", DataSourceReferenceGenerator.createData())
  };

  public static boolean createPlainText(final JFreeReport report)
  {
    try
    {
      final PageableReportProcessor pr = new PageableReportProcessor(report);
      final OutputStream fout = new BufferedOutputStream(new NullOutputStream());
      final PrinterCommandSet pc = new PrinterCommandSet(fout, report.getDefaultPageFormat(), 10, 15);
      final PlainTextOutputTarget target = new PlainTextOutputTarget(report.getDefaultPageFormat(), pc);

      pr.setOutputTarget(target);
      target.open();
      pr.processReport();
      target.close();
      fout.close();
      return true;
    }
    catch (EmptyReportException ere)
    {
      // ignored ... expected ...
      return true;
    }
    catch (Exception rpe)
    {
      Log.debug ("Failed to execute plain text: " , rpe);
      return false;
    }
  }

  public static void createRTF(final JFreeReport report)
      throws Exception
  {
    final RTFProcessor pr = new RTFProcessor(report);
    pr.setStrictLayout(false);
    final OutputStream fout = new BufferedOutputStream(new NullOutputStream());
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

  public static void createCSV(final JFreeReport report)
      throws Exception
  {
    final CSVTableProcessor pr = new CSVTableProcessor(report);
    pr.setStrictLayout(false);
    final Writer fout = new BufferedWriter(new OutputStreamWriter(new NullOutputStream()));
    pr.setWriter(fout);
    pr.processReport();
    fout.close();
  }

  public static void createXLS(final JFreeReport report)
      throws Exception
  {
    final ExcelProcessor pr = new ExcelProcessor(report);
    pr.setStrictLayout(false);
    final OutputStream fout = new BufferedOutputStream(new NullOutputStream());
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

  public static void createStreamHTML(final JFreeReport report)
      throws Exception
  {
    final HtmlProcessor pr = new HtmlProcessor(report);
    pr.setStrictLayout(false);
    final OutputStream fout = new BufferedOutputStream(new NullOutputStream());
    pr.setFilesystem(new StreamHtmlFilesystem(fout));
    pr.processReport();
    fout.close();
  }

  public static void createZIPHTML(final JFreeReport report)
      throws Exception
  {
    final HtmlProcessor pr = new HtmlProcessor(report);
    final OutputStream fout = new BufferedOutputStream(new NullOutputStream());
    pr.setFilesystem(new ZIPHtmlFilesystem(fout, "data"));
    pr.processReport();
    fout.close();
  }

  public static boolean execGraphics2D (final JFreeReport report)
  {
    try
    {
      final G2OutputTarget target =
          new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), report.getDefaultPageFormat());
      target.configure(report.getReportConfiguration());
      target.open();

      final PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setOutputTarget(target);
      proc.processReport();

      target.close();
      return true;
    }
    catch (EmptyReportException ere)
    {
      return true;
    }
    catch (Exception e)
    {
      Log.error ("Generating Graphics2D failed.", e);
      return false;
    }
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report  the report.
   *
   * @return true or false.
   */
  public static boolean createPDF(final JFreeReport report)
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new NullOutputStream());
      final PageFormat pf = report.getDefaultPageFormat();
      final PDFOutputTarget target = new PDFOutputTarget(out, pf, true);
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

  public static class ReportTest
  {
    public ReportTest(final String reportDefinition, final TableModel reportTableModel)
    {
      this.reportDefinition = reportDefinition;
      this.reportTableModel = reportTableModel;
    }

    private String reportDefinition;
    private TableModel reportTableModel;

    public String getReportDefinition()
    {
      return reportDefinition;
    }

    public TableModel getReportTableModel()
    {
      return reportTableModel;
    }
  }
}
