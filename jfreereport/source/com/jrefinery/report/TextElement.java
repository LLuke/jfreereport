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
 * $Id: TextElement.java,v 1.6 2002/05/21 23:06:18 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Modified constructors (DG);
 * 10-May-2002 : removed all but the default constructor. Added accessor functions for all properties.
 * 15-May-2002 : The null value is handled specially, initiated by thomas.rynne@edftrading.com
 * 16-May-2002 : Line delimiters adjusted
 *               using protected member m_paint instead of getter methode
 * 20-May-2002 : TextElement is now line break capable. (Functionality moved from MultilineTextElement)
 *               This class is no longer abstract. The filter/datasource interfaces are used to feed
 *               and convert data.
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.filter.DataFilter;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import java.awt.Font;
import java.awt.Paint;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * The base class for all elements that display text in a report band.
 */
public class TextElement extends Element
{
  private StringFilter stringfilter;

  /** Font for displaying text. */
  private Font font;

  /** Text alignment: LEFT, CENTER, RIGHT. */
  private int alignment;

  /**
   * Constructs an element using a Rectangle2D.
   */
  protected TextElement()
  {
    stringfilter = new StringFilter ();
    super.setDataSource(stringfilter);
    setAlignment(ElementConstants.LEFT);
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
    return stringfilter.getNullValue();
  }

  /**
   * Defines the null value representation for this element.
   */
  public void setNullString(String s)
  {
    String nstring = (s == null) ? "null" : s;
    stringfilter.setNullValue(nstring);
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
   */
  public void draw (OutputTarget target, Band band) throws OutputTargetException
  {
    if (m_paint != null)
    {
      target.setPaint(m_paint);
    }
    else
    {
      target.setPaint (band.getDefaultPaint ());
    }
    // set the font...
    if (getFont (band) != null)
    {
      target.setFont (getFont (band));
    }
    // draw the text...
    target.drawMultiLineText (this.getFormattedText (), getAlignment ());
  }

  /**
   * Returns a formatted version of the data element.  Typically used for numbers and dates which
   * can be formatted in various ways.
   *
   * @return A formatted version of the data value.
   * @deprecated this method is replaced by Element.getValue().
   */
  public String getFormattedText()
  {
    return String.valueOf(super.getValue());
  }

  /**
   * Makes sure that getFormattedText is called and evaluated.
   */
  public final Object getValue ()
  {
    return getFormattedText();
  }

  protected DataFilter getTextFilter ()
  {
    return stringfilter;
  }

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
    b.append(getValue());
    b.append(", getFormattedText=");
    b.append(getFormattedText());
    b.append("}");
    return b.toString();
  }

  public void setDataSource (DataSource ds)
  {
    if (ds == null) throw new NullPointerException();
    stringfilter.setDataSource(ds);
  }

}