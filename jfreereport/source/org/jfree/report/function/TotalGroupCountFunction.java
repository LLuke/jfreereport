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
 * $Id: TotalGroupCountFunction.java,v 1.2 2003/09/08 18:11:48 taqua Exp $
 *
 * Changes
 * -------------------------
 * 25.08.2003 : Initial version
 *
 */

package org.jfree.report.function;

import java.io.Serializable;

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

  /**
   * Default constructor.
   */
  public TotalGroupCountFunction()
  {
  }

  /**
   * Receives notification that a new report is about to start.
   * Resets the count.
   *
   * @param event the current report event received.
   */
  public void reportInitialized(final ReportEvent event)
  {
    storage = null;
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
}
