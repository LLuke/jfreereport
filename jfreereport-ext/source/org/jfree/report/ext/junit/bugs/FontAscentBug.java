/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * FontAscentBug.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 16.01.2006 : Initial version
 */
package org.jfree.report.ext.junit.bugs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.swing.table.DefaultTableModel;

import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.ShapeElement;
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.style.FontDefinition;

/**
 * Creation-Date: 16.01.2006, 18:37:30
 *
 * @author Thomas Morgner
 */
public class FontAscentBug
{
  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    JFreeReport report = new JFreeReport();
    report.setName("ReportTextLayout001");

    ShapeElement rectangleElement = StaticShapeElementFactory
            .createRectangleShapeElement("Rectangle",
                    Color.GREEN,
                    new BasicStroke(1),
                    new Rectangle2D.Double(0, 10, -100, 104),
                    true,
                    true);

    TextElement labelElement = LabelElementFactory.createLabelElement("Label1",
            new Rectangle2D.Double(0, 10, -100, 104),
            Color.BLACK,
            ElementAlignment.LEFT,
            new FontDefinition("Arial", 40),
            "ppp Title  …  …ƒ");
    labelElement.setUnderline(true);
    labelElement.setStrikethrough(true);


    report.getReportHeader().addElement(rectangleElement);
    report.getReportHeader().addElement(labelElement);

    report.setData(new DefaultTableModel());

    try
    {
      JFreeReportBoot.getInstance().start();
      PreviewFrame preview = new PreviewFrame(report);
      preview.pack();
      preview.setVisible(true);
    }
    catch (ReportProcessingException e)
    {
      e.printStackTrace();
    }
  }

}
