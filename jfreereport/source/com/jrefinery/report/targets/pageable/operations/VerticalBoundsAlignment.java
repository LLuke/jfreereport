/**
 * Date: Dec 1, 2002
 * Time: 8:13:55 PM
 *
 * $Id: VerticalBoundsAlignment.java,v 1.1 2002/12/02 17:57:00 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;

public abstract class VerticalBoundsAlignment extends BoundsAlignment
{
  private double horizontalShift;

  public VerticalBoundsAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  public double getHorizontalShift()
  {
    return horizontalShift;
  }

  public void calculateShift (Rectangle2D bounds)
  {
    Rectangle2D alignBounds = align(bounds);
    this.horizontalShift = alignBounds.getY() - bounds.getY();
  }

  public Rectangle2D applyShift (Rectangle2D bounds)
  {
    Rectangle2D retval = bounds.getBounds2D();
    retval.setRect(bounds.getX(),
                   bounds.getY() + getHorizontalShift(),
                   bounds.getWidth(),
                   bounds.getHeight());
    return retval;
  }
}
