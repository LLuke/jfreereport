/**
 * =============================================================
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
 * $Id: TextElement.java,v 1.4 2002/05/16 13:20:09 jaosch Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Modified constructors (DG);
 * 10-May-2002 : removed all but the default constructor. Added accessor functions for all properties.
 * 15-May-2002 : The null value is handled specially, initiated by thomas.rynne@edftrading.com
 * 16-May-2002 : Line delimiters adjusted
 *               using protected member m_paint instead of getter methode
 */

package com.jrefinery.report;

import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

/**
 * The base class for all elements that display text in a report band.
 */
public abstract class TextElement extends Element
{
  /** A string representing the text which is displayed when the elements value is null */
  private String nullString;

  /** Font for displaying text. */
  private Font font;

  /** Text alignment: LEFT, CENTER, RIGHT. */
  private int alignment;

  /**
   * Constructs an element using a Rectangle2D.
   */
  protected TextElement()
  {
    setNullString("-");
  }

  /**
   * Defines the font for this element. If no font is defined, on drawing time the bands font
   * is used.
   */
  public void setFont(Font f)
  {
    this.font = f;
  }

  /**
   * Returns the font defined for this element.
   *
   * @return the font or null if no font has been defined.
   */
  public Font getFont()
  {
    return getFont(null);
  }

  /**
   * Returns the font for this element.  If a font has been explicitly set for the element,
   * then it is used.  Otherwise, if a font name, style or size has been specified, this is
   * used to derive a new font from the band's default font.  If nothing at all has been
   * specified, the band's default font is used.
   */
  public Font getFont(Band band)
  {

    if (band == null)
      return font;

    Font result = this.font;

    if (this.font == null)
    {

      result = band.getDefaultFont();
    }
    return result;

  }

  /**
   * @return the null value representation for this element
   */
  public String getNullString()
  {
    return nullString;
  }

  /**
   * Defines the null value representation for this element.
   */
  public void setNullString(String s)
  {
    nullString = (s == null) ? "null" : s;
  }

  /**
   * Returns the text alignment for this element's text. This is one of <code>ElementConstants.LEFT</code>,
   * <code>ElementConstants.CENTER</code> or <code>ElementConstants.RIGHT</code>.
   *
   * @return the alignment for this element
   */
  public int getAlignment()
  {
    return alignment;
  }

  /**
   * Defines the text alignment for this element's text. This is one of <code>ElementConstants.LEFT</code>,
   * <code>ElementConstants.CENTER</code> or <code>ElementConstants.RIGHT</code>.
   *
   * @param alignent the alignment for this element.
   */
  public void setAlignment(int alignment)
  {
    if (alignment == LEFT || alignment == RIGHT || alignment == CENTER)
      this.alignment = alignment;
    else
      throw new IllegalArgumentException("The element alignment must be one of LEFT, RIGHT or CENTER");
  }

  /**
   * Draws the element at its position relative to the supplied band co-ordinates.
   *
   * @param target The output target.
   * @param band The band.
   * @param bandX The x-coordinate of the report band.
   * @param bandY The y-coordinate of the report band.
   */
  public void draw(OutputTarget target, Band band, float bandX, float bandY)
  {
    // set the paint...
    if (m_paint != null)
    {
      target.setPaint(m_paint);
    }
    else
    {
      target.setPaint(band.getDefaultPaint());
    }

    // set the font...

    // what if no font is set in both band and element?

    Font currentFont = getFont(band);
    if (currentFont != null)
    {
      target.setFont(currentFont);
    }
    else
    {
      throw new NullPointerException("Neither band nor Item have a font set");
    }

    // draw the text...

    Rectangle2D area = getBounds();
    float x1 = bandX + (float) area.getX();
    float y1 = bandY + (float) area.getY();
    float x2 = bandX + (float) area.getMaxX();
    float y2 = bandY + (float) area.getMaxY();

    if (x2 > x1)
    {
      target.drawString(this.getFormattedText(), x1, y1, x2, y2, getAlignment());
    }
    else
    {
      target.drawString(
        this.getFormattedText(),
        bandX,
        y1,
        bandX + target.getUsableWidth(),
        y2,
        getAlignment());
    }

  }

  /**
   * Returns a formatted version of the data element.  Typically used for numbers and dates which
   * can be formatted in various ways.
   *
   * @return A formatted version of the data value.
   */
  public abstract String getFormattedText();

  /**
   * Debugging: returns the String representation of this element.
   */
  public String toString()
  {
    StringBuffer b = new StringBuffer();
    b.append(this.getClass().getName());
    b.append("={ name=");
    b.append(getName());
    b.append(", bounds=");
    b.append(getBounds());
    b.append(", font=");
    b.append(getFont());
    b.append(", text=");
    b.append(getFormattedText());
    b.append("}");
    return b.toString();
  }
}