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
 * ------------------------
 * DataSourceCollector.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A {@link DataSourceFactory} created from a number of other factories.
 * 
 * @author Thomas Morgner
 */
public class DataSourceCollector implements DataSourceFactory
{
  /** Storage for the factories. */
  private ArrayList factories;

  /**
   * Creates a new factory.
   */
  public DataSourceCollector()
  {
    factories = new ArrayList();
  }

  /**
   * Adds a factory to the collection.
   * 
   * @param factory  the factory.
   */
  public void addFactory (DataSourceFactory factory)
  {
    factories.add (factory);
  }

  /**
   * Returns an iterator that provides access to the factories.
   * 
   * @return The iterator.
   */
  public Iterator getFactories ()
  {
    return factories.iterator();
  }

  /**
   * Returns a data source description.
   * 
   * @param name  the data source name.
   * 
   * @return The description.
   */
  public ObjectDescription getDataSourceDescription(String name)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      ObjectDescription o = fact.getDataSourceDescription(name);
      if (o != null) 
      {  
        return o;
      }
    }
    return null;
  }

  /**
   * Returns a data source name.
   * 
   * @param od  the object description.
   * 
   * @return The name.
   */
  public String getDataSourceName(ObjectDescription od)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      String o = fact.getDataSourceName(od);
      if (o != null)
      {
        return o;
      }
    }
    return null;
  }

  /**
   * Returns a description for the class.
   * 
   * @param c  the class.
   * 
   * @return The description.
   */
  public ObjectDescription getDescriptionForClass(Class c)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      ObjectDescription o = fact.getDescriptionForClass(c);
      if (o != null) 
      { 
        return o;
      }
    }
    return null;
  }

  /**
   * Returns a description for the super class.
   * 
   * @param d  the class.
   * 
   * @return The description.
   */
  public ObjectDescription getSuperClassObjectDescription (Class d)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      ObjectDescription o = fact.getSuperClassObjectDescription(d);
      if (o != null) 
      {
        return o;
      }
    }
    return null;
  }

  /**
   * Returns an iterator that provides access to the registered classes.
   * 
   * @return The iterator.
   */
  public Iterator getRegisteredClasses()
  {
    ArrayList list = new ArrayList();
    for (int i = 0; i < factories.size(); i++)
    {
      ClassFactory f = (ClassFactory) factories.get(i);
      Iterator enum = f.getRegisteredClasses();
      while (enum.hasNext())
      {
        list.add(enum.next());
      }
    }
    return list.iterator();
  }
  
}
