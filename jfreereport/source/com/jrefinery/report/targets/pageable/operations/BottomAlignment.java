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
 * --------------------
 * BottomAlignment.java
 * --------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BottomAlignment.java,v 1.2 2003/02/02 23:43:51 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;

/**
 * A utility class that can align a <code>Rectangle2D</code> to the bottom edge of its reference
 * bounds.
 *
 * @author Thomas Morgner
 */
public class BottomAlignment extends VerticalBoundsAlignment
{
  /**
   * Creates a new alignment object.
   *
   * @param bounds  the reference bounds.
   */
  public BottomAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }

  /**
   * Aligns a rectangle to the bottom of the current reference bounds.
   *
   * @param r  the rectangle to align.
   *
   * @return a new rectangle resulting from the alignment operation.
   */
  public Rectangle2D align(Rectangle2D r)
  {
    if (r == null)
    {
      throw new NullPointerException("BottomAlignment.align(...): null not permitted.");
    }
    r = referenceBounds.createIntersection(r);
    double x = r.getX();
    double y = referenceBounds.getY() + referenceBounds.getHeight() - r.getHeight();
    double w = r.getWidth();
    double h = r.getHeight();
    return new Rectangle2D.Double(x, y, w, h);
  }
}
