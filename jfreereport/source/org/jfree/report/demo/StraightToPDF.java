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
 * ------------------
 * StraightToPDF.java
 * ------------------
 * (C)opyright 2002, 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: StraightToPDF.java,v 1.7.4.1 2004/10/13 17:18:55 taqua Exp $
 *
 * Changes
 * -------
 * 13-Dec-2002 : Version 1 (DG);
 *
 */

package org.jfree.report.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.WaitingImageObserver;
import org.jfree.xml.ParseException;

/**
 * A demonstration that shows how to generate a report and save it to PDF without displaying
 * the print preview or the PDF save-as dialog.
 *
 * @author David Gilbert
 */
public class StraightToPDF
{

  /**
   * Creates a new demo application.
   *
   * @param filename  the output filename.
   * @throws ParseException if the report could not be parsed.
   */
  public StraightToPDF(final String filename) throws ParseException
  {
    final URL in = getClass().getResource("/org/jfree/report/demo/OpenSourceDemo.xml");
    final JFreeReport report = parseReport(in);
    final TableModel data = new OpenSourceProjects();
    report.setData(data);
    savePDF(report, filename);
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL  the template location.
   *
   * @return a report.
   * @throws ParseException if the report could not be parsed.
   */
  private JFreeReport parseReport(final URL templateURL) throws ParseException
  {
    final ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      final JFreeReport report = generator.parseReport(templateURL);
      final URL imageURL = getClass().getResource("/org/jfree/report/demo/gorilla.jpg");
      final Image image = Toolkit.getDefaultToolkit().createImage(imageURL);
      final WaitingImageObserver obs = new WaitingImageObserver(image);
      obs.waitImageLoaded();
      report.setProperty("logo", image);
      report.setPropertyMarked("logo", true);

      return report;
    }
    catch (Exception e)
    {
      throw new ParseException("Failed to parse the report", e);
    }
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report  the report.
   * @param fileName target file name.
   *
   * @return true or false.
   */
  public boolean savePDF(final JFreeReport report, final String fileName)
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
      System.err.println("Writing PDF failed.");
      System.err.println(e.toString());
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
        System.err.println("Saving PDF failed.");
        System.err.println(e.toString());
      }
    }
  }

  /**
   * Demo starting point.
   *
   * @param args  ignored.
   */
  public static void main(final String[] args)
  {
    ReportConfiguration.getGlobalConfig().setDisableLogging(true);

    try
    {
      //final StraightToPDF demo =
      new StraightToPDF(System.getProperty("user.home") + "/test99.pdf");
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}