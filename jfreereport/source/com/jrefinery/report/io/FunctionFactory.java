/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 */
package com.jrefinery.report.io;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;

import java.util.Properties;

public class FunctionFactory extends DefaultHandler implements ReportDefinitionTags
{
  private JFreeReport report;
  private Function currentFunction;
  private Properties currentProperties;
  private StringBuffer currentText;
  private String currentProperty;
  private ReportDefinitionContentHandler handler;

  public FunctionFactory (JFreeReport report, ReportDefinitionContentHandler handler)
  {
    this.report = report;
    this.handler = handler;
  }

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
    }
    else if (elementName.equals (PROPERTIES_TAG))
    {
      currentProperties = new Properties ();
    }
    else if (elementName.equals (PROPERTY_TAG))
    {
      currentProperty = atts.getValue ("name");
      currentText = new StringBuffer ();
    }
    else if (elementName.equals (DATAREF_TAG))
    {
    }
    else
      throw new SAXException ("Expected function tag");
  }

  /**
   * Receives some (or all) of the text in the current element.
   */
  public void characters (char[] ch, int start, int length)
  {
// @todo: Parse the default entities

// accumulate the characters in case the text is split into several chunks...
    if (this.currentText != null)
      this.currentText.append (String.copyValueOf (ch, start, length));
  }

  public void endElement (String namespaceURI,
                          String localName,
                          String qName) throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
    if (elementName.equals (FUNCTION_TAG))
    {
      try
      {
        report.addFunction (currentFunction);
      }
      catch (FunctionInitializeException fie)
      {
        throw new SAXException(fie);
      }
    }
    else if (elementName.equals (FUNCTIONS_TAG))
    {
      handler.finishedHandler ();
    }
    else if (elementName.equals (DATAREF_TAG))
    {
    }
    else if (elementName.equals (PROPERTIES_TAG))
    {
      currentFunction.setProperties (currentProperties);
    }
    else if (elementName.equals (PROPERTY_TAG))
    {
      currentProperties.setProperty (currentProperty, currentText.toString ());
      Log.debug ("FunctionProperty: " + currentProperty + " = " + currentText.toString ());
      currentText = null;
    }
    else
      throw new SAXException ("Expected function tag");

  }

  public void startFunction (Attributes attr)
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
      throw new SAXException ("Function " + name + " class=" + className + " is not valid: ClassNotFound: " + e.getMessage ());
    }
    catch (IllegalAccessException e)
    {
      throw new SAXException ("Function " + name + " class=" + className + " is not valid: IllegalAccess: " + e.getMessage ());
    }
    catch (InstantiationException e)
    {
      throw new SAXException ("Function " + name + " class=" + className + " is not valid: Instantiation: " + e.getMessage ());
    }
  }
}

