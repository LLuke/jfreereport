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
 * --------------------------
 * BandLayoutManagerUtil.java
 * --------------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandLayoutManagerUtil.java,v 1.4 2003/08/25 14:29:29 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added header and Javadoc comments (DG);
 *
 */

package org.jfree.report.layout;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

/**
 * A collection of utility methods for use by classes that implement the BandLayoutManager
 * interface.
 *
 * @author Thomas Morgner
 */
public final class BandLayoutManagerUtil
{
  /**
   * Default constructor.
   */
  private BandLayoutManagerUtil()
  {
  }

  /**
   * Returns the layout manager for an element in a report.
   *
   * @param e  the element.
   * @param ot  the output target.
   *
   * @return the layout manager.
   */
  public static BandLayoutManager getLayoutManager(final Element e, final LayoutSupport ot)
  {
    final BandLayoutManager lm =
        (BandLayoutManager) e.getStyle().getStyleProperty(BandLayoutManager.LAYOUTMANAGER);

    if (ot == null)
    {
      throw new NullPointerException();
    }
    if (lm == null)
    {
      throw new IllegalStateException("There is no layout manager defined for that band.");
    }

    // always update the layout support ...
    lm.setLayoutSupport(ot);
    return lm;
  }

  /**
   * Returns the bounds of an element.
   *
   * @param e  the element.
   * @param bounds  if non-null, this structure will be updated and returned as the result (use
   *                this to avoid creating unnecessary work for the garbage collector).
   *
   * @return the element bounds.
   * @throws java.lang.NullPointerException if the given element is null
   */
  public static Rectangle2D getBounds(final Element e, Rectangle2D bounds)
  {
    if (e == null)
    {
      throw new NullPointerException("Element is null.");
    }
    if (bounds == null)
    {
      bounds = new Rectangle2D.Float();
    }
    bounds.setRect((Rectangle2D) e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS));
    return bounds;
  }

  /**
   * Sets the bounds for an element.
   *
   * @param e  the element.
   * @param bounds  the new bounds.
   * @throws java.lang.NullPointerException if the given element or the bounds are null
   */
  public static void setBounds(final Element e, final Rectangle2D bounds)
  {
    e.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, bounds.getBounds2D());
  }

  /**
   * Performs all required steps to layout the band. The bands height gets initially limited by
   * the specified width and height. The band updates these dimensions after the initial
   * layouting is done. The band will never exceed the specified bounds, they define a
   * hard limit.
   * <p>
   * The bands elements get their BOUNDS property updated
   * to reflect the new layout settings.
   *
   * @param band  the band that should be laid out.
   * @param support  the LayoutSupport implementation used to calculate the bounds of dynamic
   *                 content.
   * @param width  the initial maximum width of the container.
   * @param height  the initial maximum height of the container.
   *
   * @return the bounds for the layouted band. The band itself got updated to
   * contain the new element bounds.
   */
  public static Rectangle2D doLayout(final Band band, final LayoutSupport support,
                                     final float width, final float height)
  {
    if (band == null)
    {
      throw new NullPointerException("Band is null");
    }
    if (support == null)
    {
      throw new NullPointerException("LayoutSupport is null");
    }
    if (width < 0 || height < 0)
    {
      throw new IllegalArgumentException("Width or height is negative.");
    }
    final BandLayoutManager lm
        = BandLayoutManagerUtil.getLayoutManager(band, support);
    // in this layouter the width of a band is always the full page width
    //final Dimension2D fdim = lm.minimumLayoutSize(band, new FloatDimension(width, height));
    final Dimension2D fdim = lm.preferredLayoutSize(band, new FloatDimension(width, height));

    // the height is redefined by the band's requirements to support
    // the dynamic elements.
    final Rectangle2D bounds = new Rectangle2D.Float(0, 0,
        (float) fdim.getWidth(), (float) fdim.getHeight());
    band.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, bounds);
    lm.doLayout(band);
    lm.setLayoutSupport(null);
    return bounds;
  }


}
