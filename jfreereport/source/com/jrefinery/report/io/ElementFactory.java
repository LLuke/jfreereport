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
 * ElementFactory.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 10-May-2002 : Initial version
 * 16-May-2002 : parseStroke added for line width
 * 22-May-2002 : Structured parsing functions: all tags have a startXXX and endXXX function
 * 30-Jun-2002 : Added support for ImageField, ImageFunction
 * 10-Jul-2002 : Added support for ImageURLField, ImageURLFunction
 * 31-Aug-2002 : Element-creation uses the ItemFactory, removed references to deprecated Element-types
 */
package com.jrefinery.report.io;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.ImageElement;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.LineShapeElement;
import com.jrefinery.report.RectangleShapeElement;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;

/**
 * The ElementFactory is responsible for creating ReportElements and is called by the
 * ReportDefinitionContentHandler. For details on the format of the parser have a look
 * at the DTD supplied in the distribution or on http://jfreereport.sourceforge.net/
 * <p>
 * This factory uses the deprecated element classes. These classes will get not extension
 * for new features and as soon as the discrepancy beween implemented and possible features
 * gets too huge, this parser will be discontinued.
 */
public class ElementFactory
    extends DefaultHandler
    implements ReportDefinitionTags
{
  /** Storage for the current CDATA */
  private StringBuffer currentText;

  /** The current band, where created elements are added to */
  private Band currentBand;

  /** the fontfactory used to fill TextElements font definitions */
  private FontFactory fontFactory;

  /** The base handler */
  private ReportDefinitionContentHandler handler;

  private String textElementName;
  private Rectangle2D textElementBounds;
  private Font textElementFont;
  private int textElementAlignment;
  private Paint textElementColor;
  private String textElementNullString;
  private String textElementSourceName;
  private String textElementFormatString;

  /**
   * Creates a new ElementFactory. The factory queries the current Band of the ReportFactory
   * and will add created element to this band. If unknown end-Tags are encountered, the
   * parsing for elements will stop and the previous handler will be activated.
   */
  public ElementFactory(ReportFactory base)
  {
    currentBand = base.getCurrentBand();
    this.handler = base.getHandler();
    this.currentText = new StringBuffer();
    fontFactory = handler.getFontFactory();
  }

  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * StartTag-occurences of element definitions get handled by this factory. If an unknown
   * element is encountered, a SAXException is thrown.
   * <p>
   * The elements parsed in this factory denote common usecases. Element creation is
   * delegated to the ItemFactory
   *
   * @throws SAXException if an unknown tag is encountered.
   * @see com.jrefinery.report.ItemFactory
   */
  public void startElement(
      String namespaceURI,
      String localName,
      String qName,
      Attributes atts)
      throws SAXException
  {
    String elementName = qName.toLowerCase().trim();

    // *** LABEL ***
    if (elementName.equals(LABEL_TAG))
    {
      startLabel(atts);
    }
    else if (elementName.equals(IMAGEREF_TAG))
    {
      startImageRef(atts);
    }
    else if (elementName.equals(IMAGEFIELD_TAG))
    {
      startImageField(atts);
    }
    else if (elementName.equals(IMAGEFUNCTION_TAG))
    {
      startImageFunction(atts);
    }
    else if (elementName.equals(IMAGEURLFIELD_TAG))
    {
      startImageURLField(atts);
    }
    else if (elementName.equals(IMAGEURLFUNCTION_TAG))
    {
      startImageURLFunction(atts);
    }
    else if (elementName.equals(LINE_TAG))
    {
      startLine(atts);
    }
    else if (elementName.equals(GENERAL_FIELD_TAG))
    {
      startGeneralField(atts);
    }
    else if (elementName.equals(STRING_FIELD_TAG))
    {
      startStringField(atts);
    }
    else if (elementName.equals(MULTILINE_FIELD_TAG))
    {
      startMultilineField(atts);
    }
    else if (elementName.equals(NUMBER_FIELD_TAG))
    {
      startNumberField(atts);
    }
    else if (elementName.equals(DATE_FIELD_TAG))
    {
      startDateField(atts);
    }
    else if (elementName.equals(STRING_FUNCTION_TAG))
    {
      startStringFunction(atts);
    }
    else if (elementName.equals(NUMBER_FUNCTION_TAG))
    {
      startNumberFunction(atts);
    }
    else if (elementName.equals(DATE_FUNCTION_TAG))
    {
      startDateFunction(atts);
    }
    else if (elementName.equals(RECTANGLE_TAG))
    {
      startRectangle(atts);
    }
  }

  /** returns the current band, which receives the parsed elements */
  protected Band getCurrentBand()
  {
    return currentBand;
  }

  /** removes all text from the textbuffer at the end of an CDATA section */
  protected void clearCurrentText()
  {
    this.currentText.delete(0, currentText.length());
  }

  /** returns the current text of the textbuffer */
  protected String getCurrentText()
  {
    return currentText.toString();
  }

  /**
   * Receives some (or all) of the text in the current element.
   */
  public void characters(char[] ch, int start, int length)
  {
    if (this.currentText != null)
      this.currentText.append(String.copyValueOf(ch, start, length));
  }

  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * If an unknown element is encountered, the previous handler gets activated.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
  public void endElement(String namespaceURI, String localName, String qName)
      throws SAXException
  {
    String elementName = qName.toLowerCase().trim();

    // *** LABEL ***
    if (elementName.equals(LABEL_TAG))
    {
      endLabel();
    }
    else if (elementName.equals(IMAGEREF_TAG))
    {
      endImageRef();
    }
    else if (elementName.equals(IMAGEFUNCTION_TAG))
    {
      endImageFunction();
    }
    else if (elementName.equals(IMAGEFIELD_TAG))
    {
      endImageField();
    }
    else if (elementName.equals(IMAGEURLFUNCTION_TAG))
    {
      endImageURLFunction();
    }
    else if (elementName.equals(IMAGEURLFIELD_TAG))
    {
      endImageURLField();
    }
    else if (elementName.equals(LINE_TAG))
    {
      endLine();
    }
    else if (elementName.equals(GENERAL_FIELD_TAG))
    {
      endGeneralField();
    }
    else if (elementName.equals(STRING_FIELD_TAG))
    {
      endStringField();
    }
    else if (elementName.equals(MULTILINE_FIELD_TAG))
    {
      endMultilineField();
    }
    else if (elementName.equals(NUMBER_FIELD_TAG))
    {
      endNumberField();
    }
    else if (elementName.equals(DATE_FIELD_TAG))
    {
      endDateField();
    }
    else if (elementName.equals(STRING_FUNCTION_TAG))
    {
      endStringFunction();
    }
    else if (elementName.equals(NUMBER_FUNCTION_TAG))
    {
      endNumberFunction();
    }
    else if (elementName.equals(DATE_FUNCTION_TAG))
    {
      endDateFunction();
    }
    else if (elementName.equals(RECTANGLE_TAG))
    {
      endRectangle();
    }
    else
    {
      // Dont know who handles this, back to last handler
      handler.finishedHandler();
      handler.endElement(namespaceURI, localName, qName);
    }
  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   */
  protected void startImageRef(Attributes atts) throws SAXException
  {
    String elementName = handler.generateName(atts.getValue("name"));
    String elementSource = atts.getValue("src");
    Log.debug("Loading: " + handler.getContentBase() + " " + elementSource + " as image");
    try
    {
      ImageElement element = ItemFactory.createImageElement(
          elementName,
          ParserUtil.getElementPosition(atts),
          Color.white,
          new URL(handler.getContentBase(), elementSource));
      getCurrentBand().addElement(element);
    }
    catch (IOException mfule)
    {
      throw new SAXException(mfule.toString());
    }

  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   */
  protected void startImageField(Attributes atts) throws SAXException
  {
    String elementName = handler.generateName(atts.getValue(NAME_ATT));
    String elementSource = atts.getValue(FIELDNAME_ATT);
    try
    {
      ImageElement element = ItemFactory.createImageDataRowElement(
          elementName,
          ParserUtil.getElementPosition(atts),
          Color.white,
          elementSource);
      getCurrentBand().addElement(element);
    }
    catch (IOException mfule)
    {
      throw new SAXException(mfule.toString());
    }
  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   */
  protected void startImageURLField(Attributes atts) throws SAXException
  {
    String elementName = handler.generateName(atts.getValue(NAME_ATT));
    String elementSource = atts.getValue(FIELDNAME_ATT);
    try
    {
      ImageElement element = ItemFactory.createImageURLElement(
          elementName,
          ParserUtil.getElementPosition(atts),
          Color.white,
          elementSource);
      getCurrentBand().addElement(element);
    }
    catch (IOException mfule)
    {
      throw new SAXException(mfule.toString());
    }
  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   */
  protected void startImageFunction(Attributes atts) throws SAXException
  {
    String elementName = handler.generateName(atts.getValue(NAME_ATT));
    String elementSource = atts.getValue(FUNCTIONNAME_ATT);
    try
    {
      ImageElement element = ItemFactory.createImageDataRowElement(
          elementName,
          ParserUtil.getElementPosition(atts),
          Color.white,
          elementSource);
      getCurrentBand().addElement(element);
    }
    catch (IOException mfule)
    {
      throw new SAXException(mfule.toString());
    }
  }


  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   */
  protected void startImageURLFunction(Attributes atts) throws SAXException
  {
    String elementName = handler.generateName(atts.getValue(NAME_ATT));
    String elementSource = atts.getValue(FUNCTIONNAME_ATT);
    try
    {
      ImageElement element = ItemFactory.createImageURLElement(
          elementName,
          ParserUtil.getElementPosition(atts),
          Color.white,
          elementSource);
      getCurrentBand().addElement(element);
    }
    catch (IOException mfule)
    {
      throw new SAXException(mfule.toString());
    }
  }

  /**
   * Creates a LineShapeElement.
   */
  protected void startLine(Attributes atts) throws SAXException
  {
    String name = handler.generateName(atts.getValue(NAME_ATT));
    Paint c = ParserUtil.parseColor(atts.getValue(COLOR_ATT));
    float x1 = ParserUtil.parseFloat(atts.getValue("x1"), "Element x1 not specified");
    float y1 = ParserUtil.parseFloat(atts.getValue("y1"), "Element y1 not specified");
    float x2 = ParserUtil.parseFloat(atts.getValue("x2"), "Element x2 not specified");
    float y2 = ParserUtil.parseFloat(atts.getValue("y2"), "Element y2 not specified");

    Line2D line = new Line2D.Float(x1, y1, x2, y2);
    LineShapeElement element = ItemFactory.createLineShapeElement(
        name,
        c,
        ParserUtil.parseStroke(atts.getValue("weight")),
        line);
    getCurrentBand().addElement(element);
  }

  /**
   * Creates a RectangleShapeElement.
   */
  protected void startRectangle(Attributes atts) throws SAXException
  {
    String name = handler.generateName(atts.getValue(NAME_ATT));
    Paint c = ParserUtil.parseColor(atts.getValue(COLOR_ATT));
    Rectangle2D bounds = ParserUtil.getElementPosition(atts);
    boolean shouldDraw = ParserUtil.parseBoolean(atts.getValue("draw"), false);
    boolean shouldFill = ParserUtil.parseBoolean(atts.getValue("fill"), true);

    RectangleShapeElement element = ItemFactory.createRectangleShapeElement(
        name,
        c,
        ParserUtil.parseStroke(atts.getValue("weight")),
        bounds, shouldDraw, shouldFill);
    getCurrentBand().addElement(element);
  }

  /**
   * Creates a label element, an text element with an static datasource attached.
   */
  protected void startLabel(Attributes attr) throws SAXException
  {
    getTextElementAttributes(attr);
    clearCurrentText();
  }

  /**
   * Creates a text element. In ancient times there was a difference between string elements
   * (single line) and multiline fields. This is resolved in the text element class which
   * handles all cases of printing text in reports.
   */
  protected void startMultilineField(Attributes attr) throws SAXException
  {
    getDataElementAttributes(attr);
  }

  /**
   * Creates a text element. In ancient times there was a difference between string elements
   * (single line) and multiline fields. This is resolved in the text element class which
   * handles all cases of printing text in reports.
   */
  protected void startStringField(Attributes attr) throws SAXException
  {
    getDataElementAttributes(attr);
  }

  /**
   * Creates a general element. General elements are text elements.
   */
  protected void startGeneralField(Attributes attr) throws SAXException
  {
    getDataElementAttributes(attr);
  }

  /**
   * Creates a number element. number elements are text elements.
   */
  protected void startNumberField(Attributes attr) throws SAXException
  {
    getDataElementAttributes(attr);
    textElementFormatString = attr.getValue(FORMAT_ATT);
  }

  /**
   * Creates a date element. date elements are text elements.
   */
  protected void startDateField(Attributes attr) throws SAXException
  {
    getDataElementAttributes(attr);
    textElementFormatString = attr.getValue(FORMAT_ATT);
  }

  /**
   * Creates a number function. number functions are text elements.
   */
  protected void startNumberFunction(Attributes attr)
      throws SAXException
  {
    getFunctionElementAttributes(attr);
    textElementFormatString = attr.getValue(FORMAT_ATT);
  }

  /**
   * Creates a date function. date functions are text elements.
   */
  protected void startDateFunction(Attributes attr)
      throws SAXException
  {
    getFunctionElementAttributes(attr);
    textElementFormatString = attr.getValue(FORMAT_ATT);
  }

  /**
   * Creates a string function. string functions are text elements.
   */
  protected void startStringFunction(Attributes attr)
      throws SAXException
  {
    getFunctionElementAttributes(attr);
  }

  /**
   * ends a label tag, sets the static text for the label which was build during the
   * parsing. The label is added to the current band.
   */
  protected void endLabel()
      throws SAXException
  {
    TextElement te = ItemFactory.createLabelElement(textElementName,
        textElementBounds,
        textElementColor,
        textElementAlignment,
        textElementFont,
        getCurrentText());

    clearCurrentText();
    getCurrentBand().addElement(te);
  }

  /**
   * ends the line element and adds it to the current band.
   */
  protected void endLine()
      throws SAXException
  {
  }

  /**
   * ends the rectangle shape element and adds it to the current band.
   */
  protected void endRectangle()
      throws SAXException
  {
  }

  /**
   * ends the image element and adds it to the current band.
   */
  protected void endImageField()
      throws SAXException
  {
  }

  /**
   * ends the image element and adds it to the current band.
   */
  protected void endImageFunction()
      throws SAXException
  {
  }

  /**
   * ends the image element and adds it to the current band.
   */
  protected void endImageURLField()
      throws SAXException
  {
  }

  /**
   * ends the image element and adds it to the current band.
   */
  protected void endImageURLFunction()
      throws SAXException
  {
  }

  /**
   * ends the image element and adds it to the current band.
   */
  protected void endImageRef()
      throws SAXException
  {
  }

  /**
   * ends the multiline text element and adds it to the current band.
   */
  protected void endMultilineField()
      throws SAXException
  {
    TextElement te = ItemFactory.createStringElement(textElementName,
        textElementBounds,
        textElementColor,
        textElementAlignment,
        textElementFont,
        textElementNullString,
        textElementSourceName);
    getCurrentBand().addElement(te);
  }

  /**
   * ends the String field and adds it to the current band.
   */
  protected void endStringField()
      throws SAXException
  {
    TextElement te = ItemFactory.createStringElement(textElementName,
        textElementBounds,
        textElementColor,
        textElementAlignment,
        textElementFont,
        textElementNullString,
        textElementSourceName);
    getCurrentBand().addElement(te);
  }

  /**
   * ends the general element and adds it to the current band.
   */
  protected void endGeneralField()
      throws SAXException
  {
    TextElement te = ItemFactory.createGeneralElement(textElementName,
        textElementBounds,
        textElementColor,
        textElementAlignment,
        textElementFont,
        textElementNullString,
        textElementSourceName);
    getCurrentBand().addElement(te);
  }

  /**
   * ends the number field and adds it to the current band.
   */
  protected void endNumberField()
      throws SAXException
  {
    TextElement te = ItemFactory.createNumberElement(textElementName,
        textElementBounds,
        textElementColor,
        textElementAlignment,
        textElementFont,
        textElementNullString,
        textElementFormatString,
        textElementSourceName);
    getCurrentBand().addElement(te);
  }

  /**
   * ends the date field and adds it to the current band.
   */
  protected void endDateField()
      throws SAXException
  {
    TextElement te = ItemFactory.createDateElement(textElementName,
        textElementBounds,
        textElementColor,
        textElementAlignment,
        textElementFont,
        textElementNullString,
        textElementFormatString,
        textElementSourceName);
    getCurrentBand().addElement(te);
  }

  /**
   * ends the number function and adds it to the current band.
   */
  protected void endNumberFunction()
      throws SAXException
  {
    TextElement te = ItemFactory.createNumberElement(textElementName,
        textElementBounds,
        textElementColor,
        textElementAlignment,
        textElementFont,
        textElementNullString,
        textElementFormatString,
        textElementSourceName);
    getCurrentBand().addElement(te);
  }

  /**
   * ends the string function and adds it to the current band.
   */
  protected void endStringFunction()
      throws SAXException
  {
    TextElement te = ItemFactory.createStringElement(textElementName,
        textElementBounds,
        textElementColor,
        textElementAlignment,
        textElementFont,
        textElementNullString,
        textElementSourceName);
    getCurrentBand().addElement(te);
  }

  /**
   * ends the date function and adds it to the current band.
   */
  protected void endDateFunction()
      throws SAXException
  {
    TextElement te = ItemFactory.createDateElement(textElementName,
        textElementBounds,
        textElementColor,
        textElementAlignment,
        textElementFont,
        textElementNullString,
        textElementFormatString,
        textElementSourceName);
    getCurrentBand().addElement(te);
  }

  /**
   * Reads the attributes that are common for all band-elements, as
   * name, x, y, width, height, font, fontstyle, fontsize and alignment
   */
  protected void getTextElementAttributes(Attributes atts)
      throws SAXException
  {
    textElementName = handler.generateName(atts.getValue(NAME_ATT));
    textElementBounds = ParserUtil.getElementPosition(atts);
    textElementFont = fontFactory.createFont(atts);
    textElementAlignment = parseTextAlignment(atts.getValue(ALIGNMENT_ATT), TextElement.LEFT);
    textElementColor = ParserUtil.parseColor(atts.getValue(COLOR_ATT));
  }

  /**
   * parses the text alignment, which is one of "left", "center" or "right".
   */
  protected int parseTextAlignment(String alignment, int defaultAlignment)
  {
    int elementAlignment = defaultAlignment;
    if (alignment != null)
    {
      if (alignment.equals("left"))
        elementAlignment = Element.LEFT;
      if (alignment.equals("center"))
        elementAlignment = Element.CENTER;
      if (alignment.equals("right"))
        elementAlignment = Element.RIGHT;
    }
    return elementAlignment;
  }

  /**
   * Appends all data element relevant attributes to the data element parsed
   */
  protected void getDataElementAttributes(Attributes atts)
      throws SAXException
  {
    getTextElementAttributes(atts);
    textElementNullString = ParserUtil.parseString(atts.getValue(NULLSTRING_ATT), "-");
    textElementSourceName = atts.getValue(FIELDNAME_ATT);
  }

  /**
   * Appends all function element relevant attributes to the data element parsed
   */
  protected void getFunctionElementAttributes(Attributes atts)
      throws SAXException
  {
    getTextElementAttributes(atts);
    textElementNullString = ParserUtil.parseString(atts.getValue(NULLSTRING_ATT), "-");
    textElementSourceName = atts.getValue(FUNCTIONNAME_ATT);
  }

}
