// Curve.java - a mixture of straight line segments and cubic beziers.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie;

import gnu.bhresearch.quant.Assert;

import java.awt.Point;
import java.awt.Rectangle;

/**
 A curve is a mixture of straight line segments and cubic beziers.
 */
public final class Curve
{
  // Approximate an ellipse by 4 bezier curves.
  private final static double bezHandle = (Math.sqrt (2.0) - 1) * 4.0 / 3.0;

  private Point p[] = null;
  private boolean isBezier[] = null;
  private int size = 0;
  private Rectangle bBox = null;

  public void assertValid ()
  {
    if (!Assert.isEnabled)
      return;

    Assert.assert (p != null && isBezier != null);
    Assert.assert (p.length == isBezier.length);
    Assert.assert (size >= 0);
    Assert.assert (size <= p.length);
    Assert.assert (size == 0 || !isBezier (0));
    if (Assert.getLevel () > 1)
    {
      Assert.assert (bBox == null || bBox.equals (calcBBox ()));
      Assert.assert (getBezierCount () % 3 == 0);
    }
  }

  /** Construct an empty curve. */
  public Curve ()
  {
    this (10);
  }

  /** Construct an empty curve with the given capacity. */
  public Curve (int capacity)
  {
    if (capacity < 1)
      setCapacity (1);
    else
      setCapacity (capacity);
  }

  /** True if the curve has no points. */
  public final boolean isEmpty ()
  {
    return size == 0;
  }

  /** The number of points in the curve. */
  public final int getSize ()
  {
    return size;
  }

  /** The current capacity. */
  public final int getCapacity ()
  {
    return p.length;
  }

  /** Return the i'th point. */
  public final Point get (int i)
  {
    return p[i];
  }

  /* Parameterized get. */
  public int getX (int i)
  {
    return p[i].x;
  }

  /* Parameterized get. */
  public int getY (int i)
  {
    return p[i].y;
  }

  public int[] getAllX ()
  {
    int[] retval = new int[size];
    for (int i = 0; i < size; i++)
    {
      retval[i] = p[i].x;
    }
    return retval;
  }

  public int[] getAllY ()
  {
    System.out.println ("PointSize: " + p.length);
    int[] retval = new int[size];
    for (int i = 0; i < size; i++)
    {
      retval[i] = p[i].y;
    }
    return retval;
  }

  /** True if the i'th point is a bezier */
  public final boolean isBezier (int i)
  {
    return isBezier[i];
  }

  /**
   * Return the number of bezier points. 0 if the curve is made of
   * straight lines only.
   */
  public final int getBezierCount ()
  {
    int cnt = 0;
    for (int i = 0; i < size; i++)
    {
      if (isBezier[i])
      {
        cnt++;
      }
    }
    Assert.assert ((cnt % 3) == 0);
    return cnt;
  }

  /**
   * Change the size of the curve. New points are as if created with
   * add( 0, 0, false )
   */
  public void setSize (int newSize)
  {
    if (p == null || p.length < newSize)
      setCapacity (newSize);
    if (size < newSize)
      for (int i = size; i < newSize; i++)
      {
        p[i].x = p[i].y = 0;
        isBezier[i] = false;
      }
    size = newSize;
    bBox = null;
  }

  /**
   * Adjust capacity to minimise memory used.
   */
  public final void trim ()
  {
    setCapacity (size);
  }

  /**
   * Pre-allocate memory.
   */
  public void ensureSpareCapacity (int spare)
  {
    if (getCapacity () < size + spare)
    {
      setCapacity (size + spare);
    }
  }

  /**
   * Pre-allocate memory.
   */
  public void setCapacity (int newCapacity)
  {
    Assert.assert (newCapacity >= size);

    int oldCapacity = 0;
    if (p != null)
      oldCapacity = p.length;

    if (newCapacity == oldCapacity)
      return;

    int common = Math.min (newCapacity, oldCapacity);
    Point oldP[] = p;
    p = new Point[newCapacity];
    boolean oldIsBezier[] = isBezier;
    isBezier = new boolean[newCapacity];

    if (common > 0)
    {
      System.arraycopy (oldIsBezier, 0, isBezier, 0, common);
      System.arraycopy (oldP, 0, p, 0, common);
    }
    for (int i = common; i < newCapacity; i++)
    {
      p[i] = new Point (0, 0);
    }
  }

