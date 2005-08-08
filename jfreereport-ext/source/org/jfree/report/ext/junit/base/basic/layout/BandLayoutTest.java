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
 * BandLayoutTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandLayoutTest.java,v 1.8 2005/02/19 16:15:47 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.layout;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;
import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.JFreeReport;
import org.jfree.report.ShapeElement;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.util.Log;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.xml.ParserUtil;

public class BandLayoutTest extends TestCase
{
  public BandLayoutTest()
  {
  }

  public BandLayoutTest(final String s)
  {
    super(s);
  }
  public static final long STRICT_FACTOR = StrictGeomUtility.toInternalValue(1);

  public void testBasicLayout()
  {
    JFreeReportBoot.getInstance().start();
    final Band band = new Band();
    BandLayoutManagerUtil.doLayout(band,
            new DefaultLayoutSupport(), 500 * STRICT_FACTOR, 200 * STRICT_FACTOR);
    // width is preserved  ...
    assertEquals(new StrictBounds(0, 0, 500 * STRICT_FACTOR, 0),
            BandLayoutManagerUtil.getBounds(band, null));

    final Element e = StaticShapeElementFactory.createRectangleShapeElement(null, null, null,
        new Rectangle2D.Float(0, 0, 10, 10), true, true);
    band.addElement(e);
    BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(),
            500 * STRICT_FACTOR, 200 * STRICT_FACTOR);
    // width is preserved  ... height is the one given in the element
    assertEquals(new StrictBounds(0, 0, 500 * STRICT_FACTOR, 10 * STRICT_FACTOR), BandLayoutManagerUtil.getBounds(band, null));

  }

  public void testLineLayout()
  {
    final Band band = new Band();
    BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(),
            500 * STRICT_FACTOR, 200 * STRICT_FACTOR);
    // width is preserved  ...
    assertEquals(new StrictBounds(0, 0, 500 * STRICT_FACTOR, 0), BandLayoutManagerUtil.getBounds(band, null));

    final Element e = StaticShapeElementFactory.createHorizontalLine
        (null, null, null, 10);
    band.addElement(e);
    BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500 * STRICT_FACTOR, 200 * STRICT_FACTOR);
    // width is preserved  ... height is the one given in the element
    assertEquals(new StrictBounds(0, 0, 500 * STRICT_FACTOR, 10 * STRICT_FACTOR), BandLayoutManagerUtil.getBounds(band, null));

  }

  public void testShapeLayout ()
  {
    final JFreeReport report = new JFreeReport();
    final Line2D line = new Line2D.Float(40, 70, 140, 70);
    Log.debug ("Line: " + line.getBounds2D());
    final ShapeElement element =
        StaticShapeElementFactory.createShapeElement(
        null,
        Color.black,
        ParserUtil.parseStroke("1"),
        line, true, false);
    report.getReportHeader().addElement(element);

    Log.debug (element.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE));
    Log.debug (element.getStyle().getStyleProperty(StaticLayoutManager.ABSOLUTE_POS));
    Log.debug (BandLayoutManagerUtil.getBounds(element, null));

    BandLayoutManagerUtil.doLayout(report.getReportHeader(),
            new DefaultLayoutSupport(), 500 * STRICT_FACTOR, 200 * STRICT_FACTOR);
    assertEquals(new StrictBounds(0, 0, 500 * STRICT_FACTOR, 70 * STRICT_FACTOR),
              BandLayoutManagerUtil.getBounds(report.getReportHeader(), null));
  }
}
