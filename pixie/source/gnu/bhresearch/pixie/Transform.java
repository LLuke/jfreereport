// Transform.java - A 2D linear transform.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie;

import java.awt.Rectangle;

/**
 A 2D linear transform. Only scaling and translation are currently
 supported - no rotation, and shearing isn't used.
 */
public class Transform
{
  private boolean isIdentity;
  private float scaleX, scaleY;
  private float offsetX, offsetY;

  /** Construct identity transform. */
  public Transform ()
  {
    isIdentity = true;
    offsetX = offsetY = 0;
    scaleX = scaleY = 1;
  }

  /** Construct a transform from offset and scale values. */
  public Transform (float ox, float oy, float sx, float sy)
  {
    offsetX = ox;
    offsetY = oy;
    scaleX = sx;
    scaleY = sy;
    isIdentity = (offsetX == 0) && (offsetY == 0) && (scaleX == 1) && (scaleY == 1);
  }

  /** Construct a transform which maps the src onto the dest rectangle. */
  public Transform (Rectangle dest, Rectangle src)
  {
    if (dest.equals (src))
    {
      isIdentity = true;
      offsetX = offsetY = 0;
      scaleX = scaleY = 1;
    }
    else
    {
      isIdentity = false;
      scaleX = dest.width / (float) src.width;
      scaleY = dest.height / (float) src.height;
      offsetX = dest.x - src.x * scaleX;
      offsetY = dest.y - src.y * scaleY;
    }
  }

  /** True if this is the identity transform. */
  public boolean isIdentity ()
  {
    return isIdentity;
  }

  public int applyToX (int x)
  {
    return Math.round (x * scaleX + offsetX);
  }

  public int applyToWidth (int w)
  {
    return Math.round (w * scaleX);
  }

  public int applyToY (int y)
  {
    return Math.round (y * scaleY + offsetY);
  }

  public int applyToHeight (int h)
  {
    return Math.round (h * scaleY);
  }

  /** Transform a rectangle. */
  public Rectangle applyTo (Rectangle in)
  {
    if (isIdentity)
      return in;

    int ax = applyToX (in.x);
    int ay = applyToY (in.y);
    int aw = applyToWidth (in.width);
    int ah = applyToHeight (in.height);

    return new Rectangle (ax, ay, aw, ah);
  }

  /** Transform a curve. */
  public Curve applyTo (Curve curve)
  {
    if (isIdentity)
      return curve;

    Curve out = new Curve (curve.getSize ());
    for (int i = 0; i < curve.getSize (); i++)
    {
      int x = Math.round (curve.getX (i) * scaleX + offsetX);
      int y = Math.round (curve.getY (i) * scaleY + offsetY);
      out.add (x, y, curve.isBezier (i));
    }
    return out;
  }
}
