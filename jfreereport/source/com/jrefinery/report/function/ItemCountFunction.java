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
 * ----------------------
 * ItemCountFunction.java
 * ----------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemCountFunction.java,v 1.1.1.1 2002/04/25 17:02:32 taqua Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 * 10-May-2002 : Applied the ReportEvent interface.
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.event.ReportEvent;

/**
 * A report function that counts items in a report. If the group-property is set,
 * only items in a particular group are counted.
 */
public class ItemCountFunction extends AbstractFunction implements Cloneable
{

  private String group;

  /** The number of items. */
  private int count;

  /**
   * Default constructor.
   */
  public ItemCountFunction ()
  {
  }

  /**
   * Constructs an item count report function.
   *
   * @param name the name of the function
   * @throws NullPointerException if the name is null
   */
  public ItemCountFunction (String name)
  {
    setName (name);
  }

  /**
   * Receives notification that a new report is about to start.
   */
  public void reportStarted (ReportEvent event)
  {
    this.count = 0;
  }

  /**
   * Returns the name of the group to be counted.
   */
  public String getGroup ()
  {
    return group;
  }

  /**
   * defines the name of the group to be counted.
   * If the name is null, all groups are counted.
   */
  public void setGroup (String group)
  {
    this.group = group;
    setProperty("group", group);
  }

  /**
   * Receives notification that a new group is about to start.
   */
  public void groupStarted (ReportEvent event)
  {
    if (getGroup () == null)
      return;

    JFreeReport report = event.getReport ();
    ReportState state = event.getState ();
    Group group = report.getGroup (state.getCurrentGroupIndex ());
    if (getGroup ().equals (group.getName ()))
    {
      this.count = 0;
    }
  }

  /**
   * Move to the next row of data.
   */
  public void itemsAdvanced (ReportEvent event)
  {
    count++;
  }

  /**
   * Returns the number of items (so far) in the function's group.
   */
  public Object getValue ()
  {
    return new Integer (count);
  }

  /**
   * Returns a copy of this function.
   */
  public Object clone ()
  {

    Object result = null;

    try
    {
      result = super.clone ();
    }
    catch (CloneNotSupportedException e)
    {
      // this should never happen...
      System.err.println ("ItemCountFunction: clone not supported");
    }

    return result;

  }

  public void initialize ()
    throws FunctionInitializeException
  {
    super.initialize ();
    setGroup (getProperty ("group"));
  }
}
