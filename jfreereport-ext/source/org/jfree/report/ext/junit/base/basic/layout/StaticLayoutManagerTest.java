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
 * StaticLayoutManagerTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StaticLayoutManagerTest.java,v 1.2 2003/07/03 16:06:18 taqua Exp $
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

import org.jfree.report.Band;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;
import junit.framework.TestCase;

public class StaticLayoutManagerTest extends TestCase
{
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
  private Band createBand (final String name, final Color color, final int x, final int y, final int width, final int height)
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

  public void testBandInBandLayout ()
  {
    final Band band = createBand();
    band.setLayout(new StaticLayoutManager());
    BandLayoutManagerUtil.doLayout(band, new DefaultLayoutSupport(), 500, 500);

    assertEquals(new Rectangle2D.Float(0,0, 500, 100),
        BandLayoutManagerUtil.getBounds(band, null));

    final Band bandA1 = (Band) band.getElement("Band-A1");
    final Band bandA2 = (Band) band.getElement("Band-A2");

    assertEquals(new Rectangle2D.Float(0,0, 100, 100),
        BandLayoutManagerUtil.getBounds(bandA1, null));

    assertEquals(new Rectangle2D.Float(0,50, 50, 50),
        BandLayoutManagerUtil.getBounds(bandA1.getElement("Band-A1-B1"), null));

    assertEquals(new Rectangle2D.Float(50,0, 50, 50),
        BandLayoutManagerUtil.getBounds(bandA1.getElement("Band-A1-B2"), null));

    assertEquals(new Rectangle2D.Float(250,0, 250, 100),
        BandLayoutManagerUtil.getBounds(bandA2, null));

    // Band is relative to the parent ...
    // parent starts at 250, and A2-B1 on pos 0 within the parent ...
    assertEquals(new Rectangle2D.Float(0, 50, 125, 50),
        BandLayoutManagerUtil.getBounds(bandA2.getElement("Band-A2-B1"), null));

    // all values +/- 5%
    assertEquals(new Rectangle2D.Float(137.5f ,5, 100, 40),
        BandLayoutManagerUtil.getBounds(bandA2.getElement("Band-A2-B2"), null));

  }
}
