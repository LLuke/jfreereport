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
 * 
 */
package com.jrefinery.report.io;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jrefinery.report.Band;
import com.jrefinery.report.DataElement;
import com.jrefinery.report.DateElement;
import com.jrefinery.report.DateFunctionElement;
import com.jrefinery.report.Element;
import com.jrefinery.report.FunctionElement;
import com.jrefinery.report.GeneralElement;
import com.jrefinery.report.ImageElement;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.LabelElement;
import com.jrefinery.report.LineShapeElement;
import com.jrefinery.report.MultilineTextElement;
import com.jrefinery.report.NumberElement;
import com.jrefinery.report.NumberFunctionElement;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.StringElement;
import com.jrefinery.report.StringFunctionElement;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.util.Log;

public class ElementFactory
  extends AbstractReportDefinitionHandler
  implements ReportDefinitionTags
{
  private StringBuffer currentText;
  private Band currentBand;
  private Element currentElement;
  private FontFactory fontFactory;
  private ReportDefinitionContentHandler handler;

  public ElementFactory(Band band, ReportDefinitionContentHandler handler)
  {
    currentBand = band;
    this.handler = handler;
    fontFactory = handler.getFontFactory();
  }

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
      // CDATA is the literal text
      currentElement = generateLabelElement(atts);
      currentText = new StringBuffer();
    }
    else if (elementName.equals(IMAGEREF_TAG))
    {
      currentElement = generateImageElement(atts);
    }
    else if (elementName.equals(LINE_TAG))
    {
      currentElement = generateLineElement(atts);
    }
    else if (elementName.equals(GENERAL_FIELD_TAG))
    {
      currentElement = generateGeneralElement(atts);
    }
    else if (elementName.equals(STRING_FIELD_TAG))
    {
      currentElement = generateStringElement(atts);
    }
    else if (elementName.equals(MULTILINE_FIELD_TAG))
    {
      currentElement = generateMultilineElement(atts);
    }
    else if (elementName.equals(NUMBER_FIELD_TAG))
    {
      currentElement = generateNumberElement(atts);
    }
    else if (elementName.equals(DATE_FIELD_TAG))
    {
      currentElement = generateDateElement(atts);
    }
    else if (elementName.equals(STRING_FUNCTION_TAG))
    {
      currentElement = generateStringFunctionElement(atts);
    }
    else if (elementName.equals(NUMBER_FUNCTION_TAG))
    {
      currentElement = generateNumberFunctionElement(atts);
    }
    else if (elementName.equals(DATE_FUNCTION_TAG))
    {
      currentElement = generateDateFunctionElement(atts);
    }
  }

  /**
   * Receives some (or all) of the text in the current element.
   */
  public void characters(char[] ch, int start, int length)
  {
    if (this.currentText != null)
      this.currentText.append(String.copyValueOf(ch, start, length));
  }

  public void endElement(String namespaceURI, String localName, String qName)
    throws SAXException
  {
    String elementName = qName.toLowerCase().trim();

    // *** LABEL ***
    if (elementName.equals(LABEL_TAG))
    {
      LabelElement label = (LabelElement) currentElement;
      label.setLabel(currentText.toString());
      currentText = null;
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(IMAGEREF_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(LINE_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(GENERAL_FIELD_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(STRING_FIELD_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(MULTILINE_FIELD_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(NUMBER_FIELD_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(DATE_FIELD_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(STRING_FUNCTION_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(NUMBER_FUNCTION_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else if (elementName.equals(DATE_FUNCTION_TAG))
    {
      currentBand.addElement(currentElement);
    }
    else
    {
      // Dont know who handles this, back to last handler
      handler.finishedHandler();
      handler.endElement(namespaceURI, localName, qName);
    }
  }

  public ImageElement generateImageElement(Attributes atts) throws SAXException
  {
    String elementName = generateName(atts.getValue("name"));
    String elementSource = atts.getValue("src");
    Log.debug("Loading: " + getContentBase() + " " + elementSource + " as image");
    try
    {
      ImageReference imgRef =
        new ImageReference(
          new URL(handler.getContentBase(), elementSource),
          getElementPosition(atts));
      ImageElement element = new ImageElement();
      element.setName(elementName);
      element.setImageReference(imgRef);

      return element;
    }
    catch (IOException mfule)
    {
      throw new SAXException(mfule.toString());
    }

  }

  public ShapeElement generateLineElement(Attributes atts) throws SAXException
  {
    String name = generateName(atts.getValue("name"));
    float x1 = parseFloat(atts.getValue("x1"), "Element x1 not specified");
    float y1 = parseFloat(atts.getValue("y1"), "Element y1 not specified");
    float x2 = parseFloat(atts.getValue("x2"), "Element x2 not specified");
    float y2 = parseFloat(atts.getValue("y2"), "Element y2 not specified");

    Line2D line = new Line2D.Float(x1, y1, x2, y2);
    LineShapeElement element = new LineShapeElement();
    element.setStroke(parseStroke(atts.getValue("weight")));
    element.setPaint(parseColor(atts.getValue("color")));
    element.setBounds(new Rectangle2D.Float(x1, y1, x2 - x1, y2 - y1));
    element.setName(name);
    element.setLine(line);
    return element;
  }

  protected Stroke parseStroke(String weight) {
    try
    {
      if (weight != null)
      {
        Float w = new Float(weight);
        return new BasicStroke(w.floatValue());
      }
    }
    catch (NumberFormatException nfe)
    {
      Log.debug ("Invalid weight for line element", nfe);
    }
    return new BasicStroke(1);
  }

  protected Paint parseColor(String color)
  {
    if (color == null)
      return Color.black;

    try
    {
      // get color by hex or octal value
      return Color.decode(color);
    }
    catch (NumberFormatException nfe)
    {
      // if we can't decode lets try to get it by name
      try
      {
        // try to get a color by name using reflection
        // black is used for an instance and not for the color itselfs
        Field f = Color.black.getClass().getField(color);

        return (Color) f.get(Color.black);
      }
      catch (Exception ce)
      {

        // if we can't get any color return black
        return Color.black;
      }
    }

  }

  public LabelElement generateLabelElement(Attributes attr) throws SAXException
  {
    LabelElement label = new LabelElement();
    getTextElementAttributes(attr, label);
    return label;
  }

  public MultilineTextElement generateMultilineElement(Attributes attr) throws SAXException
  {
    MultilineTextElement mlText = new MultilineTextElement();
    getDataElementAttributes(attr, mlText);
    return mlText;
  }

  public StringElement generateStringElement(Attributes attr) throws SAXException
  {
    StringElement string = new StringElement();
    getDataElementAttributes(attr, string);
    return string;
  }

  public GeneralElement generateGeneralElement(Attributes attr) throws SAXException
  {
    GeneralElement general = new GeneralElement();
    getDataElementAttributes(attr, general);
    return general;
  }

  public NumberElement generateNumberElement(Attributes attr) throws SAXException
  {
    NumberElement numberElement = new NumberElement();
    getDataElementAttributes(attr, numberElement);
    numberElement.setDecimalFormatString(attr.getValue("format"));
    return numberElement;
  }

  public DateElement generateDateElement(Attributes attr) throws SAXException
  {
    DateElement dateElement = new DateElement();
    getDataElementAttributes(attr, dateElement);
    dateElement.setFormatString(attr.getValue("format"));
    return dateElement;
  }

  public NumberFunctionElement generateNumberFunctionElement(Attributes attr)
    throws SAXException
  {
    NumberFunctionElement numberElement = new NumberFunctionElement();
    getFunctionElementAttributes(attr, numberElement);
    numberElement.setDecimalFormatString(attr.getValue("format"));
    return numberElement;
  }

  public DateFunctionElement generateDateFunctionElement(Attributes attr)
    throws SAXException
  {
    DateFunctionElement dateElement = new DateFunctionElement();
    getFunctionElementAttributes(attr, dateElement);
    dateElement.setFormatString(attr.getValue("format"));
    return dateElement;
  }

  public StringFunctionElement generateStringFunctionElement(Attributes attr)
    throws SAXException
  {
    StringFunctionElement string = new StringFunctionElement();
    getFunctionElementAttributes(attr, string);
    return string;
  }

  //______________________________________________________________________________________
  /**
   * Reads the attributes that are common for all band-elements, as
   * name, x, y, width, height, font, fontstyle, fontsize and alignment
   */
  protected void getTextElementAttributes(Attributes atts, TextElement element)
    throws SAXException
  {
    element.setName(generateName(atts.getValue("name")));
    element.setBounds(getElementPosition(atts));
    element.setFont(fontFactory.createFont(atts));

    // alignment
    int elementAlignment = Element.LEFT;
    String s = atts.getValue("alignment");
    if (s != null)
    {
      if (s.equals("left"))
        elementAlignment = Element.LEFT;
      if (s.equals("center"))
        elementAlignment = Element.CENTER;
      if (s.equals("right"))
        elementAlignment = Element.RIGHT;
    }
    element.setAlignment(elementAlignment);
  }

  protected void getDataElementAttributes(Attributes atts, DataElement element)
    throws SAXException
  {
    getTextElementAttributes(atts, element);
    element.setField(atts.getValue("fieldname"));
  }

  protected void getFunctionElementAttributes(Attributes atts, FunctionElement element)
    throws SAXException
  {
    getTextElementAttributes(atts, element);
    element.setFunctionName(atts.getValue("function"));
  }

  protected Rectangle2D getElementPosition(Attributes atts) throws SAXException
  {
    // x
    float x = parseFloat(atts.getValue("x"), "Element x not specified");
    float y = parseFloat(atts.getValue("y"), "Element y not specified");
    float w = parseFloat(atts.getValue("width"), "Element width not specified");
    float h = parseFloat(atts.getValue("height"), "Element height not specified");
    Rectangle2D.Float retval = new Rectangle2D.Float(x, y, w, h);
    return retval;
  }

  public JFreeReport getReport()
  {
    return null;
  }

}