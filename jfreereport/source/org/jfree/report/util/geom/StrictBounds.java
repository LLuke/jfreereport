package org.jfree.report.util.geom;

import java.io.Serializable;

public class StrictBounds implements Serializable, Cloneable
{
  private long x;
  private long y;
  private long width;
  private long height;
  private boolean locked;

  public StrictBounds ()
  {
  }

  public StrictBounds (final long x, final long y,
                       final long width, final long height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public boolean isLocked ()
  {
    return locked;
  }

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
    setRect(x1, y1, x2 - x1, y2 - y1);
  }

  public StrictBounds createIntersection (final StrictBounds bounds)
  {
    final long x1 = Math.max(getX(), bounds.getX());
    final long y1 = Math.max(getY(), bounds.getY());
    final long x2 = Math.min(getX() + getWidth(), bounds.getX() + bounds.getWidth());
    final long y2 = Math.min(getY() + getHeight(), bounds.getY() + bounds.getHeight());

    return new StrictBounds(x1, y1, x2 - x1, y2 - y1);
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
}
