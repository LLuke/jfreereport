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
 * ParseTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ParseTest.java,v 1.2 2003/06/10 18:17:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.demo.OpenSourceProjects;
import com.jrefinery.report.demo.PercentageDemo;
import com.jrefinery.report.demo.SampleData1;
import com.jrefinery.report.demo.SampleData2;
import com.jrefinery.report.demo.SampleData3;
import com.jrefinery.report.demo.SampleData4;
import com.jrefinery.report.demo.SwingIconsDemoTableModel;
import com.jrefinery.report.demo.cards.CardDemo;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceReferenceGenerator;
import com.jrefinery.report.io.ext.factory.objects.ObjectReferenceGenerator;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyReferenceGenerator;
import com.jrefinery.report.util.Log;
import junit.framework.TestCase;

public class ParseTest extends TestCase
{

  public ParseTest(String s)
  {
    super(s);
  }

  public void testParseReport() throws Exception
  {
    for (int i = 0; i < FunctionalityTestLib.REPORTS.length; i++)
    {
      URL url = this.getClass().getResource(FunctionalityTestLib.REPORTS[i].getReportDefinition());
      assertNotNull(url);
      try
      {
        ReportGenerator.getInstance().parseReport(url);
      }
      catch (Exception e)
      {
        Log.debug("Failed to parse " + url, e);
        fail();
      }
    }
  }

  public void testParseSerializeReport() throws Exception
  {
    for (int i = 0; i < FunctionalityTestLib.REPORTS.length; i++)
    {
      URL url = this.getClass().getResource(FunctionalityTestLib.REPORTS[i].getReportDefinition());
      assertNotNull(url);

      JFreeReport report = ReportGenerator.getInstance().parseReport(url);

      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bo);
      out.writeObject(report);

      ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bo.toByteArray()));
      JFreeReport e2 = (JFreeReport) oin.readObject();
      assertNotNull(e2); // cannot assert equals, as this is not implemented.

    }
  }
}
