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
 * $Id: MiddleAlignment.java,v 1.3 2002/12/16 17:31:05 mungady Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.base.operations;

import com.jrefinery.report.targets.base.operations.VerticalBoundsAlignment;

import java.awt.geom.Rectangle2D;

/**
 * A utility class that can align a rectangle to the (vertical) middle of the current
 * bounds.
 *
 * @author Thomas Morgner
 */
public class MiddleAlignment extends VerticalBoundsAlignment
{
  /**
   * Creates a new middle alignment object.
   *
   * @param bounds  the current bounds.
   */
  public MiddleAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  /**
   * Aligns a rectangle with the current bounds.
   *
   * @param r  the rectangle to align (<code>null</code> not permitted).
   *
   * @return the aligned rectangle.
   */
  public Rectangle2D align(Rectangle2D r)
  {
    if (r == null)
    {
      throw new NullPointerException("MiddleAlignment.align(...): null not permitted.");
    }
    r = referenceBounds.createIntersection(r);
    double x = r.getX();
    double y = referenceBounds.getY() + ((referenceBounds.getHeight() - r.getHeight()) / 2);
    double w = r.getWidth();
    double h = r.getHeight();
    return new Rectangle2D.Double(x, y, w, h);
  }
}
