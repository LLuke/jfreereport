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
 * $Id: FnStyleSheetCollectionTest.java,v 1.10 2005/09/19 13:34:24 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 23.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.functionality;

import junit.framework.TestCase;
import org.jfree.report.Band;
import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.cards.SimpleCardDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCollection;

public class FnStyleSheetCollectionTest extends TestCase
{
  public FnStyleSheetCollectionTest()
  {
  }

  public FnStyleSheetCollectionTest(String string)
  {
    super(string);
  }

  public void testCollectStyleSheets () throws ReportDefinitionException
  {
    final SimpleCardDemoHandler cardDemoHandler = new SimpleCardDemoHandler();
    JFreeReport report = cardDemoHandler.createReport();
    assertStyleCollectionConnected(report);
    assertNotNull(report.getStyleSheetCollection().getStyleSheet("right-band"));
  }

  public void testCollectStyleSheetsClone () throws ReportDefinitionException,
          CloneNotSupportedException
  {
    final SimpleCardDemoHandler cardDemoHandler = new SimpleCardDemoHandler();
    JFreeReport report = cardDemoHandler.createReport();
    report = (JFreeReport) report.clone();

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
    // todo
  }

  private void assertStylesConnected (final ElementStyleSheet es, final StyleSheetCollection sc)
  {
    if (es.isGlobalDefault())
    {
      return;
    }

  }
}
