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
 * BlockRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BlockRenderBox.java,v 1.1 2006/07/11 13:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

/**
 * A block box behaves according to the 'display:block' layouting rules.
 * In the simplest case, all childs will consume the complete width and get
 * stacked below each other.
 *
 * Live would be boring if everything was simple: Floats and absolutly
 * positioned elements enrich and complicate the layout schema. Absolute
 * elements do not affect the normal flow, but inject an invisible placeholder
 * to keep track of their assumed normal-flow position.
 *
 * (Using Mozilla's behaviour as reference, I assume that absolutly positioned
 * elements are controled from outside the normal-flow (ie. parallel to the
 * normal flow, but positioned relative to their computed placeholder position
 * inside the normal flow. Weird? yes!)
 *
 * Float-elements are positioned inside the block context and reduce the size
 * of the linebox. Float-elements are positioned once when they are defined and
 * will not alter any previously defined content.
 *
 * As floats from previous boxes may overlap with later boxes, all floats must
 * be placed on a global storage. The only difference between absolutely
 * positioned and floating elements is *where* they can be placed, and whether
 * they influence the content under them. (Floats do, abs-pos dont).
 *
 * @author Thomas Morgner
 */
public class BlockRenderBox extends RenderBox
{
  protected static class ValidationStruct
  {
    private RenderNode node;
    private long progress;
    private long cursor;

    public ValidationStruct()
    {
    }

    public RenderNode getNode()
    {
      return node;
    }

    public void setNode(final RenderNode node)
    {
      this.node = node;
    }

    public long getProgress()
    {
      return progress;
    }

    public void setProgress(final long progress)
    {
      this.progress = progress;
    }

    public long getCursor()
    {
      return cursor;
    }

    public void setCursor(final long nodePosition)
    {
      this.cursor = nodePosition;
    }

    public void addCursorPosition(final long nodePosition)
    {
      this.cursor += nodePosition;
    }

    public boolean isFinished()
    {
      return node == null;
    }
  }

