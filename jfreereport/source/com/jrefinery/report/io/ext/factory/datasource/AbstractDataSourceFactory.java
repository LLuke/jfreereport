/**
 * Date: Jan 12, 2003
 * Time: 4:33:28 PM
 *
 * $Id: AbstractDataSourceFactory.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryImpl;

import java.util.Hashtable;
import java.util.Enumeration;

public abstract class AbstractDataSourceFactory
    extends ClassFactoryImpl implements DataSourceFactory
{
  private Hashtable dataSources;

  public AbstractDataSourceFactory()
  {
    dataSources = new Hashtable();
  }

  public void registerDataSources (String name, ObjectDescription o)
  {
    dataSources.put(name, o);
    registerClass(o.getObjectClass(), o);
  }

  public ObjectDescription getDataSourceDescription(String name)
  {
    return (ObjectDescription) dataSources.get(name);
  }

  public String getDataSourceName(ObjectDescription od)
  {
    Enumeration keys = dataSources.keys();
    while (keys.hasMoreElements())
    {
      String key = (String) keys.nextElement();
      ObjectDescription ds = (ObjectDescription)dataSources.get(key);
      if (ds.getObjectClass().equals(ds))
        return key;
    }
    return null;
  }
}
