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
 * -------------------
 * ElementFactory.java
 * -------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementFactory.java,v 1.13 2003/11/01 19:52:29 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 16-May-2002 : parseStroke added for line width
 * 22-May-2002 : Structured parsing functions: all tags have a startXXX and endXXX function
 * 30-Jun-2002 : Added support for ImageField, ImageFunction
 * 10-Jul-2002 : Added support for ImageURLField, ImageURLFunction
 * 31-Aug-2002 : Element-creation uses the ItemFactory, removed references to deprecated
 *               Element-types
 */
package org.jfree.report.modules.parser.simple;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.ElementAlignment;
import org.jfree.report.ShapeElement;
import org.jfree.report.elementfactory.DateFieldElementFactory;
import org.jfree.report.elementfactory.DrawableFieldElementFactory;
import org.jfree.report.elementfactory.ImageFieldElementFactory;
import org.jfree.report.elementfactory.ImageURLFieldElementFactory;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.NumberFieldElementFactory;
import org.jfree.report.elementfactory.ResourceFieldElementFactory;
import org.jfree.report.elementfactory.ResourceLabelElementFactory;
import org.jfree.report.elementfactory.ShapeFieldElementFactory;
import org.jfree.report.elementfactory.StaticImageURLElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.CharacterEntityParser;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.ui.FloatDimension;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The ElementFactory is responsible for creating ReportElements and is called by the
 * ReportDefinitionContentHandler. For details on the format of the parser have a look
 * at the DTD supplied in the distribution or on http://jfreereport.sourceforge.net/
 * <p>
 * This factory uses the deprecated element classes. These classes will get not extension
 * for new features and as soon as the discrepancy between implemented and possible features
 * gets too huge, this parser will be discontinued.
 *
 * @author Thomas Morgner
 */
public class ElementFactory extends AbstractReportDefinitionHandler implements ReportDefinitionTags
{
  /** A constant defining the name of the dynamic attribute. */
  public static final String DYNAMIC_ATT = "dynamic";
  /** A constant defining the name of the reserved-literal attribute. */
  public static final String RESERVED_LITERAL_ATT = "reserved-literal";
  /** A constant defining the name of the trim-text-content attribute. */
  public static final String TRIM_TEXT_CONTENT_ATT = "trim-text-content";

  /** Storage for the current CDATA. */
  private final StringBuffer currentText;

  /** The current band, where created elements are added to. */
  private final Band currentBand;

  /** A flag indicating whether this factory current works on an subband. */
  private boolean subbandActive;

  /** The current text element factory used to produce the next element. */
  private TextElementFactory textElementFactory;

  /** The character entity parser. */
  private final CharacterEntityParser entityParser;

  /**
   * Creates a new ElementFactory. The factory queries the current Band of the ReportFactory
   * and will add created element to this band. If unknown end-Tags are encountered, the
   * parsing for elements will stop and the previous handler will be activated.
   *
   * @param parser the used parser to coordinate the parsing process.
   * @param finishTag the finish tag, that should trigger the deactivation of this parser.
   * @param band the band that should be defined.
   *
   * @throws NullPointerException if the finishTag or the parser are null.
   */
  public ElementFactory(final ReportParser parser,
                        final String finishTag, final Band band)
  {
    super(parser, finishTag);
    this.currentBand = band;
    this.currentText = new StringBuffer();
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
  }

  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * StartTag-occurences of element definitions get handled by this factory. If an unknown
   * element is encountered, a SAXException is thrown.
   * <p>
   * The elements parsed in this factory denote base usecases. Element creation is
   * delegated to the element factories in the package org.jfree.repor.elementfactory
   *
   * @param qName  the element name.
   * @param atts  the element attributes.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
  public void startElement(final String qName, final Attributes atts)
      throws SAXException
  {
    if (textElementFactory != null)
    {
      throw new IllegalStateException(qName);
    }
    final String elementName = qName.toLowerCase().trim();

    // *** LABEL ***
    if (elementName.equals(LABEL_TAG))
    {
      startLabel(atts);
    }
    else if (elementName.equals(RESOURCELABEL_TAG))
    {
      startResourceLabel(atts);
    }
    else if (elementName.equals(RESOURCEFIELD_TAG))
    {
      startResourceField(atts);
    }
    else if (elementName.equals(DRAWABLE_FIELD_TAG))
    {
      startDrawableField(atts);
    }
    else if (elementName.equals(IMAGEREF_TAG))
    {
      startImageRef(atts);
    }
    else if (elementName.equals(IMAGEFIELD_TAG))
    {
      startImageField(atts);
    }
    else if (elementName.equals(IMAGEURLFIELD_TAG))
    {
      startImageURLField(atts);
    }
    else if (elementName.equals(LINE_TAG))
    {
      startLine(atts);
    }
    else if (elementName.equals(SHAPE_FIELD_TAG))
    {
      startShapeField(atts);
    }
    else if (elementName.equals(STRING_FIELD_TAG))
    {
      startStringField(atts);
    }
    else if (elementName.equals(NUMBER_FIELD_TAG))
    {
      startNumberField(atts);
    }
    else if (elementName.equals(DATE_FIELD_TAG))
    {
      startDateField(atts);
    }
    else if (elementName.equals(RECTANGLE_TAG))
    {
      startRectangle(atts);
    }
    else if (elementName.equals(BAND_TAG))
    {
      startBand(atts);
    }
  }

