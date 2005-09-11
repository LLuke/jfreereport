/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * XmlDemoHandler.java
 * ----------------
 * (C)opyright 2000-2005, by Simba Management Limited.
 *
 *
 * $Id: BookstoreTableModel.java,v 1.1 2005/08/29 17:30:33 taqua Exp $
 *
 */
package org.jfree.report.demo.helper;

import java.net.URL;

/**
 * An XML demo handler offers generic support for reading the report definition
 * from an XML file.
 *
 * @author Thomas Morgner
 */
public interface XmlDemoHandler extends InternalDemoHandler
{
  /**
   * Returns the URL of the XML definition for this report.
   *
   * @return the URL of the report definition.
   */
  public URL getReportDefinitionSource();
}
