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
 * -----------------------
 * BasicObjectHandler.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BasicObjectHandler.java,v 1.6 2003/08/24 15:08:20 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.util.CharacterEntityParser;
import org.jfree.xml.ParseException;
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
public class BasicObjectHandler extends AbstractExtReportParserHandler
{
  /** A buffer to store CDATA. */
  private final StringBuffer buffer;

  /** An object description of the to be generated object. */
  private final ObjectDescription objectDescription;

  /** A character entity parser to resolve CDATA entities. */
  private final CharacterEntityParser entityParser;

  /**
   * The comment hint path is used to store xml comments in the
   * report builder hints collection.
   */
  private final CommentHintPath commentKey;


  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param od  the object description of the target object.
   * @param commentHintPath the path on where to search for ext-parser comments
   * in the report builder hints.
   */
  public BasicObjectHandler(final ReportParser parser, final String finishTag,
                            final ObjectDescription od, final CommentHintPath commentHintPath)
  {
    super(parser, finishTag);
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
    this.buffer = new StringBuffer();
    this.objectDescription = od;
    this.commentKey = commentHintPath;
  }

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param targetObject  the object type that should be created by this handler.
   * @param commentHintPath the path on where to search for ext-parser comments
   * in the report builder hints.
   *
   * @throws SAXException  if a parser error occurs.
   */
  public BasicObjectHandler(final ReportParser parser, final String finishTag,
                            final Class targetObject, final CommentHintPath commentHintPath)
      throws SAXException
  {
    this(parser, finishTag, createObjectDescriptionFromObject(parser, targetObject),
        commentHintPath);
  }

  /**
   * Tries to find a matching object description for the given class.
   *
   * @param parser the current report parser which holds all factories.
   * @param targetObject the target object class that should be built.
   * @return the object description for the target object, never null.
   * @throws ParseException if no object description was found for this
   * object type.
   */
  private static ObjectDescription createObjectDescriptionFromObject
      (final ReportParser parser, final Class targetObject)
      throws ParseException
  {
    final ClassFactory fact = (ClassFactory) parser.getHelperObject(
        ParserConfigHandler.OBJECT_FACTORY_TAG);
    ObjectDescription objectDescription = fact.getDescriptionForClass(targetObject);
    if (objectDescription == null)
    {
      objectDescription = fact.getSuperClassObjectDescription(targetObject, null);
    }
    if (objectDescription == null)
    {
      throw new ParseException("No object definition for class " + targetObject,
          parser.getLocator());
    }
    return objectDescription;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException as the basic object tag has no child elements.
   */
  public void startElement(final String tagName, final Attributes attrs) throws SAXException
  {
    throw new SAXException("Element '" + getFinishTag() + "' has no child-elements.");
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
  public void characters(final char[] ch, final int start, final int length) throws SAXException
  {
    buffer.append(ch, start, length);
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
    if (tagName.equals(getFinishTag()) == false)
    {
      throw new SAXException("Expected tag '" + getFinishTag() + "'");
    }
    final ObjectDescription od = getTargetObjectDescription();
    od.setParameter("value", entityParser.decodeEntities(buffer.toString()));
    getParser().popFactory().endElement(tagName);
  }

  /**
   * Returns the object created by the handler.
   *
   * @return The object.
   */
  public Object getValue()
  {
    return getTargetObjectDescription().createObject();
  }

  /**
   * Returns the target object description.
   *
   * @return The object description.
   */
  protected ObjectDescription getTargetObjectDescription()
  {
    return objectDescription;
  }

  /**
   * Returns the comment hint path used in this factory. This path
   * is used to mark the parse position in the report builder hints.
   *
   * @return the comment hint path.
   */
  protected CommentHintPath getCommentKey()
  {
    return commentKey;
  }

  /**
   * Creates a new comment hint path for the given name by appending
   * it to a copy of the current path.
   *
   * @param name the name of the new path segment.
   * @return the new comment path.
   */
  protected CommentHintPath createCommentKey(final Object name)
  {
    final CommentHintPath path = commentKey.getInstance();
    path.addName(name);
    return path;
  }
}
