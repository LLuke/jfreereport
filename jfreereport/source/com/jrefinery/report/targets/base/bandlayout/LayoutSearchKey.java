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
 * LayoutSearchKey.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LayoutSearchKey.java,v 1.3 2003/06/27 14:25:23 taqua Exp $
 *
 * Changes
 * -------
 * 06.04.2003 : Initial version
 */
package com.jrefinery.report.targets.base.bandlayout;

import java.awt.geom.Dimension2D;

import com.jrefinery.report.Element;

/**
 * A layout search key.
 *
 * @author Thomas Morgner.
 */
public class LayoutSearchKey extends LayoutCacheKey
{
  /**
   * Returns <code>true</code> to indicate that this is a search key.
   *
   * @return <code>true</code>.
   */
  public boolean isSearchKey()
  {
    return true;
  }

  /**
   * Sets the search constraint.
   *
   * @param e  the element.
   * @param dim  the dimension.
   */
  public void setSearchConstraint(final Element e, final Dimension2D dim)
  {
    setElement(e);
    setParentDim(dim);
  }
}
