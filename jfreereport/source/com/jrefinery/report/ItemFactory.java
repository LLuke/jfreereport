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
 * ItemFactory.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemFactory.java,v 1.35 2003/02/24 14:57:15 mungady Exp $
 *
 * Changes
 * -------
 * 16-May-2002 : Initial version
 * 27-May-2002 : Support for the rectangle element
 * 09-Jun-2002 : Documentation
 * 30-Jun-2002 : Added Support for ImageField, ImageFunction
 * 10-Jul-2002 : Added Support for ImageURLField, ImageURLFunction
 * 31-Aug-2002 : Replaced ReportDataSource and FunctionDataSource with DataRowDataSource
 *               Deprecated create*Function and create*Field methods.
 * 06-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 * 15-Jan-2003 : Use templates for all element datasources.
 * 25-Jan-2003 : Added ResourceBundleElement and -Field
 * 04-Feb-2003 : Added javaDoc for ResourceBundleElement and -Field
 * 24-Feb-2003 : Fixed vertical alignment translation bug in createLabelElement(...) method (DG);
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.DateFormatFilter;
import com.jrefinery.report.filter.NumberFormatFilter;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.templates.DateFieldTemplate;
import com.jrefinery.report.filter.templates.ImageFieldTemplate;
import com.jrefinery.report.filter.templates.ImageURLElementTemplate;
import com.jrefinery.report.filter.templates.ImageURLFieldTemplate;
import com.jrefinery.report.filter.templates.LabelTemplate;
import com.jrefinery.report.filter.templates.NumberFieldTemplate;
import com.jrefinery.report.filter.templates.ResourceFieldTemplate;
import com.jrefinery.report.filter.templates.ResourceLabelTemplate;
import com.jrefinery.report.filter.templates.StringFieldTemplate;
import com.jrefinery.report.function.ExpressionCollection;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * A factory used to create elements and bands using a single line command.
 * The factory creates elements suitable for the static layout.
 *
 * @author Thomas Morgner
 */
