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
 * -----------
 * Cursor.java
 * -----------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 *
 */

package com.jrefinery.report;

/**
 * A utility class for keeping track of the current position on a report page.
 */
public class Cursor {

    /** The y-coordinate. */
    public float y;

    /** The left edge of the page. */
    public float pageLeft;

    /** The bottom of the page. */
    public float pageBottom;

    /**
     * Constructs a new cursor.
     */
    public Cursor() {
    }

    /**
     * Adds the specified amount to the y-coordinate.
     * @param amount The amount that the cursor should advance down the page.
     */
    public void advance(float amount) {
        y = y + amount;
    }

    /**
     * Returns true if there is space for a band with the specified height, and false otherwise.
     * @param height The height of the proposed band.
     * @return A flag indicating whether or not there is room to print the band.
     */
    public boolean spaceFor(float height) {
        return (y+height<pageBottom);
    }

}