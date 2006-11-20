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
 * ExpressionDataRow.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ExpressionDataRow.java,v 1.4 2006/11/11 20:37:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.data;

import java.util.HashMap;

import org.jfree.formula.DefaultFormulaContext;
import org.jfree.report.DataFlags;
import org.jfree.report.DataRow;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportData;
import org.jfree.report.expressions.Expression;
import org.jfree.report.expressions.ExpressionRuntime;
import org.jfree.report.expressions.Function;
import org.jfree.report.expressions.GlobalReportContext;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.report.structure.Element;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * A datarow for all expressions encountered in the report. This datarow is a
 * stack-like structure, which allows easy adding and removing of expressions,
 * even if these expressions have been cloned and or otherwisely modified.
 *
 * @author Thomas Morgner
 */
public final class ExpressionDataRow implements DataRow
{
  private static class ExpressionSlot
      implements ExpressionRuntime, Cloneable
  {
    private StaticExpressionRuntimeData staticRuntimeData;
    private Expression expression;
    private Object value;
    private String name;
    private boolean queried;
    private DataRow dataRow;

    public ExpressionSlot(final Expression expression,
                          final StaticExpressionRuntimeData runtimeData)
    {
      this.staticRuntimeData = runtimeData;
      this.expression = expression;
      this.name = expression.getName();
      this.expression.setRuntime(this);
      this.expression.setRuntime(null);
    }

    public Expression getExpression()
    {
      return expression;
    }

    public Object getValue() throws DataSourceException
    {
      if (queried == false)
      {
        synchronized (expression)
        {
          expression.setRuntime(this);
          value = expression.computeValue();
          expression.setRuntime(null);
        }
        queried = true;
      }
      return value;
    }

    public String getName()
    {
      return name;
    }

    public DataRow getDataRow()
    {
      return dataRow;
    }

    public Object clone() throws CloneNotSupportedException
    {
      return super.clone();
    }

    public void setDataRow(final DataRow dataRow)
    {
      this.dataRow = dataRow;
    }

    /**
     * Returns the report data used in this section. If subreports are used,
     * this does not reflect the complete report data.
     * <p/>
     * All access to the report data must be properly synchronized. Failure to
     * do so may result in funny results. Do not assume that the report data
     * will be initialized on the current cursor positon.
     *
     * @return
     */
    public ReportData getData()
    {
      return staticRuntimeData.getData();
    }

    public String getExportDescriptor()
    {
      return staticRuntimeData.getExportDescriptor();
    }

    public Element getDeclaringParent()
    {
      return staticRuntimeData.getDeclaringParent();
    }

    public Configuration getConfiguration()
    {
      return staticRuntimeData.getConfiguration();
    }

    public ResourceBundleFactory getResourceBundleFactory()
    {
      return staticRuntimeData.getResourceBundleFactory();
    }

    public void advance() throws DataSourceException
    {
      if (expression instanceof Function)
      {
        Function f = (Function) expression;
        expression.setRuntime(this);
        expression = f.advance();
        f.setRuntime(null);
        expression.setRuntime(null);
      }
      value = null;
      queried = false;
    }

    public boolean isDeepTraversing()
    {
      return expression.isDeepTraversing();
    }

    public int getCurrentRow()
    {
      return staticRuntimeData.getCurrentRow();
    }

    public GlobalReportContext getGlobalContext()
    {
      return staticRuntimeData.getGlobalReportContext();
    }
  }

  private ExpressionSlot[] expressions;
  private int length;
  private HashMap nameCache;
  private GlobalMasterRow masterRow;
  private GlobalReportContext globalReportContext;

  public ExpressionDataRow(final GlobalMasterRow masterRow, int capacity)
  {
    this.masterRow = masterRow;
    this.nameCache = new HashMap(capacity);
    this.expressions = new ExpressionSlot[capacity];
    final ReportContextImpl reportContext = new ReportContextImpl();
    // todo: Replace the default context by something own to allow user-defined
    // formula-functions.
    reportContext.setSystemAttribute(GlobalReportContext.FORMULA_CONTEXT, new DefaultFormulaContext());
    reportContext.setSystemAttribute(GlobalReportContext.PRECOMPUTE_REGISTRY, new PrecomputedValueRegistry());
    this.globalReportContext = reportContext;
  }

  private ExpressionDataRow(final GlobalMasterRow masterRow,
                            final ExpressionDataRow previousRow)
      throws CloneNotSupportedException
  {
    this.globalReportContext = previousRow.globalReportContext;
    this.masterRow = masterRow;
    this.nameCache = (HashMap) previousRow.nameCache.clone();
    this.expressions = new ExpressionSlot[previousRow.expressions.length];
    this.length = previousRow.length;
    for (int i = 0; i < length; i++)
    {
      ExpressionSlot expression = previousRow.expressions[i];
      if (expression == null)
      {
        Log.debug("Error: Expression is null...");
      }
      else
      {
        expressions[i] = (ExpressionSlot) expression.clone();
      }
    }
  }

  private void ensureCapacity(int requestedSize)
  {
    final int capacity = this.expressions.length;
    if (capacity > requestedSize)
    {
      return;
    }
    final int newSize = Math.max(capacity * 2, requestedSize + 10);

    final ExpressionSlot[] newExpressions = new ExpressionSlot[newSize];
    System.arraycopy(expressions, 0, newExpressions, 0, length);

    this.expressions = newExpressions;
  }

