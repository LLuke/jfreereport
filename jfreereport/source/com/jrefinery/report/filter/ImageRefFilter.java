/**
 *
 *  Date: 28.06.2002
 *  ImageRefFilter.java
 *  ------------------------------
 *  28.06.2002 : ...
 */
package com.jrefinery.report.filter;

import com.jrefinery.report.ImageReference;

import java.awt.Image;
import java.io.IOException;

public class ImageRefFilter implements DataFilter
{
  public ImageRefFilter ()
  {
  }

  private DataSource dataSource;

  public DataSource getDataSource ()
  {
    return dataSource;
  }

  public void setDataSource (DataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  /**
   * Returns the current value for the data source.
   *
   * @return The value.
   */
  public Object getValue ()
  {
    DataSource ds = getDataSource();
    if (ds == null) return null;
    Object o = ds.getValue();
    if (o == null || (o instanceof Image) == false) return null;

    return new ImageReference ((Image) o);
  }

  public Class getDataType ()
  {
    return ImageReference.class;
  }
}