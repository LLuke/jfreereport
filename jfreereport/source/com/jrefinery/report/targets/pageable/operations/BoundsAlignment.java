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
 * BoundsAlignment.java
 * --------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LogicalPage.java,v 1.2 2002/12/03 16:30:49 mungady Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;

/**
 * An abstract base class for performing alignment.
 *
 * @author Thomas Morgner
 */
public abstract class BoundsAlignment
{
  /** The bounds against which the alignment is performed. */
  protected Rectangle2D outerBounds;

  /**
   * Creates a new alignment object.
   *
   * @param bounds  the alignment bounds.
   */
  protected BoundsAlignment (Rectangle2D bounds)
  {
    this.outerBounds = bounds;
  }

  /**
   * Aligns a rectangle with this object's bounds.  Subclasses determine the exact
   * alignment behaviour.
   *
   * @param inner  the rectangle to be aligned with this object's bounds.
   *
   * @return the aligned rectangle.
   */
  public abstract Rectangle2D align (Rectangle2D inner);
}
