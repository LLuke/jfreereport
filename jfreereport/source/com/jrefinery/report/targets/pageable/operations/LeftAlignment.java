/**
 * Date: Dec 1, 2002
 * Time: 8:13:47 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;

public class LeftAlignment extends HorizontalBoundsAlignment
{
  public LeftAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  public Rectangle2D align(Rectangle2D inner)
  {
    if (inner == null) throw new NullPointerException("Inner Boud must not be null");
    inner = outerBounds.createIntersection(inner);
    double x = outerBounds.getX();
    double y = inner.getY();
    double w = Math.min (inner.getWidth(), outerBounds.getWidth());
    double h = Math.min (inner.getHeight(),outerBounds.getHeight());
    return new Rectangle2D.Double(x,y,w,h);
  }
}
