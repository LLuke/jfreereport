/* =============================================================
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
 * $Id$
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Added property support and removed the get/set Field/Group 
 *               functions.
 */

package com.jrefinery.report.function;

import javax.swing.table.TableModel;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.Group;
import java.util.Properties;

/**
 * Base class for implementing new report functions.  Provides empty implementations of all the
 * methods in the ReportFunction interface.
 *
 * All parameters are checked by the parser using the isInitialized () function.
 * If the function returns false, one or more properties are missing and the
 * report parsing will be aborted. Make sure, that you fully implement all validity 
 * checks using this function.
 */
public abstract class AbstractFunction implements Function, Cloneable {

    protected Properties properties;

    /** The function name. */
    protected String name;

    /**
     * Constructs a new function.
     *
     * @param name The function name.
     */
    protected AbstractFunction(String name) 
    {
        this.name = name;
        this.properties = new Properties ();
    }

    /**
     * Returns the function name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the function name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Initialises the function.
     */
    public void initialise() {
        // do nothing
    }

    /**
     * Receives notification that a report is starting.
     */
    public void startReport(JFreeReport report) {
        // do nothing
    }

    /**
     * Receives notification that a report is ending.
     */
    public void endReport(JFreeReport report) {
        // do nothing
    }

    /**
     * Receives notification that a page is starting.
     */
    public void startPage(int page) {
        // do nothing
    }

    /**
     * Receives notification that a page is ending.
     */
    public void endPage(int page) {
        // do nothing
    }

    /**
     * Receives notification that a group is starting.
     */
    public void startGroup(Group g) {
        // do nothing
    }

    /**
     * Receives notification that a group is ending.
     */
    public void endGroup(Group g) {
        // do nothing
    }

    /**
     * Processes a row of data.
     */
    public void advanceItems(TableModel data, int row) {
        // do nothing
    }

    /**
     * Returns a clone of the function.
     *
     * Be aware, this does not create a deep copy. If you have complex
     * strucures contained in objects, you have to overwrite this function.
     */
    public Object clone() throws CloneNotSupportedException {

        return super.clone();

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
      System.out.println ("Set Properties called" + p);
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
}