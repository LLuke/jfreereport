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
 * $Id: TotalGroupCountFunction.java,v 1.4 2003/11/07 20:37:35 taqua Exp $
 *
 * Changes
 * -------------------------
 * 25-Aug-2003 : Initial version
 * 08-Nov-2003 : BugFixes after JUnit complained ...
 */

package org.jfree.report.function;

import java.io.Serializable;
import java.util.ArrayList;

import org.jfree.report.event.ReportEvent;

/**
 * A report function that counts the total of groups in a report.
 * If a null-groupname is given, all groups are counted.
 * <p>
 * A group can be defined using the property "group".
 * If the group property is not set, all group starts get counted.
 *
 * @author Thomas Morgner
 */
public class TotalGroupCountFunction extends GroupCountFunction
{
  /**
   * An internal storage to collect the total value instead of the
   * current value. Values in this storage are not affected by cloning.
   */
  private static class GroupCountStorage implements Serializable
  {
    /** The current group count.*/
    private int groupCount;

    /**
     * DefaultConstructor.
     */
    public GroupCountStorage()
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

  /** The storage used to save the computed total. */
  private GroupCountStorage storage;

  /** A list of results. */
  private ArrayList results;

  /** The current index. */
  private int currentIndex;

  /**
   * Default constructor.
   */
  public TotalGroupCountFunction()
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
      storage = null;
      results.clear();
      if (getParentGroup() == null)
      {
        storage = new GroupCountStorage();
        results.add(storage);
      }
    }
    else
    {
      // Activate the current group, which was filled in the prepare run.
      if (getParentGroup() == null)
      {
        storage = (GroupCountStorage) results.get(0);
      }
    }
  }

  /**
   * Receives notification that a new group is about to start.
   * Increases the count if all groups are counted or the name defines the current group.
   *
   * @param event the current report event received.
   */
  public void groupStarted(ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getParentGroup(), event))
    {
      // reset ...
      if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
      {
        storage = new GroupCountStorage();
        results.add(storage);

        // if no group is defined, then count all groups, including the
        // current one...
        if (getGroup() == null)
        {
          setCount(getCount() + 1);
        }
      }
      else
      {
        if (FunctionUtilities.isLayoutLevel(event))
        {
          // Activate the current group, which was filled in the prepare run.
          currentIndex += 1;
          storage = (GroupCountStorage) results.get(currentIndex);
        }
      }
    }
    else
    {
      if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
      {
        super.groupStarted(event);
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
    if (storage == null)
    {
      return 0;
    }
    return storage.getGroupCount();
  }

  /**
   * Defines the current group count value.
   *
   * @param count the curernt group count.
   */
  protected void setCount(final int count)
  {
    if (storage == null)
    {
      storage = new GroupCountStorage();
    }
    storage.setGroupCount(count);
  }

  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    TotalGroupCountFunction fn =
        (TotalGroupCountFunction) super.getInstance();
    fn.storage = null;
    fn.results = new ArrayList();
    return fn;
  }
}
