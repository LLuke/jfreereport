/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * NumberFieldElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: NumberFieldElementFactory.java,v 1.5.4.1 2004/10/06 19:46:14 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.TextElement;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.NumberFormatFilter;
import org.jfree.report.filter.templates.NumberFieldTemplate;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;

/**
 * The number format factory can be used to create numeric text elements.
 * These text elements have special abilities to format numeric values.
 * <p>
 * Once the desired properties are set, the factory can be reused to create
 * similiar text elements.
 *
 * @author Thomas Morgner
 */
public class NumberFieldElementFactory extends TextFieldElementFactory
{
  /** The number format instance used to format numeric values in the text element. */
  private NumberFormat format;

  /**
   * Creates a new number field element factory.
   */
  public NumberFieldElementFactory()
  {
  }

  /**
   * Returns the number format used for all generated text elements. The
   * number format is shared among all generated elements.
   *
   * @return the number format used in this factory.
   */
  public NumberFormat getFormat()
  {
    return format;
  }

  /**
   * Defines the number format used for all generated text elements. The
   * number format is shared among all generated elements.
   *
   * @param format the number format used in this factory.
   */
  public void setFormat(final NumberFormat format)
  {
    this.format = format;
  }

  /**
   * Returns the format string of the used number format. This method will
   * return null, if the current number format is no instance of DecimalFormat.
   *
   * @return the formatstring of the number format instance.
   */
  public String getFormatString()
  {
    if (getFormat() instanceof DecimalFormat)
    {
      final DecimalFormat decFormat = (DecimalFormat) getFormat();
      return decFormat.toPattern();
    }
    return null;
  }

  /**
   * Defines the format string of the used number format. This method will
   * replace the number format instance of this factory.
   *
   * @param formatString the formatstring of the number format instance.
   */
  public void setFormatString(final String formatString)
  {
    setFormat(new DecimalFormat(formatString));
  }

  /**
   * Creates the number text element based on the defined settings.
   * Undefined properties will not be set in the generated element.
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   *
   * @return the generated numberic text element
   */
  public Element createElement()
  {
    final DataSource dataSource;
    if (getFormat() instanceof DecimalFormat)
    {
      final NumberFieldTemplate template = new NumberFieldTemplate();
      template.setDecimalFormat ((DecimalFormat) getFormat());
      if (getNullString() != null)
      {
        template.setNullValue(getNullString());
      }
      template.setField(getFieldname());
      dataSource = template;
    }
    else
    {
      final NumberFormatFilter filter = new NumberFormatFilter();
      if (format != null)
      {
        filter.setFormatter(format);
      }
      filter.setDataSource(new DataRowDataSource(getFieldname()));
      dataSource = filter;
    }

    final TextElement element = new TextElement();
    applyElementName(element);
    element.setDataSource(dataSource);
    applyStyle(element.getStyle());

    return element;
  }


  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field the field in the datamodel to retrieve values from
   * @param format the NumberFormat used in this number element
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createNumberElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color paint,
                                                final ElementAlignment alignment,
                                                final FontDefinition font,
                                                final String nullString,
                                                final NumberFormat format,
                                                final String field)
  {
    return createNumberElement(name, bounds, paint, alignment,
        ElementAlignment.TOP,
        font, nullString,
        format, field);
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param color  the text color of this text element.
   * @param alignment  the horizontal text alignment.
   * @param valign  the vertical alignment.
   * @param font  the font for this element.
   * @param nullString  the text used when the value of this element is null.
   * @param field  the field in the datamodel to retrieve values from.
   * @param format  the NumberFormat used in this number element.
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createNumberElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color color,
                                                final ElementAlignment alignment,
                                                final ElementAlignment valign,
                                                final FontDefinition font,
                                                final String nullString,
                                                final NumberFormat format,
                                                final String field)
  {

    final NumberFieldElementFactory factory = new NumberFieldElementFactory();
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
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field the fieldname in the datamodel to retrieve values from
   * @param format the DecimalFormatString used in this text field
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createNumberElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color paint,
                                                final ElementAlignment alignment,
                                                final FontDefinition font,
                                                final String nullString,
                                                final String format,
                                                final String field)
  {
    return createNumberElement(name, bounds, paint, alignment,
        null,
        font, nullString,
        format, field);
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param color the text color of the element.
   * @param alignment  the horizontal text alignment.
   * @param valign  the vertical alignment.
   * @param font  the font for this element.
   * @param nullString t he text used when the value of this element is null.
   * @param field  the fieldname in the datamodel to retrieve values from.
   * @param format  the DecimalFormatString used in this text field.
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createNumberElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color color,
                                                final ElementAlignment alignment,
                                                final ElementAlignment valign,
                                                final FontDefinition font,
                                                final String nullString,
                                                final String format,
                                                final String field)
  {

    final NumberFieldElementFactory factory = new NumberFieldElementFactory();
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
