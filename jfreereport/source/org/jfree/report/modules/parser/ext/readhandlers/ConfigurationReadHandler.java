package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.report.util.ReportConfiguration;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.coretypes.StringReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConfigurationReadHandler extends AbstractXmlReadHandler
{
  private ReportConfiguration configuration;
  private HashMap fieldHandlers;

  public ConfigurationReadHandler (final ReportConfiguration configuration)
  {
    this.configuration = configuration;
    this.fieldHandlers = new HashMap();
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
    if (tagName.equals("property"))
    {
      final String name = atts.getValue("name");
      if (name == null)
      {
        throw new ElementDefinitionException("Required attribute 'name' is missing.");
      }

      final StringReadHandler readHandler = new StringReadHandler ();
      fieldHandlers.put (name, readHandler);
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
    final Iterator it = fieldHandlers.entrySet().iterator();
    while(it.hasNext())
    {
      final Map.Entry entry = (Map.Entry) it.next();
      final String key = (String) entry.getKey();
      final StringReadHandler readHandler = (StringReadHandler) entry.getValue();
      configuration.setConfigProperty(key, readHandler.getResult());
    }
  }

  /**
   * Returns the object for this element.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return configuration;
  }
}
