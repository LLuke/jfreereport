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
