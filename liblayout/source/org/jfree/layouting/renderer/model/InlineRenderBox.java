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
 * InlineRenderBox.java
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

import org.jfree.util.Log;

/**
 * An inline box is some floating text that might be broken down into lines. The
 * actual linebreaking is performed/initiated by the block context.
 * <p/>
 * Breaking behaviour: An InlineBox forwards all breaks to its childs and tries
 * to break them according to their rules. BlockContent and ImageContent is
 * considered not-very-breakable by default, breaks on that should be avoided.
 *
 * @author Thomas Morgner
 */
public class InlineRenderBox extends RenderBox
{

  public InlineRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);

    // hardcoded for now, content forms lines, which flow from top to bottom
    // and each line flows horizontally (later with support for LTR and RTL)
    setMajorAxis(VERTICAL_AXIS);
    setMinorAxis(HORIZONTAL_AXIS);
  }

  /**
   * Splits the render node at the given splitPoint. This method returns an
   * array with the length of two; if the node is not splittable, the first
   * element should be empty (in the element's behavioural context) and the
   * second element should contain an independent copy of the original node.
   * <p/>
   * If the break splitPoint is ambugious, the break should appear *in front of*
   * the splitPoint - where in front-of depends on the reading direction.
   *
   * @param axis       the axis on which to break
   * @param splitPoint the break splitPoint within that axis.
   * @param target     the target array that should receive the broken node. If
   *                   the target array is not null, it must have at least two
   *                   slots.
   * @return the broken nodes contained in the target array.
   */
  public RenderNode[] split(int axis, long splitPoint, RenderNode[] target)
  {
    if (splitPoint == 0)
    {
      throw new IllegalArgumentException();
    }

    Log.debug("ENTER SPLIT : " + this + " " + splitPoint);
    if (target == null || target.length < 2)
    {
      target = new RenderNode[2];
    }

    if (axis == getMajorAxis())
    {
      // not splitable. By using the invisible render box, we allow the content
      // to move into the next line ..
      Log.debug("InlineRenderBox is not spittable on this axis");
      target[0] = new InvisibleRenderBox();
      target[1] = derive(true);
      return target;
    }

    // first, find the split point.
    long[] paddings = getBordersAndPadding(axis);
    long size = paddings[0];
    RenderNode splitPointChild = getFirstChild();
    while (splitPointChild != null && size <= splitPoint)
    {
      final long prefSize = splitPointChild.getPreferredSize(axis);
//      Log.debug("Child: PrefSize: " + prefSize);
//      if (prefSize == 395000)
//      {
//        Log.debug("ERE");
//        child.getFirstBreak(axis);
//      }
      if (prefSize + size <= splitPoint)
      {
        size += prefSize;
        splitPointChild = splitPointChild.getNext();
      }
      else
      {
        break;
      }
    }

    // we will fit ...
    if (splitPointChild == null)
    {
      Log.warn("PERFORMANCE: Unnecessarily stupid split detected.");
      target[0] = derive(true);
      target[1] = null;
      return target;
    }

    BoxDefinition[] boxes = getBoxDefinition().splitVertically();

    InlineRenderBox firstBox = (InlineRenderBox) derive(false);
    firstBox.setBoxDefinition(boxes[0]);

    int firstSize = 0;
    RenderNode preChild = getFirstChild();
    while (preChild != null && splitPointChild != preChild)
    {
      // first, add everything up to the child .
      firstBox.addChild(preChild.derive(true));
      firstSize += 1;
      preChild = preChild.getNext();
    }

    // now we've reached the split point ..
    final long splitPos = splitPoint - firstBox.getPreferredSize(axis);
    if (splitPos < 0)
    {
      throw new IllegalStateException("The selected child split is not valid");
    }

    InlineRenderBox secondBox = (InlineRenderBox) derive(false);
    secondBox.setBoxDefinition(boxes[1]);
    int secondSize = 0;

    // the split pos is directly after the first boxes end.
    if (splitPos == 0)
    {
      Log.debug("Not splitting, deriving a second box. ");
      secondBox.addChild(splitPointChild.derive(true));
      secondSize += 1;
    }
    else
    {
      Log.debug("       Extra Info: PS: " + splitPointChild.getPreferredSize(axis));
      Log.debug("       Extra Info: FB: " + splitPointChild.getFirstBreak(axis));
      Log.debug("       Extra Info: BP: " + splitPointChild.getBestBreak(axis, splitPos));
      Log.debug("       Extra Info:*BP: " + getBestBreak(axis, splitPos));
      Log.debug("       Extra Info:*FP: " + getFirstBreak(axis));
      long bestBreakPos = splitPointChild.getBestBreak(axis, splitPos);
      if (bestBreakPos > 0)
      {
        target = splitPointChild.split(axis, splitPos, target);
        Log.debug("splitting, adding childs.");
        firstBox.addChild(target[0]);
        firstSize += 1;
        if (target[1] != null)
        {
          secondSize += 1;
          secondBox.addChild(target[1]);
        }
      }
      else
      {
        Log.debug("Not splitting, deriving a second box. " + bestBreakPos);

        secondBox.addChild(splitPointChild.derive(true));
        secondSize += 1;
      }
    }

    RenderNode postChild = splitPointChild.getNext();
    while (postChild != null)
    {
      // first, add everything up to the child .
      secondBox.addChild(postChild.derive(true));
      secondSize += 1;
      postChild = postChild.getNext();
    }

    Log.debug("Result: " + firstSize + " " + secondSize);
    if (firstSize == 0)
    {
      Log.debug("PERFORMANCE: This split is avoidable.");
      // correct the second box. The box is not split.
      secondBox.setBoxDefinition(getBoxDefinition());
      target[0] = new InvisibleRenderBox();
      target[1] = secondBox;
    }
    else if (secondSize == 0)
    {
      Log.debug("PERFORMANCE: Invalid split implementation. Second box should never be empty.");
      firstBox.setBoxDefinition(getBoxDefinition());
      target[0] = firstBox;
      target[1] = null;
    }
    else
    {
      target[0] = firstBox;
      target[1] = secondBox;
    }
    Log.debug("LEAVE SPLIT : " + this);

    // make sure that noone works with that anymore ..
    return target;
  }

  /**
   * Returns the nearest break-point that occurrs before that splitPosition. If
   * the splitPosition already is a break point, return that point. If there is
   * no break opportinity at all, return zero (= BREAK_NONE).
   * <p/>
   * (This causes the split to behave correctly; this moves all non-splittable
   * elements down to the next free area.)
   *
   * @param axis          the axis.
   * @param splitPosition the maximum splitPosition
   * @return the best break splitPosition.
   */
  public long getBestBreak(int axis, long splitPosition)
  {
    if (splitPosition <= 0)
    {
      throw new IllegalArgumentException();
    }

    Log.debug("InlineRenderer: Start BestBreak: " + splitPosition);
    if (axis == getMajorAxis())
    {
      // not splitable.
      Log.debug("No split on the major axis");
      return 0;
    }

    // this is simply the best we can get from our contents.

    long bestBreak = 0;
    long[] paddings = getBordersAndPadding(axis);
    long cursor = paddings[0];
    RenderNode child = getFirstChild();
    while (child != null)
    {
      final long currentSize = child.getPreferredSize(axis);
      Log.debug("InlineRenderer: Best Break: Preferred Size: " + currentSize + " " + child);

      final long posAfterGeneric = currentSize + cursor;
      final long posAfter;
      if (child.getNext() == null)
      {
        posAfter = posAfterGeneric + paddings[1];
      }
      else
      {
        posAfter = posAfterGeneric;
      }

      // shortcut .. test for a direct hit.
      final BreakAfterEnum breakAfterAllowed = child.getBreakAfterAllowed();
      if (posAfter == splitPosition)
      {
        if (breakAfterAllowed == BreakAfterEnum.BREAK_ALLOW)
        {
          return posAfter;
        }
      }

      // this would go over the limit .. try to find a suitable break point
      // inside the element.
      if (posAfter >= splitPosition)
      {
        final long effectiveSplitPos = splitPosition - cursor;
        final long bestBreakLocal = child.getBestBreak(axis, effectiveSplitPos);
        if (bestBreakLocal > 0)
        {
          Log.debug("InlineRenderer: Best Break(1): " + (cursor + bestBreakLocal));
          return cursor + bestBreakLocal;
        }
        else if (bestBreak > 0)
        {
          Log.debug("InlineRenderer: Best Break(3): " + (bestBreak));
          return bestBreak;
        }
      }

      if (breakAfterAllowed == BreakAfterEnum.BREAK_ALLOW)
      {
        Log.debug("InlineRenderer: Setting best Break: " + (posAfter));
        bestBreak = Math.max(bestBreak, posAfter);
      }

      if (posAfter >= splitPosition)
      {
        Log.debug("InlineRenderer: Best Break(2): " + (bestBreak));
        return bestBreak;
      }
      child = child.getNext();
      cursor += currentSize;
    }
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
    if (axis == getMajorAxis())
    {
      // not splitable.
      Log.debug("No split on the major axis");
      return 0;
    }

    // this is simply the best we can get from our contents.

    long[] paddings = getBordersAndPadding(axis);
    long cursor = paddings[0];
    RenderNode child = getFirstChild();
    while (child != null)
    {
      long currentSize = child.getPreferredSize(axis);
      final long posAfter = currentSize + cursor;
      final long bestBreakLocal = child.getFirstBreak(axis);
      if (bestBreakLocal > 0)
      {
        return bestBreakLocal + cursor;
      }

      final BreakAfterEnum breakAfterAllowed = child.getBreakAfterAllowed();
      if (breakAfterAllowed == BreakAfterEnum.BREAK_ALLOW)
      {
        if (child.getNext() == null)
        {
          // if this is the last child, include the paddings and borders ..
          return posAfter + paddings[1];
        }
        return posAfter;
      }
      else
      {
        Log.debug("BreakAfter: " + breakAfterAllowed);
      }
      cursor = posAfter;
      child = child.getNext();
    }
    return 0;
  }

  public long getMinimumChunkSize(int axis)
  {
    // todo: Adjacent chunks needs to be added or we run into trouble later.
    // but as said .. later :)

    // maybe we need something like getFirstChunk, getlastChunk or so.
    return super.getMinimumChunkSize(axis);
  }

  public long getMinimumSize(int axis)
  {
    long[] paddings = getBordersAndPadding(axis);

    if (axis == getMajorAxis())
    {
      // minor axis means: Drive through all childs and query their size
      // then find the maximum
      long size = paddings[0];
      RenderNode child = getFirstChild();
      while (child != null)
      {
        size = Math.max(size, child.getMinimumSize(axis));
        child = child.getNext();
      }
      return size + paddings[1];
    }

    long size = paddings[0];
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size += child.getMinimumSize(axis);
      child = child.getNext();
    }
    return size + paddings[1];
  }

  public long getPreferredSize(int axis)
  {
    long[] paddings = getBordersAndPadding(axis);

    if (axis == getMajorAxis())
    {
      // minor axis means: Drive through all childs and query their size
      // then find the maximum
      long size = 0;
      RenderNode child = getFirstChild();
      while (child != null)
      {
        size = Math.max(size, child.getPreferredSize(axis));
        child = child.getNext();
      }
      return size + paddings[1];
    }

    long size = paddings[0];
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size += child.getPreferredSize(axis);
      child = child.getNext();
    }
    return size + paddings[1];
  }

  public long getMaximumSize(int axis)
  {
    long[] paddings = getBordersAndPadding(axis);

    if (axis == getMajorAxis())
    {
      // minor axis means: Drive through all childs and query their size
      // then find the maximum
      long size = 0;
      RenderNode child = getFirstChild();
      while (child != null)
      {
        size = Math.max(size, child.getMaximumSize(axis));
        child = child.getNext();
      }
      return size + paddings[1];
    }

    long size = paddings[0];
    RenderNode child = getFirstChild();
    while (child != null)
    {
      size += child.getMaximumSize(axis);
      child = child.getNext();
    }
    return size + paddings[1];
  }

  public void validate()
  {
    final RenderNodeState state = getState();
    if (state == RenderNodeState.FINISHED)
    {
      return; // nothing to do
    }

    if (state == RenderNodeState.UNCLEAN)
    {
      // ok, recompute the margins, paddings and border-sizes and get me a
      // valid imageable-area. This computation is slightly different from
      // the one done in the Block-context.

      validateBorders();
      validateMargins();
      validatePaddings();

      // For now, I assume a MBP (margin-border-padding) size of 1, just
      // to get some visual appearance..
      setState(RenderNodeState.LAYOUTING);
    }

    final long width = getWidth() - getLeftInsets() - getRightInsets();
    long nodePos = getX() + getLeftInsets();
    // as defined by the stylesheet property with the same name.
    final long lineHeight = 0;
    long effectiveHeight = 0;
    RenderNode node = getFirstChild();
    while (node != null)
    {
      node.setX(nodePos);
      node.setY(getY() + getTopInsets());
      final long preferredWidth = node.getPreferredSize(HORIZONTAL_AXIS);
      node.setWidth(Math.min(Math.max(0, width - nodePos), preferredWidth));
      node.validate();

      node.setHeight(Math.max(node.getHeight(), lineHeight));
      effectiveHeight = Math.max(node.getHeight(), effectiveHeight);
      nodePos += node.getWidth();
      node = node.getNext();
    }

    setWidth((nodePos + getRightInsets()) - getX() - getLeftInsets());
    setHeight(effectiveHeight);
    setState(RenderNodeState.FINISHED);
  }


  /**
   * search all childs for splittable content. This is something static, we dive
   * through and perform splits based on the clear-left and clear-right flag.
   * <p/>
   * multiple clear-lefts can be merged into a single one. &lt;span
   * style="clear: left"&gt;&lt;span style="clear: left"&gt;&lt/span>&lt/span&gt;
   * still causes only one linebreak, as the second one has no effect, if the
   * linebox is already empty.
   * <p/>
   * One thing to note: We do not take any sizes into account here. Whether a
   * line eats one or one million points does not matter.
   *
   * @param isStartOfLine whether this box would be the first child in a new
   *                      linebox. All firstChilds of a first element are also
   *                      considered to be at the start of the line.
   * @return true, if we need to force a split, false otherwise.
   */
  public boolean isForcedSplitNeeded(boolean isEndOfLine)
  {
    // never check your own clear properties. We dont know the context of
    // these...
    RenderNode child = getFirstChild();
    while (child != null)
    {
      final boolean endOfLine = (child.getNext() == null) && isEndOfLine;
      if (child.isForcedSplitNeeded(endOfLine))
      {
        return true;
      }
      child = child.getNext();
    }
    return false;
  }

  /**
   * Performs a forced split. That split, unlike the other split-operation does
   * not take sizes into account. It looks at the 'clear' property and splits,
   * if either clear-left or clear-right are enabled.
   *
   * @param isStartOfLine
   * @param isEndOfLine
   * @param target
   * @return null, if no split is needed, else the splitted nodes in an array.
   */
  public RenderNode[] splitForLinebreak(final boolean isEndOfLine,
                                        RenderNode[] target)
  {
    // never check your own clear properties. We dont know the context of
    // these...
    RenderNode child = getFirstChild();
    while (child != null)
    {
      final boolean endOfLine = (child.getNext() == null) && isEndOfLine;
      if (child.isForcedSplitNeeded(endOfLine))
      {
        target = child.splitForLinebreak(endOfLine, target);
        InlineRenderBox splitted =
                split(null, getFirstChild(), child.getPrev());
        splitted.addChild(target[0]);
        target[0] = splitted;
        target[1] = split(target[1], child.getNext(), getLastChild());
        return target;
      }
      if (child.isForcedSplitRequested(endOfLine))
      {
        if (target == null || target.length < 2)
        {
          target = new RenderNode[2];
        }
        target[0] = split(null, getFirstChild(), child);
        target[1] = split(null, child.getNext(), getLastChild());
        return target;
      }
      child = child.getNext();
    }
    return null;
  }

  /**
   * @param first    is assumed to be already derived ..
   * @param boundary
   * @return
   */
  private InlineRenderBox split(RenderNode prefix,
                                RenderNode first,
                                RenderNode last)
  {
    InlineRenderBox rb = (InlineRenderBox) clone();
    if (prefix != null)
    {
      rb.addChild(prefix);
    }
    RenderNode node = first;
    while (node != null)
    {
      rb.addChild(node.derive(true));
      if (node == last)
      {
        break;
      }
      node = node.getNext();
    }
    return rb;
  }
}
