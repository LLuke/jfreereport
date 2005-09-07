/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ConfigurationReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ConfigurationReadHandler.java,v 1.3 2005/03/03 23:00:20 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.base.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.PropertyStringReadHandler;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ConfigurationReadHandler extends AbstractPropertyXmlReadHandler
{
  private ModifiableConfiguration configuration;
  private HashMap fieldHandlers;

  public ConfigurationReadHandler (final ModifiableConfiguration configuration)
  {
    this.configuration = configuration;
    this.fieldHandlers = new HashMap();
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
                                               final PropertyAttributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("property"))
    {
      final String name = atts.getValue("name");
      if (name == null)
      {
        throw new ElementDefinitionException("Required attribute 'name' is missing.");
      }

      final CommentHintPath path = new CommentHintPath("report-definition");
      path.addName("report-config");
      path.addName("configuration");

      final PropertyStringReadHandler readHandler =
              new PropertyStringReadHandler(path);
      fieldHandlers.put(name, readHandler);
      return readHandler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    final Iterator it = fieldHandlers.entrySet().iterator();
    while (it.hasNext())
    {
      final Map.Entry entry = (Map.Entry) it.next();
      final String key = (String) entry.getKey();
      final PropertyStringReadHandler readHandler = (PropertyStringReadHandler) entry.getValue();
      configuration.setConfigProperty(key, readHandler.getResult());
    }
  }

  /**
   * Returns the object for this element.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return configuration;
  }

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath path = new CommentHintPath(configuration);
    defaultStoreComments(path);
  }
}
