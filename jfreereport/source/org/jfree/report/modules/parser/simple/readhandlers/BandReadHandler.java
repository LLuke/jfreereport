package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.util.ArrayList;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.simple.FontFactory;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class BandReadHandler extends AbstractXmlReadHandler
{
  /** Literal text for an XML report element. */
  public static final String LABEL_TAG = "label";

  /** Literal text for an XML report element. */
  public static final String STRING_FIELD_TAG = "string-field";

  /** Literal text for an XML report element. */
  public static final String NUMBER_FIELD_TAG = "number-field";

  /** Literal text for an XML report element. */
  public static final String DATE_FIELD_TAG = "date-field";

  /** Literal text for an XML report element. */
  public static final String IMAGEREF_TAG = "imageref";

  /** Literal text for an XML report element. */
  public static final String IMAGEFIELD_TAG = "image-field";

  /** Literal text for an XML report element. */
  public static final String IMAGEURLFIELD_TAG = "imageurl-field";

  /** Literal text for an XML report element. */
  public static final String RECTANGLE_TAG = "rectangle";

  /** Literal text for an XML report element. */
  public static final String RESOURCELABEL_TAG = "resource-label";

  /** Literal text for an XML report element. */
  public static final String RESOURCEFIELD_TAG = "resource-field";

  /** Literal text for an XML report element. */
  public static final String LINE_TAG = "line";

  /** Literal text for an XML report element. */
  public static final String DRAWABLE_FIELD_TAG = "drawable-field";

  /** Literal text for an XML report element. */
  public static final String SHAPE_FIELD_TAG = "shape-field";

  /** Literal text for an XML report element. */
  public static final String BAND_TAG = "band";

  /** Literal text for an XML report element. */
  public static final String MESSAGE_FIELD_TAG = "message-field";

  /** Literal text for an XML report element. */
  public static final String ANCHOR_FIELD_TAG = "anchor-field";


  /** Literal text for an XML attribute. */
  public static final String ALIGNMENT_ATT = "alignment";

  /** Literal text for an XML attribute. */
  public static final String VALIGNMENT_ATT = "vertical-alignment";


  private static final String NAME_ATT= "name";
  private static final String COLOR_ATT = "color";
  public static final String RESERVED_LITERAL_ATT = "reserved-literal";

  private Band band;
  private ArrayList elementHandlers;
  private static final String DRAWABLE_REF_TAG = "drawableref";
  private static final String DRAWABLE_URL_FIELD_TAG = "drawable-url-field";

  public BandReadHandler ()
  {
    this (new Band());
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
  protected void startParsing (final Attributes attr)
          throws SAXException
  {
    final String name = attr.getValue(NAME_ATT);
    if (name != null)
    {
      band.setName(name);
    }


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
  }

  private void handleVisible (final Attributes attr)
  {
    final String trimTextContent = attr.getValue("visible");
    if (trimTextContent != null)
    {
      getBand().getStyle().setStyleProperty
              (ElementStyleSheet.VISIBLE,
                      new Boolean(ParserUtil.parseBoolean(trimTextContent, true)));
    }
  }

  private void handleLayoutCachable (final Attributes attr)
  {
    final String trimTextContent = attr.getValue("layout-cachable");
    if (trimTextContent != null)
    {
      getBand().getStyle().setStyleProperty
              (ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE,
                      new Boolean(ParserUtil.parseBoolean(trimTextContent, true)));
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

  private void handleTrimTextContent (final Attributes attr)
  {
    final String trimTextContent = attr.getValue("trim-text-content");
    if (trimTextContent != null)
    {
      getBand().getStyle().setStyleProperty
              (ElementStyleSheet.TRIM_TEXT_CONTENT,
                      new Boolean(ParserUtil.parseBoolean(trimTextContent, true)));
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
  private void handlePosition(final Attributes atts) throws SAXException
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
                                               final Attributes atts)
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
}
