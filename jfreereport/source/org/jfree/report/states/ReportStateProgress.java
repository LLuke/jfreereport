/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------
 * ReportStateProgress.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportStateProgress.java,v 1.3 2003/08/24 15:13:23 taqua Exp $
 *
 * Changes
 * -------
 * 06.04.2003 : Initial version
 */
package org.jfree.report.states;

/**
 * Report state progress.
 *
 * @author Thomas Morgner.
 */
public class ReportStateProgress
{
  /** The current group index. */
  private int currentGroupIndex;

  /** The current data item. */
  private int currentDataItem;

  /** The current page. */
  private int currentPage;

  /** The state class. */
  private Class stateClass;

  /**
   * Default constructor.
   */
  public ReportStateProgress()
  {
  }

  /**
   * Creates a new instance.
   *
   * @param currentGroupIndex  the current group index.
   * @param currentDataItem  the current data item.
   * @param currentPage  the current page.
   * @param stateClass  the state class.
   */
  public ReportStateProgress(final int currentGroupIndex, final int currentDataItem,
                             final int currentPage, final Class stateClass)
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

  /**
   * Sets the current group index.
   *
   * @param currentGroupIndex  the new index value.
   */
  public void setCurrentGroupIndex(final int currentGroupIndex)
  {
    this.currentGroupIndex = currentGroupIndex;
  }

  /**
   * Sets the current data item.
   *
   * @param currentDataItem  the current data item.
   */
  public void setCurrentDataItem(final int currentDataItem)
  {
    this.currentDataItem = currentDataItem;
  }

  /**
   * Sets the current page.
   *
   * @param currentPage  the current page.
   */
  public void setCurrentPage(final int currentPage)
  {
    this.currentPage = currentPage;
  }

  /**
   * Sets the state class.
   *
   * @param stateClass  the state class.
   */
  public void setStateClass(final Class stateClass)
  {
    if (stateClass == null)
    {
      throw new NullPointerException("StateClass must not be null");
    }
    this.stateClass = stateClass;
  }

  /**
   * Returns the current group index.
   *
   * @return The group index.
   */
  public int getCurrentGroupIndex()
  {
    return currentGroupIndex;
  }

  /**
   * Returns the current data item.
   *
   * @return The current data item.
   */
  public int getCurrentDataItem()
  {
    return currentDataItem;
  }

  /**
   * Returns the current page.
   *
   * @return The current page.
   */
  public int getCurrentPage()
  {
    return currentPage;
  }

  /**
   * Returns the state class.
   *
   * @return The state class.
   */
  public Class getStateClass()
  {
    return stateClass;
  }
}
