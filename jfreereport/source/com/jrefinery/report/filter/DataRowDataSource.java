/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataRowDataSource.java,v 1.8 2003/06/01 17:39:24 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 * 28-Aug-2002 : Documentation
 * 06-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.filter;

import java.io.Serializable;

import com.jrefinery.report.DataRow;

/**
 * A DataSource that can access values from the 'data-row'. The data-row contains all values from
 * the current row of the report's <code>TableModel</code>, plus the current values of the defined
 * expressions and functions for the report.
 * <p>
 * This class replaces the three classes: <code>ExpressionDataSource</code>,
 * <code>FunctionDataSource</code> and <code>ReportDataSource</code>.
 * <p>
 * @see com.jrefinery.report.DataRow
 *
 * @author Thomas Morgner
 */
public class DataRowDataSource implements DataSource, DataRowConnectable, Serializable
{
  /**  The name of the field/expression/function referenced by this data source. */
  private String dataSourceColumnName;

  /**
   * The DataRow connected with this DataSource. The datarow will not be serialized,
   * as it is assigned during the report processing and we dont support serializing during
   * that state.
   */
  private transient DataRow dataRow;

  /**
   * Default constructor.
   * <p>
   * The expression name is empty ("", not null), the value initially null.
   */
  public DataRowDataSource()
  {
    setDataSourceColumnName("");
  }

  /**
   * Constructs a new data source.
   *
   * @param column  the name of the field, function or expression in the data-row.
   */
  public DataRowDataSource(String column)
  {
    setDataSourceColumnName(column);
  }

  /**
   * Returns the data source column name.
   *
   * @return the column name.
   */
  public String getDataSourceColumnName()
  {
    return dataSourceColumnName;
  }

  /**
   * Defines the name of the column in the datarow to be queried.
   *
   * @param dataSourceColumnName  the name of the column in the datarow to be queried.
   *
   * @throws NullPointerException if the name is <code>null</code>.
   *
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
   * Returns the current value of the data source, obtained from a particular column in the
   * data-row.
   *
   * @return  the value.
   *
   * @throws IllegalStateException if there is no data-row connected.
   */
  public Object getValue()
  {
    if (getDataRow() == null)
    {
      throw new IllegalStateException("No DataRowBackend Connected");
    }
    return getDataRow().get(getDataSourceColumnName());
  }

  /**
   * Clones the data source.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException if the cloning is not supported.
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Connects a data-row to the data source.
   *
   * @param row  the data-row (null not permitted).
   *
   * @throws NullPointerException if the given row is null
   * @throws IllegalStateException if there is a datarow already connected.
   */
  public void connectDataRow(DataRow row) throws IllegalStateException
  {
    if (row == null)
    {
      throw new NullPointerException("DataRowDataSource.connectDataRow: null data-row.");
    }
    if (dataRow != null)
    {
      throw new IllegalStateException("DataRowDataSource.connectDataRow: "
          + "datarow already connected.");
    }
    dataRow = row;
  }

  /**
   * Releases the connection to the data-row.
   * <p>
   * If no datarow is connected, an <code>IllegalStateException</code> is thrown to indicate the
   * programming error.
   *
   * @param row the datarow to be disconnected.
   *
   * @throws NullPointerException if the given row is null
   * @throws IllegalStateException if there is currently no datarow connected.
   */
  public void disconnectDataRow(DataRow row) throws IllegalStateException
  {
    if (row == null)
    {
      throw new NullPointerException("Null-DataRowBackend cannot be disconnected.");
    }
    if (dataRow == null)
    {
      throw new IllegalStateException("There is no datarow connected");
    }
    dataRow = null;
  }

  /**
   * Returns the current data-row.
   *
   * @return the data-row.
   */
  public DataRow getDataRow()
  {
    return dataRow;
  }

}