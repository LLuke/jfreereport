/**
 * Date: Jan 10, 2003
 * Time: 5:11:51 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Properties;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;

public class PropertyHandler implements ReportDefinitionHandler
{
  public static final String PROPERTY = "property";
  public static final String NAME_ATTR = "name";

  private Parser parser;
  private String finishTag;
  private Properties properties;
  private StringBuffer buffer = null;
  private String name;

  public PropertyHandler (Parser parser, String finishTag)
  {
    properties = new Properties();
    this.finishTag = finishTag;
    this.parser = parser;
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(PROPERTY) == false)
      throw new SAXException("Expected 'property' tag");

    name = attrs.getValue(NAME_ATTR);
    if (name == null)
      throw new SAXException("Attribute 'name' is missing for tag 'property'.");

    buffer = new StringBuffer();
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    // todo: Parse the default entities
    // accumulate the characters in case the text is split into several chunks...
    if (this.buffer != null)
    {
      this.buffer.append (String.copyValueOf (ch, start, length));
    }
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(PROPERTY))
    {
      properties.setProperty(name, buffer.toString());
      name = null;
      buffer = null;
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Expected 'property' tag or '" + finishTag + "'. " + tagName);
    }
  }

  public Parser getParser()
  {
    return parser;
  }

  public Properties getProperties()
  {
    return properties;
  }
}
