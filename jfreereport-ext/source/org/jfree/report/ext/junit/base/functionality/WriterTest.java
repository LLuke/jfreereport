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
 * WriterTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: WriterTest.java,v 1.5 2003/07/03 16:06:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.functionality;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.modules.parser.ext.writer.ReportConverter;
import org.jfree.report.util.Log;
import junit.framework.TestCase;
import org.xml.sax.InputSource;

public class WriterTest extends TestCase
{

  public WriterTest(final String s)
  {
    super(s);
  }

  public void testConvertReport() throws Exception
  {
    final ReportConverter rc = new ReportConverter();
    for (int i = 0; i < FunctionalityTestLib.REPORTS.length; i++)
    {
      final URL url = this.getClass().getResource(FunctionalityTestLib.REPORTS[i].getReportDefinition());
      assertNotNull(url);
      final ByteArrayOutputStream bo = new ByteArrayOutputStream();
      try
      {
        rc.convertReport(url, url,
            new OutputStreamWriter (bo, "UTF-16"), "UTF-16");
        final ByteArrayInputStream bin = new ByteArrayInputStream(bo.toByteArray());
        ReportGenerator.getInstance().parseReport(new InputSource(bin), url);
      }
      catch (Exception e)
      {
        Log.debug("Failed to write or parse " + url, e);
        Log.debug (bo.toString("UTF-16"));
        fail();
      }
    }
  }

}
