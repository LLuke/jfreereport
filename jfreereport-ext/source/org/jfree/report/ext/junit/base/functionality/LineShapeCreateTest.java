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
 * LineShapeCreateTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LineShapeCreateTest.java,v 1.5 2003/11/01 19:57:03 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.functionality;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import junit.framework.TestCase;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportHeader;
import org.jfree.report.ShapeElement;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.xml.ParserUtil;

public class LineShapeCreateTest extends TestCase
{
  private class LSDebugOutputTarget extends DebugOutputTarget
  {
    public LSDebugOutputTarget()
    {
    }

    private int shapeCount;

    /**
     * Opens the target.
     *
     * @throws OutputTargetException if there is some problem opening the target.
     */
    public void open() throws OutputTargetException
    {
      super.open();
      shapeCount = 0;
    }

    /**
     * Draws a shape relative to the current position.
     *
     * @param shape  the shape to draw.
     */
    public void drawShape(final Shape shape)
    {
      shapeCount++;
    }

    public int getShapeCount()
    {
      return shapeCount;
    }

    public void setShapeCount(final int shapeCount)
    {
      this.shapeCount = shapeCount;
    }
  }

  public LineShapeCreateTest (final String s)
  {
    super(s);
  }

  public JFreeReport getReport()
  {
    final JFreeReport report = new JFreeReport();
    final ReportHeader header = report.getReportHeader();
    final Color c = Color.red;
    for (int i = 10; i < 100; i += 10)
    {
      header.addElement(
          LabelElementFactory.createLabelElement(
              "Asse" + Integer.toString(i),
              new Rectangle2D.Float(340 + i * 4, 40, 20, 10),
              null,
              ElementAlignment.CENTER, ElementAlignment.MIDDLE,
              new FontDefinition("Serif", 10, (i == 50), false, false, false),
              Integer.toString(i)
          )
      );
      header.addElement(
          StaticShapeElementFactory.createShapeElement(
              "tick" + Integer.toString(i),
              c,
              new BasicStroke(0.25f),
              new Line2D.Float(350 + i * 4, 52, 350 + i * 4, 49),
              true, false
          )
      );
    }

    header.addElement(
        StaticShapeElementFactory.createShapeElement(
            "linea",
            null,
            new BasicStroke(0.25f),
            new Line2D.Float(390, 52, 710, 52),
            true, false
        )
    );

    return report;
  }

  public void testDoReport() throws Exception
  {
    final JFreeReport report = getReport();
    final PageFormatFactory pff = PageFormatFactory.getInstance();
    final Paper paper = pff.createPaper("A0");
    final PageFormat pf = pff.createPageFormat(paper, PageFormat.PORTRAIT);
    assertEquals((int) pf.getHeight(), PageFormatFactory.A0[1]);
    assertEquals((int) pf.getWidth(), PageFormatFactory.A0[0]);
    report.setPageDefinition(new SimplePageDefinition(pf));
    final LSDebugOutputTarget lsd = new LSDebugOutputTarget();
    final PageableReportProcessor prp = new PageableReportProcessor(report);
    prp.setOutputTarget(lsd);
    lsd.open();
    prp.processReport();
    assertEquals(lsd.getShapeCount(), 10);
  }

  public void testDoReport2() throws Exception
  {
    final JFreeReport report = new JFreeReport();
    final Line2D line = new Line2D.Float(40, 70, 140, 70);
    final ShapeElement element = StaticShapeElementFactory.createShapeElement(
        null,
        Color.black,
        ParserUtil.parseStroke("1"),
        line, true, false);
    report.getReportHeader().addElement(element);

    BandLayoutManagerUtil.doLayout(report.getReportHeader(), new DefaultLayoutSupport(), 500, 200);
    assertEquals(new Rectangle2D.Float(0, 0, 500, 70),
        BandLayoutManagerUtil.getBounds(report.getReportHeader(), null));

    final PageFormatFactory pff = PageFormatFactory.getInstance();
    final Paper paper = pff.createPaper("A0");
    final PageFormat pf = pff.createPageFormat(paper, PageFormat.PORTRAIT);
    assertEquals((int) pf.getHeight(), PageFormatFactory.A0[1]);
    assertEquals((int) pf.getWidth(), PageFormatFactory.A0[0]);
    report.setPageDefinition(new SimplePageDefinition(pf));
    final LSDebugOutputTarget lsd = new LSDebugOutputTarget();
    final PageableReportProcessor prp = new PageableReportProcessor(report);
    prp.setOutputTarget(lsd);
    lsd.open();
    prp.processReport();
    assertEquals(lsd.getShapeCount(), 1);
  }

}
