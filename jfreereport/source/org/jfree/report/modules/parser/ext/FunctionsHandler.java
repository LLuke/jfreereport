/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ---------------------
 * FunctionsHandler.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionsHandler.java,v 1.6 2003/08/18 18:28:02 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import java.util.Iterator;

import org.jfree.report.function.Expression;
import org.jfree.report.function.Function;
import org.jfree.report.function.FunctionInitializeException;
import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A functions handler. Handles the initialization of Functions, Expressions
 * and Property-References.
 *
 * @author Thomas Morgner.
 */
public class FunctionsHandler extends AbstractExtReportParserHandler
{
  /** The name of the function tag. */
  public static final String FUNCTION_TAG = "function";

  /** The name of the expression tag. */
  public static final String EXPRESSION_TAG = "expression";

  /** The name of the 'property-ref' tag. */
  public static final String PROPERTY_REF_TAG = "property-ref";

  /** The property name. */
  private String propertyName;

  /** The expression handler. */
  private ExpressionHandler expressionHandler;

  /** The property reference handler. */
  private BasicObjectHandler propertyRefHandler;

  /**
   * Creates a new functions handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public FunctionsHandler(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(final String tagName, final Attributes attrs)
      throws SAXException
  {
    if (tagName.equals(EXPRESSION_TAG) || tagName.equals(FUNCTION_TAG))
    {
      final String className = attrs.getValue("class");
      if (className == null)
      {
        throw new ParseException("The attribute 'class' is missing for " + tagName,
            getParser().getLocator());
      }
      final String expName = attrs.getValue("name");
      if (expName == null)
      {
        throw new ParseException("The attribute 'name' is missing for" + tagName,
            getParser().getLocator());
      }
      final int depLevel = ParserUtil.parseInt(attrs.getValue("deplevel"), 0);

      final Expression e = loadExpression(className, expName, depLevel);
      CommentHintPath path = createPath(e);
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      expressionHandler = new ExpressionHandler(getReportParser(), tagName, e, path);
      getParser().pushFactory(expressionHandler);
    }
    else if (tagName.equals(PROPERTY_REF_TAG))
    {
      String className = attrs.getValue("class");
      if (className == null)
      {
        className = String.class.getName();
      }

      propertyName = attrs.getValue("name");
      if (propertyName == null)
      {
        throw new ParseException("The attribute 'name' is missing for the property-ref",
            getParser().getLocator());
      }
      final ObjectDescription od = loadObjectDescription(className);
      CommentHintPath path = createPath(propertyName);
      if (isBasicObject(od))
      {
        propertyRefHandler = new BasicObjectHandler(getReportParser(), tagName, od, path);
      }
      else
      {
        propertyRefHandler = new CompoundObjectHandler(getReportParser(), tagName, od, path);
      }
      addComment(path, CommentHandler.OPEN_TAG_COMMENT);
      getParser().pushFactory(propertyRefHandler);

    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + EXPRESSION_TAG + ", "
          + FUNCTION_TAG + ", "
          + PROPERTY_REF_TAG);
    }
  }

  /**
   * Loads an object description.
   *
   * @param className  the class name.
   *
   * @return The description.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  private ObjectDescription loadObjectDescription(final String className)
      throws SAXException
  {
    try
    {
      final Class propertyClass = getClass().getClassLoader().loadClass(className);
      final ClassFactoryCollector fc = (ClassFactoryCollector) getParser().getHelperObject(
          ParserConfigHandler.OBJECT_FACTORY_TAG);
      ObjectDescription retval = fc.getDescriptionForClass(propertyClass);
      if (retval == null)
      {
        retval = fc.getSuperClassObjectDescription(propertyClass, null);
      }
      return retval;
    }
    catch (Exception e)
    {
      throw new ParseException("Unable to load the given class.", e, getParser().getLocator());
    }
  }

  /**
   * Loads an expression.
   *
   * @param className  the class name.
   * @param expName  the expression name.
   * @param depLevel  the dependency level.
   *
   * @return The expression.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  private Expression loadExpression
    (final String className, final String expName, final int depLevel)
      throws SAXException
  {
    try
    {
      final Class fnC = getClass().getClassLoader().loadClass(className);
      final Expression retVal = (Expression) fnC.newInstance();
      retVal.setName(expName);
      retVal.setDependencyLevel(depLevel);
      return retVal;
    }
    catch (ClassNotFoundException e)
    {
      throw new ParseException("Expression " + expName + " class=" + className
          + " is not valid. ", e, getParser().getLocator());
    }
    catch (IllegalAccessException e)
    {
      throw new ParseException("Expression " + expName + " class=" + className
          + " is not valid. ", e, getParser().getLocator());
    }
    catch (InstantiationException e)
    {
      throw new ParseException("Expression " + expName + " class=" + className
          + " is not valid. ", e, getParser().getLocator());
    }
  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    // ignore ..
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(final String tagName)
      throws SAXException
  {
    if (tagName.equals(EXPRESSION_TAG))
    {
      try
      {
        Expression expression = expressionHandler.getExpression();
        getReport().addExpression(expression);
        addComment(createPath(expression), CommentHandler.CLOSE_TAG_COMMENT);
        expressionHandler = null;
      }
      catch (FunctionInitializeException fe)
      {
        expressionHandler = null;
        throw new ParseException("Unable to initialize function.", fe, getParser().getLocator());
      }
    }
    else if (tagName.equals(FUNCTION_TAG))
    {
      try
      {
        Function expression = (Function) expressionHandler.getExpression();
        getReport().addFunction(expression);
        addComment(createPath(expression.getName()), CommentHandler.CLOSE_TAG_COMMENT);
        expressionHandler = null;
      }
      catch (FunctionInitializeException fe)
      {
        expressionHandler = null;
        throw new ParseException("Unable to initialize function.", fe, getParser().getLocator());
      }
    }
    else if (tagName.equals(PROPERTY_REF_TAG))
    {
      addComment(createPath(propertyName), CommentHandler.CLOSE_TAG_COMMENT);
      getReport().setPropertyMarked(propertyName, true);
      final Object value = propertyRefHandler.getValue();
      if ("".equals(value) == false)
      {
        getReport().setProperty(propertyName, propertyRefHandler.getValue());
      }
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + EXPRESSION_TAG + ", "
          + FUNCTION_TAG + ", "
          + PROPERTY_REF_TAG + ", "
          + getFinishTag());
    }
  }

  /**
   * Returns <code>true</code> for...
   *
   * @param od  the object description.
   *
   * @return A boolean.
   */
  private boolean isBasicObject(final ObjectDescription od)
  {
    final Iterator odNames = od.getParameterNames();
    if (odNames.hasNext() == false)
    {
      return false;
    }
    final String param = (String) odNames.next();
    if (odNames.hasNext() == true)
    {
      return false;
    }
    if (param.equals("value"))
    {
      if (od.getParameterDefinition("value").equals(String.class))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Creates a comment hint path for the given object.
   *
   * @param target the target object that should be the last segment in
   * the path.
   * @return the created path object.
   */
  private CommentHintPath createPath (Object target)
  {
    CommentHintPath path = new CommentHintPath();
    path.addName(ExtParserModuleInit.REPORT_DEFINITION_TAG);
    path.addName(ExtReportHandler.FUNCTIONS_TAG);
    path.addName(target);
    return path;
  }
}

