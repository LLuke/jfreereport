/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplate;

public class LabelTemplate extends AbstractTemplate
{
  public LabelTemplate()
  {
    setParameterDefinition(VALUE_PARAMETER, String.class);
    setParameterDefinition(NULL_VALUE_PARAMETER, String.class);
  }

  public DataSource createDataSource()
  {
    StaticDataSource sds = new StaticDataSource();
    Object value = getParameter(VALUE_PARAMETER);
    sds.setValue(value);

    String nullValue = (String) getParameter(NULL_VALUE_PARAMETER);
    StringFilter filter = new StringFilter();
    filter.setDataSource(sds);
    filter.setNullValue(nullValue);

    return filter;
  }
}
