/**
 * Date: Jan 11, 2003
 * Time: 2:03:10 AM
 *
 * $Id: Template.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

public interface Template extends ObjectDescription
{
  public void setName(String name);
  public String getName();
  public DataSource createDataSource ();
}
