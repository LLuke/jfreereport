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
 * $Id: FunctionalityTestLib.java,v 1.13 2005/09/19 13:34:24 taqua Exp $
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
import java.util.ArrayList;

import org.jfree.report.EmptyReportException;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.DemoFrontend;
import org.jfree.report.demo.helper.DemoHandler;
import org.jfree.report.demo.helper.DemoSelector;
import org.jfree.report.demo.helper.XmlDemoHandler;
import org.jfree.report.demo.helper.InternalDemoHandler;
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
import org.jfree.report.util.NullOutputStream;
import org.jfree.util.Log;

public class FunctionalityTestLib
{
  private FunctionalityTestLib()
  {
  }

  public static InternalDemoHandler[] getAllDemoHandlers()
  {
    DemoSelector selector = DemoFrontend.createDemoInfo();
    final ArrayList demos = new ArrayList();
    collectDemos(demos, selector);
    return (InternalDemoHandler[]) demos.toArray(
            new InternalDemoHandler[demos.size()]);
  }

  private static void collectDemos(ArrayList list, DemoSelector selector)
  {
    DemoHandler[] demoHandlers = selector.getDemos();
    for (int i = 0; i < demoHandlers.length; i++)
    {
      DemoHandler demoHandler = demoHandlers[i];
      if (demoHandler instanceof InternalDemoHandler)
      {
        list.add(demoHandler);
      }
    }
    DemoSelector[] childs = selector.getChilds();
    for (int i = 0; i < childs.length; i++)
    {
      DemoSelector child = childs[i];
      collectDemos(list, child);
    }
  }


  public static XmlDemoHandler[] getAllXmlDemoHandlers()
  {
    DemoSelector selector = DemoFrontend.createDemoInfo();
    final ArrayList demos = new ArrayList();
    collectXmlDemos(demos, selector);
    return (XmlDemoHandler[]) demos.toArray(new XmlDemoHandler[demos.size()]);
  }

  private static void collectXmlDemos(ArrayList list, DemoSelector selector)
  {
    DemoHandler[] demoHandlers = selector.getDemos();
    for (int i = 0; i < demoHandlers.length; i++)
    {
      DemoHandler demoHandler = demoHandlers[i];
      if (demoHandler instanceof XmlDemoHandler)
      {
        list.add(demoHandler);
      }
    }
    DemoSelector[] childs = selector.getChilds();
    for (int i = 0; i < childs.length; i++)
    {
      DemoSelector child = childs[i];
      collectXmlDemos(list, child);
    }
  }

  public static boolean createPlainText(final JFreeReport report)
  {
    try
    {
      final PageableReportProcessor pr = new PageableReportProcessor(report);
      final OutputStream fout = new BufferedOutputStream(
              new NullOutputStream());
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
      Log.debug("Failed to execute plain text: ", rpe);
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
    final Writer fout = new BufferedWriter(new OutputStreamWriter(
            new NullOutputStream()));
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

  public static boolean execGraphics2D(final JFreeReport report)
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
      Log.error("Generating Graphics2D failed.", e);
      return false;
    }
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report the report.
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
}
