/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 06.07.2003 : Initial version
 *  
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.TextElement;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.DateFormatFilter;
import org.jfree.report.filter.templates.DateFieldTemplate;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;

public class DateFieldElementFactory extends TextFieldElementFactory
{
  private DateFormat format;

  public DateFieldElementFactory()
  {
  }

  public DateFormat getFormat()
  {
    return format;
  }

  public void setFormat(DateFormat format)
  {
    this.format = format;
  }

  public String getFormatString()
  {
    if (getFormat() instanceof SimpleDateFormat)
    {
      SimpleDateFormat decFormat = (SimpleDateFormat) getFormat();
      return decFormat.toPattern();
    }
    return null;
  }

  public void setFormatString(String formatString)
  {
    setFormat(new SimpleDateFormat(formatString));
  }

  public Element createElement()
  {
    DataSource dataSource;
    if (getFormatString() != null)
    {
      final DateFieldTemplate template = new DateFieldTemplate();
      template.setFormat(getFormatString());
      if (getNullString() != null)
      {
        template.setNullValue(getNullString());
      }
      template.setField(getFieldname());
      dataSource = template;
    }
    else
    {
      final DateFormatFilter filter = new DateFormatFilter();
      if (format != null)
      {
        filter.setFormatter(format);
      }
      filter.setDataSource(new DataRowDataSource(getFieldname()));
      dataSource = filter;
    }

    TextElement element = new TextElement();
    element.setDataSource(dataSource);
    applyStyle(element.getStyle());

    return element;
  }

  /**
   * Creates a new {@link TextElement} containing a date filter structure.
   *
   * @param name  the name of the new element
   * @param bounds  the bounds of the new element
   * @param paint  the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param font  the font for this element
   * @param nullString  the text used when the value of this element is <code>null</code>
   * @param format  the SimpleDateFormat-formatstring used to format the date
   * @param field  the fieldname to retrieve values from
   *
   * @return a report element for displaying a java.util.Date value.
   *
   * @throws NullPointerException if bounds, format or field are <code>null</code>
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createDateElement(final String name,
                                              final Rectangle2D bounds,
                                              final Color paint,
                                              final ElementAlignment alignment,
                                              final FontDefinition font,
                                              final String nullString,
                                              final String format,
                                              final String field)
  {
    return createDateElement(name,
        bounds,
        paint,
        alignment, null,
        font,
        nullString,
        format,
        field);
  }

  /**
   * Creates a new {@link TextElement} containing a date filter structure.
   *
   * @param name  the name of the new element
   * @param bounds  the bounds of the new element
   * @param paint  the text color of this text element
   * @param alignment  the horizontal text alignment
   * @param valign  the vertical text alignment
   * @param font  the font for this element
   * @param nullString  the text used when the value of this element is <code>null</code>
   * @param format  the SimpleDateFormat-formatstring used to format the date
   * @param field  the fieldname to retrieve values from
   *
   * @return a report element for displaying a java.util.Date value.
   *
   * @throws NullPointerException if bounds, format or field are <code>null</code>
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createDateElement(final String name,
                                              final Rectangle2D bounds,
                                              final Color paint,
                                              final ElementAlignment alignment,
                                              final ElementAlignment valign,
                                              final FontDefinition font,
                                              final String nullString,
                                              final String format,
                                              final String field)
  {
    DateFieldElementFactory factory = new DateFieldElementFactory();
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setName(name);
    factory.setColor(paint);
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

  /**
   * Creates a new {@link TextElement} containing a date filter structure.
   *
   * @param name  the name of the new element
   * @param bounds  the bounds of the new element
   * @param paint  the text color of this text element
   * @param alignment  the horizontal text alignment
   * @param font  the font for this element
   * @param nullString  the text used when the value of this element is <code>null</code>
   * @param format  the SimpleDateFormat used to format the date
   * @param field  the fieldname to retrieve values from
   *
   * @return a report element for displaying a java.util.Date value.
   *
   * @throws NullPointerException if bounds, name, format or field are <code>null</code>
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createDateElement(final String name,
                                              final Rectangle2D bounds,
                                              final Color paint,
                                              final ElementAlignment alignment,
                                              final FontDefinition font,
                                              final String nullString,
                                              final DateFormat format,
                                              final String field)
  {
    return createDateElement(name, bounds, paint, alignment,
        null, font, nullString, format, field);
  }

  /**
   * Creates a new TextElement containing a date filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the horizontal text alignment.
   * @param valign the vertical text alignment
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param format the SimpleDateFormat used to format the date
   * @param field the fieldname to retrieve values from
   *
   * @return a report element for displaying a java.util.Date value.
   *
   * @throws NullPointerException if bounds, name, format or field are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createDateElement(final String name,
                                              final Rectangle2D bounds,
                                              final Color paint,
                                              final ElementAlignment alignment,
                                              final ElementAlignment valign,
                                              final FontDefinition font,
                                              final String nullString,
                                              final DateFormat format,
                                              final String field)
  {
    DateFieldElementFactory factory = new DateFieldElementFactory();
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setName(name);
    factory.setColor(paint);
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

}
