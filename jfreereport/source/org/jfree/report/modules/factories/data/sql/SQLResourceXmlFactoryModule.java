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
 * SQLResourceXmlFactoryModule.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.factories.data.sql;

import org.jfree.report.modules.factories.common.XmlDocumentInfo;
import org.jfree.report.modules.factories.common.XmlFactoryModule;
import org.jfree.report.modules.factories.common.XmlReadHandler;

/**
 * Creation-Date: 07.04.2006, 15:29:17
 *
 * @author Thomas Morgner
 */
public class SQLResourceXmlFactoryModule implements XmlFactoryModule
{
  public SQLResourceXmlFactoryModule()
  {
  }

  public int getDocumentSupport(XmlDocumentInfo documentInfo)
  {
    final String rootNamespace = documentInfo.getRootElementNameSpace();
    if (rootNamespace != null)
    {
      if (SQLDataFactoryModule.NAMESPACE.equals(rootNamespace) == false)
      {
        return NOT_RECOGNIZED;
      }
      else if ("sql-datasource".equals(documentInfo.getRootElement()))
      {
        return RECOGNIZED_BY_NAMESPACE;
      }
    }
    else if ("sql-datasource".equals(documentInfo.getRootElement()))
    {
      return RECOGNIZED_BY_TAGNAME;
    }

    return NOT_RECOGNIZED;
  }

  public XmlReadHandler createReadHandler(XmlDocumentInfo documentInfo)
  {
    return new SQLDataSourceReadHandler();
  }
}
