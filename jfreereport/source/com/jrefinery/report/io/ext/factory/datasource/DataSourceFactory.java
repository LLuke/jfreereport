/**
 * Date: Jan 12, 2003
 * Time: 4:29:45 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

public interface DataSourceFactory
{
  public ObjectDescription getDataSourceDescription (String name);
}
