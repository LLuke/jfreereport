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
 * ImageElement.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morgner
 * Contributor(s):   -;
 *
 * $Id$
 *
 * 24-Apr-2002 : Defines a reference to an Bitmap or Wmf-Image for the reports.
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.AffineTransform;
import java.net.URL;

/**
 * Used to draw images (Gif, JPEG, PNG or wmf) on a report band.
 * PNG Support needs JDK 1.3 or higher.
 *
 * @todo: Performance! and create something to support better drawing
 * of WMF-Elements. Don't handle them as BitMap-Images.
 */
public class ImageElement extends Element {

    /** The image to draw. */
    protected ImageReference image;

    /**
     * Constructs a image element.
     *
     * @param name The name of the element.
     * @param image The image.
     */
    public ImageElement(String name, ImageReference image) 
    {
        this(name, image, DEFAULT_PAINT);

    }

    /**
     * Constructs a image element.
     *
     * @param name The name of the element.
     * @param image The image.
     * @param paint The paint.
     */
    public ImageElement(String name, ImageReference image, Paint paint) {
        super(name, image.getBounds(), paint);
        System.out.println("Image ELement created");
        this.image = image;

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

        System.out.println ("Draw image reference: " + bandX + ", " + bandY);
        // set the paint...
        if (this.paint!=null) {
            target.setPaint(this.paint);
        }
        else {
            target.setPaint(band.getDefaultPaint());
        }
        target.drawImage(image, bandX, bandY);

    }

}