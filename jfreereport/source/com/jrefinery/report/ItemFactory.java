/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * ItemFactory.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 16-May-2002 : Initial version
 * 27-May-2002 : Support for the rectangle element
 */
package com.jrefinery.report;

import com.jrefinery.report.filter.DateFormatFilter;
import com.jrefinery.report.filter.SimpleDateFormatFilter;
import com.jrefinery.report.filter.ReportDataSource;
import com.jrefinery.report.filter.FunctionDataSource;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.NumberFormatFilter;
import com.jrefinery.report.filter.DecimalFormatFilter;

import javax.swing.table.TableModel;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.print.PageFormat;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.net.URL;
import java.io.IOException;

/**
 * A factory used to create elements and bands using a single line command.
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
   * @throws NullPointerException if bounds, name, format or field are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createDateElement (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String format,
                                               String field)
  {
    SimpleDateFormatFilter filter = new SimpleDateFormatFilter();
    filter.setFormatString(format);
    filter.setDataSource(new ReportDataSource(field));

    TextElement dateElement = new TextElement();
    dateElement.setName (name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.getTextFilter().setDataSource(filter);
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
   * @throws NullPointerException if bounds, name, format or field are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createDateElement (String name,
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
    filter.setDataSource(new ReportDataSource(field));

    TextElement dateElement = new TextElement();
    dateElement.setName (name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.getTextFilter().setDataSource(filter);
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
   * @throws NullPointerException if bounds, name, format or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createDateFunction (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String format,
                                               String function)
  {
    SimpleDateFormatFilter filter = new SimpleDateFormatFilter();
    filter.setFormatString(format);
    filter.setDataSource(new FunctionDataSource(function));

    TextElement dateElement = new TextElement();
    dateElement.setName (name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.getTextFilter().setDataSource(filter);
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
   * @throws NullPointerException if bounds, name, format or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createDateFunction (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               DateFormat format,
                                               String function)
  {
    DateFormatFilter filter = new DateFormatFilter();
    filter.setFormatter(format);
    filter.setDataSource(new FunctionDataSource(function));

    TextElement dateElement = new TextElement();
    dateElement.setName (name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.getTextFilter().setDataSource(filter);
    return dateElement;
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
   * @throws NullPointerException if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createGeneralElement (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String function)
  {
    TextElement gElement = new TextElement();
    gElement.setName (name);
    gElement.setBounds(bounds);
    gElement.setPaint(paint);
    gElement.setAlignment(alignment);
    gElement.setFont(font);
    gElement.setNullString(nullString);
    gElement.getTextFilter().setDataSource(new ReportDataSource(function));
    return gElement;
  }

  /**
   * Creates a new ImageElement.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the color of this element (currently not used)
   * @param source the source url from where to load the image
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageElement (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               URL source)
    throws IOException
  {
    ImageReference reference = new ImageReference(source);
    ImageElement element = new ImageElement();
    element.setName(name);
    element.setPaint(paint);
    element.setBounds(bounds);
    element.setImageReference(reference);
    return element;
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
   * @throws NullPointerException if bounds, name, format or field are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createLabelElement (String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                int alignment,
                                                Font font,
                                                String labeltext)
  {
    TextElement label = new TextElement ();
    label.setName (name);
    label.setBounds (bounds);
    label.setPaint (paint);
    label.setAlignment (alignment);
    label.setFont (font);
    label.getTextFilter().setDataSource(new StaticDataSource(labeltext));
    return label;
  }

  /**
   * Creates a new LineShapeElement.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param paint the line color of this element
   * @param stroke the stroke of this shape. For pdf use, restrict to BasicStokes.
   * @param shape the Line2D shape
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static LineShapeElement createLineShapeElement (String name,
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

  public static RectangleShapeElement createRectangleShapeElement (String name,
                                                         Paint paint,
                                                         Stroke stroke,
                                                         Rectangle2D shape)
  {
    RectangleShapeElement line = new RectangleShapeElement();
    line.setName(name);
    line.setPaint(paint);
    line.setStroke(stroke);
    line.setShape(shape);
    return line;
  }

  public static TextElement createMultilineTextElement (String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                int alignment,
                                                Font font,
                                                String nullstring,
                                                String field)
  {
    TextElement text = new TextElement ();
    text.setName (name);
    text.setBounds (bounds);
    text.setPaint (paint);
    text.setAlignment (alignment);
    text.setFont (font);
    text.setNullString(nullstring);
    text.getTextFilter().setDataSource(new ReportDataSource(field));
    return text;
  }

  public static TextElement createNumberElement (String name,
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
    filter.setDataSource(new ReportDataSource(field));

    TextElement element = new TextElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.getTextFilter().setDataSource(filter);
    return element;
  }

  public static TextElement createNumberElement (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String format,
                                               String field)
  {
    DecimalFormatFilter filter = new DecimalFormatFilter();
    filter.setFormatString(format);
    filter.setDataSource(new ReportDataSource(field));

    TextElement element = new TextElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.getTextFilter().setDataSource(filter);
    return element;
  }

  public static TextElement createNumberFunction (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String format,
                                               String function)
  {
    DecimalFormatFilter filter = new DecimalFormatFilter();
    filter.setFormatString(format);
    filter.setDataSource(new FunctionDataSource(function));

    TextElement element = new TextElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.getTextFilter().setDataSource(filter);
    return element;
  }

  public static TextElement createNumberFunction (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               NumberFormat format,
                                               String function)
  {
    NumberFormatFilter filter = new NumberFormatFilter();
    filter.setFormatter(format);
    filter.setDataSource(new FunctionDataSource(function));

    TextElement element = new TextElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.getTextFilter().setDataSource(filter);
    return element;
  }

  public static TextElement createStringElement (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String field)
  {
    TextElement element = new TextElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.getTextFilter().setDataSource(new ReportDataSource(field));
    return element;
  }

  public static TextElement createStringFunction (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String function)
  {
    TextElement element = new TextElement ();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.getTextFilter().setDataSource(new FunctionDataSource(function));
    return element;
  }

  public static Band createGroupFooter (float height, Font defaultFont, Paint defaultPaint)
  {
    GroupFooter footer = new GroupFooter();
    footer.setHeight(height);
    footer.setDefaultFont(defaultFont);
    footer.setDefaultPaint(defaultPaint);
    return footer;
  }

  public static Band createGroupHeader (float height, Font defaultFont, Paint defaultPaint, boolean pagebreak)
  {
    GroupHeader header = new GroupHeader();
    header.setHeight(height);
    header.setDefaultFont(defaultFont);
    header.setDefaultPaint(defaultPaint);
    header.setPageBreakBeforePrint(pagebreak);
    return header;
  }

  public static Band createPageFooter (float height, Font defaultFont, Paint defaultPaint, boolean onfirstpage, boolean onlagepage)
  {
    PageFooter footer = new PageFooter();
    footer.setHeight(height);
    footer.setDefaultFont(defaultFont);
    footer.setDefaultPaint(defaultPaint);
    footer.setDisplayOnFirstPage(onfirstpage);
    footer.setDisplayOnLastPage(onlagepage);
    return footer;
  }

  public static Band createPageHeader (float height, Font defaultFont, Paint defaultPaint, boolean onfirstpage, boolean onlagepage)
  {
    PageHeader header = new PageHeader ();
    header.setHeight(height);
    header.setDefaultFont(defaultFont);
    header.setDefaultPaint(defaultPaint);
    header.setDisplayOnFirstPage(onfirstpage);
    header.setDisplayOnLastPage(onlagepage);
    return header;
  }

  public static Band createReportFooter (float height, Font defaultFont, Paint defaultPaint, boolean isownpage)
  {
    ReportFooter footer = new ReportFooter ();
    footer.setHeight(height);
    footer.setDefaultFont(defaultFont);
    footer.setDefaultPaint(defaultPaint);
    footer.setOwnPage(isownpage);
    return footer;
  }

  public static Band createReportHeader (float height, Font defaultFont, Paint defaultPaint, boolean isownpage)
  {
    ReportHeader header = new ReportHeader ();
    header.setHeight(height);
    header.setDefaultFont(defaultFont);
    header.setDefaultPaint(defaultPaint);
    header.setOwnPage(isownpage);
    return header;
  }

  public static Band createItemBand (float height, Font defaultFont, Paint defaultPaint)
  {
    ItemBand band = new ItemBand();
    band.setHeight(height);
    band.setDefaultFont(defaultFont);
    band.setDefaultPaint(defaultPaint);
    return band;
  }

  public static Group createGroup (String name, List fields, GroupFooter footer, GroupHeader header)
  {
    Group g = new Group ();
    g.setName(name);
    if (fields != null) g.setFields(fields);
    if (footer != null) g.setFooter(footer);
    if (header != null) g.setHeader(header);
    return g;
  }

  public static JFreeReport createReport (String name,
                                          ReportHeader rheader,
                                          ReportFooter rfooter,
                                          PageHeader pheader,
                                          PageFooter pfooter,
                                          GroupList groups,
                                          ItemBand items,
                                          FunctionCollection functions,
                                          PageFormat pageformat,
                                          TableModel data)
  {
    JFreeReport report = new JFreeReport();
    report.setName(name);
    if (rheader != null) report.setReportHeader(rheader);
    if (rfooter != null) report.setReportFooter(rfooter);
    if (pheader != null) report.setPageHeader(pheader);
    if (pfooter != null) report.setPageFooter(pfooter);
    if (items != null) report.setItemBand(items);
    if (functions != null) report.setFunctions(functions);
    if (data != null) report.setData(data);
    if (groups != null) report.setGroups(groups);
    report.setDefaultPageFormat(pageformat);
    return report;
  }
}
