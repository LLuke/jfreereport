/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -------------------
 * Expression.java
 * ------------------------------
 *
 * ChangeLog
 * ------------
 * 26-Jul-2002 : Initial version
 * 28-Aug-2002 : Documentation
 */
package com.jrefinery.report.function;

import com.jrefinery.report.DataRow;

import java.util.Properties;

/**
 * An expression is a lightweight function that does not maintain a state. Expressions are used
 * to calculate values within a single row of an report. Expressions can use a dataRow to access
 * other fields, expressions or functions within the current row in the report.
 */
public interface Expression extends Cloneable
{

  /**
   * Returns the name of the expression.
   * <P>
   * Every expression, function and column in the datamodel within a report is required to have a
   * unique name.
   *
   * @return The function name.
   */
  public String getName();

  /**
   * Sets the name of the expression.
   * <P>
   * The name must not be null and must be unique within the expression group.
   *
   * @param name The name.
   */
  public void setName(String name);

  /**
   * Return the current expression value.
   * <P>
   * The value depends (obviously) on the expression implementation.
   *
   * @return The value of the function.
   */
  public Object getValue();

  /**
   * Set the expression properties.
   * <P>
   * expression parameters are recorded as properties.
   *
   * @param p The properties.
   */
  public void setProperties(Properties p);

  /**
   * Checks that the expression has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   *
   * @throws FunctionInitializeException in case the expression is not initialized properly.
   */
  public void initialize() throws FunctionInitializeException;

  public DataRow getDataRow();

  public void setDataRow(DataRow theDataRow);

  /**
   * Clones the expression in its current state.
   * <P>
   * Expression maintain no state, cloning is done at the beginning of the report processing to
   * disconnect the used expression from any other object space.
   *
   * @return A clone of this expression.
   */
  public Object clone() throws CloneNotSupportedException;


}