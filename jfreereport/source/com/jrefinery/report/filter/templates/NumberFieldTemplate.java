/**
 * Date: Jan 14, 2003
 * Time: 6:35:22 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DecimalFormatFilter;
import com.jrefinery.report.filter.StringFilter;

import java.text.DecimalFormat;

public class NumberFieldTemplate
{
  private DecimalFormatFilter decimalFormatFilter;
  private DataRowDataSource dataRowDataSource;
  private StringFilter stringFilter;

  public NumberFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    decimalFormatFilter = new DecimalFormatFilter();
    decimalFormatFilter.setDataSource(dataRowDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(decimalFormatFilter);
  }

  public DecimalFormat getDecimalFormat()
  {
    return (DecimalFormat) decimalFormatFilter.getFormatter();
  }

  public void setDecimalFormat(DecimalFormat decimalFormat)
  {
    decimalFormatFilter.setFormatter(decimalFormat);
  }

  public String getFormat()
  {
    return decimalFormatFilter.getFormatString();
  }

  public void setFormat(String format)
  {
    decimalFormatFilter.setFormatString(format);
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
    NumberFieldTemplate template = (NumberFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.decimalFormatFilter = (DecimalFormatFilter) template.stringFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.decimalFormatFilter.getDataSource();
    return template;
  }


}
