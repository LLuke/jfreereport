/**
 * Date: Jan 11, 2003
 * Time: 4:52:58 PM
 *
 * $Id: BandHandler.java,v 1.2 2003/01/13 19:00:41 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.jrefinery.report.Band;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.Element;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.io.ext.factory.elements.ElementFactoryCollector;
import com.jrefinery.report.io.Parser;

public class BandHandler extends ElementHandler
{
  public static final String BAND_TAG = "band";
  public static final String ELEMENT_TAG = "element";
  public static final String DEFAULT_STYLE_TAG = "default-style";

  private ElementHandler elementHandler;

  public BandHandler(Parser parser, String finishTag, Band band)
  {
    super(parser, finishTag, band);
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(BAND_TAG))
    {
      Band band = new Band();
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

      String name = attrs.getValue("name");
      if (name != null)
      {
        element.setName(name);
      }

      elementHandler = new ElementHandler(getParser(), tagName, element);
      getParser().pushFactory(elementHandler);
    }
    else if (tagName.equals(DEFAULT_STYLE_TAG))
    {
      ElementStyleSheet styleSheet = getBand().getBandDefaults();
      StyleSheetHandler styleSheetFactory = new StyleSheetHandler(getParser(), tagName, styleSheet);
      getParser().pushFactory(styleSheetFactory);
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
    else if (tagName.equals(DEFAULT_STYLE_TAG))
    {
      // ignore event ...
    }
    else
    {
      super.endElement(tagName);
    }
  }
}
