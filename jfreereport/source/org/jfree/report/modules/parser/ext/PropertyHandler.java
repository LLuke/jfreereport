/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: PropertyHandler.java,v 1.5 2003/08/18 18:28:02 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import java.util.Properties;

import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.util.CharacterEntityParser;
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
public class PropertyHandler extends AbstractExtReportParserHandler
{
  /** The 'property' tag name. */
  public static final String PROPERTY_TAG = "property";

  /** The 'name' attribute text. */
  public static final String NAME_ATTR = "name";

  /** The properties. */
  private Properties properties;

  /** The string buffer. */
  private StringBuffer buffer = null;

  /** The name. */
  private String name;

  /** The character entity parser. */
  private CharacterEntityParser entityParser;

  /** The base of the comment hint path. */
  private CommentHintPath base;

  /**
   * Creates a new property handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param base the base comment hint path to store the extra parser information.
   */
  public PropertyHandler(final ReportParser parser, final String finishTag,
                         final CommentHintPath base)
  {
    super(parser, finishTag);
    entityParser = CharacterEntityParser.createXMLEntityParser();
    properties = new Properties();
    if (base == null)
    {
      throw new NullPointerException("CommentHint base must not be null.");
    }
    this.base = base;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(final String tagName, final Attributes attrs) throws SAXException
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

    CommentHintPath path = base.getInstance();
    path.addName(name);
    getReport().getReportBuilderHints().putHint
        (path, CommentHandler.OPEN_TAG_COMMENT, getReportParser().getComments());
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
  public void characters(final char[] ch, final int start, final int length) throws SAXException
  {
    // accumulate the characters in case the text is split into several chunks...
    if (this.buffer != null)
    {
      this.buffer.append(String.copyValueOf(ch, start, length));
    }
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(final String tagName) throws SAXException
  {
    if (tagName.equals(PROPERTY_TAG))
    {
      properties.setProperty(name, entityParser.decodeEntities(buffer.toString()));
      name = null;
      buffer = null;
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new ParseException("Expected 'property' tag or '" + getFinishTag() + "'. " + tagName,
          getParser().getLocator());
    }
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
