/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ------------------------------
 * AbstractDataSourceFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractDataSourceFactory.java,v 1.7 2003/03/18 18:28:43 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.datasource;

import java.util.HashMap;
import java.util.Iterator;

import com.jrefinery.xml.factory.objects.ClassFactoryImpl;
import com.jrefinery.xml.factory.objects.ObjectDescription;

/**
 * A base class for implementing the {@link DataSourceFactory} interface.
 * 
 * @author Thomas Morgner
 */
public abstract class AbstractDataSourceFactory
    extends ClassFactoryImpl implements DataSourceFactory
{
  /** Storage for the data sources. */
  private HashMap dataSources;

  /**
   * Creates a new factory.
   */
  public AbstractDataSourceFactory()
  {
    dataSources = new HashMap();
  }

  /**
   * Registers a data source.
   * 
   * @param name  the name.
   * @param o  the object description.
   */
  public void registerDataSources (String name, ObjectDescription o)
  {
    dataSources.put(name, o);
    registerClass(o.getObjectClass(), o);
  }

  /**
   * Returns a data source description.
   * 
   * @param name  the data source name.
   * 
   * @return The object description.
   */
  public ObjectDescription getDataSourceDescription(String name)
  {
    return (ObjectDescription) dataSources.get(name);
  }

  /**
   * Returns a data source name given a description.
   * 
   * @param od  the object description.
   * 
   * @return The name.
   */
  public String getDataSourceName(ObjectDescription od)
  {
    Iterator keys = dataSources.keySet().iterator();
    while (keys.hasNext())
    {
      String key = (String) keys.next();
      ObjectDescription ds = (ObjectDescription) dataSources.get(key);
      if (ds.getObjectClass().equals(od.getObjectClass()))
      {
        return key;
      }
    }
    return null;
  }
}
