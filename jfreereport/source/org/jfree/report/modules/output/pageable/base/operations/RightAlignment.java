/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RightAlignment.java,v 1.2 2003/08/24 15:03:59 taqua Exp $
 *
 * Changes
 * -------
 * 10-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.operations;

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
  public RightAlignment(final Rectangle2D bounds)
  {
    super(bounds);
  }

  /**
   * Aligns a rectangle to the right of the reference bounds.
   *
   * @param rect  the rectangle.
   *
   * @return the aligned rectangle.
   */
  public Rectangle2D align(Rectangle2D rect)
  {
    if (rect == null)
    {
      throw new NullPointerException("Rect must not be null");
    }
    // rect = getReferenceBounds().createIntersection(rect);
    final float w = (float) Math.min (rect.getWidth(), getReferenceBounds().getWidth());
    final float x = (float) (getReferenceBounds().getX() +
        getReferenceBounds().getWidth() - w);
    final float y = (float) rect.getY();
    final float h = (float) rect.getHeight();
    rect.setRect(x, y, w, h);
    return rect;
  }
}
