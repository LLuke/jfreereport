/**
 * Date: Jan 31, 2003
 * Time: 12:00:43 AM
 *
 * $Id$
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

public class SampleReport2
{
  public SampleReport2()
  {
  }

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

  public JFreeReport createReport ()
  {
    Band levelA1 = createBand("A1", Color.magenta,0,0, 100, 100);
    //levelA1.addElement(createBand("A1-B1", Color.blue, 0, 50, 50, 50));
    levelA1.addElement(createBand("A1-B2", Color.yellow, 50, 0, 150, 50));
/*
    Band levelA2 = createBand("A2", Color.green,-55,-5, -45, -100);
    levelA2.addElement(createBand("A2-B1", Color.red, -5, -55, -40, -40));
    levelA2.addElement(createBand("A2-B2", Color.pink, -55, -5, -40, -40));
*/
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
//    header.addElement(levelA2);

    JFreeReport report = new JFreeReport();
    report.setReportHeader(header);
    return report;
  }

}
