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
 * $Id: ExportTest.java,v 1.4 2003/06/20 12:02:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.net.URL;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.util.Log;
import junit.framework.TestCase;

public class ExportTest extends TestCase
{
  public ExportTest(final String s)
  {
    super(s);
  }

  public void testConvertReport() throws Exception
  {
    try
    {
      for (int i = 0; i < FunctionalityTestLib.REPORTS.length; i++)
      {
        final URL url = this.getClass().getResource(FunctionalityTestLib.REPORTS[i].getReportDefinition());
        assertNotNull(url);
        Log.debug("Processing: " + url);
        final JFreeReport report = ReportGenerator.getInstance().parseReport(url);
        report.setData(FunctionalityTestLib.REPORTS[i].getReportTableModel());

        Log.debug("   GRAPHICS2D ..");
        assertTrue(FunctionalityTestLib.execGraphics2D(report));
        Log.debug("   PDF ..");
        assertTrue(FunctionalityTestLib.createPDF(report));
        Log.debug("   CSV ..");
        FunctionalityTestLib.createCSV(report);
        Log.debug("   PLAIN_TEXT ..");
        assertTrue(FunctionalityTestLib.createPlainText(report));
        Log.debug("   RTF ..");
        FunctionalityTestLib.createRTF(report); 
        Log.debug("   STREAM_HTML ..");
        FunctionalityTestLib.createStreamHTML(report);
        Log.debug("   EXCEL ..");
        FunctionalityTestLib.createXLS(report);
        Log.debug("   ZIP_HTML ..");
        FunctionalityTestLib.createZIPHTML(report);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }
  }


}
