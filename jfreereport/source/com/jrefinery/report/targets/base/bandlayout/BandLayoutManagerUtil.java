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
 * $Id: BandLayoutManagerUtil.java,v 1.1 2003/01/29 03:13:01 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added header and Javadoc comments (DG);
 *
 */

package com.jrefinery.report.targets.base.bandlayout;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.LayoutSupport;
import com.jrefinery.report.targets.style.ElementStyleSheet;

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
  
}
