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
 * ----------------------------
 * CompoundStyleKeyHandler.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CompoundStyleKeyHandler.java,v 1.1 2003/07/07 22:44:08 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.xml.ParseException;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A compound style-key handler. Handles the creation of compound objects, which
 * should be stored in the element style sheet.
 *
 * @author Thomas Morgner.
 */
public class CompoundStyleKeyHandler extends BasicStyleKeyHandler
{
  /** The compound object tag name. */
  public static final String COMPOUND_OBJECT_TAG = CompoundObjectHandler.COMPOUND_OBJECT_TAG;

  /** The basic object tag name. */
  public static final String BASIC_OBJECT_TAG = CompoundObjectHandler.BASIC_OBJECT_TAG;

  /** The basic object handler. */
  private BasicObjectHandler basicFactory;

  /** The object description. */
  private ObjectDescription keyObjectDescription;

  /** A parameter name. */
  private String parameterName;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param name  the name.
   * @param c  the class.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public CompoundStyleKeyHandler(final ReportParser parser, final String finishTag, final String name, final Class c)
      throws SAXException
  {
    super(parser, finishTag, name, c);
    final ClassFactory fact = (ClassFactory) getParser().getHelperObject(
        ParserConfigHandler.OBJECT_FACTORY_TAG);
    keyObjectDescription = fact.getDescriptionForClass(getKeyValueClass());
    if (keyObjectDescription == null)
    {
      keyObjectDescription = fact.getSuperClassObjectDescription(getKeyValueClass(), null);
    }
    if (keyObjectDescription == null)
    {
      throw new ParseException("No object definition for class " + getStyleKey().getValueType(),
          getParser().getLocator());
    }
  }

  /**
   * Returns a description of the key.
   *
   * @return The description.
   */
  private ObjectDescription getKeyObjectDescription()
  {
    return keyObjectDescription;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(final String tagName, final Attributes attrs) throws SAXException
  {
    if (tagName.equals(BASIC_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
      {
        throw new ParseException("Attribute 'name' is missing.", getParser().getLocator());
      }
      final ObjectDescription od = getKeyObjectDescription();
      Class parameter = od.getParameterDefinition(parameterName);
      if (parameter == null)
      {
        throw new ParseException("No such parameter: " + parameterName, getParser().getLocator());
      }
      final String overrideClassName = attrs.getValue("class");
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

      basicFactory = new BasicObjectHandler(getReportParser(), tagName, parameter);
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(COMPOUND_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
      {
        throw new ParseException("Attribute 'name' is missing.", getParser().getLocator());
      }

      final ObjectDescription od = getKeyObjectDescription();
      Class parameter = od.getParameterDefinition(parameterName);
      if (parameter == null)
      {
        throw new ParseException("No such parameter", getParser().getLocator());
      }
      final String overrideClassName = attrs.getValue("class");
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

      basicFactory = new CompoundObjectHandler(getReportParser(), tagName, parameter);
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
  public void characters(final char[] ch, final int start, final int length) throws SAXException
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
  public void endElement(final String tagName) throws SAXException
  {
    if ((tagName.equals(BASIC_OBJECT_TAG)) || (tagName.equals(COMPOUND_OBJECT_TAG)))
    {
      final Object o = basicFactory.getValue();
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

  /**
   * Returns the value.
   *
   * @return The value.
   */
  public Object getValue()
  {
    return getKeyObjectDescription().createObject();
  }
}
