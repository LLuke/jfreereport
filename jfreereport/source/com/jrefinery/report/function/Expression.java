/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * Expression.java
 * ---------------
 *
 * $Id: Expression.java,v 1.11 2002/12/02 17:29:09 taqua Exp $
 *
 * ChangeLog
 * ------------
 * 26-Jul-2002 : Initial version
 * 28-Aug-2002 : Documentation
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package com.jrefinery.report.function;

import com.jrefinery.report.DataRow;

import java.util.Properties;

/**
 * An expression is a lightweight function that does not maintain a state. Expressions are used
 * to calculate values within a single row of a report. Expressions can use a dataRow to access
 * other fields, expressions or functions within the current row in the report.
 *
 * @author Thomas Morgner
 */
public interface Expression extends Cloneable
{
  /** Literal text for the 'autoactivate' property. */
  public static final String AUTOACTIVATE_PROPERTY = "autoactivate";

  /**
   * Returns the name of the expression.
   * <P>
   * Every expression, function and column in the datamodel within a report is required to have a
   * unique name.
   *
   * @return the function name.
   */
  public String getName();

  /**
   * Sets the name of the expression.
   * <P>
   * The name must not be null and must be unique within the expression group.
   *
   * @param name  the name.
   */
  public void setName(String name);

  /**
   * Return the current expression value.
   * <P>
   * The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue();

  /**
   * Set the expression properties.
   * <P>
   * expression parameters are recorded as properties.
   *
   * @param p  the properties.
   */
  public void setProperties(Properties p);

  /**
   * Returns a copy of this Expression's properties.
   *
   * @return the properties for the expression.
   */
  public Properties getProperties();

  /**
   * Returns true if this expression contains autoactive content and should be called by the
   * system, regardless whether this expression is referenced in the datarow.
   *
   * @return boolean.
   */
  public boolean isActive();

  /**
   * Checks that the expression has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   *
   * @throws FunctionInitializeException in case the expression is not initialized properly.
   */
  public void initialize() throws FunctionInitializeException;

  /**
   * Returns the DataRow used in this expression. The dataRow is set when the report processing
   * starts and can be used to access the values of functions, expressions and the reports
   * datasource.
   *
   * @return the assigned DataRow for this report processing.
   */
  public DataRow getDataRow();

  /**
   * Defines the DataRow used in this expression. The dataRow is set when the report processing
   * starts and can be used to access the values of functions, expressions and the reports
   * datasource.
   *
   * @param theDataRow the DataRow for this expression.
   */
  public void setDataRow(DataRow theDataRow);

  /**
   * Clones the expression, expression should be reinitialized after the cloning.
   * <P>
   * Expression maintain no state, cloning is done at the beginning of the report processing to
   * disconnect the used expression from any other object space.
   *
   * @return A clone of this expression.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException;

  /**
   * The depency level defines the level of execution for this function. Higher depency functions
   * are executed before lower depency functions. The range for depencies is defined to start
   * from 0 (lowest depency possible) to 2^31 (upper limit of int).
   *
   * @return the level.
   */
  public int getDepencyLevel();

  /**
   * Sets the dependency level for the expression.
   *
   * @param level  the level.
   */
  public void setDepencyLevel(int level);
}