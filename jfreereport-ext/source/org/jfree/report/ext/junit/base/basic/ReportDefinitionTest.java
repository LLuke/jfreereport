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
 * ReportDefinitionTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDefinitionTest.java,v 1.4 2003/11/01 19:57:02 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDefinition;
import org.jfree.report.states.ReportDefinitionImpl;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;

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
    final ElementStyleSheet es = report.getStyleSheetCollection().createStyleSheet("test");
    es.setStyleProperty(testKey, "Hello World!");
    report.getReportHeader().getStyle().addParent(es);
    assertEquals(report.getReportHeader().getStyle().getStyleProperty(testKey), "Hello World!");

    final ReportDefinition rd = new ReportDefinitionImpl(report);
    assertEquals(rd.getReportHeader().getStyle().getStyleProperty(testKey), "Hello World!");

    // redefining the outside stylesheet must not change the stylesheet 
    // inside the report definition.
    // check for deep-cloning!
    es.setStyleProperty(testKey, "Hello Little Green Man!");
    assertSame(rd.getReportHeader().getStyle().getStyleProperty(testKey), "Hello World!");

  }

}
