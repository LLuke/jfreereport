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
 * --------------------
 * TemplateHandler.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TemplateHandler.java,v 1.12 2003/06/10 16:07:50 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.ext.factory.templates.TemplateDescription;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.ParseException;
import org.jfree.xml.Parser;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A template handler. A template is a predefined usecase for some DataSources.
 *
 * @see com.jrefinery.report.filter.templates.Template
 * @author Thomas Morgner.
 */
public class TemplateHandler implements ElementDefinitionHandler
{
  /** The compound object tag. */
  public static final String COMPOUND_OBJECT_TAG = "compound-object";

  /** The basic object tag. */
  public static final String BASIC_OBJECT_TAG = "basic-object";

  /** A basic object handler. */
  private BasicObjectHandler basicFactory;

  /** The parameter name. */
  private String parameterName;

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /** The template description. */
  private TemplateDescription template;

  /**
   * Creates a new template handler. The handler will be deactivated when the
   * finishTag was reached on endElement.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param template  the template description.
   */
  public TemplateHandler(Parser parser, String finishTag, TemplateDescription template)
  {
    if (parser == null) 
    {
      throw new NullPointerException("Parser is null");
    }
    if (finishTag == null) 
    {
      throw new NullPointerException("FinishTag is null");
    }
    if (template == null) 
    {
      throw new NullPointerException("Template is null");
    }

    this.parser = parser;
    this.finishTag = finishTag;
    this.template = template;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(BASIC_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
      {
        throw new ParseException ("Attribute 'name' is missing.", getParser().getLocator());
      }
      ObjectDescription od = getTemplate();
      Class parameter = od.getParameterDefinition(parameterName);
      if (parameter == null)
      {
        throw new ParseException("No such parameter '" + parameterName + "' in template. ",
            getParser().getLocator());
      }
      String overrideClassName = attrs.getValue("class");
      if (overrideClassName != null)
      {
        try
        {
          parameter = getClass().getClassLoader().loadClass(overrideClassName);
        }
        catch (Exception e)
        {
          throw new ParseException("Attribute 'class' is invalid.", e, getParser().getLocator());
        }
      }

      basicFactory = new BasicObjectHandler(getParser(), tagName, parameter);
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(COMPOUND_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
      {
        throw new ParseException ("Attribute 'name' is missing.", getParser().getLocator());
      }
      ObjectDescription od = getTemplate();
      Class parameter = od.getParameterDefinition(parameterName);
      if (parameter == null)
      {
        throw new ParseException("No such parameter: " + parameterName, getParser().getLocator());
      }
      String overrideClassName = attrs.getValue("class");
      if (overrideClassName != null)
      {
        try
        {
          parameter = getClass().getClassLoader().loadClass(overrideClassName);
        }
        catch (Exception e)
        {
          throw new ParseException("Attribute 'class' is invalid.", e, getParser().getLocator());
        }
      }

      basicFactory = new CompoundObjectHandler(getParser(), tagName, parameter);
      getParser().pushFactory(basicFactory);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + COMPOUND_OBJECT_TAG + ", " + BASIC_OBJECT_TAG + ". ");
    }
  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void characters(char ch[], int start, int length) throws SAXException
  {
    // ignore ...
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(String tagName) throws SAXException
  {
    if ((tagName.equals(BASIC_OBJECT_TAG))
        || (tagName.equals(COMPOUND_OBJECT_TAG)))
    {
      Object o = basicFactory.getValue();
      if (o == null)
      {
        throw new ParseException("Parameter value is null", getParser().getLocator());
      }
      getTemplate().setParameter(parameterName, o);
      basicFactory = null;
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + finishTag + ", "
                              + COMPOUND_OBJECT_TAG + ", "
                              + BASIC_OBJECT_TAG + ". ");
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
   * Returns the template description.
   *
   * @return The template description.
   */
  public TemplateDescription getTemplate()
  {
    return template;
  }
}
