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
 * -----------------
 * ShapeElement.java
 * -----------------
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
 * 05-Mar-2002 : Added paint attribute to Element.java (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.AffineTransform;

/**
 * Used to draw shapes (typically lines and boxes) on a report band.
 */
public class ShapeElement extends Element {

    /** The shape to draw. */
    protected Shape shape;

    /**
     * Constructs a shape element.
     *
     * @param name The name of the element.
     * @param shape The shape.
     */
    public ShapeElement(String name, Shape shape) {

        this(name, shape.getBounds2D(), DEFAULT_PAINT);

    }

    /**
     * Constructs a shape element.
     *
     * @param name The name of the element.
     * @param shape The shape.
     * @param paint The paint.
     */
    public ShapeElement(String name, Shape shape, Paint paint) {

        super(name, shape.getBounds2D(), paint);
        this.shape = shape;

    }

    /**
     * Draws the element at its location relative to the band co-ordinates supplied.
     *
     * @param g2 The graphics device.
     * @param band The band.
     * @param bandX The x-coordinate for the element within its band.
     * @param bandY The y-coordinate for the element within its band.
     */
    public void draw(OutputTarget target, Band band, float bandX, float bandY) {

        // set the paint...
        if (this.paint!=null) {
            target.setPaint(this.paint);
        }
        else {
            target.setPaint(band.getDefaultPaint());
        }

        Shape s = this.shape;

        if (s instanceof Line2D) {
            Line2D l = (Line2D)s;
            if ((l.getX1()==l.getX2()) && (l.getY1()==l.getY2())) {
                s = new Line2D.Float(0.0f, (float)l.getY1(),
                                     target.getUsableWidth(), (float)l.getY2());
            }
        }

        target.drawShape(s, bandX, bandY);

    }

}