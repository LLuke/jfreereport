/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ------------------
 * LeftAlignment.java
 * ------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LeftAlignment.java,v 1.5 2002/12/11 01:10:41 mungady Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;

/**
 * A utility class that can align a rectangle to the left edge of the current
 * bounds.
 *
 * @author Thomas Morgner
 */
public class LeftAlignment extends HorizontalBoundsAlignment
{
  /**
   * Creates a new left alignment object.
   *
   * @param bounds  the bounds.
   */
  public LeftAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  /**
   * Aligns a rectangle with the current bounds.
   *
   * @param inner  the rectangle to align.
   *
   * @return the aligned rectangle.
   */
  public Rectangle2D align(Rectangle2D inner)
  {
    if (inner == null)
    {
      throw new NullPointerException("Inner Bound must not be null");
    }
    inner = referenceBounds.createIntersection(inner);
    double x = referenceBounds.getX();
    double y = inner.getY();
    double w = Math.min (inner.getWidth(), referenceBounds.getWidth());
    double h = Math.min (inner.getHeight(), referenceBounds.getHeight());

    return new Rectangle2D.Double(x, y, w, h);
  }
}
