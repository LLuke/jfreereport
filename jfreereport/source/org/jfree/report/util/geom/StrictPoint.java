/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * StrictPoint.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util.geom;

import java.io.Serializable;

public class StrictPoint implements Serializable, Cloneable
{
  private long x;
  private long y;
  private boolean locked;

  public StrictPoint ()
  {
  }

  public StrictPoint (final long x, final long y)
  {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the X coordinate of this <code>Point2D</code> in <code>double</code>
   * precision.
   *
   * @return the X coordinate of this <code>Point2D</code>.
   *
   * @since 1.2
   */
  public long getX ()
  {
    return x;
  }

  /**
   * Returns the Y coordinate of this <code>Point2D</code> in <code>double</code>
   * precision.
   *
   * @return the Y coordinate of this <code>Point2D</code>.
   *
   * @since 1.2
   */
  public long getY ()
  {
    return y;
  }

  /**
   * Sets the location of this <code>Point2D</code> to the specified <code>double</code>
   * coordinates.
   *
   * @param x the coordinates of this <code>Point2D</code>
   * @param y the coordinates of this <code>Point2D</code>
   * @since 1.2
   */
  public void setLocation (final long x, final long y)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }

    this.x = x;
    this.y = y;
  }

  public boolean isLocked ()
  {
    return locked;
  }

  public void setLocked (boolean locked)
  {
    this.locked = locked;
  }

  public Object clone ()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new InternalError("Clone must always be supported.");
    }
  }


  public String toString ()
  {
    return "org.jfree.report.util.geom.StrictPoint{" +
            "x=" + x +
            ", y=" + y +
            "}";
  }
}
