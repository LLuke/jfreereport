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
 * -----------
 * ExpressionCollection.java
 * -----------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * ------------------------------
 * 27.07.2002 : Inital version
 */
package com.jrefinery.report;

import com.jrefinery.report.function.Expression;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Collects all expressions used in the report.
 */
public class ExpressionCollection implements Cloneable
{

  private static class ReadOnlyExpressionCollection extends ExpressionCollection
  {
    public ReadOnlyExpressionCollection (ExpressionCollection copy)
    {
      expressionPositions = copy.expressionPositions;
      expressionList = new ArrayList (copy.expressionList.size ());

      for (int i = 0; i < copy.expressionList.size (); i++)
      {
        Expression f = (Expression) copy.expressionList.get (i);
        try
        {
          expressionList.add (f);
        }
        catch (Exception e)
        {
          Log.warn ("Expression " + f.getName () + " failed while cloning", e);
        }
      }
    }

    /**
     * Adds a new Expression to the collection.
     * The Expression is initialized before it is added to this collection.
     * @param f the new Expression instance.
     * @throws ExpressionInitializeException if the Expression could not be initialized correctly
     */
    public void add (Expression f)
            throws FunctionInitializeException
    {
      throw new IllegalStateException ("This is a readonly collection");
    }

    public void removeExpression (Expression f)
    {
      throw new IllegalStateException ("This is a readonly collection");
    }

    public void connectDataRow (DataRow dr)
    {
      if (dr == null) throw new NullPointerException();
      for (int i = 0; i < expressionList.size (); i++)
      {
        Expression f = (Expression) expressionList.get (i);
        f.setDataRow(dr);
      }
    }

    public void disconnectDataRow (DataRow dr)
    {
      if (dr == null) throw new NullPointerException ("Null-DataRowBackend cannot be disconnected.");
      for (int i = 0; i < expressionList.size (); i++)
      {
        Expression f = (Expression) expressionList.get (i);
        f.setDataRow(null);
      }
    }
  }

  /** Storage for the Expressions in the collection. */
  protected Hashtable expressionPositions;

  /** Ordered storage for the Expressions */
  protected ArrayList expressionList;

  /**
   * Creates a new empty Expression collection.
   */
  public ExpressionCollection ()
  {
    expressionPositions = new Hashtable ();
    expressionList = new ArrayList ();
  }

  /**
   * Constructs a new Expression collection, populated with the supplied Expressions.
   *
   * @throws ClassCastException if the collection does not contain Expressions
   */
  public ExpressionCollection (Collection expressions)
          throws FunctionInitializeException
  {
    this ();
    addAll(expressions);
  }

  public void addAll (Collection expressions)
      throws FunctionInitializeException
  {
    if (expressions != null)
    {
      Iterator iterator = expressions.iterator ();
      while (iterator.hasNext ())
      {
        Expression f = (Expression) iterator.next ();
        add (f);
      }
    }
  }

  private void privateAddAll (Collection expressions)
  {
    if (expressions != null)
    {
      Iterator iterator = expressions.iterator ();
      while (iterator.hasNext ())
      {
        Expression f = (Expression) iterator.next ();
        privateAdd(f);
      }
    }
  }

  /**
   * Returns a copy of the Expression collection.
   * This is no cloning, a fully Expressional collection is made readonly by creating a
   * ReadOnlyExpressionCollection (internal private class).
   *
   * @returns a Expression collection that is immutable
   */
  public ExpressionCollection getCopy ()
  {
    return new ExpressionCollection.ReadOnlyExpressionCollection (this);
  }

  /**
   * Returns the Expression with the specified name (or null).
   * @throws NullPointerException if the name given is null
   */
  public Expression get (String name)
  {
    return (Expression) expressionPositions.get (name);
  }

  /**
   * Adds a new Expression to the collection.
   * The Expression is initialized before it is added to this collection.
   * @param f the new Expression instance.
   * @throws ExpressionInitializeException if the Expression could not be initialized correctly
   */
  public void add (Expression f)
          throws FunctionInitializeException
  {
    if (f == null)
      throw new NullPointerException ("Expression is null");

    if (expressionPositions.containsKey (f.getName ()))
    {
      removeExpression (f);
    }

    f.initialize ();
    privateAdd (f);
  }

  /**
   * Adds a new Expression to the collection.
   * @param f the new Expression instance.
   * @throws NullPointerException if the given Expression is null.
   */
  protected void privateAdd (Expression f)
  {
    expressionPositions.put (f.getName (), new Integer (expressionList.size ()));
    expressionList.add (f);
  }

  /**
   * removes the Expression from the collection.
   *
   * @throws NullPointerException if the given Expression is null.
   */
  public void removeExpression (Expression f)
  {
    Integer val = (Integer) expressionPositions.get (f.getName ());
    if (val == null)
    {
      return;
    }
    expressionPositions.remove (f.getName ());
    expressionList.remove (val.intValue ());
  }

  /**
   * Returns the number of active functions in this collection
   */
  public int size ()
  {
    return expressionList.size ();
  }

  public Expression getExpression (int pos)
  {
    return (Expression) expressionList.get (pos);
  }

  public Object clone () throws CloneNotSupportedException
  {
    ExpressionCollection col = (ExpressionCollection) super.clone ();
    col.expressionPositions = new Hashtable ();
    col.expressionList = new ArrayList ();
    col.privateAddAll(expressionList);
    return col;
  }


  public void connectDataRow (DataRow dr)
  {
    throw new IllegalStateException("Only readonly collections can be connected");
  }

  public void disconnectDataRow (DataRow dr)
  {
    throw new IllegalStateException("Only readonly collections can be connected");
  }

}