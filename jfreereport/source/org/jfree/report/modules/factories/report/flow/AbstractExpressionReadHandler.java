/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * AbstractExpressionReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AbstractExpressionReadHandler.java,v 1.1 2006/04/18 11:45:16 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.factories.report.flow;

import java.beans.IntrospectionException;

import org.jfree.report.function.Expression;
import org.jfree.xmlns.parser.AbstractXmlReadHandler;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.XmlReadHandler;
import org.jfree.report.util.CharacterEntityParser;
import org.jfree.report.util.beans.BeanUtility;
import org.jfree.util.ObjectUtilities;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 09.04.2006, 13:23:32
 *
 * @author Thomas Morgner
 */
public abstract class AbstractExpressionReadHandler
        extends AbstractXmlReadHandler
{
  private Expression expression;
  private BeanUtility expressionBeanUtility;
  private CharacterEntityParser characterEntityParser;

  public AbstractExpressionReadHandler()
  {
    this.characterEntityParser = CharacterEntityParser.createXMLEntityParser();
  }

  protected String getDefaultClassName()
  {
    return null;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing(final Attributes attrs) throws SAXException
  {

    String className = attrs.getValue(getUri(), "class");
    if (className == null)
    {
      className = getDefaultClassName();
      if (className == null)
      {
        throw new ParseException("Required attribute 'class' is missing.",
                getRootHandler().getDocumentLocator());
      }
    }

    try
    {
      final Class fnC = ObjectUtilities.getClassLoader
              (getClass()).loadClass(className);
      expression = (Expression) fnC.newInstance();
      expressionBeanUtility = new BeanUtility(expression);
    }
    catch (ClassNotFoundException e)
    {
      throw new ParseException("Expression '" + className +
              "' is not valid. The specified class was not found.",
              e, getRootHandler().getDocumentLocator());
    }
    catch (IllegalAccessException e)
    {
      throw new ParseException("Expression " + className +
              "' is not valid. The specified class does not define a public default constructor.",
              e, getRootHandler().getDocumentLocator());
    }
    catch (InstantiationException e)
    {
      throw new ParseException("Expression '" + className +
              "' is not valid. The specified class cannot be instantiated.",
              e, getRootHandler().getDocumentLocator());
    }
    catch (ClassCastException e)
    {
      throw new ParseException("Expression '" + className +
              "' is not valid. The specified class is not an expression or function.",
              e, getRootHandler().getDocumentLocator());
    }
    catch (IntrospectionException e)
    {
      throw new ParseException("Expression '" + className +
              "' is not valid. Introspection failed for this expression.",
              e, getRootHandler().getDocumentLocator());
    }

  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
          throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }
    if (tagName.equals("property"))
    {
      return new TypedPropertyReadHandler
              (expressionBeanUtility, expression.getName(),
                      characterEntityParser);
    }
    return null;
  }

  public Expression getExpression()
  {
    return expression;
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   * @throws XmlReaderException if there is a parsing error.
   */
  public Object getObject() throws SAXException
  {
    return expression;
  }
}
