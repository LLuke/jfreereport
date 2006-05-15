/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * PropertiesReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PropertiesReadHandler.java,v 1.1 2006/04/18 11:45:16 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.xmlns.parser;

import java.util.ArrayList;
import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.jfree.xmlns.parser.AbstractXmlReadHandler;

/**
 * Creation-Date: 07.04.2006, 18:11:50
 *
 * @author Thomas Morgner
 */
public class PropertiesReadHandler extends AbstractXmlReadHandler
{
  private ArrayList propertyHandlers;
  private String propertyTagName;
  private Properties result;

  public PropertiesReadHandler()
  {
    this("property");
  }


  public PropertiesReadHandler(final String propertyTagName)
  {
    if (propertyTagName == null)
    {
      throw new NullPointerException();
    }
    this.propertyHandlers = new ArrayList();
    this.propertyTagName = propertyTagName;
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
          throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }
    if (tagName.equals(propertyTagName))
    {
      PropertyReadHandler prh = new PropertyReadHandler();
      propertyHandlers.add(prh);
      return prh;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected void doneParsing() throws SAXException
  {
    result = new Properties();
    for (int i = 0; i < propertyHandlers.size(); i++)
    {
      final PropertyReadHandler handler =
              (PropertyReadHandler) propertyHandlers.get(i);
      result.setProperty(handler.getName(), handler.getResult());
    }
  }

  public Properties getResult()
  {
    return result;
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   * @throws XmlReaderException if there is a parsing error.
   */
  public Object getObject() throws SAXException
  {
    return result;
  }
}
