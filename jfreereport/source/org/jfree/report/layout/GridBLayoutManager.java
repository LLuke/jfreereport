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
 * -----------------------
 * GridBLayoutManager.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: GridBLayoutManager.java,v 1.5 2003/09/13 15:14:40 taqua Exp $
 *
 * Changes
 * -------
 * 22.03.2003 : Initial version
 */
package org.jfree.report.layout;

import java.awt.geom.Dimension2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.util.ElementLayoutInformation;

/**
 * A layout manager.
 *
 * @author Thomas Morgner.
 */
public strictfp class GridBLayoutManager extends AbstractBandLayoutManager
{
  /**
   * Default constructor.
   */
  public GridBLayoutManager()
  {
  }

  /**
   * Calculates the preferred layout size for a band.
   *
   * @param b  the band.
   * @param containerDims the bounds of the surrounding container.
   *
   * @return the preferred size.
   */
  public Dimension2D preferredLayoutSize
      (final Band b, final Dimension2D containerDims, final LayoutSupport support)
  {
    final ElementLayoutInformation eli = createLayoutInformationForPreferredSize(b, containerDims);
    if (eli.getPreferredSize() != null)
    {
      return eli.getPreferredSize();
    }

    // Now adjust the defined sizes by using the elements stored in the band.
    final Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
      // todo ...
    }
    return null;
  }

  /**
   * Calculates the minimum layout size for a band.
   *
   * @param b  the band.
   * @param containerBounds the bounds of the surrounding container.
   *
   * @return the minimum size.
   */
  public Dimension2D minimumLayoutSize
      (final Band b, final Dimension2D containerBounds, final LayoutSupport support)
  {
    // Check the position of the elements inside and calculate the minimum width
    // needed to display all elements
    // final Element[] elements = b.getElementArray();
    return null;
  }

  /**
   * Performs the layout of a band.
   *
   * @param b  the band.
   */
  public void doLayout(final Band b, final LayoutSupport support)
  {
  }

  /**
   * Clears any cached items used by the layout manager. Invalidates the layout.
   *
   * @param container  the container.
   */
  public void invalidateLayout(final Band container)
  {
  }
}
