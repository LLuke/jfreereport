/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -----------
 * Cursor.java
 * -----------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: Cursor.java,v 1.10 2002/08/08 15:28:37 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 10-May-2002 : Declared all fields private and created accessor functions. Functionaltiy is
 *               encapsulated in functions.
 * 20-May-2002 : Added advanceTo to better support PageFooter drawing.
 * 04-Jun-2002 : Documentation updated.
 * 04-Jul-2002 : Serializable and Cloneable
 * 05-Sep-2002 : Documentation
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;

import java.io.Serializable;

/**
 * A utility class for keeping track of the current position on a report page. The cursor
 * is used by the ReportProcessor to determine the current position on the paper.
 * The cursor is only able to advance forward. There is no way of reseting the cursor once
 * it has moved.
 */
public class Cursor implements Cloneable, Serializable
{

  /** The y-coordinate. */
  private float y;

  /** The left edge of the page. */
  private float pageLeft;

  /** The bottom of the page. */
  private float pageBottom;

  /**
   * Constructs a new cursor.
   */
  public Cursor (OutputTarget target)
  {
    pageLeft = target.getUsableX ();
    y = target.getUsableY ();
    pageBottom = y + target.getUsableHeight ();
  }

  /**
   * Reserves the given space on the bottom of the page. This space is considered printed
   * and does not get filled by the various bands and elements. This functionality is
   * used to reserve space for the page footer.
   */
  public void reserveSpace (float reserve)
  {
    if (reserve < 0)
      throw new IllegalArgumentException ("Cannot free reserved space");

    pageBottom -= reserve;
  }

  /**
   * Adds the specified amount to the y-coordinate.
   * @param amount The amount that the cursor should advance down the page.
   */
  public void advance (float amount)
  {
    if (amount < 0)
      throw new IllegalArgumentException ("Cannot advance negative");
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
      throw new IllegalArgumentException ("Cannot advance negative");
    y = amount;
  }

  /**
   * Returns true if there is space for a band with the specified height, and false otherwise.
   * @param height The height of the proposed band.
   * @return A flag indicating whether or not there is room to print the band.
   */
  public boolean isSpaceFor (float height)
  {
    return (y + height < pageBottom);
  }

  /**
   * @return the current y-position of this cursor.
   */
  public float getY ()
  {
    return y;
  }

  /**
   * @return the left border of the printable area.
   */
  public float getPageLeft ()
  {
    return pageLeft;
  }

  /**
   * @return the bottom border of the printable area.
   */
  public float getPageBottom ()
  {
    return pageBottom;
  }

  /**
   * @returns a clone of this cursor.
   */
  public Object clone () throws CloneNotSupportedException
  {
    return super.clone ();
  }
}
