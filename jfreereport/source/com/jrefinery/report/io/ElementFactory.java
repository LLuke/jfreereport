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
 */
package com.jrefinery.report.io;

import com.jrefinery.report.Band;
import com.jrefinery.report.DataElement;
import com.jrefinery.report.DateElement;
import com.jrefinery.report.DateFunctionElement;
import com.jrefinery.report.Element;
import com.jrefinery.report.FunctionElement;
import com.jrefinery.report.GeneralElement;
import com.jrefinery.report.ImageElement;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.LabelElement;
import com.jrefinery.report.LineShapeElement;
import com.jrefinery.report.MultilineTextElement;
import com.jrefinery.report.NumberElement;
import com.jrefinery.report.NumberFunctionElement;
import com.jrefinery.report.RectangleShapeElement;
import com.jrefinery.report.StringElement;
import com.jrefinery.report.StringFunctionElement;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.ImageFunctionElement;
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

  /** The current element */
  private Element currentElement;

  /** the fontfactory used to fill TextElements font definitions */
  private FontFactory fontFactory;

  /** The base handler */
  private ReportDefinitionContentHandler handler;

  /**
   * Creates a new ElementFactory. The factory queries the current Band of the ReportFactory
   * and will add created element to this band. If unknown end-Tags are encountered, the
   * parsing for elements will stop and the previous handler will be activated.
   */
  public ElementFactory (ReportFactory base)
  {
    currentBand = base.getCurrentBand ();
    this.handler = base.getHandler ();
    this.currentText = new StringBuffer ();
    fontFactory = handler.getFontFactory ();
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
  public void startElement (
          String namespaceURI,
          String localName,
          String qName,
          Attributes atts)
          throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();

    // *** LABEL ***
    if (elementName.equals (LABEL_TAG))
    {
      startLabel (atts);
    }
    else if (elementName.equals (IMAGEREF_TAG))
    {
      startImageRef (atts);
    }
    else if (elementName.equals (IMAGEFIELD_TAG))
    {
      startImageField (atts);
    }
    else if (elementName.equals (IMAGEFUNCTION_TAG))
    {
      startImageFunction (atts);
    }
    else if (elementName.equals (LINE_TAG))
    {
      startLine (atts);
    }
    else if (elementName.equals (GENERAL_FIELD_TAG))
    {
      startGeneralField (atts);
    }
    else if (elementName.equals (STRING_FIELD_TAG))
    {
      startStringField (atts);
    }
    else if (elementName.equals (MULTILINE_FIELD_TAG))
    {
      startMultilineField (atts);
    }
    else if (elementName.equals (NUMBER_FIELD_TAG))
    {
      startNumberField (atts);
    }
    else if (elementName.equals (DATE_FIELD_TAG))
    {
      startDateField (atts);
    }
    else if (elementName.equals (STRING_FUNCTION_TAG))
    {
      startStringFunction (atts);
    }
    else if (elementName.equals (NUMBER_FUNCTION_TAG))
    {
      startNumberFunction (atts);
    }
    else if (elementName.equals (DATE_FUNCTION_TAG))
    {
      startDateFunction (atts);
    }
    else if (elementName.equals (RECTANGLE_TAG))
    {
      startRectangle (atts);
    }
  }

  /** Defines the current element that gets parsed */
  protected void setCurrentElement (Element e)
  {
    this.currentElement = e;
  }

  /** returns the current element */
  protected Element getCurrentElement ()
  {
    return currentElement;
  }

  /** returns the current band, which receives the parsed elements */
  protected Band getCurrentBand ()
  {
    return currentBand;
  }

  /** removes all text from the textbuffer at the end of an CDATA section */
  protected void clearCurrentText ()
  {
    this.currentText.delete (0, currentText.length ());
  }

  /** returns the current text of the textbuffer */
  protected String getCurrentText ()
  {
    return currentText.toString ();
  }

  /**
   * Receives some (or all) of the text in the current element.
   */
  public void characters (char[] ch, int start, int length)
  {
    if (this.currentText != null)
      this.currentText.append (String.copyValueOf (ch, start, length));
  }

  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * If an unknown element is encountered, the previous handler gets activated.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
  public void endElement (String namespaceURI, String localName, String qName)
          throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();

    // *** LABEL ***
    if (elementName.equals (LABEL_TAG))
    {
      endLabel ();
    }
    else if (elementName.equals (IMAGEREF_TAG))
    {
      endImageRef ();
    }
    else if (elementName.equals (IMAGEFUNCTION_TAG))
    {
      endImageFunction ();
    }
    else if (elementName.equals (IMAGEFIELD_TAG))
    {
      endImageField ();
    }
    else if (elementName.equals (LINE_TAG))
    {
      endLine ();
    }
    else if (elementName.equals (GENERAL_FIELD_TAG))
    {
      endGeneralField ();
    }
    else if (elementName.equals (STRING_FIELD_TAG))
    {
      endStringField ();
    }
    else if (elementName.equals (MULTILINE_FIELD_TAG))
    {
      endMultilineField ();
    }
    else if (elementName.equals (NUMBER_FIELD_TAG))
    {
      endNumberField ();
    }
    else if (elementName.equals (DATE_FIELD_TAG))
    {
      endDateField ();
    }
    else if (elementName.equals (STRING_FUNCTION_TAG))
    {
      endStringFunction ();
    }
    else if (elementName.equals (NUMBER_FUNCTION_TAG))
    {
      endNumberFunction ();
    }
    else if (elementName.equals (DATE_FUNCTION_TAG))
    {
      endDateFunction ();
    }
    else if (elementName.equals (RECTANGLE_TAG))
    {
      endRectangle ();
    }
    else
    {
      // Dont know who handles this, back to last handler
      handler.finishedHandler ();
      handler.endElement (namespaceURI, localName, qName);
    }
  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   */
  protected void startImageRef (Attributes atts) throws SAXException
  {
    String elementName = handler.generateName (atts.getValue ("name"));
    String elementSource = atts.getValue ("src");
    Log.debug ("Loading: " + handler.getContentBase () + " " + elementSource + " as image");
    try
    {
      ImageElement element = ItemFactory.createImageElement (
              elementName,
              ParserUtil.getElementPosition (atts),
              Color.white,
              new URL (handler.getContentBase (), elementSource));
      setCurrentElement (element);
    }
    catch (IOException mfule)
    {
      throw new SAXException (mfule.toString ());
    }

  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   */
  protected void startImageField (Attributes atts) throws SAXException
  {
    String elementName = handler.generateName (atts.getValue (NAME_ATT));
    String elementSource = atts.getValue (FIELDNAME_ATT);
    try
    {
      ImageElement element = ItemFactory.createImageFunctionElement (
              elementName,
              ParserUtil.getElementPosition (atts),
              Color.white,
              elementSource);
      setCurrentElement (element);
    }
    catch (IOException mfule)
    {
      throw new SAXException (mfule.toString ());
    }
  }

  /**
   * Create a ImageElement with an static ImageDataSource. The ImageData is read from
   * the supplied URL (attribute "src") in conjunction with the contentbase defined in the
   * ReportDefintionContentHandler
   */
  protected void startImageFunction (Attributes atts) throws SAXException
  {
    String elementName = handler.generateName (atts.getValue (NAME_ATT));
    String elementSource = atts.getValue (FUNCTIONNAME_ATT);
    try
    {
      ImageElement element = ItemFactory.createImageFunctionElement (
              elementName,
              ParserUtil.getElementPosition (atts),
              Color.white,
              elementSource);
      setCurrentElement (element);
    }
    catch (IOException mfule)
    {
      throw new SAXException (mfule.toString ());
    }
  }

  /**
   * Creates a LineShapeElement.
   */
  protected void startLine (Attributes atts) throws SAXException
  {
    String name = handler.generateName (atts.getValue (NAME_ATT));
    Paint c = ParserUtil.parseColor (atts.getValue (COLOR_ATT));
    float x1 = ParserUtil.parseFloat (atts.getValue ("x1"), "Element x1 not specified");
    float y1 = ParserUtil.parseFloat (atts.getValue ("y1"), "Element y1 not specified");
    float x2 = ParserUtil.parseFloat (atts.getValue ("x2"), "Element x2 not specified");
    float y2 = ParserUtil.parseFloat (atts.getValue ("y2"), "Element y2 not specified");

    Line2D line = new Line2D.Float (x1, y1, x2, y2);
    LineShapeElement element = ItemFactory.createLineShapeElement (
            name,
            c,
            ParserUtil.parseStroke (atts.getValue ("weight")),
            line);
    setCurrentElement (element);
  }

  /**
   * Creates a RectangleShapeElement.
   */
  protected void startRectangle (Attributes atts) throws SAXException
  {
    String name = handler.generateName (atts.getValue (NAME_ATT));
    Paint c = ParserUtil.parseColor (atts.getValue (COLOR_ATT));
    Rectangle2D bounds = ParserUtil.getElementPosition (atts);

    RectangleShapeElement element = ItemFactory.createRectangleShapeElement (
            name,
            c,
            ParserUtil.parseStroke (atts.getValue ("weight")),
            bounds);
    setCurrentElement (element);
  }

  /**
   * Creates a label element, an text element with an static datasource attached.
   */
  protected void startLabel (Attributes attr) throws SAXException
  {
    LabelElement label = new LabelElement ();
    getTextElementAttributes (attr, label);
    setCurrentElement (label);
    clearCurrentText ();
  }

  /**
   * Creates a text element. In ancient times there was a difference between string elements
   * (single line) and multiline fields. This is resolved in the text element class which
   * handles all cases of printing text in reports.
   */
  protected void startMultilineField (Attributes attr) throws SAXException
  {
    MultilineTextElement mlText = new MultilineTextElement ();
    getDataElementAttributes (attr, mlText);
    setCurrentElement (mlText);
  }

  /**
   * Creates a text element. In ancient times there was a difference between string elements
   * (single line) and multiline fields. This is resolved in the text element class which
   * handles all cases of printing text in reports.
   */
  protected void startStringField (Attributes attr) throws SAXException
  {
    StringElement string = new StringElement ();
    getDataElementAttributes (attr, string);
    setCurrentElement (string);
  }

  /**
   * Creates a general element. General elements are text elements.
   */
  protected void startGeneralField (Attributes attr) throws SAXException
  {
    GeneralElement general = new GeneralElement ();
    getDataElementAttributes (attr, general);
    setCurrentElement (general);
  }

  /**
   * Creates a number element. number elements are text elements.
   */
  protected void startNumberField (Attributes attr) throws SAXException
  {
    NumberElement numberElement = new NumberElement ();
    getDataElementAttributes (attr, numberElement);
    numberElement.setDecimalFormatString (attr.getValue ("format"));
    setCurrentElement (numberElement);
  }

  /**
   * Creates a date element. date elements are text elements.
   */
  protected void startDateField (Attributes attr) throws SAXException
  {
    DateElement dateElement = new DateElement ();
    getDataElementAttributes (attr, dateElement);
    dateElement.setFormatString (attr.getValue ("format"));
    setCurrentElement (dateElement);
  }

  /**
   * Creates a number function. number functions are text elements.
   */
  protected void startNumberFunction (Attributes attr)
          throws SAXException
  {
    NumberFunctionElement numberElement = new NumberFunctionElement ();
    getFunctionElementAttributes (attr, numberElement);
    numberElement.setDecimalFormatString (attr.getValue ("format"));
    setCurrentElement (numberElement);
  }

  /**
   * Creates a date function. date functions are text elements.
   */
  protected void startDateFunction (Attributes attr)
          throws SAXException
  {
    DateFunctionElement dateElement = new DateFunctionElement ();
    getFunctionElementAttributes (attr, dateElement);
    dateElement.setFormatString (attr.getValue ("format"));
    setCurrentElement (dateElement);
  }

  /**
   * Creates a string function. string functions are text elements.
   */
  protected void startStringFunction (Attributes attr)
          throws SAXException
  {
    StringFunctionElement string = new StringFunctionElement ();
    getFunctionElementAttributes (attr, string);
    setCurrentElement (string);
  }

  /**
   * ends a label tag, sets the static text for the label which was build during the
   * parsing. The label is added to the current band.
   */
  protected void endLabel ()
          throws SAXException
  {
    LabelElement label = (LabelElement) getCurrentElement ();
    label.setLabel (getCurrentText ());
    clearCurrentText ();
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the line element and adds it to the current band.
   */
  protected void endLine ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the rectangle shape element and adds it to the current band.
   */
  protected void endRectangle ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the image element and adds it to the current band.
   */
  protected void endImageField ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the image element and adds it to the current band.
   */
  protected void endImageFunction ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the image element and adds it to the current band.
   */
  protected void endImageRef ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the multiline text element and adds it to the current band.
   */
  protected void endMultilineField ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the String field and adds it to the current band.
   */
  protected void endStringField ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the general element and adds it to the current band.
   */
  protected void endGeneralField ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the number field and adds it to the current band.
   */
  protected void endNumberField ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the date field and adds it to the current band.
   */
  protected void endDateField ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the number function and adds it to the current band.
   */
  protected void endNumberFunction ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the string function and adds it to the current band.
   */
  protected void endStringFunction ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * ends the date function and adds it to the current band.
   */
  protected void endDateFunction ()
          throws SAXException
  {
    getCurrentBand ().addElement (getCurrentElement ());
  }

  /**
   * Reads the attributes that are common for all band-elements, as
   * name, x, y, width, height, font, fontstyle, fontsize and alignment
   */
  protected void getTextElementAttributes (Attributes atts, TextElement element)
          throws SAXException
  {
    String name = handler.generateName (atts.getValue (NAME_ATT));
    Rectangle2D bounds = ParserUtil.getElementPosition (atts);
    Font font = fontFactory.createFont (atts);
    int alignment = parseTextAlignment (atts.getValue (ALIGNMENT_ATT), TextElement.LEFT);
    Paint color = ParserUtil.parseColor (atts.getValue (COLOR_ATT));

    element.setName (name);
    element.setBounds (bounds);
    element.setFont (font);
    element.setAlignment (alignment);
    element.setPaint (color);
  }

  /**
   * parses the text alignment, which is one of "left", "center" or "right".
   */
  protected int parseTextAlignment (String alignment, int defaultAlignment)
  {
    int elementAlignment = defaultAlignment;
    if (alignment != null)
    {
      if (alignment.equals ("left"))
        elementAlignment = Element.LEFT;
      if (alignment.equals ("center"))
        elementAlignment = Element.CENTER;
      if (alignment.equals ("right"))
        elementAlignment = Element.RIGHT;
    }
    return elementAlignment;
  }

  /**
   * Appends all data element relevant attributes to the data element parsed
   */
  protected void getDataElementAttributes (Attributes atts, DataElement element)
          throws SAXException
  {
    getTextElementAttributes (atts, element);
    element.setNullString (ParserUtil.parseString (atts.getValue (NULLSTRING_ATT), "-"));
    element.setField (atts.getValue (FIELDNAME_ATT));
  }

  /**
   * adds all function element relevant attributes to the function element
   */
  protected void getFunctionElementAttributes (Attributes atts, FunctionElement element)
          throws SAXException
  {
    getTextElementAttributes (atts, element);
    element.setNullString (ParserUtil.parseString (atts.getValue (NULLSTRING_ATT), "-"));
    element.setFunctionName (atts.getValue (FUNCTIONNAME_ATT));
  }
}
