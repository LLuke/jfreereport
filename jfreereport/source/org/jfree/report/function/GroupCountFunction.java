/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: GroupCountFunction.java,v 1.7 2005/01/25 00:00:10 taqua Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 10-May-2002 : Added propertyHandling to support the ReportGenerators initializations.
 */

package org.jfree.report.function;

import java.io.Serializable;

import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.util.Log;

/**
 * A report function that counts groups in a report. If a null-groupname is given, all
 * groups are counted.
 * <p/>
 * The group to be counted can be defined using the property "group". An optional
 * container group can be defined using the property "parent-group". When the group start
 * event of that group is encountered, the counter will be reset to '0'.
 * <p/>
 * If the group property is not set, all group starts get counted.
 *
 * @author David Gilbert
 */
public class GroupCountFunction extends AbstractFunction implements Serializable
{
  private String group;
  private String parentGroup;
  /**
   * The number of groups.
   */
  private int count;

  /**
   * Default constructor.
   */
  public GroupCountFunction ()
  {
  }

  /**
   * Constructs a report function for counting groups.
   *
   * @param name  The function name.
   * @param group The group name.
   * @throws NullPointerException if the given name is null
   */
  public GroupCountFunction (final String name, final String group)
  {
    setName(name);
    setGroup(group);
  }

  /**
   * Returns the name of the group on which to reset the counter.
   *
   * @return the name of the group or null, if all groups are counted
   */
  public String getParentGroup ()
  {
    return parentGroup;
  }

  /**
   * defines the name of the group on which to reset the counter. If the name is null, all
   * groups are counted.
   *
   * @param group the name of the group to be counted.
   */
  public void setParentGroup (final String group)
  {
    this.parentGroup = group;
  }

  /**
   * Returns the name of the group to be counted.
   *
   * @return the name of the group or null, if all groups are counted
   */
  public String getGroup ()
  {
    return group;
  }

  /**
   * defines the name of the group to be counted. If the name is null, all groups are
   * counted.
   *
   * @param group the name of the group to be counted.
   */
  public void setGroup (final String group)
  {
    this.group = group;
  }

  /**
   * Receives notification that a new report is about to start. Resets the count.
   *
   * @param event the current report event received.
   */
  public void reportInitialized (final ReportEvent event)
  {
    setCount(0);
  }

  /**
   * Receives notification that a new group is about to start. Increases the count if all
   * groups are counted or the name defines the current group.
   *
   * @param event the current report event received.
   */
  public void groupStarted (final ReportEvent event)
  {
    final Group group = FunctionUtilities.getCurrentGroup(event);

    if (getParentGroup() != null)
    {
      if (getParentGroup().equals(group.getName()))
      {
        setCount(0);
      }
    }

    if (getGroup() == null)
    {
      // count all groups...
      setCount(getCount() + 1);
    }
    else if (getGroup().equals(group.getName()))
    {
      setCount(getCount() + 1);
    }
    else
    {
      Log.debug("Missmatch " + group.getName());
    }

  }

  /**
   * Returns the current group count value.
   *
   * @return the curernt group count.
   */
  protected int getCount ()
  {
    return count;
  }

  /**
   * Defines the current group count value.
   *
   * @param count the curernt group count.
   */
  protected void setCount (final int count)
  {
    this.count = count;
  }

  /**
   * Returns the number of groups processed so far (including the current group).
   *
   * @return the number of groups processed as java.lang.Integer.
   */
  public Object getValue ()
  {
    return new Integer(getCount());
  }
}
