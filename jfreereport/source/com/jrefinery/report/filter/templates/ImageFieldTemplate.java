/**
 * Date: Jan 14, 2003
 * Time: 1:42:50 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.filter.ImageRefFilter;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DataRowConnectable;

public class ImageFieldTemplate extends AbstractTemplate implements DataRowConnectable
{
  private DataRowDataSource dataRowDataSource;
  private ImageRefFilter imageRefFilter;

  public ImageFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    imageRefFilter = new ImageRefFilter();
    imageRefFilter.setDataSource(dataRowDataSource);
  }

  public String getField()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  public void setField(String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return imageRefFilter.getValue();
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
    ImageFieldTemplate template = (ImageFieldTemplate) super.clone();
    template.imageRefFilter = (ImageRefFilter) imageRefFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) imageRefFilter.getDataSource();
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
