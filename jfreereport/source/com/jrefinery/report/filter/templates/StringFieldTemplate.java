/**
 * Date: Jan 14, 2003
 * Time: 6:40:52 PM
 *
 * $Id: StringFieldTemplate.java,v 1.2 2003/01/15 16:54:06 taqua Exp $
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.filter.DataRowConnectable;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.StringFilter;

public class StringFieldTemplate extends AbstractTemplate implements DataRowConnectable
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

  /**
   * Connects the DataRow to the data source.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void connectDataRow(DataRow row) throws IllegalStateException
  {
    dataRowDataSource.connectDataRow(row);
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
    dataRowDataSource.disconnectDataRow(row);
  }
}
