/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.ImageRefFilter;
import com.jrefinery.report.io.ext.factory.templates.AbstractTemplate;

public class ImageFieldTemplate extends AbstractTemplate
{
  public ImageFieldTemplate()
  {
    setParameterDefinition(FIELD_PARAMETER, String.class);
  }

  public DataSource createDataSource()
  {
    String field = (String) getParameter(FIELD_PARAMETER);

    ImageRefFilter filter = new ImageRefFilter();
    filter.setDataSource(new DataRowDataSource(field));
    return filter;
  }
}
