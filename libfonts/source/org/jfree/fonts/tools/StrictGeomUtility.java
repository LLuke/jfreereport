/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.tools;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.jfree.ui.FloatDimension;

/**
 * This class is the heart of the alternative geometrics toolkit. It performs
 * the neccessary conversions from and to the AWT classes to the Strict-classes.
 *
 * @author Thomas Morgner
 */
public class StrictGeomUtility
{
  /**
   * This is the correction factor used to convert points into 'Micro-Points'.
   */
  private static final double CORRECTION_FACTOR = 1000.0d;

  /**
   * Hidden, non usable constructor.
   */
  private StrictGeomUtility ()
  {
  }

  /**
   * Converts the given AWT value into a strict value.
   *
   * @param value the AWT point value.
   * @return the internal micro point value.
   */
  public static long toInternalValue (final double value)
  {
    return (long) (value * StrictGeomUtility.CORRECTION_FACTOR);
  }

  /**
   * Converts the given micro point value into an AWT value.
   *
   * @param value the micro point point value.
   * @return the AWT point value.
   */
  public static double toExternalValue (final long value)
  {
    return (value / StrictGeomUtility.CORRECTION_FACTOR);
  }

  public static long multiply (final long x, final long y)
  {
    return (long) ((x * y) / StrictGeomUtility.CORRECTION_FACTOR);
  }
}
