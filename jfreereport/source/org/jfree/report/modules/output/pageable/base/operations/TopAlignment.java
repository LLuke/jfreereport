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
 * -----------------
 * TopAlignment.java
 * -----------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TopAlignment.java,v 1.7 2004/05/07 12:53:06 mungady Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.operations;

import org.jfree.report.util.geom.StrictBounds;

/**
 * A utility class that can align a rectangle to the top edge of the reference bounds.
 *
 * @author Thomas Morgner
 */
public strictfp class TopAlignment extends VerticalBoundsAlignment
{
  /**
   * Creates a new alignment object.
   *
   * @param bounds  the reference bounds.
   */
  public TopAlignment(final StrictBounds bounds)
  {
    super(bounds);
  }

  /**
   * Aligns a rectangle to the top of the current bounds.
   *
   * @param r  the region to align (<code>null</code> not permitted).
   *
   * @return the aligned rectangle.
   */
  public StrictBounds align(final StrictBounds r)
  {
    if (r == null)
    {
      throw new NullPointerException("TopAlignment.align(...) : null not permitted.");
    }
    final long x = r.getX();
    final long y = getReferenceBounds().getY();
    final long h = Math.min (r.getHeight(), getReferenceBounds().getHeight());
    final long w = Math.min(r.getWidth(), getReferenceBounds().getWidth());

    r.setRect(x, y, w, h);
    return r;
  }
}
