/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * ItemFactory.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 16-May-2002 : Initial version
 * 27-May-2002 : Support for the rectangle element
 * 09-Jun-2002 : Documentation
 * 30-Jun-2002 : Added Support for ImageField, ImageFunction
 * 10-Jul-2002 : Added Support for ImageURLField, ImageURLFunction
 * 31-Aug-2002 : Replaced ReportDataSource and FunctionDataSource with DataRowDataSource
 *               Deprecated create*Function and create*Field methods.
 */
package com.jrefinery.report;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DateFormatFilter;
import com.jrefinery.report.filter.DecimalFormatFilter;
import com.jrefinery.report.filter.ImageLoadFilter;
import com.jrefinery.report.filter.ImageRefFilter;
import com.jrefinery.report.filter.NumberFormatFilter;
import com.jrefinery.report.filter.SimpleDateFormatFilter;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.URLFilter;
import com.jrefinery.report.function.ExpressionCollection;

import javax.swing.table.TableModel;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * A factory used to create elements and bands using a single line command.
 *
 * @author TM
 */
public class ItemFactory
{
  /**
   * Creates a new TextElement containing a date filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param format the SimpleDateFormat-formatstring used to format the date
   * @param field the fieldname to retrieve values from
   *
   * @return a report element for displaying a java.util.Date value.
   *
   * @throws NullPointerException if bounds, name, format or field are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createDateElement(String name,
                                              Rectangle2D bounds,
                                              Paint paint,
                                              int alignment,
                                              Font font,
                                              String nullString,
                                              String format,
                                              String field)
  {

    SimpleDateFormatFilter filter = new SimpleDateFormatFilter();
    if (format != null) filter.setFormatString(format);
    filter.setDataSource(new DataRowDataSource(field));

    TextElement dateElement = new TextElement();
    dateElement.setName(name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.setDataSource(filter);

    return dateElement;
  }

  /**
   * Creates a new TextElement containing a date filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
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
  public static TextElement createDateElement(String name,
                                              Rectangle2D bounds,
                                              Paint paint,
                                              int alignment,
                                              Font font,
                                              String nullString,
                                              DateFormat format,
                                              String field)
  {
    DateFormatFilter filter = new DateFormatFilter();
    filter.setFormatter(format);
    filter.setDataSource(new DataRowDataSource(field));

    TextElement dateElement = new TextElement();
    dateElement.setName(name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.setDataSource(filter);
    return dateElement;
  }

  /**
   * Creates a new TextElement containing a date filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param format the SimpleDateFormat-formatstring used to format the date
   * @param function the function name to retrieve values from
   *
   * @throws NullPointerException if bounds, name, format or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   *
   * @return a report element for displaying a java.util.Date function value.
   *
   * @deprecated use createDateElement instead, as all DataAccess has been unified
   */
  public static TextElement createDateFunction(String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String format,
                                               String function)
  {
    return createDateElement(name, bounds, paint, alignment, font, nullString, format, function);
  }

  /**
   * Creates a new TextElement containing a date filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param format the SimpleDateFormat-formatstring used to format the date
   * @param function the function name to retrieve values from
   *
   * @throws NullPointerException if bounds, name, format or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   *
   * @return a report element for displaying a java.util.Date function value.
   *
   * @deprecated use createDateElement instead, as all DataAccess has been unified
   */
  public static TextElement createDateFunction(String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               DateFormat format,
                                               String function)
  {
    return createDateElement(name, bounds, paint, alignment, font, nullString, format, function);
  }

