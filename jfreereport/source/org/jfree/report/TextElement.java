/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: TextElement.java,v 1.6 2003/11/07 18:33:47 taqua Exp $
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

package org.jfree.report;

import org.jfree.report.filter.StringFilter;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;

/**
 * The base class for all elements that display text in a report band.
 * <p/>
 * All content is converted to String using the String.valueOf () method. To convert
 * values in a more sophisicated way, add filters to this element. Known filters are for
 * instance the <code>NumberFormatFilter</code> or the <code>SimpleDateFormatFilter</code>.
 * <p/>
 * For more information on filters have a look at the filter package {@link
 * org.jfree.report.filter}
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class TextElement extends Element
{
  /**
   * The content type string.
   */
  public static final String CONTENT_TYPE = "text/plain";

  /**
   * The filter used to convert values (Objects) to strings.
   */
  private StringFilter stringfilter;

  /**
   * Creates a new empty text element.
   */
  public TextElement ()
  {
    stringfilter = new StringFilter();
    setNullString(null);
  }

  /**
   * Return the null-value representation for this element. This will never return null,
   * although you may feed a null value into the set method of this property.
   *
   * @return the null value representation for this element.
   *
   * @see #setNullString(String)
   */
  public String getNullString ()
  {
    return stringfilter.getNullValue();
  }

  /**
   * Defines the null value representation for this element. If null is given, the value
   * is set to a reasonable value (this implementation sets the value to the string "-").
   *
   * @param s the null string.
   */
  public void setNullString (final String s)
  {
    final String nstring = (s == null) ? "-" : s;
    stringfilter.setNullValue(nstring);
  }

  /**
   * Returns the value for this text element.
   * <p/>
   * Internally, a <code>StringFilter</code> is used to ensure that the final result is an
   * instance of String (even though it is returned as an Object.
   *
   * @return the value for the element.
   */
  public final Object getValue ()
  {
    stringfilter.setDataSource(getDataSource());
    return stringfilter.getValue();
  }

  /**
   * Returns a string representation of this element, useful for debugging purposes.
   *
   * @return a string.
   */
  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
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
  public Object clone ()
          throws CloneNotSupportedException
  {
    final TextElement te = (TextElement) super.clone();
    te.stringfilter = (StringFilter) stringfilter.clone();
    return te;
  }

  /**
   * Returns the content type, in this case '<code>text/plain</code>'.
   *
   * @return the content type.
   */
  public String getContentType ()
  {
    return CONTENT_TYPE;
  }

  /**
   * Returns the name of the current font.
   *
   * @return the font name
   */
  public String getFontName ()
  {
    return (String) getStyle().getStyleProperty(ElementStyleSheet.FONT);
  }

  /**
   * Defines the font name of the current font.
   *
   * @param fontName the font name
   */
  public void setFontName (final String fontName)
  {
    getStyle().setStyleProperty(ElementStyleSheet.FONT, fontName);
  }

  /**
   * Returns the font size in points.
   *
   * @return the font size.
   */
  public int getFontSize ()
  {
    final Integer i = (Integer) getStyle().getStyleProperty
            (ElementStyleSheet.FONTSIZE);
    // fontsize is never null.
    return i.intValue();
  }

  /**
   * Defines the height of the font in points.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param fontSize the font size in points.
   */
  public void setFontSize (final int fontSize)
  {
    getStyle().setStyleProperty
            (ElementStyleSheet.FONTSIZE, new Integer(fontSize));
  }

  /**
   * Checks, whether the font should be displayed in bold style.
   *
   * @return true, if the font should be bold, false otherwise.
   */
  public boolean isBold ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.BOLD);
  }

  /**
   * Defines, whether the font should be displayed in bold, false otherwise.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param bold true, if the font should be displayed in bold, false otherwise
   */
  public void setBold (final boolean bold)
  {
    getStyle().setBooleanStyleProperty(ElementStyleSheet.BOLD, bold);
  }

  /**
   * Checks, whether the font should be displayed in italic style.
   *
   * @return true, if the font should be italic, false otherwise.
   */
  public boolean isItalic ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.ITALIC);
  }

  /**
   * Defines, whether the font should be displayed in italics.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param italic true, if the font should be in italic style, false otherwise.
   */
  public void setItalic (final boolean italic)
  {
    getStyle().setBooleanStyleProperty(ElementStyleSheet.ITALIC, italic);
  }

  /**
   * Returns whether the text should be displayed underlined.
   *
   * @return true, if the fond should be underlined, false otherwise.
   */
  public boolean isUnderline ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.UNDERLINED);
  }

  /**
   * Defines, whether the text should be displayed with the underline style applied.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param underline true, if the text should be displayed underlined, false otherwise.
   */
  public void setUnderline (final boolean underline)
  {
    getStyle().setBooleanStyleProperty(ElementStyleSheet.UNDERLINED, underline);
  }

  /**
   * Returns whether the text should have the strikethough style applied.
   *
   * @return true, if the font should be striked through, false otherwise.
   */
  public boolean isStrikethrough ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.STRIKETHROUGH);
  }

  /**
   * Defines, whether the text should be displayed striked through.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param strikethrough whether to display the text with strikethrough style applied
   */
  public void setStrikethrough (final boolean strikethrough)
  {
    getStyle().setBooleanStyleProperty(ElementStyleSheet.STRIKETHROUGH, strikethrough);
  }

  /**
   * Returns the font definition object assigned with this element. Never null.
   *
   * @return the font definition for this element.
   */
  public FontDefinition getFont ()
  {
    return getStyle().getFontDefinitionProperty();
  }

  /**
   * Defines all font properties by applying the values from the given font definition
   * object.
   * <p/>
   * Calling this function with either parameter will override any previously defined
   * value for the layoutcachable attribute. The value can no longer be inherited from
   * parent stylesheets.
   *
   * @param font the font definition for this element.
   */
  public void setFont (final FontDefinition font)
  {
    getStyle().setFontDefinitionProperty(font);

  }

  /**
   * Returns the lineheight for the element. The lineheight can be used to extend the
   * space between two text lines, the effective lineheight will be the maximum of this
   * property and the font height.
   *
   * @return the defined line height.
   */
  public float getLineHeight ()
  {
    final Float i = (Float) getStyle().getStyleProperty
            (ElementStyleSheet.LINEHEIGHT);
    if (i == null)
    {
      return 0;
    }
    return i.floatValue();
  }

  /**
   * Defines the lineheight for the element. The lineheight can be used to extend the
   * space between two text lines, the effective lineheight will be the maximum of this
   * property and the font height.
   * <p/>
   * Calling this function with any parameter will override any previously defined value
   * for the layoutcachable attribute. The value can no longer be inherited from parent
   * stylesheets.
   *
   * @param lineHeight the defined line height.
   */
  public void setLineHeight (final float lineHeight)
  {
    getStyle().setStyleProperty
            (ElementStyleSheet.LINEHEIGHT, new Float(lineHeight));
  }

  /**
   * Returns the reserved literal for this text element. This literal is appended,
   * whenever the text from tne content does not fully fit into the element's bounds.
   *
   * @return the reserved literal.
   */
  public String getReservedLiteral ()
  {
    return (String) getStyle().getStyleProperty
            (ElementStyleSheet.RESERVED_LITERAL);
  }

  /**
   * Defines the reserved literal for this text element. This literal is appended,
   * whenever the text from tne content does not fully fit into the element's bounds.
   *
   * @param reservedLiteral the reserved literal.
   */
  public void setReservedLiteral (final String reservedLiteral)
  {
    getStyle().setStyleProperty
            (ElementStyleSheet.RESERVED_LITERAL, reservedLiteral);
  }
}
