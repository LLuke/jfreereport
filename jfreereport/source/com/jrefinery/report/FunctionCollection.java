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
 * $Id: FunctionCollection.java,v 1.5 2002/05/21 23:06:18 taqua Exp $
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

import javax.swing.table.TableModel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * A function collection contains all function elements of a particular report.
 * The collection and its funtions are cloned with every page to enable some
 * sort of caching.
 * <p>
 * Every event in JFreeReport is passed via callback to every function in the
 * collection.
 */
public class FunctionCollection extends ReportListenerAdapter implements Cloneable
{

  /** Storage for the functions in the collection. */
  private Map functions;

  /**
   * Creates a new empty function collection.
   */
  public FunctionCollection ()
  {
    this.functions = new TreeMap ();
  }

  /**
   * Constructs a new function collection, populated with the supplied functions.
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
        add  (f);
      }
    }

  }

  /**
   * Returns the function with the specified name (or null).
   */
  public Function get (String name)
  {

    Function result = (Function) functions.get (name);

    return result;

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

    f.initialize();
    privateAdd (f);
  }

  /**
   * Adds a new function to the collection.
   * @param f the new function instance.
   */
  protected void privateAdd (Function f)
  {
    functions.put (f.getName (), f);
  }

  /**
   * Notifies every function in the collection that a report is starting.  This gives each
   * function an opportunity to initialise itself for a new report.
   *
   * @param report The report.
   */
  public void reportStarted (ReportEvent event)
  {
    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        f.reportStarted (event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName() + " failed while processing 'reportStarted'", e);
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
    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        f.reportFinished(event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName() + " failed while processing 'reportFinished'", e);
      }
    }

  }

  /**
   * Notifies every function in the collection that a page is starting.
   */
  public void pageStarted (ReportEvent event)
  {
    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        f.pageStarted(event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName() + " failed while processing 'pageStarted'", e);
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
    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        f.pageFinished(event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName() + " failed while processing 'pageFinished'", e);
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
    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        f.groupStarted(event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName() + " failed while processing 'groupStarted'", e);
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
    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        f.groupFinished(event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName() + " failed while processing 'groupFinished'", e);
      }
    }

  }

  /**
   * Notifies every function in the collection that a new row of data is being processed.  This
   * gives each function an opportunity to update its value.
   */
  public void itemsAdvanced (ReportEvent event)
  {
    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        f.itemsAdvanced(event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName() + " failed while processing 'itemsAdvanced'", e);
      }
    }
  }

  /**
   * Notifies every function in the collection that a new row of data is being processed.  This
   * gives each function an opportunity to update its value.
   */
  public void itemsStarted (ReportEvent event)
  {
    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        f.itemsStarted(event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName() + " failed while processing 'itemsStarted'", e);
      }

    }
  }

  /**
   * Notifies every function in the collection that a new row of data is being processed.  This
   * gives each function an opportunity to update its value.
   */
  public void itemsFinished (ReportEvent event)
  {
    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        f.itemsFinished(event);
      }
      catch (Exception e)
      {
        Log.warn ("Function " + f.getName() + " failed while processing 'itemsFinished'", e);
      }
    }
  }

  /**
   * Returns a string representation of the function collection.  Used in debugging only.
   *
  public String toString ()
  {

    StringBuffer result = new StringBuffer ();
    result.append ("Function Collection:\n");

    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      result.append (f.getName ());
      result.append (" = ");
      result.append (f.getValue ());
      result.append ("\n");
    }

    return result.toString();

  }
*/
  /**
   * Returns a copy of the function collection.
   */
  public Object clone ()
  {

    FunctionCollection result = new FunctionCollection ();

    Iterator iterator = this.functions.values ().iterator ();
    while (iterator.hasNext ())
    {
      Function f = (Function) iterator.next ();
      try
      {
        Function copy = (Function) f.clone ();
        result.privateAdd(copy);
      }
      catch (CloneNotSupportedException e)
      {
        Log.error ("FunctionCollection: problem cloning function.", e);
      }
    }

    return result;

  }

  ////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the value of the current clone of the keying function.
   *
   * @param key the function element which was created by the parser.
   */
  public Object getValue (Function key)
  {

    Function fn = (Function) functions.get (key);

    if (fn == null)
    {
      Log.warn ("No Function " + fn + " found, using key " + key + " as function");
      fn = key;
    }
    return fn.getValue ();

  }

  /**
   * Returns the number of active functions in this collection
   */
  public int size ()
  {
    return functions.size ();
  }

}