  /** True if last and first points are equal. */
  public boolean isClosed ()
  {
    return size > 1 && p[size - 1].x == p[0].x && p[size - 1].y == p[0].y;
  }

  /** Append a point if necessary to close the curve. */
  public void addClose ()
  {
    if (size > 1 && !isClosed ())
      add (p[0].x, p[0].y, false);
  }

  /** Append a single relative point */
  public void addRel (int x, int y)
  {
    add (p[size - 1].x + x, p[size - 1].y + y, false);
  }

  /** Append a single absolute point. */
  public final void add (Point p, boolean isBezier)
  {
    add (p.x, p.y, isBezier);
  }

  /** Append a single absolute point. */
  public final void add (Point p)
  {
    add (p.x, p.y, false);
  }

  /** Append a single absolute point. */
  public void add (int x, int y)
  {
    add (x, y, false);
  }

  /** Append a single absolute point. */
  public void add (int x, int y, boolean isBezier)
  {
    if (size <= p.length)
    {
      setCapacity (size * 2 + 10);
    }
    p[size].x = x;
    p[size].y = y;
    this.isBezier[size] = isBezier;
    size++;
    bBox = null;
  }

  /** Append another curve to this one. */
  public void add (Curve src)
  {
    add (src, 0, src.getSize ());
  }

  /** Append another curve to this one. */
  public void add (Curve src, int start, int cnt)
  {
    ensureSpareCapacity (cnt);

    System.arraycopy (src.isBezier, start, isBezier, size, cnt);
    System.arraycopy (src.p, start, p, size, cnt);

    size += cnt;

    /**
     for (int i = start; i < start+cnt; i++)
     {
     p[size].x = src.p[i].x;
     p[size].y = src.p[i].y;
     isBezier[size] = src.isBezier[i];
     size++;
     }
     */
    bBox = null;
  }

  /** Append a rectangle (ie 5 points). */
  public void addRectangle (Rectangle rect)
  {
    addRectangle (rect.x, rect.y, rect.width, rect.height);
  }

  /** Append a rectangle (ie 5 points). */
  public void addRectangle (int x, int y, int width, int height)
  {
    ensureSpareCapacity (5);
    add (x, y, false);
    add (x + width, y, false);
    add (x + width, y + height, false);
    add (x, y + height, false);
    add (x, y, false);
  }

  /** Append a regular polygon. */
  public void addPolygon (int sides, Rectangle rect, double startAngle)
  {
    addPolygon (sides, rect.x, rect.y, rect.width, rect.height, startAngle);
  }

  /** Append a regular polygon. */
  public void addPolygon (int sides, int left, int top, int width, int height,
                          double startAngle)
  {
    ensureSpareCapacity (sides + 1);
    double rx = width / 2.0;
    double ry = height / 2.0;
    double cx = left + rx;
    double cy = top + ry;
    for (int s = 0; s <= sides; s++)
    {
      double angle = s * (2.0 * Math.PI / sides) + startAngle;
      int x = (int) Math.round (cx + rx * Math.sin (angle));
      int y = (int) Math.round (cy - ry * Math.cos (angle));
      add (x, y, false);
    }
  }

  /** Append an ellipse, approximated by 4 bezier curves. */
  public void addEllipse (Rectangle box)
  {
    addEllipse (box.x, box.y, box.width, box.height);
  }

