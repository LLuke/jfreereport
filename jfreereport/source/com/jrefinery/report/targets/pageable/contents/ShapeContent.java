/**
 * Date: Nov 20, 2002
 * Time: 8:16:54 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.contents;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class ShapeContent  implements Content
{
  private Shape shape;
  private Rectangle2D bounds;

  public ShapeContent(Shape s)
  {
    this (s, s.getBounds2D());
  }

  public ShapeContent(Shape s, Rectangle2D bounds)
  {
    this.shape = s;
    this.bounds = bounds;
  }

  // get all contentParts making up that content or null, if this class
  // has no subcontents
  public Content getContentPart(int part)
  {
    return null;
  }

  public int getContentPartCount()
  {
    return 0;
  }

  public Shape getShape ()
  {
    return shape;
  }

  // return a shape that starts a 0.0,0.0
  public Shape getNormalizedShape ()
  {
    Rectangle2D bounds = shape.getBounds2D();
    return AffineTransform.getTranslateInstance(0 - bounds.getX(),0 - bounds.getY()).createTransformedShape(getShape());
  }

  public Rectangle2D getBounds()
  {
    return bounds.getBounds2D();
  }

  public ContentType getContentType()
  {
    return ContentType.Shape;
  }

  public Content getContentForBounds(Rectangle2D bounds)
  {
    Rectangle2D newBounds = bounds.createIntersection(getBounds());
    return new ShapeContent(getShape(), newBounds);
  }

  public Rectangle2D getMinimumContentSize()
  {
    return getBounds();
  }
}

