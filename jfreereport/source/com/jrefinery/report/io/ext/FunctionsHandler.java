/**
 * Date: Jan 9, 2003
 * Time: 9:08:15 PM
 *
 * $Id: FunctionsHandler.java,v 1.2 2003/01/22 19:38:24 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.function.Expression;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.io.InitialReportHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ParserUtil;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryCollector;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Iterator;

public class FunctionsHandler implements ReportDefinitionHandler
{
  public static final String FUNCTION_TAG = "function";
  public static final String EXPRESSION_TAG = "expression";
  public static final String PROPERTY_REF_TAG = "property-ref";

  private Parser parser;
  private String finishTag;
  private String propertyName;
  private ExpressionHandler expressionHandler;
  private BasicObjectHandler propertyRefHandler;

  public FunctionsHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
  }

  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(EXPRESSION_TAG))
    {
      String className = attrs.getValue("class");
      if (className == null)
        throw new SAXException("The attribute 'class' is missing for expression");

      String expName = attrs.getValue("name");
      if (expName == null)
        throw new SAXException("The attribute 'name' is missing for expression");

      int depLevel = ParserUtil.parseInt(attrs.getValue("deplevel"), 0);

      Expression e = loadExpression(className, expName, depLevel);
      expressionHandler = new ExpressionHandler(getParser(), tagName, e);
      getParser().pushFactory(expressionHandler);
    }
    else if (tagName.equals(FUNCTION_TAG))
    {
      String className = attrs.getValue("class");
      if (className == null)
        throw new SAXException("The attribute 'class' is missing for function");

      String expName = attrs.getValue("name");
      if (expName == null)
        throw new SAXException("The attribute 'name' is missing for function");

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
        throw new SAXException("The attribute 'name' is missing for the property-ref");

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
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              EXPRESSION_TAG + ", " +
                              FUNCTION_TAG + ", " +
                              PROPERTY_REF_TAG + ", " +
                              finishTag);
    }
  }

  private ObjectDescription loadObjectDescription (String className)
    throws SAXException
  {
    try
    {
      Class propertyClass = getClass().getClassLoader().loadClass(className);
      ClassFactoryCollector fc =
          (ClassFactoryCollector) getParser().getConfigurationValue(ParserConfigHandler.OBJECT_FACTORY_TAG);
      return fc.getDescriptionForClass(propertyClass);
    }
    catch (Exception e)
    {
      throw new SAXException("Unable to load the given class.");
    }
  }

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

  public void characters(char ch[], int start, int length)
  {
    // ignore ..
  }

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
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              EXPRESSION_TAG + ", " +
                              FUNCTION_TAG + ", " +
                              PROPERTY_REF_TAG + ", " +
                              finishTag);
    }
  }

  public Parser getParser()
  {
    return parser;
  }

  private JFreeReport getReport ()
  {
    return (JFreeReport) getParser().getConfigurationValue(InitialReportHandler.REPORT_DEFINITION_TAG);
  }

 private boolean isBasicObject(ObjectDescription od)
  {
    Iterator odNames = od.getParameterNames();
    if (odNames.hasNext() == false)
      return false;

    String param = (String) odNames.next();
    if (odNames.hasNext() == true)
      return false;

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

