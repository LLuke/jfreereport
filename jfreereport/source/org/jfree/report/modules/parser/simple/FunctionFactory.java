/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * --------------------
 * FunctionFactory.java
 * --------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: FunctionFactory.java,v 1.9 2003/12/06 17:15:21 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 23-May-2002 : Rewrite and better structured, divided into several start* & end* methods
 * 08-Jun-2002 : Documentation
 * 19-Aug-2002 : Added support for expressions
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.modules.parser.simple;

import java.util.Properties;

import org.jfree.report.function.Expression;
import org.jfree.report.function.Function;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.util.CharacterEntityParser;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The functionFactory creates functions and adds these functions to the FunctionCollection
 * of the current report.
 *
 * @author Thomas Morgner
 */
public class FunctionFactory extends AbstractReportDefinitionHandler implements ReportDefinitionTags
{
  /** A constant defining the XML attribute name for the class specification. */
  public static final String CLASS_ATT = "class";

  /** The current function/expression. */
  private Expression currentFunction;

  /** The report properties. */
  private Properties currentProperties;

  /** The current text. */
  private StringBuffer currentText;

  /** The current property. */
  private String currentProperty;

  /** The current encoding. */
  private String currentEncoding;

  /** A character entity parser. */
  private final CharacterEntityParser entityParser;

  /**
   * Creates a new function handler.
   *
   * @param parser the used parser to coordinate the parsing process.
   * @param finishTag the finish tag, that should trigger the deactivation of this parser.
   * @throws NullPointerException if the finishTag or the parser are null.
   */
  public FunctionFactory(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
    entityParser = CharacterEntityParser.createXMLEntityParser();
  }

  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * StartTag-occurences of function definitions get handled by this factory. If an unknown
   * tag is encountered, a SAXException is thrown.
   *
   * @param qName  the element name.
   * @param atts  the attributes.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
  public void startElement(final String qName,
                           final Attributes atts) throws SAXException
  {
    final String elementName = qName.toLowerCase().trim();
    if (elementName.equals(FUNCTION_TAG))
    {
      startFunction(atts);
    }
    else if (elementName.equals(FUNCTIONS_TAG))
    {
      // do nothing
    }
    else if (elementName.equals(PROPERTIES_TAG))
    {
      startProperties(atts);
    }
    else if (elementName.equals(PROPERTY_TAG))
    {
      startProperty(atts);
    }
    else if (elementName.equals(DATAREF_TAG))
    {
      // data ref is ignored, was specified some times ago, but never
      // implemented and is now obsolete
    }
    else if (elementName.equals(EXPRESSION_TAG))
    {
      startExpression(atts);
    }
    else if (elementName.equals(PROPERTY_REFERENCE_TAG))
    {
      startPropertyRef(atts);
    }
    else
    {
      throw new SAXException("Expected one of 'function', 'functions', 'data-ref', 'properties', "
          + "'property' tag");
    }
  }

  /**
   * Returns the current properties bundle for the function that is currently created.
   *
   * @return the function/expression properties.
   */
  protected Properties getProperties()
  {
    return currentProperties;
  }

  /**
   * Sets the properties for the current function.
   *
   * @param p  the properties.
   */
  protected void setProperties(final Properties p)
  {
    this.currentProperties = p;
  }

  /**
   * Returns the function under construction.
   *
   * @return the function just built (or under construction).
   */
  protected Function getCurrentFunction()
  {
    return (Function) currentFunction;
  }

  /**
   * Defines the current function. This function gets properties set and is then added
   * to the report's function collection.
   *
   * @param function  the function.
   */
  protected void setCurrentFunction(final Function function)
  {
    this.currentFunction = function;
  }

  /**
   * Returns the current expression.
   *
   * @return the expression just built (or under construction).
   */
  protected Expression getCurrentExpression()
  {
    return currentFunction;
  }

  /**
   * Sets the current expression. This expression gets properties set and is then added
   * to the reports expression collection.
   *
   * @param function  the expression.
   */
  protected void setCurrentExpression(final Expression function)
  {
    this.currentFunction = function;
  }

  /**
   * Starts the Properties tag to create a new property bundle for a function.
   *
   * @param atts  the element attributes.
   */
  protected void startProperties(final Attributes atts)
  {
    setProperties(new Properties());
  }

  /**
   * Starts a new property entry for the current function.
   *
   * @param atts  the element attributes.
   */
  protected void startProperty(final Attributes atts)
  {
    currentProperty = atts.getValue(NAME_ATT);
    currentEncoding = atts.getValue(PROPERTY_ENCODING_ATT);
    if (currentEncoding == null)
    {
      currentEncoding = PROPERTY_ENCODING_TEXT;
    }
    currentText = new StringBuffer();
  }

  /**
   * Starts processing an expression element.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is an error parsing the XML.
   */
  protected void startExpression(final Attributes attr)
      throws SAXException
  {
    final String name = getNameGenerator().generateName(attr.getValue(NAME_ATT));
    final String className = attr.getValue(CLASS_ATT);
    final int depLevel = ParserUtil.parseInt(attr.getValue(DEPENCY_LEVEL_ATT), 0);

    if (className == null)
    {
      throw new ParseException("Expression class not specified", getLocator());
    }

    try
    {
      final Class fnC = getClass().getClassLoader().loadClass(className);
      setCurrentExpression((Expression) fnC.newInstance());
      getCurrentExpression().setName(name);
      getCurrentExpression().setDependencyLevel(depLevel);
    }
    catch (ClassNotFoundException e)
    {
      throw new ParseException("Expression " + name + " class=" + className
          + " is not valid. ", e, getLocator());
    }
    catch (IllegalAccessException e)
    {
      throw new ParseException("Expression " + name + " class=" + className
          + " is not valid. ", e, getLocator());
    }
    catch (InstantiationException e)
    {
      throw new ParseException("Expression " + name + " class=" + className
          + " is not valid. ", e, getLocator());
    }
  }


