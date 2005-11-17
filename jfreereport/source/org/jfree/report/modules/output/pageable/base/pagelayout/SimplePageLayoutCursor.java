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
 * ---------------------------
 * SimplePageLayoutCursor.java
 * ---------------------------
 * (C)opyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: SimplePageLayoutCursor.java,v 1.8 2005/02/23 21:05:29 taqua Exp $
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
 * 02-Dec-2002 : Old layouting implementation got transformed into SimplePageLayout
 * 05-Dec-2002 : Updates to Javadocs (DG);
 * 07-Dec-2002 : More documentation
 * 07-Jan-2003 : Layouting Fix III: Limited max band height to the available space of a single page
 */

package org.jfree.report.modules.output.pageable.base.pagelayout;

import java.io.Serializable;

/**
 * A utility class for keeping track of the current output position on a logical page.
 * Only the vertical location is tracked, it begins at zero (the top of the page) and
 * increases as the cursor moves down the page.
 *
 * @author David Gilbert
 */
public class SimplePageLayoutCursor implements Cloneable, Serializable
{
  /**
   * The y-coordinate.
   */
  private long y;

  /**
   * The bottom of the page.
   */
  private final long pageBottom;

  /**
   * The reserved space at the bottom of the page.
   */
  private long reserved;

  /**
   * The space used by the pageheader.
   */
  private long pageTop;

  /**
   * Constructs a new cursor.
   *
   * @param height the logical page height.
   */
  public SimplePageLayoutCursor (final long height)
  {
    y = 0;
    pageBottom = height;
  }

  /**
   * Reserves the given space at the bottom of the page. This space is considered printed
   * and does not get filled by the various bands and elements. This functionality is used
   * to reserve space for the page footer.
   *
   * @param reserve the space (in points) to reserve at the bottom of the page.
   */
  public void setReservedSpace (final long reserve)
  {
    if (reserve < 0)
    {
      throw new IllegalArgumentException("Cannot free reserved space");
    }
    else if (reserve > pageBottom)
    {
      throw new IllegalArgumentException("Cannot reserve more than the available space");
    }
    reserved = reserve;
  }

  /**
   * Returns the amount of space reserved at the bottom of the page (usually for the page
   * footer).
   *
   * @return the reserved space (in Java2D units).
   */
  public long getReservedSpace ()
  {
    return reserved;
  }

  /**
   * Adds the specified amount to the y-coordinate.
   *
   * @param amount The amount that the cursor should advance down the page.
   */
  public void advance (final long amount)
  {
    if (amount < 0)
    {
      throw new IllegalArgumentException("Cannot advance negative");
    }
    y += amount;
  }

  /**
   * Moves the cursor to the given y-coordinate. All space beween the current position
   * before the move and the new position is considered filled and won't get filled by the
   * generator.
   *
   * @param amount The amount that the cursor should advance down the page.
   */
  public void advanceTo (final long amount)
  {
    if (amount < y)
    {
      throw new IllegalArgumentException("Cannot advance negative");
    }
    y = amount;
  }

  /**
   * Returns true if there is space for a band with the specified height, and false
   * otherwise.
   *
   * @param height The height of the proposed band.
   * @return A flag indicating whether or not there is room to print the band.
   */
  public boolean isSpaceFor (final long height)
  {
    return (y + height <= (pageBottom - reserved));
  }

  /**
   * Gets the current y-position for printing the next band.
   *
   * @return the current y-position of this cursor.
   */
  public long getY ()
  {
    return y;
  }

  /**
   * Returns the bottom border position of the printable page area.
   *
   * @return the bottom border of the printable area.
   */
  public long getPageBottom ()
  {
    return pageBottom;
  }

  /**
   * Returns the y-coordinate for the start of the reserved space at the bottom of the
   * page.
   *
   * @return the start of the space reserved for the page footer.
   */
  public long getPageBottomReserved ()
  {
    return (pageBottom - reserved);
  }

  /**
   * Clones the cursor.
   *
   * @return a clone of this cursor.
   *
   * @throws CloneNotSupportedException n.a.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Returns a string representing the cursor (useful for debugging).
   *
   * @return the string.
   */
  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("SimplePageLayoutCursor={y=");
    b.append(y);
    b.append(", pagebottom=");
    b.append(pageBottom);
    b.append(", reserved=");
    b.append(reserved);
    b.append("}");

    return b.toString();
  }

  /**
   * Returns the space reserved by the page header.
   *
   * @return the reserved page header space.
   */
  public long getPageTop ()
  {
    return pageTop;
  }

  /**
   * Defines the space reserved by the page header.
   *
   * @param pageTop the reserved page header space.
   */
  public void setPageTop (final long pageTop)
  {
    this.pageTop = pageTop;
  }
}
