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
 * $Id: ItemCountFunction.java,v 1.4 2002/05/28 19:36:41 taqua Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 * 10-May-2002 : Applied the ReportEvent interface.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.event.ReportEvent;

/**
 * A report function that counts items in a report.  If the "group" property is set, the item
 * count is reset to zero whenever the group changes.
 */
public class ItemCountFunction extends AbstractFunction implements Cloneable
{

  /** The group (null permitted). */
  private String group;

  /** The item count. */
  private int count;

  /**
   * Constructs an unnamed function.
   * <P>
   * This constructor is intended for use by the SAX handler class only.
   */
  public ItemCountFunction ()
  {
  }

  /**
   * Constructs an item count report function.
   *
   * @param name The name of the function.
   *
   * @throws NullPointerException if the name is null
   */
  public ItemCountFunction (String name)
  {
    setName (name);
  }

  /**
   * Receives notification that a new report is about to start.  The item count is set to zero.
   *
   * @param event Information about the event.
   */
  public void reportStarted (ReportEvent event)
  {
    this.count = 0;
  }

  /**
   * Returns the name of the group (possibly null) for this function.  The item count is reset
   * to zero at the start of each instance of this group.
   *
   * @return The group name.
   */
  public String getGroup ()
  {
    return group;
  }

  /**
   * Setss the name of the group for this function.  The item count is reset to zero at the start
   * of each instance of this group.  If the name is null, all items in the report are counted.
   *
   * @param group The group name.
   */
  public void setGroup (String group)
  {
    this.group = group;
    setProperty("group", group);
  }

  /**
   * Receives notification that a new group is about to start.  Checks to see if the group that
   * is starting is the same as the group defined for this function...if so, the item count is
   * reset to zero.
   *
   * @param event Information about the event.
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
   * Received notification of a move to the next row of data.  Increments the item count.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced (ReportEvent event)
  {
    count++;
  }

  /**
   * Returns the number of items counted (so far) by the function.  This is either the number
   * of items in the report, or the group (if a group has been defined for the function).
   *
   * @return The item count.
   */
  public Object getValue ()
  {
    return new Integer (count);
  }

  /**
   * ???
   */
  public void initialize ()
    throws FunctionInitializeException
  {
    super.initialize ();
    setGroup (getProperty ("group"));
  }

}
