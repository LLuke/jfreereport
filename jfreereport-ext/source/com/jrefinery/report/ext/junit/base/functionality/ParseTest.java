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
 * $Id$
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

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.io.ReportGenerator;
import junit.framework.TestCase;

public class ParseTest extends TestCase
{
  public ParseTest(String s)
  {
    super(s);
  }

  public final static String [] REPORTS = {
    "/com/jrefinery/report/demo/report1.xml",
    "/com/jrefinery/report/demo/report1a.xml",
    "/com/jrefinery/report/demo/report2.xml",
    "/com/jrefinery/report/demo/report2a.xml",
    "/com/jrefinery/report/demo/report2b.xml",
    "/com/jrefinery/report/demo/report2c.xml",
    "/com/jrefinery/report/demo/report2d.xml",
    "/com/jrefinery/report/demo/report3.xml",
    "/com/jrefinery/report/demo/report4.xml",
    "/com/jrefinery/report/demo/report5.xml",
    "/com/jrefinery/report/demo/lgpl.xml",
    "/com/jrefinery/report/demo/OpenSourceDemo.xml",
    "/com/jrefinery/report/demo/PercentageDemo.xml",
    "/com/jrefinery/report/demo/shape-and-drawable.xml",
    "/com/jrefinery/report/demo/swing-icons.xml",
    "/com/jrefinery/report/demo/cards/usercards.xml",
    "/com/jrefinery/report/demo/shape-and-drawable.xml",
    "/com/jrefinery/report/io/ext/factory/objects/ObjectReferenceReport.xml",
    "/com/jrefinery/report/io/ext/factory/stylekey/StyleKeyReferenceReport.xml"
  };

  public void testParseReport () throws Exception
  {
    for (int i = 0; i < REPORTS.length; i++)
    {
      URL url = this.getClass().getResource(REPORTS[i]);
      assertNotNull(url);
      try
      {
        ReportGenerator.getInstance().parseReport(url);
      }
      catch (Exception e)
      {
        Log.debug ("Failed to parse " + url, e);
        fail();
      }
    }
  }

  public void testParseSerializeReport () throws Exception
  {
    for (int i = 0; i < REPORTS.length; i++)
    {
      URL url = this.getClass().getResource(REPORTS[i]);
      assertNotNull(url);

      JFreeReport report = ReportGenerator.getInstance().parseReport(url);

      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream (bo);
      out.writeObject(report);

      ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bo.toByteArray()));
      JFreeReport e2 = (JFreeReport) oin.readObject();
      assertNotNull(e2); // cannot assert equals, as this is not implemented.

    }
  }
}
