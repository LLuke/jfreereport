/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: RepaginationState.java,v 1.4 2004/05/07 08:03:37 mungady Exp $
 *
 * Changes
 * -------
 * 18.03.2003 : Initial version
 */
package org.jfree.report.event;

import java.util.EventObject;

/**
 * The repagination state.
 *
 * @author Thomas Morgner.
 */
public class RepaginationState extends EventObject
{
  /**
   * The current pass.
   */
  private int pass;

  /**
   * The page.
   */
  private int page;

  /**
   * The current row.
   */
  private int currentRow;

  /**
   * The maximum row.
   */
  private int maxRow;

  /**
   * Whether the event was generated during the prepare run...
   */
  private boolean prepare;

  /**
   * Creates a new state.
   *
   * @param source     the source object that fired the event.
   * @param pass       the pass the current function level of the processor. This counts
   *                   down to -1.
   * @param page       the page that is currently being processed, or -1 if the page is
   *                   not known.
   * @param currentRow the current row the current row of the tablemodel that is
   *                   processed.
   * @param maxRow     the maximum row the total number of rows in the tablemodel.
   * @param prepare    true, if the event was fired by a prepare run, false otherwise.
   */
  public RepaginationState (final Object source, final int pass,
                            final int page, final int currentRow,
                            final int maxRow, final boolean prepare)
  {
    super(source);
    reuse(pass, page, currentRow, maxRow, prepare);
  }

  /**
   * Returns the pass, which is the current function level of the report processor.
   *
   * @return the report processors function level.
   */
  public int getPass ()
  {
    return pass;
  }

  /**
   * Returns the current page.
   *
   * @return The page or -1 if the page is not known.
   */
  public int getPage ()
  {
    return page;
  }

  /**
   * Returns the current row.
   *
   * @return the current row.
   */
  public int getCurrentRow ()
  {
    return currentRow;
  }

  /**
   * Returns the number of rows in the tablemodel of the report.
   *
   * @return the max row.
   */
  public int getMaxRow ()
  {
    return maxRow;
  }

  /**
   * Makes it possible to reuse the event object. Repagination events are generated in
   * masses, and it wastes resources to throw them away.
   *
   * @param pass       the pass the current function level of the processor. This counts
   *                   down to -1.
   * @param page       the page that is currently being processed, or -1 if the page is
   *                   not known.
   * @param currentRow the current row the current row of the tablemodel that is
   *                   processed.
   * @param maxRow     the maximum row the total number of rows in the tablemodel.
   * @param prepare    true, if the event was fired by a prepare run, false otherwise.
   */
  public void reuse (final int pass, final int page, final int currentRow,
                     final int maxRow, final boolean prepare)
  {
    this.pass = pass;
    this.page = page;
    this.currentRow = currentRow;
    this.maxRow = maxRow;
    this.prepare = prepare;
  }

  /**
   * Checks, whether the event was fired during a prepare run of the report processor.
   *
   * @return true, if the report processor works on a prepare run, false otherwise.
   */
  public boolean isPrepare ()
  {
    return prepare;
  }
}
