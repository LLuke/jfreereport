/**
 * Date: Dec 1, 2002
 * Time: 8:13:51 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;

public class RightAlignment extends HorizontalBoundsAlignment
{
  public RightAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  public Rectangle2D align(Rectangle2D inner)
  {
    if (inner == null) throw new NullPointerException("Inner Boud must not be null");
    inner = outerBounds.createIntersection(inner);
    double x = outerBounds.getX() + outerBounds.getWidth() - inner.getWidth();
    double y = inner.getY();
    double w = inner.getWidth();
    double h = inner.getHeight();
    return new Rectangle2D.Double(x,y,w,h);
  }
}
