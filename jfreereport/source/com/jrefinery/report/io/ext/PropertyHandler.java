/**
 * Date: Jan 10, 2003
 * Time: 5:11:51 PM
 *
 * $Id: PropertyHandler.java,v 1.2 2003/01/23 18:07:44 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Properties;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.util.CharacterEntityParser;

public class PropertyHandler implements ReportDefinitionHandler
{
  public static final String PROPERTY_TAG = "property";
  public static final String NAME_ATTR = "name";

  private Parser parser;
  private String finishTag;
  private Properties properties;
  private StringBuffer buffer = null;
  private String name;

  private CharacterEntityParser entityParser;

  public PropertyHandler (Parser parser, String finishTag)
  {
    entityParser = CharacterEntityParser.createXMLEntityParser();
    properties = new Properties();
    this.finishTag = finishTag;
    this.parser = parser;
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(PROPERTY_TAG) == false)
      throw new SAXException("Expected 'property' tag");

    name = attrs.getValue(NAME_ATTR);
    if (name == null)
      throw new SAXException("Attribute 'name' is missing for tag 'property'.");

    buffer = new StringBuffer();
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    // accumulate the characters in case the text is split into several chunks...
    if (this.buffer != null)
    {
      this.buffer.append (String.copyValueOf (ch, start, length));
    }
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(PROPERTY_TAG))
    {
      properties.setProperty(name, entityParser.decodeEntities(buffer.toString()));
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
