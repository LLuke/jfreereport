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
 * $Id: TextElement.java,v 1.17 2002/08/08 15:28:38 taqua Exp $
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
 * 24-May-2002 : BugFix: Alignment was not initialized and made pdf-printing imposible.
 * 04-Jun-2002 : Documentation.
 * 19-Jun-2002 : More documentation
 * 02-Jul-2002 : TextElements constructor has to be public, of course.
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import java.awt.Font;

/**
 * The base class for all elements that display text in a report band.
 * <p>
 * All Values are converted
 * to Strings using the String.valueOf () method. To convert values in a more sophisicated
 * way add filters to this element. Known filters are for instance the NumberFormatFilter or
 * the SimpleDateFormatFilter.
 * <p>
 * For more information on filters have a look at the filter package com.jrefinery.report.filter.
 * <p>
 * The multiline hints apply to both known OutputTargets (G2OutputTarget & PDFOutputTarget):
 * <p>
 * For multiline elements you have to ensure that your elements height is at least twice the
 * height of your font. The number of text-lines in the element is calculated by using the formula:
 * <code>int maxLinesToDisplay = (int) (elementHeight / fontheight);</code>
 * <p>
 * If maxLinesToDisplay is lesser than two lines, a single line print is assumed, the text will
 * be displayed with full length on a single line. This behaviour is backward compatiblity with
 * the old TextElements behaviour.<br>
 * If maxLinesToDisplay is two or more lines, the text is broken
 * into multiple lines, and if the last line is to long to be printed, the text is shortened and
 * a "..." is appended.
 * <p>
 * The font style flags isUnderlined and isStriketrough are not implemented in version 0.7.3
 */
public class TextElement extends Element
{
  /** This elements stringfilter is used to convert any value into an string */
  private StringFilter stringfilter;

  /** Font for displaying text. */
  private Font font;

  /** Text alignment: LEFT, CENTER, RIGHT. */
  private int alignment;

  /** A flag indicating whether this elements text should be underlined */
  private boolean isUnderlined;

  /** A flag indicating whether this elements text should be striked through.*/
  private boolean isStrikethr;

  private boolean dynamic;

  /**
   * Constructs an element using a Rectangle2D.
   */
  public TextElement()
  {
    stringfilter = new StringFilter();
    setAlignment(ElementConstants.LEFT);
    setNullString(null);
  }

  /**
   * @returns true, if the text should be printed in underline style.
   */
  public boolean isUnderlined()
  {
    return isUnderlined;
  }

  /**
   * Defines whether the text should be printed in underline style.
   */
  public void setUnderlined(boolean b)
  {
    isUnderlined = b;
  }

  /**
   * @returns true, if the text should be printed in strike-though style.
   */
  public boolean isStrikethrough()
  {
    return isStrikethr;
  }

  /**
   * Defines whether the text should be printed in strike-though style.
   */
  public void setStrikethrough(boolean b)
  {
    isStrikethr = b;
  }

  /**
   * Defines the font for this element. If no font is defined, on drawing time the bands font
   * is used.
   *
   * @param f the new elements font or null if the default font should be used.
   */
  public void setFont(Font f)
  {
    this.font = f;
  }

  /**
   * Returns the font defined for this element.
   *
   * @return the font or null if no font has been defined for this element.
   */
  public Font getFont()
  {
    return getFont(null);
  }

  /**
   * Returns the font for this element.
   * <p>
   * If a font has been explicitly set for the element,
   * then it is used. If nothing at all has been
   * specified, the band's default font is used.
   * <p>
   * If no band is specified, this function may return null.
   * If a band is defined, the function will never return null.
   * If neither the band and the element have defined a font, a
   * NullPointerException is thrown instead.
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
    if (result == null)
      throw new NullPointerException("Neither element nor band have defined a font, this is not valid");

    return result;

  }

  /**
   * Return the null-value representation for this element. This will never return null,
   * although you may feed a null value into the set method of this property.
   *
   * @return the null value representation for this element.
   * @see setNullString
   */
  public String getNullString()
  {
    return stringfilter.getNullValue();
  }

  /**
   * Defines the null value representation for this element. If null is given, the value
   * is set to a reasonable value (this implementation sets the value to the string "null".
   */
  public void setNullString(String s)
  {
    String nstring = (s == null) ? "-" : s;
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
  public void draw(OutputTarget target, Band band) throws OutputTargetException
  {
    target.setPaint(getPaint(band));
    // set the font...
    target.setFont(getFont(band));
    // draw the text...
    target.drawMultiLineText(this.getFormattedText(), getAlignment(), isDynamic());
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
    return String.valueOf(getValue());
  }

  /**
   * Makes sure that getFormattedText is called and evaluated.
   */
  public final Object getValue()
  {
    stringfilter.setDataSource(getDataSource());
    return stringfilter.getValue();
  }

  public boolean isDynamic()
  {
    return dynamic;
  }

  public void setDynamic(boolean dynamic)
  {
    this.dynamic = dynamic;
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
}
