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
 * BandRemoveTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandRemoveTest.java,v 1.1 2003/10/05 21:54:45 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 24.09.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.functionality;

import java.awt.geom.Point2D;
import java.awt.Color;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.ElementAlignment;
import org.jfree.report.Element;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.ui.FloatDimension;

public class BandRemoveTest extends TestCase
{
  public BandRemoveTest(String s)
  {
    super(s);
  }

  public void testRemoveElement()
  {
    final JFreeReport report = new JFreeReport();
    report.setName("A Very Simple Report");


    TextFieldElementFactory factory = new TextFieldElementFactory();
    factory.setName("T1");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(150, 20));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("-");
    factory.setFieldname("Column1");

    final Element element1 = factory.createElement();
    report.getItemBand().addElement(element1);

    factory = new TextFieldElementFactory();
    factory.setName("T2");
    factory.setAbsolutePosition(new Point2D.Float(200, 0));
    factory.setMinimumSize(new FloatDimension(150, 20));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("-");
    factory.setFieldname("Column2");

    final Element element2 = factory.createElement();
    report.getItemBand().addElement(element2);

    //report.getStyleSheetCollection().debug();

    report.getItemBand().removeElement(element1);
    //report.getStyleSheetCollection().debug();
    FunctionalityTestLib.execGraphics2D(report);
  }

  public void testRemoveElementComplete()
  {
    final JFreeReport report = new JFreeReport();
    report.setName("A Very Simple Report");


    TextFieldElementFactory factory = new TextFieldElementFactory();
    factory.setName("T1");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(150, 20));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("-");
    factory.setFieldname("Column1");

    final Element element1 = factory.createElement();
    report.getItemBand().addElement(element1);

    factory = new TextFieldElementFactory();
    factory.setName("T2");
    factory.setAbsolutePosition(new Point2D.Float(200, 0));
    factory.setMinimumSize(new FloatDimension(150, 20));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("-");
    factory.setFieldname("Column2");

    final Element element2 = factory.createElement();
    report.getItemBand().addElement(element2);

    //report.getStyleSheetCollection().debug();

    report.getItemBand().removeElement(element1);
    report.getItemBand().removeElement(element2);

    //report.getStyleSheetCollection().debug();
    FunctionalityTestLib.execGraphics2D(report);

  }

}
