/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: DateFieldTemplate.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.SimpleDateFormatFilter;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplate;
import com.jrefinery.report.io.ext.factory.objects.ObjectFactoryException;

public class DateFieldTemplate extends AbstractTemplate
{
  public DateFieldTemplate(String name)
  {
    super(name);
    setParameterDefinition(FIELD_PARAMETER, String.class);
    setParameterDefinition(FORMAT_PARAMETER, String.class);
    setParameterDefinition(NULL_VALUE_PARAMETER, String.class);
  }

  public DataSource createDataSource()
  {
    String format = (String) getParameter(FORMAT_PARAMETER);
    String field = (String) getParameter(FIELD_PARAMETER);
    String nullValue = (String) getParameter(NULL_VALUE_PARAMETER);

    SimpleDateFormatFilter dateFilter = new SimpleDateFormatFilter();
    if (format != null)
    {
      dateFilter.setFormatString(format);
    }
    dateFilter.setDataSource(new DataRowDataSource(field));
    StringFilter filter = new StringFilter();
    filter.setDataSource(dateFilter);
    filter.setNullValue(nullValue);
    return filter;
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
  }
}
