/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * -----------------------
 * FunctionCollection.java
 * -----------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionCollection.java,v 1.12 2002/09/05 09:34:53 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 10-May-2002 : Adjusted to support new EventInterface.
 */

package com.jrefinery.report;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.event.ReportListener;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * A function collection contains all function elements of a particular report.
 * The collection and its functions are cloned with every page to enable some
 * sort of caching.
 * <p>
 * Every event in JFreeReport is passed via callback to every function in the collection.
 * <p>
 * A function collection gets immutable after it is passed to the report state.
 * An immutable function collection cannot have functions added or removed.
 *
 * @author TM
 */
public class FunctionCollection implements Cloneable, Serializable, ReportListener
{
  /**
   * A ReadOnlyFunctionCollection cannot be modified in any way. Trying to add or remove
   * an function will result in an IllegalStateException. This ReadOnlyFunctionCollection
   * is able to connect a datarow.
   */
  private static class ReadOnlyFunctionCollection extends FunctionCollection
  {
    /**
     * Creates a new ReadOnlyFunctionCollection based on the given FunctionCollection.
     * The new ReadOnbly-Collection cannot be modified.
     *
     * @param copy  the base collection.
     */
    public ReadOnlyFunctionCollection (FunctionCollection copy)
    {
      functionPositions = copy.functionPositions;
      functionList = new ArrayList (copy.functionList.size ());

      for (int i = 0; i < copy.functionList.size (); i++)
      {
        Function f = (Function) copy.functionList.get (i);
        try
        {
          functionList.add (f.clone ());
        }
        catch (Exception e)
        {
          Log.warn ("Function " + f.getName () + " failed while cloning", e);
        }
      }
    }

    /**
     * Normally, this method adds a function to the collection. However, in this subclass it
     * throws an IllegalStateException because this collection is read-only.
     *
     * @param f  the function.
     *
     * @throws IllegalStateException as this is a ReadOnly class
     * @throws FunctionInitializeException if the function could not be initialized correctly
     */
    public void add (Function f)
            throws FunctionInitializeException
    {
      throw new IllegalStateException ("This is a read-only collection");
    }

    /**
     * Normally, this method removes a function from the collection. However, in this subclass it
     * throws an IllegalStateException because this collection is read-only.
     *
     * @param f  the function.
     *
     * @throws IllegalStateException because this collection is read-only.
     */
    public void removeFunction (Function f)
    {
      throw new IllegalStateException ("This is a read-only collection");
    }


    /**
     * Connects the given datarow to the function collection and all functions contained in this
     * collection.
     *
     * @param dr the datarow to be connected.
     *
     * @throws IllegalStateException if there is a datarow already connected.
     * @throws NullPointerException if the given datarow is null.
     */
    public void connectDataRow (DataRow dr)
    {
      if (dr == null)
      {
        throw new NullPointerException();
      }
      for (int i = 0; i < functionList.size (); i++)
      {
        Function f = (Function) functionList.get (i);
        f.setDataRow(dr);
      }
    }

    /**
     * Disconnects the given datarow from the function collection and all functions contained
     * in this collection.
     *
     * @param dr the datarow to be connected.
     *
     * @throws IllegalStateException if there is a datarow already connected.
     * @throws NullPointerException if the given datarow is null.
     */
    public void disconnectDataRow (DataRow dr)
    {
      if (dr == null)
      {
        throw new NullPointerException ("FunctionCollection: "
                                      + "Null DataRowBackend cannot be disconnected.");
      }
      for (int i = 0; i < functionList.size(); i++)
      {
        Function f = (Function) functionList.get (i);
        f.setDataRow(null);
      }
    }
  }

  /** Storage for the functions in the collection. */
  protected Hashtable functionPositions;

  /** Ordered storage for the functions */
  protected ArrayList functionList;

  /**
   * Creates a new empty function collection.
   */
  public FunctionCollection ()
  {
    functionPositions = new Hashtable ();
    functionList = new ArrayList ();
  }

  /**
   * Constructs a new function collection, populated with the supplied functions.
   *
   * @param functions  the functions.
   *
   * @throws ClassCastException if the collection does not contain Function objects.
   * @throws FunctionInitializeException if any of the functions cannot be initialized.
   */
  public FunctionCollection (Collection functions)
          throws FunctionInitializeException
  {
    this ();
    if (functions != null)
    {
      Iterator iterator = functions.iterator ();
      while (iterator.hasNext ())
      {
        Function f = (Function) iterator.next ();
        add (f);
      }
    }

  }

  /**
   * Returns a copy of the function collection.
   * This is no cloning, a fully functional collection is made read-only by creating a
   * ReadOnlyFunctionCollection (internal private class).
   *
   * @return a function collection that is immutable.
   */
  public FunctionCollection getCopy ()
  {
    return new ReadOnlyFunctionCollection (this);
  }

  /**
   * Returns the function with the specified name (or null).
   *
   * @param name  the function name (null not permitted).
   *
   * @return The function.
   *
   * @throws NullPointerException if the name given is null.
   */
  public Function get (String name)
  {
    return (Function) functionPositions.get (name);
  }

  /**
   * Adds a new function to the collection.
   * The function is initialized before it is added to this collection.
   *
   * @param f the function.
   *
   * @throws FunctionInitializeException if the function could not be initialized correctly.
   */
  public void add (Function f)
          throws FunctionInitializeException
  {
    if (f == null)
    {
      throw new NullPointerException ("Function is null");
    }

    if (functionPositions.containsKey (f.getName ()))
    {
      removeFunction (f);
    }

    f.initialize ();
    privateAdd (f);
  }

