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
 * -----------------------
 * FunctionCollection.java
 * -----------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionCollection.java,v 1.7 2002/05/31 19:31:14 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 10-May-2002 : Adjusted to support new EventInterface.
 */

package com.jrefinery.report;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.event.ReportListenerAdapter;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * A function collection contains all function elements of a particular report.
 * The collection and its funtions are cloned with every page to enable some
 * sort of caching.
 * <p>
 * Every event in JFreeReport is passed via callback to every function in the
 * collection.
 * <p>
 * A function collection gets immutable after it is passed to the report state.
 * An immutable function collection cannot have functions added or removed.
 */
public class FunctionCollection extends ReportListenerAdapter implements Cloneable
{
  private static class ReadOnlyFunctionCollection extends FunctionCollection
  {
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
     * Adds a new function to the collection.
     * The function is initialized before it is added to this collection.
     * @param f the new function instance.
     * @throws FunctionInitializeException if the function could not be initialized correctly
     */
    public void add (Function f)
            throws FunctionInitializeException
    {
      throw new IllegalStateException ("This is a readonly collection");
    }

    public void removeFunction (Function f)
    {
      throw new IllegalStateException ("This is a readonly collection");
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
   * @throws ClassCastException if the collection does not contain Functions
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
   * This is no cloning, a fully functional collection is made readonly by creating a
   * ReadOnlyFunctionCollection (internal private class).
   *
   * @returns a function collection that is immutable
   */
  public FunctionCollection getCopy ()
  {
    return new ReadOnlyFunctionCollection (this);
  }

  /**
   * Returns the function with the specified name (or null).
   * @throws NullPointerException if the name given is null
   */
  public Function get (String name)
  {
    Integer result = (Integer) functionPositions.get (name);
    if (result == null) return null;

    return (Function) functionList.get (result.intValue ());
  }

  /**
   * Adds a new function to the collection.
   * The function is initialized before it is added to this collection.
   * @param f the new function instance.
   * @throws FunctionInitializeException if the function could not be initialized correctly
   */
  public void add (Function f)
          throws FunctionInitializeException
  {
    if (f == null)
      throw new NullPointerException ("Function is null");

    if (functionPositions.containsKey (f.getName ()))
    {
      removeFunction (f);
    }

    f.initialize ();
    privateAdd (f);
  }

  /**
   * Adds a new function to the collection.
   * @param f the new function instance.
   * @throws NullPointerException if the given function is null.
   */
  protected void privateAdd (Function f)
  {
    functionPositions.put (f.getName (), new Integer (functionList.size ()));
    functionList.add (f);
  }

  /**
   * removes the function from the collection.
   *
   * @throws NullPointerException if the given function is null.
   */
  public void removeFunction (Function f)
  {
    Integer val = (Integer) functionPositions.get (f.getName ());
    if (val == null)
    {
      return;
    }
    functionPositions.remove (f.getName ());
    functionList.remove (val.intValue ());
  }

  /**
   * Notifies every function in the collection that a report is starting.  This gives each
   * function an opportunity to initialise itself for a new report.
   *
   * @param report The report.
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
   * @param report The report.
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
   * Send "EndPage" to every function in this collection.
   * <p>
   * Function Events are sended before the function is asked to
   * print itself.
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
   * @param group The group that is starting.
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
   * @param group The group that is ending.
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
   * Returns the number of active functions in this collection
   */
  public int size ()
  {
    return functionList.size ();
  }

}
