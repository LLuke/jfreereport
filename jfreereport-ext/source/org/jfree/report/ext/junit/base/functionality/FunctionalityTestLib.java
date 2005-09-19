/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * FunctionalityTestLib.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionalityTestLib.java,v 1.12 2005/09/07 11:24:09 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 13.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.functionality;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.EmptyReportException;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.bookstore.BookstoreTableModel;
import org.jfree.report.demo.cards.AccountCard;
import org.jfree.report.demo.cards.AdminCard;
import org.jfree.report.demo.cards.CardTableModel;
import org.jfree.report.demo.cards.FreeCard;
import org.jfree.report.demo.cards.PrepaidCard;
import org.jfree.report.demo.cards.UserCard;
import org.jfree.report.demo.cards.WrappingTableModel;
import org.jfree.report.demo.fonts.FontTableModel;
import org.jfree.report.demo.functions.PercentageDemo;
import org.jfree.report.demo.groups.ColorAndLetterTableModel;
import org.jfree.report.demo.opensource.OpenSourceProjects;
import org.jfree.report.demo.swingicons.SwingIconsDemoTableModel;
import org.jfree.report.demo.world.CountryDataTableModel;
import org.jfree.report.modules.misc.referencedoc.DataSourceReferenceGenerator;
import org.jfree.report.modules.misc.referencedoc.ObjectReferenceGenerator;
import org.jfree.report.modules.misc.referencedoc.StyleKeyReferenceGenerator;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.graphics.G2OutputTarget;
import org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PrinterDriver;
import org.jfree.report.modules.output.pageable.plaintext.TextFilePrinterDriver;
import org.jfree.report.modules.output.table.csv.CSVTableProcessor;
import org.jfree.report.modules.output.table.html.HtmlProcessor;
import org.jfree.report.modules.output.table.html.StreamHtmlFilesystem;
import org.jfree.report.modules.output.table.html.ZIPHtmlFilesystem;
import org.jfree.report.modules.output.table.rtf.RTFProcessor;
import org.jfree.report.modules.output.table.xls.ExcelProcessor;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.NullOutputStream;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public class FunctionalityTestLib
{
  public final static ReportTest[] REPORTS = {
    new ReportTest ("org/jfree/report/demo/world/country-report.xml", new CountryDataTableModel()),
    new ReportTest ("org/jfree/report/demo/world/country-report-ext.xml", new CountryDataTableModel()),
    new ReportTest ("org/jfree/report/demo/groups/data-groups.xml", new ColorAndLetterTableModel()),
    new ReportTest ("org/jfree/report/demo/functions/paint-component.xml", new ColorAndLetterTableModel()),
    new ReportTest ("org/jfree/report/demo/functions/item-hiding.xml", new ColorAndLetterTableModel()),
    new ReportTest ("org/jfree/report/demo/bookstore/bookstore.xml", new BookstoreTableModel()),
    new ReportTest ("org/jfree/report/demo/fonts/fonts.xml", new FontTableModel()),
    new ReportTest ("org/jfree/report/demo/report5.xml", new DefaultTableModel()),
    new ReportTest ("org/jfree/report/demo/largetext/lgpl.xml", new DefaultTableModel()),
    new ReportTest ("org/jfree/report/demo/opensource/opensource.xml", new OpenSourceProjects()),
    new ReportTest ("org/jfree/report/demo/functions/percentage.xml", PercentageDemo.createData()),
    new ReportTest ("org/jfree/report/demo/layouts/shape-and-drawable.xml", new DefaultTableModel()),
    new ReportTest ("org/jfree/report/demo/swingicons/swing-icons.xml", new SwingIconsDemoTableModel()),
    new ReportTest ("org/jfree/report/demo/cards/usercards.xml", createSimpleCardDemoModel()),
    new ReportTest
      ("org/jfree/report/modules/misc/referencedoc/ObjectReferenceReport.xml",
      ObjectReferenceGenerator.createData()),
    new ReportTest
      ("org/jfree/report/modules/misc/referencedoc/StyleKeyReferenceReport.xml",
      StyleKeyReferenceGenerator.createData()),
    new ReportTest
      ("org/jfree/report/modules/misc/referencedoc/DataSourceReferenceReport.xml",
      DataSourceReferenceGenerator.createData())
  };

  public static TableModel createSimpleCardDemoModel ()
  {
    final CardTableModel model = new CardTableModel();
    model.addCard(new AdminCard("Jared", "Diamond", "NR123123", "login", "secret", new Date()));
    model.addCard(new FreeCard("NR123123", new Date()));
    model.addCard(new PrepaidCard("First Name", "Last Name", "NR123123"));
    model.addCard(new AccountCard("John", "Doe", "NR123123", "login", "secret"));
    model.addCard(new UserCard("Richard", "Helm", "NR123123", "login", "secret", new Date()));
    return new WrappingTableModel(model, "C1_", "C2_");
  }


  public static boolean createPlainText(final JFreeReport report)
  {
    try
    {
      final PageableReportProcessor pr = new PageableReportProcessor(report);
      final OutputStream fout = new BufferedOutputStream(new NullOutputStream());
      final PrinterDriver pc =
          new TextFilePrinterDriver(fout, 10, 15);
      final PlainTextOutputTarget target = 
          new PlainTextOutputTarget(pc);

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
          new G2OutputTarget(G2OutputTarget.createEmptyGraphics());
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

  public static JFreeReport createReport (final ReportTest reportDefinition)
  {
    final URL url = ObjectUtilities.getResource
            (reportDefinition.getReportDefinition(), FunctionalityTestLib.class);
    if (url == null)
    {
      throw new IllegalStateException("URL is null.");
    }
    try
    {
      final JFreeReport report = ReportGenerator.getInstance().parseReport(url);
      report.setData(reportDefinition.getReportTableModel());
      return report;
    }
    catch (Exception e)
    {
      Log.debug("Failed to parse " + url, e);
      throw new IllegalStateException("Failed to parse");
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
  
  private FunctionalityTestLib()
  {
  }
}
