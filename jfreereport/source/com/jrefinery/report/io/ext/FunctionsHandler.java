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
 * ---------------------
 * FunctionsHandler.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionsHandler.java,v 1.9 2003/05/02 12:40:03 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import java.util.Iterator;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.function.Expression;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.io.InitialReportHandler;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
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
public class FunctionsHandler implements ElementDefinitionHandler
{
  /** The name of the function tag. */
  public static final String FUNCTION_TAG = "function";

  /** The name of the expression tag. */
  public static final String EXPRESSION_TAG = "expression";

  /** The name of the 'property-ref' tag. */
  public static final String PROPERTY_REF_TAG = "property-ref";

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

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
  public FunctionsHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(EXPRESSION_TAG))
    {
      String className = attrs.getValue("class");
      if (className == null)
      {
        throw new SAXException("The attribute 'class' is missing for expression");
      }
      String expName = attrs.getValue("name");
      if (expName == null)
      {
        throw new SAXException("The attribute 'name' is missing for expression");
      }
      int depLevel = ParserUtil.parseInt(attrs.getValue("deplevel"), 0);

      Expression e = loadExpression(className, expName, depLevel);
      expressionHandler = new ExpressionHandler(getParser(), tagName, e);
      getParser().pushFactory(expressionHandler);
    }
    else if (tagName.equals(FUNCTION_TAG))
    {
      String className = attrs.getValue("class");
      if (className == null)
      {
        throw new SAXException("The attribute 'class' is missing for function");
      }
      String expName = attrs.getValue("name");
      if (expName == null)
      {
        throw new SAXException("The attribute 'name' is missing for function");
      }
      int depLevel = ParserUtil.parseInt(attrs.getValue("deplevel"), 0);

      Expression e = loadExpression(className, expName, depLevel);
      expressionHandler = new ExpressionHandler(getParser(), tagName, e);
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
        throw new SAXException("The attribute 'name' is missing for the property-ref");
      }
      ObjectDescription od = loadObjectDescription(className);
      if (isBasicObject(od))
      {
        propertyRefHandler = new BasicObjectHandler(getParser(), tagName, od);
      }
      else
      {
        propertyRefHandler  = new CompoundObjectHandler(getParser(), tagName, od);
      }
      getParser().pushFactory(propertyRefHandler);

    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + EXPRESSION_TAG + ", "
                              + FUNCTION_TAG + ", "
                              + PROPERTY_REF_TAG + ", "
                              + finishTag);
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
  private ObjectDescription loadObjectDescription (String className)
    throws SAXException
  {
    try
    {
      Class propertyClass = getClass().getClassLoader().loadClass(className);
      ClassFactoryCollector fc = (ClassFactoryCollector) getParser().getConfigurationValue(
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
      throw new SAXException("Unable to load the given class.");
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
  private Expression loadExpression (String className, String expName, int depLevel)
    throws SAXException
  {
    try
    {
      Class fnC = getClass().getClassLoader().loadClass(className);
      Expression retVal = (Expression) fnC.newInstance ();
      retVal.setName (expName);
      retVal.setDependencyLevel(depLevel);
      return retVal;
    }
    catch (ClassNotFoundException e)
    {
      throw new SAXException ("Expression " + expName + " class=" + className
                            + " is not valid: ClassNotFound: " + e.getMessage ());
    }
    catch (IllegalAccessException e)
    {
      throw new SAXException ("Expression " + expName + " class=" + className
                            + " is not valid: IllegalAccess: " + e.getMessage ());
    }
    catch (InstantiationException e)
    {
      throw new SAXException ("Expression " + expName + " class=" + className
                            + " is not valid: Instantiation: " + e.getMessage ());
    }
  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(char ch[], int start, int length)
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
  public void endElement(String tagName)
    throws SAXException
  {
    if (tagName.equals(EXPRESSION_TAG))
    {
      try
      {
        getReport().addExpression(expressionHandler.getExpression());
        expressionHandler = null;
      }
      catch (FunctionInitializeException fe)
      {
        expressionHandler = null;
        throw new SAXException("Unable to initialize function." , fe);
      }
    }
    else if (tagName.equals(FUNCTION_TAG))
    {
      try
      {
        getReport().addFunction((Function) expressionHandler.getExpression());
        expressionHandler = null;
      }
      catch (FunctionInitializeException fe)
      {
        expressionHandler = null;
        throw new SAXException("Unable to initialize function." , fe);
      }
    }
    else if (tagName.equals(PROPERTY_REF_TAG))
    {
      getReport().setPropertyMarked(propertyName, true);
      getReport().setProperty(propertyName, propertyRefHandler.getValue());
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(finishTag);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + EXPRESSION_TAG + ", "
                              + FUNCTION_TAG + ", "
                              + PROPERTY_REF_TAG + ", "
                              + finishTag);
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
   * Returns the report.
   *
   * @return The report.
   */
  private JFreeReport getReport ()
  {
    return (JFreeReport) getParser().getConfigurationValue(
        InitialReportHandler.REPORT_DEFINITION_TAG);
  }

  /**
   * Returns <code>true</code> for...
   *
   * @param od  the object description.
   *
   * @return A boolean.
   */
  private boolean isBasicObject(ObjectDescription od)
  {
    Iterator odNames = od.getParameterNames();
    if (odNames.hasNext() == false)
    {
      return false;
    }
    String param = (String) odNames.next();
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

}

