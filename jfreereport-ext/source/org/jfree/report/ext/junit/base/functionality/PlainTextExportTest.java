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
 * PlainTextExportTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextExportTest.java,v 1.4 2003/11/01 19:57:03 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 29.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.functionality;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.SampleData1;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PrinterDriver;
import org.jfree.report.modules.output.pageable.plaintext.TextFilePrinterDriver;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;

public class PlainTextExportTest extends TestCase
{
  private static final FunctionalityTestLib.ReportTest TEST_REPORT =
      new FunctionalityTestLib.ReportTest ("/org/jfree/report/demo/report1.xml",
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
    final PrinterDriver pc = new TextFilePrinterDriver(fout, 10, 15);
    final PlainTextOutputTarget target = 
      new PlainTextOutputTarget (pc);
    target.setEncoding(encoding);
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
