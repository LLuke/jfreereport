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
 * ----------------------------
 * VerticalBoundsAlignment.java
 * ----------------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: VerticalBoundsAlignment.java,v 1.6 2003/11/01 19:52:28 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.operations;

import java.awt.geom.Rectangle2D;

/**
 * An abstract base class for performing vertical alignment.
 *
 * @author Thomas Morgner
 */
public abstract strictfp class VerticalBoundsAlignment extends BoundsAlignment
{

  /**
   * Creates a new horizontal alignment object.
   *
   * @param bounds  the bounds.
   */
  protected VerticalBoundsAlignment(final Rectangle2D bounds)
  {
    super(bounds);
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object.
   */
  public String toString ()
  {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(this.getClass().getName());
    buffer.append("={bounds=");
    buffer.append(getReferenceBounds());
    buffer.append("}");
    return buffer.toString();
  }

}
