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
 * -------------------
 * RightAlignment.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LeftAlignment.java,v 1.4 2002/12/06 20:34:13 taqua Exp $
 *
 * Changes
 * -------
 * 10-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;

/**
 * A utility class that can align a rectangle to the bottom edge of the current
 * bounds.
 *
 * @author Thomas Morgner
 */
public class RightAlignment extends HorizontalBoundsAlignment
{
  /**
   * Creates a new alignment object.
   *
   * @param bounds  the reference bounds.
   */
  public RightAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  /**
   * Aligns a rectangle to the right of the reference bounds.
   *
   * @param rect  the rectangle.
   */
  public Rectangle2D align(Rectangle2D rect)
  {
    if (rect == null)
    {
      throw new NullPointerException("Rect must not be null");
    }
    rect = outerBounds.createIntersection(rect);
    double x = outerBounds.getX() + outerBounds.getWidth() - rect.getWidth();
    double y = rect.getY();
    double w = rect.getWidth();
    double h = rect.getHeight();
    return new Rectangle2D.Double(x, y, w, h);
  }
}
