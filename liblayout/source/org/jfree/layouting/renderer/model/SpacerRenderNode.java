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
 * SpacerRenderNode.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SpacerRenderNode.java,v 1.5 2006/07/26 12:09:51 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.util.geom.StrictInsets;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;

/**
 * A spacer reserves space for whitespaces found in the text. When encountered
 * at the beginning or end of lines, it gets removed.
 * <p/>
 * Spacers are always considered discardable, so when encountered alone, they
 * will get pruned.
 *
 * @author Thomas Morgner
 */
public class SpacerRenderNode extends RenderNode
{
  private long width;
  private long height;
  private boolean preserve;

  public SpacerRenderNode()
  {
    this(0,0,false);
  }

  public SpacerRenderNode(final long width,
                          final long height,
                          final boolean preserve)
  {
    this.width = width;
    this.height = height;
    this.preserve = preserve;
  }

  /**
   * Splits the render node at the given position. This method returns an array
   * with the length of two; if the node is not splittable, the first element
   * should be empty (in the element's behavioural context) and the second
   * element should contain an independent copy of the original node.
   * <p/>
   * If the break position is ambugious, the break should appear *in front of*
   * the position - where in front-of depends on the reading direction.
   *
   * @param axis     the axis on which to break
   * @param position the break position within that axis.
   * @param target   the target array that should receive the broken node. If
   *                 the target array is not null, it must have at least two
   *                 slots.
   * @return the broken nodes contained in the target array.
   */
  public RenderNode[] split(int axis, long position, RenderNode[] target)
  {
    if (target == null || target.length < 2)
    {
      target = new RenderNode[2];
    }
    target[0] = derive(true);
    target[1] = null;
    return target;
  }

  public long getMinimumChunkSize(int axis)
  {
    return 0;
  }

  /**
   * The preferred size returned here is always a box-size - that is the size
   * from one border-edge to the opposite border edge (excluding the margins).
   *
   * @param axis
   * @return
   */
  public long getPreferredSize(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return width;
    }
    return height;
  }

  public void validate()
  {
    validateMargins();
    setState(RenderNodeState.FINISHED);
  }

  protected void validateMargins()
  {
    if (isMarginsValidated())
    {
      return;
    }

    StrictInsets margins = getAbsoluteMarginsInternal();
    margins.setTop(height);
    margins.setBottom(height);
    margins.setLeft(width);
    margins.setRight(width);

    StrictInsets effectiveMargins = getEffectiveMarginsInternal();
    effectiveMargins.setTop(height);
    effectiveMargins.setBottom(height);
    effectiveMargins.setLeft(width);
    effectiveMargins.setRight(width);

    setMarginsValidated(true);
  }

  public BreakAfterEnum getBreakAfterAllowed(final int axis)
  {
    return BreakAfterEnum.BREAK_DONT_KNOW;
  }

  public boolean isEmpty()
  {
    return width == 0 && height == 0;
  }

  protected long getDefinedTrailingMargin(int axis)
  {
    return getPreferredSize(axis);
  }

  protected long getDefinedLeadingMargin(int axis)
  {
    return getPreferredSize(axis);
  }

  public boolean isDiscardable()
  {
    return false;//preserve == false;
  }

  /**
   * If that method returns true, the element will not be used for rendering.
   * For the purpose of computing sizes or performing the layouting (in the
   * validate() step), this element will treated as if it is not there.
   * <p/>
   * If the element reports itself as non-empty, however, it will affect the
   * margin computation.
   *
   * @return
   */
  public boolean isIgnorableForRendering()
  {
    return true;
  }

  public CSSValue getVerticalAlignment()
  {
    return VerticalAlign.BASELINE;
  }

  /**
   * Returns the baseline info for the given node. This can be null, if the node
   * does not have any baseline info.
   *
   * @return
   */
  public ExtendedBaselineInfo getBaselineInfo()
  {
    return null;
  }
}
