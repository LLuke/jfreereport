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
 * ----------------------
 * ExpressionHandler.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExpressionHandler.java,v 1.5 2003/08/18 18:28:02 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.function.Expression;
import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An expression handler. Handles the creation of a single expression or
 * function.
 *
 * @see Expression
 * @see org.jfree.report.function.Function
 * @author Thomas Morgner
 */
public class ExpressionHandler extends AbstractExtReportParserHandler
{
  /** The properties tag name. */
  public static final String PROPERTIES_TAG = "properties";

  /** The property handler. */
  private PropertyHandler propertyHandler;

  /** The expression that should be processed. */
  private Expression expression;

  /** The comment hint path that should be used to store additional parser information. */
  private CommentHintPath path;
  
  /**
   * Creates a new expression handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param expression  the expression.
   * @param base the comment hint path that should be used to store additional xml 
   * informations for the writer.
   */
  public ExpressionHandler(final ReportParser parser, final String finishTag,
                           final Expression expression, final CommentHintPath base)
  {
    super(parser, finishTag);
    if (expression == null)
    {
      throw new NullPointerException("Expression must not be null.");
    }
    if (base == null)
    {
      throw new NullPointerException("CommentHintBase must not be null");
    }
    this.expression = expression;
    this.path = base;
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
    if (tagName.equals(PROPERTIES_TAG))
    {
      CommentHintPath path = this.path.getInstance();
      path.addName(tagName);
      getReport().getReportBuilderHints().putHint
          (path, CommentHandler.OPEN_TAG_COMMENT, getReportParser().getComments());
      propertyHandler = new PropertyHandler(getReportParser(), tagName, path);
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
  public void characters(final char[] ch, final int start, final int length) throws SAXException
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
  public void endElement(final String tagName) throws SAXException
  {
    if (tagName.equals(PROPERTIES_TAG))
    {
      CommentHintPath path = this.path.getInstance();
      path.addName(tagName);
      getReport().getReportBuilderHints().putHint
          (path, CommentHandler.CLOSE_TAG_COMMENT, getReportParser().getComments());
      expression.setProperties(propertyHandler.getProperties());
      propertyHandler = null;
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Expected 'properties' or '" + getFinishTag() + "'");
    }
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

