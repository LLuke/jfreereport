/**
 * Date: Jan 14, 2003
 * Time: 6:40:52 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.filter.DecimalFormatFilter;

public class StringFieldTemplate extends AbstractTemplate
{
  private DataRowDataSource dataRowDataSource;
  private StringFilter stringFilter;

  public StringFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    stringFilter = new StringFilter();
    stringFilter.setDataSource(dataRowDataSource);
  }

  public String getField()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  public void setField(String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  public String getNullValue()
  {
    return stringFilter.getNullValue();
  }

  public void setNullValue(String nullValue)
  {
    stringFilter.setNullValue(nullValue);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return stringFilter.getValue();
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    StringFieldTemplate template = (StringFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.stringFilter.getDataSource();
    return template;
  }

}
