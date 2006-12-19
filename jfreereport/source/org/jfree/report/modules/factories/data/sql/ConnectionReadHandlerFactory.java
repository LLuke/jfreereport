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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.modules.factories.data.sql;

import java.util.Iterator;

import org.jfree.report.JFreeReportBoot;
import org.jfree.xmlns.parser.AbstractReadHandlerFactory;
import org.jfree.util.Configuration;

/**
 * Creation-Date: Dec 17, 2006, 8:58:11 PM
 *
 * @author Thomas Morgner
 */
public class ConnectionReadHandlerFactory extends AbstractReadHandlerFactory
{
  private static final String PREFIX_SELECTOR =
      "org.jfree.report.modules.factories.data.sql.connection-factory-prefix.";

  private static ConnectionReadHandlerFactory readHandlerFactory;

  public ConnectionReadHandlerFactory()
  {
  }

  protected Class getTargetClass()
  {
    return ConnectionReadHandler.class;
  }

  public static synchronized ConnectionReadHandlerFactory getInstance()
  {
    if (readHandlerFactory == null)
    {
      readHandlerFactory = new ConnectionReadHandlerFactory();
      final Configuration config = JFreeReportBoot.getInstance().getGlobalConfig();
      final Iterator propertyKeys = config.findPropertyKeys(PREFIX_SELECTOR);
      while (propertyKeys.hasNext())
      {
        final String key = (String) propertyKeys.next();
        final String value = config.getConfigProperty(key);
        if (value != null)
        {
          readHandlerFactory.configure(config, value);
        }
      }
    }
    return readHandlerFactory;
  }

}
