/**
 * Date: Mar 7, 2003
 * Time: 5:46:55 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter.templates;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.filter.DataRowConnectable;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.ShapeFilter;

public class ShapeFieldTemplate extends AbstractTemplate implements DataRowConnectable
{
  /** The data row reader. */
  private DataRowDataSource dataRowDataSource;

  /** A shape filter. */
  private ShapeFilter shapeFilter;

  /**
   * Creates a new shape field template.
   */
  public ShapeFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    shapeFilter = new ShapeFilter();
    shapeFilter.setDataSource(dataRowDataSource);
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field  the field name.
   */
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
    return shapeFilter.getValue();
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    ShapeFieldTemplate template = (ShapeFieldTemplate) super.clone();
    template.shapeFilter = (ShapeFilter) shapeFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.shapeFilter.getDataSource();
    return template;
  }

  /**
   * Connects a {@link com.jrefinery.report.DataRow} to the data source.
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
