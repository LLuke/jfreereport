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
 * ShapeFieldElementFactory.java
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
 * 09.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.elementfactory;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

public class ShapeFieldElementFactory extends ShapeElementFactory
{
  private String fieldname;

  public ShapeFieldElementFactory()
  {
  }

  public String getFieldname()
  {
    return fieldname;
  }

  public void setFieldname(String fieldname)
  {
    this.fieldname = fieldname;
  }

  public Element createElement()
  {
    ShapeElement e = new ShapeElement();
    ElementStyleSheet style = e.getStyle();
    if (getName() != null)
    {
      e.setName(getName());
    }
    style.setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, getAbsolutePosition());
    style.setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT, getDynamicHeight());
    style.setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, getKeepAspectRatio());
    style.setStyleProperty(ElementStyleSheet.MAXIMUMSIZE, getMaximumSize());
    style.setStyleProperty(ElementStyleSheet.MINIMUMSIZE, getMinimumSize());
    style.setStyleProperty(ElementStyleSheet.PAINT, getColor());
    style.setStyleProperty(ElementStyleSheet.PREFERREDSIZE, getPreferredSize());
    style.setStyleProperty(ElementStyleSheet.SCALE, getScale());
    style.setStyleProperty(ElementStyleSheet.STROKE, getStroke());
    style.setStyleProperty(ShapeElement.DRAW_SHAPE, getShouldDraw());
    style.setStyleProperty(ShapeElement.FILL_SHAPE, getShouldFill());

    e.setDataSource(new DataRowDataSource(getFieldname()));
    return e;
  }

  /**
   * Creates a new ShapeElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds.
   * @param paint  the line color of this element.
   * @param stroke  the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param fieldname  the fieldname from where to get the shape.
   * @param shouldDraw  draw the shape?
   * @param shouldFill  fill the shape?
   * @param shouldScale  scale the shape?
   * @param keepAspectRatio  preserve the aspect ratio?
   *
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(String name,
                                                Rectangle2D bounds,
                                                Color paint,
                                                Stroke stroke,
                                                String fieldname,
                                                boolean shouldDraw,
                                                boolean shouldFill,
                                                boolean shouldScale,
                                                boolean keepAspectRatio)
  {
    ShapeFieldElementFactory factory = new ShapeFieldElementFactory();
    factory.setName(name);
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setColor(paint);
    factory.setKeepAspectRatio(new Boolean(keepAspectRatio));
    factory.setScale(new Boolean(shouldScale));
    factory.setShouldDraw(new Boolean(shouldDraw));
    factory.setShouldFill(new Boolean(shouldFill));
    factory.setFieldname(fieldname);
    factory.setStroke(stroke);
    return (ShapeElement) factory.createElement();
  }

}
