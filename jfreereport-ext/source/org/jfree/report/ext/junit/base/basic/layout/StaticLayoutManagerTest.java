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
 * StaticLayoutManagerTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StaticLayoutManagerTest.java,v 1.6 2005/03/24 23:09:10 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 11.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.layout;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;
import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

public class StaticLayoutManagerTest extends TestCase
{
  public StaticLayoutManagerTest (final String s)
  {
    super(s);
  }

  /**
   * create a band. The band contains a rectangle shape element in that band
   * with the same boundries as the band.
   *
   * @param name An optional name
   * @param color the color of the rectangle element
   * @param x the x coordinates
   * @param y the y coordinates
   * @param width the width of the band and the rectangle
   * @param height the height of the band and the rectangle
   * @return the created band
   */
  private Band createBand (final String name, final Color color, 
                           final int x, final int y, 
                           final int width, final int height)
  {
    final Band band = new Band ();
    band.setName("Band-" + name);
    band.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                     new FloatDimension(width, height));
    band.getStyle().setStyleProperty(ElementStyleSheet.MAXIMUMSIZE,
                                     new FloatDimension(width, height));
    band.getStyle().setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, new Point2D.Double(x, y));

    // create the marker shape, the shape fills the generated band and paints the colored background
    // all coordinates or dimensions are within the band, and not affected by the bands placement in
    // the outer report bands
    band.addElement(
        StaticShapeElementFactory.createRectangleShapeElement(name,
                                                color,
                                                null,
                                                new Rectangle(0, 0, -100, -100),
                                                false,
                                                true)
    );
    return band;
  }

  private Band createBand ()
  {
    final Band levelA1 = createBand("A1", Color.magenta, 0, 0, 100, 100);
    levelA1.addElement(createBand("A1-B1", Color.blue, 0, 50, 50, 50));
    levelA1.addElement(createBand("A1-B2", Color.yellow, 50, 0, 150, 50));
    // x=55%, y=5%, width=40%, height=100%
    final Band levelA2 = createBand("A2", Color.green, -50, 0, -50, -100);
    // x=5%, y=55%, width=40%, height=40%
    levelA2.addElement(createBand("A2-B1", Color.red, 0, -50, -50, -50));
    // x=55%, y=5%, width=40%, height=40%
    levelA2.addElement(createBand("A2-B2", Color.darkGray, -55, -5, -40, -40));

    final Band header = new Band ();
    header.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                       new FloatDimension(-100, 100));
    header.getStyle().setStyleProperty(ElementStyleSheet.MAXIMUMSIZE,
                                       new FloatDimension(Short.MAX_VALUE, 100));

    header.addElement(
        StaticShapeElementFactory.createRectangleShapeElement("Root-Shape",
                                                Color.orange,
                                                null,
                                                new Rectangle(0, 0, -100, -100),
                                                false,
                                                true));
    header.addElement(levelA1);
    header.addElement(levelA2);
    return header;
  }

  public static final long STRICT_FACTOR = StrictGeomUtility.toInternalValue(1);

  public void testBandInBandLayout ()
  {
    final Band band = createBand();
    band.setLayout(new StaticLayoutManager());
    BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(),
            500 * STRICT_FACTOR, 500 * STRICT_FACTOR);

    assertEquals(new StrictBounds(0,0, 500 * STRICT_FACTOR, 100 * STRICT_FACTOR),
        BandLayoutManagerUtil.getBounds(band, null));

    final Band bandA1 = (Band) band.getElement("Band-A1");
    final Band bandA2 = (Band) band.getElement("Band-A2");

    assertEquals(new StrictBounds(0,0, 100 * STRICT_FACTOR, 100 * STRICT_FACTOR),
        BandLayoutManagerUtil.getBounds(bandA1, null));

    assertEquals(new StrictBounds(0,50 * STRICT_FACTOR, 50 * STRICT_FACTOR, 50 * STRICT_FACTOR),
        BandLayoutManagerUtil.getBounds(bandA1.getElement("Band-A1-B1"), null));

    assertEquals(new StrictBounds(50 * STRICT_FACTOR,0, 50 * STRICT_FACTOR, 50 * STRICT_FACTOR),
        BandLayoutManagerUtil.getBounds(bandA1.getElement("Band-A1-B2"), null));

    assertEquals(new StrictBounds(250 * STRICT_FACTOR,0, 250 * STRICT_FACTOR, 100 * STRICT_FACTOR),
        BandLayoutManagerUtil.getBounds(bandA2, null));

    // Band is relative to the parent ...
    // parent starts at 250, and A2-B1 on pos 0 within the parent ...
    assertEquals(new StrictBounds(0, 50 * STRICT_FACTOR, 125 * STRICT_FACTOR, 50 * STRICT_FACTOR),
        BandLayoutManagerUtil.getBounds(bandA2.getElement("Band-A2-B1"), null));

    // all values +/- 5%
    assertEquals(new StrictBounds((long) (137.5f  * STRICT_FACTOR),5 * STRICT_FACTOR, 100 * STRICT_FACTOR, 40 * STRICT_FACTOR),
        BandLayoutManagerUtil.getBounds(bandA2.getElement("Band-A2-B2"), null));

  }

  public void testAllElementDynamicLayout ()
  {
    final Element label1 = LabelElementFactory.createLabelElement
            (null, new Rectangle2D.Float(0,0, 250, 0), null, null, null, "A very short text");
    label1.setDynamicContent(true);

    final Element label2 = LabelElementFactory.createLabelElement
            (null, new Rectangle2D.Float(250,0, 250, 0), null, null, null,
                    "A very short text but enough to force a line break so that " +
                    "we can check whether the dynamic element is truely dynamic " +
                    "or whether it is just a fake.");
    label2.setDynamicContent(true);

    final Band band = new Band ();
    band.addElement(label1);
    band.addElement(label2);

    final StrictBounds bounds =
            BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(),
            500 * STRICT_FACTOR, 500 * STRICT_FACTOR);
    assertEquals(500 * STRICT_FACTOR, bounds.getWidth());
    assertTrue(bounds.getHeight() > 0);
  }

  public void testHalfRelativeContent ()
  {
    final Element fixedRect =
            StaticShapeElementFactory.createRectangleShapeElement
            ("fixed", null, null, new Rectangle2D.Float(0, 0, 100, 100), true, true);
    final Element halfRect =
            StaticShapeElementFactory.createRectangleShapeElement
            ("half-fixed", null, null, new Rectangle2D.Float(0, 0, 100, -100), true, true);

    final Band band = new Band ();
    band.addElement(fixedRect);
    band.addElement(halfRect);

    final StrictBounds bounds =
            BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(),
            500 * STRICT_FACTOR, 500 * STRICT_FACTOR);
    assertEquals(500 * STRICT_FACTOR, bounds.getWidth());
    assertEquals(100 * STRICT_FACTOR, bounds.getHeight());
  }

  public void testBandInBand ()
  {
    final Band subBand = new Band();
    subBand.setMinimumSize(new FloatDimension(-50, 40));
    subBand.setMaximumSize(new FloatDimension(-50, 32768));

    final Band band = new Band ();
    band.setMinimumSize(new FloatDimension(-50, 40));
    band.setMaximumSize(new FloatDimension(-50, 32768));
    band.addElement(subBand);

    final Band rootBand = new Band ();
    rootBand.addElement(band);

    final StrictBounds bounds =
            BandLayoutManagerUtil.doLayout(rootBand, new DefaultLayoutSupport(),
            500 * STRICT_FACTOR, 500 * STRICT_FACTOR);
    final StrictBounds bandBounds = (StrictBounds)
            band.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    assertEquals(250 * STRICT_FACTOR, bandBounds.getWidth());
    assertEquals(40 * STRICT_FACTOR, bandBounds.getHeight());

    final StrictBounds subBandBounds = (StrictBounds)
            subBand.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    assertEquals(125 * STRICT_FACTOR, subBandBounds.getWidth());
    assertEquals(40 * STRICT_FACTOR, subBandBounds.getHeight());
  }

}
