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
 * $Id: WriterTest.java,v 1.2 2003/06/10 18:17:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.io.ext.writer.ReportConverter;
import com.jrefinery.report.util.Log;
import junit.framework.TestCase;
import org.xml.sax.InputSource;

public class WriterTest extends TestCase
{
  public final static String[] REPORTS = ParseTest.REPORTS;

  public WriterTest(String s)
  {
    super(s);
  }

  public void testConvertReport() throws Exception
  {
    ReportConverter rc = new ReportConverter();
    for (int i = 0; i < REPORTS.length; i++)
    {
      URL url = this.getClass().getResource(REPORTS[i]);
      assertNotNull(url);
      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      try
      {
        rc.convertReport(url, url,
            new OutputStreamWriter (bo, "UTF-16"), "UTF-16");
        ByteArrayInputStream bin = new ByteArrayInputStream(bo.toByteArray());
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
