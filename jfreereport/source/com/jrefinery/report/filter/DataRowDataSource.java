/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ----------------------
 * DataRowDataSource.java
 * ----------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * $Id$
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 * 28-Aug-2002 : Documentation
 */
package com.jrefinery.report.filter;

import com.jrefinery.report.DataRow;

/**
 * A DataSource that queries the datarow for the value in a named column. The DataRow
 * contains all values from the current row of the report's TableModel and the current value of
 * the defined expressions and functions.
 * <p>
 * This class replaces the three classes: ExpressionDataSource, FunctionDataSource and
 * ReportDataSource.
 * <p>
 * @see com.jrefinery.report.DataRow
 *
 * @author TM
 */
public class DataRowDataSource implements DataSource, DataRowConnectable
{
  /**
   * The name of the field/expression/function referenced by this data source.
   */
  private String dataSourceColumnName;

  /**
   * the DataRow connected with this DataSource.
   */
  private DataRow dataRow;

  /**
   * Default constructor. The expression name is empty ("", not null), the value initially
   * null.
   */
  public DataRowDataSource ()
  {
    setDataSourceColumnName("");
  }

  /**
   * Constructs a new expression data source.
   *
   * @param expression The expression.
   */
  public DataRowDataSource (String expression)
  {
    setDataSourceColumnName(expression);
  }

  /**
   * Returns the data source column name.
   *
   * @return  the column name.
   */
  public String getDataSourceColumnName()
  {
    return dataSourceColumnName;
  }

  /**
   * Defines the name of the column in the datarow to be queried.
   *
   * @param dataSourceColumnName the name of the column in the datarow to be queried.
   * @throws NullPointerException if the name is null
   * @see com.jrefinery.report.DataRow#get
   */
  public void setDataSourceColumnName(String dataSourceColumnName)
  {
    if (dataSourceColumnName == null)
    {
      throw new NullPointerException();
    }
    this.dataSourceColumnName = dataSourceColumnName;
  }

  /**
   * Returns the value of the dataRow-column named by the DataSourceColumnName.
   *
   * @return The value.
   * @throws IllegalStateException if there is no datarow connected.
   */
  public Object getValue ()
  {
    if (getDataRow () == null)
    {
      throw new IllegalStateException ("No DataRowBackend Connected");
    }
    return getDataRow ().get (getDataSourceColumnName());
  }

  /**
   * @return a clone of this DataRowDataSource
   * @throws CloneNotSupportedException if the cloning is not supported.
   */
  public Object clone () throws CloneNotSupportedException
  {
    return super.clone ();
  }

  /**
   * Connects the DataRowBackend with the named DataSource or DataFilter.
   * The filter is now able to query the other DataSources to compute the result.
   * <p>
   * If there is already a datarow connected, an IllegalStateException is thrown.
   *
   * @param row the datarow to be connected.
   *
   * @throws NullPointerException if the given row is null
   * @throws IllegalStateException if there is a datarow already connected.
   */
  public void connectDataRow (DataRow row) throws IllegalStateException
  {
    if (row == null)
    {
      throw new NullPointerException ("Null-DataRowBackend cannot be set.");
    }
    if (dataRow != null)
    {
      throw new IllegalStateException ("There is a datarow already connected");
    }
    dataRow = row;
  }

  /**
   * Releases the connection to the datarow. If no datarow is connected, an
   * IllegalStateException is thrown to indicate the programming error.
   *
   * @throws NullPointerException if the given row is null
   * @throws IllegalStateException if there is currently no datarow connected.
   * @param row the datarow to be disconnected.
   */
  public void disconnectDataRow (DataRow row) throws IllegalStateException
  {
    if (row == null)
    {
      throw new NullPointerException ("Null-DataRowBackend cannot be disconnected.");
    }
    if (dataRow == null)
    {
      throw new IllegalStateException ("There is no datarow connected");
    }
    dataRow = null;
  }

  /**
   * @return the datarow connected with this datasource.
   */
  protected DataRow getDataRow ()
  {
    return dataRow;
  }

}