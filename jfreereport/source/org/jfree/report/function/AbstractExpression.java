/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -----------------------
 * AbstractExpression.java
 * -----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractExpression.java,v 1.8 2005/02/04 19:22:53 taqua Exp $
 *
 * Changes
 * -------
 * 12-Aug-2002 : Initial version
 * 27-Aug-2002 : Documentation
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 * 18-Dec-2002 : More Javadoc updates (DG);
 *
 */

package org.jfree.report.function;

import java.io.Serializable;

import org.jfree.report.DataRow;

/**
 * An abstract base class for implementing new report expressions.
 * <p/>
 * Expressions are stateless functions which have access to the report's {@link DataRow}.
 * All expressions are named and the defined names have to be unique within the report's
 * expressions, functions and fields of the datasource. Expressions are configured using
 * properties.
 * <p/>
 *
 * @author Thomas Morgner
 */
public abstract class AbstractExpression implements Expression, Serializable
{
  /**
   * The expression name.
   */
  private String name;

  /**
   * The dependency level.
   */
  private int dependency;

  /**
   * The data row.
   */
  private transient DataRow dataRow;

  private boolean active;

  /**
   * Creates an unnamed expression. Make sure the name of the expression is set using
   * {@link #setName} before the expression is added to the report's expression
   * collection.
   */
  protected AbstractExpression ()
  {
    name = super.toString();
  }

  /**
   * Returns the name of the expression.
   *
   * @return the name.
   */
  public String getName ()
  {
    return name;
  }

  /**
   * Sets the name of the expression. <P> The name should be unique among: <ul> <li>the
   * functions and expressions for the report; <li>the columns in the report's
   * <code>TableModel</code>; </ul> This allows the expression to be referenced by name
   * from any report element.
   * <p/>
   * You should not change the name of an expression once it has been added to the
   * report's expression collection.
   *
   * @param name the name (<code>null</code> not permitted).
   */
  public void setName (final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("AbstractExpression.setName(...) : name is null.");
    }
    this.name = name;
  }

  /**
   * Returns <code>true</code> if this expression contains "auto-active" content and
   * should be called by the system regardless of whether this expression is referenced in
   * the {@link DataRow}.
   *
   * @return true, if the expression is activated automaticly, false otherwise.
   */
  public final boolean isActive ()
  {
    return active;
  }

  public final void setActive (final boolean active)
  {
    this.active = active;
  }

  /**
   * Returns the dependency level for the expression (controls evaluation order for
   * expressions and functions).
   *
   * @return the level.
   */
  public int getDependencyLevel ()
  {
    return dependency;
  }

  /**
   * Sets the dependency level for the expression.
   * <p/>
   * The dependency level controls the order of evaluation for expressions and functions.
   * Higher level expressions are evaluated before lower level expressions.  Any level in
   * the range 0 to Integer.MAX_VALUE is allowed.  Negative values are reserved for system
   * functions (printing and layouting).
   *
   * @param level the level (must be greater than or equal to 0).
   */
  public void setDependencyLevel (final int level)
  {
    if (level < 0)
    {
      throw new IllegalArgumentException("AbstractExpression.setDependencyLevel(...) : negative "
              + "dependency not allowed for user-defined expressions.");
    }
    this.dependency = level;
  }

  /**
   * Returns the current {@link DataRow}.
   *
   * @return the data row.
   */
  public DataRow getDataRow ()
  {
    return dataRow;
  }

  /**
   * Sets the current {@link DataRow} for the expression.  The data row is set when the
   * report processing starts and can be used to access the values of other expressions,
   * functions, and the report's <code>TableModel</code>.
   * <p/>
   * This method is used by the report processing engine, you shouldn't need to call it
   * yourself.
   *
   * @param row the data row.
   */
  public void setDataRow (final DataRow row)
  {
    this.dataRow = row;
  }

  /**
   * Clones the expression.  The expression should be reinitialized after the cloning. <P>
   * Expressions maintain no state, cloning is done at the beginning of the report
   * processing to disconnect the expression from any other object space.
   *
   * @return a clone of this expression.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final AbstractExpression function = (AbstractExpression) super.clone();
    return function;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function. Only the datarow may be shared.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    try
    {
      return (Expression) clone();
    }
    catch (CloneNotSupportedException cne)
    {
      return null;
    }
  }
}
