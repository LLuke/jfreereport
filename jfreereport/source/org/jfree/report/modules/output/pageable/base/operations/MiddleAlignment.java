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
 * ------------------
 * LeftAlignment.java
 * ------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: MiddleAlignment.java,v 1.10 2005/02/23 21:05:29 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.operations;

import org.jfree.report.util.geom.StrictBounds;

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
   * @param bounds the current bounds.
   */
  public MiddleAlignment (final StrictBounds bounds)
  {
    super(bounds);
  }

  /**
   * Aligns a rectangle with the current bounds.
   *
   * @param r the rectangle to align (<code>null</code> not permitted).
   * @return the aligned rectangle.
   */
  public StrictBounds align (final StrictBounds r)
  {
    if (r == null)
    {
      throw new NullPointerException("MiddleAlignment.align(...): null not permitted.");
    }
    final long x = r.getX();
    final long h = Math.min(r.getHeight(), getReferenceBounds().getHeight());
    final long y = (getReferenceBounds().getY()
            + ((getReferenceBounds().getHeight() - h) / 2));
    final long w = r.getWidth();
    r.setRect(x, y, w, h);
    return r;
  }
}
