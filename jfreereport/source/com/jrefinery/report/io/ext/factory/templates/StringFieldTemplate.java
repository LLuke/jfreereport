/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplate;

public class StringFieldTemplate extends AbstractTemplate
{
  public StringFieldTemplate()
  {
    setParameterDefinition(FIELD_PARAMETER, String.class);
    setParameterDefinition(NULL_VALUE_PARAMETER, String.class);
  }

  public DataSource createDataSource()
  {
    DataRowDataSource sds = new DataRowDataSource();
    String value = (String) getParameter(FIELD_PARAMETER);
    sds.setDataSourceColumnName(value);

    String nullValue = (String) getParameter(NULL_VALUE_PARAMETER);
    StringFilter filter = new StringFilter();
    filter.setDataSource(sds);
    filter.setNullValue(nullValue);

    return filter;
  }
}
