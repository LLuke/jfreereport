/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ---------------------
 * DataRowConnector.java
 * ---------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DataRowConnector.java,v 1.6 2005/01/25 00:22:34 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 * 02-Dec-2002 : Updated connection/disconnection to support band composites.
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 06-Dec-2002 : Added more exceptions when there is no backend connected.
 * 18-Dec-2003 : Added toString() method
 */

package org.jfree.report.states;

import org.jfree.report.DataRow;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.DataTarget;

/**
 * This is the connection-proxy to the various data sources contained in the elements.
 * During report processing the report states get cloned while the elements remain
 * uncloned. The DataRowConnector connects the DataRowBackend (which contains the data)
 * with the stateless elements.
 *
 * @author Thomas Morgner
 * @see DataRowBackend
 */
public class DataRowConnector implements DataRow
{
  /**
   * The data row backend.
   */
  private DataRowBackend dataRow;

  /**
   * Default constructor.
   */
  public DataRowConnector ()
  {
  }

  /**
   * Returns the assigned data row backend.
   *
   * @return the currently assigned DataRowBackend for this DataRowConnector.
   */
  public DataRowBackend getDataRowBackend ()
  {
    return dataRow;
  }

  /**
   * Sets the data row backend for this DataRowConnector. The backend actually contains
   * the data which will be queried, while this DataRowConnector is simply a proxy
   * forwarding all requests to the backend.
   *
   * @param dataRow the data row backend
   */
  public void setDataRowBackend (final DataRowBackend dataRow)
  {
    this.dataRow = dataRow;
  }

  /**
   * Return the value of the function, expression or column in the tablemodel using the
   * column number.
   *
   * @param col the column, function or expression index.
   * @return the column, function or expression value.
   *
   * @throws java.lang.IllegalStateException
   *          if there is no backend connected.
   */
  public Object get (final int col)
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return dataRow.get(col);
  }

  /**
   * Returns the value of the column, function or expression using its name.
   *
   * @param col the column, function or expression index.
   * @return The column, function or expression value.
   *
   * @throws java.lang.IllegalStateException
   *          if there is no backend connected
   */
  public Object get (final String col)
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return dataRow.get(col);
  }

  /**
   * Returns the name of the column, function or expression.
   *
   * @param col the column, function or expression index.
   * @return the column, function or expression name.
   *
   * @throws java.lang.IllegalStateException
   *          if there is no backend connected.
   */
  public String getColumnName (final int col)
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return dataRow.getColumnName(col);
  }

  /**
   * Looks up the position of the column with the name <code>name</code>. returns the
   * position of the column or -1 if no columns could be retrieved.
   *
   * @param name the column, function or expression name.
   * @return the column position of the column, expression or function with the given name
   *         or -1 if the given name does not exist in this DataRow.
   *
   * @throws java.lang.IllegalStateException
   *          if there is no backend connected.
   */
  public int findColumn (final String name)
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return getDataRowBackend().findColumn(name);
  }

  /**
   * Returns the count of columns in this datarow. The columncount is the sum of all
   * DataSource columns, all functions and all expressions.
   *
   * @return the number of accessible columns in this datarow.
   *
   * @throws java.lang.IllegalStateException
   *          if there is no backend connected.
   */
  public int getColumnCount ()
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return getDataRowBackend().getColumnCount();
  }

  /**
   * Queries the last datasource in the chain of targets and filters.
   * <p/>
   * The last datasource is used to feed data into the data processing chain. The result
   * of this computation is retrieved by the element using the registered datasource to
   * query the queue.
   *
   * @param e the data target.
   * @return The last DataSource in the chain.
   */
  public static DataSource getLastDatasource (final DataTarget e)
  {
    if (e == null)
    {
      throw new NullPointerException();
    }
    final DataSource s = e.getDataSource();
    if (s instanceof DataTarget)
    {
      final DataTarget tgt = (DataTarget) s;
      return getLastDatasource(tgt);
    }
    return s;
  }

  /**
   * Returns a string describing the object.
   *
   * @return The string.
   */
  public String toString ()
  {
    if (dataRow == null)
    {
      return "org.jfree.report.states.DataRowConnector=Not Connected";
    }
    return "org.jfree.report.states.DataRowConnector=Connected:" + dataRow.getCurrentRow();

  }
}
