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
 * JFreeReportTest.java
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

package com.jrefinery.report.ext.junit.base.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jrefinery.report.JFreeReport;
import junit.framework.TestCase;

public class JFreeReportTest extends TestCase
{
  public JFreeReportTest(String s)
  {
    super(s);
  }

  public void testCreate () throws Exception
  {
    JFreeReport report = new JFreeReport();
    assertNotNull(report.getDefaultPageFormat());
    assertNotNull(report.getExpressions());
    assertNotNull(report.getFunctions());
    assertNotNull(report.getGroups());
    assertEquals(report.getGroupCount(), 1);
    assertNotNull(report.getItemBand());
    assertNotNull(report.getName());
    assertNotNull(report.getPageFooter());
    assertNotNull(report.getPageHeader());
    assertNotNull(report.getProperties());
    assertNotNull(report.getReportConfiguration());
    assertNotNull(report.getReportFooter());
    assertNotNull(report.getReportHeader());
    assertNotNull(report.getGroup(0)); // the default group must be defined ...
    assertNotNull(report.clone());
  }

  public void testSerialize () throws Exception
  {
    JFreeReport report = new JFreeReport();
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream (bo);
    out.writeObject(report);

    ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bo.toByteArray()));
    JFreeReport e2 = (JFreeReport) oin.readObject();
    assertNotNull(e2); // cannot assert equals, as this is not implemented.
  }

  
}
