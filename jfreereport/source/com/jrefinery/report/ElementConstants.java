/* =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 * 05-Mar-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Color;
import java.awt.Font;

/**
 * Useful constants for report elements.
 */
public interface ElementConstants {

    /** Useful constant representing left-alignment. */
    public static final int LEFT = 1;

    /** Useful constant representing right-alignment. */
    public static final int RIGHT = 2;

    /** Useful constant representing center-alignment. */
    public static final int CENTER = 3;

    /** The default paint. */
    public static final Paint DEFAULT_PAINT = Color.black;

    /** The default element font (null indicates that the font should be derived from the band). */
    public static final Font DEFAULT_FONT = null;

    public static final String DEFAULT_FONT_NAME = null;

    public static final int DEFAULT_FONT_STYLE = -1;

    public static final int DEFAULT_FONT_SIZE = 0;

    /** The default alignment. */
    public static int DEFAULT_ALIGNMENT = Element.LEFT;

}