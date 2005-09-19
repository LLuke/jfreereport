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
 * ReportFooterLostBug.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportFooterLostBug.java,v 1.2 2005/01/31 17:16:37 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05.11.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.bugs;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.ext.junit.TestSystem;
import org.jfree.report.ext.junit.base.functionality.DebugOutputTarget;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.util.PageFormatFactory;

public class ReportFooterLostBug extends TestCase
{
  public ReportFooterLostBug()
  {
  }

  public ReportFooterLostBug(String s)
  {
    super(s);
  }

  private JFreeReport createReport()
  {
    JFreeReport report = new JFreeReport();

    Paper p = PageFormatFactory.getInstance().createPaper(100, 100);
    report.setPageDefinition(new SimplePageDefinition
            (PageFormatFactory.getInstance().createPageFormat
                (p, PageFormat.LANDSCAPE)));

    report.getReportFooter().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0,0,90, 100), null, null, null, "Test RF"));
    report.getItemBand().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0,0,90, 100), null, null, null, "Test IB"));

    report.setData(new DefaultTableModel(1,1));
    return report;
  }

  public void testBandLayout () throws Exception
  {
    JFreeReport report = createReport();
    OutputTarget ot = new DebugOutputTarget();
    assertEquals("BandLayout", new Rectangle2D.Float(0,0, 100, 100),
        BandLayoutManagerUtil.doLayout(report.getReportFooter(), ot, 100, 100));
  }

  public void testReportSize () throws Exception
  {
    JFreeReport report = createReport();
    PageableReportProcessor p = new PageableReportProcessor(report);
    p.setOutputTarget(new DebugOutputTarget());
    p.repaginate();
    assertEquals("ReportStateList.Size", 2, p.getPageCount());
  }

  public static void main (String[] args) throws Exception
  {
    TestSystem.showPreview(new ReportFooterLostBug().createReport());
    System.exit(0);
  }
}
