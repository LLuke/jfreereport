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
 * -----------------------
 * BasicObjectHandler.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BasicObjectHandler.java,v 1.11 2003/05/27 08:31:56 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.util.CharacterEntityParser;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A basic object handler. Basic objects simplify the description of
 * simple data types, like Float, String, Integer etc..
 * <p>
 * Simple data types only have one string property, which is called "value".
 * The property value is parsed by the ObjectDescription object to create
 * the object.
 *
 * @author Thomas Morgner.
 */
public class BasicObjectHandler implements ElementDefinitionHandler
{
  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /** A buffer. */
  private StringBuffer buffer;

  /** An object description. */
  private ObjectDescription objectDescription;

  /** A character entity parser. */
  private CharacterEntityParser entityParser;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param od  the object description.
   */
  public BasicObjectHandler(Parser parser, String finishTag, ObjectDescription od)
  {
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
    this.parser = parser;
    this.finishTag = finishTag;
    this.buffer = new StringBuffer();
    this.objectDescription = od;
  }

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param targetObject  the class.
   *
   * @throws SAXException  if a parser error occurs.
   */
  public BasicObjectHandler(Parser parser, String finishTag, Class targetObject)
    throws SAXException
  {
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
    this.parser = parser;
    this.finishTag = finishTag;
    this.buffer = new StringBuffer();

    ClassFactory fact = (ClassFactory) getParser().getHelperObject(
        ParserConfigHandler.OBJECT_FACTORY_TAG);
    objectDescription = fact.getDescriptionForClass(targetObject);
    if (objectDescription == null)
    {
      objectDescription = fact.getSuperClassObjectDescription(targetObject, null);
    }
    if (objectDescription == null)
    {
      throw new SAXException("No object definition for class " + targetObject);
    }
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException as the basic object tag has no child elements.
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
   * @throws SAXException if an parser error occurs.
   */
  public void characters(char ch[], int start, int length) throws SAXException
  {
    buffer.append(ch, start,  length);
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
    ObjectDescription od = getTargetObjectDescription();
    od.setParameter("value", entityParser.decodeEntities(buffer.toString()));
    getParser().popFactory().endElement(tagName);
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
   * Returns the object created by the handler.
   *
   * @return The object.
   */
  public Object getValue ()
  {
    return getTargetObjectDescription().createObject();
  }

  /**
   * Returns the target object description.
   *
   * @return The object description.
   */
  protected ObjectDescription getTargetObjectDescription ()
  {
    return objectDescription;
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
}
