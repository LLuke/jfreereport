/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * CenterAlignment.java
 * --------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CenterAlignment.java,v 1.8 2004/05/07 14:29:49 mungady Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.operations;

import org.jfree.report.util.geom.StrictBounds;

/**
 * A utility class that can align a rectangle to the (horizontal) center of the current
 * bounds.
 *
 * @author Thomas Morgner
 */
public strictfp class CenterAlignment extends HorizontalBoundsAlignment
{
  /**
   * Creates a new alignment object.
   *
   * @param bounds  the bounds.
   */
  public CenterAlignment(final StrictBounds bounds)
  {
    super(bounds);
  }

  /**
   * Aligns a rectangle to the (horizontal) center of the current bounds.
   *
   * @param inner  the region to align.
   *
   * @return the aligned rectangle.
   */
  public StrictBounds align(final StrictBounds inner)
  {
    if (inner == null)
    {
      throw new NullPointerException("Inner Bound must not be null");
    }

    final long y = inner.getY();
    final long w = Math.min (inner.getWidth(), getReferenceBounds().getWidth());
    final long x = getReferenceBounds().getX() +
            ((getReferenceBounds().getWidth() - w) / 2);
    final long h = inner.getHeight();
    inner.setRect(x, y, w, h);
    return inner;
  }
}
