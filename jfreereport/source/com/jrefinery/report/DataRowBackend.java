/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataRowBackend.java,v 1.34 2003/04/06 18:10:53 taqua Exp $
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

package com.jrefinery.report;

import java.util.HashMap;

import javax.swing.table.TableModel;

import com.jrefinery.report.function.Expression;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.LevelledExpressionList;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.report.util.ReportPropertiesList;

/**
 * The DataRow-Backend maintains the state of a datarow. Whenever the  report state changes
 * the backend of the datarow is updated and then reconnected with the DataRowConnector.
 *
 * @see DataRowConnector
 *
 * @author Thomas Morgner
 */
public class DataRowBackend implements Cloneable
{

  /** The item cache. */
  private HashMap colcache;

  /** The preview DataRowBackend. */
  private DataRowPreview preview;

  /** The functions (set by the report state). */
  private LevelledExpressionList functions;

  /** The table model (set by the report state). */
  private TableModel tablemodel;

  /** all previously marked report-properties for this report. */
  private ReportPropertiesList reportProperties;

  /** The current row (set by the report state). */
  private int currentRow = -1;

  /** Column locks. */
  private boolean[] columnlocks;

  /** if true, invalid columns get printed to the logs. */
  private boolean warnInvalidColumns;

  /** An empty boolean array. */
  private static final boolean[] EMPTY_BOOLS = new boolean[0];

  /** The index of the last function. */
  private int functionsEndIndex;
  
  /** The index of the last property. */
  private int propertiesEndIndex;
  
  /** The index of the table end. */
  private int tableEndIndex;
  
  /** The last row. */
  private int lastRow;

  /**
   * Creates a new DataRowBackend.
   */
  public DataRowBackend()
  {
    columnlocks = EMPTY_BOOLS;
    colcache = new HashMap();
    warnInvalidColumns = ReportConfiguration.getGlobalConfig().isWarnInvalidColumns();
    tableEndIndex = -1;
    propertiesEndIndex = -1;
    functionsEndIndex = -1;
    lastRow = -1;
  }

  /**
   * Creates a new DataRowBackend.
   * 
   * @param db  the data row backend.
   */
  protected DataRowBackend(DataRowBackend db)
  {
    this.columnlocks = EMPTY_BOOLS;
    this.colcache = db.colcache;
    this.warnInvalidColumns = db.warnInvalidColumns;
    this.tableEndIndex = db.tableEndIndex;
    this.tablemodel = db.tablemodel;
    this.lastRow = db.lastRow;
    this.functions = db.functions;
    this.reportProperties = db.reportProperties;
    this.propertiesEndIndex = db.propertiesEndIndex;
    this.functionsEndIndex = db.functionsEndIndex;
    revalidateColumnLock();
  }

  /**
   * Returns the function collection used in this DataRowBackend. The FunctionCollection is
   * stateful and will be set to a new function collection when the ReportState advances.
   *
   * @return the currently set function collection
   */
  public LevelledExpressionList getFunctions()
  {
    return functions;
  }

  /**
   * Returns the tablemodel used in this DataRowBackend.
   *
   * @return the TableModel of the Report.
   */
  public TableModel getTablemodel()
  {
    return tablemodel;
  }

  /**
   * Returns the current row in the tablemodel. The row is advanced while the report is processed.
   * If the row is negative, the DataRowBackend <code>isBeforeFirstRow</code>.
   *
   * @return the current row.
   */
  public int getCurrentRow()
  {
    return currentRow;
  }

  /**
   * Sets the current row of the tablemodel. The current row is advanced while the Report is being
   * processed.
   *
   * @param currentRow the current row
   */
  public void setCurrentRow(int currentRow)
  {
    this.currentRow = currentRow;
  }

  /**
   * Sets the function collection used in this DataRow. As the function collection is stateful,
   * a new instance of the function collection is set for every new ReportState.
   *
   * @param functions the current function collection
   */
  public void setFunctions(LevelledExpressionList functions)
  {
    if (functions == null)
    {
      throw new NullPointerException();
    }
    this.functions = functions;
    revalidateColumnLock();
  }

  /**
   * sets the tablemodel used in this DataRow. The tablemodel contains the base values for the
   * report and the currentRow-property contains a pointer to the current row within the
   * tablemodel.
   *
   * @param tablemodel the tablemodel used as base for the reporting
   */
  public void setTablemodel(TableModel tablemodel)
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
   * Returns the value of the function, expression or column in the tablemodel using the column
   * number.
   *
   * @param column  the item index.
   *
   * @return The item value.
   *
   * @throws IndexOutOfBoundsException if the index is negative or greater than the number of
   *         columns in this row.
   * @throws IllegalStateException if a deadlock is detected.
   */
  public Object get(int column)
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
          Log.debug ("CurrentRow: " + currentRow + " LastRow: " + lastRow);
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

