/**
 * Date: Dec 1, 2002
 * Time: 8:13:25 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;

public abstract class BoundsAlignment
{
  protected Rectangle2D outerBounds;

  public BoundsAlignment (Rectangle2D bounds)
  {
    outerBounds = bounds;
  }

  public abstract Rectangle2D align (Rectangle2D inner);
}
