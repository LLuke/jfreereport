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
 * -----------------------
 * GridBLayoutManager.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: GridBLayoutManager.java,v 1.2 2003/04/09 15:49:53 mungady Exp $
 *
 * Changes
 * -------
 * 22.03.2003 : Initial version
 */
package com.jrefinery.report.targets.base.bandlayout;

import java.awt.geom.Dimension2D;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.ElementLayoutInformation;

/**
 * A layout manager.
 *
 * @author Thomas Morgner.
 */
public class GridBLayoutManager extends AbstractBandLayoutManager
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
  public Dimension2D preferredLayoutSize(Band b, Dimension2D containerDims)
  {
    ElementLayoutInformation eli = createLayoutInformationForPreferredSize(b, containerDims);
    if (eli.getPreferredSize() != null)
    {
      return eli.getPreferredSize();
    }

    // Now adjust the defined sizes by using the elements stored in the band.
    Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
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
  public Dimension2D minimumLayoutSize(Band b, Dimension2D containerBounds)
  {
    // Check the position of the elements inside and calculate the minimum width
    // needed to display all elements
    Element[] elements = b.getElementArray();
    return null;
  }

  /**
   * Performs the layout of a band.
   *
   * @param b  the band.
   */
  public void doLayout(Band b)
  {
  }

  /**
   * Clears any cached items used by the layout manager. Invalidates the layout.
   *
   * @param container  the container.
   */
  public void invalidateLayout(Band container)
  {
  }

  /**
   * Adds the specified component to the layout, the specified constraints are stored
   * in the elements style sheet.
   *
   * @param e the element to be added to the layout manager.
   */
  public void addLayoutElement(Element e)
  {
  }

  /**
   * Removed the specified component from the layout.
   *
   * @param e the element to be removed from the layout manager.
   */
  public void removeLayoutElement(Element e)
  {
  }
}
