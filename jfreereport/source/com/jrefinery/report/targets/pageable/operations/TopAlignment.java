/**
 * Date: Dec 1, 2002
 * Time: 8:14:43 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;

public class TopAlignment extends VerticalBoundsAlignment
{
  public TopAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  public Rectangle2D align(Rectangle2D inner)
  {
    if (inner == null) throw new NullPointerException("Inner Boud must not be null");
    inner = outerBounds.createIntersection(inner);
    double x = inner.getX();
    double y = outerBounds.getY();
    double w = Math.min (inner.getWidth(), outerBounds.getWidth());
    double h = Math.min (inner.getHeight(),outerBounds.getHeight());

    return new Rectangle2D.Double(x,y,w,h);
  }
}
