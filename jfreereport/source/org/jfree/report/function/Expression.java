/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ---------------
 * Expression.java
 * ---------------
 *
 * $Id: Expression.java,v 1.7 2005/02/04 19:22:54 taqua Exp $
 *
 * ChangeLog
 * ------------
 * 26-Jul-2002 : Initial version
 * 28-Aug-2002 : Documentation
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.function;

import org.jfree.report.DataRow;

/**
 * An expression is a lightweight function that does not maintain a state. Expressions are
 * used to calculate values within a single row of a report. Expressions can use a dataRow
 * to access other fields, expressions or functions within the current row in the report.
 *
 * @author Thomas Morgner
 */
public interface Expression extends Cloneable
{
  /**
   * Returns the name of the expression. <P> Every expression, function and column in the
   * datamodel within a report is required to have a unique name.
   *
   * @return the function name.
   */
  public String getName ();

  /**
   * Sets the name of the expression. <P> The name must not be null and must be unique
   * within the expression group.
   *
   * @param name the name.
   */
  public void setName (String name);

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ();

  /**
   * Returns true if this expression contains autoactive content and should be called by
   * the system, regardless whether this expression is referenced in the datarow.
   *
   * @return true, if the expression is activated automaticly, false otherwise.
   */
  public boolean isActive ();

  /**
   * Returns the DataRow used in this expression. The dataRow is set when the report
   * processing starts and can be used to access the values of functions, expressions and
   * the reports datasource.
   *
   * @return the assigned DataRow for this report processing.
   */
  public DataRow getDataRow ();

  /**
   * Defines the DataRow used in this expression. The dataRow is set when the report
   * processing starts and can be used to access the values of functions, expressions and
   * the reports datasource.
   *
   * @param theDataRow the DataRow for this expression.
   */
  public void setDataRow (DataRow theDataRow);

  /**
   * Clones the expression, expression should be reinitialized after the cloning. <P>
   * Expression maintain no state, cloning is done at the beginning of the report
   * processing to disconnect the used expression from any other object space.
   *
   * @return A clone of this expression.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException;

  /**
   * The dependency level defines the level of execution for this function. Higher
   * dependency functions are executed before lower dependency functions. For ordinary
   * functions and expressions, the range for dependencies is defined to start from 0
   * (lowest dependency possible) to 2^31 (upper limit of int).
   * <p/>
   * Levels below 0 are reserved for system-functionality (printing and layouting).
   * <p/>
   * The level must not change during the report processing, or the result is invalid.
   *
   * @return the level.
   */
  public int getDependencyLevel ();

  /**
   * Sets the dependency level for the expression.
   *
   * @param level the level.
   */
  public void setDependencyLevel (int level);

  /**
   * Return a new instance of this expression. The copy is initialized and uses the same
   * parameters as the original, but does not share any objects.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ();
}