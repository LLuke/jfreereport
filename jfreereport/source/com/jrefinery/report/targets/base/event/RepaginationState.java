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
 * ----------------------
 * RepaginationState.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RepaginationState.java,v 1.2 2003/04/09 15:50:33 mungady Exp $
 *
 * Changes
 * -------
 * 18.03.2003 : Initial version
 */
package com.jrefinery.report.targets.base.event;

/**
 * The repagination state.
 *
 * @author Thomas Morgner.
 */
public class RepaginationState
{
  /** The current pass. */
  private int pass;

  /** The page. */
  private int page;

  /** The current row. */
  private int currentRow;

  /** The maximum row. */
  private int maxRow;

  /**
   * Creates a new state.
   *
   * @param pass  the pass.
   * @param page  the page.
   * @param currentRow  the current row.
   * @param maxRow  the maximum row.
   */
  public RepaginationState(int pass, int page, int currentRow, int maxRow)
  {
    this.pass = pass;
    this.page = page;
    this.currentRow = currentRow;
    this.maxRow = maxRow;
  }

  /**
   * Returns the pass.
   *
   * @return The pass.
   */
  public int getPass()
  {
    return pass;
  }

  /**
   * Returns the page.
   *
   * @return The page.
   */
  public int getPage()
  {
    return page;
  }

  /**
   * Returns the current row.
   *
   * @return the current row.
   */
  public int getCurrentRow()
  {
    return currentRow;
  }

  /**
   * Returns the max row.
   *
   * @return the max row.
   */
  public int getMaxRow()
  {
    return maxRow;
  }
}
