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
 * TableWriterCursor.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TableWriterCursor.java,v 1.5 2005/01/25 00:12:48 taqua Exp $
 *
 * Changes
 * -------
 * 14-Jan-2003 : Initial version
 * 17-Feb-2003 : Documentation
 *
 */
package org.jfree.report.modules.output.table.base;

/**
 * A utility class for keeping track of the current output position on a table sheet.  Only the
 * vertical location is tracked, it begins at zero (the top of the page) and increases as the
 * cursor moves down the page.
 *
 * @author Thomas Morgner
 */
public strictfp class TableWriterCursor
{
  /** The y-coordinate. */
  private long y;

  /**
   * Default Constructor.
   */
  public TableWriterCursor()
  {
  }

  /**
   * Adds the specified amount to the y-coordinate.
   * @param amount The amount that the cursor should advance down the page.
   */
  public void advance(final long amount)
  {
    if (amount < 0)
    {
      throw new IllegalArgumentException("Cannot advance negative");
    }
    y += amount;
  }

  /**
   * Moves the cursor to the given y-coordinate. All space beween the current position
   * before the move and the new position is considered filled and won't get filled by
   * the generator.
   *
   * @param amount The amount that the cursor should advance down the page.
   */
  public void advanceTo(final long amount)
  {
    if (amount < y)
    {
      throw new IllegalArgumentException("Cannot advance negative");
    }
    y = amount;
  }

  /**
   * Gets the current vertical (y) position of the cursor.
   *
   * @return the current y-position of this cursor.
   */
  public long getY()
  {
    return y;
  }

}
