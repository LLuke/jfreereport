/**
 * Date: Jan 12, 2003
 * Time: 4:29:45 PM
 *
 * $Id: DataSourceFactory.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

public interface DataSourceFactory extends ClassFactory
{
  public ObjectDescription getDataSourceDescription (String name);
  public String getDataSourceName (ObjectDescription od);
}
