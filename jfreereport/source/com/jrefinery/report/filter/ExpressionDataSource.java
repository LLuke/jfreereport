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
 * ---------------
 * ExpressionDataSource.java
 * ---------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 *
 */
package com.jrefinery.report.filter;

import com.jrefinery.report.DataRow;

/**
 * A datasource that queries the datarow and computes a value using an expression.
 * Expressions are simple stateless functions which do not maintain any state and get
 * not informed of reportstate changes. All informations for expressions are queried
 * using the datarow given in the connectDataRow method.
 */
public class ExpressionDataSource implements DataSource, DataRowConnectable
{
  /**
   * The name of the expression as defined in the function collection for the report
   *
   * @see com.jrefinery.report.function.Expression#setName(String)
   * @see com.jrefinery.report.function.Expression#getName
   */
  private String expression;

  private DataRow dataRow;

  /**
   * Default constructor. The expression name is empty ("", not null), the value initially
   * null.
   */
  public ExpressionDataSource ()
  {
    setExpression ("");
  }

  /**
   * Constructs a new expression data source.
   *
   * @param expression The expression.
   */
  public ExpressionDataSource (String expression)
  {
    setExpression (expression);
  }

  /**
   * Sets the expression.
   *
   * @param field the name of the expression as defined in the expression collection.
   */
  public void setExpression (String field)
  {
    if (field == null) throw new NullPointerException ();
    this.expression = field;
  }

  /**
   * Returns the name of the expression bound to this datasource.
   *
   * @return the registered expression name
   */
  public String getExpression ()
  {
    return expression;
  }

  /**
   * Returns the value of the expression.
   *
   * @return The value.
   */
  public Object getValue ()
  {
    if (getDataRow () == null)
    {
      throw new IllegalStateException ("No DataRowBackend Connected");
    }
    return getDataRow ().get (getExpression ());
  }

  public Object clone () throws CloneNotSupportedException
  {
    return super.clone ();
  }

  /**
   * Connects the DataRowBackend with the named DataSource or DataFilter.
   * The filter is now able to query the other DataSources to compute the result.
   *
   * If there is already a datarow connected, an IllegalStateException is thrown.
   */
  public void connectDataRow (DataRow row) throws IllegalStateException
  {
    if (row == null) throw new NullPointerException ("Null-DataRowBackend cannot be set.");
    if (dataRow != null) throw new IllegalStateException ("There is a datarow already connected");
    dataRow = row;
  }

  /**
   * Releases the connection to the datarow. If no datarow is connected, an
   * IllegalStateException is thrown to indicate the programming error.
   */
  public void disconnectDataRow (DataRow row) throws IllegalStateException
  {
    if (row == null) throw new NullPointerException ("Null-DataRowBackend cannot be disconnected.");
    if (dataRow == null) throw new IllegalStateException ("There is no datarow connected");
    dataRow = null;
  }

  protected DataRow getDataRow ()
  {
    return dataRow;
  }

}