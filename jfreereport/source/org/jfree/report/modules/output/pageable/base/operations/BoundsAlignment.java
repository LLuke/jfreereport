/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * --------------------
 * BoundsAlignment.java
 * --------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BoundsAlignment.java,v 1.1 2003/07/07 22:44:06 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.operations;

import java.awt.geom.Rectangle2D;

/**
 * An abstract base class for performing alignment of one <code>Rectangle2D</code> to another.
 *
 * @author Thomas Morgner
 */
public abstract class BoundsAlignment
{
  /** The bounds against which the alignment is performed. */
  private Rectangle2D referenceBounds;

  /**
   * Creates a new alignment object.
   *
   * @param bounds  the reference bounds (alignment will be performed relative to this rectangle).
   */
  protected BoundsAlignment(final Rectangle2D bounds)
  {
    if (bounds == null)
    {
      throw new NullPointerException("Bounds are null");
    }
    this.referenceBounds = bounds;
  }

  /**
   * Returns the internal reference bounds. This is no clone or copy, so take
   * care!
   *
   * @return the reference bounds.
   */
  protected Rectangle2D getReferenceBounds()
  {
    return referenceBounds;
  }

  /**
   * Aligns a rectangle with this object's reference bounds.
   * <P>
   * Subclasses determine the exact alignment behaviour (for example, the
   * {@link TopAlignment}
   * class will align a rectangle to the top of the reference bounds).
   *
   * @param r  the rectangle to be aligned with this object's reference bounds.
   *
   * @return the aligned rectangle.
   */
  public abstract Rectangle2D align(Rectangle2D r);
}
