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
 * -------------------
 * DataRowBackend.java
 * -------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DataRowBackend.java,v 1.14 2006/02/08 18:03:56 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 * 01-Sep-2002 : Deadlockcheck added. If a column is accessed twice within a single query, (can
 *               happen on expression evaluation), a IllegalStateException is thrown
 * 02-Sep-2002 : Deadlock detection was no implemented correctly, fixed.
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 13-Sep-2002 : Ran Checkstyle agains the sources
 * 15-Oct-2002 : Functions and Expressions are now contained in an LevelledExpressionList
 * 23-Oct-2002 : Added support for ReportProperty-Queries to the datarow.
 * 06-Dec-2002 : Added configurable Invalid-Column-Warning
 * 05-Feb-2002 : Removed/Changed log statements
 */

package org.jfree.report.states;

import java.util.HashMap;
import java.util.HashSet;
import javax.swing.table.TableModel;

import org.jfree.report.DataRow;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.function.Expression;
import org.jfree.report.function.LevelledExpressionList;
import org.jfree.report.util.ReportPropertiesList;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * The DataRow-Backend maintains the state of a datarow. Whenever the  report state
 * changes the backend of the datarow is updated and then reconnected with the
 * DataRowConnector.
 *
 * @author Thomas Morgner
 * @see DataRowConnector
 */
public class DataRowBackend implements Cloneable
{

  /**
   * The 'warn on invalid columns' property key.
   */
  public static final String WARN_INVALID_COLUMNS_KEY
          = "org.jfree.report.WarnInvalidColumns";

  /**
   * The item cache.
   */
  private final HashMap colcache;
  private HashSet invalidColumns;

  /**
   * The functions (set by the report state).
   */
  private LevelledExpressionList functions;

  /**
   * The table model (set by the report state).
   */
  private TableModel tablemodel;

  /**
   * all previously marked report-properties for this report.
   */
  private ReportPropertiesList reportProperties;

  /**
   * The current row (set by the report state).
   */
  private int currentRow = -1;

  /**
   * Column locks.
   */
  private boolean[] columnlocks;

  /**
   * if true, invalid columns get printed to the logs.
   */
  private final boolean warnInvalidColumns;

  /**
   * An empty boolean array.
   */
  private static final boolean[] EMPTY_BOOLS = new boolean[0];

  /**
   * The index of the last function.
   */
  private int functionsEndIndex;

  /**
   * The index of the last property.
   */
  private int propertiesEndIndex;

  /**
   * The index of the table end.
   */
  private int tableEndIndex;

  /**
   * The last row.
   */
  private int lastRow;

  /**
   * The datarow connector that is used to feed the functions.
   */
  private DataRowConnector dataRowConnector;
  protected static final Integer NOT_FOUND_VALUE = new Integer(-1);
  private DataRowExpressionRuntime expressionRuntime;

  /**
   * Creates a new DataRowBackend.
   */
  public DataRowBackend (final ResourceBundleFactory resourceBundleFactory,
                         final Configuration configuration,
                         final String exportDescriptor)
  {
    columnlocks = EMPTY_BOOLS;
    dataRowConnector = new DataRowConnector();
    dataRowConnector.setDataRowBackend(this);
    colcache = new HashMap();
    warnInvalidColumns =
            JFreeReportBoot.getInstance().getExtendedConfig().getBoolProperty
                    (WARN_INVALID_COLUMNS_KEY);
    if (warnInvalidColumns)
    {
      invalidColumns = new HashSet();
    }
    lastRow = -1;
    expressionRuntime = new DataRowExpressionRuntime
            (this, configuration, resourceBundleFactory, exportDescriptor);

    revalidateColumnLock();
  }

  /**
   * Creates a new DataRowBackend based on the given datarow backend. Both datarow
   * backends will share the objects, no cloning is done. Functions will not be included
   * in the copy...
   *
   * @param db the data row backend.
   */
  protected DataRowBackend (final DataRowBackend db)
  {
    this.dataRowConnector = new DataRowConnector();
    this.dataRowConnector.setDataRowBackend(this);
    this.columnlocks = EMPTY_BOOLS;
    this.colcache = new HashMap();
    this.warnInvalidColumns = db.warnInvalidColumns;
    this.tablemodel = db.tablemodel;
    this.lastRow = db.lastRow;
    this.reportProperties = db.reportProperties;
    this.invalidColumns = db.invalidColumns;
    this.expressionRuntime = db.expressionRuntime;
    revalidateColumnLock();

  }

  /**
   * Returns the datarow connector used to connect the functions and element with this
   * datarow.
   *
   * @return the datarow connector.
   */
  protected DataRowConnector getDataRowConnector ()
  {
    return dataRowConnector;
  }

  /**
   * Returns the public datarow instance used to query this datarow..
   *
   * @return the datarow.
   */
  public DataRow getDataRow ()
  {
    return getDataRowConnector();
  }

  /**
   * Returns the function collection used in this DataRowBackend. The FunctionCollection
   * is stateful and will be set to a new function collection when the ReportState
   * advances.
   *
   * @return the currently set function collection
   */
  public LevelledExpressionList getFunctions ()
  {
    return functions;
  }

