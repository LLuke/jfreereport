/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * ScrollableResultSetTableModel.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 25-Apr-2002 : Initial version
 */
package com.jrefinery.report.util;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * A tableModel which is backed up by a java.sql.ResultSet. Use this to directly feed your
 * database data into JFreeReport. If you have trouble using this TableModel and you have
 * either enough memory or your query result is not huge, you may want to use
 * <code>ResultSetTableModelFactory.generateDefaultTableModel (ResultSet rs)</code>.
 * That implementation will read all data from the given ResultSet and keep that data in
 * memory.
 */
public class ScrollableResultSetTableModel extends AbstractTableModel
{
  /**
   * The scrollable ResultSet source.
   */
  protected ResultSet resultset;

  /**
   * The ResultSetMetaData object for this result set.
   */
  protected ResultSetMetaData dbmd;

  /**
   * The number of rows in the result set.
   */
  private int rowCount;

  /**
   * Constructs the model.
   */
  public ScrollableResultSetTableModel(ResultSet resultset) throws SQLException
  {
    if (resultset != null)
    {
      updateResultSet(resultset);
    }
    else
    {
      clear();
    }
  }

  /**
   * DefaultConstructor.
   */
  protected ScrollableResultSetTableModel()
  {
  }

  /**
   * Updates the result set in this model with the given ResultSet object.
   */
  public void updateResultSet(ResultSet resultset) throws SQLException
  {
    if (this.resultset != null)
    {
      clear();
    }

    this.resultset = resultset;
    this.dbmd = resultset.getMetaData();

    if (resultset.last())
    {
      rowCount = resultset.getRow();
    }
    else
    {
      rowCount = 0;
    }

    fireTableStructureChanged();
  }

  /**
   * Clears the model of the current result set.
   */
  public void clear()
  {
    // Close the old result set if needed.
    if (resultset != null)
    {
      try
      {
        resultset.close();
      }
      catch (SQLException e)
      {
        // Just in case the JDBC driver can't close a result set twice.
        //				e.printStackTrace();
      }
    }
    resultset = null;
    dbmd = null;
    rowCount = 0;
    fireTableStructureChanged();
  }

  /**
   * get an rowCount. This can be a very expensive operation on large
   * datasets. Returns -1 if the total amount of rows is not known to the result set.
   */
  public int getRowCount()
  {
    if (resultset == null)
      return 0;

    try
    {
      if (resultset.last())
      {
        rowCount = resultset.getRow();
        if (rowCount == -1)
        {
          rowCount = 0;
        }
      }
      else
      {
        rowCount = 0;
      }
    }
    catch (SQLException sqle)
    {
      return 0;
    }
    return rowCount;
  }

  /**
   * Returns the number of columns in the ResultSet. Returns 0 if no result set is set
   * or the column count could not be retrieved.
   *
   * @see ResultSetMetaData.getColumnCount
   */
  public int getColumnCount()
  {
    if (resultset == null)
      return 0;

    if (dbmd != null)
    {
      try
      {
        return dbmd.getColumnCount();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return 0;
  }

  /**
   * Returns the columnLabel for the given column.
   *
   * @see ResultSetMetaData.getColumnLabel
   */
  public String getColumnName(int column)
  {
    if (dbmd != null)
    {
      try
      {
        return dbmd.getColumnLabel(column + 1);
      }
      catch (SQLException e)
      {
      }
    }
    return null;
  }

  public Object getValueAt(int row, int column)
  {
    if (resultset != null)
    {
      try
      {
        resultset.absolute(row + 1);
        return resultset.getObject(column + 1);
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return null;
  }

  public Class getColumnClass(int column)
  {
    if (dbmd != null)
    {
      try
      {
        return Class.forName(getColumnClassName(column));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return Object.class;
  }

  public String getColumnClassName(int column)
  {
    if (dbmd != null)
    {
      try
      {
        return mckoiDBFixClassName(dbmd.getColumnClassName(column + 1));
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return Object.class.getName();
  }

  /**
   * Just removes the word class from the start of the classname string
   * McKoiDB version 0.92 was not able to properly return classnames of
   * resultset elements.
   */
  private String mckoiDBFixClassName(String classname)
  {
    if (classname.startsWith("class "))
    {
      return classname.substring(6).trim();
    }
    return classname;
  }
}