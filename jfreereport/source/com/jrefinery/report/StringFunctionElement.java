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
 * StringFunctionElement.java
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
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner, with modifications by DG (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Font;

/**
 * A function element that displays String values.
 * <P>
 * This class adds nothing to the FunctionElement class apart from a descriptive name.
 */
public class StringFunctionElement extends FunctionElement {

    /**
     * Constructs a string element using integer coordinates.
     *
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param function The name of the function used to populate this element with data.
     */
    public StringFunctionElement(String name,
                                 int x, int y, int w, int h,
                                 String function) {

        this(name,
             x, y, w, h,
             DEFAULT_PAINT,
             DEFAULT_FONT,
             DEFAULT_FONT_NAME,
             DEFAULT_FONT_STYLE,
             DEFAULT_FONT_SIZE,
             DEFAULT_ALIGNMENT,
             function);

    }

    /**
     * Constructs a string element using integer coordinates.
     *
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param function The name of the function used to populate this element with data.
     */
    public StringFunctionElement(String name,
                                 int x, int y, int w, int h,
                                 Paint paint,
                                 Font font, String fontName, int fontStyle, int fontSize,
                                 int alignment, String function) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment, function);

    }

    /**
     * Constructs a string element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param function The name of the function used to populate this element with data.
     */
    public StringFunctionElement(String name,
                                 float x, float y, float w, float h,
                                 String function) {

        super(name,
              x, y, w, h,
              DEFAULT_PAINT,
              DEFAULT_FONT,
              DEFAULT_FONT_NAME,
              DEFAULT_FONT_STYLE,
              DEFAULT_FONT_SIZE,
              DEFAULT_ALIGNMENT,
              function);

    }

    /**
     * Constructs a string element using float coordinates.
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     * @param function The name of the function used to populate this element with data.
     */
    public StringFunctionElement(String name,
                                 float x, float y, float w, float h,
                                 Paint paint,
                                 Font font, String fontName, int fontStyle, int fontSize,
                                 int alignment, String function) {

        super(name,
              x, y, w, h,
              paint,
              font, fontName, fontStyle, fontSize,
              alignment,
              function);

    }

}