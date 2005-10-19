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
 * $Id: StraightToPlainText.java,v 1.2 2005/09/07 14:23:49 taqua Exp $
 *
 * Changes
 * -------
 * 13-Dec-2002 : Version 1 (DG);
 *
 */

package org.jfree.report.demo.nogui;

import java.net.URL;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.opensource.OpenSourceProjects;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.TextFilePrinterDriver;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.ParseException;

/**
 * A demonstration that shows how to generate a report and save it to PDF without
 * displaying the print preview or the PDF save-as dialog.
 *
 * @author David Gilbert
 */
public class StraightToPlainText
{

  /**
   * Creates a new demo application.
   *
   * @param filename the output filename.
   * @throws org.jfree.xml.ParseException if the report could not be parsed.
   */
  public StraightToPlainText (final String filename)
          throws ParseException
  {
    final URL in = ObjectUtilities.getResource
            ("org/jfree/report/demo/opensource/opensource.xml", StraightToPlainText.class);
    final JFreeReport report = parseReport(in);
    final TableModel data = new OpenSourceProjects();
    report.setData(data);
    savePlainText(report, filename);
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL the template location.
   * @return a report.
   *
   * @throws org.jfree.xml.ParseException if the report could not be parsed.
   */
  private JFreeReport parseReport (final URL templateURL)
          throws ParseException
  {
    final ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      final JFreeReport report = generator.parseReport(templateURL);
      // plain text does not support images, so we do not care about the logo ..
      report.setProperty("logo", null);
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
   * @param report   the report.
   * @param fileName target file name.
   * @return true or false.
   */
  public boolean savePlainText (final JFreeReport report, final String fileName)
  {
    try
    {
      final BufferedOutputStream bout = new BufferedOutputStream
              (new FileOutputStream(fileName));

      final TextFilePrinterDriver tf = new TextFilePrinterDriver(bout, 10, 10);
      tf.setEndOfLine(new char[]{'\n'});
      tf.setEndOfPage(new char[]{'\n'});
      final PlainTextOutputTarget target = new PlainTextOutputTarget(tf);
      target.configure(report.getReportConfiguration());
      target.open();

      final PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setOutputTarget(target);
      proc.processReport();
      target.close();
      bout.close();
      return true;
    }
    catch (Exception e)
    {
      Log.error ("Writing PlainText failed.", e);
      return false;
    }
  }

  /**
   * Demo starting point.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    try
    {
      //final StraightToPDF demo =
      new StraightToPlainText(System.getProperty("user.home") + "/OpenSource-Demo.txt");
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}