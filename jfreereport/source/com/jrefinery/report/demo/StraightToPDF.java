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
 * ------------------
 * StraightToPDF.java
 * ------------------
 * (C)opyright 2002, 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: StraightToPDF.java,v 1.7 2003/03/18 17:14:19 taqua Exp $
 *
 * Changes
 * -------
 * 13-Dec-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo;

import java.awt.print.PageFormat;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.output.PDFOutputTarget;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;

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
   */
  public StraightToPDF(String filename)
  {
    URL in  = getClass().getResource("/com/jrefinery/report/demo/OpenSourceDemo.xml");
    JFreeReport report = parseReport(in);
    TableModel data = new OpenSourceProjects();
    report.setData(data);
    savePDF(report, filename);
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
      Log.error ("Failed to parse", e);
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
  public boolean savePDF(JFreeReport report, String fileName)
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
  public static void main(String args[])
  {
    ReportConfiguration.getGlobalConfig().setDisableLogging(true);
    ReportConfiguration.getGlobalConfig().setPDFTargetAutoInit(false);
    StraightToPDF demo = new StraightToPDF(System.getProperty("user.home") + "/test99.pdf");
    System.exit(0);
  }

}