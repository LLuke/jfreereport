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
 * ----------------------
 * ExpressionHandler.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExpressionHandler.java,v 1.5 2003/02/25 12:48:19 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.function.Expression;
import com.jrefinery.xml.ElementDefinitionHandler;
import com.jrefinery.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An expression handler. Handles the creation of a single expression or
 * function.
 *
 * @see Expression
 * @see com.jrefinery.report.function.Function
 * @author Thomas Morgner
 */
public class ExpressionHandler implements ElementDefinitionHandler
{
  /** The properties tag name. */
  public static final String PROPERTIES_TAG = "properties";

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /** The property handler. */
  private PropertyHandler propertyHandler;

  /** The expression. */
  private Expression expression;

  /**
   * Creates a new expression handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param expression  the expression.
   */
  public ExpressionHandler(Parser parser, String finishTag, Expression expression)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    this.expression = expression;
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
    if (tagName.equals(PROPERTIES_TAG))
    {
      propertyHandler = new PropertyHandler(getParser(), tagName);
      getParser().pushFactory(propertyHandler);
    }
    else
    {
      throw new SAXException("Expected 'properties' tag");
    }
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
    // ignore ...
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
    if (tagName.equals(PROPERTIES_TAG))
    {
      expression.setProperties(propertyHandler.getProperties());
      propertyHandler = null;
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Expected 'properties' or '" + finishTag + "'");
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
   * Returns the expression.
   *
   * @return The expression.
   */
  public Expression getExpression()
  {
    return expression;
  }
}

