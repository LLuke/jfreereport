package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.ArrayList;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.xml.ParseException;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.util.Log;
import org.xml.sax.SAXException;

public class BandReadHandler extends ElementReadHandler
{
  private ArrayList elementHandlers;

  public BandReadHandler (final Band element)
  {
    super(element);
    elementHandlers = new ArrayList();
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
    if (tagName.equals("style"))
    {
      return new StyleReadHandler(getElement().getStyle());
    }
    else if (tagName.equals("default-style"))
    {
      Log.warn ("Tag <default-style> is deprecated. All definitions " +
              "have been mapped into the bands primary style sheet.");
      return new StyleReadHandler(getElement().getStyle());
    }
    else if (tagName.equals("element"))
    {
      final String type = atts.getValue("type");
      if (type == null)
      {
        throw new ParseException("The element's 'type' attribute is missing",
            getRootHandler().getLocator());
      }

      final ElementFactoryCollector fc = (ElementFactoryCollector)
              getRootHandler().getHelperObject
              (ReportDefinitionReadHandler.ELEMENT_FACTORY_KEY);
      final Element element = fc.getElementForType(type);
      if (element == null)
      {
        throw new ParseException("There is no factory for elements of type '" + type + "'");
      }

      final XmlReadHandler readHandler = new ElementReadHandler(element);
      elementHandlers.add(readHandler);
      return readHandler;
    }
    else if (tagName.equals("band"))
    {
      final XmlReadHandler readHandler = new BandReadHandler(new Band());
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
    super.doneParsing();
    final Band band = (Band) getElement();
    for (int i = 0; i < elementHandlers.size(); i++)
    {
      final ElementReadHandler readHandler = (ElementReadHandler) elementHandlers.get(i);
      band.addElement(readHandler.getElement());
    }
  }

  protected void storeComments ()
          throws SAXException
  {
    //getRootHandler().
  }
}
