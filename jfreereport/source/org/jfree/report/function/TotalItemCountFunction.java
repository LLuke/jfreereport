/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * TotalGroupCountFunction.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TotalItemCountFunction.java,v 1.9.4.1 2004/12/30 14:46:12 taqua Exp $
 *
 * Changes
 * -------------------------
 * 25.08.2003 : Initial version
 *
 */

package org.jfree.report.function;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.jfree.report.event.ReportEvent;

/**
 * A report function that counts the total number of items contained in groups
 * in a report. If a null-groupname is given, all groups are counted.
 * <p>
 * A group can be defined using the property "group".
 * If the group property is not set, all group starts get counted.
 * 
 * @author Thomas Morgner
 */
public class TotalItemCountFunction extends AbstractFunction implements Serializable
{
  /**
   * An internal storage to collect the total value instead of the 
   * current value. Values in this storage are not affected by cloning. 
   */
  private static class ItemCountStorage implements Serializable
  {
    /** The current group count.*/ 
    private int groupCount;

    /**
     * DefaultConstructor.
     */
    public ItemCountStorage()
    {
    }

    /**
     * Returns the current group count.
     * @return the current group count.
     */
    public int getGroupCount()
    {
      return groupCount;
    }

    /**
     * Defines the current group count.
     * @param groupCount the new value for the group count. 
     */
    public void setGroupCount(final int groupCount)
    {
      this.groupCount = groupCount;
    }
  }

  /** The group sum. */
  private transient ItemCountStorage groupResult;

  /** A list of results. */
  private transient ArrayList results;

  /** The current index. */
  private transient int currentIndex;

  private String group;

  /**
   * Default constructor.
   */
  public TotalItemCountFunction()
  {
    results = new ArrayList();
  }

  /**
   * Receives notification that a new report is about to start.
   * Resets the count.
   *
   * @param event the current report event received.
   */
  public void reportInitialized(final ReportEvent event)
  {
    currentIndex = -1;
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      results.clear();
      groupResult = new ItemCountStorage();
      // if no group is defined, then handle the default ...
      if (getGroup() == null)
      {
        results.add(groupResult);
      }
    }
    else
    {
      // Activate the current group, which was filled in the prepare run.
      if (getGroup() == null)
      {
        groupResult = (ItemCountStorage) results.get(0);
      }
    }
  }

  /**
   * Returns the current group count value.
   *
   * @return the curernt group count.
   */
  protected int getCount()
  {
    if (groupResult == null)
    {
      return 0;
    }
    return groupResult.getGroupCount();
  }

  /**
   * Receives notification that a new group is about to start.
   * Increases the count if all groups are counted or the name defines the current group.
   *
   * @param event the current report event received.
   */
  public void groupStarted(final ReportEvent event)
  {
    // enter defined group ...?
    if (FunctionUtilities.isDefinedGroup(getGroup(), event))
    {
      if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
      {
          groupResult = new ItemCountStorage();
          results.add(groupResult);
      }
      else
      {
        // Activate the current group, which was filled in the prepare run.
        currentIndex += 1;
        groupResult = (ItemCountStorage) results.get(currentIndex);
      }
    }
  }

  /**
   * Defines the current group count value.
   *
   * @param count the curernt group count.
   */
  protected void setCount(final int count)
  {
    if (groupResult == null)
    {
      groupResult = new ItemCountStorage();
    }
    groupResult.setGroupCount(count);
  }

  /**
   * Returns the name of the group to be totalled.
   *
   * @return the group name.
   */
  public String getGroup()
  {
    return group;
  }

  /**
   * Defines the name of the group to be totalled.
   * If the name is null, all groups are totalled.
   *
   * @param group  the group name.
   */
  public void setGroup(final String group)
  {
    this.group = group;
  }


  /**
   * Received notification of a move to the next row of data.  Increments the item count.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      setCount(getCount() + 1);
    }
  }

  /**
   * Returns the number of items counted (so far) by the function.  This is either the number
   * of items in the report, or the group (if a group has been defined for the function).
   *
   * @return The item count.
   */
  public Object getValue()
  {
    return new Integer(getCount());
  }

  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final TotalItemCountFunction function = (TotalItemCountFunction) super.getInstance();
    function.groupResult = new ItemCountStorage();
    function.results = new ArrayList();
    return function;
  }

  private void readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    results = new ArrayList();
    groupResult = new ItemCountStorage();
  }


}
