/**
 * Date: Jan 11, 2003
 * Time: 4:52:58 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.jrefinery.report.Band;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.Element;
import com.jrefinery.report.io.ext.factory.elements.ElementFactoryCollector;
import com.jrefinery.report.io.Parser;

public class BandHandler extends ElementHandler
{
  public static final String BAND_TAG = "band";
  public static final String ELEMENT_TAG = "element";

  private ElementHandler elementHandler;

  public BandHandler(Parser parser, String finishTag, Band band)
  {
    super(parser, finishTag, band);
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(BAND_TAG))
    {
      Band band = new ItemBand();
      String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      elementHandler = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(elementHandler);
      // ignore
    }
    else if (tagName.equals(ELEMENT_TAG))
    {
      String type = attrs.getValue("type");
      if (type == null)
        throw new SAXException("The element's 'type' attribute is missing");

      ElementFactoryCollector fc =
          (ElementFactoryCollector) getParser().getConfigurationValue(ParserConfigHandler.ELEMENT_FACTORY_TAG);
      Element element = fc.getElementForType(type);
      elementHandler = new ElementHandler(getParser(), tagName, element);
      getParser().pushFactory(elementHandler);
    }
    else
    {
      super.startElement(tagName, attrs);
    }
  }

  private Band getBand ()
  {
    return (Band) getElement();
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(BAND_TAG))
    {
      if (elementHandler != null)
      {
        getBand().addElement(elementHandler.getElement());
      }
      else
      {
        super.endElement(tagName);
      }
    }
    else if (tagName.equals(ELEMENT_TAG))
    {
      getBand().addElement(elementHandler.getElement());
    }
    else
    {
      super.endElement(tagName);
    }
  }
}
