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
 * ----------------
 * ReportStateProgress.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 06.04.2003 : Initial version
 */
package com.jrefinery.report.states;

public class ReportStateProgress
{
  private int currentGroupIndex;
  private int currentDataItem;
  private int currentPage;
  private Class stateClass;

  public ReportStateProgress()
  {
  }

  public ReportStateProgress(int currentGroupIndex, int currentDataItem, int currentPage, Class stateClass)
  {
    this.currentGroupIndex = currentGroupIndex;
    this.currentDataItem = currentDataItem;
    this.currentPage = currentPage;
    if (stateClass == null)
    {
      throw new NullPointerException("StateClass must not be null");
    }
    this.stateClass = stateClass;
  }

  public void setCurrentGroupIndex(int currentGroupIndex)
  {
    this.currentGroupIndex = currentGroupIndex;
  }

  public void setCurrentDataItem(int currentDataItem)
  {
    this.currentDataItem = currentDataItem;
  }

  public void setCurrentPage(int currentPage)
  {
    this.currentPage = currentPage;
  }

  public void setStateClass(Class stateClass)
  {
    if (stateClass == null)
    {
      throw new NullPointerException("StateClass must not be null");
    }
    this.stateClass = stateClass;
  }

  public int getCurrentGroupIndex()
  {
    return currentGroupIndex;
  }

  public int getCurrentDataItem()
  {
    return currentDataItem;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public Class getStateClass()
  {
    return stateClass;
  }
}
