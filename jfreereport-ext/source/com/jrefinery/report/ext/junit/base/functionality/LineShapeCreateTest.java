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
 * LineShapeCreateTest.java
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
 * 10.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.util.PageFormatFactory;
import junit.framework.TestCase;

public class LineShapeCreateTest extends TestCase
{
  private class LSDebugOutputTarget extends DebugOutputTarget
  {
    public LSDebugOutputTarget(PageFormat format)
    {
      super(format);
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
    public void drawShape(Shape shape)
    {
      shapeCount++;
    }

    public int getShapeCount()
    {
      return shapeCount;
    }

    public void setShapeCount(int shapeCount)
    {
      this.shapeCount = shapeCount;
    }
  }

  public JFreeReport getReport()
  {
    JFreeReport report = new JFreeReport();
    ReportHeader header = report.getReportHeader();
    Color c = Color.red;
    for (int i = 10; i < 100; i += 10)
    {
      header.addElement(
          ItemFactory.createLabelElement(
              "Asse" + Integer.toString(i),
              new Rectangle2D.Float(340 + i * 4, 40, 20, 10),
              null,
              ElementAlignment.CENTER.getOldAlignment(), ElementAlignment.MIDDLE.getOldAlignment(),
              new Font("Serif", (i == 50 ? Font.BOLD : Font.PLAIN), 10),
              Integer.toString(i)
          )
      );
      header.addElement(
          ItemFactory.createLineShapeElement(
              "tick" + Integer.toString(i),
              c,
              new BasicStroke(0.25f),
              new Line2D.Float(350 + i * 4, 52, 350 + i * 4, 49)
          )
      );
    }

    header.addElement(
        ItemFactory.createLineShapeElement(
            "linea",
            null,
            new BasicStroke(0.25f),
            new Line2D.Float(390, 52, 710, 52)
        )
    );

    return report;
  };

  public void testDoReport() throws Exception
  {
    JFreeReport report = getReport();
    PageFormatFactory pff = PageFormatFactory.getInstance();
    Paper paper = pff.createPaper("A0");
    PageFormat pf = pff.createPageFormat(paper, PageFormat.PORTRAIT);
    assertEquals((int) pf.getHeight(), PageFormatFactory.A0[1]);
    assertEquals((int) pf.getWidth(), PageFormatFactory.A0[0]);
    report.setDefaultPageFormat(pf);
    assertEquals(report.getDefaultPageFormat(), pf);
    LSDebugOutputTarget lsd = new LSDebugOutputTarget(report.getDefaultPageFormat());
    PageableReportProcessor prp = new PageableReportProcessor(report);
    prp.setOutputTarget(lsd);
    lsd.open();
    prp.processReport();
    assertEquals(lsd.getShapeCount(), 10);
  }
}
