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
 * TextElement.java
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
 * 05-Mar-2002 : Modified constructors (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * The base class for all elements that display text in a report band.
 */
public abstract class TextElement extends Element {

    /** Font for displaying text. */
    protected Font font;

    protected String fontName;

    protected int fontStyle;

    protected int fontSize;

    /** Text alignment: LEFT, CENTER, RIGHT. */
    protected int alignment;

    /**
     * Constructs a text element using integer coordinates.
     *
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param paint The paint.
     * @param font The font.
     * @param alignment The horizontal alignment (LEFT, CENTER or RIGHT).
     */
    protected TextElement(String name,
                          int x, int y, int w, int h,
                          Paint paint, Font font,
                          String fontName, int fontStyle, int fontSize,
                          int alignment) {

        this(name, new Rectangle2D.Float(x, y, w, h), paint, font, fontName, fontStyle, fontSize,
             alignment);

    }

    /**
     * Constructs a text element using float coordinates.
     *
     * @param name The name of the element.
     * @param x The x-coordinate of the element (within its band).
     * @param y The y-coordinate of the element (within its band).
     * @param w The width of the element.
     * @param h The height of the element.
     * @param paint The paint.
     * @param font The font used to display the element.
     * @param alignment The text alignment (LEFT, CENTER or RIGHT).
     */
    protected TextElement(String name,
                          float x, float y, float w, float h,
                          Paint paint, Font font,
                          String fontName, int fontStyle, int fontSize,
                          int alignment) {

        this(name, new Rectangle2D.Float(x, y, w, h), paint, font, fontName, fontStyle, fontSize,
             alignment);

    }

    /**
     * Constructs an element using a Rectangle2D.
     *
     * @param name The name of the element.
     * @param area The location and bounds of the element.
     * @param paint The paint.
     */
    protected TextElement(String name, Rectangle2D area, Paint paint, Font font,
                          String fontName, int fontStyle, int fontSize,
                          int alignment) {

        super(name, area, paint);
        this.font = font;
        this.fontName = fontName;
        this.fontStyle = fontStyle;
        this.fontSize = fontSize;
        this.alignment = alignment;

    }

    /**
     * Returns the font for this element.  If a font has been explicitly set for the element,
     * then it is used.  Otherwise, if a font name, style or size has been specified, this is
     * used to derive a new font from the band's default font.  If nothing at all has been
     * specified, the band's default font is used.
     */
    public Font deriveFont(Band band) {

        Font result = this.font;

        if (this.font==null) {

            Font f = band.getDefaultFont();

            if (this.fontName!=null) {

                if (this.fontSize>0) {  // name and size
                    result = new Font(this.fontName, f.getStyle(), this.fontSize);
                }
                else {
                    if (this.fontStyle!=-1) {  // name and style
                        result = new Font(this.fontName, this.fontStyle, f.getSize());
                    }
                    else { // name only
                        result = new Font(this.fontName, f.getStyle(), f.getSize());
                    }
                }

            }

            else {

                if (this.fontSize>0) {
                    if (this.fontStyle!=-1) { // size and style
                        result = new Font(f.getName(), this.fontStyle, this.fontSize);
                    }
                    else { // size only
                        result = new Font(f.getName(), f.getStyle(), this.fontSize);
                    }
                }
                else {
                    if (this.fontStyle!=-1) {
                        result = new Font(f.getName(), this.fontStyle, f.getSize());
                    }
                    else {
                        result = band.getDefaultFont();
                    }
                }
            }

        }

        return result;

    }

    /**
     * Draws the element at its position relative to the supplied band co-ordinates.
     *
     * @param target The output target.
     * @param band The band.
     * @param bandX The x-coordinate of the report band.
     * @param bandY The y-coordinate of the report band.
     */
    public void draw(OutputTarget target, Band band, float bandX, float bandY) {

        // set the paint...
        if (this.paint!=null) {
            target.setPaint(this.paint);
        }
        else {
            target.setPaint(band.getDefaultPaint());
        }

        // set the font...
        if (this.font!=null) {
            target.setFont(this.font);
        }
        else {
            target.setFont(this.deriveFont(band));
        }

        // draw the text...
        float x1 = bandX+(float)area.getX();
        float y1 = bandY+(float)area.getY();
        float x2 = bandX+(float)area.getMaxX();
        float y2 = bandY+(float)area.getMaxY();

        if (x2>x1) {
            target.drawString(this.getFormattedText(), x1, y1, x2, y2, this.alignment);
        }
        else {
            target.drawString(this.getFormattedText(), bandX, y1, bandX+target.getUsableWidth(), y2, this.alignment);
        }

    }

    /**
     * Returns a formatted version of the data element.  Typically used for numbers and dates which
     * can be formatted in various ways.
     *
     * @return A formatted version of the data value.
     */
    protected abstract String getFormattedText();

}