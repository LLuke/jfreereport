/**
 * Date: Jan 27, 2003
 * Time: 5:58:07 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ShapeTransform
{
  public static Shape transformShape (Shape s, boolean scale, boolean keepAR, Point2D point, Dimension2D dim)
  {
    AffineTransform af = AffineTransform.getTranslateInstance(point.getX(), point.getY());

    /**
     * Always scale to the maximum bounds ...
     */
    if (scale)
    {
      Rectangle2D boundsShape = s.getBounds2D();
      double w = boundsShape.getWidth();
      double h = boundsShape.getHeight();
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
        if (keepAR)
        {
          double scaleFact = Math.min (scaleX, scaleY);
          af.concatenate(AffineTransform.getScaleInstance(scaleFact, scaleFact));
        }
        else
        {
          af.concatenate(AffineTransform.getScaleInstance(scaleX, scaleY));
        }
      }
    }
    return af.createTransformedShape(s);
  }
}
