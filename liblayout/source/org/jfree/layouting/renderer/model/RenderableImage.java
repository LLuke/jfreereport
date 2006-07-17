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
 * $Id: RenderableImage.java,v 1.2 2006/07/14 14:34:41 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.util.geom.StrictDimension;
import org.jfree.resourceloader.ResourceKey;

/**
 * This handles all kinds of renderable rectangular content, mostly images and
 * drawables. It is assumed, that the image can be split on any position,
 * although this is avoided as far as possible.
 *
 * @author Thomas Morgner
 */
public class RenderableImage extends RenderNode
{
  private Object rawObject;
  private StrictDimension contentSize;
  private ResourceKey source;

  public RenderableImage(final Object rawObject,
                         final ResourceKey source, final StrictDimension contentSize)
  {
    this.rawObject = rawObject;
    this.source = source;
    this.contentSize = contentSize;
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

//  public long getMinimumSize(int axis)
//  {
//    if (axis == HORIZONTAL_AXIS)
//    {
//      return contentSize.getWidth();
//    }
//    return contentSize.getHeight();
//  }

  public long getPreferredSize(int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return contentSize.getWidth();
    }
    return contentSize.getHeight();
  }

//  public long getMaximumSize(int axis)
//  {
//    if (axis == HORIZONTAL_AXIS)
//    {
//      return contentSize.getWidth();
//    }
//    return contentSize.getHeight();
//  }
//

  public void validate()
  {
    // who cares about such events at all..
    setState(RenderNodeState.FINISHED);
  }

  public BreakAfterEnum getBreakAfterAllowed()
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


}
