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
 * ----------------
 * DataElement.java
 * ----------------
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
 * 05-Mar-2002 : Changed constructors from public --> protected (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Font;

/**
 * The base class for all report elements that display data (that is, information from the report's
 * data source) rather than just static information.
 */
public class DataElement extends TextElement {

    /** The name of the field in the data source that this element obtains its data from. */
    protected String field;

    /** The current value from the data source. */
    protected Object value;

    /**
     * Constructs a data element using integer coordinates.
     *
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param field The name of the field used to populate this element with data.
     */
    protected DataElement(String name,
                          int x, int y, int w, int h,
                          Paint paint, Font font, String fontName, int fontStyle, int fontSize,
                          int alignment,
                          String field) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment);
        this.field = field;

    }

    /**
     * Constructs a data element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param baseline The baseline for the text.
     * @param field The name of the field used to populate this element with data.
     */
    protected DataElement(String name,
                          float x, float y, float w, float h,
                          Paint paint, Font font, String fontName, int fontStyle, int fontSize,
                          int alignment, String field) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment);
        this.field = field;

    }

    /**
     * Returns the name of the field in the data source that this element obtains its data from.
     * @return The field name.
     */
    public String getField() {
        return this.field;
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