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
 * ------------------------
 * ParserConfigHandler.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id:$
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceFactory;
import com.jrefinery.report.io.ext.factory.elements.ElementFactory;
import com.jrefinery.report.io.ext.factory.elements.ElementFactoryCollector;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryCollector;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactory;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactoryCollector;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollection;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollector;
import com.jrefinery.report.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A parser configuration handler.
 * 
 * @author Thomas Morgner
 */
public class ParserConfigHandler implements ReportDefinitionHandler
{
  /** The 'stylekey-factory' tag name. */
  public static final String STYLEKEY_FACTORY_TAG = "stylekey-factory";
  
  /** The 'template-factory' tag name. */
  public static final String TEMPLATE_FACTORY_TAG = "template-factory";
  
  /** The 'object-factory' tag name. */
  public static final String OBJECT_FACTORY_TAG = "object-factory";
  
  /** The 'datadefinition-factory' tag name. */
  public static final String DATADEFINITION_FACTORY_TAG = "datadefinition-factory";
  
  /** The 'datasource-factory' tag name. */
  public static final String DATASOURCE_FACTORY_TAG = "datasource-factory";
  
  /** The 'element-factory' tag name. */
  public static final String ELEMENT_FACTORY_TAG = "element-factory";

  /** The class attribute name. */
  public static final String CLASS_ATTRIBUTE = "class";

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /**
   * The parser configuration handler.
   * 
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public ParserConfigHandler(Parser parser, String finishTag)
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
   * @throws SAXException ??.
   */
  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(STYLEKEY_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) 
      {
        throw new SAXException("Attribute 'class' is missing.");
      }
      StyleKeyFactoryCollector fc =
          (StyleKeyFactoryCollector) getParser().getConfigurationValue(STYLEKEY_FACTORY_TAG);

      StyleKeyFactory factory = (StyleKeyFactory) createFactory(className);
      if (factory == null)
      {
        throw new SAXException("Unable to create Factory");
      }
      factory.init(getParser());
      fc.addFactory(factory);
    }
    else if (tagName.equals(OBJECT_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) 
      {
        throw new SAXException("Attribute 'class' is missing.");
      }
      ClassFactoryCollector fc =
          (ClassFactoryCollector) getParser().getConfigurationValue(OBJECT_FACTORY_TAG);
      fc.addFactory((ClassFactory) createFactory(className));
    }
    else if (tagName.equals(TEMPLATE_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) 
      {
        throw new SAXException("Attribute 'class' is missing.");
      }
      TemplateCollector fc =
          (TemplateCollector) getParser().getConfigurationValue(TEMPLATE_FACTORY_TAG);
      fc.addTemplateCollection((TemplateCollection) createFactory(className));
    }
    else if (tagName.equals(DATASOURCE_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) 
      {
        throw new SAXException("Attribute 'class' is missing.");
      }
      DataSourceFactory factory = (DataSourceFactory) createFactory(className);
      DataSourceCollector fc =
          (DataSourceCollector) getParser().getConfigurationValue(DATASOURCE_FACTORY_TAG);
      fc.addFactory(factory);
    }
    else if (tagName.equals(ELEMENT_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) 
      {
        throw new SAXException("Attribute 'class' is missing.");
      }
      ElementFactoryCollector fc =
          (ElementFactoryCollector) getParser().getConfigurationValue(ELEMENT_FACTORY_TAG);
      fc.addFactory((ElementFactory) createFactory(className));
    }
    else if (tagName.equals(DATADEFINITION_FACTORY_TAG))
    {

    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + STYLEKEY_FACTORY_TAG + ", "
                              + DATASOURCE_FACTORY_TAG + ", "
                              + DATADEFINITION_FACTORY_TAG + ", "
                              + TEMPLATE_FACTORY_TAG + ", "
                              + OBJECT_FACTORY_TAG
                             );
    }
  }

  /**
   * Creates a factory object.
   * 
   * @param classname  the class name.
   * 
   * @return The factory.
   * 
   * @throws SAXException if the specified class cannot be loaded.
   */
  private Object createFactory (String classname) throws SAXException
  {
    try
    {
      Class f = getClass().getClassLoader().loadClass(classname);
      return f.newInstance();
    }
    catch (Exception e)
    {
      Log.error ("Failed to parse Factory: ", e);
      throw new SAXException("Invalid Factory class specified");
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
    // is not used ...
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser. 
   * 
   * @param tagName  the tag name.
   * 
   * @throws SAXException ??.
   */
  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(STYLEKEY_FACTORY_TAG) 
        || tagName.equals(DATADEFINITION_FACTORY_TAG)
        || tagName.equals(TEMPLATE_FACTORY_TAG)
        || tagName.equals(DATASOURCE_FACTORY_TAG)
        || tagName.equals(ELEMENT_FACTORY_TAG)
        || tagName.equals(OBJECT_FACTORY_TAG))
    {
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + STYLEKEY_FACTORY_TAG + ", "
                              + DATADEFINITION_FACTORY_TAG + ", "
                              + TEMPLATE_FACTORY_TAG + ", "
                              + DATASOURCE_FACTORY_TAG + ", "
                              + ELEMENT_FACTORY_TAG + ", "
                              + OBJECT_FACTORY_TAG);
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
}
