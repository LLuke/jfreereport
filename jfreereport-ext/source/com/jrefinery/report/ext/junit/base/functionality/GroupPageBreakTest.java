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
 * $Id: GroupPageBreakTest.java,v 1.1 2003/06/13 22:58:25 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.awt.geom.Rectangle2D;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.Group;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.preview.PreviewDialog;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.ReportStateList;
import com.jrefinery.report.targets.pageable.output.G2OutputTarget;
import com.jrefinery.report.targets.style.BandStyleSheet;
import junit.framework.TestCase;

public class GroupPageBreakTest extends TestCase
{
  public GroupPageBreakTest()
  {
  }

  public GroupPageBreakTest(final String s)
  {
    super(s);
  }

  private JFreeReport getReport() throws Exception
  {
    final JFreeReport report = new JFreeReport();
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
        setBooleanStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE, true);
    report.getGroup(0).getFooter().getStyle().
        setBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER, true);

    return report;
  }

  public void testPageCount() throws Exception
  {
    final JFreeReport report = getReport();

    final G2OutputTarget target =
        new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), report.getDefaultPageFormat());
    target.configure(report.getReportConfiguration());
    target.open();

    final PageableReportProcessor proc = new PageableReportProcessor(report);
    proc.setOutputTarget(target);
    final ReportStateList rsl = proc.repaginate();
    assertEquals(rsl.size(), 3);
    target.close();
  }


  public void testSmallPageCount() throws Exception
  {
    final JFreeReport report = getReport();
    report.setReportFooter(new ReportFooter());
    report.setReportHeader(new ReportHeader());

    final G2OutputTarget target =
        new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), report.getDefaultPageFormat());
    target.configure(report.getReportConfiguration());
    target.open();

    final PageableReportProcessor proc = new PageableReportProcessor(report);
    proc.setOutputTarget(target);
    final ReportStateList rsl = proc.repaginate();
    assertEquals(1, rsl.size());
    target.close();
  }

  public JFreeReport getReportTest2()
  {
    /**
     * <groups>
     * <group name="CMR">
     * <fields>
     * <field>CMR</field>
     * </fields>
     * <groupheader height="20">
     * </groupheader>
     * </group>
     *
     * <!-- Second group-->
     * <group name="OC" >
     * <fields>
     * <field>CMR</field>
     * <field>OC</field>
     * </fields>
     *
     * <groupheader height="20" repeat="true">
     * ......................
     * </groupheader>
     *
     * <groupfooter height="10" pagebreak-after-print="true">
     * ....................
     * </groupfooter>
     * </group>
     * </groups>
     *
     * <items height="95" fontname="Serif" fontstyle="plain" fontsize="10">
     */
    final JFreeReport report = new JFreeReport();
    final Group cmr = new Group();
    cmr.addField("CMR");
    cmr.getHeader().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "CMR header"));

    report.addGroup(cmr);

    final Group oc = new Group();
    oc.addField("CMR");
    oc.addField("OC");
    oc.getHeader().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "CMR-OC header"));
    oc.getHeader().getStyle().setBooleanStyleProperty(BandStyleSheet.REPEAT_HEADER, true);

    oc.getFooter().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "CMR-OC footer"));
    oc.getFooter().getStyle().setBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER, true);
    report.addGroup(oc);

    report.getItemBand().addElement(ItemFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT.getOldAlignment(), null, "ItemBand"));
    report.setData(createTest2Model());
    return report;
  }

  public TableModel createTest2Model ()
  {
    final Object[][] data = {
      { "cmr1", "oc1" },
      { "cmr1", "oc2" },
      { "cmr2", "oc1" },
      { "cmr2", "oc2" },
    };

    final String[] names = new String[]{
      "CMR", "OC"
    };
    final DefaultTableModel model = new DefaultTableModel(data, names);
    return model;
  }

  public void testGroupReport2 () throws Exception
  {
    final JFreeReport report = getReportTest2();
    report.setReportFooter(new ReportFooter());
    report.setReportHeader(new ReportHeader());

    final G2OutputTarget target =
        new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), report.getDefaultPageFormat());
    target.configure(report.getReportConfiguration());
    target.open();

    final PageableReportProcessor proc = new PageableReportProcessor(report);
    proc.setOutputTarget(target);
    final ReportStateList rsl = proc.repaginate();
    assertEquals(4, rsl.size());
    target.close();
  }

  public void VtestGroupReport2Visual () throws Exception
  {
    final JFreeReport report = getReportTest2();
    report.setReportFooter(new ReportFooter());
    report.setReportHeader(new ReportHeader());

    final PreviewDialog dialog = new PreviewDialog(report);
    dialog.setModal(true);
    dialog.pack();
    dialog.setVisible(true);

  }
}
