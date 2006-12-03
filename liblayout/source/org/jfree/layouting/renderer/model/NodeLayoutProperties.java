/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
package org.jfree.layouting.renderer.model;

import java.io.Serializable;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.border.RenderLength;

/**
 * A static properties collection. That one is static; once computed it does
 * not change anymore. It does not (under no thinkable circumstances) depend
 * on the given content. It may depend on static content of the parent.
 *
 * @author Thomas Morgner
 */
public class NodeLayoutProperties implements Serializable, Cloneable
{
  // This represents the computed with.
  private RenderLength blockContextWidth;

  // Either AUTO or a valid width.
  private RenderLength computedWidth;

  private long maximumBoxWidth;
  private long minimumChunkWidth;
  private boolean icmFinished;

  private CSSValue alignmentBaseline;
  private CSSValue alignmentAdjust;
  private CSSValue baselineShift;
  private CSSValue verticalAlignment;
  private RenderLength baselineShiftResolved;
  private RenderLength alignmentAdjustResolved;

  private String namespace;
  private String tagName;

  /**
   * Whether that element will need intrinsic sizing.
   */
  private boolean intrinsic;
  private boolean finished;

  public NodeLayoutProperties()
  {
    computedWidth = RenderLength.AUTO;
  }

  public RenderLength getBlockContextWidth()
  {
    return blockContextWidth;
  }

  public void setBlockContextWidth(final RenderLength blockContextWidth)
  {
    if (blockContextWidth.isPercentage())
    {
      throw new IllegalArgumentException
              ("Percentages are not allowed at this stage.");
    }
    if (blockContextWidth == RenderLength.AUTO)
    {
      this.blockContextWidth = RenderLength.AUTO;
    }
    else if (blockContextWidth.getValue() <= 0)
    {
      this.blockContextWidth = RenderLength.EMPTY;
    }
    else
    {
      this.blockContextWidth = blockContextWidth;
    }
  }

  public boolean isIntrinsic()
  {
    return intrinsic;
  }

  public void setIntrinsic(final boolean intrinsic)
  {
    this.intrinsic = intrinsic;
  }

  public RenderLength getComputedWidth()
  {
    return computedWidth;
  }

  public void setComputedWidth(final RenderLength computedWidth)
  {
    if (computedWidth.isPercentage())
    {
      throw new IllegalArgumentException
              ("Percentages are not allowed at this stage.");
    }
    if (computedWidth == RenderLength.AUTO)
    {
      this.computedWidth = RenderLength.AUTO;
    }
    else if (computedWidth.getValue() <= 0)
    {
      this.computedWidth = RenderLength.EMPTY;
    }
    else
    {
      this.computedWidth = computedWidth;
    }
  }

  public boolean isFinished()
  {
    return finished;
  }

  public void setFinished(final boolean finished)
  {
    this.finished = finished;
  }


  public String toString()
  {
    return "NodeLayoutProperties{" +
            "finished=" + finished +
            ", blockContextWidth=" + blockContextWidth +
            ", computedWidth=" + computedWidth +
            ", maximumBoxWidth=" + maximumBoxWidth +
            ", minimumChunkWidth=" + minimumChunkWidth +
            ", icmFinished=" + icmFinished +
            ", intrinsic=" + intrinsic +
            '}';
  }

  public long getMaximumBoxWidth()
  {
    return maximumBoxWidth;
  }

  public void setMaximumBoxWidth(final long maximumBoxWidth)
  {
    this.maximumBoxWidth = maximumBoxWidth;
  }

  public long getMinimumChunkWidth()
  {
    return minimumChunkWidth;
  }

  public void setMinimumChunkWidth(final long minimumChunkWidth)
  {
    this.minimumChunkWidth = minimumChunkWidth;
  }

  public boolean isIcmFinished()
  {
    return icmFinished;
  }

  public void setIcmFinished(final boolean icmFinished)
  {
    this.icmFinished = icmFinished;
  }

  public Object clone () throws CloneNotSupportedException
  {
    return super.clone();
  }

  public CSSValue getAlignmentBaseline()
  {
    return alignmentBaseline;
  }

  public void setAlignmentBaseline(final CSSValue alignmentBaseline)
  {
    this.alignmentBaseline = alignmentBaseline;
  }

  public CSSValue getAlignmentAdjust()
  {
    return alignmentAdjust;
  }

  public void setAlignmentAdjust(final CSSValue alignmentAdjust)
  {
    this.alignmentAdjust = alignmentAdjust;
  }

  public CSSValue getBaselineShift()
  {
    return baselineShift;
  }

  public void setBaselineShift(final CSSValue baselineShift)
  {
    this.baselineShift = baselineShift;
  }

  public CSSValue getVerticalAlignment()
  {
    return verticalAlignment;
  }

  public void setVerticalAlignment(final CSSValue verticalAlignment)
  {
    this.verticalAlignment = verticalAlignment;
  }

  public RenderLength getBaselineShiftResolved()
  {
    return baselineShiftResolved;
  }

  public void setBaselineShiftResolved(final RenderLength baselineShiftResolved)
  {
    this.baselineShiftResolved = baselineShiftResolved;
  }

  public RenderLength getAlignmentAdjustResolved()
  {
    return alignmentAdjustResolved;
  }

  public void setAlignmentAdjustResolved(final RenderLength alignmentAdjustResolved)
  {
    this.alignmentAdjustResolved = alignmentAdjustResolved;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public void setNamespace(final String namespace)
  {
    this.namespace = namespace;
  }

  public String getTagName()
  {
    return tagName;
  }

  public void setTagName(final String tagName)
  {
    this.tagName = tagName;
  }
}