public class ItemFactory
{
  /**
   * Constructor. Not used.
   */
  protected ItemFactory()
  {
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
  public static TextElement createDateElement(String name,
                                              Rectangle2D bounds,
                                              Paint paint,
                                              int alignment,
                                              Font font,
                                              String nullString,
                                              String format,
                                              String field)
  {
    return createDateElement(name,
                             bounds,
                             paint,
                             alignment,
                             ElementAlignment.TOP.getOldAlignment(),
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
  public static TextElement createDateElement(String name,
                                              Rectangle2D bounds,
                                              Paint paint,
                                              int alignment,
                                              int valign,
                                              Font font,
                                              String nullString,
                                              String format,
                                              String field)
  {

    DateFieldTemplate dft = new DateFieldTemplate();
    if (format != null)
    {
      dft.setFormat(format);
    }
    dft.setNullValue(nullString);
    dft.setField(field);

    TextElement dateElement = new TextElement();
    if (name != null)
    {
      dateElement.setName(name);
    }
    setElementBounds(dateElement, bounds);
    if (paint != null)
    {
      dateElement.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    if (font != null)
    {
      dateElement.getStyle().setFontDefinitionProperty(new FontDefinition(font));
    }
    dateElement.getStyle().setStyleProperty(
        ElementStyleSheet.ALIGNMENT, 
        ElementAlignment.translateHorizontalAlignment(alignment));
    dateElement.getStyle().setStyleProperty(
        ElementStyleSheet.VALIGNMENT, 
        ElementAlignment.translateVerticalAlignment(valign));
    dateElement.setDataSource(dft);

    return dateElement;
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
  public static TextElement createDateElement(String name,
                                              Rectangle2D bounds,
                                              Paint paint,
                                              int alignment,
                                              Font font,
                                              String nullString,
                                              DateFormat format,
                                              String field)
  {
    return createDateElement(name, bounds, paint, alignment, 
                             ElementAlignment.TOP.getOldAlignment(), 
                             font, nullString, format, field);
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
  public static TextElement createDateElement(String name,
                                              Rectangle2D bounds,
                                              Paint paint,
                                              int alignment,
                                              int valign,
                                              Font font,
                                              String nullString,
                                              DateFormat format,
                                              String field)
  {
    DataSource ds;
    if (format instanceof SimpleDateFormat)
    {
      DateFieldTemplate dft = new DateFieldTemplate();
      dft.setDateFormat((SimpleDateFormat) format);
      dft.setNullValue(nullString);
      dft.setField(field);
      ds = dft;
    }
    else
    {
      DateFormatFilter filter = new DateFormatFilter();
      if (format != null)
      {
        filter.setFormatter(format);
      }
      filter.setDataSource(new DataRowDataSource(field));
      ds = filter;
    }

    TextElement dateElement = new TextElement();
    if (name != null)
    {
      dateElement.setName(name);
    }
    setElementBounds(dateElement, bounds);
    if (paint != null)
    {
      dateElement.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    if (font != null)
    {
      dateElement.getStyle().setFontDefinitionProperty(new FontDefinition(font));
    }
    dateElement.getStyle().setStyleProperty(
        ElementStyleSheet.ALIGNMENT, 
        ElementAlignment.translateHorizontalAlignment(alignment));
    dateElement.getStyle().setStyleProperty(
        ElementStyleSheet.VALIGNMENT, 
        ElementAlignment.translateVerticalAlignment(valign));
    dateElement.setDataSource(ds);
    return dateElement;
  }

  /**
   * Creates a new TextElement containing a date filter structure.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment  the horizontal text alignment.
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
   * @param alignment  the horizontal text alignment.
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
   * @param alignment  the horizontal text alignment.
   * @param font the font for this element
   * @param nullString the text used when the value of this element is null
   * @param function the function to retrieve values from
   *
   * @return a report element for displaying a general object.
   *
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   * @deprecated don't use the itemfactory for that kind of element or use the
   * createStringElement method
   */
  public static TextElement createGeneralElement(String name,
                                                 Rectangle2D bounds,
                                                 Paint paint,
                                                 int alignment,
                                                 Font font,
                                                 String nullString,
                                                 String function)
  {
    return createStringElement(name, bounds, paint, alignment, font,  nullString, function);
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
   */
  public static ImageElement createImageElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                URL source)
  {
    return createImageElement(name, bounds, paint, source, true);
  }

  /**
   * Creates a new ImageElement. The source URL is predefined in an StaticDataSource and will
   * not change during the report processing.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the color of this element (currently not used).
   * @param source  the source url from where to load the image.
   * @param scale  scale the image?
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                URL source,
                                                boolean scale)
  {
    return createImageElement(name, bounds, paint, source, scale, false);
  }

  /**
   * Creates a new ImageElement. The source URL is predefined in an StaticDataSource and will
   * not change during the report processing.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the color of this element (currently not used).
   * @param source  the source url from where to load the image.
   * @param scale  scale the image?
   * @param keepAspectRatio  preserve the aspect ratio?
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                URL source,
                                                boolean scale,
                                                boolean keepAspectRatio)
  {
    ImageURLElementTemplate template = new ImageURLElementTemplate();
    template.setContent(source.toExternalForm());

    ImageElement element = new ImageElement();
    if (name != null)
    {
      element.setName(name);
    }
    if (paint != null)
    {
      element.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    setElementBounds(element, bounds);
    element.setDataSource(template);
    element.setScale(scale);
    element.setKeepAspectRatio(keepAspectRatio);
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
   *
   * @deprecated use createImageURLElement instead
   */
  public static ImageElement createImageURLField(String name,
                                                 Rectangle2D bounds,
                                                 Paint paint,
                                                 String field)
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
   */
  public static ImageElement createImageURLElement(String name,
                                                   Rectangle2D bounds,
                                                   Paint paint,
                                                   String field)
  {
    return createImageURLElement(name, bounds, paint, field, true);
  }

  /**
   * Creates a new ImageElement, which is fed from an URL stored in the datasource.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the color of this element (currently not used).
   * @param field  the name of the column/function/expression that returns the URL for the image.
   * @param scale  scale the image?
   *
   * @return a report element for displaying an image based on a URL.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageURLElement(String name,
                                                   Rectangle2D bounds,
                                                   Paint paint,
                                                   String field,
                                                   boolean scale)
  {
    return createImageURLElement(name, bounds, paint, field, scale, false);
  }

  /**
   * Creates a new ImageElement, which is fed from an URL stored in the datasource.
   *
   * @param name  the name of the new element
   * @param bounds  the bounds of the new element
   * @param paint  the color of this element (currently not used)
   * @param field  the name of the column/function/expression that returns the URL for the image.
   * @param scale  true if the content should be scaled to fit.
   * @param keepAspectRatio  preserve the aspect ratio.
   *
   * @return a report element for displaying an image based on a URL.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageURLElement(String name,
                                                   Rectangle2D bounds,
                                                   Paint paint,
                                                   String field,
                                                   boolean scale,
                                                   boolean keepAspectRatio)
  {
    ImageURLFieldTemplate template = new ImageURLFieldTemplate();
    template.setField(field);

    ImageElement element = new ImageElement();
    if (name != null)
    {
      element.setName(name);
    }
    if (paint != null)
    {
      element.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    setElementBounds(element, bounds);
    element.setDataSource(template);
    element.setScale(scale);
    element.setKeepAspectRatio(keepAspectRatio);
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
   *
   * @deprecated use createImageURLElement instead
   */
  public static ImageElement createImageURLFunction(String name,
                                                    Rectangle2D bounds,
                                                    Paint paint,
                                                    String function)
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
   *
   * @deprecated use createImageDataRowElement instead
   */
  public static ImageElement createImageFieldElement(String name,
                                                     Rectangle2D bounds,
                                                     Paint paint,
                                                     String field)
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
   */
  public static ImageElement createImageDataRowElement(String name,
                                                       Rectangle2D bounds,
                                                       Paint paint,
                                                       String field
                                                       )
  {
    return createImageDataRowElement(name, bounds, paint, field, true);
  }

  /**
   * Creates a new ImageElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the color of this element (currently not used).
   * @param field  the name of the column/function/expression that returns the URL for the image.
   * @param scale  scale the image?
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageDataRowElement(String name,
                                                       Rectangle2D bounds,
                                                       Paint paint,
                                                       String field,
                                                       boolean scale)
  {
    return createImageDataRowElement(name, bounds, paint, field, scale, false);
  }
  /**
   * Creates a new ImageElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the color of this element (currently not used).
   * @param field  the name of the column/function/expression that returns the URL for the image.
   * @param scale  scale the image?
   * @param keepAspectRatio  preserve the aspect ratio?
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageDataRowElement(String name,
                                                       Rectangle2D bounds,
                                                       Paint paint,
                                                       String field,
                                                       boolean scale,
                                                       boolean keepAspectRatio)
  {
    ImageFieldTemplate template = new ImageFieldTemplate();
    template.setField(field);
    ImageElement element = new ImageElement();
    if (name != null)
    {
      element.setName(name);
    }
    if (paint != null)
    {
      element.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    setElementBounds(element, bounds);
    element.setDataSource(template);
    element.setScale(scale);
    element.setKeepAspectRatio(keepAspectRatio);
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
   *
   * @deprecated use createImageDataRowElement instead
   */
  public static ImageElement createImageFunctionElement(String name,
                                                        Rectangle2D bounds,
                                                        Paint paint,
                                                        String function)
  {
    return createImageDataRowElement(name, bounds, paint, function);
  }

  /**
   * Creates a new TextElement containing a label.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment  the horizontal text alignment.
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
    return createLabelElement(name, bounds, paint, alignment, 
                              ElementAlignment.TOP.getOldAlignment(), 
                              font, labeltext);
  }

  /**
   * Creates a new {@link TextElement} containing a label.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the text color of this text element.
   * @param alignment  the horizontal alignment.
   * @param valign  the vertical alignment.
   * @param font  the font for this element.
   * @param labeltext  the text to display.
   *
   * @return a report element for displaying a label (static text).
   *
   * @throws NullPointerException if bounds, name, format or field are <code>null</code>.
   * @throws IllegalArgumentException if the given alignment is invalid.
   */
  public static TextElement createLabelElement(String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               int valign,
                                               Font font,
                                               String labeltext)
  {
    LabelTemplate template = new LabelTemplate();
    template.setContent(labeltext);

    TextElement label = new TextElement();
    if (name != null)
    {
      label.setName(name);
    }
    setElementBounds(label, bounds);
    if (paint != null)
    {
      label.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    if (font != null)
    {
      label.getStyle().setFontDefinitionProperty(new FontDefinition(font));
    }
    label.getStyle().setStyleProperty(
        ElementStyleSheet.ALIGNMENT, 
        ElementAlignment.translateHorizontalAlignment(alignment));
    label.getStyle().setStyleProperty(
        ElementStyleSheet.VALIGNMENT, 
        ElementAlignment.translateVerticalAlignment(valign));
    label.setDataSource(template);
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
  public static ShapeElement createLineShapeElement(String name,
                                                        Paint paint,
                                                        Stroke stroke,
                                                        Line2D shape)
  {
    if (shape.getX1() == shape.getX2() && shape.getY1 () == shape.getY2())
    {
      // scale the line, is horizontal
      shape.setLine(0, shape.getY1(), 100, shape.getY2());
      Rectangle2D bounds = new Rectangle2D.Float (0, (float) shape.getY1(), -100, 0);
      return createShapeElement(name, bounds, paint, stroke, shape, true, false, true);
    }
    else
    {
      return createShapeElement(name, paint, stroke, shape, true, false);
    }
  }

  /**
   * Creates a new LineShapeElement.
   *
   * @param name  the name of the new element.
   * @param paint  the line color of this element.
   * @param stroke  the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param shape  the shape.
   * @param shouldDraw  draw the shape?
   * @param shouldFill  fill the shape?
   *
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(String name,
                                                Paint paint,
                                                Stroke stroke,
                                                Shape shape,
                                                boolean shouldDraw,
                                                boolean shouldFill)
  {
    return createShapeElement(name, shape.getBounds2D(), paint, stroke, shape,
                              shouldDraw, shouldFill, true);
  }


  /**
   * Creates a new ShapeElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds.
   * @param paint  the line color of this element.
   * @param stroke  the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param shape  the shape.
   * @param shouldDraw  draw the shape?
   * @param shouldFill  fill the shape?
   * @param shouldScale  scale the shape?
   *
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                Stroke stroke,
                                                Shape shape,
                                                boolean shouldDraw,
                                                boolean shouldFill,
                                                boolean shouldScale)
  {
    return createShapeElement(name, bounds, paint, stroke, shape, shouldDraw,
                              shouldFill, shouldScale, false);
  }

  /**
   * Creates a new ShapeElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds.
   * @param paint  the line color of this element.
   * @param stroke  the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param shape  the shape.
   * @param shouldDraw  draw the shape?
   * @param shouldFill  fill the shape?
   * @param shouldScale  scale the shape?
   * @param keepAspectRatio  preserve the aspect ratio?
   *
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                Stroke stroke,
                                                Shape shape,
                                                boolean shouldDraw,
                                                boolean shouldFill,
                                                boolean shouldScale,
                                                boolean keepAspectRatio)
  {
    ShapeElement shapeElement = new ShapeElement();
    if (name != null)
    {
      shapeElement.setName(name);
    }
    if (paint != null)
    {
      shapeElement.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    if (stroke != null)
    {
      shapeElement.setStroke(stroke);
    }
    shapeElement.setDataSource(new StaticDataSource(shape));
    shapeElement.setShouldDraw(shouldDraw);
    shapeElement.setShouldFill(shouldFill);
    shapeElement.setScale(shouldScale);
    shapeElement.setKeepAspectRatio(keepAspectRatio);
    setElementBounds(shapeElement, bounds);
    return shapeElement;
  }


  /**
   * Creates a new ShapeElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds.
   * @param paint  the line color of this element.
   * @param stroke  the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param fieldname  the fieldname from where to get the shape.
   * @param shouldDraw  draw the shape?
   * @param shouldFill  fill the shape?
   * @param shouldScale  scale the shape?
   * @param keepAspectRatio  preserve the aspect ratio?
   *
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                Stroke stroke,
                                                String fieldname,
                                                boolean shouldDraw,
                                                boolean shouldFill,
                                                boolean shouldScale,
                                                boolean keepAspectRatio)
  {
    ShapeElement shapeElement = new ShapeElement();
    if (name != null)
    {
      shapeElement.setName(name);
    }
    if (paint != null)
    {
      shapeElement.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    if (stroke != null)
    {
      shapeElement.setStroke(stroke);
    }
    shapeElement.setDataSource(new DataRowDataSource(fieldname));
    shapeElement.setShouldDraw(shouldDraw);
    shapeElement.setShouldFill(shouldFill);
    shapeElement.setScale(shouldScale);
    shapeElement.setKeepAspectRatio(keepAspectRatio);
    setElementBounds(shapeElement, bounds);
    return shapeElement;
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
  public static ShapeElement createRectangleShapeElement(String name,
                                                          Paint paint,
                                                          Stroke stroke,
                                                          Rectangle2D shape,
                                                          boolean shouldDraw,
                                                          boolean shouldFill)
  {
    if (shape.getX() < 0 || shape.getY() < 0 || shape.getWidth() < 0 || shape.getHeight() < 0)
    {
      return createShapeElement(name, shape, paint, stroke, new Rectangle2D.Float(0, 0, 100, 100),
                                shouldDraw, shouldFill, true);
    }
    return createShapeElement(name, paint, stroke, shape, shouldDraw, shouldFill);
  }

  /**
   * Creates a new TextElement. The difference between StringElements and MultilineTextElements
   * is historical and no longer relevant.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment  the horizontal text alignment.
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
  public static TextElement createNumberElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                int alignment,
                                                Font font,
                                                String nullString,
                                                NumberFormat format,
                                                String field)
  {
    return createNumberElement(name, bounds, paint, alignment, 
                               ElementAlignment.TOP.getOldAlignment(), 
                               font, nullString,
                               format, field);
  }
  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the text color of this text element.
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
  public static TextElement createNumberElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                int alignment,
                                                int valign,
                                                Font font,
                                                String nullString,
                                                NumberFormat format,
                                                String field)
  {
    DataSource ds;
    if (format instanceof DecimalFormat)
    {
      NumberFieldTemplate template = new NumberFieldTemplate ();
      template.setDecimalFormat((DecimalFormat) format);
      template.setNullValue(nullString);
      template.setField(field);
      ds = template;
    }
    else
    {
      NumberFormatFilter filter = new NumberFormatFilter();
      if (format != null)
      {
        filter.setFormatter(format);
      }
      filter.setDataSource(new DataRowDataSource(field));
      ds = filter;
    }

    TextElement element = new TextElement();
    if (name != null)
    {
      element.setName(name);
    }
    setElementBounds(element, bounds);
    if (paint != null)
    {
      element.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    if (font != null)
    {
      element.getStyle().setFontDefinitionProperty(new FontDefinition(font));
    }
    element.getStyle().setStyleProperty(
        ElementStyleSheet.ALIGNMENT, 
        ElementAlignment.translateHorizontalAlignment(alignment)
    );
    element.getStyle().setStyleProperty(
        ElementStyleSheet.VALIGNMENT, 
        ElementAlignment.translateVerticalAlignment(valign)
    );
    element.setDataSource(ds);
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
    return createNumberElement(name, bounds, paint, alignment, 
                               ElementAlignment.TOP.getOldAlignment(), 
                               font, nullString,
                               format, field);
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param paint  the text color of this text element.
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
  public static TextElement createNumberElement(String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                int alignment,
                                                int valign,
                                                Font font,
                                                String nullString,
                                                String format,
                                                String field)
  {
    NumberFieldTemplate template = new NumberFieldTemplate();
    if (format != null)
    {
      template.setFormat(format);
    }
    template.setNullValue(nullString);
    template.setField(field);

    TextElement element = new TextElement();
    if (name != null)
    {
      element.setName(name);
    }
    setElementBounds(element, bounds);
    if (paint != null)
    {
      element.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    if (font != null)
    {
      element.getStyle().setFontDefinitionProperty(new FontDefinition(font));
    }
    element.getStyle().setStyleProperty(
        ElementStyleSheet.ALIGNMENT, 
        ElementAlignment.translateHorizontalAlignment(alignment)
    );
    element.getStyle().setStyleProperty(
        ElementStyleSheet.VALIGNMENT, 
        ElementAlignment.translateVerticalAlignment(valign)
    );
    element.setDataSource(template);
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
   * @param alignment  the horizontal text alignment.
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
   * @param alignment  the horizontal text alignment.
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
    return createStringElement(name,
                               bounds,
                               paint,
                               alignment,
                               ElementAlignment.TOP.getOldAlignment(),
                               font,
                               nullString,
                               field);
  }

  /**
   * Creates a new TextElement without any additional filtering.
   *
   * @param name  the name of the new element
   * @param bounds  the bounds of the new element
   * @param paint  the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param valign  the vertical alignment.
   * @param font  the font for this element
   * @param nullString  the text used when the value of this element is null
   * @param field  the field in the datamodel to retrieve values from
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
                                                int valign,
                                                Font font,
                                                String nullString,
                                                String field)
  {
    StringFieldTemplate template = new StringFieldTemplate();
    template.setField(field);
    template.setNullValue(nullString);

    TextElement element = new TextElement();
    if (name != null)
    {
      element.setName(name);
    }
    setElementBounds(element, bounds);
    if (paint != null)
    {
      element.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    if (font != null)
    {
      element.getStyle().setFontDefinitionProperty(new FontDefinition(font));
    }
    element.getStyle().setStyleProperty(
        ElementStyleSheet.ALIGNMENT, 
        ElementAlignment.translateHorizontalAlignment(alignment)
    );
    element.getStyle().setStyleProperty(
        ElementStyleSheet.VALIGNMENT, 
        ElementAlignment.translateVerticalAlignment(valign)
    );
    element.setDataSource(template);
    return element;
  }

  /**
   * Creates a new TextElement without any additional filtering.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the text color of this text element
   * @param alignment  the horizontal text alignment.
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
    footer.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                       new FloatDimension(0, height));
    if (defaultFont != null)
    {
      footer.getBandDefaults().setFontDefinitionProperty(new FontDefinition(defaultFont));
    }
    if (defaultPaint != null)
    {
      footer.getBandDefaults().setStyleProperty(ElementStyleSheet.PAINT, defaultPaint);
    }
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
    header.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                       new FloatDimension(0, height));
    if (defaultFont != null)
    {
      header.getBandDefaults().setFontDefinitionProperty(new FontDefinition(defaultFont));
    }
    if (defaultPaint != null)
    {
      header.getBandDefaults().setStyleProperty(ElementStyleSheet.PAINT, defaultPaint);
    }
    header.getStyle().setStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE, new Boolean(pageBreak));
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
    footer.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                       new FloatDimension (0, height));
    if (defaultFont != null)
    {
      footer.getBandDefaults().setFontDefinitionProperty(new FontDefinition(defaultFont));
    }
    if (defaultPaint != null)
    {
      footer.getBandDefaults().setStyleProperty(ElementStyleSheet.PAINT, defaultPaint);
    }

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
    header.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                       new FloatDimension (0, height));
    if (defaultFont != null)
    {
      header.getBandDefaults().setFontDefinitionProperty(new FontDefinition(defaultFont));
    }
    if (defaultPaint != null)
    {
      header.getBandDefaults().setStyleProperty(ElementStyleSheet.PAINT, defaultPaint);
    }
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
    footer.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                       new FloatDimension (0, height));
    if (defaultFont != null)
    {
      footer.getBandDefaults().setFontDefinitionProperty(new FontDefinition(defaultFont));
    }
    if (defaultPaint != null)
    {
      footer.getBandDefaults().setStyleProperty(ElementStyleSheet.PAINT, defaultPaint);
    }
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
    header.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                       new FloatDimension (0, height));
    if (defaultFont != null)
    {
      header.getBandDefaults().setFontDefinitionProperty(new FontDefinition(defaultFont));
    }
    if (defaultPaint != null)
    {
      header.getBandDefaults().setStyleProperty(ElementStyleSheet.PAINT, defaultPaint);
    }
    header.setOwnPage(isownpage);
    return header;
  }


  /**
   * Creates an ItemBand.
   *
   * @param height  the height of the band in points.
   * @param defaultFont  the default font for this band.
   * @param defaultPaint  the default paint for this band.
   *
   * @return the band.
   */
  public static Band createItemBand(float height, Font defaultFont, Paint defaultPaint)
  {
    ItemBand band = new ItemBand();
    band.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension (0, height));
    if (defaultFont != null)
    {
      band.getBandDefaults().setFontDefinitionProperty(new FontDefinition(defaultFont));
    }
    if (defaultPaint != null)
    {
      band.getBandDefaults().setStyleProperty(ElementStyleSheet.PAINT, defaultPaint);
    }
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
   * Creates a new report.
   *
   * @param name  the name of the report.
   * @param rheader  the optional report header.
   * @param rfooter  the optional report footer.
   * @param pheader  the optional page header.
   * @param pfooter  the optional page footer.
   * @param groups  the list of groups for this report (optional).
   * @param items  the itemband for this report (optional).
   * @param functions  the (optional) functions used in this report.
   * @param expressions  the (optional) expressions used in this report.
   * @param pageformat the (optional) default pageformat.
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

  /**
   * A utility method for setting the element bounds.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   */
  public static void setElementBounds (Element e, Rectangle2D bounds)
  {
    e.getStyle().setStyleProperty(StaticLayoutManager.ABSOLUTE_POS,
                                  new Point2D.Float((float) bounds.getX(), (float) bounds.getY()));
    e.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                  new FloatDimension((float) bounds.getWidth(),
                                                     (float) bounds.getHeight()));
  }

  /**
   * creates a ResourceElement. ResourceElements resolve their value using a
   * <code>java.util.ResourceBundle</code>.
   *
   * @param name the name of the element (null allowed)
   * @param bounds the element's bounds
   * @param color the text color of the element
   * @param alignment the element's horizontal text alignment
   * @param valignment the element's vertical text alignment
   * @param font the elements font
   * @param nullValue the text used when the value of this element is null
   * @param field the field in the datamodel to retrieve values from
   * @param resourceBase the classname/basename of the assigned resource bundle
   * @return the created ResourceElement
   */
  public static TextElement createResourceElement(String name,
                                                  Rectangle2D bounds,
                                                  Color color,
                                                  int alignment,
                                                  int valignment,
                                                  Font font,
                                                  String nullValue,
                                                  String resourceBase,
                                                  String field)
  {
    ResourceFieldTemplate template = new ResourceFieldTemplate();
    template.setField(field);
    template.setNullValue(nullValue);
    template.setResourceClassName(resourceBase);

    TextElement element = new TextElement();
    if (name != null)
    {
      element.setName(name);
    }
    setElementBounds(element, bounds);
    if (color != null)
    {
      element.getStyle().setStyleProperty(ElementStyleSheet.PAINT, color);
    }
    if (font != null)
    {
      element.getStyle().setFontDefinitionProperty(new FontDefinition(font));
    }
    element.getStyle().setStyleProperty(
        ElementStyleSheet.ALIGNMENT, 
        ElementAlignment.translateHorizontalAlignment(alignment)
    );
    element.getStyle().setStyleProperty(
        ElementStyleSheet.VALIGNMENT, 
        ElementAlignment.translateVerticalAlignment(valignment)
    );
    element.setDataSource(template);
    return element;

  }

  /**
   * creates a ResourceElement. ResourceElements resolve their value using a
   * <code>java.util.ResourceBundle</code>.
   *
   * @param name the name of the element (null allowed)
   * @param bounds the element's bounds
   * @param paint the text color of the element
   * @param alignment the element's horizontal text alignment
   * @param valign the element's vertical text alignment
   * @param font the elements font
   * @param nullValue the text used when the value of this element is null
   * @param resourceKey the key which is used to query the resource bundle
   * @param resourceBase the classname/basename of the assigned resource bundle
   * @return the created ResourceElement
   */
  public static TextElement createResourceLabel(String name,
                                                Rectangle2D bounds,
                                                Color paint,
                                                int alignment,
                                                int valign,
                                                Font font,
                                                String nullValue,
                                                String resourceBase,
                                                String resourceKey)
  {
    ResourceLabelTemplate template = new ResourceLabelTemplate();
    template.setResourceClassName(resourceBase);
    template.setContent(resourceKey);
    template.setNullValue(nullValue);

    TextElement label = new TextElement();
    if (name != null)
    {
      label.setName(name);
    }
    setElementBounds(label, bounds);
    if (paint != null)
    {
      label.getStyle().setStyleProperty(ElementStyleSheet.PAINT, paint);
    }
    if (font != null)
    {
      label.getStyle().setFontDefinitionProperty(new FontDefinition(font));
    }
    label.getStyle().setStyleProperty(
        ElementStyleSheet.ALIGNMENT, 
        ElementAlignment.translateHorizontalAlignment(alignment)
    );
    label.getStyle().setStyleProperty(
        ElementStyleSheet.VALIGNMENT, 
        ElementAlignment.translateVerticalAlignment(valign)
    );
    label.setDataSource(template);
    return label;
  }
}
