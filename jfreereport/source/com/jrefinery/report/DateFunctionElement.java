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
 * ------------------------
 * DateFunctionElement.java
 * ------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Font;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Presentation element for date functions.
 */
public class DateFunctionElement extends FunctionElement {

    /** The formatting object for this data element. */
    protected DateFormat formatter;

    /**
     * Constructs a date element using integer coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param field The name of the field used to populate this element with data.
     * @param format The format string for the Date.
     */
    public DateFunctionElement(String name,
                               int x, int y, int w, int h,
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
     * Constructs a date element using integer coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param field The name of the field used to populate this element with data.
     * @param format The format string for the Date.
     */
    public DateFunctionElement(String name,
                               int x, int y, int w, int h,
                               Paint paint, Font font, String fontName, int fontStyle, int fontSize,
                               int alignment,
                               String functionName, String format) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment, functionName);
        formatter = new SimpleDateFormat(format);
        this.value = new Date();

    }

    /**
     * Constructs a date element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param field The name of the field used to populate this element with data.
     * @param format The format string for the Date.
     */
    public DateFunctionElement(String name,
                               float x, float y, float w, float h,
                               Font font, int alignment,
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
     * Constructs a date element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param field The name of the field used to populate this element with data.
     * @param format The format string for the Date.
     */
    public DateFunctionElement(String name,
                               float x, float y, float w, float h,
                               Paint paint, Font font, String fontName, int fontStyle, int fontSize,
                               int alignment,
                               String function, String format) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment, function);
	formatter = new SimpleDateFormat(format);
        this.value = new Date();

    }

    /**
     * Returns a string representing the formatted date.
     * @return A formatted version of the data value;
     */
    public String getFormattedText() {

        String result = "";

  	Object value = getValue();
        if (value instanceof Date) {
            result = formatter.format(value);
        }
        else {
            result = value.toString();
        }

        return result;

    }

}