/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * SQLReportData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SQLReportData.java,v 1.1 2006/04/18 11:45:15 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.data.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.jfree.report.DataSourceException;
import org.jfree.report.ReportData;

/**
 * Creation-Date: 19.02.2006, 17:37:42
 *
 * @author Thomas Morgner
 */
public class SQLReportData implements ReportData
{
  private ResultSet resultSet;
  private int rowCount;
  private int columnCount;
  private int cursor;
  private String[] columnNames;
  private boolean labelMapping;

  public SQLReportData(final ResultSet resultSet,
                       final boolean labelMapping)
      throws SQLException, DataSourceException
  {
    if (resultSet == null)
    {
      throw new NullPointerException();
    }
    if (resultSet.getType() == ResultSet.TYPE_FORWARD_ONLY)
    {
      throw new IllegalArgumentException();
    }
    this.resultSet = resultSet;
    this.labelMapping = labelMapping;

    if (resultSet.last())
    {
      rowCount = resultSet.getRow();
    }
    else
    {
      rowCount = 0;
    }

    final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    columnCount = resultSetMetaData.getColumnCount();
    columnNames = new String[columnCount];
    for (int i = 1; i <= columnCount; i++)
    {
      if (labelMapping)
      {
        columnNames[i - 1] = resultSetMetaData.getColumnLabel(i);
      }
      else
      {
        columnNames[i - 1] = resultSetMetaData.getColumnName(i);
      }
    }

    if (resultSet.first() == false)
    {
      throw new DataSourceException("Unable to reset the dataset.");
    }
    cursor = 0;
  }

  public boolean isLabelMapping()
  {
    return labelMapping;
  }

  public int getRowCount() throws DataSourceException
  {
    return rowCount;
  }

  /**
   * This operation checks, whether a call to next will be likely to succeed. If
   * there is a next data row, this should return true.
   *
   * @return
   * @throws org.jfree.report.DataSourceException
   *
   */
  public boolean isAdvanceable() throws DataSourceException
  {
    return cursor < (rowCount - 1);
  }

  public int getColumnCount() throws DataSourceException
  {
    return columnCount;
  }

  public void setCursorPosition(int row) throws DataSourceException
  {
    if (row < 0)
    {
      throw new DataSourceException("Negative row number is not valid");
    }
    if (row >= rowCount)
    {
      throw new DataSourceException("OutOfBounds:");
    }
    try
    {
      if (resultSet.absolute(row + 1))
      {
        cursor = row;
      }
      else
      {
        throw new DataSourceException("Unable to scroll the resultset.");
      }
    }
    catch (SQLException e)
    {
      throw new DataSourceException("Failed to move the cursor: ", e);
    }
  }

  public boolean next() throws DataSourceException
  {
    try
    {
      if (resultSet.next())
      {
        cursor += 1;
        return true;
      }
      else
      {
        return false;
      }
    }
    catch (SQLException e)
    {
      throw new DataSourceException("Failed to move the cursor: ", e);
    }
  }

  public void close() throws DataSourceException
  {
    try
    {
      resultSet.close();
    }
    catch (SQLException e)
    {
      throw new DataSourceException("Failed to close the resultset: ", e);
    }
  }

  public String getColumnName(int column) throws DataSourceException
  {
    return columnNames[column];
  }

  public Object get(int column) throws DataSourceException
  {
    try
    {
      final Object retval = resultSet.getObject(column + 1);
      if (resultSet.wasNull())
      {
        return null;
      }
      return retval;
    }
    catch (SQLException e)
    {
      throw new DataSourceException("Failed to query data", e);
    }
  }

  public int getCursorPosition() throws DataSourceException
  {
    return cursor;
  }
}
