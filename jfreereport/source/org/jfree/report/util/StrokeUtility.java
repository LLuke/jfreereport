/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * StrokeUtility.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.01.2006 : Initial version
 */
package org.jfree.report.util;

import java.awt.Stroke;
import java.awt.BasicStroke;
import java.util.Arrays;

/**
 * Creation-Date: 27.01.2006, 18:50:32
 *
 * @author Thomas Morgner
 */
public class StrokeUtility
{
  public static final int STROKE_SOLID = 0;
  public static final int STROKE_DASHED = 1;
  public static final int STROKE_DOTTED = 2;
  public static final int STROKE_DOT_DASH = 3;
  public static final int STROKE_DOT_DOT_DASH = 4;

  private StrokeUtility()
  {
  }

  public static Stroke createStroke (int type, float width)
  {
    switch(type)
    {
      case STROKE_DASHED:
        return new BasicStroke(width, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER,
                10.0f, new float[]{6, 6}, 0.0f);
      case STROKE_DOTTED:
        return new BasicStroke(width, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER,
                5.0f, new float[]{0.1f, 2}, 0.0f);
      case STROKE_DOT_DASH:
        return new BasicStroke(width, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER,
                10.0f, new float[]{2, 2, 6, 2}, 0.0f);
      case STROKE_DOT_DOT_DASH:
        return new BasicStroke(width, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER,
                10.0f, new float[]{2, 2, 2, 2, 6, 2}, 0.0f);
      default:
        return new BasicStroke(width);
    }
  }

  public static float getStrokeWidth (Stroke s)
  {
    if (s instanceof BasicStroke)
    {
      BasicStroke bs = (BasicStroke) s;
      return bs.getLineWidth();
    }
    return 1;
  }

  public static int getStrokeType (Stroke s)
  {
    if (s instanceof BasicStroke == false)
    {
      return STROKE_SOLID;
    }
    final BasicStroke bs = (BasicStroke) s;
    float[] dashes = bs.getDashArray();
    if (dashes == null)
    {
      return STROKE_SOLID;
    }
    if (dashes.length < 2)
    {
      return STROKE_SOLID;
    }
    if (dashes.length == 3 || dashes.length == 5)
    {
      return STROKE_SOLID;
    }

    if (dashes.length == 2)
    {
      // maybe dashed or dotted ...
      if (dashes[0] < 2 && dashes[1] < 2) {
        return STROKE_DOTTED;
      }
      float factor = dashes[0] / dashes[1];
      if (factor > 0.9 && factor < 1.1)
      {
        return STROKE_DASHED;
      }
      // else ... not recognized ...
      return STROKE_SOLID;
    }
    if (dashes.length == 4)
    {
      // maybe a dot-dashed stroke ...
      float[] copyDashes = (float[]) dashes.clone();
      Arrays.sort(copyDashes);
      // test that the first three values have the same size
      final float factor1 = copyDashes[0] / copyDashes[1];
      final float factor2 = copyDashes[0] / copyDashes[2];
      final float factorBig = copyDashes[0] / copyDashes[3];

      if ((factor1 < 0.9 || factor1 > 1.1) ||
          (factor2 < 0.9 || factor2 > 1.1))
      {
        return STROKE_SOLID;
      }

      if ((factorBig < 0.9 || factorBig > 1.1))
      {
        if (copyDashes[0] < 2)
        {
          return STROKE_DOTTED;
        }
        return STROKE_DASHED;
      }
      if (factorBig > 2.5)
      {
        return STROKE_DOT_DASH;
      }
    }
    if (dashes.length == 6)
    {
      // maybe a dot-dashed stroke ...
      float[] copyDashes = (float[]) dashes.clone();
      Arrays.sort(copyDashes);
      // test that the first three values have the same size
      final float factor1 = copyDashes[0] / copyDashes[1];
      final float factor2 = copyDashes[0] / copyDashes[2];
      final float factor3 = copyDashes[0] / copyDashes[3];
      final float factor4 = copyDashes[0] / copyDashes[4];
      final float factorBig = copyDashes[0] / copyDashes[5];

      if ((factor1 < 0.9 || factor1 > 1.1) ||
          (factor2 < 0.9 || factor2 > 1.1) ||
          (factor3 < 0.9 || factor3 > 1.1) ||
          (factor4 < 0.9 || factor4 > 1.1))
      {
        return STROKE_SOLID;
      }

      if ((factorBig < 0.9 || factorBig > 1.1))
      {
        if (copyDashes[0] < 2)
        {
          return STROKE_DOTTED;
        }
        return STROKE_DASHED;
      }
      if (factorBig > 2.5)
      {
        return STROKE_DOT_DASH;
      }
    }
    // not recognized ...
    return STROKE_SOLID;
  }
}
