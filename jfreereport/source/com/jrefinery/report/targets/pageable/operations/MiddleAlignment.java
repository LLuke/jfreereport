/**
 * Date: Dec 1, 2002
 * Time: 8:15:44 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;

public class MiddleAlignment extends VerticalBoundsAlignment
{
  public MiddleAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  public Rectangle2D align(Rectangle2D inner)
  {
    if (inner == null) throw new NullPointerException("Inner Boud must not be null");
    inner = outerBounds.createIntersection(inner);
    double x = inner.getX();
    double y = outerBounds.getY() + ((outerBounds.getHeight() - inner.getHeight()) / 2);
    double w = inner.getWidth();
    double h = inner.getHeight();
    return new Rectangle2D.Double(x,y,w,h);
  }
}
