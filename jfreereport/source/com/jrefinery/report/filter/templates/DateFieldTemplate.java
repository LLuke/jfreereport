/**
 * Date: Jan 14, 2003
 * Time: 1:14:54 PM
 *
 * $Id: DateFieldTemplate.java,v 1.1 2003/01/14 21:04:43 taqua Exp $
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.filter.DataRowConnectable;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.SimpleDateFormatFilter;
import com.jrefinery.report.filter.StringFilter;

import java.text.SimpleDateFormat;

public class DateFieldTemplate extends AbstractTemplate implements DataRowConnectable
{
  private SimpleDateFormatFilter dateFilter;
  private DataRowDataSource dataRowDataSource;
  private StringFilter stringFilter;

  public DateFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    dateFilter = new SimpleDateFormatFilter();
    dateFilter.setDataSource(dataRowDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(dateFilter);
  }

  public String getFormat()
  {
    return getDateFilter().getFormatString();
  }

  public void setFormat(String format)
  {
    getDateFilter().setFormatString(format);
  }

  public String getField()
  {
    return getDataRowDataSource().getDataSourceColumnName();
  }

  public void setField(String field)
  {
    getDataRowDataSource().setDataSourceColumnName(field);
  }

  public String getNullValue()
  {
    return getStringFilter().getNullValue();
  }

  public void setNullValue(String nullValue)
  {
    getStringFilter().setNullValue(nullValue);
  }

  public SimpleDateFormat getDateFormat()
  {
    return (SimpleDateFormat) getDateFilter().getFormatter();
  }

  public void setDateFormat(SimpleDateFormat dateFormat)
  {
    getDateFilter().setFormatter(dateFormat);
  }

  /**
   * Connects the DataRow to the data source.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void connectDataRow(DataRow row) throws IllegalStateException
  {
    getDataRowDataSource().connectDataRow(row);
  }

  /**
   * Releases the connection to the data row.
   * <p>
   * If no data row is connected, an <code>IllegalStateException</code> is thrown to indicate the
   * programming error.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void disconnectDataRow(DataRow row) throws IllegalStateException
  {
    getDataRowDataSource().disconnectDataRow(row);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return getStringFilter().getValue();
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
    DateFieldTemplate template = (DateFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.dateFilter = (SimpleDateFormatFilter) template.stringFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.dateFilter.getDataSource();
    return template;
  }

  protected SimpleDateFormatFilter getDateFilter()
  {
    return dateFilter;
  }

  protected DataRowDataSource getDataRowDataSource()
  {
    return dataRowDataSource;
  }

  protected StringFilter getStringFilter()
  {
    return stringFilter;
  }

}
