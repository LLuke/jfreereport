/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * BandReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BandReadHandler.java,v 1.10 2006/01/12 20:54:54 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.JFreeReport;
import org.jfree.report.layout.BandLayoutManager;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.simple.FontFactory;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.Log;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class BandReadHandler extends AbstractPropertyXmlReadHandler
{
  /**
   * Literal text for an XML report element.
   */
  public static final String LABEL_TAG = "label";

  /**
   * Literal text for an XML report element.
   */
  public static final String STRING_FIELD_TAG = "string-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String NUMBER_FIELD_TAG = "number-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String DATE_FIELD_TAG = "date-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String IMAGEREF_TAG = "imageref";

  /**
   * Literal text for an XML report element.
   */
  public static final String IMAGEFIELD_TAG = "image-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String IMAGEURLFIELD_TAG = "imageurl-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String RECTANGLE_TAG = "rectangle";

  /**
   * Literal text for an XML report element.
   */
  public static final String RESOURCELABEL_TAG = "resource-label";

  /**
   * Literal text for an XML report element.
   */
  public static final String RESOURCEFIELD_TAG = "resource-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String RESOURCEMESSAGE_TAG = "resource-message";

  /**
   * Literal text for an XML report element.
   */
  public static final String COMPONENTFIELD_TAG = "component-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String LINE_TAG = "line";

  /**
   * Literal text for an XML report element.
   */
  public static final String DRAWABLE_FIELD_TAG = "drawable-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String SHAPE_FIELD_TAG = "shape-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String BAND_TAG = "band";

  /**
   * Literal text for an XML report element.
   */
  public static final String MESSAGE_FIELD_TAG = "message-field";

  /**
   * Literal text for an XML report element.
   */
  public static final String ANCHOR_FIELD_TAG = "anchor-field";


  /**
   * Literal text for an XML attribute.
   */
  public static final String ALIGNMENT_ATT = "alignment";

  /**
   * Literal text for an XML attribute.
   */
  public static final String VALIGNMENT_ATT = "vertical-alignment";


  /** Literal text for an XML attribute value. */
  private static final String LAYOUT_ATT = "layout";
  /** Literal text for an XML attribute value. */
  private static final String NAME_ATT = "name";
  /** Literal text for an XML attribute value. */
  private static final String COLOR_ATT = "color";
  /** Literal text for an XML attribute value. */
  private static final String RESERVED_LITERAL_ATT = "reserved-literal";
  /** Literal text for an XML attribute value. */
  private static final String DRAWABLE_REF_TAG = "drawableref";
  /** Literal text for an XML attribute value. */
  private static final String DRAWABLE_URL_FIELD_TAG = "drawable-url-field";
  /** Literal text for an XML attribute value. */
  private static final String STYLE_CLASS_ATT = "styleClass";
  /** Literal text for an XML attribute value. */
  private static final String LAYOUT_CACHABLE_ATT = "layout-cachable";
  /** Literal text for an XML attribute value. */
  private static final String VISIBLE_ATT = "visible";
  /** Literal text for an XML attribute value. */
  private static final String TRIM_TEXT_CONTENT_ATT = "trim-text-content";
  /** Literal text for an XML attribute value. */
  private static final String HREF_ATT = "href";
  /** Literal text for an XML attribute value. */
  private static final String HREF_WINDOW_ATT = "href-window";
  /** Literal text for an XML attribute value. */
  public static final String LINEHEIGHT = "line-height";
  public static final String WRAP_TEXT = "excel-wrap-text";

  private Band band;
  private ArrayList elementHandlers;

  public BandReadHandler ()
  {
    this(new Band());
  }

  protected BandReadHandler (final Band band)
  {
    this.band = band;
    this.elementHandlers = new ArrayList();
  }

  protected Band getBand ()
  {
    return band;
  }

  /**
   * Starts parsing.
   *
   * @param attr the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attr)
          throws SAXException
  {
    final String name = attr.getValue(NAME_ATT);
    if (name != null)
    {
      band.setName(name);
    }

    handleStyleClass(attr);

    final FontFactory.FontInformation fi = FontFactory.createFont(attr);
    FontFactory.applyFontInformation(band.getStyle(), fi);

    handleColor(attr);
    handleMinSize(attr);
    handleAlignment(attr);
    handlePosition(attr);
    handleReservedLiteral(attr);
    handleTrimTextContent(attr);
    handleVisible(attr);
    handleLayoutCachable(attr);
    handleLayout(attr);
    handleHRef(attr);

    band.getStyle().setStyleProperty
            (ElementStyleSheet.LINEHEIGHT, parseFloat(attr.getValue(LINEHEIGHT)));
    band.getStyle().setStyleProperty
            (ElementStyleSheet.EXCEL_WRAP_TEXT, parseBoolean(attr.getValue(WRAP_TEXT)));

  }

  /**
   * Translates an boolean string ("true" or "false") into the corresponding Boolean
   * object.
   *
   * @param value the string that represents the boolean.
   * @return Boolean.TRUE or Boolean.FALSE
   *
   * @throws SAXException if an parse error occured.
   */
  private Boolean parseBoolean (final String value)
          throws SAXException
  {
    if (value == null)
    {
      return null;
    }
    if (value.equals("true"))
    {
      return Boolean.TRUE;
    }
    else if (value.equals("false"))
    {
      return Boolean.FALSE;
    }
    throw new SAXException("Failed to parse value: Expected 'true' or 'false'");
  }

  /**
   * Reads an attribute as float and returns <code>def</code> if that fails.
   *
   * @param value the attribute value.
   * @return the float value.
   *
   * @throws SAXException if an parse error occured.
   */
  private Float parseFloat (final String value)
          throws SAXException
  {
    if (value == null)
    {
      return null;
    }
    try
    {
      return new Float(value);
    }
    catch (Exception ex)
    {
      throw new SAXException("Failed to parse value", ex);
    }
  }

  private void handleHRef(final PropertyAttributes attr) {
    final String href = attr.getValue(HREF_ATT);
    if (href != null)
    {
      getBand().getStyle().setStyleProperty(ElementStyleSheet.HREF_TARGET, href);
    }

    final String hrefTarget = attr.getValue(HREF_WINDOW_ATT);
    if (hrefTarget != null)
    {
      getBand().getStyle().setStyleProperty(ElementStyleSheet.HREF_WINDOW, hrefTarget);
    }

  }

  private void handleStyleClass(final PropertyAttributes attr) {
    final String styleClass = attr.getValue(STYLE_CLASS_ATT);
    if (styleClass != null)
    {
      final JFreeReport report = (JFreeReport) getRootHandler().getHelperObject
              (ReportParser.HELPER_OBJ_REPORT_NAME);
      final ElementStyleSheet existingStyleSheet =
              report.getStyleSheetCollection().getStyleSheet(styleClass);
      if (existingStyleSheet != null)
      {
        getBand().getStyle().addParent(existingStyleSheet);
      }
      else
      {
        Log.warn ("The specified stylesheet '" + styleClass + "' is not defined - creating a new instance.");
        getBand().getStyle().addParent
                (report.getStyleSheetCollection().createStyleSheet(styleClass));
      }
    }
  }

  private void handleVisible (final Attributes attr)
  {
    final String trimTextContent = attr.getValue(VISIBLE_ATT);
    if (trimTextContent != null)
    {
      final Boolean value =
              ParserUtil.parseBoolean(trimTextContent, true) ?
                      Boolean.TRUE : Boolean.FALSE;
      getBand().getStyle().setStyleProperty
              (ElementStyleSheet.VISIBLE, value);
    }
  }

  private void handleLayoutCachable (final Attributes attr)
  {
    final String trimTextContent = attr.getValue(LAYOUT_CACHABLE_ATT);
    if (trimTextContent != null)
    {
      final Boolean value =
              (ParserUtil.parseBoolean(trimTextContent, true)) ?
                      Boolean.TRUE : Boolean.FALSE;
      getBand().getStyle().setStyleProperty
              (ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE, value);
    }
  }

  private void handleReservedLiteral (final Attributes attr)
  {
    final String reservedLiteral = attr.getValue(RESERVED_LITERAL_ATT);
    if (reservedLiteral != null)
    {
      getBand().getStyle().setStyleProperty
              (ElementStyleSheet.RESERVED_LITERAL, reservedLiteral);
    }
  }

  private void handleLayout (final Attributes attr)
          throws SAXException
  {
    final String layoutManagerName = attr.getValue(LAYOUT_ATT);
    if (layoutManagerName != null)
    {
      final Object o = ObjectUtilities.loadAndInstantiate(layoutManagerName, BandReadHandler.class);
      if (o instanceof BandLayoutManager == false)
      {
        throw new SAXException("Expected layout manager as attribute value");
      }
      getBand().getStyle().setStyleProperty
              (BandLayoutManager.LAYOUTMANAGER, o);
    }
  }

  private void handleTrimTextContent (final Attributes attr)
  {
    final String trimTextContent = attr.getValue(TRIM_TEXT_CONTENT_ATT);
    if (trimTextContent != null)
    {
      final Boolean value =
              (ParserUtil.parseBoolean(trimTextContent, true)) ?
                      Boolean.TRUE : Boolean.FALSE;
      getBand().getStyle().setStyleProperty
              (ElementStyleSheet.TRIM_TEXT_CONTENT, value);
    }
  }

  private void handleColor (final Attributes attr)
  {
    final String colorValue = attr.getValue(COLOR_ATT);
    if (colorValue != null)
    {
      final Color c = ParserUtil.parseColor(colorValue);
      getBand().getStyle().setStyleProperty(ElementStyleSheet.PAINT, c);
    }
  }

  protected void handleMinSize (final Attributes attr)
          throws SAXException
  {
    String widthValue = attr.getValue("width");
    if (widthValue == null)
    {
      widthValue = "0";
    }
    String heightValue = attr.getValue("height");
    if (heightValue == null)
    {
      heightValue = "0";
    }
    final float w = ParserUtil.parseRelativeFloat(widthValue, "Element width is invalid");
    final float h = ParserUtil.parseRelativeFloat(heightValue, "Element height is invalid");
    final Dimension2D minSize = new FloatDimension(w, h);
    getBand().getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, minSize);
  }

  protected void handleAlignment (final Attributes attr)
          throws SAXException
  {
    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      getBand().getStyle().setStyleProperty(ElementStyleSheet.VALIGNMENT,
              ReportParserUtil.parseVerticalElementAlignment(valign));
    }

    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      getBand().getStyle().setStyleProperty(ElementStyleSheet.ALIGNMENT,
              ReportParserUtil.parseHorizontalElementAlignment(halign));
    }
  }


  /**
   * Parses the element position.
   *
   * @param atts the attribute set containing the "x" and "y" attributes.
   * @throws SAXException if parsing the element position failed.
   */
  private void handlePosition (final Attributes atts)
          throws SAXException
  {
    String xValue = atts.getValue("x");
    if (xValue == null)
    {
      xValue = "0";
    }
    String yValue = atts.getValue("y");
    if (yValue == null)
    {
      yValue = "0";
    }
    final float x = ParserUtil.parseRelativeFloat(xValue, "Element x not valid");
    final float y = ParserUtil.parseRelativeFloat(yValue, "Element y not valid");
    getBand().getStyle().setStyleProperty
            (StaticLayoutManager.ABSOLUTE_POS, new Point2D.Float(x, y));
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild (final String tagName,
                                               final PropertyAttributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals(LABEL_TAG))
    {
      final XmlReadHandler readHandler = new LabelReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(STRING_FIELD_TAG))
    {
      final XmlReadHandler readHandler = new StringFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(NUMBER_FIELD_TAG))
    {
      final XmlReadHandler readHandler = new NumberFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(DATE_FIELD_TAG))
    {
      final XmlReadHandler readHandler = new DateFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(IMAGEREF_TAG))
    {
      final XmlReadHandler readHandler = new ImageRefReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(IMAGEFIELD_TAG))
    {
      final XmlReadHandler readHandler = new ImageFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(IMAGEURLFIELD_TAG))
    {
      final XmlReadHandler readHandler = new ImageURLFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(RECTANGLE_TAG))
    {
      final XmlReadHandler readHandler = new RectangleReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(RESOURCEFIELD_TAG))
    {
      final XmlReadHandler readHandler = new ResourceFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(RESOURCELABEL_TAG))
    {
      final XmlReadHandler readHandler = new ResourceLabelReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(RESOURCEMESSAGE_TAG))
    {
      final XmlReadHandler readHandler = new ResourceMessageReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(LINE_TAG))
    {
      final XmlReadHandler readHandler = new LineReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(MESSAGE_FIELD_TAG))
    {
      final XmlReadHandler readHandler = new MessageFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(ANCHOR_FIELD_TAG))
    {
      final XmlReadHandler readHandler = new AnchorFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(COMPONENTFIELD_TAG))
    {
      final XmlReadHandler readHandler = new ComponentFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(DRAWABLE_FIELD_TAG))
    {
      final XmlReadHandler readHandler = new DrawableFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(DRAWABLE_URL_FIELD_TAG))
    {
      final XmlReadHandler readHandler = new DrawableURLFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(DRAWABLE_REF_TAG))
    {
      final XmlReadHandler readHandler = new DrawableRefReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(SHAPE_FIELD_TAG))
    {
      final XmlReadHandler readHandler = new ShapeFieldReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals(BAND_TAG))
    {
      final XmlReadHandler readHandler = new BandReadHandler();
      elementHandlers.add(readHandler);
      return readHandler;
    }
    return null;
  }


  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    for (int i = 0; i < elementHandlers.size(); i++)
    {
      final XmlReadHandler readHandler = (XmlReadHandler) elementHandlers.get(i);
      final Element e = (Element) readHandler.getObject();
      band.addElement(e);
    }
    super.doneParsing();
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return band;
  }

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath commentHintPath = new CommentHintPath(band);
    defaultStoreComments(commentHintPath);
  }
}