  /**
   * Returns the tablemodel used in this DataRowBackend.
   *
   * @return the TableModel of the Report.
   */
  public TableModel getTablemodel ()
  {
    return tablemodel;
  }

  /**
   * Returns the current row in the tablemodel. The row is advanced while the report is
   * processed. If the row is negative, the DataRowBackend <code>isBeforeFirstRow</code>.
   *
   * @return the current row.
   */
  public int getCurrentRow ()
  {
    return currentRow;
  }

  /**
   * Sets the current row of the tablemodel. The current row is advanced while the Report
   * is being processed.
   *
   * @param currentRow the current row
   */
  public void setCurrentRow (final int currentRow)
  {
    if (currentRow < -1)
    {
      throw new IllegalArgumentException("CurrentRow must not be < -1");
    }
    if (getTablemodel() != null)
    {
      // in some cases, the current row can be +1 greater than the row count.
      // (when empty table model given or in preview at the end of the table)
      if (currentRow > getTablemodel().getRowCount())
      {
        throw new IllegalArgumentException
                ("CurrentRow cannot be greater than the tablemodel's rowcount." + currentRow);
      }
    }
    else
    {
      if (currentRow != -1)
      {
        throw new IllegalArgumentException
                ("Without an TableModel, the currentRow must always be -1");
      }
    }
    this.currentRow = currentRow;
  }

  public DataRowExpressionRuntime getExpressionRuntime()
  {
    return expressionRuntime;
  }

  /**
   * Sets the function collection used in this DataRow. This also updates the function's
   * dataRow reference.
   *
   * @param functions the current function collection
   */
  public void setFunctions (final LevelledExpressionList functions)
  {
    if (this.functions != null)
    {
      // remove the old dataRow
      this.functions.setExpressionRuntime(null);
    }
    this.functions = functions;
    if (this.functions != null)
    {
      // and connect the new one...
      this.functions.setExpressionRuntime(getExpressionRuntime());
    }
    revalidateColumnLock();
  }

  /**
   * sets the tablemodel used in this DataRow. The tablemodel contains the base values for
   * the report and the currentRow-property contains a pointer to the current row within
   * the tablemodel.
   *
   * @param tablemodel the tablemodel used as base for the reporting
   */
  public void setTablemodel (final TableModel tablemodel)
  {
    this.tablemodel = tablemodel;
    if (tablemodel != null)
    {
      this.lastRow = (tablemodel.getRowCount() - 1);
    }
    else
    {
      this.lastRow = -1;
    }
    revalidateColumnLock();
  }

  /**
   * Returns the value of the function, expression or column in the tablemodel using the
   * column number.
   *
   * @param column the item index.
   * @return The item value.
   *
   * @throws IndexOutOfBoundsException if the index is negative or greater than the number
   *                                   of columns in this row.
   * @throws IllegalStateException     if a deadlock is detected.
   */
  public Object get (final int column)
  {
    if (column >= getColumnCount())
    {
      throw new IndexOutOfBoundsException("requested " + column + " , have " + getColumnCount());
    }
    if (column < 0)
    {
      throw new IndexOutOfBoundsException("Column with negative index is invalid");
    }
    if (columnlocks[column])
    {
      throw new IllegalStateException("Column " + column + " already accessed. Deadlock!");
    }

    Object returnValue = null;
    try
    {
      columnlocks[column] = true;
      int col = column;
      if (col < getTableEndIndex())
      {
        // Handle Pos == BEFORE_FIRST_ROW
        final int currentRow = getCurrentRow();
        if (currentRow < 0 || currentRow > lastRow)
        {
          returnValue = null;
        }
        else
        {
          returnValue = getTablemodel().getValueAt(currentRow, col);
        }
      }
      else if (col < getFunctionEndIndex())
      {
        col -= getTableEndIndex();
        // the datarow preview will make sure that only expressions are
        // contained in the function collection.
        returnValue = getFunctions().getValue(col);
      }
      else if (col < getPropertiesEndIndex())
      {
        col -= getFunctionEndIndex();
        return getReportProperties().get(col);
      }
    }
    catch (IllegalStateException ise)
    {
      throw ise;
    }
    catch (Exception e)
    {
      Log.error(new org.jfree.util.Log.SimpleMessage("Column ", new Integer(column),
              " caused an error on get()", e));
    }
    finally
    {
      columnlocks[column] = false;
    }
    return returnValue;
  }

  /**
   * Returns the value of the function, expression or column using its specific name. This
   * method returns null if the named column was not found.
   *
   * @param name the item name.
   * @return The item value.
   *
   * @throws IndexOutOfBoundsException if the index is negative or greater than the number
   *                                   of columns in this row.
   * @throws IllegalStateException     if a deadlock is detected.
   */
  public Object get (final String name)
  {
    final int idx = findColumn(name);
    if (idx == -1)
    {
      if (warnInvalidColumns)
      {
        if (invalidColumns.contains(name) == false)
        {
          // print an warning for the logs.
          Log.warn(new Log.SimpleMessage
                  ("Invalid column name specified on query: ", name));
          invalidColumns.add(name);
        }
      }
      return null;
    }
    return get(idx);
  }

