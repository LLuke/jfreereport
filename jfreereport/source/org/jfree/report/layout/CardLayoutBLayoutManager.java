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
 * -----------------------------
 * CardLayoutBLayoutManager.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CardLayoutBLayoutManager.java,v 1.3 2003/09/11 22:17:09 taqua Exp $
 *
 * Changes
 * -------
 * 24.03.2003 : Initial version
 */
package org.jfree.report.layout;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.util.ElementLayoutInformation;
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
public strictfp class CardLayoutBLayoutManager extends AbstractBandLayoutManager
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
  public Dimension2D minimumLayoutSize(final Band b, final Dimension2D containerDims)
  {
    final ElementLayoutInformation eli = createLayoutInformationForMinimumSize(b, containerDims);

    final Element[] elements = b.getElementArray();
    final Dimension2D retval = (Dimension2D) eli.getMinimumSize().clone();
    final Dimension2D tmpDim = null;
    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      unionMax(retval, computeMinimumSize(e, containerDims, tmpDim));
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
  public Dimension2D preferredLayoutSize(final Band b, final Dimension2D containerDims)
  {
    final ElementLayoutInformation eli = createLayoutInformationForPreferredSize(b, containerDims);
    if (eli.getPreferredSize() != null)
    {
      return eli.getPreferredSize();
    }

    final Element[] elements = b.getElementArray();
    final Dimension2D retval = (Dimension2D) eli.getMinimumSize().clone();
    final Dimension2D tmpDim = null;

    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      unionMax(retval, computePreferredSize(e, containerDims, tmpDim));
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
  private void unionMax(final Dimension2D retval, final Dimension2D elementDim)
  {
    retval.setSize(StrictMath.max(elementDim.getWidth(), retval.getWidth()),
        StrictMath.max(elementDim.getHeight(), retval.getHeight()));
  }

  /**
   * Performs the layout of a band.
   *
   * @param b  the band.
   */
  public void doLayout(final Band b)
  {
    final Rectangle2D parentBounds = BandLayoutManagerUtil.getBounds(b, null);
    if (parentBounds == null)
    {
      throw new IllegalStateException("Need the parent's bound set");
    }

    final Dimension2D parentDim = new FloatDimension((float) parentBounds.getWidth(),
        (float) parentBounds.getHeight());

    final Dimension2D dim = preferredLayoutSize(b, parentDim);
    final Element[] elements = b.getElementArray();
    final LayoutSupport layoutSupport = getLayoutSupport();
    dim.setSize(align((float) dim.getWidth(), layoutSupport.getHorizontalAlignmentBorder()),
        align((float) dim.getHeight(), layoutSupport.getVerticalAlignmentBorder()));

    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      if (e.isVisible() == false)
      {
        continue;
      }

      // here apply the bounds ...
      final Rectangle2D bounds
          = new Rectangle2D.Float(0, 0, (float) dim.getWidth(), (float) dim.getHeight());
      BandLayoutManagerUtil.setBounds(e, bounds);
      if (e instanceof Band)
      {
        final BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e, getLayoutSupport());
        lm.doLayout((Band) e);
      }
    }
  }

  /**
   * Clears any cached items used by the layout manager. Invalidates the layout.
   *
   * @param container  the container band.
   */
  public void invalidateLayout(final Band container)
  {
  }
}
