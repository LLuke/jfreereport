package org.jfree.report.filter;

import org.jfree.report.Anchor;

public class AnchorFilter implements DataFilter
{
  private DataSource dataSource;

  public AnchorFilter ()
  {
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final AnchorFilter af = (AnchorFilter) super.clone();
    af.dataSource = null;
    return af;
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue ()
  {
    if (dataSource == null)
    {
      return null;
    }
    final Object value = dataSource.getValue();
    if (value instanceof Anchor)
    {
      return value;
    }
    if (value == null)
    {
      return null;
    }
    return new Anchor(String.valueOf(value));
  }

  /**
   * Returns the assigned DataSource for this Target.
   *
   * @return The datasource.
   */
  public DataSource getDataSource ()
  {
    return dataSource;
  }

  /**
   * Assigns a DataSource for this Target.
   *
   * @param ds The data source.
   */
  public void setDataSource (final DataSource ds)
  {
    this.dataSource = ds;
  }
}
