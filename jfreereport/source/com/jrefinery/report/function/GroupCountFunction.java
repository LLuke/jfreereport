/**
 * =============================================================
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
 * $Id: GroupCountFunction.java,v 1.1 2002/05/07 14:20:07 mungady Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 10-May-2002 : Added propertyHandling to support the ReportGenerators initializations.
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.event.ReportEvent;

/**
 * A report function that counts groups in a report.
 * If a null-groupname is given, all groups are counted.
 * A group can be defined using the property "group".
 */
public class GroupCountFunction extends AbstractFunction implements Cloneable
{

  /** The number of groups. */
  private int count;

  private String groupName;

  /**
   * Default constructor.
   */
  public GroupCountFunction ()
  {
  }

  /**
   * Constructs a report function for counting groups.
   *
   * @param name The function name.
   * @param group The group name.
   * @throws NullPointerException if the given name is null
   */
  public GroupCountFunction (String name, String group)
  {
    setName (name);
    this.groupName = group;
  }

  /**
   * Returns the name of the group to be counted.
   */
  public String getGroup ()
  {
    return groupName;
  }

  /**
   * defines the name of the group to be counted.
   * If the name is null, all groups are counted.
   */
  public void setGroup (String group)
  {
    this.groupName = group;
    setProperty("group", group);
  }

  /**
   * Receives notification that a new report is about to start.
   */
  public void reportStarted (ReportEvent event)
  {
    this.count = 0;
  }

  /**
   * Receives notification that a new group is about to start.
   */
  public void groupStarted (ReportEvent event)
  {
    if (getGroup() == null)
    {
      this.count++;  // count all groups...
      return;
    }

    JFreeReport report = event.getReport();
    ReportState state = event.getState();
    Group group = report.getGroup(state.getCurrentGroupIndex());
    if (getGroup().equals (group.getName ()))
    {
      this.count++;
    }

  }

  /**
   * Returns the number of groups processed so far (including the current group).
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

  /**
   * Initializes this function.
   * If the property "group" is present, the group will be set to the properties value.
   */
  public void initialize ()
     throws FunctionInitializeException
  {
    super.initialize ();
    setGroup(getProperty("group"));
  }

}