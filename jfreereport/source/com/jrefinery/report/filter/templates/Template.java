/**
 * Date: Jan 14, 2003
 * Time: 1:06:54 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.filter.DataSource;

public interface Template extends DataSource
{
  public void setName (String name);
  public String getName ();

  public Template getInstance ();
}
