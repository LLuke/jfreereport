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
 */
package com.jrefinery.report;

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
  public static TextElement createDateElement (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String format,
                                               String field)
  {
    DateElement dateElement = new DateElement();
    dateElement.setName (name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.setFormatString(format);
    dateElement.setField(field);
    return dateElement;
  }

  public static TextElement createDateElement (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               DateFormat format,
                                               String field)
  {
    DateElement dateElement = new DateElement();
    dateElement.setName (name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.setFormatter(format);
    dateElement.setField(field);
    return dateElement;
  }

  public static TextElement createDateFunction (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String format,
                                               String function)
  {
    DateFunctionElement dateElement = new DateFunctionElement();
    dateElement.setName (name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.setFormatString(format);
    dateElement.setFunctionName(function);
    return dateElement;
  }

  public static TextElement createDateFunction (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               DateFormat format,
                                               String function)
  {
    DateFunctionElement dateElement = new DateFunctionElement();
    dateElement.setName (name);
    dateElement.setBounds(bounds);
    dateElement.setPaint(paint);
    dateElement.setAlignment(alignment);
    dateElement.setFont(font);
    dateElement.setNullString(nullString);
    dateElement.setFormatter(format);
    dateElement.setFunctionName(function);
    return dateElement;
  }

  public static TextElement createGeneralElement (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               int alignment,
                                               Font font,
                                               String nullString,
                                               String function)
  {
    GeneralElement gElement = new GeneralElement();
    gElement.setName (name);
    gElement.setBounds(bounds);
    gElement.setPaint(paint);
    gElement.setAlignment(alignment);
    gElement.setFont(font);
    gElement.setNullString(nullString);
    gElement.setField(function);
    return gElement;
  }

  public static ImageElement createImageElement (String name,
                                               Rectangle2D bounds,
                                               Paint paint,
                                               URL source)
    throws IOException
  {
    ImageReference reference = new ImageReference(source, bounds);
    ImageElement element = new ImageElement();
    element.setName(name);
    element.setPaint(paint);
    element.setImageReference(reference);
    return element;
  }

  public static TextElement createLabelElement (String name,
                                                Rectangle2D bounds,
                                                Paint paint,
                                                int alignment,
                                                Font font,
                                                String labeltext)
  {
    LabelElement label = new LabelElement ();
    label.setName (name);
    label.setBounds (bounds);
    label.setPaint (paint);
    label.setAlignment (alignment);
    label.setFont (font);
    label.setLabel (labeltext);
    return label;
  }

  public static LineShapeElement createLineShapeElement (String name,
                                                         Stroke stroke,
                                                         Line2D shape)
  {
    LineShapeElement line = new LineShapeElement();
    line.setName(name);
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
    MultilineTextElement text = new MultilineTextElement ();
    text.setName (name);
    text.setBounds (bounds);
    text.setPaint (paint);
    text.setAlignment (alignment);
    text.setFont (font);
    text.setNullString(nullstring);
    text.setField(field);
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
    NumberElement element = new NumberElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.setFormatter(format);
    element.setField(field);
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
    NumberElement element = new NumberElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.setDecimalFormatString(format);
    element.setField(field);
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
    NumberFunctionElement element = new NumberFunctionElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.setDecimalFormatString(format);
    element.setFunctionName(function);
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
    NumberFunctionElement element = new NumberFunctionElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.setFormatter(format);
    element.setFunctionName(function);
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
    StringElement element = new StringElement();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.setField(field);
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
    StringFunctionElement element = new StringFunctionElement ();
    element.setName (name);
    element.setBounds(bounds);
    element.setPaint(paint);
    element.setAlignment(alignment);
    element.setFont(font);
    element.setNullString(nullString);
    element.setFunctionName(function);
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
