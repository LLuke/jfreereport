/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.factories.data.base;

import java.util.Iterator;

import org.jfree.report.JFreeReportBoot;
import org.jfree.util.Configuration;
import org.jfree.xmlns.parser.AbstractReadHandlerFactory;

/**
 * Creation-Date: 27.11.2006, 11:48:02
 *
 * @author Thomas Morgner
 */
public class DataFactoryReadHandlerFactory extends AbstractReadHandlerFactory
{

  private static final String PREFIX_SELECTOR =
      "org.jfree.report.modules.factories.data.data-factory-prefix.";

  private static DataFactoryReadHandlerFactory readHandlerFactory;

  public DataFactoryReadHandlerFactory()
  {
  }

  protected Class getTargetClass()
  {
    return DataFactoryReadHandler.class;
  }

  public static synchronized DataFactoryReadHandlerFactory getInstance()
  {
    if (DataFactoryReadHandlerFactory.readHandlerFactory == null)
    {
      DataFactoryReadHandlerFactory.readHandlerFactory = new DataFactoryReadHandlerFactory();
      final Configuration config = JFreeReportBoot.getInstance().getGlobalConfig();
      final Iterator propertyKeys = config.findPropertyKeys(DataFactoryReadHandlerFactory.PREFIX_SELECTOR);
      while (propertyKeys.hasNext())
      {
        final String key = (String) propertyKeys.next();
        final String value = config.getConfigProperty(key);
        if (value != null)
        {
          DataFactoryReadHandlerFactory.readHandlerFactory.configure(config, value);
        }
      }
    }
    return DataFactoryReadHandlerFactory.readHandlerFactory;
  }
}
