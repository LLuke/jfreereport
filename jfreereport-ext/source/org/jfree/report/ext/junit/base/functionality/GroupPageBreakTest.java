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
 * $Id: GroupPageBreakTest.java,v 1.2 2003/07/03 16:06:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.functionality;

import java.awt.geom.Rectangle2D;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import junit.framework.TestCase;
import org.jfree.report.ElementAlignment;
import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportFooter;
import org.jfree.report.ReportHeader;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.modules.output.pageable.graphics.G2OutputTarget;
import org.jfree.report.style.BandStyleSheet;

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
    report.getReportHeader().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getReportFooter().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getPageHeader().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getPageFooter().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getItemBand().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getGroup(0).getHeader().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

    report.getGroup(0).getFooter().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "Text"));

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

    final org.jfree.report.modules.output.pageable.base.PageableReportProcessor proc = new org.jfree.report.modules.output.pageable.base.PageableReportProcessor(report);
    proc.setOutputTarget(target);
    final org.jfree.report.modules.output.pageable.base.ReportStateList rsl = proc.repaginate();
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

    final org.jfree.report.modules.output.pageable.base.PageableReportProcessor proc = new org.jfree.report.modules.output.pageable.base.PageableReportProcessor(report);
    proc.setOutputTarget(target);
    final org.jfree.report.modules.output.pageable.base.ReportStateList rsl = proc.repaginate();
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
    cmr.getHeader().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "CMR header"));

    report.addGroup(cmr);

    final Group oc = new Group();
    oc.addField("CMR");
    oc.addField("OC");
    oc.getHeader().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "CMR-OC header"));
    oc.getHeader().getStyle().setBooleanStyleProperty(BandStyleSheet.REPEAT_HEADER, true);

    oc.getFooter().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "CMR-OC footer"));
    oc.getFooter().getStyle().setBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER, true);
    report.addGroup(oc);

    report.getItemBand().addElement(LabelElementFactory.createLabelElement
        (null, new Rectangle2D.Float(0, 0, 150, 20), null,
            ElementAlignment.LEFT, null, "ItemBand"));
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

    final org.jfree.report.modules.output.pageable.base.PageableReportProcessor proc = new org.jfree.report.modules.output.pageable.base.PageableReportProcessor(report);
    proc.setOutputTarget(target);
    final org.jfree.report.modules.output.pageable.base.ReportStateList rsl = proc.repaginate();
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
