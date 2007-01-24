/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * HibernateDataSourceReadHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.ext.modules.data.hibernate.parser;

import java.util.ArrayList;

import org.jfree.report.modules.factories.data.base.DataFactoryReadHandler;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.ext.modules.data.hibernate.SessionProvider;
import org.jfree.report.ext.modules.data.hibernate.HQLDataFactory;
import org.jfree.xmlns.parser.AbstractXmlReadHandler;
import org.jfree.xmlns.parser.PropertyReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 07.04.2006, 17:47:53
 *
 * @author Thomas Morgner
 */
public class HibernateDataSourceReadHandler extends AbstractXmlReadHandler
  implements DataFactoryReadHandler
{
  private SessionProviderReadHandler connectionProviderReadHandler;
  private ArrayList queries;
  private ReportDataFactory dataFactory;

  public HibernateDataSourceReadHandler()
  {
    queries = new ArrayList();
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws org.xml.sax.SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
          throws SAXException
  {
    final SessionProviderReadHandlerFactory factory = SessionProviderReadHandlerFactory.getInstance();
    final SessionProviderReadHandler handler = (SessionProviderReadHandler) factory.getHandler(uri, tagName);
    if (handler != null)
    {
      connectionProviderReadHandler = handler;
      return connectionProviderReadHandler;
    }

    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if (tagName.equals("query"))
    {
      XmlReadHandler queryReadHandler = new PropertyReadHandler();
      queries.add(queryReadHandler);
      return queryReadHandler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected void doneParsing() throws SAXException
  {
    SessionProvider provider = null;
    if (connectionProviderReadHandler != null)
    {
      provider = (SessionProvider) connectionProviderReadHandler.getObject();
    }
    if (provider == null)
    {
      provider = (SessionProvider)
              getRootHandler().getHelperObject("session-provider");
    }
    if (provider == null)
    {
      throw new SAXException(
              "Unable to create SQL Factory: No connection provider.");
    }

    final HQLDataFactory srdf = new HQLDataFactory(provider);
    for (int i = 0; i < queries.size(); i++)
    {
      final PropertyReadHandler handler = (PropertyReadHandler) queries.get(i);
      srdf.setQuery(handler.getName(), handler.getResult());
    }

    srdf.setContentBase(getRootHandler().getSource());
    dataFactory = srdf;
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
    return dataFactory;
  }

  public ReportDataFactory getDataFactory()
  {
    return dataFactory;
  }
}
