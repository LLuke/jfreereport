/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * MessageFieldElementFactory.java
 * ------------------------------
 * (C)opyright 2003-2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   J&ouml;rg Schaible (for Elsag-Solutions AG);
 *
 * $Id: MessageFieldElementFactory.java,v 1.1 2005/01/28 19:34:06 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-Dec-2004 : Initial version (copied from NumberFieldElementFactory)
 *
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.TextElement;
import org.jfree.report.filter.templates.MessageFieldTemplate;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;

/**
 * The message format factory can be used to create formatted text elements using 
 * the format defined for {@link MessageFormat}. These text elements have special 
 * abilities to format numeric values and dates based on the MessageFormat string.
 * <p>
 * Once the desired properties are set, the factory can be reused to create
 * similiar text elements.
 *
 * @author J&ouml;rg Schaible
 */
public class MessageFieldElementFactory extends TextFieldElementFactory
{
  /** The message format instance used to format the text element. */
  private MessageFormat format;

  /**
   * Creates a new message field element factory.
   */
  public MessageFieldElementFactory()
  {
  }

  /**
   * Returns the message format used for all generated text elements. The
   * message format is shared among all generated elements.
   *
   * @return the message format used in this factory.
   */
  public MessageFormat getFormat()
  {
    return format;
  }

  /**
   * Defines the message format used for all generated text elements. The format
   * string should contain a format for the element 0. The message format is shared 
   * among all generated elements.
   *
   * @param format the message format used in this factory.
   */
  public void setFormat(final MessageFormat format)
  {
    this.format = format;
  }

  /**
   * Returns the format string of the used message format.
   *
   * @return the formatstring of the number format instance.
   */
  public String getFormatString()
  {
    return format.toPattern();
  }

  /**
   * Defines the format string of the used message format. The format string should contain
   * a format for the element 0. This method will replace the message format instance of this 
   * factory.
   *
   * @param formatString the formatstring of the message format instance.
   */
  public void setFormatString(final String formatString)
  {
    setFormat(new MessageFormat(formatString));
  }

  /**
   * Creates the message text element based on the defined settings.
   * Undefined properties will not be set in the generated element.
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   *
   * @return the generated numberic text element
   */
  public Element createElement()
  {
    final MessageFieldTemplate messageFieldTemplate = new MessageFieldTemplate();
    messageFieldTemplate.setField(getFieldname());
    messageFieldTemplate.setMessageFormat(getFormat());

    final TextElement element = new TextElement();
    applyElementName(element);
    element.setDataSource(messageFieldTemplate);
    applyStyle(element.getStyle());

    return element;
  }


  /**
   * Creates a new TextElement containing a message filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field the field in the datamodel to retrieve values from
   * @param format the MessageFormat used in this number element
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createMessageElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color paint,
                                                final ElementAlignment alignment,
                                                final FontDefinition font,
                                                final String nullString,
                                                final MessageFormat format,
                                                final String field)
  {
    return createMessageElement(name, bounds, paint, alignment,
        ElementAlignment.TOP,
        font, nullString,
        format, field);
  }

  /**
   * Creates a new TextElement containing a message filter structure.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param color  the text color of this text element.
   * @param alignment  the horizontal text alignment.
   * @param valign  the vertical alignment.
   * @param font  the font for this element.
   * @param nullString  the text used when the value of this element is null.
   * @param field  the field in the datamodel to retrieve values from.
   * @param format  the MessageFormat used in this number element.
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createMessageElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color color,
                                                final ElementAlignment alignment,
                                                final ElementAlignment valign,
                                                final FontDefinition font,
                                                final String nullString,
                                                final MessageFormat format,
                                                final String field)
  {

    final MessageFieldElementFactory factory = new MessageFieldElementFactory();
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setName(name);
    factory.setColor(color);
    factory.setHorizontalAlignment(alignment);
    factory.setVerticalAlignment(valign);

    if (font != null)
    {
      factory.setFontName(font.getFontName());
      factory.setFontSize(new Integer(font.getFontSize()));
      factory.setBold(new Boolean(font.isBold()));
      factory.setItalic(new Boolean(font.isItalic()));
      factory.setEncoding(font.getFontEncoding(null));
      factory.setUnderline(new Boolean(font.isUnderline()));
      factory.setStrikethrough(new Boolean(font.isStrikeThrough()));
      factory.setEmbedFont(new Boolean(font.isEmbeddedFont()));
    }
    factory.setNullString(nullString);
    factory.setFormat(format);
    factory.setFieldname(field);
    return (TextElement) factory.createElement();
  }

  /**
   * Creates a new TextElement containing a message filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field the fieldname in the datamodel to retrieve values from
   * @param format the MessageFormatString used in this text field
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createMessageElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color paint,
                                                final ElementAlignment alignment,
                                                final FontDefinition font,
                                                final String nullString,
                                                final String format,
                                                final String field)
  {
    return createMessageElement(name, bounds, paint, alignment,
        null,
        font, nullString,
        format, field);
  }

  /**
   * Creates a new TextElement containing a message filter structure.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param color the text color of the element.
   * @param alignment  the horizontal text alignment.
   * @param valign  the vertical alignment.
   * @param font  the font for this element.
   * @param nullString t he text used when the value of this element is null.
   * @param field  the fieldname in the datamodel to retrieve values from.
   * @param format  the MessageFormatString used in this text field.
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createMessageElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color color,
                                                final ElementAlignment alignment,
                                                final ElementAlignment valign,
                                                final FontDefinition font,
                                                final String nullString,
                                                final String format,
                                                final String field)
  {

    final MessageFieldElementFactory factory = new MessageFieldElementFactory();
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setName(name);
    factory.setColor(color);
    factory.setHorizontalAlignment(alignment);
    factory.setVerticalAlignment(valign);

    if (font != null)
    {
      factory.setFontName(font.getFontName());
      factory.setFontSize(new Integer(font.getFontSize()));
      factory.setBold(new Boolean(font.isBold()));
      factory.setItalic(new Boolean(font.isItalic()));
      factory.setEncoding(font.getFontEncoding(null));
      factory.setUnderline(new Boolean(font.isUnderline()));
      factory.setStrikethrough(new Boolean(font.isStrikeThrough()));
      factory.setEmbedFont(new Boolean(font.isEmbeddedFont()));
    }
    factory.setNullString(nullString);
    factory.setFormatString(format);
    factory.setFieldname(field);
    return (TextElement) factory.createElement();
  }

}