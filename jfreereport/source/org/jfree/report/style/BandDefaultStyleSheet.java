/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: BandDefaultStyleSheet.java,v 1.4 2005/01/30 23:37:25 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadoc comments (DG);
 *
 */

package org.jfree.report.style;

import org.jfree.report.util.geom.StrictBounds;
import org.jfree.ui.FloatDimension;

/**
 * A default band style sheet. This StyleSheet defines the default attribute
 * values for all Bands.
 *
 * @author Thomas Morgner
 */
public class BandDefaultStyleSheet extends ElementDefaultStyleSheet
{
  /** A shared default style-sheet. */
  private static BandDefaultStyleSheet defaultStyle;

  /**
   * Creates a new default style sheet.
   */
  protected BandDefaultStyleSheet()
  {
    super("GlobalBand");
    setLocked(false);
    setStyleProperty(MINIMUMSIZE, new FloatDimension(0, 0));
    setStyleProperty(MAXIMUMSIZE, new FloatDimension(Short.MAX_VALUE, Short.MAX_VALUE));
    setStyleProperty(BOUNDS, new StrictBounds());
    setStyleProperty(BandStyleKeys.PAGEBREAK_AFTER, Boolean.FALSE);
    setStyleProperty(BandStyleKeys.PAGEBREAK_BEFORE, Boolean.FALSE);
    setStyleProperty(BandStyleKeys.DISPLAY_ON_FIRSTPAGE, Boolean.TRUE);
    setStyleProperty(BandStyleKeys.DISPLAY_ON_LASTPAGE, Boolean.TRUE);
    setLocked(true);
  }

  /**
   * Returns the default band style sheet.
   *
   * @return the style-sheet.
   */
  public static final BandDefaultStyleSheet getBandDefaultStyle()
  {
    if (defaultStyle == null)
    {
      defaultStyle = new BandDefaultStyleSheet();
    }
    return defaultStyle;
  }
}
