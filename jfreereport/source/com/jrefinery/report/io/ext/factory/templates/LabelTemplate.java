/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: LabelTemplate.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplate;
import com.jrefinery.report.io.ext.factory.objects.ObjectFactoryException;

public class LabelTemplate extends AbstractTemplate
{
  public LabelTemplate(String name)
  {
    super(name);
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

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
  }
}
