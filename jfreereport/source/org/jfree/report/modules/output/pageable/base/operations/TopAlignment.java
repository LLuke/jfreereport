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
 * -----------------
 * TopAlignment.java
 * -----------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TopAlignment.java,v 1.4 2003/09/13 15:14:40 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.operations;

import java.awt.geom.Rectangle2D;

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
  public TopAlignment(final Rectangle2D bounds)
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
  public Rectangle2D align(Rectangle2D r)
  {
    if (r == null)
    {
      throw new NullPointerException("TopAlignment.align(...) : null not permitted.");
    }
    final float x = (float) r.getX();
    final float y = (float) getReferenceBounds().getY();
    final float h = (float) Math.min (r.getHeight(), getReferenceBounds().getHeight());
    final float w = (float) Math.min(r.getWidth(), getReferenceBounds().getWidth());

    r.setRect(x, y, w, h);
    return r;
  }
}
