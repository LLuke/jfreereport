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
 * StaticShapeElementFactory.java
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
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

public class StaticShapeElementFactory extends ShapeElementFactory
{
  private Shape shape;

  public StaticShapeElementFactory()
  {
  }

  public Shape getShape()
  {
    return shape;
  }

  public void setShape(Shape shape)
  {
    if (shape == null)
      throw new NullPointerException();

    this.shape = shape;
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

    e.setDataSource(new StaticDataSource(getShape()));
    return e;
  }

  /**
   * Creates a new LineShapeElement. The line must not contain negative coordinates,
   * or an IllegalArgumentException will be thrown. If you want to define scaling
   * lines, you will have use one of the createShape methods.
   *
   * @param name the name of the new element
   * @param paint the line color of this element
   * @param stroke the stroke of this shape. For pdf use, restrict to BasicStokes.
   * @param shape the Line2D shape
   *
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createLineShapeElement(String name,
                                                    Color paint,
                                                    Stroke stroke,
                                                    Line2D shape)
  {
    if (shape.getX1() == shape.getX2() && shape.getY1() == shape.getY2())
    {
      // scale the line, is horizontal,the line is on pos 0,0 within the element
      Rectangle2D bounds = new Rectangle2D.Float(0, (float) shape.getY1(), -100, 0);
      return createShapeElement(name, bounds, paint, stroke, new Line2D.Float(0, 0, 100, 0),
          true, false, true);
    }
    else
    {
      Rectangle2D bounds = shape.getBounds2D();
      if (bounds.getX() < 0)
      {
        throw new IllegalArgumentException("Line coordinates must not be negative.");
      }
      if (bounds.getY() < 0)
      {
        throw new IllegalArgumentException("Line coordinates must not be negative.");
      }

      shape.setLine(shape.getX1() - bounds.getX(),
          shape.getY1() - bounds.getY(),
          shape.getX2() - bounds.getX(),
          shape.getY2() - bounds.getY());
      bounds.setRect(0, 0, bounds.getWidth(), bounds.getHeight());
      return createShapeElement(name, bounds, paint, stroke, shape, true, false, true);
    }
  }

  /**
   * Creates a new LineShapeElement. This methods extracts the bounds from the shape and correct
   * the shape to start from point (0,0) by using an AffineTransform. Use one of the createShape
   * methods, that allow you to supply separate bounds and shapes, if you want to have full
   * control over the creation process.
   *
   * @param name  the name of the new element.
   * @param paint  the line color of this element.
   * @param stroke  the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param shape  the shape.
   * @param shouldDraw  draw the shape?
   * @param shouldFill  fill the shape?
   *
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(String name,
                                                Color paint,
                                                Stroke stroke,
                                                Shape shape,
                                                boolean shouldDraw,
                                                boolean shouldFill)
  {
    // we have two choices here: let the element be big enough to take up
    // the complete shape and let the element start at 0,0, and the shape
    // therefore starts at x,y
    //
    // or
    //
    // translate the shape to start at 0,0 and let the element start at
    // the shapes origin (x,y).

    // we have to translate the shape, as anything else would mess up the table layout

    Rectangle2D shapeBounds = shape.getBounds2D();

    if (shapeBounds.getX() == 0 && shapeBounds.getY() == 0)
    {
      // no need to translate ...
      return createShapeElement(name, shapeBounds, paint, stroke, shape,
          shouldDraw, shouldFill, true);
    }

    AffineTransform af = AffineTransform.getTranslateInstance(-shapeBounds.getX(),
        -shapeBounds.getY());
    return createShapeElement(name, shapeBounds, paint, stroke, af.createTransformedShape(shape),
        shouldDraw, shouldFill, true);
  }


  /**
   * Creates a new ShapeElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds.
   * @param paint  the line color of this element.
   * @param stroke  the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param shape  the shape.
   * @param shouldDraw  draw the shape?
   * @param shouldFill  fill the shape?
   * @param shouldScale  scale the shape?
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
                                                Shape shape,
                                                boolean shouldDraw,
                                                boolean shouldFill,
                                                boolean shouldScale)
  {
    return createShapeElement(name, bounds, paint, stroke, shape, shouldDraw,
        shouldFill, shouldScale, false);
  }

  /**
   * Creates a new ShapeElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds.
   * @param paint  the line color of this element.
   * @param stroke  the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param shape  the shape.
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
                                                Shape shape,
                                                boolean shouldDraw,
                                                boolean shouldFill,
                                                boolean shouldScale,
                                                boolean keepAspectRatio)
  {
    StaticShapeElementFactory factory = new StaticShapeElementFactory();
    factory.setName(name);
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setColor(paint);
    factory.setKeepAspectRatio(new Boolean(keepAspectRatio));
    factory.setScale(new Boolean(shouldScale));
    factory.setShouldDraw(new Boolean(shouldDraw));
    factory.setShouldFill(new Boolean(shouldFill));
    factory.setShape(shape);
    factory.setStroke(stroke);
    return (ShapeElement) factory.createElement();
  }


  /**
   * Creates a new RectangleShapeElement.
   *
   * @param name the name of the new element
   * @param paint the line color of this element
   * @param stroke the stroke of this shape. For pdf use, restrict to BasicStokes.
   * @param shape the Rectangle2D shape
   * @param shouldDraw  a flag controlling whether or not the shape outline is drawn.
   * @param shouldFill  a flag controlling whether or not the shape interior is filled.
   *
   * @return a report element for drawing a rectangle.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createRectangleShapeElement(String name,
                                                         Color paint,
                                                         Stroke stroke,
                                                         Rectangle2D shape,
                                                         boolean shouldDraw,
                                                         boolean shouldFill)
  {
    if (shape.getX() < 0 || shape.getY() < 0 || shape.getWidth() < 0 || shape.getHeight() < 0)
    {
      // this is a relative rectangle element, so the shape defines the bounds
      // and expects to draw a scaled rectangle within these bounds
      return createShapeElement(name, shape, paint, stroke, new Rectangle2D.Float(0, 0, 100, 100),
          shouldDraw, shouldFill, true);
    }
    Rectangle2D rect = (Rectangle2D) shape.clone();
    rect.setRect(0, 0, rect.getWidth(), rect.getHeight());
    return createShapeElement(name, shape, paint, stroke, rect, shouldDraw, shouldFill, false);
  }

}
