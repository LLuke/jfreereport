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
 * PlainTextExportTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 29.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import junit.framework.TestCase;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.output.PrinterCommandSet;
import com.jrefinery.report.targets.pageable.output.EpsonPrinterCommandSet;
import com.jrefinery.report.targets.pageable.output.PlainTextOutputTarget;
import com.jrefinery.report.util.NullOutputStream;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.demo.SampleData1;
import com.jrefinery.report.io.ReportGenerator;

public class PlainTextExportTest extends TestCase
{
  private static final FunctionalityTestLib.ReportTest TEST_REPORT =
      new FunctionalityTestLib.ReportTest ("/com/jrefinery/report/demo/report1.xml",
          new SampleData1());

  public PlainTextExportTest()
  {
  }

  public PlainTextExportTest(final String s)
  {
    super(s);
  }

  private String exportReport (final JFreeReport report, final String encoding) throws Exception
  {
    final ByteArrayOutputStream bo = new ByteArrayOutputStream();
    final PageableReportProcessor pr = new PageableReportProcessor(report);
    final OutputStream fout = new BufferedOutputStream(bo);
    final PrinterCommandSet pc = new PrinterCommandSet(fout, report.getDefaultPageFormat(), 10, 15);
    final PlainTextOutputTarget target = new PlainTextOutputTarget(report.getDefaultPageFormat(), pc);
    target.setDocumentEncoding(encoding);

    pr.setOutputTarget(target);
    target.open();
    pr.processReport();
    target.close();
    fout.close();
    final byte[] data = bo.toByteArray();
    return new String (data, encoding);
  }

  public void testExport ()
    throws Exception
  {
    final URL url = this.getClass().getResource(TEST_REPORT.getReportDefinition());
    assertNotNull(url);
    JFreeReport report = null;
    try
    {
      report = ReportGenerator.getInstance().parseReport(url);
      report.setData(TEST_REPORT.getReportTableModel());
    }
    catch (Exception e)
    {
      Log.debug("Failed to parse " + url, e);
      fail();
    }
    final String utf16 = exportReport(report, "UTF-16");
    final String utf8 = exportReport(report, "UTF-8");
    assertEquals(utf8, utf16);
  }
}
