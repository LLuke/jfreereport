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
 * --------------------
 * FunctionElement.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner, with modifications by DG (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Font;
import com.jrefinery.report.function.Function;

/**
 * The base class for all elements in JFreeReport that display function values.
 * This class separates the functional part (@see ReportFunction) from the presentation
 * layer.  The report-function given at construction time is used as key for the
 * function collection.
 */
public abstract class FunctionElement extends TextElement {

    /** The name of the function that this element gets its value from. */
    protected String functionName;

    /** The current value of the function. */
    protected Object value;

    /**
     * Constructs a function element using integer coordinates.
     *
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param baseline The baseline for the text.
     */
    protected FunctionElement (String name,
                               int x, int y, int w, int h,
                               Paint paint, Font font, String fontName, int fontStyle, int fontSize,
                               int alignment, String functionName) {

        super (name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment);
        this.functionName = functionName;

    }

    /**
     * Constructs a function element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param baseline The baseline for the text.
     */
    protected FunctionElement (String name,
                               float x, float y, float w, float h,
                               Paint paint, Font font, String fontName, int fontStyle, int fontSize,
                               int alignment, String functionName) {

 	super (name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment);
	this.functionName = functionName;

    }

    /**
     * Returns the name of the function that this element obtains its value from.
     * @return The function name.
     */
    public String getFunctionName() {
        return this.functionName;
    }

    /**
     * Returns the value for the element.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Sets the current value of the element.
     * @param value The new value for the element;
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Returns a string representing the formatted data.
     * @return A formatted version of the data value.
     */
    public String getFormattedText() {
        return value.toString();
    }

}
