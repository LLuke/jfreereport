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
 * StyleSheetCollectionTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FnStyleSheetCollectionTest.java,v 1.7 2005/05/31 18:28:01 taqua Exp $
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
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public class FnStyleSheetCollectionTest extends TestCase
{
  private static final FunctionalityTestLib.ReportTest TEST_REPORT =
      new FunctionalityTestLib.ReportTest
          ("org/jfree/report/demo/cards/usercards.xml",
              CardDemo.createSimpleDemoModel());

  public void testCollectStyleSheets ()
  {
    final URL url = ObjectUtilities.getResource
            (TEST_REPORT.getReportDefinition(), FnStyleSheetCollectionTest.class);
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
    assertNotNull(report.getStyleSheetCollection().getStyleSheet("right-band"));
  }

  public void testCollectStyleSheetsClone ()
  {
    final URL url = ObjectUtilities.getResource
            (TEST_REPORT.getReportDefinition(), FnStyleSheetCollectionTest.class);
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
    assertNotNull(report.getStyleSheetCollection().getStyleSheet("right-band"));
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
  }

  private void assertStylesConnected (final ElementStyleSheet es, final StyleSheetCollection sc)
  {
    if (es.isGlobalDefault())
    {
      return;
    }

  }
}
