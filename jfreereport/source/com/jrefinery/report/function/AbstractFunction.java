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
 * ---------------------
 * AbstractFunction.java
 * ---------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractFunction.java,v 1.3 2002/05/15 20:47:23 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Added property support and removed the get/set Field/Group
 *               functions.
 * 10-May-2002 : Support for ReportListenerInterface added. All old eventFunctions are
 *               marked deprecated. The name-attribute must not be null, or the validity check
 *               will fail.
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.event.ReportListenerAdapter;
import com.jrefinery.report.event.ReportEvent;

import javax.swing.table.TableModel;
import java.util.Properties;

/**
 * Base class for implementing new report functions.  Provides empty implementations of all the
 * methods in the ReportFunction interface.
 * <p>
 * All parameters are checked by the parser using the isInitialized () function.
 * If the function returns false, one or more properties are missing and the
 * report parsing will be aborted. Make sure, that you fully implement all validity
 * checks using this function.
 */
public abstract class AbstractFunction extends ReportListenerAdapter implements Function
{
  private Properties properties;

  /** The function name. */
  private String name;

  /**
   * Constructs a new function.
   *
   * @param name The function name.
   */
  protected AbstractFunction ()
  {
    this.properties = new Properties ();
  }

  /**
   * Returns the function name.
   */
  public String getName ()
  {
    return this.name;
  }

  /**
   * Sets the function name.
   */
  public void setName (String name)
  {
    if (name == null)
      throw new NullPointerException ("Name must not be null");

    this.name = name;
  }

  public void initialize () throws FunctionInitializeException
  {
    if (name == null) throw new FunctionInitializeException("FunctionName is null");
    if (!isInitialized()) throw new FunctionInitializeException("isInitialized failed.");
  }

  /**
   * Maps the reportStarted-method to the legacy function startReport ().
   */
  public void reportStarted (ReportEvent event)
  {
    JFreeReport report = event.getReport ();
    startReport (report);
  }

  /**
   * Receives notification that a report is starting.
   *
   * @deprecated Use the ReportListener interface instead.
   */
  public final void startReport (JFreeReport report)
  {
    // do nothing
  }

  /**
   * Maps the reportFinished-method to the legacy function endReport ().
   */
  public void reportFinished (ReportEvent event)
  {
    JFreeReport report = event.getReport ();
    endReport (report);
  }

  /**
   * Receives notification that a report is ending.
   *
   * @deprecated Use the ReportListener interface instead.
   */
  public final void endReport (JFreeReport report)
  {
    // do nothing
  }

  /**
   * Maps the pageStarted-method to the legacy function startPage (int).
   */
  public void pageStarted (ReportEvent event)
  {
    ReportState state = event.getState ();
    startPage (state.getCurrentPage());
  }

  /**
   * Maps the pageFinished-method to the legacy function endPage (int).
   */
  public void pageFinished (ReportEvent event)
  {
    ReportState state = event.getState ();
    endPage (state.getCurrentPage());
  }

  /**
   * Receives notification that a page is starting.
   *
   * @deprecated Use the ReportListener interface instead.
   */
  public final void startPage (int page)
  {
    // do nothing
  }

  /**
   * Receives notification that a page is ending.
   *
   * @deprecated Use the ReportListener interface instead.
   */
  public final void endPage (int page)
  {
    // do nothing
  }

  /**
   * Maps the pageStarted-method to the legacy function startPage (int).
   */
  public void groupStarted (ReportEvent event)
  {
    JFreeReport report = event.getReport ();
    ReportState state = event.getState ();
    startGroup (report.getGroup (state.getCurrentGroupIndex()));
  }

  /**
   * Maps the pageFinished-method to the legacy function endPage (int).
   */
  public void groupFinished (ReportEvent event)
  {
    JFreeReport report = event.getReport ();
    ReportState state = event.getState ();
    endGroup (report.getGroup (state.getCurrentGroupIndex()));
  }

  /**
   * Receives notification that a group is starting.
   *
   * @deprecated Use the ReportListener interface instead.
   */
  public final void startGroup (Group g)
  {
    // do nothing
  }

  /**
   * Receives notification that a group is ending.
   *
   * @deprecated Use the ReportListener interface instead.
   */
  public final void endGroup (Group g)
  {
    // do nothing
  }

  /**
   * Maps the pageStarted-method to the legacy function startPage (int).
   */
  public void itemsAdvanced (ReportEvent event)
  {
    JFreeReport report = event.getReport ();
    ReportState state = event.getState ();
    advanceItems (report.getData(), state.getCurrentDataItem());
  }

  /**
   * Processes a row of data.
   *
   * @deprecated Use the ReportListener interface instead.
   */
  public final void advanceItems (TableModel data, int row)
  {
    // do nothing
  }

  /**
   * Returns a clone of the function.
   *
   * Be aware, this does not create a deep copy. If you have complex
   * strucures contained in objects, you have to overwrite this function.
   */
  public Object clone () throws CloneNotSupportedException
  {
    return super.clone ();
  }

  /**
   * Sets the properties for this function. All parameters are defined
   * by properties. Common parameters are "field" and "group" to define
   * the targets of the function.
   *
   * Every function defines its own set of properties and it is up to
   * the report generator to fill the properties.
   *
   * The properties in <code>p</code> are added to the functions properties,
   * eventually overwriting existing properties with the same name.
   *
   * @todo create a property query interface. Maybe the same as used in
   * JDBC (@see java.sql.PropertyInfo)?
   */
  public void setProperties (Properties p)
  {
    if (p != null)
      this.properties.putAll (p);
  }

  /**
   * Queries a property and returns null if no such property was found.
   */
  public String getProperty (String name)
  {
    return getProperty (name, null);
  }

  /**
   * Queries a property and returns the String contained in defaultVal if
   * no property with that name was defined.
   */
  public String getProperty (String name, String defaultVal)
  {
    return properties.getProperty (name, defaultVal);
  }

  public void setProperty (String name, String value)
  {
    if (value == null)
      properties.remove (name);
    else
      properties.setProperty(name, value);
  }
  /**
   * @deprecated initialize() is used to initialize a function.
   */
  public boolean isInitialized ()
  {
    return true;
  }
}