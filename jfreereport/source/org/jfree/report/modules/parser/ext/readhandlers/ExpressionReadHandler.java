package org.jfree.report.modules.parser.ext.readhandlers;

import java.beans.IntrospectionException;

import org.jfree.report.function.Expression;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ExpressionReadHandler extends AbstractXmlReadHandler
{
  /** The dependency level attribute. */
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
  protected void startParsing (final Attributes attrs)
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
    catch(ClassCastException e)
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
                                               final Attributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("properties"))
    {
      try
      {
        return new org.jfree.report.modules.parser.simple.readhandlers.ExpressionPropertiesReadHandler(expression);
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
}
