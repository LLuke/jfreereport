/**
 * Date: Jan 11, 2003
 * Time: 2:03:10 AM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

public interface Template extends ObjectDescription
{
  public DataSource createDataSource ();
}
