/**
 * Date: Jan 14, 2003
 * Time: 6:27:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.filter.DataRowConnectable;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.ImageLoadFilter;
import com.jrefinery.report.filter.URLFilter;

import java.net.URL;

public class ImageURLFieldTemplate extends AbstractTemplate implements DataRowConnectable
{
  private ImageLoadFilter imageLoadFilter;
  private DataRowDataSource dataRowDataSource;
  private URLFilter urlFilter;

  public ImageURLFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    urlFilter = new URLFilter();
    urlFilter.setDataSource(dataRowDataSource);
    imageLoadFilter = new ImageLoadFilter();
    imageLoadFilter.setDataSource(urlFilter);
  }

  public String getField()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  public void setField(String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  public URL getBaseURL()
  {
    return urlFilter.getBaseURL();
  }

  public void setBaseURL(URL baseURL)
  {
    urlFilter.setBaseURL(baseURL);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return imageLoadFilter.getValue();
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
    ImageURLFieldTemplate template = (ImageURLFieldTemplate) super.clone();
    template.imageLoadFilter = (ImageLoadFilter) imageLoadFilter.clone();
    template.urlFilter = (URLFilter) template.imageLoadFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.urlFilter.getDataSource();
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
