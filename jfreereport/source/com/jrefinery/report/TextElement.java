/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: TextElement.java,v 1.28 2003/02/05 15:38:13 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Modified constructors (DG);
 * 10-May-2002 : Removed all but the default constructor. Added accessor functions for all
 *               properties.
 * 15-May-2002 : The null value is handled specially, initiated by thomas.rynne@edftrading.com
 * 16-May-2002 : Line delimiters adjusted
 *               using protected member m_paint instead of getter methode
 * 20-May-2002 : TextElement is now line break capable. (Functionality moved from
 *               MultilineTextElement)
 *               This class is no longer abstract. The filter/datasource interfaces are used to
 *               feed and convert data.
 * 24-May-2002 : BugFix: Alignment was not initialized and made pdf-printing imposible.
 * 04-Jun-2002 : Documentation.
 * 19-Jun-2002 : More documentation
 * 02-Jul-2002 : TextElements constructor has to be public, of course.
 * 05-Sep-2002 : Cloning added
 * 06-Dec-2002 : Updated Javadocs (DG);
 * 10-Dec-2002 : Removed getFont(StyleSheet s) - this is handled by the stylesheet
 * 04-Feb-2002 : Replaced deprecated FontStyle with the new FontDefinition style key
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.FontDefinition;

import java.awt.Font;

