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
 * -------------------------
 * ExpressionCollection.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 * 27-Aug-2002 : Documentation
 */
package com.jrefinery.report.function;

import com.jrefinery.report.util.Log;
import com.jrefinery.report.event.ReportListener;
import com.jrefinery.report.event.ReportEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Collects all expressions used in the report. There exist 2 states of the ExpressionCollection.
 * In the first, modifiable state, expressions can be added to the collection. During the adding
 * the expressions get initialized. An ExpressionCollection in this state is not able to connect
 * to an DataRow.
 * <p>
 * The second state is an immutable state of this collection, no expressions can be added or
 * removed.  This ReadOnlyExpressionCollection can be created by calling getCopy() on the
 * first-state expression collection. The ReadOnlyExpressionCollection is able to connect to a
 * DataRow.
 *
 * @author TM
 */
public class ExpressionCollection implements Cloneable
{
  /** Storage for the Expressions in the collection. */
  protected Hashtable expressionPositions;

  /** Ordered storage for the Expressions */
  protected ArrayList expressionList;

  /**
   * Creates a new expression collection (initially empty).
   */
  public ExpressionCollection()
  {
    expressionPositions = new Hashtable();
    expressionList = new ArrayList();
  }

  /**
   * Creates a new expression collection, populated with the supplied expressions.
   *
   * @param expressions  a collection of expressions.
   *
   * @throws FunctionInitializeException if any of the expressions cannot be initialized.
   * @throws ClassCastException if the collection does not contain Expressions
   */
  public ExpressionCollection(Collection expressions)
      throws FunctionInitializeException
  {
    this();
    addAll(expressions);
  }

  /**
   * Adds all expressions contained in the given collection to this expression collection.
   * The expressions get initialized during the adding process.
   *
   * @param expressions  the expressions to be added.
   *
   * @throws ClassCastException if the collection does not contain expressions
   * @throws FunctionInitializeException if a contained expression could not be initialized.
   */
  public void addAll(Collection expressions)
      throws FunctionInitializeException
  {
    if (expressions != null)
    {
      Iterator iterator = expressions.iterator();
      while (iterator.hasNext())
      {
        Expression f = (Expression) iterator.next();
        add(f);
      }
    }
  }

  /**
   * Private add function for internal use. Add the expressions contained in the collection, but do
   * not initialize them.
   *
   * @param expressions  a collection of expressions.
   */
  private void privateAddAll(Collection expressions)
  {
    if (expressions != null)
    {
      Iterator iterator = expressions.iterator();
      while (iterator.hasNext())
      {
        Expression f = (Expression) iterator.next();
        privateAdd(f);
      }
    }
  }

  /**
   * Returns the Expression with the specified name (or null).
   *
   * @param name  the expression name (null not permitted).
   *
   * @return The expression.
   *
   * @throws NullPointerException if the name given is null
   */
  public Expression get(String name)
  {
    return (Expression) expressionPositions.get(name);
  }

  /**
   * Adds an expression to the collection.  The expression is initialized before it is added to
   * this collection.
   *
   * @param e  the expression.
   *
   * @throws FunctionInitializeException if the Expression could not be initialized correctly
   */
  public void add(Expression e)
      throws FunctionInitializeException
  {
    if (e == null)
    {
      throw new NullPointerException("Expression is null");
    }

    if (expressionPositions.containsKey(e.getName()))
    {
      removeExpression(e);
    }

    e.initialize();
    privateAdd(e);
  }

  /**
   * Adds an expression to the collection.
   *
   * @param e  the expression.
   *
   * @throws NullPointerException if the given Expression is null.
   */
  protected void privateAdd(Expression e)
  {
    expressionPositions.put(e.getName(), new Integer(expressionList.size()));
    expressionList.add(e);
  }

  /**
   * Removes an expression from the collection.
   *
   * @param e  the expression.
   *
   * @throws NullPointerException if the given Expression is null.
   */
  public void removeExpression(Expression e)
  {
    Integer val = (Integer) expressionPositions.get(e.getName());
    if (val == null)
    {
      return;
    }
    expressionPositions.remove(e.getName());
    expressionList.remove(val.intValue());
  }

  /**
   * Returns the number of active expressions in this collection.
   *
   * @return the number of expressions in this collection
   */
  public int size()
  {
    return expressionList.size();
  }

  /**
   * Returns the expression on the given position in the list.
   *
   * @param pos  the position in the list.
   *
   * @return the expression.
   *
   * @throws IndexOutOfBoundsException if the given position is invalid
   */
  public Expression getExpression(int pos)
  {
    return (Expression) expressionList.get(pos);
  }

  /**
   * Clones this expression collection and all expressions contained in the collection.
   *
   * @return The clone.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    ExpressionCollection col = (ExpressionCollection) super.clone();
    col.expressionPositions = new Hashtable();
    col.expressionList = new ArrayList();

    Iterator it = expressionList.iterator();
    while (it.hasNext())
    {
      Expression ex = (Expression) it.next();
      col.privateAdd((Expression) ex.clone());
    }
    return col;
  }
}