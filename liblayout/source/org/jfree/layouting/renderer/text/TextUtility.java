/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * TextUtility.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.text;

import org.jfree.fonts.registry.BaselineInfo;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.keys.line.DominantBaseline;
import org.jfree.layouting.input.style.keys.line.AlignmentBaseline;

/**
 * Creation-Date: 12.10.2006, 17:23:30
 *
 * @author Thomas Morgner
 */
public class TextUtility
{
  private TextUtility()
  {
  }


  public static int translateDominantBaseline(CSSValue baseline, int defaultValue)
  {
    if (baseline == null || CSSAutoValue.getInstance().equals(baseline))
    {
      return defaultValue;
    }

    if (DominantBaseline.ALPHABETIC.equals(baseline))
    {
      return ExtendedBaselineInfo.ALPHABETHIC;
    }
    if (DominantBaseline.CENTRAL.equals(baseline))
    {
      return ExtendedBaselineInfo.CENTRAL;
    }
    if (DominantBaseline.HANGING.equals(baseline))
    {
      return ExtendedBaselineInfo.HANGING;
    }
    if (DominantBaseline.IDEOGRAPHIC.equals(baseline))
    {
      return ExtendedBaselineInfo.IDEOGRAPHIC;
    }
    if (DominantBaseline.MATHEMATICAL.equals(baseline))
    {
      return ExtendedBaselineInfo.MATHEMATICAL;
    }
    if (DominantBaseline.MIDDLE.equals(baseline))
    {
      return ExtendedBaselineInfo.MIDDLE;
    }
    if (DominantBaseline.TEXT_AFTER_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.TEXT_AFTER_EDGE;
    }
    if (DominantBaseline.TEXT_BEFORE_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.TEXT_BEFORE_EDGE;
    }
    return defaultValue;
  }

  public static int translateAlignmentBaseline(CSSValue baseline, int defaultValue)
  {
    if (baseline == null || CSSAutoValue.getInstance().equals(baseline))
    {
      return defaultValue;
    }

    if (AlignmentBaseline.ALPHABETIC.equals(baseline))
    {
      return ExtendedBaselineInfo.ALPHABETHIC;
    }
    if (AlignmentBaseline.CENTRAL.equals(baseline))
    {
      return ExtendedBaselineInfo.CENTRAL;
    }
    if (AlignmentBaseline.HANGING.equals(baseline))
    {
      return ExtendedBaselineInfo.HANGING;
    }
    if (AlignmentBaseline.IDEOGRAPHIC.equals(baseline))
    {
      return ExtendedBaselineInfo.IDEOGRAPHIC;
    }
    if (AlignmentBaseline.MATHEMATICAL.equals(baseline))
    {
      return ExtendedBaselineInfo.MATHEMATICAL;
    }
    if (AlignmentBaseline.MIDDLE.equals(baseline))
    {
      return ExtendedBaselineInfo.MIDDLE;
    }
    if (AlignmentBaseline.TEXT_AFTER_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.TEXT_AFTER_EDGE;
    }
    if (AlignmentBaseline.TEXT_BEFORE_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.TEXT_BEFORE_EDGE;
    }
    if (AlignmentBaseline.AFTER_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.AFTER_EDGE;
    }
    if (AlignmentBaseline.BEFORE_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.BEFORE_EDGE;
    }
    return defaultValue;
  }

  public static int translateBaselines(int baseline)
  {
    switch (baseline)
    {
      case BaselineInfo.HANGING:
        return ExtendedBaselineInfo.HANGING;
      case BaselineInfo.ALPHABETIC:
        return ExtendedBaselineInfo.ALPHABETHIC;
      case BaselineInfo.CENTRAL:
        return ExtendedBaselineInfo.CENTRAL;
      case BaselineInfo.IDEOGRAPHIC:
        return ExtendedBaselineInfo.IDEOGRAPHIC;
      case BaselineInfo.MATHEMATICAL:
        return ExtendedBaselineInfo.MATHEMATICAL;
      case BaselineInfo.MIDDLE:
        return ExtendedBaselineInfo.MIDDLE;
    }

    throw new IllegalArgumentException("Invalid baseline");
  }

  public static ExtendedBaselineInfo createBaselineInfo
      (int codepoint, FontMetrics fontMetrics)
  {
    final BaselineInfo baselineInfo = fontMetrics.getBaselines(codepoint, null);
    final int dominantBaseline =
        TextUtility.translateBaselines(baselineInfo.getDominantBaseline());
    final DefaultExtendedBaselineInfo extBaselineInfo =
        new DefaultExtendedBaselineInfo(dominantBaseline);

    long[] baselines = new long[ExtendedBaselineInfo.BASELINE_COUNT];
    baselines[ExtendedBaselineInfo.ALPHABETHIC] =
        StrictGeomUtility.toInternalValue
            (baselineInfo.getBaseline(BaselineInfo.ALPHABETIC));
    baselines[ExtendedBaselineInfo.CENTRAL] =
        StrictGeomUtility.toInternalValue
            (baselineInfo.getBaseline(BaselineInfo.CENTRAL));
    baselines[ExtendedBaselineInfo.HANGING] =
        StrictGeomUtility.toInternalValue
            (baselineInfo.getBaseline(BaselineInfo.HANGING));
    baselines[ExtendedBaselineInfo.IDEOGRAPHIC] =
        StrictGeomUtility.toInternalValue
            (baselineInfo.getBaseline(BaselineInfo.IDEOGRAPHIC));
    baselines[ExtendedBaselineInfo.MATHEMATICAL] =
        StrictGeomUtility.toInternalValue
            (baselineInfo.getBaseline(BaselineInfo.MATHEMATICAL));
    baselines[ExtendedBaselineInfo.MIDDLE] =
        StrictGeomUtility.toInternalValue
            (baselineInfo.getBaseline(BaselineInfo.MIDDLE));
    baselines[ExtendedBaselineInfo.BEFORE_EDGE] = 0;
    baselines[ExtendedBaselineInfo.TEXT_BEFORE_EDGE] = 0;
    baselines[ExtendedBaselineInfo.TEXT_AFTER_EDGE] =
        StrictGeomUtility.toInternalValue
            (fontMetrics.getMaxHeight());
    baselines[ExtendedBaselineInfo.AFTER_EDGE] =
        baselines[ExtendedBaselineInfo.TEXT_AFTER_EDGE];
    extBaselineInfo.setBaselines(baselines);
    return extBaselineInfo;
  }
}
