/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -----------------------------
 * CardLayoutBLayoutManager.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CardLayoutBLayoutManager.java,v 1.6 2003/04/24 18:08:56 taqua Exp $
 *
 * Changes
 * -------
 * 24.03.2003 : Initial version
 */
package com.jrefinery.report.targets.base.bandlayout;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import org.jfree.ui.FloatDimension;

/**
 * A CardLayout object is a layout manager for a container. It treats each
 * component in the container as a card. Only one card is visible at a time,
 * and the container acts as a stack of cards. The first component added to
 * a CardLayout object is the visible component when the container is first
 * displayed.
 * <p>
 * The ordering of cards is determined by the container's own internal
 * ordering of its component objects. CardLayout defines a set of methods
 * that allow an application to flip through these cards sequentially, or
 * to show a specified card. The CardLayout.addLayoutComponent method can be
 * used to associate a string identifier with a given card for fast random
 * access.
 * 
 * @author Thomas Morgner
 */
public class CardLayoutBLayoutManager extends AbstractBandLayoutManager
{
  /**
   * Default constructor.
   */
  public CardLayoutBLayoutManager()
  {
  }
  
  /**
   * Calculates the minimum layout size for a band.
   *
   * @param b  the band.
   * @param containerDims the bounds of the surrounding container.
   *
   * @return the preferred size.
   */
  public Dimension2D minimumLayoutSize(Band b, Dimension2D containerDims)
  {
    ElementLayoutInformation eli = createLayoutInformationForMinimumSize(b, containerDims);

    Element[] elements = b.getElementArray();
    Dimension2D retval = (Dimension2D) eli.getMinimumSize().clone();

    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      unionMax(retval, getMinimumSize(e, containerDims));
    }
    return ElementLayoutInformation.unionMin(eli.getMaximumSize(), retval);
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

    Element[] elements = b.getElementArray();
    Dimension2D retval = (Dimension2D) eli.getMinimumSize().clone();

    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      unionMax(retval, getPreferredSize(e, containerDims));
    }
    return ElementLayoutInformation.unionMin(eli.getMaximumSize(), retval);
  }

  /**
   * Sets the size of <code>retval</code> to have the maximum width and height from the two
   * dimensions.
   * 
   * @param retval  the first dimension (and return value carrier).
   * @param elementDim  the second dimension.
   */
  private void unionMax (Dimension2D retval, Dimension2D elementDim)
  {
    retval.setSize(Math.max(elementDim.getWidth(), retval.getWidth()),
                   Math.max(elementDim.getHeight(), retval.getHeight()));
  }

  /**
   * Performs the layout of a band.
   *
   * @param b  the band.
   */
  public void doLayout(Band b)
  {
    Rectangle2D parentBounds = BandLayoutManagerUtil.getBounds(b, null);
    if (parentBounds == null)
    {
      throw new IllegalStateException("Need the parent's bound set");
    }

    Dimension2D parentDim = new FloatDimension((float) parentBounds.getWidth(),
                                               (float) parentBounds.getHeight());

    Dimension2D dim = preferredLayoutSize(b, parentDim);
    Element[] elements = b.getElementArray();
    LayoutSupport layoutSupport = getLayoutSupport();
    dim.setSize(align((float) dim.getWidth(), layoutSupport.getHorizontalAlignmentBorder()),
                align((float) dim.getHeight(), layoutSupport.getVerticalAlignmentBorder()));

    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      if (e.isVisible() == false)
      {
        continue;
      }

      // here apply the bounds ...
      Rectangle2D bounds 
          = new Rectangle2D.Float(0, 0, (float) dim.getWidth(), (float) dim.getHeight());
      BandLayoutManagerUtil.setBounds(e, bounds);
      if (e instanceof Band)
      {
        BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e, getLayoutSupport());
        lm.doLayout((Band) e);
      }
    }
  }

  /**
   * Clears any cached items used by the layout manager. Invalidates the layout.
   * 
   * @param container  the container band.
   */
  public void invalidateLayout(Band container)
  {
  }
}
