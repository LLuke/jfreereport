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
 * ---------------------------
 * SimplePageLayoutCursor.java
 * ---------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: SimplePageLayoutCursor.java,v 1.6 2003/02/02 23:43:52 taqua Exp $
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

package com.jrefinery.report.targets.pageable.pagelayout;

import java.io.Serializable;

/**
 * A utility class for keeping track of the current output position on a logical page.  Only the
 * vertical location is tracked, it begins at zero (the top of the page) and increases as the
 * cursor moves down the page.
 * 
 * @author David Gilbert
 */
public class SimplePageLayoutCursor implements Cloneable, Serializable
{
  /** The y-coordinate. */
  private float y;

  /** The bottom of the page. */
  private float pageBottom;

  /** The reserved space at the bottom of the page. */
  private float reserved;

  /** The space used by the pageheader */
  private float pageTop;

  /**
   * Constructs a new cursor.
   *
   * @param height  the logical page height.
   */
  public SimplePageLayoutCursor (float height)
  {
    y = (float) 0;
    pageBottom = height;
  }

  /**
   * Reserves the given space at the bottom of the page. This space is considered printed
   * and does not get filled by the various bands and elements. This functionality is
   * used to reserve space for the page footer.
   *
   * @param reserve  the space (in points) to reserve at the bottom of the page.
   */
  public void setReservedSpace (float reserve)
  {
    if (reserve < 0)
    {
      throw new IllegalArgumentException ("Cannot free reserved space");
    }
    else if (reserve > pageBottom)
    {
      throw new IllegalArgumentException("Cannot reserve more than the available space");
    }
    reserved = reserve;
  }

  /**
   * Returns the amount of space reserved at the bottom of the page (usually for the page footer).
   *
   * @return the reserved space (in Java2D units).
   */
  public float getReservedSpace ()
  {
    return reserved;
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
   * Returns true if there is space for a band with the specified height, and false otherwise.
   *
   * @param height The height of the proposed band.
   *
   * @return A flag indicating whether or not there is room to print the band.
   */
  public boolean isSpaceFor (float height)
  {
    return (y + height <= (pageBottom - reserved));
  }

  /**
   * @return the current y-position of this cursor.
   */
  public float getY ()
  {
    return y;
  }

  /**
   * @return the bottom border of the printable area.
   */
  public float getPageBottom ()
  {
    return pageBottom;
  }

  /**
   * Returns the y-coordinate for the start of the reserved space at the bottom of the page.
   *
   * @return the start of the space reserved for the page footer.
   */
  public float getPageBottomReserved ()
  {
    return (pageBottom - reserved);
  }

  /**
   * Clones the cursor.
   *
   * @return a clone of this cursor.
   *
   * @throws CloneNotSupportedException  n.a.
   */
  public Object clone () throws CloneNotSupportedException
  {
    return super.clone ();
  }

  /**
   * Returns a string representing the cursor (useful for debugging).
   *
   * @return the string.
   */
  public String toString()
  {
    StringBuffer b = new StringBuffer();
    b.append("SimplePageLayoutCursor={y=");
    b.append(y);
    b.append(", pagebottom=");
    b.append(pageBottom);
    b.append(", reserved=");
    b.append(reserved);
    b.append("}");

    return b.toString();
  }

  public float getPageTop()
  {
    return pageTop;
  }

  public void setPageTop(float pageTop)
  {
    this.pageTop = pageTop;
  }
}
