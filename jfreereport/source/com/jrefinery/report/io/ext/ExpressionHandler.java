package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.jrefinery.report.function.Expression;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;

/**
 * The functionFactory creates functions and adds these functions to the FunctionCollection
 * of the current report.
 *
 * @author Thomas Morgner
 */
public class ExpressionHandler implements ReportDefinitionHandler
{
  public static final String PROPERTIES_TAG = "properties";

  private Parser parser;
  private String finishTag;
  private PropertyHandler propertyHandler;
  private Expression expression;

  public ExpressionHandler(Parser parser, String finishTag, Expression expression)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    this.expression = expression;
  }

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

  public void characters(char ch[], int start, int length) throws SAXException
  {
    // ignore ...
  }

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

  public Parser getParser()
  {
    return parser;
  }

  public Expression getExpression()
  {
    return expression;
  }
}

