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
 * -------------------
 * ShapeTransform.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeTransform.java,v 1.6.4.1 2004/12/13 19:26:20 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jan-2003 : Initial version
 *
 */
package org.jfree.report.content;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import org.jfree.ui.FloatDimension;

/**
 * Utility class, which resizes or translates a Shape.
 * The class contains special handlers for Rectangles and Lines.
 *
 * @author Thomas Morgner
 */
public final strictfp class ShapeTransform
{
  /**
   * Default constructor.
   *
   */
  private ShapeTransform()
  {
  }

  public static Line2D resizeLine (final Line2D line,
                                    final double width, final double height)
  {
    final Line2D newLine = getNormalizedLine(line);
    final Point2D p1 = newLine.getP1();
    final Point2D p2 = newLine.getP2();
    final double normPointX  = (p1.getX() - p2.getX());
    final double normPointY  = (p1.getY() - p2.getY());
    final double scaleX = (normPointX == 0) ? 1 : width / Math.abs(normPointX);
    final double scaleY = (normPointY == 0) ? 1 : height / Math.abs(normPointY);
    p2.setLocation((p2.getX() - p1.getX()) * scaleX + p1.getX(),
                   (p2.getY() - p1.getY()) * scaleY + p1.getY());
    newLine.setLine(p1, p2);
    return newLine;
  }

//  public static void main (final String[] args)
//  {
//    BaseBoot.getInstance().start();
//    final Line2D l1 = new Line2D.Double( 10, 20, -10, -20);
//    final Line2D message = resizeLine(l1, 10, 20);
//    Log.debug(message.getP1() + " " + message.getP2());
//  }
//
  /**
   * Normalize the line; the point with the lowest X is the primary point,
   * if both points have the same X, that point with the lowest Y value wins.
   *
   * @param line the original line
   * @return the normalized line
   */
  private static Line2D getNormalizedLine (final Line2D line)
  {
    final Line2D lineClone = (Line2D) line.clone();

    final Point2D p1 = line.getP1();
    final Point2D p2 = line.getP2();
    if (p1.getX() < p2.getX())
    {
      return lineClone;
    }
    if (p1.getX() > p2.getX())
    {
      lineClone.setLine(p2, p1);
      return lineClone;
    }
    if (p1.getY() < p2.getY())
    {
      return lineClone;
    }
    lineClone.setLine(p2, p1);
    return lineClone;
  }

  /**
   * Resizes a shape, so that the shape has the given width and height, but the
   * origin of the shape does not change.
   * <p>
   * Unlike the AffineTransform, this method tries to preserve the Shape's Type.
   *
   * @param s the shape
   * @param width the new width
   * @param height the new height
   * @return the resized shape.
   */
  public static Shape resizeShape (final Shape s,
                                   final float width,
                                   final float height)
  {
    if (s instanceof Line2D)
    {
      return resizeLine((Line2D) s, width, height);
    }
    if (s instanceof RectangularShape)
    {
      return resizeRect ((RectangularShape) s, width, height);
    }
    return transformShape(s, true, false, new FloatDimension(width, height));
  }

  public static Shape resizeRect (final RectangularShape rectangularShape,
                                   final double width,
                                   final double height)
  {
    final RectangularShape retval = (RectangularShape) rectangularShape.clone();
    retval.setFrame(retval.getX(), retval.getY(), width, height);
    return retval;
  }

  /**
   * Translates the given shape. The shape is translated to the origin supplied
   * in <code>point</code>. If scaling is requested, the shape will also be scaled
   * using an AffineTransform.
   *
   * @param s the shape that should be transformed
   * @param scale true, if the shape should be scaled, false otherwise
   * @param keepAR true, if the scaled shape should keep the aspect ratio
   * @param dim the target dimension.
   * @return the transformed shape
   */
  public static Shape transformShape
      (final Shape s, final boolean scale, final boolean keepAR, final Dimension2D dim)
  {
    /**
     * Always scale to the maximum bounds ...
     */
    if (scale)
    {

      final Rectangle2D boundsShape = s.getBounds2D();
      final double w = boundsShape.getWidth();
      final double h = boundsShape.getHeight();
      double scaleX = 1;
      double scaleY = 1;

      if (w != 0)
      {
        scaleX = dim.getWidth() / w;
      }
      if (h != 0)
      {
        scaleY = dim.getHeight() / h;
      }
      if (scaleX != 1 || scaleY != 1)
      {
        if (s instanceof RectangularShape)
        {
          return ShapeTransform.resizeRect((RectangularShape) s, w * scaleX, h * scaleY);
        }
        if (s instanceof Line2D)
        {
          return ShapeTransform.resizeLine((Line2D) s, w * scaleX, h * scaleY);
        }

        if (keepAR)
        {
          final double scaleFact = Math.min(scaleX, scaleY);
          return performDefaultTransformation(s, scaleFact, scaleFact);
        }
        else
        {
          return performDefaultTransformation(s, scaleX, scaleY);
        }
      }
    }

    // No clipping for rects or lines ..
    if (s instanceof RectangularShape)
    {
      return s;
    }

    if (s instanceof Line2D)
    {
      return s;
    }

    final Area a = new Area(s);
    if (a.isEmpty())
    {
      // don't clip  ... Area does not like lines
      // operations with lines always result in an empty Bounds:(0,0,0,0) area
      //
      // there is no trivial workaround known ...
      // except by reimplementing the clipping, and this is not a option - never!
      return s;
    }


    // mask everything outside of the clipping area
    final Rectangle2D shape = s.getBounds2D();
    final Rectangle2D clip = new Rectangle2D.Double(shape.getX(),
        shape.getY(), dim.getWidth(), dim.getHeight());

    final Area clipArea = new Area(clip);
    clipArea.subtract(new Area(clip.createIntersection(shape)));

    a.subtract(new Area(clipArea));
    return a;
  }

  private static Shape performDefaultTransformation (final Shape shape, final double scaleX, final double scaleY)
  {
    /**
     * Apply the normalisation shape transform ... bring the shape to pos (0,0)
     */
    final Rectangle2D bounds = shape.getBounds2D();
    AffineTransform af
        = AffineTransform.getTranslateInstance(0 - bounds.getX(), 0 - bounds.getY());
    // apply normalisation translation ...
    Shape s = af.createTransformedShape(shape);

    af = AffineTransform.getScaleInstance(scaleX, scaleY);
    // apply scaling ...
    s = af.createTransformedShape(s);

    // now retranslate the shape to its original position ...
    af = AffineTransform.getTranslateInstance(bounds.getX(), bounds.getY());
    return af.createTransformedShape(s);
  }

  public static Shape translateShape (final Shape s, final double x, final double y)
  {
    if (s instanceof RectangularShape)
    {
      final RectangularShape rect = (RectangularShape) s;
      final RectangularShape retval = (RectangularShape) rect.clone();
      retval.setFrame(retval.getX() + x, retval.getY() + y, retval.getWidth(), retval.getHeight());
      return retval;
    }
    if (s instanceof Line2D)
    {
      final Line2D line = (Line2D) s;
      final Line2D retval = (Line2D) line.clone();
      retval.setLine(retval.getX1() + x, retval.getY1() + y,
                     retval.getX2() + x, retval.getY2() + y);
      return retval;
    }

    final AffineTransform af = AffineTransform.getTranslateInstance(x, y);
    final Shape transformedShape = af.createTransformedShape(s);
    return transformedShape;
  }
}
