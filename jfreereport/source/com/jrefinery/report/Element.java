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
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Integration of Thomas Morgner's code, plus PDF report generation via iText (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * Base class for all report elements (display items that can appear within a report band).
 */
public abstract class Element implements ElementConstants {

    /** The name of the element. */
    protected String name;

    /** The area that the element occupies within its band. */
    protected Rectangle2D area;

    /** The paint used to draw the element. */
    protected Paint paint;

    /**
     * Constructs an element using integer coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     */
    protected Element(String name, int x, int y, int w, int h, Paint paint) {

        this(name, new Rectangle2D.Float(x, y, w, h), paint);

    }

    /**
     * Constructs an element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     */
    protected Element(String name, float x, float y, float w, float h, Paint paint) {

        this(name, new Rectangle2D.Float(x, y, w, h), paint);

    }

    /**
     * Constructs an element using a Rectangle2D.
     * @param name The name of the element.
     * @param area The location and bounds of the element.
     */
    protected Element(String name, Rectangle2D area, Paint paint) {

        this.name = name;
        this.area = area;
        this.paint = paint;

    }

    /**
     * Returns the name of the element.
     *
     * @return The name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the paint used to draw this element.
     *
     * @return The paint.
     */
    public Paint getPaint() {
        return this.paint;
    }

    /**
     * Draws the element at its location relative to the band co-ordinates supplied.
     *
     * @param target The output target.
     * @param band The band that the element is being drawn inside of.
     * @param bandX The x-coordinate for the element within its band.
     * @param bandY The y-coordinate for the element within its band.
     */
    public abstract void draw(OutputTarget target, Band band, float bandX, float bandY);

}