  /** Append an ellipse, approximated by 4 bezier curves. */
  public void addEllipse (int x, int y, int width, int height)
  {
    ensureSpareCapacity (13);
    int rx = width / 2;
    int ry = height / 2;
    int cx = x + rx;
    int cy = y + ry;
    int px = (int) Math.round (rx * bezHandle);
    int py = (int) Math.round (ry * bezHandle);

    add (x, cy, false);
    add (x, cy - py, true);
    add (cx - px, y, true);
    add (cx, y, true);

    add (cx + px, y, true);
    add (x + width, cy - py, true);
    add (x + width, cy, true);

    add (x + width, cy + py, true);
    add (cx + px, y + height, true);
    add (cx, y + height, true);

    add (cx - px, y + height, true);
    add (x, cy + py, true);
    add (x, cy, true);
  }

  /** Remove colinear (ie redundant) points. */
  public void removeColinear ()
  {
    int wp = 1;
    for (int rp = 1; rp < size; rp++)
    {
      if (!isBezier[rp])
      { // Always keep beziers.
        // Compare with previous point.
        int dx0 = p[rp].x - p[wp - 1].x;
        int dy0 = p[rp].y - p[wp - 1].y;
        if (dx0 == 0 && dy0 == 0)
          continue;  // The same.
        if (rp < size - 1)
        {
          // Compare with next point.
          int dx1 = p[rp + 1].x - p[rp].x;
          int dy1 = p[rp + 1].y - p[rp].y;
          if (dx0 * dy1 == dx1 * dy0)
            continue;  // Straight line.
        }
      }
      // Keep this point.
      p[wp].x = p[rp].x;
      p[wp].y = p[rp].y;
      isBezier[wp] = isBezier[rp];
      wp++;
    }
    //Debug.println( "Colinear saved " + (size-wp) );
    size = wp;
    bBox = null;
  }

  /** Return bounding box. */
  public Rectangle getBBox ()
  {
    if (bBox == null)
      bBox = calcBBox ();
    // Always return a copy in case the caller changes it.
    return (bBox == null) ?
            new Rectangle (0, 0, 0, 0) :
            new Rectangle (bBox.x, bBox.y, bBox.width, bBox.height);
  }

  /* Force bounding box cache to be refreshed. */
  public void flushBBox ()
  {
    bBox = null;
  }

  /** Recalculate bounding box. */
  private Rectangle calcBBox ()
  {
    if (isEmpty ())
      return null;
    int minX = p[0].x;
    int maxX = minX;
    int minY = p[0].y;
    int maxY = minY;
    for (int i = 1; i < size; i++)
    {
      if (minX > p[i].x)
        minX = p[i].x;
      if (maxX < p[i].x)
        maxX = p[i].x;
      if (minY > p[i].y)
        minY = p[i].y;
      if (maxY < p[i].y)
        maxY = p[i].y;
    }
    return new Rectangle (minX, minY, maxX - minX, maxY - minY);
  }

  /** True if the curve approximates the given box. */
  public boolean equals (Rectangle box)
  {
    if (size < 5)
    {
      if (size < 3 && getBBox ().equals (box))
      {
        if (box.width == 0 || box.height == 0)
        {
          return true;
        }
        else
        {
          return false;
        }
      }
    }

    int prevDx = 0;
    int prevDy = 0;

    for (int i = 0; i < size; i++)
    {
      if (isBezier[i])
        return false;
      if (p[i].x != box.x && p[i].x != box.x + box.width)
        return false;
      if (p[i].y != box.y && p[i].y != box.y + box.height)
        return false;

      int prevI = 0;
      if (i > 0)
      {
        prevI = i - 1;
      }
      else
      {
        prevI = size - 1;
      }

      int dx = p[i].x - p[prevI].x;
      int dy = p[i].y - p[prevI].y;

      if (dx != 0 && dy != 0)
        return false; // Line at an angle.

      if (i > 0)
      {
        // Line must be at 90 degrees to last line.
        if (dx == 0 && prevDx == 0)
          return false;
        if (dy == 0 && prevDy == 0)
          return false;
      }
      prevDx = dx;
      prevDy = dy;
    }
    return true;
  }

  public void clear ()
  {
    size = 0;
  }
}
