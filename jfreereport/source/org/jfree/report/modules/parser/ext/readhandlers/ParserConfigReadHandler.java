package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ParserConfigReadHandler extends AbstractXmlReadHandler
{
  public ParserConfigReadHandler ()
  {
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
    if (tagName.equals("element-factory"))
    {
      return new ElementFactoryReadHandler();
    }
    else if (tagName.equals("stylekey-factory"))
    {
      return new StyleKeyFactoryReadHandler();
    }
    else if (tagName.equals("template-factory"))
    {
      return new TemplatesFactoryReadHandler();
    }
    else if (tagName.equals("object-factory"))
    {
      return new ClassFactoryReadHandler();
    }
    else if (tagName.equals("datasource-factory"))
    {
      return new DataSourceFactoryReadHandler();
    }
    return null;
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
    return null;
  }
}