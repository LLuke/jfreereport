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
 * -------------------------
 * MultilineTextElement.java
 * -------------------------
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
 * 24-Apr-2002 : Changed to use the OutputTarget.
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.text.BreakIterator;
import javax.swing.*;
import java.util.Vector;

public class MultilineTextElement extends StringElement {

    /**
     * Standard constructor - builds a string element using integer coordinates.
     * @param name The name of the element;
     * @param x The x-coordinate of the element (within its band);
     * @param y The y-coordinate of the element (within its band);
     * @param w The width of the element;
     * @param h The height of the element;
     * @param font The font used to display the element;
     * @param alignment The text alignment (LEFT, CENTER or RIGHT);
     * @param field The name of the field used to populate this element with data;
     */
    public MultilineTextElement(String name, int x, int y, int w, int h,
                                Paint paint, Font font, String fontName, int fontStyle, int fontSize, int alignment, String field) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment, field);

    }

    /**
     * Standard constructor - builds a string element using float coordinates.
     * @param name The name of the element;
     * @param x The x-coordinate of the element (within its band);
     * @param y The y-coordinate of the element (within its band);
     * @param w The width of the element;
     * @param h The height of the element;
     * @param font The font used to display the element;
     * @param alignment The text alignment (LEFT, CENTER or RIGHT);
     * @param field The name of the field used to populate this element with data;
     */
    public MultilineTextElement(String name, float x, float y, float w, float h,
                                Paint paint, Font font, String fontName, int fontStyle, int fontSize, int alignment, String field) {

        super(name, x, y, w, h, paint, font, fontName, fontStyle, fontSize, alignment, field);
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
            target.drawMultiLineText(this.getFormattedText(), x1, y1, x2, y2, this.alignment);
        }
        else {
            target.drawMultiLineText(this.getFormattedText(), bandX, y1, bandX+target.getUsableWidth(), y2, this.alignment);
        }

    }

    /**
     * Draws the band onto the specified graphics device.
     * @param g2 The graphics device.
     * @param x The x-coordinate for the band.
     * @param y The y-coordinate for the band.
     *
    public void draw(Graphics2D g2, float x, float y) {

  	  g2.setFont (myFont);
	    FontRenderContext frc = g2.getFontRenderContext();
	    FontMetrics fm = g2.getFontMetrics();

     	String mytext = getFormattedText ();
      breakit.setText (mytext);

      Vector lines = breakLines (mytext, frc);
      int fontheight = fm.getHeight ();

	    for (int i = 0; i < lines.size(); i++) {
    	    String line = (String)lines.elementAt(i);
	        drawText(g2, x, y + i*fontheight, line);
      }

    }
*/
    /**
     * Draws the element at its position relative to the supplied band co-ordinates.
     * @param g2 The graphics device.
     * @param bandX The x-coordinate of the report band.
     * @param bandY The y-coordinate of the report band.
     *
    public void drawText(Graphics2D g2, float bandX, float bandY, String text) {

        g2.setFont(font);
        float textX = (float)area.getX();
        // x-coordinate must be adjusted for CENTER or RIGHT alignment
        if (alignment==Element.CENTER) {
            FontRenderContext frc = g2.getFontRenderContext();
            Rectangle2D textBounds = font.getStringBounds(text, frc);
            textX = (float)area.getX()+(float)(area.getWidth()/2)-(float)(textBounds.getWidth()/2);
        }
        if (alignment==Element.RIGHT) {
            FontRenderContext frc = g2.getFontRenderContext();
            Rectangle2D textBounds = font.getStringBounds(text, frc);
            textX = (float)(area.getMaxX())-(float)(textBounds.getWidth());
        }
        g2.drawString(text, bandX+textX, bandY + (int)area.getMaxY());

    }

    public Vector breakLines (String mytext, FontRenderContext frc) {

  	Vector lines = new Vector();
	int pos = 0;
	int len = mytext.length();

        while (pos < len) {
            int last = breakit.next ();
            float x = 0;

            while (x < this.w && last != BreakIterator.DONE) {

                Rectangle2D textBounds = font.getStringBounds(mytext, pos, last, frc);
                x = (float) textBounds.getWidth();
	        last = breakit.next ();
            }

            if (last == BreakIterator.DONE) {
                lines.add (mytext.substring (pos));
	        pos = len;
            }
	    else {
                lines.add (mytext.substring (pos, last));
                pos = last;
            }
        }

        return lines;
    }
*/
}
