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
 * StrictBounds.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StrictBounds.java,v 1.5 2005/03/24 22:24:57 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util.geom;

import java.io.Serializable;

/**
 * The StrictBounds class is a replacement for the Rectangle2D classes.
 * This class uses integer mathematics instead of floating point values
 * to achive a higher degree of stability.
 *
 * @author Thomas Morgner
 */
public class StrictBounds implements Serializable, Cloneable
{
  /** The x-coordinate of the upper left corner. */
  private long x;
  /** The y-coordinate of the upper left corner. */
  private long y;
  /** The width of this rectangle. */
  private long width;
  /** The height of this rectangle. */
  private long height;
  /** A flag indicating whether attempts to change this rectangle should trigger Exceptions. */
  private boolean locked;

  /**
   * DefaultConstructor.
   */
  public StrictBounds ()
  {
  }

  /**
   * Creates a StrictBounds object with the given coordinates, width
   * and height.
   *
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  public StrictBounds (final long x, final long y,
                       final long width, final long height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Checks, whether this bounds object is locked.
   *
   * @return true, if the bounds are locked and therefore immutable, false otherwise.
   */
  public boolean isLocked ()
  {
    return locked;
  }

  /**
   * Defines, whether this bounds object should be locked and therefore immutable.
   *
   * @param locked true, if the bounds should be locked, false otherwise.
   */
  public void setLocked (final boolean locked)
  {
    this.locked = locked;
  }

  /**
   * Sets the location and size of this <code>Rectangle2D</code> to the specified double
   * values.
   *
   * @param x the coordinates to which to set the location of the upper left corner of
   *          this <code>Rectangle2D</code>
   * @param y the coordinates to which to set the location of the upper left corner of
   *          this <code>Rectangle2D</code>
   * @param w the value to use to set the width of this <code>Rectangle2D</code>
   * @param h the value to use to set the height of this <code>Rectangle2D</code>
   */
  public void setRect (final long x, final long y,
                       final long w, final long h)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
  }

  /**
   * Returns the height of the framing rectangle in <code>double</code> precision.
   *
   * @return the height of the framing rectangle.
   */
  public long getHeight ()
  {
    return height;
  }

  /**
   * Returns the width of the framing rectangle in <code>double</code> precision.
   *
   * @return the width of the framing rectangle.
   */
  public long getWidth ()
  {
    return width;
  }

  /**
   * Returns the X coordinate of the upper left corner of the framing rectangle in
   * <code>double</code> precision.
   *
   * @return the x coordinate of the upper left corner of the framing rectangle.
   */
  public long getX ()
  {
    return x;
  }

  /**
   * Returns the Y coordinate of the upper left corner of the framing rectangle in
   * <code>double</code> precision.
   *
   * @return the y coordinate of the upper left corner of the framing rectangle.
   */
  public long getY ()
  {
    return y;
  }

  /**
   * Determines whether the <code>RectangularShape</code> is empty. When the
   * <code>RectangularShape</code> is empty, it encloses no area.
   *
   * @return <code>true</code> if the <code>RectangularShape</code> is empty;
   *         <code>false</code> otherwise.
   */
  public boolean isEmpty ()
  {
    return width == 0 || height == 0;
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

  /**
   * Checks, whether this rectangle contains the given point.
   *
   * @param x the x-coordinate of the point.
   * @param y the y-coordinate of the point.
   * @return true, if the point is inside or directly on the border of this
   * rectangle, false otherwise.
   */
  public boolean contains (final long x, final long y)
  {
    if (x < this.x)
    {
      return false;
    }
    if (y < this.y)
    {
      return false;
    }
    if (x > (this.x + this.width))
    {
      return false;
    }
    if (y > (this.y + this.height))
    {
      return false;
    }
    return true;
  }


  /**
   * Checks, whether the given rectangle1 fully contains rectangle 2 (even if rectangle 2
   * has a height or width of zero!).
   *
   * @param rect1 the first rectangle.
   * @param rect2 the second rectangle.
   * @return A boolean.
   */
  public static boolean intersects (final StrictBounds rect1,
                                    final StrictBounds rect2)
  {

    final double x0 = rect1.getX();
    final double y0 = rect1.getY();

    final double x = rect2.getX();
    final double width = rect2.getWidth();
    final double y = rect2.getY();
    final double height = rect2.getHeight();
    return (x + width >= x0 &&
            y + height >= y0 &&
            x <= x0 + rect1.getWidth() &&
            y <= y0 + rect1.getHeight());
  }

  public void add (final StrictBounds bounds)
  {
    final long x1 = Math.min(getX(), bounds.getX());
    final long x2 = Math.max(getX() + getWidth(), bounds.getX() + bounds.getWidth());
    final long y1 = Math.min(getY(), bounds.getY());
    final long y2 = Math.max(getY() + getHeight(), bounds.getY() + bounds.getHeight());
    setRect(x1, y1, Math.max (0, x2 - x1), Math.max (0, y2 - y1));
  }

  public StrictBounds createIntersection (final StrictBounds bounds)
  {
    final long x1 = Math.max(getX(), bounds.getX());
    final long y1 = Math.max(getY(), bounds.getY());
    final long x2 = Math.min(getX() + getWidth(), bounds.getX() + bounds.getWidth());
    final long y2 = Math.min(getY() + getHeight(), bounds.getY() + bounds.getHeight());

    return new StrictBounds(x1, y1, Math.max(0, x2 - x1), Math.max(0, y2 - y1));
  }


  /**
   * Checks, whether the given rectangle1 fully contains rectangle 2 (even if rectangle 2
   * has a height or width of zero!).
   *
   * @param rect1 the first rectangle.
   * @param rect2 the second rectangle.
   * @return A boolean.
   */
  public static boolean contains (final StrictBounds rect1,
                                  final StrictBounds rect2)
  {

    final long x0 = rect1.getX();
    final long y0 = rect1.getY();
    final long x = rect2.getX();
    final long y = rect2.getY();
    final long w = rect2.getWidth();
    final long h = rect2.getHeight();

    return ((x >= x0) && (y >= y0) &&
            ((x + w) <= (x0 + rect1.getWidth())) &&
            ((y + h) <= (y0 + rect1.getHeight())));

  }

  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof StrictBounds))
    {
      return false;
    }

    final StrictBounds strictBounds = (StrictBounds) o;

    if (height != strictBounds.height)
    {
      return false;
    }
    if (width != strictBounds.width)
    {
      return false;
    }
    if (x != strictBounds.x)
    {
      return false;
    }
    if (y != strictBounds.y)
    {
      return false;
    }

    return true;
  }

  public int hashCode ()
  {
    int result;
    result = (int) (x ^ (x >>> 32));
    result = 29 * result + (int) (y ^ (y >>> 32));
    result = 29 * result + (int) (width ^ (width >>> 32));
    result = 29 * result + (int) (height ^ (height >>> 32));
    return result;
  }


  public String toString ()
  {
    return new StringBuffer().append("org.jfree.report.util.geom.StrictBounds{").append("x=")
            .append(x)
            .append(", y=")
            .append(y)
            .append(", width=")
            .append(width)
            .append(", height=")
            .append(height)
            .append("}")
            .toString();
  }

  public StrictBounds createUnion (final StrictBounds bg)
  {
    final long x = Math.min(getX(), bg.getX());
    final long y = Math.min(getY(), bg.getY());
    final long w = Math.max(getX() + getWidth(), bg.getX() + bg.getWidth()) - x;
    final long h = Math.max(getY() + getHeight(), bg.getY() + bg.getHeight()) - y;
    return new StrictBounds(x, y, w, h);
  }
}
