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
 * --------------------------
 * BandDefaultStyleSheet.java
 * --------------------------
 * (C)opyright 2002, 2003 by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandDefaultStyleSheet.java,v 1.4 2002/12/12 20:24:03 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadoc comments (DG);
 *
 */

package com.jrefinery.report.targets.style;

import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.pageable.bandlayout.StaticLayoutManager;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

/**
 * A default band style sheet. This StyleSheet defines the default attribute
 * values for all Bands.
 * 
 * @author Thomas Morgner
 */
public class BandDefaultStyleSheet extends BandStyleSheet
{
  /** A shared default style-sheet. */
  private static BandDefaultStyleSheet defaultStyle;

  /**
   * Creates a new default style sheet.
   */
  protected BandDefaultStyleSheet()
  {
    super("GlobalBand");
    setStyleProperty(MINIMUMSIZE, new FloatDimension(0, 0));
    setStyleProperty(MAXIMUMSIZE, new FloatDimension(Short.MAX_VALUE, Short.MAX_VALUE));
    setStyleProperty(StaticLayoutManager.ABSOLUTE_DIM, new FloatDimension(-100, -100));
    setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, new Point2D.Double(0, 0));
    setStyleProperty(BOUNDS, new Rectangle2D.Float());
    setStyleProperty(PAGEBREAK_AFTER, Boolean.FALSE);
    setStyleProperty(PAGEBREAK_BEFORE, Boolean.FALSE);
    setStyleProperty(DISPLAY_ON_FIRSTPAGE, Boolean.TRUE);
    setStyleProperty(DISPLAY_ON_LASTPAGE, Boolean.TRUE);
  }

  /**
   * Returns the default band style sheet.
   *
   * @return the style-sheet.
   */
  public static final BandDefaultStyleSheet getBandDefaultStyle ()
  {
    if (defaultStyle == null)
    {
      defaultStyle = new BandDefaultStyleSheet();
    }
    return defaultStyle;
  }

}
