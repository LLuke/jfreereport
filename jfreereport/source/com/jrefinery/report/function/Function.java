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
 * -------------------
 * ReportFunction.java
 * -------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner and modified by DG (DG);
 *
 */

package com.jrefinery.report.function;

import javax.swing.table.TableModel;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.Group;
import java.util.Properties;

/**
 * The interface for report functions.  A report function separates the business logic from
 * presentation of the result.  The function is called whenever JFreeReport changes its state
 * while generating the report. The working model for the functions is based on cloning the
 * state of the function on certain checkpoints to support the ReportState implementation of
 * JFreeReport.
 * <p>
 */
public interface Function {

    /**
     * Returns the name of the function (every function is required to have a unique name).
     */
    public String getName();

    /**
     * Sets the name of the function.
     * @param name The name.
     */
    public void setName(String name);

    /**
     * A new report will be generated. Reset internal values to an initial state.
     */
    public void initialise();

    /**
     * This method will be called when a new report is started, giving the function a chance to
     * initialise itself.
     */
    public void startReport(JFreeReport report);

    /**
     * This method will be called when a report is ended.
     */
    public void endReport(JFreeReport report);

    /**
     * A new page will be started. Prepare for printing the page header.
     */
    public void startPage(int page);

    /**
     * A page is fully printed. Prepare for printing the page footer.
     */
    public void endPage(int page);

    /**
     * A new group has been encountered. Prepare for printing the group header.
     */
    public void startGroup(Group group);

    /**
     * A group has ended. Prepare for printing the group footer.
     */
    public void endGroup(Group group);

    /**
     * A data line will be printed.
     */
    public void advanceItems(TableModel data, int row);

    /**
     * Return the current function value. The value depends on the
     * function. A page counting function for instance will return the
     * current page number.
     */
    public Object getValue();

    /**
     * Clones the function in its current state.  This is used for recording the report state at
     * page boundaries.
     */
    public Object clone() throws CloneNotSupportedException;

    /**
     * Set the function properties. This is the only way to feed
     * parameters to an function
     */
    public void setProperties (Properties p);
    
    /**
     * return false, if not all parameters are set, or some other
     * error occures during initialisation
     */
    public boolean isInitialized ();
    
}