/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * TableWriterCursor.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableWriterCursor.java,v 1.2 2003/02/17 22:01:11 taqua Exp $
 *
 * Changes
 * -------
 * 14-Jan-2003 : Initial version
 * 17-Feb-2003 : Documentation
 * 
 */
package com.jrefinery.report.targets.table;

/**
 * A utility class for keeping track of the current output position on a table sheet.  Only the
 * vertical location is tracked, it begins at zero (the top of the page) and increases as the
 * cursor moves down the page.
 * 
 * @author Thomas Morgner
 */
public class TableWriterCursor
{
  /** The y-coordinate. */
  private float y;

  /**
   * Default Constructor
   */
  public TableWriterCursor()
  {
  }

  /**
   * Adds the specified amount to the y-coordinate.
   * @param amount The amount that the cursor should advance down the page.
   */
  public void advance (float amount)
  {
    if (amount < 0)
    {
      throw new IllegalArgumentException ("Cannot advance negative");
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
  public void advanceTo (float amount)
  {
    if (amount < y)
    {
      throw new IllegalArgumentException ("Cannot advance negative");
    }
    y = amount;
  }

  /**
   * @return the current y-position of this cursor.
   */
  public float getY ()
  {
    return y;
  }

}
