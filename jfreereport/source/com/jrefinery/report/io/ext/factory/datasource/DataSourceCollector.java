/**
 * Date: Jan 12, 2003
 * Time: 4:31:10 PM
 *
 * $Id: DataSourceCollector.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

import java.util.ArrayList;
import java.util.Iterator;

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
}
