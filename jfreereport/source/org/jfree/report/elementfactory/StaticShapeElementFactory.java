/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * StaticShapeElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StaticShapeElementFactory.java,v 1.8 2005/01/24 23:59:23 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09-Jun-2003 : Initial version
 * 14-Jul-2003 : LineShapes are still a killer :(
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ShapeElement;
import org.jfree.report.content.ShapeTransform;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.ui.FloatDimension;
import org.jfree.util.Log;

/**
 * A factory to produce static shape elements. The shapes must not contain negative
 * coordinates and should start at (0,0). The factory does not scale shapes which have a
 * negative width or height. This behaviour was valid for JFreeReport versions up to
 * version 0.8.3 and is highly dangerous.
 * <p/>
 * The shape is considered immutable.
 * <p/>
 * The static utility methods provided in that class try to map negative values of lines
 * and rectangles in the createLineShapeElement and createRectangleElement to preserve the
 * old behaviour.
 * <p/>
 * The static method {@link StaticShapeElementFactory#createShapeElement(String, Color,
    * Stroke, Shape, boolean, boolean)} extracts the bounds from the given shape and performs
 * an tranlate transform to map the shape to the coordinate (0,0).
 *
 * @author Thomas Morgner
 */
public class StaticShapeElementFactory extends ShapeElementFactory
{
  /**
   * The shape that should be the content of the element.
   */
  private Shape shape;

  /**
   * Default Constructor.
   */
  public StaticShapeElementFactory()
  {
  }

  /**
   * Returns the shape object used as content of the new elements.
   *
   * @return the shape content.
   */
  public Shape getShape()
  {
    return shape;
  }

  /**
   * Defines the shape object used as content of the new elements.
   *
   * @param shape the shape content.
   */
  public void setShape(final Shape shape)
  {
    if (shape == null)
    {
      throw new NullPointerException();
    }

    this.shape = shape;
  }

  /**
   * Generates a new shape element.
   *
   * @return the generated element.
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement()
  {
    final ShapeElement e = new ShapeElement();
    applyElementName(e);
    applyStyle(e.getStyle());
    e.setDataSource(new StaticDataSource(getShape()));
    return e;
  }

  public static ShapeElement createHorizontalLine(final String name,
                                                  final Color paint,
                                                  final Stroke stroke,
                                                  final double y1)
  {
    // scale the line, is horizontal,the line is on pos 0,0 within the element
    final Rectangle2D bounds = new Rectangle2D.Float(0, (float) y1, -100, 0);
    return createShapeElement(name, bounds, paint, stroke, new Line2D.Float(0, 0, 100, 0),
        true, false, true);
  }

  public static ShapeElement createVerticalLine(final String name,
                                                  final Color paint,
                                                  final Stroke stroke,
                                                  final double y1)
  {
    // scale the line, is horizontal,the line is on pos 0,0 within the element
    final Rectangle2D bounds = new Rectangle2D.Float(0, (float) y1, 00, -100);
    return createShapeElement(name, bounds, paint, stroke, new Line2D.Float(0, 0, 0, 100),
        true, false, true);
  }

  /**
   * Creates a new LineShapeElement. The line must not contain negative coordinates, or an
   * IllegalArgumentException will be thrown. If you want to define scaling lines, you
   * will have use one of the createShape methods.
   * <p/>
   * This method is now deprecated, as it has an unclean syntax. For horizontal lines use
   * the {@link StaticShapeElementFactory#createHorizontalLine(String, Color, Stroke, double)}
   * method.
   *
   * @param name   the name of the new element
   * @param paint  the line color of this element
   * @param stroke the stroke of this shape. For pdf use, restrict to BasicStokes.
   * @param shape  the Line2D shape
   * @return a report element for drawing a line.
   * @throws NullPointerException     if bounds or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   * @deprecated the line shape elements should be created by using one of the
   *             <code>createShapeElement</code> methods or the
   *             <code>createHorizontalLine</code> method for horizontal lines which
   *             span the complete band.
   */
  public static ShapeElement createLineShapeElement(final String name,
                                                    final Color paint,
                                                    final Stroke stroke,
                                                    final Line2D shape)
  {
    if (shape.getX1() == shape.getX2() && shape.getY1() == shape.getY2())
    {
      Log.info("The use of Line(x1, y1, x1, y1) to create a scaled horizontal line is deprecated.\n" +
          "Use a Horizontal-Line element instead.");
      return createHorizontalLine(name, paint, stroke, shape.getY1());
    }
    else
    {
      final Rectangle2D bounds = shape.getBounds2D();
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
      return createShapeElement(name, bounds, paint, stroke, shape, true, false, true);
    }
  }

  /**
   * Creates a new LineShapeElement. This methods extracts the bounds from the shape and
   * correct the shape to start from point (0,0) by using an AffineTransform. Use one of
   * the createShape methods, that allow you to supply separate bounds and shapes, if you
   * want to have full control over the creation process.
   *
   * @param name       the name of the new element.
   * @param paint      the line color of this element.
   * @param stroke     the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param shape      the shape.
   * @param shouldDraw draw the shape?
   * @param shouldFill fill the shape?
   * @return a report element for drawing a line.
   * @throws NullPointerException     if bounds or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(final String name,
                                                final Color paint,
                                                final Stroke stroke,
                                                final Shape shape,
                                                final boolean shouldDraw,
                                                final boolean shouldFill)
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

    final Rectangle2D shapeBounds = shape.getBounds2D();
    // Log.debug ("ShapeBounds: " + shapeBounds);
    if (shapeBounds.getX() == 0 && shapeBounds.getY() == 0)
    {
      // no need to translate ...
      return createShapeElement(name, shapeBounds, paint, stroke, shape,
          shouldDraw, shouldFill, true);
    }

    final Shape transformedShape =
        ShapeTransform.translateShape(shape, -shapeBounds.getX(), -shapeBounds.getY());
    return createShapeElement(name, shapeBounds, paint, stroke, transformedShape,
        shouldDraw, shouldFill, true);
  }


  /**
   * Creates a new ShapeElement.
   *
   * @param name        the name of the new element.
   * @param bounds      the bounds.
   * @param paint       the line color of this element.
   * @param stroke      the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param shape       the shape.
   * @param shouldDraw  draw the shape?
   * @param shouldFill  fill the shape?
   * @param shouldScale scale the shape?
   * @return a report element for drawing a line.
   * @throws NullPointerException     if bounds or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color paint,
                                                final Stroke stroke,
                                                final Shape shape,
                                                final boolean shouldDraw,
                                                final boolean shouldFill,
                                                final boolean shouldScale)
  {
    return createShapeElement(name, bounds, paint, stroke, shape, shouldDraw,
        shouldFill, shouldScale, false);
  }

  /**
   * Creates a new ShapeElement.
   *
   * @param name            the name of the new element.
   * @param bounds          the bounds.
   * @param paint           the line color of this element.
   * @param stroke          the stroke of this shape. For pdf use, restrict to
   *                        BasicStrokes.
   * @param shape           the shape.
   * @param shouldDraw      draw the shape?
   * @param shouldFill      fill the shape?
   * @param shouldScale     scale the shape?
   * @param keepAspectRatio preserve the aspect ratio?
   * @return a report element for drawing a line.
   * @throws NullPointerException     if bounds or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color paint,
                                                final Stroke stroke,
                                                final Shape shape,
                                                final boolean shouldDraw,
                                                final boolean shouldFill,
                                                final boolean shouldScale,
                                                final boolean keepAspectRatio)
  {
    final StaticShapeElementFactory factory = new StaticShapeElementFactory();
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
   * @param name       the name of the new element
   * @param paint      the line color of this element
   * @param stroke     the stroke of this shape. For pdf use, restrict to BasicStokes.
   * @param shape      the Rectangle2D shape
   * @param shouldDraw a flag controlling whether or not the shape outline is drawn.
   * @param shouldFill a flag controlling whether or not the shape interior is filled.
   * @return a report element for drawing a rectangle.
   * @throws NullPointerException     if bounds or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createRectangleShapeElement(final String name,
                                                         final Color paint,
                                                         final Stroke stroke,
                                                         final Rectangle2D shape,
                                                         final boolean shouldDraw,
                                                         final boolean shouldFill)
  {
    if (shape.getX() < 0 || shape.getY() < 0 || shape.getWidth() < 0 || shape.getHeight() < 0)
    {
      // this is a relative rectangle element, so the shape defines the bounds
      // and expects to draw a scaled rectangle within these bounds
      return createShapeElement(name, shape, paint, stroke, new Rectangle2D.Float(0, 0, 100, 100),
          shouldDraw, shouldFill, true);
    }
    final Rectangle2D rect = (Rectangle2D) shape.clone();
    rect.setRect(0, 0, rect.getWidth(), rect.getHeight());
    return createShapeElement(name, shape, paint, stroke, rect, shouldDraw, shouldFill, false);
  }

}