/**
 * The base class for all elements that display text in a report band.
 * <p>
 * All content is converted to String using the String.valueOf () method.
 * To convert values in a more sophisicated way, add filters to this element.
 * Known filters are for instance the <code>NumberFormatFilter</code> or
 * the <code>SimpleDateFormatFilter</code>.
 * <p>
 * For more information on filters have a look at the filter package
 * {@link com.jrefinery.report.filter}
 * <p>
 * The font style flags isUnderlined and isStriketrough are not yet implemented
 * in version 0.8.0.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class TextElement extends Element
{
  /** The filter used to convert values (Objects) to strings. */
  private StringFilter stringfilter;

  /**
   * Creates a new text element.
   */
  public TextElement()
  {
    stringfilter = new StringFilter();
    setNullString(null);
  }

  /**
   * Returns true, if the text should be underlined, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isUnderlined()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.UNDERLINED);
  }

  /**
   * Defines whether the text should be printed in underline style.
   *
   * @param b  the flag.
   */
  public void setUnderlined(boolean b)
  {
    getStyle().setStyleProperty(ElementStyleSheet.UNDERLINED, new Boolean(b));
  }

  /**
   * Returns true, if the text should be strike-through style, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isStrikethrough()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.STRIKETHROUGH);
  }

  /**
   * Defines whether the text should be printed in strike-through style.
   *
   * @param b  the flag.
   */
  public void setStrikethrough(boolean b)
  {
    getStyle().setStyleProperty(ElementStyleSheet.STRIKETHROUGH, new Boolean(b));
  }

  /**
   * Defines the font for this element. If no font is defined, on drawing time the band's font
   * is used.
   *
   * @param f  the font.
   */
  public void setFont(Font f)
  {
    getStyle().setFontDefinitionProperty(new FontDefinition (f));
  }

  /**
   * Returns the font defined for this element.
   *
   * @return the font or null if no font has been defined for this element.
   * @deprecated use the stylesheet to access the font if needed.
   */
  public Font getFont()
  {
    return getStyle().getFontDefinitionProperty().getFont();
  }

  /**
   * Return the null-value representation for this element. This will never return null,
   * although you may feed a null value into the set method of this property.
   *
   * @return the null value representation for this element.
   *
   * @see #setNullString(String)
   */
  public String getNullString()
  {
    return stringfilter.getNullValue();
  }

  /**
   * Defines the null value representation for this element. If null is given, the value
   * is set to a reasonable value (this implementation sets the value to the string "-").
   *
   * @param s  the null string.
   */
  public void setNullString(String s)
  {
    String nstring = (s == null) ? "-" : s;
    stringfilter.setNullValue(nstring);
  }

  /**
   * Returns the text alignment for this element's text.
   * <p>
   * This is one of <code>ElementConstants.LEFT</code>, <code>ElementConstants.CENTER</code> or
   * <code>ElementConstants.RIGHT</code>.
   *
   * @return the alignment for this element.
   */
  public int getAlignment()
  {
    ElementAlignment al = (ElementAlignment)
        getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT, ElementAlignment.LEFT);
    return al.getOldAlignment();
  }

  /**
   * Defines the text alignment for this element's text.
   * <p>
   * This is one of <code>Element.LEFT</code>, <code>Element.CENTER</code> or
   * <code>Element.RIGHT</code>.
   *
   * @param alignment  the alignment for this element.
   */
  public void setAlignment(int alignment)
  {
    if (alignment == LEFT)
    {
      getStyle().setStyleProperty(ElementStyleSheet.ALIGNMENT, ElementAlignment.LEFT);
    }
    else if (alignment == RIGHT)
    {
      getStyle().setStyleProperty(ElementStyleSheet.ALIGNMENT, ElementAlignment.RIGHT);
    }
    else if (alignment == CENTER)
    {
      getStyle().setStyleProperty(ElementStyleSheet.ALIGNMENT, ElementAlignment.CENTER);
    }
    else
    {
      throw new IllegalArgumentException("The alignment must be one of LEFT, RIGHT or CENTER.");
    }
  }

  /**
   * Returns the vertical alignment for this element's text.
   * <p>
   * This is one of <code>Element.TOP</code>, <code>Element.MIDDLE</code> or
   * <code>Element.BOTTOM</code>.
   *
   * @return the alignment.
   */
  public int getVerticalAlignment()
  {
    ElementAlignment al = (ElementAlignment)
        getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT, ElementAlignment.TOP);
    return al.getOldAlignment();
  }

  /**
   * Defines the vertical alignment for this element's text.
   * <p>
   * This is one of the constants defined in the <code>Element</code> class: <code>TOP</code>,
   * <code>MIDDLE</code> or <code>RIGHT</code>.
   *
   * @param alignment the alignment.
   */
  public void setVerticalAlignment(int alignment)
  {
    if (alignment == TOP)
    {
      getStyle().setStyleProperty(ElementStyleSheet.VALIGNMENT, ElementAlignment.TOP);
    }
    else if (alignment == BOTTOM)
    {
      getStyle().setStyleProperty(ElementStyleSheet.VALIGNMENT, ElementAlignment.BOTTOM);
    }
    else if (alignment == MIDDLE)
    {
      getStyle().setStyleProperty(ElementStyleSheet.VALIGNMENT, ElementAlignment.MIDDLE);
    }
    else
    {
      throw new IllegalArgumentException("The alignment must be one of TOP, BOTTOM or MIDDLE.");
    }
  }

  /**
   * Returns a formatted version of the data element.  Typically used for numbers and dates which
   * can be formatted in various ways.
   *
   * @return  a formatted version of the data value.
   *
   * @deprecated this method is replaced by Element.getValue(), filters are used for any
   *             formatting that is required.
   */
  public String getFormattedText()
  {
    return String.valueOf(getValue());
  }

  /**
   * Returns the value for this text element.  
   * <p>
   * Internally, a <code>StringFilter</code> is used to ensure that the final result is an 
   * instance of String (even though it is returned as an Object.
   *
   * @return the value for the element.
   */
  public final Object getValue()
  {
    stringfilter.setDataSource(getDataSource());
    return stringfilter.getValue();
  }

  /**
   * Returns a string representation of this element, useful for debugging purposes.
   *
   * @return a string.
   */
  public String toString()
  {
    StringBuffer b = new StringBuffer();
    b.append(this.getClass().getName());
    b.append("={ name=");
    b.append(getName());
    b.append(", font=");
    b.append(getStyle().getFontDefinitionProperty());
    b.append("}");
    return b.toString();
  }

  /**
   * Clones this element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    TextElement te = (TextElement) super.clone();
    te.stringfilter = (StringFilter) stringfilter.clone();
    return te;
  }

  /** The content type string. */
  public static final String CONTENT_TYPE = "text/plain";

  /**
   * Returns the content type, in this case '<code>text/plain</code>'.
   *
   * @return the content type.
   */
  public String getContentType()
  {
    return CONTENT_TYPE;
  }
}
