/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * SubBandParsingTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SubBandParsingTest.java,v 1.3 2005/02/22 20:28:10 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 11.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.functionality;

import java.net.URL;

import junit.framework.TestCase;
import org.jfree.report.Band;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.ObjectUtilities;

public class SubBandParsingTest extends TestCase
{
  public SubBandParsingTest()
  {
  }

  public SubBandParsingTest(final String s)
  {
    super(s);
  }

  public void testParsing () throws Exception
  {
    final URL url = ObjectUtilities.getResourceRelative
            ("resources/subband.xml", SubBandParsingTest.class);
    assertNotNull(url);
    final JFreeReport report = ReportGenerator.getInstance().parseReport(url);

    final Band band = report.getReportHeader();
    assertEquals(2, band.getElementCount());
    for (int i = 0; i < 2; i++)
    {
      final Band subband = (Band) band.getElement(i);
      assertEquals(2, subband.getElementCount());
      for (int x = 0; x < 2; x++)
      {
        final Band bandLowest =  (Band) subband.getElement(x);
        assertTrue(bandLowest.getElementCount() > 0);
      }
    }
  }

  public static void main (final String[] args) throws Exception
  {
    final URL url = ObjectUtilities.getResource
            ("resources/subband.xml", SubBandParsingTest.class);
    assertNotNull(url);
    final JFreeReport report = ReportGenerator.getInstance().parseReport(url);
    final PreviewDialog dialog = new PreviewDialog(report);
    dialog.setModal(true);
    dialog.pack();
    dialog.setVisible(true);

//    final ReportWriter rc = new ReportWriter
//        (report, "UTF-8", ReportWriter.createDefaultConfiguration(report));
//    final OutputStreamWriter owriter = new OutputStreamWriter (System.out, "UTF-8");
//    rc.write(owriter);
//    owriter.close();
  }
}
