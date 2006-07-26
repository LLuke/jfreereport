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
 * AlignmentCollector.java
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
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;

/**
 * The collector holds the total height of all elements and the baseline
 * positions of the last element. When a new element is added, it gets added
 * either unaligned (in respect to any baseline) or alignd to one of the
 * baselines.
 *
 * @author Thomas Morgner
 */
public class AlignmentCollector
{
  private long height;
  private long[] baselinePositions;
  //private long[] initialBaselinePositions;
  private int axis;
  private int dominantBaseline;

  /**
   *
   * @param minorAxis
   * @param lineheight
   */
  public AlignmentCollector(final int minorAxis,
                            final long lineheight)
  {
    this.height = lineheight;
    this.axis = minorAxis;
//    this.baselinePositions = new long[8];
  }

  public long getHeight()
  {
    return height;
  }

  public int getAxis()
  {
    return axis;
  }

  /**
   * Adding unaligned elements increases the total height and either shifts the
   * stored baselines upward or downwards.
   *
   * If a bottom aligned element is greater than the already seen content,
   * extending the total height will require a shift of the computed last
   * baseline by the added height.
   *
   * The returned position will only be valid, if the total lineheight will not
   * change while adding nodes.
   *
   * @param height
   * @param shiftDown
   * @return the position of the top edge of the node.
   */
  public long add (RenderNode node)
  {
    final long preferredSize = node.getEffectiveLayoutSize(axis);
    final ExtendedBaselineInfo baselineInfo = node.getBaselineInfo();

    if (baselinePositions == null)
    {
      this.height = Math.max (height, preferredSize);
      final long delta = height - preferredSize;

      if (baselineInfo == null)
      {
        // ok, no baseline info at all. Dont panic. We have recorded the
        // height and wait for the next element ...
        return delta;
      }

      this.dominantBaseline = baselineInfo.getDominantBaseline();
      this.baselinePositions = baselineInfo.getBaselines();
      if (delta > 0)
      {
        for (int i = 0; i < baselinePositions.length; i++)
        {
          baselinePositions[i] += delta;
        }
      }
      //this.initialBaselinePositions = (long[]) baselinePositions.clone();
      return delta;
    }

    // now here comes the tricky part
    final CSSValue verticalAlignment = node.getVerticalAlignment();
    if (baselineInfo == null ||
        VerticalAlign.TOP.equals(verticalAlignment) ||
        VerticalAlign.BOTTOM.equals(verticalAlignment) ||
        VerticalAlign.MIDDLE.equals(verticalAlignment))
    {
      return alignSimple(verticalAlignment, preferredSize);
    }
    else
    {
      // This is one of the baseline alignments. The alignment value tells us
      // which baseline will be used for the alignment.
      int localBase;
      int nodeBase;
      if (VerticalAlign.CENTRAL.equals(verticalAlignment))
      {
        nodeBase = ExtendedBaselineInfo.CENTRAL;
        localBase = ExtendedBaselineInfo.CENTRAL;
      }
      else if (VerticalAlign.TEXT_BOTTOM.equals(verticalAlignment))
      {
        nodeBase = ExtendedBaselineInfo.TEXT_AFTER_EDGE;
        localBase = ExtendedBaselineInfo.TEXT_AFTER_EDGE;
      }
      else if (VerticalAlign.TEXT_TOP.equals(verticalAlignment))
      {
        nodeBase = ExtendedBaselineInfo.TEXT_BEFORE_EDGE;
        localBase = ExtendedBaselineInfo.TEXT_BEFORE_EDGE;
      }
      else if (VerticalAlign.SUB.equals(verticalAlignment))
      {
        nodeBase = ExtendedBaselineInfo.MATHEMATICAL;
        localBase = dominantBaseline;
      }
      else if (VerticalAlign.SUPER.equals(verticalAlignment))
      {
        nodeBase = baselineInfo.getDominantBaseline();
        localBase = ExtendedBaselineInfo.MATHEMATICAL;
      }
      else if (VerticalAlign.BASELINE.equals(verticalAlignment))
      {
        nodeBase = ExtendedBaselineInfo.ALPHABETHC;
        localBase = ExtendedBaselineInfo.ALPHABETHC;
      }
      else
      {
        nodeBase = baselineInfo.getDominantBaseline();
        localBase = baselineInfo.getDominantBaseline();
      }

      final long[] baselines = baselineInfo.getBaselines();
      long myAscent = baselinePositions[localBase];
      long nodeAscent = baselines[nodeBase];

      if (nodeAscent > myAscent)
      {
        long delta = nodeAscent - myAscent;
        for (int i = 0; i < baselinePositions.length; i++)
        {
          baselinePositions[i] += delta;
        }
      }

      if (preferredSize > height)
      {
        // no need to correct the baselines, as the decent never affects them.
        height = preferredSize;
      }
      if (nodeAscent > myAscent)
      {
        return 0;
      }
      return myAscent - nodeAscent;
    }
  }

  private long alignSimple(final CSSValue verticalAlignment,
                           final long preferredSize)
  {
    if (VerticalAlign.TOP.equals(verticalAlignment))
    {
      // get the total height. Then shift everything downwards ..
      final long delta = preferredSize - height;
      if (delta > 0)
      {
        for (int i = 0; i < baselinePositions.length; i++)
        {
          baselinePositions[i] += delta;
        }
        this.height += delta;
      }
      return 0;
    }
    else if (VerticalAlign.BOTTOM.equals(verticalAlignment))
    {
      final long delta = preferredSize - height;
      if (delta > 0)
      {
        this.height += delta;
        return 0;
      }
      return -delta;
    }
    else if (VerticalAlign.MIDDLE.equals(verticalAlignment))
    {
      final long delta = preferredSize - height;

      if (delta > 0)
      {
        // The new element is greater than the old line ..
        final long deltaHalf = delta / 2;
        for (int i = 0; i < baselinePositions.length; i++)
        {
          baselinePositions[i] -= deltaHalf;
        }
        this.height += delta;
        return 0;
      }

      // the old line is greater ..
      return -delta/2;
    }
    else // align baseline of parent with bottom of child ..
    {
      // ok, we have no baseline info, but the user wants us to align on a
      // baseline. Thats a funny request, isnt it?
      //
      // Well, we can do this by using the last dominant baseline and by
      // assuming that the element's content will be all ascending.
      final long baselineDelta =
              preferredSize - baselinePositions[dominantBaseline];
      if (baselineDelta > 0)
      {
        // the preferred size is greater than the current baseline pos
        // we have to shift ...
        for (int i = 0; i < baselinePositions.length; i++)
        {
          baselinePositions[i] += baselineDelta;
//          initialBaselinePositions[i] -= baselineDelta;
        }
        height += baselineDelta;
        return 0;
      }

      return -baselineDelta;
    }
  }

}
