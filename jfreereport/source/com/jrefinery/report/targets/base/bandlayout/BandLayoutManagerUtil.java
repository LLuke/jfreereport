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
 * --------------------------
 * BandLayoutManagerUtil.java
 * --------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandLayoutManagerUtil.java,v 1.7 2003/02/17 16:07:19 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added header and Javadoc comments (DG);
 *
 */

package com.jrefinery.report.targets.base.bandlayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * A collection of utility methods for use by classes that implement the BandLayoutManager 
 * interface.
 *
 * @author Thomas Morgner
 */
public class BandLayoutManagerUtil
{
  /**
   * Returns the layout manager for an element in a report.
   *
   * @param e  the element.
   * @param ot  the output target.
   *
   * @return the layout manager.
   */
  public static BandLayoutManager getLayoutManager (Element e, LayoutSupport ot)
  {
    BandLayoutManager lm =
        (BandLayoutManager) e.getStyle().getStyleProperty(BandLayoutManager.LAYOUTMANAGER);

    if (ot == null)
      throw new NullPointerException();

    if (lm == null)
    {
      lm = ot.getDefaultLayoutManager();
      e.getStyle().setStyleProperty(BandLayoutManager.LAYOUTMANAGER, lm);
    }

    // always update the layout support ...
    if (lm != null)
    {
      lm.setLayoutSupport(ot);
    }

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
   */
  public static Rectangle2D getBounds (Element e, Rectangle2D bounds)
  {
    if (bounds == null)
    {
      bounds = new Rectangle2D.Float ();
    }
    bounds.setRect((Rectangle2D) e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS));
    return bounds;
  }

  /**
   * Sets the bounds for an element.
   *
   * @param e  the element.
   * @param bounds  the new bounds.
   */
  public static void setBounds (Element e, Rectangle2D bounds)
  {
    e.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, bounds.getBounds2D());
  }

  public static Rectangle2D doLayout (Band band, LayoutSupport support, float width, float height)
  {
    BandLayoutManager lm
        = BandLayoutManagerUtil.getLayoutManager(band, support);
    // in this layouter the width of a band is always the full page width
    Dimension2D fdim = lm.minimumLayoutSize(band, new FloatDimension(width, height));

    // the height is redefined by the band's requirements to support
    // the dynamic elements.
    Log.debug ("Band Defined a MinimumSize: " + fdim);
    height = (float) fdim.getHeight();
    Rectangle2D bounds = new Rectangle2D.Float(0, 0, width, height);
    band.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, bounds);
    lm.doLayout(band);
    return bounds;
  }
}
