package org.jfree.report.util.geom;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.jfree.ui.FloatDimension;

public class StrictGeomUtility
{
  public static final float CORRECTION_FACTOR = 1000f;

  private StrictGeomUtility ()
  {
  }

  public static StrictDimension createDimension (final double w, final double h)
  {
    return new StrictDimension((long) (w * CORRECTION_FACTOR),
            (long) (h * CORRECTION_FACTOR));
  }

  public static StrictPoint createPoint (final double x, final double y)
  {
    return new StrictPoint((long) (x * CORRECTION_FACTOR),
            (long) (y * CORRECTION_FACTOR));
  }

  public static StrictBounds createBounds (final double x, final double y,
                                           final double width, final double height)
  {
    return new StrictBounds((long) (x * CORRECTION_FACTOR),
            (long) (y * CORRECTION_FACTOR),
            (long) (width * CORRECTION_FACTOR),
            (long) (height * CORRECTION_FACTOR));
  }

  public static Dimension2D createAWTDimension
          (final long width, final long height)
  {
    return new FloatDimension
            (width / CORRECTION_FACTOR, height / CORRECTION_FACTOR);
  }

  public static Rectangle2D createAWTRectangle
          (final long x, final long y, final long width, final long height)
  {
    return new Rectangle2D.Double
            (x / CORRECTION_FACTOR, y / CORRECTION_FACTOR,
                    width / CORRECTION_FACTOR, height / CORRECTION_FACTOR);
  }

  public static long toInternalValue (final double value)
  {
    return (long) (value * CORRECTION_FACTOR);
  }

  public static double toExternalValue (final long value)
  {
    return (value / CORRECTION_FACTOR);
  }
}
