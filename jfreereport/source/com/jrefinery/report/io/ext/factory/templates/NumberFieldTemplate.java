/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.DecimalFormatFilter;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplate;

public class NumberFieldTemplate extends AbstractTemplate
{
  public NumberFieldTemplate()
  {
    setParameterDefinition(FIELD_PARAMETER, String.class);
    setParameterDefinition(FORMAT_PARAMETER, String.class);
    setParameterDefinition(NULL_VALUE_PARAMETER, String.class);
  }

  public DataSource createDataSource()
  {
    String format = (String) getParameter(FORMAT_PARAMETER);
    String field = (String) getParameter(FIELD_PARAMETER);
    String nullValue = (String) getParameter(NULL_VALUE_PARAMETER);

    DecimalFormatFilter decimalFilter = new DecimalFormatFilter();
    if (format != null)
    {
      decimalFilter.setFormatString(format);
    }
    decimalFilter.setDataSource(new DataRowDataSource(field));
    StringFilter filter = new StringFilter();
    filter.setDataSource(decimalFilter);
    filter.setNullValue(nullValue);
    return filter;
  }
}
