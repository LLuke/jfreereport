/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------------
 * FunctionFactory.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 10-May-2002 : Initial version
 * 23-May-2002 : Rewrite and better structured, divided into several start* & end* methods
 * 08-Jun-2002 : Documentation
 * 19-Aug-2002 : Added support for expressions
 */
package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.function.Expression;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Properties;

/**
 * The functionFactory creates functions and adds these functions to the FunctionCollection
 * of the current report.
 */
public class FunctionFactory extends DefaultHandler implements ReportDefinitionTags
{
  private JFreeReport report;
  private Expression currentFunction;
  private Properties currentProperties;
  private StringBuffer currentText;
  private String currentProperty;
  private ReportDefinitionContentHandler handler;

  public FunctionFactory (ReportFactory baseFactory)
  {
    this.report = baseFactory.getReport ();
    this.handler = baseFactory.getHandler ();
  }

  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * StartTag-occurences of function definitions get handled by this factory. If an unknown
   * tag is encountered, a SAXException is thrown.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
  public void startElement (String namespaceURI,
                            String localName,
                            String qName,
                            Attributes atts) throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
    if (elementName.equals (FUNCTION_TAG))
    {
      startFunction (atts);
    }
    else if (elementName.equals (FUNCTIONS_TAG))
    {
      startFunctions (atts);
    }
    else if (elementName.equals (PROPERTIES_TAG))
    {
      startProperties (atts);
    }
    else if (elementName.equals (PROPERTY_TAG))
    {
      startProperty (atts);
    }
    else if (elementName.equals (DATAREF_TAG))
    {
      startDataRef (atts);
    }
    else if (elementName.equals(EXPRESSION_TAG))
    {
      startExpression (atts);
    }
    else
      throw new SAXException ("Expected one of 'function', 'functions', 'data-ref', 'properties', "
                            + "'property' tag");
  }

  /**
   * returns the current properties bundle for the function that is currently created
   */
  protected Properties getProperties ()
  {
    return currentProperties;
  }

  /**
   * defines the properties for the current function.
   */
  protected void setProperties (Properties p)
  {
    this.currentProperties = p;
  }

  /**
   * @return the current function to be produced
   */
  protected Function getCurrentFunction ()
  {
    return (Function) currentFunction;
  }

  /**
   * defines the current function. This function gets properties set and is then added
   * to the reports function collection.
   */
  protected void setCurrentFunction (Function function)
  {
    this.currentFunction = function;
  }

  /**
   * @return the current function to be produced
   */
  protected Expression getCurrentExpression ()
  {
    return (Expression) currentFunction;
  }

  /**
   * defines the current function. This function gets properties set and is then added
   * to the reports function collection.
   */
  protected void setCurrentExpression (Expression function)
  {
    this.currentFunction = function;
  }

  /**
   * returns the report to be processed.
   */
  protected JFreeReport getReport ()
  {
    return report;
  }

  /**
   * starts the Properties tag to create a new property bundle for a function.
   */
  protected void startProperties (Attributes atts)
          throws SAXException
  {
    setProperties (new Properties ());
  }

  /**
   * starts a new property entry for the current function
   */
  protected void startProperty (Attributes atts)
          throws SAXException
  {
    currentProperty = atts.getValue ("name");
    currentText = new StringBuffer ();
  }

  /**
   * starts a new data-reference. This is not implemented yet.
   */
  protected void startDataRef (Attributes atts)
          throws SAXException
  {
  }

  /**
   * starts a new function collection. Function collections are already contained in
   * the report, so this function does nothing.
   */
  protected void startFunctions (Attributes attr)
          throws SAXException
  {
  }

  protected void startExpression (Attributes attr)
      throws SAXException
  {
    String name = handler.generateName (attr.getValue ("name"));
    String className = attr.getValue ("class");
    if (className == null)
    {
      throw new SAXException ("Expression class not specified");
    }

    try
    {
      Class fnC = Class.forName (className);
      this.currentFunction = (Expression) fnC.newInstance ();
      this.currentFunction.setName (name);
    }
    catch (ClassNotFoundException e)
    {
      throw new SAXException ("Expression " + name + " class=" + className
                            + " is not valid: ClassNotFound: " + e.getMessage ());
    }
    catch (IllegalAccessException e)
    {
      throw new SAXException ("Expression " + name + " class=" + className
                            + " is not valid: IllegalAccess: " + e.getMessage ());
    }
    catch (InstantiationException e)
    {
      throw new SAXException ("Expression " + name + " class=" + className
                            + " is not valid: Instantiation: " + e.getMessage ());
    }
  }

  /**
   * starts and loads a function by instantating the functions class. The function must
   * have a default constructor defined.
   */
  protected void startFunction (Attributes attr)
          throws SAXException
  {
    String name = handler.generateName (attr.getValue ("name"));
    String className = attr.getValue ("class");
    if (className == null)
    {
      throw new SAXException ("Function class not specified");
    }

    try
    {
      Class fnC = Class.forName (className);
      this.currentFunction = (Function) fnC.newInstance ();
      this.currentFunction.setName (name);
    }
    catch (ClassNotFoundException e)
    {
      throw new SAXException ("Function " + name + " class=" + className
                            + " is not valid: ClassNotFound: " + e.getMessage ());
    }
    catch (IllegalAccessException e)
    {
      throw new SAXException ("Function " + name + " class=" + className
                            + " is not valid: IllegalAccess: " + e.getMessage ());
    }
    catch (InstantiationException e)
    {
      throw new SAXException ("Function " + name + " class=" + className
                            + " is not valid: Instantiation: " + e.getMessage ());
    }
  }

  /**
   * Receives some (or all) of the text in the current element.
   */
  public void characters (char[] ch, int start, int length)
  {
    // @todo: Parse the default entities
    // accumulate the characters in case the text is split into several chunks...
    if (this.currentText != null)
    {
      this.currentText.append (String.copyValueOf (ch, start, length));
    }
  }

  /**
   * Ends the current element.
   */
  public void endElement (String namespaceURI,
                          String localName,
                          String qName) throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
    if (elementName.equals (FUNCTION_TAG))
    {
      endFunction ();
    }
    else if (elementName.equals (FUNCTIONS_TAG))
    {
      endFunctions ();
    }
    else if (elementName.equals (DATAREF_TAG))
    {
      endDataRef ();
    }
    else if (elementName.equals (PROPERTIES_TAG))
    {
      endProperties ();
    }
    else if (elementName.equals (PROPERTY_TAG))
    {
      endProperty ();
    }
    else if (elementName.equals (EXPRESSION_TAG))
    {
      endExpression();
    }
    else
      throw new SAXException ("Expected function tag");

  }

  /**
   * Ends the function. The current function is added to the report and initialized during
   * this process.
   */
  protected void endFunction ()
          throws SAXException
  {
    try
    {
      getReport ().addFunction (getCurrentFunction());
    }
    catch (FunctionInitializeException fie)
    {
      throw new SAXException (fie);
    }
  }

  protected void endExpression ()
    throws SAXException
  {
    try
    {
      getReport ().addExpression (getCurrentExpression());
    }
    catch (FunctionInitializeException fie)
    {
      throw new SAXException (fie);
    }
  }

  /**
   * Ends the parsing of functions.
   */
  protected void endFunctions ()
          throws SAXException
  {
    handler.finishedHandler ();
  }

  /**
   * Data-refs are not yet implemented
   */
  protected void endDataRef ()
          throws SAXException
  {
  }

  /**
   * Ends the properties parsing for the current function. The properties are added to the
   * current function.
   */
  protected void endProperties ()
          throws SAXException
  {
    Expression f = getCurrentExpression();
    if (f == null)
    {
      throw new SAXException ("End properties reached without a function defined");
    }
    f.setProperties (currentProperties);
  }

  /**
   * Ends the definition of a single property entry.
   */
  protected void endProperty ()
          throws SAXException
  {
    Properties currentProps = getProperties ();
    if (currentProps == null)
    {
      throw new SAXException ("EndProperty without properties tag?");
    }

    currentProps.setProperty (currentProperty, currentText.toString ());
    currentText = null;
  }
}

