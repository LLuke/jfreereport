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
 * -----------------------
 * FunctionCollection.java
 * -----------------------
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
 *
 */

package com.jrefinery.report;

import java.util.Collection;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.table.TableModel;
import com.jrefinery.report.function.Function;

/**
 * A function collection contains all function elements of a particular report.
 * The collection and its funtions are cloned with every page to enable some
 * sort of caching.
 * <p>
 * Every event in JFreeReport is passed via callback to every function in the
 * collection.
 */
public class FunctionCollection implements Cloneable {

    /** Storage for the functions in the collection. */
    protected Map functions;

    /**
     * Creates a new empty function collection.
     */
    public FunctionCollection() {

        this(null);

    }

    /**
     * Constructs a new function collection, populated with the supplied functions.
     */
    public FunctionCollection(Collection functions) {

        this.functions = new TreeMap();

        if (functions!=null) {
            Iterator iterator = functions.iterator();
            while (iterator.hasNext()) {
                Function f = (Function)iterator.next();
                this.functions.put(f.getName(), f);
            }
        }

    }

    /**
     * Returns the function with the specified name (or null).
     */
    public Function get(String name) {

        Function result = (Function)functions.get(name);

        return result;

    }

    /**
     * Adds a new function to the collection.
     * @param f the new function instance.
     */
    public void add(Function f) {
      if (f == null)
         throw new NullPointerException ("Function is null");
         
  	functions.put(f.getName(), f);

    }

    /**
     * Notifies every function in the collection that a report is starting.  This gives each
     * function an opportunity to initialise itself for a new report.
     *
     * @param report The report.
     */
    public void startReport(JFreeReport report) {

        Iterator iterator = this.functions.values().iterator();
        while (iterator.hasNext()) {
            Function f = (Function)iterator.next();
            f.startReport(report);
        }

    }

    /**
     * Notifies every function in the collection that a report is ending.
     *
     * @param report The report.
     */
    public void endReport(JFreeReport report) {

        Iterator iterator = this.functions.values().iterator();
        while (iterator.hasNext()) {
            Function f = (Function)iterator.next();
            f.endReport(report);
        }

    }

    /**
     * Notifies every function in the collection that a page is starting.
     */
    public void startPage(int page) {

        Iterator iterator = this.functions.values().iterator();
        while (iterator.hasNext()) {
            Function f = (Function)iterator.next();
            f.startPage(page);
        }

    }

    /**
     * Send "EndPage" to every function in this collection.
     * <p>
     * Function Events are sended before the function is asked to
     * print itself.
     */
    public void endPage(int page) {

        Iterator iterator = this.functions.values().iterator();
        while (iterator.hasNext()) {
            Function f = (Function)iterator.next();
            f.endPage(page);
        }

    }

    /**
     * Notifies every function in the collection that a new group is starting.  This gives each
     * function an opportunity to reset itself, if it belongs to the group.
     *
     * @param group The group that is starting.
     */
    public void startGroup(Group group) {

        Iterator iterator = this.functions.values().iterator();
        while (iterator.hasNext()) {
            Function f = (Function)iterator.next();
            f.startGroup(group);
        }

    }

    /**
     * Notifies every function in the collection that the current group is ending.
     *
     * @param group The group that is ending.
     */
    public void endGroup(Group group) {

        Iterator iterator = this.functions.values().iterator();
        while (iterator.hasNext()) {
            Function f = (Function)iterator.next();
            f.endGroup(group);
        }

    }

    /**
     * Notifies every function in the collection that a new row of data is being processed.  This
     * gives each function an opportunity to update its value.
     */
    public void advanceItems(TableModel data, int row) {

        Iterator iterator = this.functions.values().iterator();
        while (iterator.hasNext()) {
            Function f = (Function)iterator.next();
            f.advanceItems(data, row);
        }

    }

    /**
     * Returns a string representation of the function collection.  Used in debugging only.
     */
    public String toString() {

        String result = "Function Collection:\n";

        Iterator iterator = this.functions.values().iterator();
        while (iterator.hasNext()) {
            Function f = (Function)iterator.next();
            result = result+f.getName()+" = "+f.getValue().toString()+"\n";
        }

        return result;

    }

    /**
     * Returns a copy of the function collection.
     */
    public Object clone() {

        FunctionCollection result = new FunctionCollection();

        Iterator iterator = this.functions.values().iterator();
        while (iterator.hasNext()) {
            Function f = (Function)iterator.next();
            try {
                Function copy = (Function)f.clone();
                result.add(copy);
            }
            catch (CloneNotSupportedException e) {
                System.err.println("FunctionCollection: problem cloning function.");
            }
        }

        return result;

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Send "initReport" to every function in this collection.
     * <p>
     * Function Events are sended before the function is asked to print itself.
     */
    public void sendInitReport() {

        //Iterator iterator = function
        //while (e.hasMoreElements()) {
    	//    Function fn = (Function)e.nextElement();
        //    fn.initialise();
        // }

    }

    /**
     * Returns the value of the current clone of the keying function.
     *
     * @param key the function element which was created by the parser.
     */
    public Object getValue (Function key) {

        Function fn = (Function)functions.get(key);

        if (fn == null) {
    	    System.out.println("No Function " + fn + " found, using key " + key + " as function");
    	    fn = key;
        }
        return fn.getValue();

    }

    /**
     * Returns the number of active functions in this collection
     */
    public int size () {

  	return functions.size ();

    }

}