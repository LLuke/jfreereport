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
 * --------------------------
 * NumberFunctionElement.java
 * --------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Font;
import java.text.DecimalFormat;
import com.jrefinery.report.function.Function;

/**
 * Presentation Element for numerical functions.
 */
public class NumberFunctionElement extends FunctionElement {

    /** Useful constant for zero. */
    private static final Number ZERO = new Double(0.0);

    /** The formatting object for this data element. */
    protected DecimalFormat formatter;

    /**
     * Constructs a number element using integer coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param function The name of the function used to populate this element with data.
     * @param format The format string for the number.
     */
    public NumberFunctionElement(String name, int x, int y, int w, int h,
                                 String function, String format) {

        this(name,
             x, y, w, h,
             DEFAULT_PAINT,
             DEFAULT_FONT,
             DEFAULT_FONT_NAME,
             DEFAULT_FONT_STYLE,
             DEFAULT_FONT_SIZE,
             DEFAULT_ALIGNMENT,
             function, format);

    }

    /**
     * Constructs a number element using integer coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param field The name of the field used to populate this element with data.
     * @param format The format string for the number.
     */
    public NumberFunctionElement(String name,
                                 int x, int y, int w, int h,
                                 Paint paint,
                                 Font font, String fontName, int fontStyle, int fontSize,
                                 int alignment, String function, String format) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment, function);
        this.formatter = new DecimalFormat(format);
        this.value = ZERO;

    }

    /**
     * Constructs a number element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param field The name of the field used to populate this element with data.
     * @param format The format string for the number.
     */
    public NumberFunctionElement(String name, float x, float y, float w, float h,
                                 String function, String format) {

        this(name,
             x, y, w, h,
             DEFAULT_PAINT,
             DEFAULT_FONT,
             DEFAULT_FONT_NAME,
             DEFAULT_FONT_STYLE,
             DEFAULT_FONT_SIZE,
             DEFAULT_ALIGNMENT,
             function, format);

    }

    /**
     * Constructs a number element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param field The name of the field used to populate this element with data.
     * @param format The format string for the number.
     */
    public NumberFunctionElement(String name, float x, float y, float w, float h,
                                 Paint paint, Font font, String fontName, int fontStyle, int fontSize,
                                 int alignment,
                                 String function, String format) {

        super(name, x, y, w, h, paint, font, fontName, fontSize, fontStyle, alignment, function);
        this.formatter = new DecimalFormat(format);
        this.value = ZERO;

    }

    /**
     * Returns a formatted version of the number.
     * @return A formatted version of the number.
     */
    public String getFormattedText() {

        String result = "";

        Object value = getValue();
        if (value instanceof Number) {
            result = this.formatter.format(value);
        }
        else {
            result = value.toString();
        }

        return result;

    }

}