/**
 * Date: Jan 13, 2003
 * Time: 6:49:58 PM
 *
 * $Id$
 */
package com.jrefinery.report.filter;

public class TemplateDataFilter implements DataFilter
{
  private DataSource ds;
  private String name;

  public TemplateDataFilter(String name)
  {
    if (name == null)
      throw new NullPointerException();

    this.name = name;
  }

  public TemplateDataFilter(DataSource ds, String name)
  {
    this (name);
    this.ds = ds;
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    if (getDataSource() == null)
      return null;
    return getDataSource().getValue();
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
    TemplateDataFilter filter = (TemplateDataFilter) super.clone();
    if (ds != null)
    {
      filter.ds = (DataSource) ds.clone();
    }
    return filter;
  }

  /**
   * Returns the assigned DataSource for this Target.
   *
   * @return The datasource.
   */
  public DataSource getDataSource()
  {
    return ds;
  }

  /**
   * Assigns a DataSource for this Target.
   *
   * @param ds The data source.
   */
  public void setDataSource(DataSource ds)
  {
    this.ds = ds;
  }

  public String getName()
  {
    return name;
  }
}
