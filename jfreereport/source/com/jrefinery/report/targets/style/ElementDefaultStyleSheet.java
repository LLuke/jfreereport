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
 * ElementDefaultStyleSheet.java
 * -----------------------------
 * (C)opyright 2002, 2003 by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementDefaultStyleSheet.java,v 1.3 2002/12/07 20:53:13 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadoc comments (DG);
 *
 */

package com.jrefinery.report.targets.style;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FloatDimension;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

/**
 * The default element style sheet. This style sheet defines default attribute
 * values for all elements.
 * 
 * @author Thomas Morgner
 */
public class ElementDefaultStyleSheet extends ElementStyleSheet
{
  /** The default paint. */
  public static final Paint DEFAULT_PAINT = Color.black;
  
  /** The default font. */
  public static final Font DEFAULT_FONT = new Font ("Serif", Font.PLAIN, 10);

  /** A shared default style-sheet. */
  private static ElementDefaultStyleSheet defaultStyle;

  /**
   * Creates a new style sheet.
   */
  protected ElementDefaultStyleSheet()
  {
    super("GlobalBand");  // should this be 'GlobalElement'??
    setStyleProperty(MINIMUMSIZE, new FloatDimension(0, 0));
    setStyleProperty(MAXIMUMSIZE, new FloatDimension(Short.MAX_VALUE, Short.MAX_VALUE));
    setStyleProperty(BOUNDS, new Rectangle2D.Float());
    setStyleProperty(PAINT, DEFAULT_PAINT);
    setFontStyleProperty(DEFAULT_FONT);
    setStyleProperty(VALIGNMENT, ElementAlignment.BOTTOM);
    setStyleProperty(ALIGNMENT, ElementAlignment.LEFT);
    setStyleProperty(VISIBLE, Boolean.TRUE);
  }

  /**
   * Returns the default element style sheet.
   *
   * @return the style-sheet.
   */
  public static final ElementDefaultStyleSheet getDefaultStyle ()
  {
    if (defaultStyle == null)
    {
      defaultStyle = new ElementDefaultStyleSheet();
    }
    return defaultStyle;
  }

}
