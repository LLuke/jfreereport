/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * ------------
 * Element.java
 * ------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ElementConstants.java,v 1.6 2002/09/13 15:38:04 mungady Exp $
 *
 * Changes
 * -------
 * 05-Mar-2002 : Version 1 (DG);
 * 10-May-2002 : Removed DEFAULT_FONT.. constants. The font can be determined on construction
 *               time (TM);
 * 16-May-2002 : Updated source header (DG);
 * 13-Nov-2002 : Added vertical alignment constants (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Color;
import java.awt.Paint;

/**
 * Useful constants for report elements.
 *
 * @author DG
 */
public interface ElementConstants
{

  /** Useful constant representing left-alignment. */
  public static final int LEFT = 1;

  /** Useful constant representing right-alignment. */
  public static final int RIGHT = 2;

  /** Useful constant representing center-alignment. */
  public static final int CENTER = 3;

  /** Constant for vertical alignment (top). */
  public static final int TOP = 4;

  /** Constant for vertical alignment (bottom). */
  public static final int BOTTOM = 5;

  /** Constant for vertical alignment (middle). */
  public static final int MIDDLE = 3;

  /** The default paint. */
  public static final Paint DEFAULT_PAINT = Color.black;

  /** The default horizontal alignment. */
  public static int DEFAULT_ALIGNMENT = Element.LEFT;

  /** The default vertical alignment. */
  public static int DEFAULT_VERTICAL_ALIGNMENT = Element.TOP;

}
