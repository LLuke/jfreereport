package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.Element;
import org.jfree.report.filter.DataSource;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ElementReadHandler extends AbstractXmlReadHandler
{
  private XmlReadHandler dataSourceHandler;
  private Element element;

  public ElementReadHandler (final Element element)
  {
    this.element = element;
  }

  protected Element getElement ()
  {
    return element;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final Attributes attrs)
          throws SAXException, XmlReaderException
  {
    final String name = attrs.getValue("name");
    if (name != null)
    {
      element.setName(name);
    }

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
    if (tagName.equals("style"))
    {
      return new StyleReadHandler(element.getStyle());
    }
    else if (tagName.equals("datasource"))
    {
      dataSourceHandler = new DataSourceReadHandler();
      return dataSourceHandler;
    }
    else if (tagName.equals("template"))
    {
      dataSourceHandler = new TemplateReadHandler(false);
      return dataSourceHandler;
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
    if (dataSourceHandler != null)
    {
      element.setDataSource((DataSource) dataSourceHandler.getObject());
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
    return element;
  }
}
