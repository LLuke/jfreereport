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
 * GroupPageBreakTest.java
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
 * 13.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.awt.geom.Rectangle2D;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.pageable.output.G2OutputTarget;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.ReportStateList;
import junit.framework.TestCase;

public class GroupPageBreakTest extends TestCase
{
  public GroupPageBreakTest()
  {
  }

  public GroupPageBreakTest(String s)
  {
    super(s);
  }

  private JFreeReport getReport() throws Exception
  {
    JFreeReport report = new JFreeReport();
    report.getReportHeader().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "Text"));

    report.getReportFooter().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "Text"));

    report.getPageHeader().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "Text"));

    report.getPageFooter().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "Text"));

    report.getItemBand().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "Text"));

    report.getGroup(0).getHeader().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "Text"));

    report.getGroup(0).getFooter().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "Text"));

    report.getGroup(0).getHeader().getStyle().
        setBooleanStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE,true);
    report.getGroup(0).getFooter().getStyle().
        setBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER,true);

    return report;
  }

  public void testPageCount () throws Exception
  {
    JFreeReport report = getReport();

    G2OutputTarget target =
        new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), report.getDefaultPageFormat());
    target.configure(report.getReportConfiguration());
    target.open();

    PageableReportProcessor proc = new PageableReportProcessor(report);
    proc.setOutputTarget(target);
    ReportStateList rsl = proc.repaginate();
    assertEquals(rsl.size(), 3);
    target.close();
  }


  public void testSmallPageCount () throws Exception
  {
    JFreeReport report = getReport();
    report.setReportFooter(new ReportFooter());
    report.setReportHeader(new ReportHeader());

    G2OutputTarget target =
        new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), report.getDefaultPageFormat());
    target.configure(report.getReportConfiguration());
    target.open();

    PageableReportProcessor proc = new PageableReportProcessor(report);
    proc.setOutputTarget(target);
    ReportStateList rsl = proc.repaginate();
    assertEquals(1, rsl.size());
    target.close();
  }
}
