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
 * ----------------------
 * ItemCountFunction.java
 * ----------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 */

package com.jrefinery.report.function;

import javax.swing.table.TableModel;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.Group;

/**
 * A report function that counts items in a report.
 */
public class ItemCountFunction extends AbstractFunction implements Cloneable {

    private String group;

    /** The number of items. */
    private int count;

    /**
     * Default constructor.
     */
    public ItemCountFunction() {
        this(null);
    }

    /**
     * Constructs an item count report function.
     */
    public ItemCountFunction(String name) {
        super(name);
    }

    /**
     * Initialises the function when it is first created.  The function is also given a chance
     * to perform initialisation every time a report starts (see the startReport() method).
     */
    public void initialise() {
  	this.count=0;
    }

    /**
     * Receives notification that a new report is about to start.
     */
    public void startReport(JFreeReport report) {
        this.count=0;
    }

    public String getGroup ()
    {
      return group;
    }
    
    public void setGroup (String group)
    {
      if (group == null)
        throw new NullPointerException ();
      this.group = group;  
    }

    /**
     * Receives notification that a new group is about to start.
     */
    public void startGroup(Group group) {

      if (getGroup().equals(group.getName())) {
	        this.count = 0;
      }

    }

    /**
     * Move to the next row of data.
     */
    public void advanceItems(TableModel data, int row) {
  	count++;
    }

    /**
     * Returns the number of items (so far) in the function's group.
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

  public boolean isInitialized ()
  {
    String groupProp = getProperty ("group");
    if (groupProp == null)
    {
       System.out.println ("No Such Property : group");
       return false;
    }
    setGroup (groupProp);
    return (getGroup() != null);
  }
}
