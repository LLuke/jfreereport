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
 * LabelElement.java
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
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Font;

/**
 * An element that draws static text within a report band.
 */
public class LabelElement extends TextElement {

    /** Text to display. */
    protected String label;

    /**
     * Constructs a label element using integer coordinates.
     *
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param string label The label text.
     */
    public LabelElement(String name,
                        int x, int y, int w, int h,
                        String label) {

        this(name,
             x, y, w, h,
             DEFAULT_PAINT,
             DEFAULT_FONT,
             DEFAULT_FONT_NAME,
             DEFAULT_FONT_STYLE,
             DEFAULT_FONT_SIZE,
             DEFAULT_ALIGNMENT,
             label);


    }

    /**
     * Constructs a label element using integer coordinates.
     *
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param string label The label text.
     */
    public LabelElement(String name,
                        int x, int y, int w, int h,
                        Paint paint, Font font, String fontName, int fontStyle, int fontSize,
                        int alignment, String label) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment);
        this.label = label;

    }

    /**
     * Constructs a label element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param string label The label text.
     */
    public LabelElement(String name,
                        float x, float y, float w, float h,
                        String label) {

        this(name,
             x, y, w, h,
             DEFAULT_PAINT,
             DEFAULT_FONT,
             DEFAULT_FONT_NAME,
             DEFAULT_FONT_STYLE,
             DEFAULT_FONT_SIZE,
             DEFAULT_ALIGNMENT,
             label);

    }

    /**
     * Constructs a label element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param string label The label text.
     */
    public LabelElement(String name,
                        float x, float y, float w, float h,
                        Paint paint, Font font, String fontName, int fontStyle, int fontSize,
                        int alignment, String label) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment);
        this.label = label;

    }

    /**
     * Returns the label.
     * @return The label.
     */
    public String getFormattedText() {
        return label;
    }

}