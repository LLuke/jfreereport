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
 * $Id$
 *
 * Changes
 * -------------------------
 * 25.08.2003 : Initial version
 *
 */

package org.jfree.report.function;

import org.jfree.report.event.ReportEvent;

public class TotalGroupCountFunction extends GroupCountFunction
{
  private static class GroupCountStorage
  {
    private int groupCount;

    public GroupCountStorage()
    {
    }

    public int getGroupCount()
    {
      return groupCount;
    }

    public void setGroupCount(final int groupCount)
    {
      this.groupCount = groupCount;
    }
  }

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
