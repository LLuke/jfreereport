/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ExpressionReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.base.common;

import java.beans.IntrospectionException;

import org.jfree.report.function.Expression;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ExpressionReadHandler extends AbstractPropertyXmlReadHandler
{
  /**
   * The dependency level attribute.
   */
  public static final String DEPENCY_LEVEL_ATT = "deplevel";

  private Expression expression;

  public ExpressionReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    final int depLevel = ParserUtil.parseInt(attrs.getValue(DEPENCY_LEVEL_ATT), 0);
    final String expressionName = attrs.getValue("name");
    if (expressionName == null)
    {
      throw new ElementDefinitionException("Required attribute 'name' is missing.");
    }

    final String className = attrs.getValue("class");
    if (className == null)
    {
      throw new ElementDefinitionException("Required attribute 'class' is missing.");
    }

    try
    {
      final Class fnC = ObjectUtilities.getClassLoader(getClass()).loadClass(className);
      expression = (Expression) fnC.newInstance();
      expression.setName(expressionName);
      expression.setDependencyLevel(depLevel);
    }
    catch (ClassNotFoundException e)
    {
      throw new ParseException("Expression '" + className +
              "' is not valid. The specified class was not found.",
              e, getRootHandler().getLocator());
    }
    catch (IllegalAccessException e)
    {
      throw new ParseException("Expression " + className +
              "' is not valid. The specified class does not define a public default constructor.",
              e, getRootHandler().getLocator());
    }
    catch (InstantiationException e)
    {
      throw new ParseException("Expression '" + className +
              "' is not valid. The specified class cannot be instantiated.",
              e, getRootHandler().getLocator());
    }
    catch (ClassCastException e)
    {
      throw new ParseException("Expression '" + className +
              "' is not valid. The specified class is not an expression or function.",
              e, getRootHandler().getLocator());
    }

  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild (final String tagName,
                                               final PropertyAttributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("properties"))
    {
      try
      {
        return new ExpressionPropertiesReadHandler(expression);
      }
      catch (IntrospectionException e)
      {
        throw new XmlReaderException
                ("Unable to create Introspector for the specified expression.");
      }
    }
    return null;
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return expression;
  }

  protected void storeComments ()
          throws SAXException
  {
    defaultStoreComments(new CommentHintPath(expression));
  }
}
