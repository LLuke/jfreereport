/**
 * Date: Jan 12, 2003
 * Time: 4:33:28 PM
 *
 * $Id: AbstractDataSourceFactory.java,v 1.3 2003/01/23 18:07:44 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.io.ext.factory.objects.ClassFactoryImpl;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

import java.util.Enumeration;
import java.util.Hashtable;

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
      if (ds.getObjectClass().equals(od.getObjectClass()))
        return key;
    }
    return null;
  }
}