  /**
   * This adds the expression to the data-row and queries the expression for the
   * first time.
   *
   * @param ex
   * @param rd
   * @throws DataSourceException
   */
  public synchronized void pushExpression(final Expression ex,
                                          final StaticExpressionRuntimeData rd)
      throws DataSourceException
  {
    if (ex == null)
    {
      throw new NullPointerException();
    }

    ensureCapacity(length + 1);

    final ExpressionSlot expressionSlot =
        new ExpressionSlot(ex.getInstance(), rd);
    this.expressions[length] = expressionSlot;
    final String name = ex.getName();
    if (name != null)
    {
      nameCache.put(name, expressionSlot);
    }
    length += 1;

    expressionSlot.setDataRow(masterRow.getGlobalView());
    // A manual advance to initialize the function.
    expressionSlot.advance();
    if (name != null)
    {
      final Object value = expressionSlot.getValue();
      final MasterDataRowChangeEvent chEvent = new MasterDataRowChangeEvent
          (MasterDataRowChangeEvent.COLUMN_ADDED, name, value);
      masterRow.dataRowChanged(chEvent);
    }
  }

  public synchronized void pushExpressions(final Expression[] ex,
                                           final StaticExpressionRuntimeData rd)
      throws DataSourceException
  {
    if (ex == null)
    {
      throw new NullPointerException();
    }

    ensureCapacity(length + ex.length);
    for (int i = 0; i < ex.length; i++)
    {
      Expression expression = ex[i];
      if (expression == null)
      {
        continue;
      }
      pushExpression(expression, rd);
    }
  }

  public synchronized void popExpressions(final int counter) throws
      DataSourceException
  {
    for (int i = 0; i < counter; i++)
    {
      popExpression();
    }
  }

  public synchronized void popExpression() throws DataSourceException
  {
    if (length == 0)
    {
      return;
    }
    String originalName = expressions[length - 1].getName();
    this.expressions[length - 1] = null;
    this.length -= 1;
    if (originalName != null)
    {
      int otherIndex = -1;
      for (int i = length - 1; i >= 0; i -= 1)
      {
        ExpressionSlot expression = expressions[i];
        if (originalName.equals(expression.getName()))
        {
          otherIndex = i;
          break;
        }
      }
      if (otherIndex == -1)
      {
        nameCache.remove(originalName);
      }
      else
      {
        nameCache.put(originalName, expressions[otherIndex]);
      }
    }

    if (originalName != null)
    {
      final MasterDataRowChangeEvent chEvent = new MasterDataRowChangeEvent
          (MasterDataRowChangeEvent.COLUMN_REMOVED, originalName, null);
      masterRow.dataRowChanged(chEvent);
    }

  }

  /**
   * Returns the value of the expressions or column in the tablemodel using the
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
    return expressions[col].getValue();
  }

  /**
   * Returns the value of the function, expressions or column using its specific
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
    final ExpressionSlot es = (ExpressionSlot) nameCache.get(col);
    if (es == null)
    {
      return null;
    }

    return es.getValue();
  }

  /**
   * Returns the name of the column, expressions or function. For columns from
   * the tablemodel, the tablemodels <code>getColumnName</code> method is
   * called. For functions, expressions and report properties the assigned name
   * is returned.
   *
   * @param col the item index.
   * @return the name.
   */
  public String getColumnName(int col)
  {
    return expressions[col].getName();
  }

  /**
   * Returns the number of columns, expressions and functions and marked
   * ReportProperties in the report.
   *
   * @return the item count.
   */
  public int getColumnCount()
  {
    return length;
  }

  public DataFlags getFlags(String col)
  {
    throw new UnsupportedOperationException();
  }

  public DataFlags getFlags(int col)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Advances to the next row and attaches the given master row to the objects
   * contained in that client data row.
   *
   * @param master
   * @param deepTraversing only advance expressions that have been marked as
   *                       deeply traversing
   * @return
   */
  public ExpressionDataRow advance(final GlobalMasterRow master,
                                   final boolean deepTraversing)
      throws DataSourceException
  {
    try
    {
      ExpressionDataRow edr = new ExpressionDataRow(master, this);

      // It is defined, that new expressions get evaluated before any older
      // expression.
      for (int i = edr.length - 1; i >= 0; i--)
      {
        ExpressionSlot expressionSlot = edr.expressions[i];
        expressionSlot.setDataRow(master.getGlobalView());
        if (deepTraversing && expressionSlot.isDeepTraversing() ||
            deepTraversing == false)
        {
          expressionSlot.advance();
        }
        // Query the value (once per advance) ..
        final Object value = expressionSlot.getValue();
        if (expressionSlot.getName() != null)
        {
          final MasterDataRowChangeEvent chEvent = new MasterDataRowChangeEvent
              (MasterDataRowChangeEvent.COLUMN_UPDATED,
                  expressionSlot.getName(), value);
          master.dataRowChanged(chEvent);
        }
      }
      return edr;
    }
    catch (CloneNotSupportedException e)
    {
      throw new DataSourceException("Cloning failed", e);
    }
  }

  public ExpressionDataRow derive(final GlobalMasterRow master)
      throws DataSourceException
  {
    try
    {
      return new ExpressionDataRow(master, this);
    }
    catch (CloneNotSupportedException e)
    {
      throw new DataSourceException("Cloning failed", e);
    }
  }

  public GlobalReportContext getGlobalReportContext()
  {
    return globalReportContext;
  }
}
