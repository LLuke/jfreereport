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
 * ReportDataRow.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportDataRow.java,v 1.3 2006/11/11 20:37:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jfree.report.DataFlags;
import org.jfree.report.DataRow;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportData;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.DataSet;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.util.IntegerCache;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 20.02.2006, 15:32:32
 *
 * @author Thomas Morgner
 */
public final class ReportDataRow implements DataRow
{
  private Map nameCache;
  private DataFlags[] data;
  private final ReportData reportData;
  private int cursor;

  private ReportDataRow(final ReportData reportData) throws DataSourceException
  {
    if (reportData == null)
    {
      throw new NullPointerException();
    }
    synchronized (reportData)
    {
      this.reportData = reportData;
      this.cursor = reportData.getCursorPosition();
    }
  }

  private void rebuildFromScratch() throws DataSourceException
  {
    final int columnCount = reportData.getColumnCount();
    this.data = new DataFlags[columnCount];

    HashMap nameCache = new HashMap();
    for (int i = 0; i < columnCount; i++)
    {
      final String columnName = reportData.getColumnName(i);
      if (columnName != null)
      {
        nameCache.put(columnName, IntegerCache.getInteger(i));
      }

      final Object value = reportData.get(i);
      this.data[i] = new DefaultDataFlags(columnName, value, true);
    }
    this.nameCache = Collections.unmodifiableMap(nameCache);
  }

  private ReportDataRow(final ReportData reportData,
                        final ReportDataRow reportDataRow)
          throws DataSourceException
  {
    this(reportData);

    if (reportDataRow == null)
    {
      throw new NullPointerException();
    }

    synchronized (reportData)
    {
      final int columnCount = reportData.getColumnCount();
      this.data = new DataFlags[columnCount];

      for (int i = 0; i < columnCount; i++)
      {
        final String columnName = reportData.getColumnName(i);
        final Object value = reportData.get(i);
        final boolean changed = ObjectUtilities.equal
                (value, reportDataRow.get(i));
        this.data[i] = new DefaultDataFlags
                (columnName, value, changed);
      }
      this.nameCache = reportDataRow.nameCache;
    }
  }

  public static ReportDataRow createDataRow(final ReportDataFactory dataFactory,
                                            final String query,
                                            final DataSet parameters)
      throws DataSourceException, ReportDataFactoryException
  {
    final ReportData reportData = dataFactory.queryData(query, parameters);
    final ReportDataRow dataRow = new ReportDataRow(reportData);
    dataRow.rebuildFromScratch();
    return dataRow;
  }

  /**
   * Returns the value of the expression or column in the tablemodel using the
   * given column number as index. For functions and expressions, the
   * <code>getValue()</code> method is called and for columns from the
   * tablemodel the tablemodel method <code>getValueAt(row, column)</code> gets
   * called.
   *
   * @param col the item index.
   * @return the value.
   * @throws IllegalStateException if the datarow detected a deadlock.
   */
  public Object get(int col) throws DataSourceException
  {
    return data[col].getValue();
  }

  /**
   * Returns the value of the function, expression or column using its specific
   * name. The given name is translated into a valid column number and the the
   * column is queried. For functions and expressions, the
   * <code>getValue()</code> method is called and for columns from the
   * tablemodel the tablemodel method <code>getValueAt(row, column)</code> gets
   * called.
   *
   * @param col the item index.
   * @return the value.
   * @throws IllegalStateException if the datarow detected a deadlock.
   */
  public Object get(String col) throws DataSourceException
  {
    Integer colIdx = (Integer) nameCache.get(col);
    if (colIdx == null)
    {
      throw new DataSourceException
              ("Invalid name specified. There is no such column.");
    }

    return data[colIdx.intValue()].getValue();
  }

  /**
   * Returns the name of the column, expression or function. For columns from
   * the tablemodel, the tablemodels <code>getColumnName</code> method is
   * called. For functions, expressions and report properties the assigned name
   * is returned.
   *
   * @param col the item index.
   * @return the name.
   */
  public String getColumnName(int col)
  {
    return data[col].getName();
  }

  /**
   * Returns the number of columns, expressions and functions and marked
   * ReportProperties in the report.
   *
   * @return the item count.
   */
  public int getColumnCount()
  {
    return data.length;
  }

  public DataFlags getFlags(String col) throws DataSourceException
  {
    Integer colIdx = (Integer) nameCache.get(col);
    if (colIdx == null)
    {
      throw new DataSourceException
              ("Invalid name specified. There is no such column.");
    }

    return data[colIdx.intValue()];
  }

  public DataFlags getFlags(int col)
  {
    return data[col];
  }

  /**
   * Advances to the next row and attaches the given master row to the objects
   * contained in that client data row.
   *
   * @param master
   * @return
   */
  public ReportDataRow advance() throws DataSourceException
  {
    synchronized (reportData)
    {
      if (reportData.getCursorPosition() != cursor)
      {
        // directly go to the position we need.
        reportData.setCursorPosition(cursor + 1);
      }
      else
      {
        if (reportData.next() == false)
        {
          throw new DataSourceException(
                  "Unable to advance cursor position");
        }
      }
      return new ReportDataRow(reportData, this);
    }
  }

  public boolean isAdvanceable() throws DataSourceException
  {
    synchronized (reportData)
    {
      if (reportData.getCursorPosition() != cursor)
      {
        // directly go to the position we need.
        reportData.setCursorPosition(cursor);
      }
      return reportData.isAdvanceable();
    }
  }

  public ReportData getReportData()
  {
    return reportData;
  }

  public int getCursor()
  {
    return cursor;
  }
}
