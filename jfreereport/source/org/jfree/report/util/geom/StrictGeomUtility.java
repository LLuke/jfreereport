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
 * StrictGeomUtility.java
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