  /**
   * Adds a new function to the collection.
   *
   * @param f the function.
   *
   * @throws NullPointerException if the given function is null.
   */
  protected void privateAdd (Function f)
  {
    functionPositions.put (f.getName (), f);
    functionList.add (f);
  }

  /**
   * Removes a function from the collection.
   *
   * @param f  the function to remove.
   *
   * @throws NullPointerException if the given function is null.
   */
  public void removeFunction (Function f)
  {
    functionPositions.remove (f.getName ());
    functionList.remove (f);
  }

  /**
   * Notifies every function in the collection that a report is starting.  This gives each
   * function an opportunity to initialise itself for a new report.
   *
   * @param event The report event.
   */
  public void reportStarted (ReportEvent event)
  {
    for (int i = 0; i < functionList.size (); i++)
    {
      Function f = (Function) functionList.get (i);
      try
      {
        f.reportStarted (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName () + " failed while processing 'reportStarted'", e);
      }
    }
  }

  /**
   * Notifies every function in the collection that a report is ending.
   *
   * @param event The report event.
   */
  public void reportFinished (ReportEvent event)
  {
    for (int i = 0; i < functionList.size (); i++)
    {
      Function f = (Function) functionList.get (i);
      try
      {
        f.reportFinished (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName () + " failed while processing 'reportFinished'", e);
      }
    }

  }

  /**
   * Notifies every function in the collection that a page is starting.
   *
   * @param event The report event.
   */
  public void pageStarted (ReportEvent event)
  {
    for (int i = 0; i < functionList.size (); i++)
    {
      Function f = (Function) functionList.get (i);
      try
      {
        f.pageStarted (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName () + " failed while processing 'pageStarted'", e);
      }

    }

  }

  /**
   * Notifies every function in the collection that a page has finished.
   *
   * @param event The report event.
   */
  public void pageFinished (ReportEvent event)
  {
    for (int i = 0; i < functionList.size (); i++)
    {
      Function f = (Function) functionList.get (i);
      try
      {
        f.pageFinished (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName () + " failed while processing 'pageFinished'", e);
      }
    }

  }

  /**
   * Notifies every function in the collection that a new group is starting.  This gives each
   * function an opportunity to reset itself, if it belongs to the group.
   *
   * @param event The report event.
   */
  public void groupStarted (ReportEvent event)
  {
    for (int i = 0; i < functionList.size (); i++)
    {
      Function f = (Function) functionList.get (i);
      try
      {
        f.groupStarted (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName () + " failed while processing 'groupStarted'", e);
      }

    }

  }

  /**
   * Notifies every function in the collection that the current group is ending.
   *
   * @param event The report event.
   */
  public void groupFinished (ReportEvent event)
  {
    for (int i = 0; i < functionList.size (); i++)
    {
      Function f = (Function) functionList.get (i);
      try
      {
        f.groupFinished (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName () + " failed while processing 'groupFinished'", e);
      }
    }

  }

  /**
   * Notifies every function in the collection that a new row of data is being processed.  This
   * gives each function an opportunity to update its value.
   *
   * @param event The report event.
   */
  public void itemsAdvanced (ReportEvent event)
  {
    for (int i = 0; i < functionList.size (); i++)
    {
      Function f = (Function) functionList.get (i);
      try
      {
        f.itemsAdvanced (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName () + " failed while processing 'itemsAdvanced'", e);
      }
    }
  }

  /**
   * Notifies every function in the collection that a new row of data is being processed.  This
   * gives each function an opportunity to update its value.
   *
   * @param event The report event.
   */
  public void itemsStarted (ReportEvent event)
  {
    for (int i = 0; i < functionList.size (); i++)
    {
      Function f = (Function) functionList.get (i);
      try
      {
        f.itemsStarted (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName () + " failed while processing 'itemsStarted'", e);
      }

    }
  }

  /**
   * Notifies every function in the collection that a new row of data is being processed.  This
   * gives each function an opportunity to update its value.
   *
   * @param event The report event.
   */
  public void itemsFinished (ReportEvent event)
  {
    for (int i = 0; i < functionList.size (); i++)
    {
      Function f = (Function) functionList.get (i);
      try
      {
        f.itemsFinished (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName () + " failed while processing 'itemsFinished'", e);
      }
    }
  }

  /**
   * Returns the number of functions in this collection.
   *
   * @return The function count.
   */
  public int size ()
  {
    return functionList.size ();
  }

  /**
   * Returns the function at the given position in the list.
   *
   * @param pos  the function index (zero-based).
   *
   * @return the function.
   *
   * @throws IndexOutOfBoundsException if the given position is invalid
   *
   */
  public Function getFunction (int pos)
  {
    return (Function) functionList.get (pos);
  }

  /**
   * Clones this function collection and all functions contained in the collection.
   *
   * @return A clone.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    FunctionCollection col = (FunctionCollection) super.clone ();
    col.functionList = (ArrayList) functionList.clone ();
    col.functionPositions = (Hashtable) functionPositions.clone ();
    return col;
  }

  /**
   * Connects the given datarow to the function collection and all functions contained in this
   * collection.
   *
   * @param dr  the data row.
   *
   * @throws IllegalStateException only ReadOnlyFunctionCollections can be connected to a datarow.
   */
  public void connectDataRow(DataRow dr)
  {
    throw new IllegalStateException("Only readonly collections can be connected");
  }

  /**
   * Disconnects the given datarow to the function collection and all expressions contained in this
   * collection.
   *
   * @param dr  the data row.
   *
   * @throws IllegalStateException only ReadOnlyFunctionCollections can be connected to a datarow.
   */
  public void disconnectDataRow(DataRow dr)
  {
    throw new IllegalStateException("Only readonly collections can be connected");
  }

}
