/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * --------------------
 * PropertyHandler.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PropertyHandler.java,v 1.10 2003/05/02 12:40:06 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import java.util.Properties;

import com.jrefinery.report.util.CharacterEntityParser;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.jfree.xml.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A property handler. Handles the defintion of properties. The defined properties
 * are collected in a java.util.Properties object and returned when the finishTag
 * was reached on endElement.
 *
 * @author Thomas Morgner.
 */
public class PropertyHandler implements ElementDefinitionHandler
{
  /** The 'property' tag name. */
  public static final String PROPERTY_TAG = "property";

  /** The 'name' attribute text. */
  public static final String NAME_ATTR = "name";

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /** The properties. */
  private Properties properties;

  /** The string buffer. */
  private StringBuffer buffer = null;

  /** The name. */
  private String name;

  /** The character entity parser. */
  private CharacterEntityParser entityParser;

  /**
   * Creates a new property handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public PropertyHandler (Parser parser, String finishTag)
  {
    entityParser = CharacterEntityParser.createXMLEntityParser();
    properties = new Properties();
    this.finishTag = finishTag;
    this.parser = parser;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(PROPERTY_TAG) == false)
    {
      throw new SAXException("Expected 'property' tag");
    }
    name = attrs.getValue(NAME_ATTR);
    if (name == null)
    {
      throw new ParseException("Attribute 'name' is missing for tag 'property'.",
          getParser().getLocator());
    }
    buffer = new StringBuffer();
  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void characters(char ch[], int start, int length) throws SAXException
  {
    // accumulate the characters in case the text is split into several chunks...
    if (this.buffer != null)
    {
      this.buffer.append (String.copyValueOf (ch, start, length));
    }
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
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
      throw new ParseException("Expected 'property' tag or '" + finishTag + "'. " + tagName,
          getParser().getLocator());
    }
  }

  /**
   * Returns the parser.
   *
   * @return The parser.
   */
  public Parser getParser()
  {
    return parser;
  }

  /**
   * Returns the properties.
   *
   * @return The properties.
   */
  public Properties getProperties()
  {
    return properties;
  }
}
