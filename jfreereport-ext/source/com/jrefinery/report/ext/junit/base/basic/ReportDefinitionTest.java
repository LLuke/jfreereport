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
 * ReportDefinitionTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDefinitionTest.java,v 1.3 2003/06/16 15:34:34 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.basic;

import junit.framework.TestCase;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleKey;

public class ReportDefinitionTest extends TestCase
{
  private StyleKey testKey =
      StyleKey.getStyleKey("ReportDefinitionTest", String.class);

  public ReportDefinitionTest(final String s)
  {
    super(s);
  }

  public void testReport () throws Exception
  {
    final JFreeReport report = new JFreeReport();
    final ElementStyleSheet es = new ElementStyleSheet("test");
    es.setStyleProperty(testKey, "Hello World!");
    report.getReportHeader().getStyle().addParent(es);
    assertEquals(report.getReportHeader().getStyle().getStyleProperty(testKey), "Hello World!");

    final ReportDefinition rd = new ReportDefinition(report);
    assertEquals(rd.getReportHeader().getStyle().getStyleProperty(testKey), "Hello World!");

    es.setStyleProperty(testKey, "Hello Little Green Man!");
    assertNotSame(rd.getReportHeader().getStyle().getStyleProperty(testKey), "Hello World!");

  }

}
