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
 * $Id: TotalItemCountFunction.java,v 1.1 2003/10/17 17:39:33 taqua Exp $
 *
 * Changes
 * -------------------------
 * 25.08.2003 : Initial version
 *
 */

package org.jfree.report.function;

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
public class TotalItemCountFunction extends AbstractFunction
{
  /**
   * An internal storage to collect the total value instead of the 
   * current value. Values in this storage are not affected by cloning. 
   */
  private static class ItemCountStorage
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

  /** Literal text for the 'group' property. */
  public static final String GROUP_PROPERTY = "group";

  /** The group sum. */
  private ItemCountStorage groupResult;

  /** A list of results. */
  private ArrayList results;

  /** The current index. */
  private int currentIndex;

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
    groupResult = null;
    currentIndex = -1;
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      results.clear();
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
  public void groupStarted(ReportEvent event)
  {
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      groupResult = new ItemCountStorage();
      results.add(groupResult);
    }
    else
    {
      if (FunctionUtilities.isLayoutLevel(event))
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
    return getProperty(GROUP_PROPERTY);
  }

  /**
   * Defines the name of the group to be totalled.
   * If the name is null, all groups are totalled.
   *
   * @param group  the group name.
   */
  public void setGroup(final String group)
  {
    setProperty(GROUP_PROPERTY, group);
  }


  /**
   * Received notification of a move to the next row of data.  Increments the item count.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    setCount(getCount() + 1);
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
    function.groupResult = new TotalItemCountFunction.ItemCountStorage();
    function.results = new ArrayList();
    return function;
  }

}
