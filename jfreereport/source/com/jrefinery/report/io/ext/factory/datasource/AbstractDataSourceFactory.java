/**
 * Date: Jan 12, 2003
 * Time: 4:33:28 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

import java.util.Hashtable;

public abstract class AbstractDataSourceFactory implements DataSourceFactory
{
  private Hashtable dataSources;

  public AbstractDataSourceFactory()
  {
    dataSources = new Hashtable();
  }

  public void registerDataSources (String name, ObjectDescription o)
  {
    dataSources.put(name, o);
  }

  public ObjectDescription getDataSourceDescription(String name)
  {
    return (ObjectDescription) dataSources.get(name);
  }

}