  /**
   * Creates a new TextElement containing a general filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param function the function to retrieve values from
   *
   * @return a report element for displaying a general object.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createGeneralElement(String name,
                                                 Rectangle2D bounds,
                                                 Paint paint,
                                                 int alignment,
                                                 Font font,
                                                 String nullString,
                                                 String function)
  {
    TextElement gElement = new TextElement();
    gElement.setName(name);
    gElement.setBounds(bounds);
    gElement.setPaint(paint);
    gElement.setAlignment(alignment);
    gElement.setFont(font);
    gElement.setNullString(nullString);
    gElement.setDataSource(new DataRowDataSource(function));
    return gElement;
  }

  /**
   * Creates a new ImageElement. The source URL is predefined in an StaticDataSource and will
   * not change during the report processing.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the color of this element (currently not used)
   * @param source the source url from where to load the image
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   * @throws IOException if there is a problem with the source URL.
   */
  public static ImageElement createImageElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                URL source)
      throws IOException
  {
    ImageReference reference = new ImageReference(source);
    StaticDataSource sds = new StaticDataSource(reference);
    ImageElement element = new ImageElement();
    element.setName(name);
    element.setPaint(paint);
    element.setBounds(bounds);
    element.setDataSource(sds);
    return element;
  }

  /**
   * Creates a new ImageElement, which is fed from an URL stored in the datasource.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the color of this element (currently not used)
   * @param field  the name of the column/function/expression that returns the URL for the image.
   *
   * @return a report element for displaying an image from a URL.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   * @throws IOException if there is a problem with the image URL.
   *
   * @deprecated use createImageURLElement instead
   */
  public static ImageElement createImageURLField(String name,
                                                 Rectangle2D bounds,
                                                 Paint paint,
                                                 String field)
      throws IOException
  {
    return createImageURLElement(name, bounds, paint, field);
  }

  /**
   * Creates a new ImageElement, which is fed from an URL stored in the datasource.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the color of this element (currently not used)
   * @param field  the name of the column/function/expression that returns the URL for the image.
   *
   * @return a report element for displaying an image based on a URL.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   * @throws IOException if there is a problem with the image URL.
   */
  public static ImageElement createImageURLElement(String name,
                                                   Rectangle2D bounds,
                                                   Paint paint,
                                                   String field)
      throws IOException
  {
    URLFilter urlfilter = new URLFilter();
    urlfilter.setDataSource(new DataRowDataSource(field));

    ImageLoadFilter imagefilter = new ImageLoadFilter();
    imagefilter.setDataSource(urlfilter);

    ImageElement element = new ImageElement();
    element.setName(name);
    element.setPaint(paint);
    element.setBounds(bounds);
    element.setDataSource(imagefilter);
    return element;
  }

  /**
   * Creates a new ImageElement, which is fed from an URL stored in the datasource.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the color of this element (currently not used)
   * @param function the name of the function that returns the image URL.
   *
   * @return a report element for displaying an image based on a URL.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   * @throws IOException if there is a problem with the image URL.
   *
   * @deprecated use createImageURLElement instead
   */
  public static ImageElement createImageURLFunction(String name,
                                                    Rectangle2D bounds,
                                                    Paint paint,
                                                    String function)
      throws IOException
  {
    return createImageURLElement(name, bounds, paint, function);
  }

  /**
   * Creates a new ImageElement.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the color of this element (currently not used)
   * @param field  the name of the column/function/expression that returns the URL for the image.
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   * @throws IOException if there is a problem with the image URL.
   *
   * @deprecated use createImageDataRowElement instead
   */
  public static ImageElement createImageFieldElement(String name,
                                                     Rectangle2D bounds,
                                                     Paint paint,
                                                     String field)
      throws IOException
  {
    return createImageDataRowElement(name, bounds, paint, field);
  }

  /**
   * Creates a new ImageElement.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the color of this element (currently not used)
   * @param field  the name of the column/function/expression that returns the URL for the image.
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   * @throws IOException if there is a problem with the image URL.
   */
  public static ImageElement createImageDataRowElement(String name,
                                                       Rectangle2D bounds,
                                                       Paint paint,
                                                       String field)
      throws IOException
  {
    ImageRefFilter filter = new ImageRefFilter();
    filter.setDataSource(new DataRowDataSource(field));

    ImageElement element = new ImageElement();
    element.setName(name);
    element.setPaint(paint);
    element.setBounds(bounds);
    element.setDataSource(filter);
    return element;
  }

  /**
   * Creates a new ImageElement.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the color of this element (currently not used)
   * @param function  the name of the function that returns the URL for the image.
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   * @throws IOException if there is a problem with the image URL.
   *
   * @deprecated use createImageDataRowElement instead
   */
  public static ImageElement createImageFunctionElement(String name,
                                                        Rectangle2D bounds,
                                                        Paint paint,
                                                        String function)
      throws IOException
  {
    return createImageDataRowElement(name, bounds, paint, function);
  }

  /**
   * Creates a new TextElement containing a label.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param labeltext the text to display
   *
   * @return a report element for displaying a label (static text).
   *
   * @throws NullPointerException if bounds, name, format or field are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createLabelElement(String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String labeltext)
  {
    TextElement label = new TextElement();
    label.setName(name);
    label.setBounds(bounds);
    label.setPaint(paint);
    label.setAlignment(alignment);
    label.setFont(font);
    label.setDataSource(new StaticDataSource(labeltext));
    return label;
  }

  /**
   * Creates a new LineShapeElement.
   *
   * @param name the name of the new element
   * @param paint the line color of this element
   * @param stroke the stroke of this shape. For pdf use, restrict to BasicStokes.
   * @param shape the Line2D shape
   *
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static LineShapeElement createLineShapeElement(String name,
                                                        Paint paint,
                                                        Stroke stroke,
                                                        Line2D shape)
  {
    LineShapeElement line = new LineShapeElement();
    line.setName(name);
    line.setPaint(paint);
    line.setStroke(stroke);
    line.setShape(shape);
    return line;
  }

  /**
   * Creates a new RectangleShapeElement.
   *
   * @param name the name of the new element
   * @param paint the line color of this element
   * @param stroke the stroke of this shape. For pdf use, restrict to BasicStokes.
   * @param shape the Rectangle2D shape
   * @param shouldDraw  a flag controlling whether or not the shape outline is drawn.
   * @param shouldFill  a flag controlling whether or not the shape interior is filled.
   *
   * @return a report element for drawing a rectangle.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static RectangleShapeElement createRectangleShapeElement(String name,
                                                                  Paint paint,
                                                                  Stroke stroke,
                                                                  Rectangle2D shape,
                                                                  boolean shouldDraw,
                                                                  boolean shouldFill)
  {
    RectangleShapeElement line = new RectangleShapeElement();
    line.setName(name);
    line.setPaint(paint);
    line.setStroke(stroke);
    line.setShape(shape);
    line.setShouldDraw(shouldDraw);
    line.setShouldFill(shouldFill);
    return line;
  }

  /**
   * Creates a new TextElement. The difference between StringElements and MultilineTextElements
   * is historical and no longer relevant.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field the field in the datamodel to retrieve values from
   *
   * @return a report element for displaying text on multiple lines.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   *
   * @deprecated use createStringElement instead
   */
  public static TextElement createMultilineTextElement(String name,
                                                       Rectangle2D bounds,
                                                       Paint paint,
                                                       int alignment,
                                                       Font font,
                                                       String nullString,
                                                       String field)
  {
    return createStringElement(name, bounds, paint, alignment, font, nullString, field);
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
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
  public static TextElement createNumberElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                int alignment,
                                                Font font,
                                                String nullString,
                                                NumberFormat format,
                                                String field)
  {
    NumberFormatFilter filter = new NumberFormatFilter();
    filter.setFormatter(format);
    filter.setDataSource(new DataRowDataSource(field));

    TextElement element = new TextElement();
    element.setName(name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.setDataSource(filter);
    return element;
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
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
  public static TextElement createNumberElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                int alignment,
                                                Font font,
                                                String nullString,
                                                String format,
                                                String field)
  {
    DecimalFormatFilter filter = new DecimalFormatFilter();
    if (format != null) filter.setFormatString(format);
    filter.setDataSource(new DataRowDataSource(field));

    TextElement element = new TextElement();
    element.setName(name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.setDataSource(filter);
    return element;
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param function the function to retrieve values from
   * @param format the DecimalFormatString used in this text field
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   *
   * @deprecated use createNumberElement instead
   */
  public static TextElement createNumberFunction(String name,
                                                 Rectangle2D bounds,
                                                 Paint paint,
                                                 int alignment,
                                                 Font font,
                                                 String nullString,
                                                 String format,
                                                 String function)
  {
    return createNumberElement(name, bounds, paint, alignment, font, nullString, format, function);
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param function the function to retrieve values from
   * @param format the NumberFormat used in this text field
   *
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   *
   * @deprecated use createNumberElement instead
   */
  public static TextElement createNumberFunction(String name,
                                                 Rectangle2D bounds,
                                                 Paint paint,
                                                 int alignment,
                                                 Font font,
                                                 String nullString,
                                                 NumberFormat format,
                                                 String function)
  {
    return createNumberElement(name, bounds, paint, alignment, font, nullString, format, function);
  }

  /**
   * Creates a new TextElement without any additional filtering.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field the field in the datamodel to retrieve values from
   *
   * @return a report element for displaying <code>String</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createStringElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                int alignment,
                                                Font font,
                                                String nullString,
                                                String field)
  {
    TextElement element = new TextElement();
    element.setName(name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.setDataSource(new DataRowDataSource(field));
    return element;
  }

  /**
   * Creates a new TextElement without any additional filtering.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment the text alignment (one of ElementConstants.LEFT,
   *        ElementConstants.CENTER, ElementConstants.RIGHT
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param function the name of the function to retrieve values from
   *
   * @return a report element for displaying <code>String</code> objects.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   *
   * @deprecated use createStringElement instead
   */
  public static TextElement createStringFunction(String name,
                                                 Rectangle2D bounds,
                                                 Paint paint,
                                                 int alignment,
                                                 Font font,
                                                 String nullString,
                                                 String function)
  {
    return createStringElement(name, bounds, paint, alignment, font, nullString, function);
  }

  /**
   * Creates a GroupFooter-band with the specified height and the DefaultFont and DefaultPaint
   * given.
   *
   * @param height the height of the band in points
   * @param defaultFont the default font for this band
   * @param defaultPaint the default paint for this band
   *
   * @return the GroupFooter
   */
  public static Band createGroupFooter(float height, Font defaultFont, Paint defaultPaint)
  {
    GroupFooter footer = new GroupFooter();
    footer.setHeight(height);
    footer.setDefaultFont(defaultFont);
    footer.setDefaultPaint(defaultPaint);
    return footer;
  }

  /**
   * Creates a GroupHeader-band with the specified height and the DefaultFont and DefaultPaint
   * given.
   *
   * @param height the height of the band in points
   * @param defaultFont the default font for this band
   * @param defaultPaint the default paint for this band
   * @param pageBreak a flag indicating whether to do a pagebreak before this header is printed
   *
   * @return the GroupHeader
   */
  public static Band createGroupHeader(float height, Font defaultFont, Paint defaultPaint,
                                       boolean pageBreak)
  {
    GroupHeader header = new GroupHeader();
    header.setHeight(height);
    header.setDefaultFont(defaultFont);
    header.setDefaultPaint(defaultPaint);
    header.setPageBreakBeforePrint(pageBreak);
    return header;
  }

  /**
   * Creates a PageFooter-band with the specified height and the DefaultFont and DefaultPaint
   * given.
   *
   * @param height the height of the band in points
   * @param defaultFont the default font for this band
   * @param defaultPaint the default paint for this band
   * @param onfirstpage a flag indicating whether to print this footer on the first page of the
   *                    report
   * @param onlastpage a flag indicating whether to print this footer on the last page of the
   *                   report
   *
   * @return the PageFooter
   */
  public static Band createPageFooter(float height, Font defaultFont, Paint defaultPaint,
                                      boolean onfirstpage, boolean onlastpage)
  {
    PageFooter footer = new PageFooter();
    footer.setHeight(height);
    footer.setDefaultFont(defaultFont);
    footer.setDefaultPaint(defaultPaint);
    footer.setDisplayOnFirstPage(onfirstpage);
    footer.setDisplayOnLastPage(onlastpage);
    return footer;
  }

  /**
   * Creates a PageHeader-band with the specified height and the DefaultFont and DefaultPaint
   * given.
   *
   * @param height the height of the band in points
   * @param defaultFont the default font for this band
   * @param defaultPaint the default paint for this band
   * @param onfirstpage a flag indicating whether to print this footer on the first page of the
   *                    report
   * @param onlastpage a flag indicating whether to print this footer on the last page of the
   *                   report
   *
   * @return the PageHeader
   */
  public static Band createPageHeader(float height, Font defaultFont, Paint defaultPaint,
                                      boolean onfirstpage, boolean onlastpage)
  {
    PageHeader header = new PageHeader();
    header.setHeight(height);
    header.setDefaultFont(defaultFont);
    header.setDefaultPaint(defaultPaint);
    header.setDisplayOnFirstPage(onfirstpage);
    header.setDisplayOnLastPage(onlastpage);
    return header;
  }

  /**
   * Creates a ReportFooter-band with the specified height and the DefaultFont and DefaultPaint
   * given.
   *
   * @param height the height of the band in points
   * @param defaultFont the default font for this band
   * @param defaultPaint the default paint for this band
   * @param isownpage a flag indicating whether to issue a pagebreak before the report footer is
   *                  printed
   *
   * @return the ReportFooter
   */
  public static Band createReportFooter(float height, Font defaultFont, Paint defaultPaint,
                                        boolean isownpage)
  {
    ReportFooter footer = new ReportFooter();
    footer.setHeight(height);
    footer.setDefaultFont(defaultFont);
    footer.setDefaultPaint(defaultPaint);
    footer.setOwnPage(isownpage);
    return footer;
  }

  /**
   * Creates a ReportHeader-band with the specified height and the DefaultFont and DefaultPaint
   * given.
   *
   * @param height the height of the band in points
   * @param defaultFont the default font for this band
   * @param defaultPaint the default paint for this band
   * @param isownpage a flag indicating whether to issue a pagebreak after the report header is
   *                  printed
   *
   * @return the ReportHeader
   */
  public static Band createReportHeader(float height, Font defaultFont, Paint defaultPaint,
                                        boolean isownpage)
  {
    ReportHeader header = new ReportHeader();
    header.setHeight(height);
    header.setDefaultFont(defaultFont);
    header.setDefaultPaint(defaultPaint);
    header.setOwnPage(isownpage);
    return header;
  }


  /**
   * Creates an ItemBand with the specified height and the DefaultFont and DefaultPaint
   * given.
   *
   * @param height the height of the band in points
   * @param defaultFont the default font for this band
   * @param defaultPaint the default paint for this band
   *
   * @return the ReportFooter
   */
  public static Band createItemBand(float height, Font defaultFont, Paint defaultPaint)
  {
    ItemBand band = new ItemBand();
    band.setHeight(height);
    band.setDefaultFont(defaultFont);
    band.setDefaultPaint(defaultPaint);
    return band;
  }

  /**
   * Creates a new group with the specified name and GroupHeader and -footer. The columnnames
   * forming the group are contained as Strings in the fields list. If no field list is given,
   * a default group is created. This group contains all rows of the datamodel and behaves like
   * a report header and -footer.
   *
   * @param name the name of the group
   * @param fields the fields as a list of strings (optional)
   * @param footer the optional groupfooter
   * @param header the optional groupheader
   *
   * @return the ReportFooter
   */
  public static Group createGroup(String name, List fields, GroupFooter footer, GroupHeader header)
  {
    Group g = new Group();
    g.setName(name);
    if (fields != null)
    {
      g.setFields(fields);
    }
    if (footer != null)
    {
      g.setFooter(footer);
    }
    if (header != null)
    {
      g.setHeader(header);
    }
    return g;
  }

  /**
   * Creates a new ReportDefinition.
   *
   * @param name the name of the report
   * @param rheader the optional report header
   * @param rfooter the optional report footer
   * @param pheader the optional page header
   * @param pfooter the optional page footer
   * @param groups the list of groups for this report (optional)
   * @param items the itemband for this report (optional)
   * @param functions the optional functioncollection used in this report
   * @param pageformat the (optional) default pageformat
   * @param data the data for this report, which is optional at this point.
   *
   * @return the created report.
   */
  public static JFreeReport createReport(String name,
                                         ReportHeader rheader,
                                         ReportFooter rfooter,
                                         PageHeader pheader,
                                         PageFooter pfooter,
                                         GroupList groups,
                                         ItemBand items,
                                         ExpressionCollection functions,
                                         ExpressionCollection expressions,
                                         PageFormat pageformat,
                                         TableModel data)
  {
    JFreeReport report = new JFreeReport();
    report.setName(name);
    if (rheader != null)
    {
      report.setReportHeader(rheader);
    }
    if (rfooter != null)
    {
      report.setReportFooter(rfooter);
    }
    if (pheader != null)
    {
      report.setPageHeader(pheader);
    }
    if (pfooter != null)
    {
      report.setPageFooter(pfooter);
    }
    if (items != null)
    {
      report.setItemBand(items);
    }
    if (functions != null)
    {
      report.setFunctions(functions);
    }
    if (expressions != null)
    {
      report.setExpressions(expressions);
    }
    if (data != null)
    {
      report.setData(data);
    }
    if (groups != null)
    {
      report.setGroups(groups);
    }
    report.setDefaultPageFormat(pageformat);
    return report;
  }

}
