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
 * --------------------------
 * CompoundObjectHandler.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CompoundObjectHandler.java,v 1.12 2003/06/19 18:44:09 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import org.jfree.xml.ParseException;
import org.jfree.xml.Parser;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A compound object handler. Handles the creation of compound objects. Compound
 * objects are complex objects, which have several properties to be filled and may
 * contain other compound objects.
 *
 * @author Thomas Morgner.
 */
public class CompoundObjectHandler extends BasicObjectHandler
{
  /** The text for the 'compound-object' tag. */
  public static final String COMPOUND_OBJECT_TAG = "compound-object";

  /** The text for the 'basic-object' tag. */
  public static final String BASIC_OBJECT_TAG = "basic-object";

  /** A basic object handler. */
  private BasicObjectHandler basicFactory;

  /** The parameter name. */
  private String parameterName;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param od  the object description.
   */
  public CompoundObjectHandler(Parser parser, String finishTag, ObjectDescription od)
  {
    super(parser, finishTag, od);
  }

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param targetObject  the class.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public CompoundObjectHandler(Parser parser, String finishTag, Class targetObject)
      throws SAXException
  {
    super(parser, finishTag, targetObject);
  }

  /**
   * Returns the object description.
   *
   * @return the object description.
   */
  private ObjectDescription getKeyObjectDescription()
  {
    return getTargetObjectDescription();
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
        throw new ParseException("Attribute 'name' is missing.", getParser().getLocator());
      }
      ObjectDescription od = getKeyObjectDescription();
      if (od == null)
      {
        throw new ParseException("No ObjectFactory for the key", getParser().getLocator());
      }

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

      basicFactory = new BasicObjectHandler(getParser(), tagName, parameter);
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(COMPOUND_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
      {
        throw new ParseException("Attribute 'name' is missing.", getParser().getLocator());
      }
      ObjectDescription od = getKeyObjectDescription();
      if (od == null)
      {
        throw new ParseException("No ObjectFactory for the key", getParser().getLocator());
      }

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
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + COMPOUND_OBJECT_TAG + ", "
          + BASIC_OBJECT_TAG + ". ");
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
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    // ignore the event...
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
    if (tagName.equals(COMPOUND_OBJECT_TAG))
    {
      if (basicFactory == null && tagName.equals(getFinishTag()))
      {
        getParser().popFactory().endElement(tagName);
        return;
      }
      else
      {
        Object o = basicFactory.getValue();
        if (o == null)
        {
          throw new ParseException("Parameter value is null", getParser().getLocator());
        }

        getKeyObjectDescription().setParameter(parameterName, o);
        basicFactory = null;
      }
    }
    else if (tagName.equals(BASIC_OBJECT_TAG))
    {
      Object o = basicFactory.getValue();
      if (o == null)
      {
        throw new ParseException("Parameter value is null", getParser().getLocator());
      }
      getKeyObjectDescription().setParameter(parameterName, o);
      basicFactory = null;
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + getFinishTag() + ", "
          + COMPOUND_OBJECT_TAG + ", "
          + BASIC_OBJECT_TAG + ". ");
    }
  }

}
