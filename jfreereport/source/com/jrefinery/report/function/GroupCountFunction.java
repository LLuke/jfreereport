/* =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * GroupCountFunction.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.function;

import javax.swing.table.TableModel;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.Group;

/**
 * A report function that counts groups in a report.
 */
public class GroupCountFunction extends AbstractFunction implements Cloneable {

    /** The number of groups. */
    private int count;

    private String groupName;

    /**
     * Default constructor.
     */
    public GroupCountFunction() {
        this(null, null);
    }

    /**
     * Constructs a report function for counting groups.
     *
     * @param name The function name.
     * @param group The group name.
     *
     */
    public GroupCountFunction(String name, String group) {
        super(name);
        this.groupName = group;
    }

    /**
     * Initialises the function when it is first created.  The function is also given a chance
     * to perform initialisation every time a report starts (see the startReport() method).
     */
    public void initialise() {
        this.count=0;
    }

    public boolean isInitialized () {
        return true;
    }

    /**
     * Receives notification that a new report is about to start.
     */
    public void startReport(JFreeReport report) {
        this.count=0;
    }

    /**
     * Receives notification that a new group is about to start.
     */
    public void startGroup(Group group) {

        if (this.groupName!=null) {
            if (this.groupName.equals(group.getName())) {
                this.count++;
            }
        }
        else {
            this.count++;  // count all groups...
        }

    }

    /**
     * Returns the number of groups processed so far (including the current group).
     */
    public Object getValue() {
        return new Integer(count);
    }

    /**
     * Returns a copy of this function.
     */
    public Object clone() {

        Object result = null;

        try {
            result = super.clone();
        }
        catch (CloneNotSupportedException e) {
            // this should never happen...
            System.err.println("ItemCountFunction: clone not supported");
        }

        return result;

    }

}