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
 * -------------------------
 * ExpressionDataSource.java
 * -------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * $Id: ExpressionDataSource.java,v 1.9 2003/06/27 14:25:17 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 * 28-Aug-2002 : Documentation
 */
package com.jrefinery.report.filter;

import java.io.Serializable;

import com.jrefinery.report.DataRow;

/**
 * A datasource that queries the datarow and computes a value using an expression.
 * Expressions are simple stateless functions which do not maintain any state and get
 * not informed of reportstate changes. All informations for expressions are queried
 * using the datarow given in the connectDataRow method.
 * <p>
 * @see com.jrefinery.report.function.Expression
 *
 * @author Thomas Morgner
 *
 * @deprecated use DataRowDataSource as unified access class instead
 */
public class ExpressionDataSource implements DataSource, DataRowConnectable, Serializable
{
  /**
   * The name of the expression as defined in the function collection for the report.
   *
   * @see com.jrefinery.report.function.Expression#setName(String)
   * @see com.jrefinery.report.function.Expression#getName
   */
  private String expression;

  /**
   * the DataRow connected with this DataSource.
   */
  private DataRow dataRow;

  /**
   * Default constructor. The expression name is empty ("", not null), the value initially
   * null.
   */
  public ExpressionDataSource()
  {
    setExpression("");
  }

  /**
   * Constructs a new expression data source.
   *
   * @param expression The expression.
   */
  public ExpressionDataSource(final String expression)
  {
    setExpression(expression);
  }

  /**
   * Sets the expression.
   *
   * @param field the name of the expression as defined in the expression collection.
   */
  public void setExpression(final String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    this.expression = field;
  }

  /**
   * Returns the name of the expression bound to this datasource.
   *
   * @return the registered expression name
   */
  public String getExpression()
  {
    return expression;
  }

  /**
   * Returns the value of the expression. The value is evaluated from the given DataRow.
   *
   * @return The value.
   * @throws IllegalStateException if there is no datarow connected.
   */
  public Object getValue()
  {
    if (getDataRow() == null)
    {
      throw new IllegalStateException("No DataRowBackend Connected");
    }
    return getDataRow().get(getExpression());
  }

  /**
   * Clones this data source.
   *
   * @return a clone of this ExpressionDataSource.
   * @throws CloneNotSupportedException if the cloning is not supported.
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Connects the DataRowBackend with the named DataSource or DataFilter.
   * The filter is now able to query the other DataSources to compute the result.
   * <p>
   * If there is already a datarow connected, an IllegalStateException is thrown.
   *
   * @throws NullPointerException if the given row is null
   * @throws IllegalStateException if there is a datarow already connected.
   * @param row the datarow to be connected.
   */
  public void connectDataRow(final DataRow row) throws IllegalStateException
  {
    if (row == null)
    {
      throw new NullPointerException("Null-DataRowBackend cannot be set.");
    }
    if (dataRow != null)
    {
      throw new IllegalStateException("There is a datarow already connected");
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
  public void disconnectDataRow(final DataRow row) throws IllegalStateException
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
   * Gets the assigned dataRow.
   *
   * @return the datarow connected with this datasource.
   */
  protected DataRow getDataRow()
  {
    return dataRow;
  }

}