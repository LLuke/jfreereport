/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * AppendedExcelReport.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AppendedExcelReport.java,v 1.3 2005/09/21 12:00:17 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27-Aug-2005 : Initial version
 *
 */
package org.jfree.report.demo.nogui;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.opensource.OpenSourceXMLDemoHandler;
import org.jfree.report.demo.world.CountryReportXMLDemoHandler;
import org.jfree.report.modules.output.table.xls.AppendingExcelProcessor;
import org.jfree.util.Log;

/**
 * A demo that shows how to append multiple reports to a single Excel workbook.
 *
 * @author Thomas Morgner
 */
public class AppendedExcelReport
{
  private String filename;

  private AppendedExcelReport(final String filename)
  {
    this.filename = filename;
  }

  private void perform ()
          throws ReportDefinitionException, ReportProcessingException, IOException
  {
    final JFreeReport report = new OpenSourceXMLDemoHandler().createReport();
    final JFreeReport second = new CountryReportXMLDemoHandler().createReport();

    final AppendingExcelProcessor pr = new AppendingExcelProcessor(report);
    pr.addReport(second);

    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setOutputStream(fout);
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
    JFreeReportBoot.getInstance().start();
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
      new AppendedExcelReport(folder + "/Appending-Demo").perform();
      System.exit(0);
    }
    catch (Exception e)
    {
      Log.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}
