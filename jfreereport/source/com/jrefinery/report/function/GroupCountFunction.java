/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -----------------------
 * GroupCountFunction.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: GroupCountFunction.java,v 1.11 2002/12/12 12:26:56 mungady Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 10-May-2002 : Added propertyHandling to support the ReportGenerators initializations.
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.states.ReportState;

/**
 * A report function that counts groups in a report.
 * If a null-groupname is given, all groups are counted.
 * <p>
 * A group can be defined using the property "group".
 * If the group property is not set, all group starts get counted.
 *
 * @author David Gilbert
 */
public class GroupCountFunction extends AbstractFunction implements Cloneable
{
  /** Literal text for the 'group' property. */
  public static final String GROUP_PROPERTY = "group";

  /** The number of groups. */
  private int count;

  /**
   * Default constructor.
   */
  public GroupCountFunction()
  {
  }

  /**
   * Constructs a report function for counting groups.
   *
   * @param name The function name.
   * @param group The group name.
   * @throws NullPointerException if the given name is null
   */
  public GroupCountFunction(String name, String group)
  {
    setName(name);
    setGroup(group);
  }

  /**
   * Returns the name of the group to be counted.
   *
   * @return the name of the group or null, if all groups are counted
   */
  public String getGroup()
  {
    return getProperty(GROUP_PROPERTY);
  }

  /**
   * defines the name of the group to be counted.
   * If the name is null, all groups are counted.
   *
   * @param group the name of the group to be counted.
   */
  public void setGroup(String group)
  {
    setProperty(GROUP_PROPERTY, group);
  }

  /**
   * Receives notification that a new report is about to start.
   * Resets the count.
   *
   * @param event the current report event received.
   */
  public void reportStarted(ReportEvent event)
  {
    this.count = 0;
  }

  /**
   * Receives notification that a new group is about to start.
   * Increases the count if all groups are counted or the name defines the current group.
   *
   * @param event the current report event received.
   */
  public void groupStarted(ReportEvent event)
  {
    if (getGroup() == null)
    {
      this.count++;  // count all groups...
      return;
    }

    ReportState state = event.getState();
    Group group = event.getReport().getGroup(state.getCurrentGroupIndex());
    if (getGroup().equals(group.getName()))
    {
      this.count++;
    }

  }

  /**
   * Returns the number of groups processed so far (including the current group).
   *
   * @return the number of groups processed as java.lang.Integer.
   */
  public Object getValue()
  {
    return new Integer(count);
  }
}