        /** if this is a preview state, forbit the query of functions or be doomed */
        if (isPreviewMode() && getFunctions().getExpression(col) instanceof Function)
        {
          throw new IllegalStateException("Cannot query a function from a preview state");
        }
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
      Log.error(new Log.SimpleMessage("Column " , new Integer(column),
                                      " caused an error on get()", e));
    }
    finally
    {
      columnlocks[column] = false;
    }
    return returnValue;
  }

  /**
   * Returns a flag indicating whether this object is in 'preview' mode.
   *
   * @return The flag.
   */
  public boolean isPreviewMode()
  {
    return false;
  }

  /**
   * Returns the value of the function, expression or column using its specific name. This method
   * returns null if the named column was not found.
   *
   * @param name  the item name.
   *
   * @return The item value.
   *
   * @throws IndexOutOfBoundsException if the index is negative or greater than the number of
   *         columns in this row.
   * @throws IllegalStateException if a deadlock is detected.
   */
  public Object get(String name)
  {
    int idx = findColumn(name);
    if (idx == -1)
    {
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
  public int getColumnCount()
  {
    return getPropertiesEndIndex();
  }

  /**
   * Looks up the position of the column with the name <code>name</code>.
   * returns the position of the column or -1 if no columns could be retrieved.
   *
   * @param name  the item name.
   *
   * @return the column position of the column, expression or function with the given name or
   * -1 if the given name does not exist in this DataRow.
   */
  public int findColumn(String name)
  {
    Integer integ = (Integer) colcache.get(name);
    if (integ != null)
    {
      return integ.intValue();
    }

    int size = getColumnCount();
    for (int i = 0; i < size; i++)
    {
      String column = getColumnName(i);
      if (column.equals(name))
      {
        colcache.put(name, new Integer(i));
        return i;
      }
    }
    if (warnInvalidColumns)
    {
      // print an warning for the logs.
      Log.warn(new Log.SimpleMessage("Invalid column name specified on query: ", name));
    }
    return -1;
  }

  /**
   * Returns the name of the column, expression or function.
   *
   * @param col  the column, expression or function index.
   *
   * @return  The column, expression or function name.
   */
  public String getColumnName(int col)
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
      Expression f = getFunctions().getExpression(col);
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
   * @return true, if the processing has not yet started and the currentRow is lesser than 0.
   */
  public boolean isBeforeFirstRow()
  {
    return getCurrentRow() < 0;
  }

  /**
   * Tests whether the current row in the tablemodel is the last row in the tablemodel.
   *
   * @return true, if the current row is the last row in the tablemodel.
   */
  public boolean isLastRow()
  {
    return getCurrentRow() == lastRow;
  }

  /**
   * Clones this DataRowBackend.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    DataRowBackend db = (DataRowBackend) super.clone();
    db.preview = null;
    db.columnlocks = new boolean[getColumnCount()];
    return db;
  }

  /**
   * Create a preview backend. Such datarows will have no access to functions (all functions
   * will return null).
   *
   * @return The 'preview' data row backend.
   */
  public DataRowBackend previewNextRow()
  {
    if (preview == null)
    {
      preview = new DataRowPreview(this);
    }
    preview.setPreviewRow(getCurrentRow() + 1);
    return preview;
  }

  /**
   * Calculates the end index for tableentries. TableEntries are the first entries in the row.
   *
   * @return The end index.
   */
  private int getTableEndIndex()
  {
    if (tableEndIndex == -1)
    {
      if (getTablemodel() == null)
      {
        tableEndIndex = 0;
      }
      else
      {
        tableEndIndex = getTablemodel().getColumnCount();
      }
    }
    return tableEndIndex;
  }

  /**
   * Calculates the endindex for function entries. Function entries are following the TableEntries
   * in the row.
   *
   * @return The function end index.
   */
  private int getFunctionEndIndex()
  {
    if (functionsEndIndex == -1)
    {
      if (getFunctions() == null)
      {
        functionsEndIndex = getTableEndIndex();
      }
      else
      {
        functionsEndIndex = getTableEndIndex() + getFunctions().size();
      }
    }
    return functionsEndIndex;
  }

  /**
   * Returns the end index of the properties.
   *
   * @return the index.
   */
  private int getPropertiesEndIndex()
  {
    if (propertiesEndIndex == -1)
    {
      if (getReportProperties() == null)
      {
        propertiesEndIndex = getFunctionEndIndex();
      }
      else
      {
        propertiesEndIndex = getFunctionEndIndex() + getReportProperties().getColumnCount();
      }
    }
    return propertiesEndIndex;
  }

  /**
   * Ensures that the columnLock does match the number of columns in this row.
   */
  protected void revalidateColumnLock()
  {
    if (getColumnCount() != columnlocks.length)
    {
      columnlocks = new boolean[getColumnCount()];
    }

    tableEndIndex = -1;
    functionsEndIndex = -1;
    propertiesEndIndex = -1;
  }

  /**
   * Returns the report properties.
   *
   * @return the report properties.
   */
  public ReportPropertiesList getReportProperties()
  {
    return reportProperties;
  }

  /**
   * Sets the report properties.
   *
   * @param properties  the report properties.
   */
  public void setReportProperties(ReportPropertiesList properties)
  {
    if (properties == null)
    {
      throw new NullPointerException();
    }
    this.reportProperties = properties;
    revalidateColumnLock();
  }
}