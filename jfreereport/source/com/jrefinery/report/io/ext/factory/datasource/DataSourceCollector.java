/**
 * Date: Jan 12, 2003
 * Time: 4:31:10 PM
 *
 * $Id: DataSourceCollector.java,v 1.3 2003/01/22 19:38:25 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;

public class DataSourceCollector implements DataSourceFactory
{
  private ArrayList factories;

  public DataSourceCollector()
  {
    factories = new ArrayList();
  }

  public void addFactory (DataSourceFactory factory)
  {
    factories.add (factory);
  }

  public Iterator getFactories ()
  {
    return factories.iterator();
  }

  public ObjectDescription getDataSourceDescription(String name)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      ObjectDescription o = fact.getDataSourceDescription(name);
      if (o != null) return o;
    }
    return null;
  }

  public String getDataSourceName(ObjectDescription od)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      String o = fact.getDataSourceName(od);
      if (o != null)
        return o;
    }
    return null;
  }

  public ObjectDescription getDescriptionForClass(Class c)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      ObjectDescription o = fact.getDescriptionForClass(c);
      if (o != null) return o;
    }
    return null;
  }

  public ObjectDescription getSuperClassObjectDescription (Class d)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      DataSourceFactory fact = (DataSourceFactory) factories.get(i);
      ObjectDescription o = fact.getSuperClassObjectDescription(d);
      if (o != null) return o;
    }
    return null;
  }
}
