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
 * ------------------------------
 * HorizontalBoundsAlignment.java
 * ------------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HorizontalBoundsAlignment.java,v 1.2 2002/12/05 12:05:09 mungady Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.base.operations;

import java.awt.geom.Rectangle2D;

/**
 * An abstract base class for performing horizontal alignment.
 *
 * @author Thomas Morgner
 */
public abstract class HorizontalBoundsAlignment extends BoundsAlignment
{
  /**
   * Creates a new horizontal alignment object.
   *
   * @param bounds  the bounds.
   */
  protected HorizontalBoundsAlignment(Rectangle2D bounds)
  {
    super(bounds);
  }
}