  /**
   * Starts processing an expression element.
   *
   * @param attr  the element attributes.
   * @throws SAXException if the name attribute is missing.
   */
  protected void startPropertyRef(final Attributes attr) throws SAXException
  {
    currentProperty = attr.getValue(NAME_ATT);
    if (currentProperty == null)
    {
      throw new SAXException("Required attribute 'name' missing.");
    }
    currentEncoding = attr.getValue(PROPERTY_ENCODING_ATT);
    if (currentEncoding == null)
    {
      currentEncoding = PROPERTY_ENCODING_TEXT;
    }
    currentText = new StringBuffer();
  }

  /**
   * starts and loads a function by instantating the functions class. The function must
   * have a default constructor defined.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is an error parsing the XML.
   */
  protected void startFunction(final Attributes attr)
      throws SAXException
  {
    final String name = attr.getValue(NAME_ATT);
    if (name == null)
    {
      throw new SAXException("Required attribute 'name' missing.");
    }
    final String className = attr.getValue(CLASS_ATT);
    final int depLevel = ParserUtil.parseInt(attr.getValue(DEPENCY_LEVEL_ATT), 0);

    if (className == null)
    {
      throw new ParseException("Function class not specified", getLocator());
    }

    try
    {
      final Class fnC = getClass().getClassLoader().loadClass(className);
      setCurrentFunction((Function) fnC.newInstance());
      getCurrentFunction().setName(name);
      getCurrentFunction().setDependencyLevel(depLevel);
    }
    catch (ClassNotFoundException e)
    {
      throw new ParseException("Function " + name + " class=" + className
          + " is not valid. ", e, getLocator());
    }
    catch (IllegalAccessException e)
    {
      throw new ParseException("Function " + name + " class=" + className
          + " is not valid. ", e, getLocator());
    }
    catch (InstantiationException e)
    {
      throw new ParseException("Function " + name + " class=" + className
          + " is not valid. ", e, getLocator());
    }
  }

  /**
   * Receives some (or all) of the text in the current element.
   *
   * @param ch  the character array.
   * @param start  the first character index.
   * @param length  the length (number of valid characters).
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    // accumulate the characters in case the text is split into several chunks...
    if (this.currentText != null)
    {
      this.currentText.append(String.copyValueOf(ch, start, length));
    }
  }

  /**
   * Ends the current element.
   *
   * @param qName  the element name.
   *
   * @throws SAXException if there is a problem parsing the element.
   */
  public void endElement(final String qName) throws SAXException
  {
    final String elementName = qName.toLowerCase().trim();
    if (elementName.equals(FUNCTION_TAG))
    {
      endFunction();
    }
    else if (elementName.equals(FUNCTIONS_TAG))
    {
      endFunctions();
    }
    else if (elementName.equals(DATAREF_TAG))
    {
      // is no longer used
    }
    else if (elementName.equals(PROPERTIES_TAG))
    {
      endProperties();
    }
    else if (elementName.equals(PROPERTY_TAG))
    {
      endProperty();
    }
    else if (elementName.equals(EXPRESSION_TAG))
    {
      endExpression();
    }
    else if (elementName.equals(PROPERTY_REFERENCE_TAG))
    {
      endPropertyRef();
    }
    else if (elementName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(qName);
    }
    else
    {
      throw new ParseException("Expected closing function tag.", getLocator());
    }
  }

  /**
   * Ends the function. The current function is added to the report and initialized during
   * this process.
   *
   * @throws SAXException if there is a problem parsing the element.
   */
  protected void endFunction()
      throws SAXException
  {
    getReport().addExpression(getCurrentFunction());
  }

  /**
   * Ends the expression. The current expression is added to the report and initialized during
   * this process.
   *
   * @throws SAXException if there is a problem parsing the element.
   */
  protected void endExpression()
      throws SAXException
  {
    getReport().addExpression(getCurrentExpression());
  }

  /**
   * Ends the parsing of functions.
   *
   * @throws SAXException if there is a problem parsing the element.
   */
  protected void endFunctions()
      throws SAXException
  {
    getParser().popFactory().endElement(FUNCTIONS_TAG);
  }

  /**
   * Ends the properties parsing for the current function. The properties are added to the
   * current function.
   *
   * @throws SAXException if there is a problem parsing the element.
   */
  protected void endProperties()
      throws SAXException
  {
    final Expression f = getCurrentExpression();
    if (f == null)
    {
      throw new ParseException("End properties reached without a function defined", getLocator());
    }
    f.setProperties(currentProperties);
  }

  /**
   * Ends the definition of a single property entry.
   *
   * @throws SAXException if there is a problem parsing the element.
   */
  protected void endProperty()
      throws SAXException
  {
    final Properties currentProps = getProperties();
    if (currentProps == null)
    {
      throw new ParseException("EndProperty without properties tag?", getLocator());
    }

    currentProps.setProperty(currentProperty, entityParser.decodeEntities(currentText.toString()));
    currentText = null;
  }


  /**
   * Ends the definition of a single property entry.
   */
  protected void endPropertyRef()
  {
    getReport().getProperties().setMarked(currentProperty, true);
    if (currentText.length() != 0)
    {
      getReport().setProperty(currentProperty, entityParser.decodeEntities(currentText.toString()));
    }
    currentText = null;
  }

}

