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
 * RenderableImage.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RenderableReplacedContent.java,v 1.2 2006/07/18 14:40:28 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.util.geom.StrictDimension;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.resourceloader.ResourceKey;

/**
 * This handles all kinds of renderable rectangular content, mostly images and
 * drawables. It is assumed, that the image can be split on any position,
 * although this is avoided as far as possible.
 *
 * @author Thomas Morgner
 */
public class RenderableReplacedContent extends RenderNode
{
  private Object rawObject;
  private StrictDimension contentSize;
  private CSSValue verticalAlign;
  private ResourceKey source;
  private RenderLength width;
  private RenderLength height;

  public RenderableReplacedContent(final Object rawObject,
                                   final ResourceKey source,
                                   final StrictDimension contentSize,
                                   final RenderLength width,
                                   final RenderLength height,
                                   final CSSValue verticalAlign)
  {
    this.height = height;
    this.width = width;
    this.rawObject = rawObject;
    this.source = source;
    this.contentSize = contentSize;
    this.verticalAlign = verticalAlign;
  }

  public Object getRawObject()
  {
    return rawObject;
  }

  public StrictDimension getContentSize()
  {
    return contentSize;
  }

  public ResourceKey getSource()
  {
    return source;
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
    // todo how do we handle this? Later ...
    return new RenderNode[2];
  }

  public long getMinimumChunkSize(int axis)
  {
    return 0;
  }

  public long getPreferredSize(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      if (RenderLength.AUTO.equals(width))
      {
        return contentSize.getWidth();
      }
      else
      {
        return width.resolve(contentSize.getWidth());
      }
    }

    if (RenderLength.AUTO.equals(height))
    {
      return contentSize.getHeight();
    }
    else
    {
      return height.resolve(contentSize.getHeight());
    }
  }

  public void validate()
  {
    // who cares about such events at all..
    setState(RenderNodeState.FINISHED);
  }

  public BreakAfterEnum getBreakAfterAllowed(final int axis)
  {
    return BreakAfterEnum.BREAK_DONT_KNOW;
  }

  public int getBreakability(int axis)
  {
    return HARD_BREAKABLE;
  }

  /**
   * The reference point corresponds to the baseline of an box. For now, we
   * define only one reference point per box. The reference point of boxes
   * corresponds to the reference point of the first linebox.
   *
   * @param axis
   * @return
   */
  public long getReferencePoint(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return 0;
    }
    return getPreferredSize(axis);
  }

  /**
   * Defines a spacing, that only applies if the node is not the first node in
   * the box. This spacing gets later mixed in with the absolute margins and
   * corresponds to the effective margin of the RenderBox class.
   *
   * @param axis
   * @return
   */
  public long getLeadingSpace(int axis)
  {
    return 0;
  }

  /**
   * Defines a spacing, that only applies, if the node is not the last node in
   * the box. This spacing gets later mixed in with the absolute margins and
   * corresponds to the effective margin of the RenderBox class.
   *
   * @param axis
   * @return
   */
  public long getTrailingSpace(int axis)
  {
    return 0;
  }

  public CSSValue getVerticalAlignment()
  {
    return verticalAlign;
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
