/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * SampleReport2.java
 * ----------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SampleReport2.java,v 1.1 2003/02/01 18:30:53 taqua Exp $
 *
 * Changes:
 * --------
 * 31-Jan-2003 : Initial version
 */
package com.jrefinery.report.demo;

import com.jrefinery.report.Band;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * A sample to show the band in band capabilities of JFreeReport ...
 */
public class SampleReport2
{
  /**
   * Default constructor
   */
  public SampleReport2()
  {
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
  private Band createBand (String name, Color color, int x, int y, int width, int height)
  {
    Band band = new Band ();
    band.setName("Band-" + name);
    band.getStyle().setStyleProperty(ElementStyleSheet.MAXIMUMSIZE, new FloatDimension(x + width, y + height));
    band.addElement(
        ItemFactory.createRectangleShapeElement(name,
                                                color,
                                                null,
                                                new Rectangle(x,y,width, height),
                                                false,
                                                true)
    );
    return band;
  }

  /**
   * Create a report with a single report header band. This band contains several
   * sub bands.
   *
   * @return the created report.
   */
  public JFreeReport createReport ()
  {
    Band levelA1 = createBand("A1", Color.magenta,0,0, 100, 100);
    levelA1.addElement(createBand("A1-B1", Color.blue, 0, 50, 50, 50));
    levelA1.addElement(createBand("A1-B2", Color.yellow, 50, 0, 150, 50));

    Band levelA2 = createBand("A2", Color.green,-55,-5, -45, -100);
    levelA2.addElement(createBand("A2-B1", Color.red, -5, -55, -40, -40));
    levelA2.addElement(createBand("A2-B2", Color.pink, -55, -5, -40, -40));

    ReportHeader header = new ReportHeader ();
    header.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                       new FloatDimension(-100, 100));
    header.addElement(
        ItemFactory.createRectangleShapeElement("Root-Shape",
                                                Color.orange,
                                                null,
                                                new Rectangle(0,0,-100, -100),
                                                false,
                                                true));
    header.addElement(levelA1);
    header.addElement(levelA2);

    JFreeReport report = new JFreeReport();
    report.setReportHeader(header);
    return report;
  }

}