  /**
   * Returns the count of columns in this datarow. The column count is the sum of all
   * DataSource columns, all Functions and all expressions.
   *
   * @return the number of accessible columns in this datarow.
   */
  public int getColumnCount ()
  {
    return propertiesEndIndex;
  }

  /**
   * Looks up the position of the column with the name <code>name</code>. returns the
   * position of the column or -1 if no columns could be retrieved.
   *
   * @param name the item name.
   * @return the column position of the column, expression or function with the given name
   *         or -1 if the given name does not exist in this DataRow.
   */
  public int findColumn (final String name)
  {
    final Integer integ = (Integer) colcache.get(name);
    if (integ != null)
    {
      return integ.intValue();
    }

    final int size = getColumnCount();
    for (int i = 0; i < size; i++)
    {
      final String column = getColumnName(i);
      if (column.equals(name))
      {
        colcache.put(name, new Integer(i));
        return i;
      }
    }
    colcache.put(name, NOT_FOUND_VALUE);
    return -1;
  }

  /**
   * Returns the name of the column, expression or function.
   *
   * @param col the column, expression or function index.
   * @return The column, expression or function name.
   */
  public String getColumnName (int col)
  {
    if (col >= getColumnCount())
    {
      throw new IndexOutOfBoundsException("requested " + col + " , have " + getColumnCount());
    }
    if (col < 0)
    {
      throw new IndexOutOfBoundsException("Requested Column cannot be negative");
    }
    if (col < getTableEndIndex())
    {
      return getTablemodel().getColumnName(col);
    }
    else if (col < getFunctionEndIndex())
    {
      col -= getTableEndIndex();
      final Expression f = getFunctions().getExpression(col);
      if (f == null)
      {
        Log.warn("No such function: " + col);
        return null;
      }
      return f.getName();
    }
    else
    {
      col -= getFunctionEndIndex();
      return getReportProperties().getColumnName(col);
    }
  }

  /**
   * Tests whether the current row is set before the first row in the tablemodel.
   *
   * @return true, if the processing has not yet started and the currentRow is lesser than
   *         0.
   */
  public boolean isBeforeFirstRow ()
  {
    return getCurrentRow() < 0;
  }

  /**
   * Tests whether the current row in the tablemodel is the last row in the tablemodel.
   *
   * @return true, if the current row is the last row in the tablemodel.
   */
  public boolean isLastRow ()
  {
    return getCurrentRow() == lastRow;
  }

  /**
   * Clones this DataRowBackend. Does also clone the functions contained in this datarow.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final DataRowBackend db = (DataRowBackend) super.clone();
    db.columnlocks = new boolean[getColumnCount()];
    db.expressionRuntime = new DataRowExpressionRuntime
            (db, expressionRuntime.getConfiguration(),
                    expressionRuntime.getResourceBundleFactory(),
                    expressionRuntime.getExportDescriptor());
    db.dataRowConnector = new DataRowConnector();
    db.dataRowConnector.setDataRowBackend(db);
    if (functions != null)
    {
      db.functions = (LevelledExpressionList) functions.clone();
      db.functions.setExpressionRuntime(db.expressionRuntime);
    }
    return db;
  }

  /**
   * Calculates the end index for tableentries. TableEntries are the first entries in the
   * row.
   *
   * @return The end index.
   */
  private int getTableEndIndex ()
  {
    return tableEndIndex;
  }

  /**
   * Calculates the endindex for function entries. Function entries are following the
   * TableEntries in the row.
   *
   * @return The function end index.
   */
  private int getFunctionEndIndex ()
  {
    return functionsEndIndex;
  }

  /**
   * Returns the end index of the properties.
   *
   * @return the index.
   */
  private int getPropertiesEndIndex ()
  {
    return propertiesEndIndex;
  }

  /**
   * Ensures that the columnLock does match the number of columns in this row.
   */
  private void revalidateColumnLock ()
  {
    if (getTablemodel() == null)
    {
      tableEndIndex = 0;
    }
    else
    {
      tableEndIndex = getTablemodel().getColumnCount();
    }

    if (getFunctions() == null)
    {
      functionsEndIndex = tableEndIndex;
    }
    else
    {
      functionsEndIndex = tableEndIndex + getFunctions().size();
    }

    if (getReportProperties() == null)
    {
      propertiesEndIndex = functionsEndIndex;
    }
    else
    {
      propertiesEndIndex = functionsEndIndex + getReportProperties().getColumnCount();
    }

    if (getColumnCount() != columnlocks.length)
    {
      columnlocks = new boolean[getColumnCount()];
    }
  }

  /**
   * Returns the report properties.
   *
   * @return the report properties.
   */
  public ReportPropertiesList getReportProperties ()
  {
    return reportProperties;
  }

  /**
   * Sets the report properties.
   *
   * @param properties the report properties.
   */
  public void setReportProperties (final ReportPropertiesList properties)
  {
    this.reportProperties = properties;
    revalidateColumnLock();
  }
}