  /**
   * Returns the current band, which receives the parsed elements.
   *
   * @return the current band.
   */
  protected Band getCurrentBand()
  {
    return currentBand;
  }

  /**
   * Removes all text from the textbuffer at the end of a CDATA section.
   */
  private void clearCurrentText()
  {
    this.currentText.delete(0, currentText.length());
  }

  /**
   * Returns the current text of the textbuffer.
   *
   * @return the current text.
   */
  protected String getCurrentText()
  {
    return entityParser.decodeEntities(currentText.toString());
  }

  /**
   * Receives some (or all) of the text in the current element.
   *
   * @param ch  character buffer.
   * @param start  the start index.
   * @param length  the length of the valid character data.
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    if (this.currentText != null)
    {
      this.currentText.append(String.copyValueOf(ch, start, length));
    }
  }

  /**
   * SAX handler function that is forwarded from the ReportDefinitionContentHandler.
   * If an unknown element is encountered, the previous handler gets activated.
   *
   * @param qName  the element name.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
  public void endElement(final String qName)
      throws SAXException
  {
    final String elementName = qName.toLowerCase().trim();

    // *** LABEL ***
    if (elementName.equals(LABEL_TAG))
    {
      endLabel();
    }
    else if (elementName.equals(RESOURCELABEL_TAG))
    {
      endResourceLabel();
    }
    else if (elementName.equals(RESOURCEFIELD_TAG))
    {
      endResourceField();
    }
    else if (elementName.equals(DRAWABLE_FIELD_TAG) ||
        elementName.equals(IMAGEREF_TAG) ||
        elementName.equals(IMAGEFIELD_TAG) ||
        elementName.equals(IMAGEURLFIELD_TAG) ||
        elementName.equals(LINE_TAG) ||
        elementName.equals(SHAPE_FIELD_TAG) ||
        elementName.equals(RECTANGLE_TAG))
    {
      // do nothing ..
    }
    else if (elementName.equals(STRING_FIELD_TAG))
    {
      endStringField();
    }
    else if (elementName.equals(NUMBER_FIELD_TAG))
    {
      endNumberField();
    }
    else if (elementName.equals(DATE_FIELD_TAG))
    {
      endDateField();
    }
    else if (elementName.equals(BAND_TAG))
    {
      endBand();
    }
    else if (elementName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(qName);
      return;
    }
    else
    {
      throw new ParseException("Invalid tag: " + qName, getLocator());
    }
  }

  /**
   * Handles the start of a new sub-band.
   * @param attr the attribute set of the band-element.
   * @throws SAXException if an error occurs.
   */
  private void startBand(final Attributes attr) throws SAXException
  {
    // create the report header...
    final Band band = new Band();

    String name = attr.getValue(NAME_ATT);
    if (name != null)
    {
      band.setName(name);
    }
    band.getStyle().setStyleProperty
        (StaticLayoutManager.ABSOLUTE_POS, getElementPosition(attr));

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
    final float w = ParserUtil.parseRelativeFloat(widthValue, "Element width not specified");
    final float h = ParserUtil.parseRelativeFloat(heightValue, "Element height not specified");
    Dimension2D minSize = new FloatDimension(w, h);
    band.getStyle().setStyleProperty
        (ElementStyleSheet.MINIMUMSIZE, minSize);

    final FontFactory.FontInformation fi = FontFactory.createFont(attr);
    FontFactory.applyFontInformation(band.getBandDefaults(), fi);

    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      band.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      band.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    currentBand.addElement(band);
    getParser().pushFactory(new ElementFactory(getReportParser(), BAND_TAG, band));
    subbandActive = true;
  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefinitionContentHandler.
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startImageRef(final Attributes atts) throws SAXException
  {
    final String elementName = atts.getValue("name");
    final String elementSource = atts.getValue("src");
    final Boolean elementScale = parseBoolean(atts.getValue("scale"));
    final Boolean elementARatio = parseBoolean(atts.getValue("keepAspectRatio"));
    final Point2D absPos = getElementPosition(atts);
    final Dimension2D minSize = getElementDimension(atts);
    final Boolean elementDynamic = parseBoolean(atts.getValue("dynamic"));

    // Log.debug("Loading: " + getContentBase() + " " + elementSource + " as image");
    final StaticImageURLElementFactory factory = new StaticImageURLElementFactory();
    factory.setName(elementName);
    factory.setScale(elementScale);
    factory.setKeepAspectRatio(elementARatio);
    factory.setAbsolutePosition(absPos);
    factory.setMinimumSize(minSize);
    factory.setDynamicHeight(elementDynamic);
    factory.setBaseURL(getContentBase());
    factory.setContent(elementSource);
    getCurrentBand().addElement(factory.createElement());

  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startImageField(final Attributes atts) throws SAXException
  {
    final String elementName = atts.getValue(NAME_ATT);
    final String elementSource = atts.getValue(FIELDNAME_ATT);

    final Boolean elementScale = parseBoolean(atts.getValue("scale"));
    final Boolean elementARatio = parseBoolean(atts.getValue("keepAspectRatio"));
    final Point2D absPos = getElementPosition(atts);
    final Dimension2D minSize = getElementDimension(atts);
    final Boolean elementDynamic = parseBoolean(atts.getValue("dynamic"));

    final ImageFieldElementFactory factory = new ImageFieldElementFactory();
    factory.setName(elementName);
    factory.setScale(elementScale);
    factory.setKeepAspectRatio(elementARatio);
    factory.setFieldname(elementSource);
    factory.setAbsolutePosition(absPos);
    factory.setMinimumSize(minSize);
    factory.setDynamicHeight(elementDynamic);
    getCurrentBand().addElement(factory.createElement());

  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startImageURLField(final Attributes atts) throws SAXException
  {
    final String elementName = atts.getValue(NAME_ATT);
    final String elementSource = atts.getValue(FIELDNAME_ATT);

    final Boolean elementScale = parseBoolean(atts.getValue("scale"));
    final Boolean elementARatio = parseBoolean(atts.getValue("keepAspectRatio"));
    final Point2D absPos = getElementPosition(atts);
    final Dimension2D minSize = getElementDimension(atts);
    final Boolean elementDynamic = parseBoolean(atts.getValue("dynamic"));

    final ImageURLFieldElementFactory factory = new ImageURLFieldElementFactory();
    factory.setName(elementName);
    factory.setScale(elementScale);
    factory.setKeepAspectRatio(elementARatio);
    factory.setFieldname(elementSource);
    factory.setAbsolutePosition(absPos);
    factory.setMinimumSize(minSize);
    factory.setDynamicHeight(elementDynamic);
    getCurrentBand().addElement(factory.createElement());
  }

  /**
   * Starts a drawable field.
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX error.
   */
  private void startDrawableField(final Attributes atts) throws SAXException
  {
    final String elementName = atts.getValue(NAME_ATT);
    final String elementSource = atts.getValue(FIELDNAME_ATT);
    final Point2D absPos = getElementPosition(atts);
    final Dimension2D minSize = getElementDimension(atts);

    final DrawableFieldElementFactory factory = new DrawableFieldElementFactory();
    factory.setFieldname(elementSource);
    factory.setName(elementName);
    factory.setAbsolutePosition(absPos);
    factory.setMinimumSize(minSize);

    getCurrentBand().addElement(factory.createElement());

  }

  /**
   * Creates a LineShapeElement.
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startLine(final Attributes atts) throws SAXException
  {
    final String name = atts.getValue(NAME_ATT);
    final Color c = ParserUtil.parseColor(atts.getValue(COLOR_ATT));
    final float x1 = ParserUtil.parseFloat(atts.getValue("x1"), "Element x1 not specified");
    final float y1 = ParserUtil.parseFloat(atts.getValue("y1"), "Element y1 not specified");
    final float x2 = ParserUtil.parseFloat(atts.getValue("x2"), "Element x2 not specified");
    final float y2 = ParserUtil.parseFloat(atts.getValue("y2"), "Element y2 not specified");

    final Line2D line = new Line2D.Float(x1, y1, x2, y2);
    final ShapeElement element = StaticShapeElementFactory.createLineShapeElement(
        name,
        c,
        ParserUtil.parseStroke(atts.getValue("weight")),
        line);
    getCurrentBand().addElement(element);
  }

  /**
   * Creates a RectangleShapeElement.
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startRectangle(final Attributes atts) throws SAXException
  {
    final String name = atts.getValue(NAME_ATT);
    final Color c = ParserUtil.parseColor(atts.getValue(COLOR_ATT));
    final Rectangle2D rect = ParserUtil.getElementPosition(atts);
    final boolean shouldDraw = ParserUtil.parseBoolean(atts.getValue("draw"), false);
    final boolean shouldFill = ParserUtil.parseBoolean(atts.getValue("fill"), true);

    final ShapeElement element = StaticShapeElementFactory.createRectangleShapeElement(
        name,
        c,
        ParserUtil.parseStroke(atts.getValue("weight")),
        rect, shouldDraw, shouldFill);
    getCurrentBand().addElement(element);
  }

  /**
   * Parses the element position.
   *
   * @param atts the attribute set containing the "x" and "y" attributes.
   * @return the parsed element position, never null.
   * @throws SAXException if parsing the element position failed.
   */
  private Point2D getElementPosition(final Attributes atts) throws SAXException
  {
    final float x = ParserUtil.parseRelativeFloat(atts.getValue("x"),
        "Element x not specified");
    final float y = ParserUtil.parseRelativeFloat(atts.getValue("y"),
        "Element y not specified");
    return new Point2D.Float(x, y);
  }

  /**
   * Parses the element dimension.
   *
   * @param atts the attribute set containing the "width" and "height" attributes.
   * @return the parsed element dimensions, never null.
   * @throws SAXException if parsing the element dimensions failed.
   */
  private Dimension2D getElementDimension(final Attributes atts) throws SAXException
  {
    final float w = ParserUtil.parseRelativeFloat(atts.getValue("width"),
        "Element width not specified");
    final float h = ParserUtil.parseRelativeFloat(atts.getValue("height"),
        "Element height not specified");
    return new FloatDimension(w, h);
  }

  /**
   * Parses a simple font style for text elements. These styles contain "bold", "italic"
   * and "bold-italic". The style constants are included for compatibility with older
   * releases and should no longer be used. Use the boolean flags instead.
   *
   * @param fontStyle the font style string.
   * @param target the text element factory that should receive the parsed values.
   */
  private void parseSimpleFontStyle
      (final String fontStyle, final TextElementFactory target)
  {
    if (fontStyle != null)
    {
      if (fontStyle.equals("bold"))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.FALSE);
      }
      else if (fontStyle.equals("italic"))
      {
        target.setBold(Boolean.FALSE);
        target.setItalic(Boolean.TRUE);
      }
      else if (fontStyle.equals("bold-italic"))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.TRUE);
      }
      else if (fontStyle.equals("plain"))
      {
        target.setBold(Boolean.FALSE);
        target.setItalic(Boolean.FALSE);
      }
    }
  }

  /**
   * Translates an boolean string ("true" or "false") into the corresponding
   * Boolean object.
   *
   * @param value the string that represents the boolean.
   * @return Boolean.TRUE or Boolean.FALSE
   * @throws SAXException if an parse error occured.
   */
  private Boolean parseBoolean(final String value) throws SAXException
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
   * Creates a RectangleShapeElement.
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startShapeField(final Attributes atts) throws SAXException
  {
    final String name = atts.getValue(NAME_ATT);
    final String elementSource = atts.getValue(FIELDNAME_ATT);
    final Color c = ParserUtil.parseColor(atts.getValue(COLOR_ATT));
    final Point2D absPos = getElementPosition(atts);
    final Dimension2D minSize = getElementDimension(atts);
    final Boolean shouldDraw = parseBoolean(atts.getValue("draw"));
    final Boolean shouldFill = parseBoolean(atts.getValue("fill"));
    final Boolean scale = parseBoolean(atts.getValue("keepAspectRatio"));
    final Boolean kar = parseBoolean(atts.getValue("scale"));
    final Boolean dynamic = parseBoolean(atts.getValue("dynamic"));
    final Stroke stroke = ParserUtil.parseStroke(atts.getValue("weight"));

    final ShapeFieldElementFactory factory = new ShapeFieldElementFactory();
    factory.setName(name);
    factory.setAbsolutePosition(absPos);
    factory.setMinimumSize(minSize);
    factory.setColor(c);
    factory.setFieldname(elementSource);
    factory.setShouldDraw(shouldDraw);
    factory.setShouldFill(shouldFill);
    factory.setScale(scale);
    factory.setKeepAspectRatio(kar);
    factory.setStroke(stroke);
    factory.setDynamicHeight(dynamic);
    getCurrentBand().addElement(factory.createElement());
  }

  /**
   * Creates a label element, an text element with an static datasource attached.
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startLabel(final Attributes atts) throws SAXException
  {
    textElementFactory = new LabelElementFactory();
    getTextElementAttributes(atts, textElementFactory);
    clearCurrentText();
  }

  /**
   * Creates a text element. In ancient times there was a difference between string elements
   * (single line) and multiline fields. This is resolved in the text element class which
   * handles all cases of printing text in reports.
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startStringField(final Attributes atts) throws SAXException
  {
    final TextFieldElementFactory factory = new TextFieldElementFactory();
    getTextFieldElementAttributes(atts, factory);
    textElementFactory = factory;
  }

  /**
   * Creates a number element (a text element that displays a numerical value).
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startNumberField(final Attributes atts) throws SAXException
  {
    final NumberFieldElementFactory factory = new NumberFieldElementFactory();
    factory.setFormatString(atts.getValue(FORMAT_ATT));
    getTextFieldElementAttributes(atts, factory);
    textElementFactory = factory;
  }

  /**
   * Creates a date element (a text element that displays a date value).
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startDateField(final Attributes atts) throws SAXException
  {
    final DateFieldElementFactory factory = new DateFieldElementFactory();
    factory.setFormatString(atts.getValue(FORMAT_ATT));
    getTextFieldElementAttributes(atts, factory);
    textElementFactory = factory;
  }

  /**
   * Ends a label tag, sets the static text for the label which was build during the
   * parsing. The label is added to the current band.
   */
  private void endLabel()
  {
    final LabelElementFactory factory = (LabelElementFactory) textElementFactory;
    factory.setText(getCurrentText());
    clearCurrentText();
    getCurrentBand().addElement(factory.createElement());
    textElementFactory = null;
  }

  /**
   * Creates a resource label element, an text element with an static datasource attached.
   *
   * @param attrs  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startResourceLabel(final Attributes attrs)
      throws SAXException
  {
    final ResourceLabelElementFactory factory = new ResourceLabelElementFactory();
    textElementFactory = factory;
    getTextElementAttributes(attrs, factory);

    factory.setNullString(attrs.getValue(NULLSTRING_ATT));
    String resourceBase = attrs.getValue(RESOURCEBASE_ATTR);
    if (resourceBase == null)
    {
      final ReportConfiguration config = getReport().getReportConfiguration();
      resourceBase = config.getConfigProperty(ReportConfiguration.REPORT_RESOURCE_BUNDLE_KEY);
      if (resourceBase == null)
      {
        throw new SAXException("Resourcebase is not defined for this report. " +
            "Use the configuration key 'org.jfree.report.ResourceBundle' to define it.");
      }
    }
    factory.setResourceBase(resourceBase);
    clearCurrentText();
  }

  /**
   * Creates a resource field element.
   *
   * @param attrs  the attributes.
   *
   * @throws SAXException if there is a SAX problem.
   */
  private void startResourceField(final Attributes attrs)
      throws SAXException
  {
    final ResourceFieldElementFactory factory = new ResourceFieldElementFactory();
    getTextFieldElementAttributes(attrs, factory);

    String resourceBase = attrs.getValue(RESOURCEBASE_ATTR);
    if (resourceBase == null)
    {
      final ReportConfiguration config = getReport().getReportConfiguration();
      resourceBase = config.getConfigProperty(ReportConfiguration.REPORT_RESOURCE_BUNDLE_KEY);
      if (resourceBase == null)
      {
        throw new SAXException("Resourcebase is not defined for this report. " +
            "Use the configuration key 'org.jfree.report.ResourceBundle' to define it.");
      }
    }
    factory.setResourceBase(resourceBase);
    clearCurrentText();
  }

  /**
   * Ends a resource label tag, sets the static key for the resource label,
   * which was build during the parsing. The label is added to the current band.
   */
  private void endResourceLabel()
  {
    final ResourceLabelElementFactory factory = (ResourceLabelElementFactory) textElementFactory;
    factory.setResourceKey(getCurrentText());
    getCurrentBand().addElement(factory.createElement());
    textElementFactory = null;
  }

  /**
   * Ends the resource field and adds it to the current band.
   */
  private void endResourceField()
  {
    getCurrentBand().addElement(textElementFactory.createElement());
    textElementFactory = null;
  }


  /**
   * Ends the String field and adds it to the current band.
   */
  private void endStringField()
  {
    getCurrentBand().addElement(textElementFactory.createElement());
    textElementFactory = null;
  }

  /**
   * Ends the number field and adds it to the current band.
   */
  private void endNumberField()
  {
    getCurrentBand().addElement(textElementFactory.createElement());
    textElementFactory = null;
  }

  /**
   * Ends the date field and adds it to the current band.
   */
  private void endDateField()
  {
    getCurrentBand().addElement(textElementFactory.createElement());
    textElementFactory = null;
  }

  /**
   * Reads the attributes that are base for all text-elements, as
   * name, x, y, width, height, font, fontstyle, fontsize and alignment.
   *
   * @param atts  the attributes.
   * @param factory the text element factory that should produce the text element.
   * @throws SAXException if there is a SAX problem.
   */
  private void getTextElementAttributes
      (final Attributes atts, final TextElementFactory factory) throws SAXException
  {
    factory.setName(atts.getValue(NAME_ATT));
    factory.setAbsolutePosition(getElementPosition(atts));
    factory.setMinimumSize(getElementDimension(atts));
    if (atts.getValue(COLOR_ATT) != null)
    {
      factory.setColor(ParserUtil.parseColor(atts.getValue(COLOR_ATT)));
    }
    factory.setDynamicHeight(parseBoolean(atts.getValue(DYNAMIC_ATT)));
    factory.setHorizontalAlignment(parseTextAlignment(atts.getValue(ALIGNMENT_ATT)));
    factory.setVerticalAlignment(parseTextVerticalAlignment(atts.getValue(VALIGNMENT_ATT)));
    factory.setBold(parseBoolean(atts.getValue(FS_BOLD)));
    factory.setEmbedFont(parseBoolean(atts.getValue(FS_EMBEDDED)));
    factory.setEncoding(atts.getValue(FS_ENCODING));
    factory.setFontName(atts.getValue(FONT_NAME_ATT));
    factory.setFontSize(parseInteger(atts.getValue(FONT_SIZE_ATT)));
    factory.setItalic(parseBoolean(atts.getValue(FS_ITALIC)));
    factory.setLineHeight(parseFloat(atts.getValue(LINEHEIGHT)));
    factory.setStrikethrough(parseBoolean(atts.getValue(FS_STRIKETHR)));
    factory.setUnderline(parseBoolean(atts.getValue(FS_UNDERLINE)));
    factory.setReservedLiteral(atts.getValue(RESERVED_LITERAL_ATT));
    factory.setTrimTextContent(parseBoolean(atts.getValue(TRIM_TEXT_CONTENT_ATT)));
    parseSimpleFontStyle(atts.getValue(FONT_STYLE_ATT), factory);


  }

  /**
   * Reads an attribute as float and returns <code>def</code> if that fails.
   *
   * @param value the attribute value.
   * @return the float value.
   * @throws SAXException if an parse error occured.
   */
  private Float parseFloat(String value) throws SAXException
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

  /**
   * Reads the attributes that are base for all text-field elements, as
   * name, x, y, width, height, font, fontstyle, fontsize and alignment.
   *
   * @param atts  the attributes.
   * @param factory the text element factory that should produce the text element.
   * @throws SAXException if there is a SAX problem.
   */
  private void getTextFieldElementAttributes
      (final Attributes atts, final TextFieldElementFactory factory) throws SAXException
  {
    getTextElementAttributes(atts, factory);
    factory.setNullString(atts.getValue(NULLSTRING_ATT));
    final String textElementSourceName = atts.getValue(FIELDNAME_ATT);
    if (textElementSourceName == null)
    {
      throw new ParseException("The fieldname-attribute is required.", getLocator());
    }
    factory.setFieldname(textElementSourceName);

  }

  /**
   * Reads an attribute as int and returns <code>def</code> if that fails.
   *
   * @param val the attribute value.
   *
   * @return the int value.
   * @throws SAXException if an parse error occured.
   */
  private Integer parseInteger(final String val) throws SAXException
  {
    if (val == null)
    {
      return null;
    }
    try
    {
      return new Integer(val);
    }
    catch (NumberFormatException e)
    {
      throw new SAXException("Failed to parse value", e);
    }
  }

  /**
   * Parses the text looking for a text alignment, which is one of "left", "center" or "right".
   * <p>
   * The method returns one of the values:  Element.LEFT, Element.CENTER and Element.RIGHT.
   *
   * @param alignment  the alignment.
   * @return an alignment code.
   */
  private ElementAlignment parseTextAlignment(final String alignment)
  {
    ElementAlignment elementAlignment = null;
    if (alignment != null)
    {
      if (alignment.equals("left"))
      {
        elementAlignment = ElementAlignment.LEFT;
      }
      if (alignment.equals("center"))
      {
        elementAlignment = ElementAlignment.CENTER;
      }
      if (alignment.equals("right"))
      {
        elementAlignment = ElementAlignment.RIGHT;
      }
    }
    return elementAlignment;
  }

  /**
   * Parses the text looking for a text alignment, which is one of "top", "middle"/"center"
   * or "bottom".
   * <p>
   * The method returns one of the values:  Element.TOP, Element.BOTTOM and Element.MIDDLE.
   *
   * @param alignment  the alignment.
   * @return an alignment code.
   */
  private ElementAlignment parseTextVerticalAlignment
      (final String alignment)
  {
    ElementAlignment elementAlignment = null;
    if (alignment != null)
    {
      if (alignment.equals("top"))
      {
        elementAlignment = ElementAlignment.TOP;
      }
      if ((alignment.equals("center")) || (alignment.equals("middle")))
      {
        elementAlignment = ElementAlignment.MIDDLE;
      }
      if (alignment.equals("bottom"))
      {
        elementAlignment = ElementAlignment.BOTTOM;
      }
    }
    return elementAlignment;
  }

  /**
   * Handles the end of a band definition.
   *
   * @see org.jfree.report.ReportFooter
   *
   * @throws SAXException if a parser error occurs.
   */
  private void endBand() throws SAXException
  {
    if (subbandActive == false)
    {
      getParser().popFactory().endElement(BAND_TAG);
    }
    else
    {
      subbandActive = false;
    }
  }
}
