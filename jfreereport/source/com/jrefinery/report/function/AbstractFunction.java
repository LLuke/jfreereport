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
 * ---------------------
 * AbstractFunction.java
 * ---------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractFunction.java,v 1.8 2002/06/23 16:42:24 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Added property support and removed the get/set Field/Group
 *               functions.
 * 10-May-2002 : Support for ReportListenerInterface added. All old eventFunctions are
 *               marked deprecated. The name-attribute must not be null, or the validity check
 *               will fail.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.event.ReportListenerAdapter;

import javax.swing.table.TableModel;
import java.util.Properties;
import java.util.Enumeration;

/**
 * Base class for implementing new report functions.  Provides empty implementations of all the
 * methods in the Function interface.
 * <p>
 * All parameters are checked by the parser using the isInitialized () function.
 * If the function returns false (??? return type is void, but it throws an exception), one or
 * more properties are missing and the report parsing will be aborted. Make sure, that you fully
 * implement all validity checks using this function.
 */
public abstract class AbstractFunction implements Function
{

  /** Storage for the function properties. */
  private Properties properties;

  /** The function name. */
  private String name;

  /**
   * Constructs a new function.
   * <P>
   * Initially the function has no name...be sure to assign one before using the function.
   */
  protected AbstractFunction ()
  {
    this.properties = new Properties ();
  }

  /**
   * Returns the function name.
   *
   * @return The function name.
   */
  public String getName ()
  {
    return this.name;
  }

  /**
   * Sets the function name.
   *
   * @param name The function name (null not permitted).
   * @throws NullPointerException if the given name is null
   */
  public void setName (String name)
  {
    if (name == null)
      throw new NullPointerException ("AbstractFunction.setName():  null not permitted.");

    this.name = name;
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   * <P>
   * The default implementation checks that the function name is not null, and calls the
   * isInitialized() method (now deprecated).
   *
   * @throws FunctionInitializeException if the function name is not set or the call to
   * isInitialized returns false.
   */
  public void initialize () throws FunctionInitializeException
  {
    if (name == null) throw new FunctionInitializeException ("FunctionName is null");
    if (!isInitialized ()) throw new FunctionInitializeException ("isInitialized failed.");
  }

  /**
   * Receives notification that the report has started.
   * <P>
   * Maps the reportStarted-method to the legacy function startReport ().
   *
   * @param event Information about the event.
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
   * Receives notification that the report has finished.
   * <P>
   * Maps the reportFinished-method to the legacy function endReport ().
   *
   * @param event Information about the event.
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
   * Receives notification that a page has started.
   * <P>
   * Maps the pageStarted-method to the legacy function startPage (int).
   *
   * @param event Information about the event.
   */
  public void pageStarted (ReportEvent event)
  {
    ReportState state = event.getState ();
    startPage (state.getCurrentPage ());
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
   * Receives notification that a page has ended.
   * <P>
   * Maps the pageFinished-method to the legacy function endPage (int).
   *
   * @param event Information about the event.
   */
  public void pageFinished (ReportEvent event)
  {
    ReportState state = event.getState ();
    endPage (state.getCurrentPage ());
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
   * Receives notification that a group has started.
   * <P>
   * Maps the groupStarted-method to the legacy function startGroup (int).
   *
   * @param event Information about the event.
   */
  public void groupStarted (ReportEvent event)
  {
    JFreeReport report = event.getReport ();
    ReportState state = event.getState ();
    startGroup (report.getGroup (state.getCurrentGroupIndex ()));
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
   * Receives notification that a group has finished.
   * <P>
   * Maps the groupFinished-method to the legacy function endGroup (int).
   *
   * @param event Information about the event.
   */
  public void groupFinished (ReportEvent event)
  {
    JFreeReport report = event.getReport ();
    ReportState state = event.getState ();
    endGroup (report.getGroup (state.getCurrentGroupIndex ()));
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
   * Receives notification that a row of data is being processed.
   * <P>
   * Maps the itemsAdvanced-method to the legacy function advanceItems (int).
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced (ReportEvent event)
  {
    JFreeReport report = event.getReport ();
    ReportState state = event.getState ();
    advanceItems (report.getData (), state.getCurrentDataItem ());
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
   * <P>
   * Be aware, this does not create a deep copy. If you have complex
   * strucures contained in objects, you have to overwrite this function.
   *
   * @return A clone of the function.
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
   * @param p The properties.
   *
   * @todo create a property query interface. Maybe the same as used in
   * JDBC (@see java.sql.PropertyInfo)?
   */
  public void setProperties (Properties p)
  {
    if (p != null)
    {
      Enumeration names = p.keys();
      while (names.hasMoreElements())
      {
        String name = (String) names.nextElement();
        String prop = (String) p.get (name);
        setProperty(name, prop);
      }
    }
  }

  /**
   * Returns the value of a property.
   * <P>
   * Returns null if no such property was found.
   *
   * @param name The property name.
   *
   * @return The property value.
   */
  public String getProperty (String name)
  {
    return getProperty (name, null);
  }

  /**
   * Returns the value of a property.
   * <P>
   * If there is no property with the specified name, then the defaultVal is returned.
   *
   * @param name The property name.
   * @param defaultVal The default property value.
   *
   * @return The property value.
   */
  public String getProperty (String name, String defaultVal)
  {
    return properties.getProperty (name, defaultVal);
  }

  /**
   * Sets a property for the function.
   *
   * @param name The property name.
   * @param value The property value.
   */
  public void setProperty (String name, String value)
  {
    if (value == null)
      properties.remove (name);
    else
      properties.setProperty (name, value);
  }

  /**
   * returns true, to signal that this part of initialisation resulted in no error.
   *
   * @deprecated initialize() is used to initialize a function.
   */
  public boolean isInitialized ()
  {
    return true;
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted (ReportEvent event)
  {
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished (ReportEvent event)
  {
  }
}