  public BlockRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);

    // hardcoded for now, content forms lines, which flow from top to bottom
    // and each line flows horizontally (later with support for LTR and RTL)
    setMajorAxis(VERTICAL_AXIS);
    setMinorAxis(HORIZONTAL_AXIS);
  }

  public long getMinimumSize(int axis)
  {
    long[] paddings = getBordersAndPadding(axis);

    if (axis == getMajorAxis())
    {
      // major axis means: Drive through all childs and query their size
      // then sum them up
      long size = paddings[0];
      RenderNode child = getFirstChild();
      while (child != null)
      {
        size += child.getMinimumSize(axis);
        child = child.getNext();
      }
      return size + paddings[1];
    }

    // minor axis means: Drive through all childs and query their size
    // then find the maximum
    long size = paddings[0];
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size = Math.max (size, child.getMinimumSize(axis));
      child = child.getNext();
    }
    return size + paddings[1];
  }

  public long getPreferredSize(int axis)
  {
    long[] paddings = getBordersAndPadding(axis);
    if (axis == getMajorAxis())
    {
      // major axis means: Drive through all childs and query their size
      // then sum them up
      long size = paddings[0];
      RenderNode child = getFirstChild();
      while (child != null)
      {
        size += child.getPreferredSize(axis);
        child = child.getNext();
      }
      return size + paddings[1];
    }

    // minor axis means: Drive through all childs and query their size
    // then find the maximum
    long size = paddings[0];
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size = Math.max (size, child.getPreferredSize(axis));
      child = child.getNext();
    }
    return size + paddings[1];
  }

  public long getMaximumSize(int axis)
  {
    long[] paddings = getBordersAndPadding(axis);

    if (axis == getMajorAxis())
    {
      // major axis means: Drive through all childs and query their size
      // then sum them up
      long size = paddings[0];
      RenderNode child = getFirstChild();
      while (child != null)
      {
        size += child.getMaximumSize(axis);
        child = child.getNext();
      }
      return size + paddings[1];
    }

    // minor axis means: Drive through all childs and query their size
    // then find the maximum
    long size = paddings[0];
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size = Math.max (size, child.getMaximumSize(axis));
      child = child.getNext();
    }
    return size + paddings[1];
  }

  public void validate()
  {
    final RenderNodeState state = getState();
    if (state == RenderNodeState.FINISHED)
    {
      return;
    }
    if (state == RenderNodeState.UNCLEAN)
    {
      // ok, recompute the margins, paddings and border-sizes and get me a
      // valid imageable-area.

      // For now, I assume a MBP (margin-border-padding) size of 10, just
      // to get some visual appearance..
      setState(RenderNodeState.PENDING);
    }
    if (state == RenderNodeState.PENDING)
    {
      validateBorders();
      validateMargins();
      validatePaddings();
      setState(RenderNodeState.LAYOUTING);
    }

    final long leftMbp = getLeftInsets();
    final long rightMbp = getRightInsets();

    final long nodeWidth = getWidth() - leftMbp - rightMbp;

    ValidationStruct struct = new ValidationStruct();
    struct.setProgress(Long.MAX_VALUE);
    struct.setNode(getFirstChild());
    struct.setCursor(getY() + getTopInsets());
    RenderNode[] target = null;

    while (struct.isFinished() == false)
    {
      RenderNode node = struct.getNode();
      node.setX(getX() + leftMbp);
      node.setY(struct.getCursor());
      // first validate brings the element into a valid state.
      // And then we break it apart...
      if (node instanceof InlineRenderBox)
      {
        struct = validateInlineChild(nodeWidth, target, struct);
      }
      else
      {
        // block boxes are considered simple for now. Just validate them
        node.setWidth(nodeWidth);
        node.validate();

        struct.addCursorPosition(node.getHeight());
        struct.setNode(node.getNext());
        struct.setProgress(Long.MAX_VALUE);
      }
    }

    setHeight(struct.getCursor() + getBottomInsets() - getY());
  }

  private ValidationStruct validateInlineChild (final long nodeWidth,
                                                RenderNode[] target,
                                                ValidationStruct struct)
  {
    final RenderNode node = struct.getNode();
    final long progress = struct.getProgress();

    final long desiredWidth = computeWidth(nodeWidth, node);
    if (desiredWidth <= nodeWidth)
    {
      node.setWidth(desiredWidth);
      node.validate();
      struct.addCursorPosition(node.getHeight());
      struct.setNode(node.getNext());
      return struct;
    }

    final long prefSize = node.getPreferredSize(getMinorAxis());

    // not enough space; we have to break that beast ...
    target = node.split(getMinorAxis(), nodeWidth, target);
    replaceChilds(node, target);

    final long firstNodeWidth = computeWidth(nodeWidth, target[0]);
    target[0].setX(getX() + getLeftInsets());
    target[0].setY(struct.getCursor());
    target[0].setWidth(firstNodeWidth);
    target[0].validate();
    struct.addCursorPosition(target[0].getHeight());

    if (target[1] != null)
    {
      if (prefSize < progress)
      {
        // made at least some progress ...
        struct.setProgress(prefSize);
        struct.setNode(target[1]);
        return struct;
      }

      // oh, no progress at all? Break out.
      target[1].setX(getX() + getLeftInsets());
      target[1].setY(struct.getCursor());
      target[1].setWidth(firstNodeWidth);
      target[1].validate();
      struct.addCursorPosition(target[1].getHeight());
      struct.setNode(target[1].getNext());
      // throw new IllegalStateException("NO progress is not valid");
      return struct;
    }

    struct.setNode(target[0].getNext());
    struct.setProgress(Long.MAX_VALUE);
    return struct;
  }

  /**
   * Returns the nearest break-point that occurrs before that position. If the
   * position already is a break point, return that point. If there is no break
   * opportinity at all, return zero (= BREAK_NONE).
   * <p/>
   * (This causes the split to behave correctly; this moves all non-splittable
   * elements down to the next free area.)
   *
   * @param axis     the axis.
   * @param position the maximum position
   * @return the best break position.
   */
  public long getBestBreak(int axis, long position)
  {
    // todo
    return 0;
  }

  /**
   * Returns the first break-point in that element. If there is no break
   * opportinity at all, return zero (= BREAK_NONE).
   * <p/>
   *
   * @param axis the axis.
   * @return the first break position.
   */
  public long getFirstBreak(int axis)
  {
    // todo
    return 0;
  }

  protected long computeWidth (long parentWidth, RenderNode node)
  {
    final long minWidth = node.getMinimumSize(getMinorAxis());
    final long prefWidth = node.getPreferredSize(getMinorAxis());
    final long maxWidth = node.getMaximumSize(getMinorAxis());
    final long desiredWidth = Math.max (minWidth, Math.min (prefWidth, maxWidth));
    //return Math.min (parentWidth, desiredWidth);
    return desiredWidth;
  }

  public boolean isEmpty()
  {
    return getFirstChild() == null;
  }

  protected long getComputedBlockContextWidth()
  {
    return getBoxDefinition().getPreferredWidth().resolve(0);
  }

}
