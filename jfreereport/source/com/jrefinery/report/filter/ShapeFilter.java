/**
 * Date: Mar 7, 2003
 * Time: 5:43:31 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter;

import java.awt.Shape;

public class ShapeFilter implements DataFilter
{
  /**
   * Default constructor.
   */
  public ShapeFilter()
  {
  }

  /** The data source. */
  private DataSource dataSource;

  /**
   * Returns the data source for the filter.
   *
   * @return The data source.
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Sets the data source for the filter.
   *
   * @param dataSource The data source.
   */
  public void setDataSource(DataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  /**
   * Returns the current value for the data source.
   * <P>
   * The returned object, unless it is null, will be an instance of ImageReference.
   *
   * @return The value.
   */
  public Object getValue()
  {
    DataSource ds = getDataSource();
    if (ds == null)
    {
      return null;
    }
    Object o = ds.getValue();
    if (o instanceof Shape)
    {
      return o;
    }
    return null;
  }

  /**
   * Clones the filter.
   *
   * @return A clone of this filter.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    ShapeFilter r = (ShapeFilter) super.clone();
    if (dataSource != null)
    {
      r.dataSource = (DataSource) dataSource.clone();
    }
    return r;
  }


}
