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
 * ------------------
 * SampleReport3.java
 * ------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SampleReport2.java,v 1.11 2003/04/24 18:08:46 taqua Exp $
 *
 * Changes:
 * --------
 * 31-Jan-2003 : Initial version
 */
package com.jrefinery.report.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

import com.jrefinery.report.Band;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.function.AbstractExpression;
import com.jrefinery.report.function.Expression;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.function.PaintComponentFunction;
import com.jrefinery.report.preview.PDFSaveDialog;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;
import org.jfree.ui.FloatDimension;

/**
 * A sample to show the band in band capabilities of JFreeReport ...
 * 
 * @author Thomas Morgner.
 */
public class SampleReport2
{
  /**
   * An expression that returns a very complex component.
   */
  private class ComplexComponentExpression extends AbstractExpression
  {
    /** A component. */
    private Component pif;

    /**
     * Creates an expression.
     * 
     * @param name  the name.
     */
    public ComplexComponentExpression(String name)
    {
      setName(name);
      try
      {
        PDFSaveDialog dlg = new PDFSaveDialog();
        pif = dlg.getContentPane();
        pif.setVisible(true);
        // remove the old content pane from the dialog, so that it has no
        // parent ...
        dlg.setContentPane(new JPanel());
      }
      catch (Exception e)
      {
        Log.error ("PIF init failed");
      }
    }

    /**
     * Return a completly separated copy of this function. The copy does no
     * longer share any changeable objects with the original function. Only
     * the datarow may be shared.
     *
     * @return a copy of this function.
     */
    public Expression getInstance()
    {
      return new ComplexComponentExpression(getName());
    }

    /**
     * Return the current expression value.
     * <P>
     * The value depends (obviously) on the expression implementation.
     *
     * @return the value of the function.
     */
    public Object getValue()
    {
      return pif;
    }
  }

  /**
   * Default constructor.
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
    band.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, 
                                     new FloatDimension(width, height));
    band.getStyle().setStyleProperty(ElementStyleSheet.MAXIMUMSIZE, 
                                     new FloatDimension(width, height));
    band.getStyle().setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, new Point2D.Double(x, y));

    // create the marker shape, the shape fills the generated band and paints the colored background
    // all coordinates or dimensions are within the band, and not affected by the bands placement in
    // the outer report bands 
    band.addElement(
        ItemFactory.createRectangleShapeElement(name,
                                                color,
                                                null,
                                                new Rectangle(0, 0, -100, -100),
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
   * 
   * @throws FunctionInitializeException if there is a problem initialising the function.
   */
  public JFreeReport createReport () throws FunctionInitializeException
  {
    Band levelA1 = createBand("A1", Color.magenta, 0, 0, 100, 100);
    levelA1.addElement(createBand("A1-B1", Color.blue, 0, 50, 50, 50));
    levelA1.addElement(createBand("A1-B2", Color.yellow, 50, 0, 150, 50));
    // x=55%, y=5%, width=40%, height=100%
    Band levelA2 = createBand("A2", Color.green, -50, 0, -50, -100);
    // x=5%, y=55%, width=40%, height=40%
    levelA2.addElement(createBand("A2-B1", Color.red, 0, -50, -50, -50));
    // x=55%, y=5%, width=40%, height=40%
    levelA2.addElement(createBand("A2-B2", Color.darkGray, -55, -5, -40, -40));

    ReportHeader header = new ReportHeader ();
    header.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                       new FloatDimension(-100, 100));
    header.getStyle().setStyleProperty(ElementStyleSheet.MAXIMUMSIZE,
                                       new FloatDimension(Short.MAX_VALUE, 100));

    header.addElement(
        ItemFactory.createRectangleShapeElement("Root-Shape",
                                                Color.orange,
                                                null,
                                                new Rectangle(0, 0, -100, -100),
                                                false,
                                                true));
    header.addElement(levelA1);
    header.addElement(levelA2);

    ReportFooter footer = new ReportFooter();
    footer.addElement(ItemFactory.createImageDataRowElement("element",
                                                            new Rectangle2D.Float (0, 0, 400, 400),
                                                            Color.white, "PaintComponent"));

    JFreeReport report = new JFreeReport();
    report.setReportHeader(header);
    report.setReportFooter(footer);

    report.addExpression(new ComplexComponentExpression("CreateComponent"));

    PaintComponentFunction pc = new PaintComponentFunction();
    pc.setName("PaintComponent");
    pc.setProperty("field", "CreateComponent");
    pc.setProperty("element", "element");
    pc.setProperty("scale", "5");
    report.addFunction(pc);

    return report;
  }

}
