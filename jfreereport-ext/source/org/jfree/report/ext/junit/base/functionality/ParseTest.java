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
 * ParseTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ParseTest.java,v 1.7 2005/09/07 11:24:09 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.functionality;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.XmlDemoHandler;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.Log;

public class ParseTest extends TestCase
{

  public ParseTest(final String s)
  {
    super(s);
  }

  /**
   * Parses, clones and serializes the reports ..
   * @throws Exception
   */
  public void testParseReport() throws Exception
  {
    XmlDemoHandler[] handlers = FunctionalityTestLib.getAllXmlDemoHandlers();
    for (int i = 0; i < handlers.length; i++)
    {
      XmlDemoHandler handler = handlers[i];
      final URL url = handler.getReportDefinitionSource();
      assertNotNull("Failed to locate " + url, url);
      try
      {
        final JFreeReport report = ReportGenerator.getInstance().parseReport(url);
        assertNotNull(report);
        report.clone();

        final ByteArrayOutputStream bo = new ByteArrayOutputStream();
        final ObjectOutputStream out = new ObjectOutputStream(bo);
        out.writeObject(report);

        final ObjectInputStream oin = new ObjectInputStream
          (new ByteArrayInputStream(bo.toByteArray()));
        final JFreeReport e2 = (JFreeReport) oin.readObject();
        assertNotNull(e2); // cannot assert equals, as this is not implemented.

      }
      catch (Exception e)
      {
        Log.debug("Failed to parse " + url, e);
        fail();
      }
    }
  }
}
