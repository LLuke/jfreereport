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
 * ------------------------------
 * PDFSaveBug.java
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
 * 21.08.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.bugs;

import java.awt.print.PageFormat;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.lowagie.text.DocWriter;
import com.lowagie.text.pdf.PdfReader;
import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.SampleData1;
import org.jfree.report.ext.junit.base.functionality.FunctionalityTestLib;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget;
import org.jfree.report.util.Log;

public class PDFSaveBug extends TestCase
{
  private static final FunctionalityTestLib.ReportTest REPORT =
      new FunctionalityTestLib.ReportTest ("/org/jfree/report/demo/security.xml", new SampleData1());

  public PDFSaveBug ()
  {
  }

  public void testSaveEncrypted() throws Exception
  {
    JFreeReport report = BugLibrary.parse(REPORT.getReportDefinition(), REPORT.getReportTableModel());
    byte[] b = createPDF(report);
    PdfReader reader = new PdfReader(b, DocWriter.getISOBytes("Duck"));
    assertTrue(reader.isEncrypted());
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report  the report.
   *
   * @return true or false.
   */
  private static byte[] createPDF(final JFreeReport report)
  {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(bout);
      final PageFormat pf = report.getDefaultPageFormat();
      final PDFOutputTarget target = new PDFOutputTarget(out, pf, true);
      target.configure(report.getReportConfiguration());
      target.open();

      final PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setOutputTarget(target);
      proc.processReport();

      target.close();
    }
    catch (Exception e)
    {
      Log.error ("Writing PDF failed.", e);
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
    return bout.toByteArray();
  }

}
