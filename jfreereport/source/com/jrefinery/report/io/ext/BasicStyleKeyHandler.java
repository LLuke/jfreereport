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
 * -------------------------
 * BasicStyleKeyHandler.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BasicStyleKeyHandler.java,v 1.7 2003/02/24 17:33:56 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactory;
import com.jrefinery.report.targets.style.StyleKey;
import com.jrefinery.report.util.CharacterEntityParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A basic style key handler. Basic stylekeys simplify the description of
 * simple data types, like Float, String, Integer etc..
 * <p>
 * Simple data types only have one string property, which is called "value".
 * The property value is parsed by the ObjectDescription object to create
 * the object.
 *
 * @author Thomas Morgner.
 */
public class BasicStyleKeyHandler implements ReportDefinitionHandler
{
  /** The finish tag. */
  private String finishTag;

  /** The parser. */
  private Parser parser;

  /** A buffer. */
  private StringBuffer buffer;

  /** The style key factory. */
  private StyleKeyFactory keyfactory;

  /** The style key. */
  private StyleKey key;

  /** The key value class. */
  private Class keyValueClass;

  /** A character entity parser. */
  private CharacterEntityParser entityParser;

  /**
   * Creates a new BasicStyleKeyHandler handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param name  the name.
   * @param c  the class.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public BasicStyleKeyHandler(Parser parser, String finishTag, String name, Class c)
    throws SAXException
  {
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
    this.parser = parser;
    this.finishTag = finishTag;
    this.buffer = new StringBuffer();
    keyfactory = (StyleKeyFactory)
        getParser().getConfigurationValue(ParserConfigHandler.STYLEKEY_FACTORY_TAG);
    key = keyfactory.getStyleKey(name);
    if (key == null)
    {
      throw new SAXException("The defined StyleKey is invalid: " + name);
    }
    if (c == null)
    {
      this.keyValueClass = key.getValueType();
    }
    else
    {
      this.keyValueClass = c;
    }
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
    throw new SAXException("Element '" + finishTag + "' has no child-elements.");
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
    buffer.append(ch, start,  length);
  }

  /**
   * Returns the style key factory.
   *
   * @return The style key factory.
   */
  public StyleKeyFactory getKeyfactory()
  {
    return keyfactory;
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
    if (tagName.equals(finishTag) == false)
    {
      throw new SAXException("Expected tag '" + finishTag + "'");
    }
    getParser().popFactory().endElement(tagName);
  }

  /**
   * Returns the style key.
   *
   * @return The style key.
   */
  public StyleKey getStyleKey ()
  {
    return key;
  }

  /**
   * Returns the value.
   *
   * @return The value.
   */
  public Object getValue ()
  {
    return keyfactory.createBasicObject(key, entityParser.decodeEntities(buffer.toString()),
                                        keyValueClass);
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
   * Returns the finish tag.
   *
   * @return The finish tag.
   */
  protected String getFinishTag()
  {
    return finishTag;
  }

  /**
   * Returns the key value class.
   *
   * @return The class.
   */
  public Class getKeyValueClass()
  {
    return keyValueClass;
  }
}
