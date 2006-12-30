/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
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
 * $Id: SQLDataSourceReadHandler.java,v 1.4 2006/12/19 17:42:02 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.modules.factories.data.sql;

import java.util.ArrayList;

import org.jfree.report.ReportDataFactory;
import org.jfree.report.modules.data.sql.ConnectionProvider;
import org.jfree.report.modules.data.sql.SQLReportDataFactory;
import org.jfree.xmlns.parser.AbstractXmlReadHandler;
import org.jfree.xmlns.parser.PropertyReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;
import org.jfree.xmlns.parser.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 07.04.2006, 17:47:53
 *
 * @author Thomas Morgner
 */
public class SQLDataSourceReadHandler extends AbstractXmlReadHandler
{
  private ConnectionReadHandler connectionProviderReadHandler;
  private ArrayList queries;
  private ConfigReadHandler configReadHandler;
  private ReportDataFactory dataFactory;

  public SQLDataSourceReadHandler()
  {
    queries = new ArrayList();
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException       if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
          throws SAXException
  {

    final ConnectionReadHandlerFactory factory = ConnectionReadHandlerFactory.getInstance();
    final ConnectionReadHandler handler = (ConnectionReadHandler) factory.getHandler(uri, tagName);
    if (handler != null)
    {
      connectionProviderReadHandler = handler;
      return connectionProviderReadHandler;
    }

    if (isSameNamespace(uri) == false)
    {
      return null;
    }
    if (tagName.equals("config"))
    {
      configReadHandler = new ConfigReadHandler();
      return configReadHandler;
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
   * @throws SAXException       if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    ConnectionProvider provider = null;
    if (connectionProviderReadHandler != null)
    {
      provider = connectionProviderReadHandler.getProvider();
    }
    if (provider == null)
    {
      provider = (ConnectionProvider)
              getRootHandler().getHelperObject("connection-provider");
    }
    if (provider == null)
    {
      throw new ParseException
          ("Unable to create SQL Factory: No connection provider.", getLocator());
    }

    SQLReportDataFactory srdf = new SQLReportDataFactory(provider);
    if (configReadHandler != null)
    {
      final Boolean labelMapping = configReadHandler.isLabelMapping();
      if (labelMapping != null)
      {
        srdf.setLabelMapping(labelMapping.booleanValue());
      }
    }
    for (int i = 0; i < queries.size(); i++)
    {
      final PropertyReadHandler handler = (PropertyReadHandler) queries.get(i);
      srdf.setQuery(handler.getName(), handler.getResult());
    }
    dataFactory = srdf;
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   * @throws SAXException if there is a parsing error.
   */
  public Object getObject() throws SAXException
  {
    return dataFactory;
  }
}
