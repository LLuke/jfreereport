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
 * StyleSheetCollectionTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FnStyleSheetCollectionTest.java,v 1.1 2003/07/11 20:07:56 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 23.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.functionality;

import java.net.URL;
import java.util.List;

import junit.framework.TestCase;
import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.cards.CardDemo;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.util.Log;

public class FnStyleSheetCollectionTest extends TestCase
{
  private static final FunctionalityTestLib.ReportTest TEST_REPORT =
      new FunctionalityTestLib.ReportTest
          ("/org/jfree/report/demo/cards/usercards.xml",
              CardDemo.createSimpleDemoModel());

  public void testCollectStyleSheets ()
  {
    final URL url = this.getClass().getResource(TEST_REPORT.getReportDefinition());
    assertNotNull(url);
    JFreeReport report = null;
    try
    {
      report = ReportGenerator.getInstance().parseReport(url);
    }
    catch (Exception e)
    {
      Log.debug("Failed to parse " + url, e);
      fail();
    }

    assertStyleCollectionConnected(report);
/*
    Iterator it = report.getStyleSheetCollection().keys();
    while (it.hasNext())
    {
      Log.debug (it.next());
    }
*/
    assertNotNull(report.getStyleSheetCollection().getFirst("right-band"));
  }

  public void testCollectStyleSheetsClone ()
  {
    final URL url = this.getClass().getResource(TEST_REPORT.getReportDefinition());
    assertNotNull(url);
    JFreeReport report = null;
    try
    {
      report = ReportGenerator.getInstance().parseReport(url);
      report = (JFreeReport) report.clone();
    }
    catch (Exception e)
    {
      Log.debug("Failed to parse " + url, e);
      fail();
    }

    assertStyleCollectionConnected(report);
/*
    Iterator it = report.getStyleSheetCollection().keys();
    while (it.hasNext())
    {
      Log.debug (it.next());
    }
*/
    assertNotNull(report.getStyleSheetCollection().getFirst("right-band"));
  }


  private void assertStyleCollectionConnected(final JFreeReport report)
  {
    final StyleSheetCollection con = report.getStyleSheetCollection();
    assertStyleCollectionConnected (report.getPageFooter (), con);
    assertStyleCollectionConnected (report.getPageHeader (), con);
    assertStyleCollectionConnected (report.getReportFooter (), con);
    assertStyleCollectionConnected (report.getReportHeader (), con);
    assertStyleCollectionConnected (report.getItemBand (), con);

    final int groupCount = report.getGroupCount ();
    for (int i = 0; i < groupCount; i++)
    {
      final Group g = report.getGroup (i);
      assertStyleCollectionConnected (g.getFooter (), con);
      assertStyleCollectionConnected (g.getHeader (), con);
    }
  }

  private void assertStyleCollectionConnected(final Band band, final StyleSheetCollection sc)
  {
    assertTrue(band.getName(), band.getStyleSheetCollection() == sc);
    assertStylesConnected(band.getStyle(), sc);
    assertStylesConnected(band.getBandDefaults(), sc);
    final Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      assertTrue(elements[i].getStyleSheetCollection() == sc);
      if (elements[i] instanceof Band)
      {
        //Log.debug ("Band: " + elements[i].getName());
        assertStyleCollectionConnected((Band) elements[i], sc);
      }
      else
      {
        //Log.debug ("Element: " + elements[i].getName());
        assertTrue(elements[i].getName(), elements[i].getStyleSheetCollection() == sc);
        assertStylesConnected(elements[i].getStyle(), sc);
      }
    }
  }

  private void assertStylesConnected (final ElementStyleSheet es, final StyleSheetCollection sc)
  {
    if (es.isGlobalDefault())
      return;

    assertTrue(es.getName() + " " + es.hashCode() + " - " +
        es.getStyleSheetCollection(), es.getStyleSheetCollection() == sc);
    List parents = es.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      assertStylesConnected((ElementStyleSheet) parents.get(i), sc);
    }
    parents = es.getDefaultParents();
    for (int i = 0; i < parents.size(); i++)
    {
      assertStylesConnected((ElementStyleSheet) parents.get(i), sc);
    }
  }
}
