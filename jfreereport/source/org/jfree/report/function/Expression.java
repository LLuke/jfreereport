/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
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
 * $Id: Expression.java,v 1.10 2006/01/24 18:58:29 taqua Exp $
 *
 * ChangeLog
 * ------------
 * 26-Jul-2002 : Initial version
 * 28-Aug-2002 : Documentation
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.function;

import org.jfree.report.DataSourceException;

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
  public Object getValue () throws DataSourceException;

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
   * Return a new instance of this expression. The copy is initialized and uses the same
   * parameters as the original, but does not share any objects.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ();

  /**
   * Defines the DataRow used in this expression. The dataRow is set when the report
   * processing starts and can be used to access the values of functions, expressions and
   * the reports datasource.
   *
   * @param runtime the runtime information for the expression
   */
  public void setRuntime (ExpressionRuntime runtime);

  /**
   * Queries the dependency information provided by this expression. While this
   * method is called, an ExpressionRuntime will be made available; although it
   * is not guaranteed that this runtime has a valid DataRow. 
   *
   * @param info
   */
  public void queryDependencyInfo (final ExpressionDependencyInfo info);

}
