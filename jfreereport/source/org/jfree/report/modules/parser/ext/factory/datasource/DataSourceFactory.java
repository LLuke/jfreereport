/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------------
 * DataSourceFactory.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataSourceFactory.java,v 1.4 2003/08/24 15:08:20 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.datasource;

import java.util.Iterator;

import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ObjectDescription;

/**
 * A data source factory.
 *
 * @author Thomas Morgner
 */
public interface DataSourceFactory extends ClassFactory
{
  /**
   * Returns a data source description.
   *
   * @param name  the name.
   *
   * @return The description.
   */
  public ObjectDescription getDataSourceDescription(String name);

  /**
   * Returns a data source name.
   *
   * @param od  the description.
   *
   * @return The name.
   */
  public String getDataSourceName(ObjectDescription od);

  /**
   * Returns the names of all registered datasources as iterator.
   *
   * @return the registered names.
   */
  public Iterator getRegisteredNames();
}
