/**
 * Date: Dec 1, 2002
 * Time: 8:13:47 PM
 *
 * $Id: LeftAlignment.java,v 1.1 2002/12/02 17:56:58 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;

public class LeftAlignment extends HorizontalBoundsAlignment
{
  public LeftAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  public Rectangle2D align(Rectangle2D inner)
  {
    if (inner == null) throw new NullPointerException("Inner Bound must not be null");
    Log.debug ("AlignStart: " + inner + " Outer: " + outerBounds);
    inner = outerBounds.createIntersection(inner);
    double x = outerBounds.getX();
    double y = inner.getY();
    double w = Math.min (inner.getWidth(), outerBounds.getWidth());
    double h = Math.min (inner.getHeight(),outerBounds.getHeight());

    Log.debug ("AlignComplete: " + x + ", " + y + ", " + w + ", " + h);
    return new Rectangle2D.Double(x,y,w,h);
  }
}
